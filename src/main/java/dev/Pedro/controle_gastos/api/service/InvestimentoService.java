package dev.Pedro.controle_gastos.api.service;

import ch.obermuhlner.math.big.BigDecimalMath;
import dev.Pedro.controle_gastos.api.dto.InvestimentoRequest;
import dev.Pedro.controle_gastos.api.dto.InvestimentoResponse;
import dev.Pedro.controle_gastos.domain.entity.Investimento;
import dev.Pedro.controle_gastos.domain.entity.Registro;
import dev.Pedro.controle_gastos.domain.repository.InvestimentoRepository;
import dev.Pedro.controle_gastos.domain.repository.RegistroRepository;
import dev.Pedro.controle_gastos.enums.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
@Transactional

public class InvestimentoService {



    private final InvestimentoRepository repository;
    private final RegistroRepository registroRepository;

    public InvestimentoService(InvestimentoRepository investimentoRepository, RegistroRepository registroRepository) {

        this.repository = investimentoRepository;
        this.registroRepository = registroRepository;
    }

    public InvestimentoResponse create(InvestimentoRequest investimentoRequest) {

        validaCampoObg(investimentoRequest);
        validaValor(investimentoRequest);
        Investimento investimento = toEntity(investimentoRequest);

        return toResponse(repository.save(investimento));

    }


    public InvestimentoResponse update(Long id, InvestimentoRequest investimentoRequest) {

        validaCampoObg(investimentoRequest);
        validaValor(investimentoRequest);

        Investimento investimentoExistente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Investimento não encontrado"));


        investimentoExistente.setValorAplicado(investimentoRequest.valorAplicado());

        investimentoExistente.setDescricao(investimentoRequest.descricao());

        if (investimentoRequest.data() != null) {

            investimentoExistente.setData(investimentoRequest.data());
        }

        investimentoExistente.setCategoria(investimentoRequest.categoria());

        investimentoExistente.setTaxaJuros(investimentoRequest.taxaJuros());

        investimentoExistente.setPeriodicidadeTaxa(investimentoRequest.periodicidadeTaxa());

        return toResponse(repository.save(investimentoExistente));
    }


    public void delete(Long id) {

        if (!repository.existsById(id)) {

            throw new RuntimeException("Investimento não encontrada. id=" + id);
        }


        repository.deleteById(id);

    }

    public InvestimentoResponse findById(Long id) {

        Investimento investimento = repository.findById(id).
                orElseThrow(() -> new RuntimeException("Investimento não encontrado"));


        return toResponse(investimento);
    }

    public List<InvestimentoResponse> findByCategoria(CategoriaInvestimento categoria) {

        return listResponse(repository.findByCategoria(categoria));

    }

    public List<InvestimentoResponse> findByPeriodo(LocalDate inicio, LocalDate fim) {

        return listResponse(repository.findByDataBetween(inicio, fim));
    }

    public List<InvestimentoResponse> findAll() {

        return listResponse(repository.findAll());
    }

    public List<InvestimentoResponse> findByTipo(TipoInvestimento tipo) {

        return listResponse(repository.findByTipo(tipo));

    }


    public Registro sacar(Long id, BigDecimal valor) {

        Investimento investimentoExistente = repository.findById(id).
                orElseThrow(() -> new RuntimeException("Investimento não encontrado"));

        BigDecimal disponivel = valorDisponivelSaque(investimentoExistente);

        if (disponivel.compareTo(valor) < 0 || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Valor inválido");
        }

        investimentoExistente.setValorAplicado(disponivel.subtract(valor));   // reusa
        investimentoExistente.setUltimoSaque(LocalDate.now());

        repository.save(investimentoExistente);

        Registro registro = new Registro(
                TipoRegistro.ENTRADA,
                CategoriaRegistro.INVESTIMENTO,
                investimentoExistente.getDescricao(),
                valor,
                LocalDate.now()
        );

        registroRepository.save(registro);

        return registro;
    }

    public void validaCampoObg(InvestimentoRequest investimentoRequest) {

        if (investimentoRequest.valorAplicado() == null) {
            throw new RuntimeException("Valor Aplicado é um campo Obrigatório");
        }

        if (investimentoRequest.categoria() == null) {
            throw new RuntimeException("Categoria é um campo Obrigatório");
        }

        if (investimentoRequest.categoria() != CategoriaInvestimento.OUTROS) {
            if (investimentoRequest.taxaJuros() == null) {
                throw new RuntimeException("Taxa de juros é obrigatória para esta categoria");
            }
            if (investimentoRequest.periodicidadeTaxa() == null) {
                throw new RuntimeException("Periodicidade da taxa é obrigatória para esta categoria");
            }
        }

    }

    public void validaValor(InvestimentoRequest investimentoRequest){

        if (investimentoRequest.valorAplicado().compareTo(BigDecimal.ZERO)<=0){
            throw new RuntimeException("O valor deve ser um número positivo e maior que 0");
        }

    }

    //INICIO CALCULOS MONETARIOS


    private LocalDate dataReferencia(Investimento investimento) {
        return investimento.getUltimoSaque() == null
                ? investimento.getData()
                : investimento.getUltimoSaque();
    }

    private Long calcularDiasCorridos(LocalDate aplicacao, LocalDate saque) {

        return DAYS.between(aplicacao, saque);
    }

    private BigDecimal calcularTaxaDiaria(BigDecimal taxa, PeriodicidadeTaxa periodicidadeTaxa) {

        //Formula do jurosComposto JC = (1+i) ^ (1/n) - 1

        //Define em qual "casa" para
        MathContext mc = MathContext.DECIMAL64;

        BigDecimal taxaDecimal = taxa.divide(BigDecimal.valueOf(100), mc); //Converte a taxa em decimal

        BigDecimal base = BigDecimal.ONE.add(taxaDecimal); // (1 + i)

        BigDecimal diasPeriodo = (periodicidadeTaxa == PeriodicidadeTaxa.ANUAL) // Define se a n é 365 ou 30
                ? BigDecimal.valueOf(365)
                : BigDecimal.valueOf(30);
        BigDecimal expoente = BigDecimal.ONE.divide(diasPeriodo, mc); //(1/n)


        return BigDecimalMath.pow(base, expoente, mc).subtract(BigDecimal.ONE);//(1+i) ^ (1/n) - 1

    }

    private BigDecimal valorBrutoFinal(Investimento investimento) {




        BigDecimal taxaDiaria = calcularTaxaDiaria(investimento.getTaxaJuros(),investimento.getPeriodicidadeTaxa());
        Long diasCorridos = calcularDiasCorridos(dataReferencia(investimento), LocalDate.now());


        //Formula juros compostos = valorAplicado × (1 + taxaDiária) ^ diasCorridos

        BigDecimal base = BigDecimal.valueOf(1.0).add(taxaDiaria); //(1 + taxaDiaria)
        BigDecimal fatorPotencia = base.pow(diasCorridos.intValue());//(1+taxaDiaria)^diasCorridos




        return investimento.getValorAplicado().multiply(fatorPotencia);//valorAplicado × (1 + taxaDiária) ^ diasCorridos;


    }

    private BigDecimal calcularIOF(BigDecimal rendimentoBruto, LocalDate dataAplicacao) {


        int[] tabelaIOF = {96, 93, 90, 86, 83, 80, 76, 73, 70, 66, 63, 60, 56, 53, 50, 46, 43, 40, 36, 33, 30, 26, 23, 20, 16, 13, 10, 6, 3};
        BigDecimal iof = BigDecimal.ZERO;
        BigDecimal aliquota;
        Long diasCorridos = calcularDiasCorridos(dataAplicacao, LocalDate.now());

        if (diasCorridos >= 30) {
            aliquota = BigDecimal.ZERO;
        } else if (diasCorridos <= 0) {
            aliquota = BigDecimal.valueOf(tabelaIOF[0]);
        } else {
            aliquota = BigDecimal.valueOf(tabelaIOF[diasCorridos.intValue() - 1]);
        }

        if (rendimentoBruto.compareTo(BigDecimal.ZERO) > 0) {

            //Caso a aliquota tenha valores estranhos cujo a divisão dê dizima ex 0,333... é necessário colcoar um MC na divisão
            iof = rendimentoBruto.multiply(aliquota.divide(BigDecimal.valueOf(100)));
        }

        return iof;
    }


    private BigDecimal calcularIR(BigDecimal rendimentoLiquidoIOF, Investimento investimento) {

        if (investimento.isIsentoIR()) {
            return BigDecimal.ZERO;
        }

        Long diasCorridos = calcularDiasCorridos(dataReferencia(investimento), LocalDate.now());
        BigDecimal aliquota;

        if (diasCorridos <= 180) {
            aliquota = BigDecimal.valueOf(22.5);
        } else if (diasCorridos <= 360) {
            aliquota = BigDecimal.valueOf(20);
        } else if (diasCorridos <= 720) {
            aliquota = BigDecimal.valueOf(17.5);
        } else {
            aliquota = BigDecimal.valueOf(15);
        }

        //Caso a aliquota tenha valores estranhos cujo a divisão dê dizima ex 0,333... é necessário colcoar um MC na divisão
        return rendimentoLiquidoIOF.multiply(aliquota.divide(BigDecimal.valueOf(100)));
    }

    private BigDecimal valorDisponivelSaque(Investimento investimento) {

        if (investimento.getCategoria() == CategoriaInvestimento.OUTROS) {
            return investimento.getValorAplicado();
        }
            BigDecimal valorBruto = valorBrutoFinal(investimento);
            BigDecimal rendimento = valorBruto.subtract(investimento.getValorAplicado());

            BigDecimal calcularIOF = calcularIOF(rendimento, dataReferencia(investimento));
            BigDecimal rendimentoLiquidoIOF = rendimento.subtract(calcularIOF);

            BigDecimal calcularIR = calcularIR(rendimentoLiquidoIOF, investimento);


            return investimento.getValorAplicado().add(rendimentoLiquidoIOF).subtract(calcularIR)
                    .setScale(2, RoundingMode.HALF_EVEN);

    }

    //FIM DOS CALCULOS MONETARIOS



    private Investimento toEntity(InvestimentoRequest investimentoRequest) {

        LocalDate data;

        BigDecimal taxaJuros = investimentoRequest.taxaJuros();
        boolean isentoIR = investimentoRequest.isentoIR();
        PeriodicidadeTaxa periodicidadeTaxa = investimentoRequest.periodicidadeTaxa();
        CategoriaInvestimento categoria = investimentoRequest.categoria();


        if (investimentoRequest.data() == null) {
            data = LocalDate.now();
        } else {
            data = investimentoRequest.data();
        }

        if (categoria == CategoriaInvestimento.OUTROS) {
            taxaJuros = BigDecimal.ZERO;
            periodicidadeTaxa = null;
            isentoIR = true;   // OUTROS não rende, então não há IR mesmo
        }


        if (categoria == CategoriaInvestimento.LCI
                || categoria == CategoriaInvestimento.LCA
                || categoria == CategoriaInvestimento.POUPANCA) {
            isentoIR = true;
        }

            return new Investimento(
                    investimentoRequest.descricao(),
                    investimentoRequest.valorAplicado(),
                    data,
                    investimentoRequest.categoria(),
                    TipoInvestimento.INVESTIMENTO,
                    isentoIR,
                    taxaJuros,
                    periodicidadeTaxa
            );
        }



    private InvestimentoResponse toResponse(Investimento investimento) {

        return new InvestimentoResponse(
                investimento.getId(),
                investimento.getDescricao(),
                investimento.getValorAplicado(),
                investimento.getData(),
                investimento.getCategoria(),
                investimento.getTipo(),
                investimento.isIsentoIR(),
                investimento.getTaxaJuros(),
                investimento.getPeriodicidadeTaxa(),
                investimento.getUltimoSaque()
        );
    }

    private List<InvestimentoResponse> listResponse(List<Investimento> investimentos) {

        List<InvestimentoResponse> responses = new ArrayList<>();

        for (Investimento investimento : investimentos) {

            responses.add(toResponse(investimento));
        }

        return responses;
    }
}


