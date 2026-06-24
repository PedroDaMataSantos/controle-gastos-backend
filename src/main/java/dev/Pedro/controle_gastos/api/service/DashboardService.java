package dev.Pedro.controle_gastos.api.service;

import dev.Pedro.controle_gastos.api.dto.RegistroResponse;
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

    public DashboardService(RegistroRepository registroRepository,InvestimentoRepository investimentoRepository){

        this.registroRepository = registroRepository;
        this.investimentoRepository = investimentoRepository;
    }


    public BigDecimal totalInvestido(){

        List<Investimento> investimentos = investimentoRepository.findAll();

        BigDecimal total = BigDecimal.ZERO;

        for (Investimento investimento : investimentos){

            total = total.add(investimento.getValorAplicado());
        }

        return total;

    }

    public BigDecimal totalEntrada(){

        List<Registro> entradas = registroRepository.findByTipoRegistro(TipoRegistro.RECEITA);

        BigDecimal total = BigDecimal.ZERO;

        for (Registro registro : entradas){
            total = total.add(registro.getValor());
        }

        return total;

    }

    public BigDecimal totalSaida(){

        List <Registro> saidas = registroRepository.findByTipoRegistro(TipoRegistro.GASTO);

        BigDecimal total = BigDecimal.ZERO;

        for(Registro registro : saidas){

            total = total.add(registro.getValor());
        }

        return total;
    }

    public BigDecimal saldoTotal() {

        return totalEntrada().subtract(totalSaida());
    }

    public BigDecimal patrimonio(){

        return saldoTotal().add(totalInvestido());
    }

    public BigDecimal entradaMensal(){

        LocalDate hoje = LocalDate.now();

        LocalDate inicio = hoje.withDayOfMonth(1);

        LocalDate fim = hoje.withDayOfMonth(hoje.lengthOfMonth());

        List<Registro> entradaMensal = registroRepository.findByTipoRegistroAndDataBetween(TipoRegistro.RECEITA,inicio,fim);

        BigDecimal total = BigDecimal.ZERO;

        for (Registro registro : entradaMensal) {

            total = total.add(registro.getValor());
        }

        return total;
    }


    }



