package dev.Pedro.controle_gastos.api.service;

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


    public Registro sacar(Long id ,BigDecimal valor) {

        Investimento investimentoExistente = repository.findById(id).
                orElseThrow(() -> new RuntimeException("Investimento não encontrado"));

        if(valorDisponivelSaque(investimentoExistente).compareTo(valor) < 0 || valor.compareTo(BigDecimal.ZERO) <= 0){
            throw new RuntimeException("Valor inválido");
        }

        investimentoExistente.setValorAplicado(valorDisponivelSaque(investimentoExistente).subtract(valor));

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

    }

    public void validaValor(InvestimentoRequest investimentoRequest){

        if (investimentoRequest.valorAplicado().compareTo(BigDecimal.ZERO)<=0){
            throw new RuntimeException("O valor deve ser um número positivo e maior que 0");
        }

    }

    //INICIO CALCULOS MONETARIOS

    private Long calcularDiasCorridos(LocalDate aplicacao, LocalDate saque) {

        return DAYS.between(aplicacao, saque);
    }

    private BigDecimal calcularTaxaDiaria(Double taxa, PeriodicidadeTaxa periodicidadeTaxa) {

        double taxaDiaria;


        if (periodicidadeTaxa == PeriodicidadeTaxa.ANUAL) {
            taxaDiaria = Math.pow(1.0 + (taxa/100),(1.0/365))-1;
        }else{
            taxaDiaria = Math.pow(1.0 + (taxa/100),(1.0/30))-1;
        }

        return BigDecimal.valueOf(taxaDiaria);
    }

    private BigDecimal valorBrutoFinal(Investimento investimento) {


        LocalDate hoje = LocalDate.now();

        BigDecimal taxaDiaria = calcularTaxaDiaria(investimento.getTaxaJuros().doubleValue(),investimento.getPeriodicidadeTaxa());
        Long diasCorridos = calcularDiasCorridos(investimento.getData(),hoje);

        //Formula juros compostos = valorAplicado × (1 + taxaDiária) ^ diasCorridos

        BigDecimal base = BigDecimal.valueOf(1.0).add(taxaDiaria); //(1 + taxaDiaria)
        BigDecimal fatorPotencia = base.pow(diasCorridos.intValue());//(1+taxaDiaria)^diasCorridos




        return investimento.getValorAplicado().multiply(fatorPotencia);//valorAplicado × (1 + taxaDiária) ^ diasCorridos;


    }

    private BigDecimal calcularIOF(BigDecimal rendimentoBruto, LocalDate dataAplicacao) {

        int[] tabelaIOF = {96, 93, 90, 86, 83, 80, 76, 73, 70, 66, 63, 60, 56, 53, 50, 46, 43, 40, 36, 33, 30, 26, 23, 20, 16, 13, 10, 6, 3};
        double iof = 0;
        double aliquota;
        Long diasCorridos = calcularDiasCorridos(dataAplicacao, LocalDate.now());

        if (diasCorridos >= 30) {
            aliquota = 0;
        } else if (diasCorridos <= 0) {
            aliquota = tabelaIOF[0];
        } else {
            aliquota = tabelaIOF[diasCorridos.intValue() - 1];
        }

        if (rendimentoBruto.doubleValue() > 0) {
            iof = rendimentoBruto.doubleValue() * aliquota / 100;
        }

        return BigDecimal.valueOf(iof);
    }


    private BigDecimal calcularIR(BigDecimal rendimentoLiquidoIOF, Investimento investimento) {

        if (investimento.isIsentoIR()) {
            return BigDecimal.ZERO;
        }

        Long diasCorridos = calcularDiasCorridos(investimento.getData(), LocalDate.now());
        double aliquota;

        if (diasCorridos <= 180) {
            aliquota = 22.5;
        } else if (diasCorridos <= 360) {
            aliquota = 20;
        } else if (diasCorridos <= 720) {
            aliquota = 17.5;
        } else {
            aliquota = 15;
        }

        return BigDecimal.valueOf(rendimentoLiquidoIOF.doubleValue() * aliquota / 100);
    }

    private BigDecimal valorDisponivelSaque(Investimento investimento) {

        BigDecimal valorBruto = valorBrutoFinal(investimento);
        BigDecimal rendimento = valorBruto.subtract(investimento.getValorAplicado());

        BigDecimal calcularIOF = calcularIOF(rendimento,investimento.getData());
        BigDecimal rendimentoLiquidoIOF = rendimento.subtract(calcularIOF);

        BigDecimal calcularIR = calcularIR(rendimentoLiquidoIOF,investimento);


        return investimento.getValorAplicado().add(rendimentoLiquidoIOF).subtract(calcularIR);

    }

    //FIM DOS CALCULOS MONETARIOS



    private Investimento toEntity(InvestimentoRequest investimentoRequest) {

        LocalDate data;

        BigDecimal taxaJuros = investimentoRequest.taxaJuros();
        boolean isentoIR = investimentoRequest.isentoIR();
        PeriodicidadeTaxa periodicidadeTaxa = investimentoRequest.periodicidadeTaxa();


        if (investimentoRequest.data() == null) {
            data = LocalDate.now();
        } else {
            data = investimentoRequest.data();
        }

        if (investimentoRequest.categoria() != CategoriaInvestimento.RENDA_FIXA &&
                investimentoRequest.categoria() != CategoriaInvestimento.POUPANCA) {

            taxaJuros = BigDecimal.ZERO;
            isentoIR = true;
            periodicidadeTaxa = null;
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
                investimento.getPeriodicidadeTaxa()
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


