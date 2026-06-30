package dev.Pedro.controle_gastos.api.service;

import dev.Pedro.controle_gastos.api.dto.DashboardResponse;
import dev.Pedro.controle_gastos.domain.entity.Investimento;
import dev.Pedro.controle_gastos.domain.entity.Registro;
import dev.Pedro.controle_gastos.domain.repository.InvestimentoRepository;
import dev.Pedro.controle_gastos.domain.repository.RegistroRepository;
import dev.Pedro.controle_gastos.enums.TipoInvestimento;
import dev.Pedro.controle_gastos.enums.TipoRegistro;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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
                patrimonio()
        );
    }

    public BigDecimal totalInvestido() {

        return somarInvestimentos(investimentoRepository.findAll());

    }

    public BigDecimal totalEntrada() {

        return somarRegistros(registroRepository.findByTipoRegistro(TipoRegistro.RECEITA));

    }

    public BigDecimal totalSaida() {

        return somarRegistros(registroRepository.findByTipoRegistro(TipoRegistro.GASTO));
    }

    public BigDecimal saldoTotal() {

        return totalEntrada().subtract(totalSaida()).subtract(aporte());
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

        return somarAporteMensal(TipoInvestimento.APORTE);
    }

    public BigDecimal aporte(){

        return somarAporte(TipoInvestimento.APORTE);
    }


    private BigDecimal somarRegistros(List<Registro> registros) {

        BigDecimal total = BigDecimal.ZERO;

        for (Registro registro : registros) {

            total = total.add(registro.getValor());
        }

        return total;

    }

    private BigDecimal somarInvestimentos(List<Investimento> investimentos) {

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


        return somarRegistros(registroRepository.findByTipoRegistroAndDataBetween(
                tipo,
                inicio,
                fim
        ));

    }

    private BigDecimal somaInvestimentoMensal(){

        LocalDate hoje = LocalDate.now();
        LocalDate inicio = hoje.withDayOfMonth(1);
        LocalDate fim = hoje.withDayOfMonth(hoje.lengthOfMonth());

        return somarInvestimentos(investimentoRepository.findByDataBetween(inicio,fim));
    }


    private BigDecimal somarAporte(TipoInvestimento tipo){

        return somarInvestimentos(investimentoRepository.findByTipo(tipo));

    }

    private BigDecimal somarAporteMensal(TipoInvestimento tipo){

        LocalDate hoje = LocalDate.now();
        LocalDate inicio = hoje.withDayOfMonth(1);
        LocalDate fim = hoje.withDayOfMonth(hoje.lengthOfMonth());

        return somarInvestimentos(investimentoRepository.findByTipoAndDataBetween(
                tipo,
                inicio,
                fim
        ));

    }




}



