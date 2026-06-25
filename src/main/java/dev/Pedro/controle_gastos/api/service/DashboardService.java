package dev.Pedro.controle_gastos.api.service;

import dev.Pedro.controle_gastos.domain.entity.Investimento;
import dev.Pedro.controle_gastos.domain.entity.Registro;
import dev.Pedro.controle_gastos.domain.repository.InvestimentoRepository;
import dev.Pedro.controle_gastos.domain.repository.RegistroRepository;
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

        return totalEntrada().subtract(totalSaida());
    }

    public BigDecimal patrimonio() {

        return saldoTotal().add(totalInvestido());
    }

    public BigDecimal entradaMensal() {

        return somaMensal(TipoRegistro.RECEITA);
    }

    public BigDecimal saidaMensal() {

        return somaMensal(TipoRegistro.GASTO);
    }


    public BigDecimal saldoMensal() {

        return entradaMensal().subtract(saidaMensal());

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

    private BigDecimal somaMensal(TipoRegistro tipo){

        LocalDate hoje = LocalDate.now();
        LocalDate inicio = hoje.withDayOfMonth(1);
        LocalDate fim = hoje.withDayOfMonth(hoje.lengthOfMonth());


        return somarRegistros(registroRepository.findByTipoRegistroAndDataBetween(
                tipo,
                inicio,
                fim
        ));
    }


}



