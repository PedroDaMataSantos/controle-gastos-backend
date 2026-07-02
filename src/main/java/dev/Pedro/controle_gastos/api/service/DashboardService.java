package dev.Pedro.controle_gastos.api.service;

import dev.Pedro.controle_gastos.api.dto.DashboardResponse;
import dev.Pedro.controle_gastos.domain.entity.Investimento;
import dev.Pedro.controle_gastos.domain.entity.Registro;
import dev.Pedro.controle_gastos.domain.repository.InvestimentoRepository;
import dev.Pedro.controle_gastos.domain.repository.RegistroRepository;
import dev.Pedro.controle_gastos.enums.CategoriaRegistro;
import dev.Pedro.controle_gastos.enums.TipoInvestimento;
import dev.Pedro.controle_gastos.enums.TipoRegistro;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional

public class DashboardService {

    private final RegistroRepository registroRepository;
    private final InvestimentoRepository investimentoRepository;

    public DashboardService(RegistroRepository registroRepository, InvestimentoRepository investimentoRepository) {

        this.registroRepository = registroRepository;
        this.investimentoRepository = investimentoRepository;
    }


    public DashboardResponse dashboard() {

        return new DashboardResponse(
                saldoTotal(),
                saldoMensal(),
                totalSaida(),
                saidaMensal(),
                totalEntrada(),
                entradaMensal(),
                totalInvestido(),
                investimentoMensal(),
                patrimonio(),
                gastoCategoriaTotal(),
                gastoCategoriaMensal()

        );
    }

    public BigDecimal totalInvestido() {

        return somaInvestimentos(investimentoRepository.findAll());

    }

    public BigDecimal totalEntrada() {

        return somaRegistros(registroRepository.findByTipoRegistro(TipoRegistro.RECEITA));

    }

    public BigDecimal totalSaida() {

        return somaRegistros(registroRepository.findByTipoRegistro(TipoRegistro.GASTO));
    }

    public BigDecimal saldoTotal() {

        return totalEntrada().subtract(totalSaida()).subtract(aporte());
    }

    //Map usa chave ou invés de indices para encontrar valor , no caso minha Chave é a CATEGORIA
    public Map<CategoriaRegistro, BigDecimal> gastoCategoriaTotal() {

        //Map <Chave,Valor>                        novo Tipo de Map que so aceita ENUM como chave (Informa qual enum será usado como chave).
        Map<CategoriaRegistro, BigDecimal> gastos = new EnumMap<>(CategoriaRegistro.class);

        for (CategoriaRegistro categoria : CategoriaRegistro.values()) {

            if (categoria.getTipo() != TipoRegistro.GASTO) {
                continue;
            }
                //COLOQUE (CHAVE , VALOR)
                gastos.put(categoria, somaGastosPorCategoria(categoria));

        }

        return gastos;
    }

    public BigDecimal aporte(){

        return somaAporte(TipoInvestimento.APORTE);
    }

    public BigDecimal patrimonio() {

        return saldoTotal().add(totalInvestido());
    }

    public BigDecimal entradaMensal() {

        return somaRegistroMensal(TipoRegistro.RECEITA);
    }

    public BigDecimal saidaMensal() {

        return somaRegistroMensal(TipoRegistro.GASTO);
    }

    public BigDecimal investimentoMensal(){

        return somaInvestimentoMensal();
    }


    public BigDecimal saldoMensal() {

        return entradaMensal().subtract(saidaMensal()).subtract(aporteMensal());

    }

    public BigDecimal aporteMensal() {

        return somaAporteMensal(TipoInvestimento.APORTE);
    }



    public Map<CategoriaRegistro, BigDecimal> gastoCategoriaMensal() {

        Map<CategoriaRegistro, BigDecimal> gastos = new EnumMap<>(CategoriaRegistro.class);

        for (CategoriaRegistro categoria : CategoriaRegistro.values()) {

            if (categoria.getTipo() != TipoRegistro.GASTO) {
                continue;
            }
                gastos.put(categoria, somaGastosCategoriaMensal(categoria)
                );
            }


        return gastos;
    }



    private BigDecimal somaRegistros(List<Registro> registros) {

        BigDecimal total = BigDecimal.ZERO;

        for (Registro registro : registros) {

            total = total.add(registro.getValor());
        }

        return total;

    }

    private BigDecimal somaInvestimentos(List<Investimento> investimentos) {

        BigDecimal total = BigDecimal.ZERO;

        for (Investimento investimento : investimentos) {

            total = total.add(investimento.getValorAplicado());

        }

        return total;
    }

    private BigDecimal somaRegistroMensal(TipoRegistro tipo){

        LocalDate hoje = LocalDate.now();
        LocalDate inicio = hoje.withDayOfMonth(1);
        LocalDate fim = hoje.withDayOfMonth(hoje.lengthOfMonth());


        return somaRegistros(registroRepository.findByTipoRegistroAndDataBetween(
                tipo,
                inicio,
                fim
        ));

    }



    private BigDecimal somaGastosPorCategoria(CategoriaRegistro categoria){

        if (categoria.getTipo() != TipoRegistro.GASTO) {
            throw new IllegalArgumentException(
                    "A categoria " + categoria + " não pertence ao tipo GASTO.");
        }

        return somaRegistros(registroRepository.findByCategoria(categoria));


    }

    private BigDecimal somaInvestimentoMensal(){

        LocalDate hoje = LocalDate.now();
        LocalDate inicio = hoje.withDayOfMonth(1);
        LocalDate fim = hoje.withDayOfMonth(hoje.lengthOfMonth());

        return somaInvestimentos(investimentoRepository.findByDataBetween(inicio,fim));
    }


    private BigDecimal somaAporte(TipoInvestimento tipo){

        return somaInvestimentos(investimentoRepository.findByTipo(tipo));

    }

    private BigDecimal somaAporteMensal(TipoInvestimento tipo){

        LocalDate hoje = LocalDate.now();
        LocalDate inicio = hoje.withDayOfMonth(1);
        LocalDate fim = hoje.withDayOfMonth(hoje.lengthOfMonth());

        return somaInvestimentos(investimentoRepository.findByTipoAndDataBetween(
                tipo,
                inicio,
                fim
        ));

    }



    private BigDecimal somaGastosCategoriaMensal(CategoriaRegistro categoria){

        LocalDate hoje = LocalDate.now();
        LocalDate inicio = hoje.withDayOfMonth(1);
        LocalDate fim = hoje.withDayOfMonth(hoje.lengthOfMonth());

         return somaRegistros(registroRepository.findByCategoriaAndDataBetween(categoria,inicio,fim));



    }

}



