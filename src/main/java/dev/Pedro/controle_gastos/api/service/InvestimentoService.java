package dev.Pedro.controle_gastos.api.service;

import dev.Pedro.controle_gastos.api.dto.InvestimentoRequest;
import dev.Pedro.controle_gastos.api.dto.InvestimentoResponse;
import dev.Pedro.controle_gastos.domain.entity.Investimento;
import dev.Pedro.controle_gastos.domain.entity.Registro;
import dev.Pedro.controle_gastos.domain.repository.InvestimentoRepository;
import dev.Pedro.controle_gastos.domain.repository.RegistroRepository;
import dev.Pedro.controle_gastos.enums.CategoriaInvestimento;
import dev.Pedro.controle_gastos.enums.CategoriaRegistro;
import dev.Pedro.controle_gastos.enums.TipoInvestimento;
import dev.Pedro.controle_gastos.enums.TipoRegistro;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
        ;

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

        if(investimentoExistente.getValorAplicado().compareTo(valor) < 0 || valor.compareTo(BigDecimal.ZERO) <= 0){
            throw new RuntimeException("Valor inválido");
        }

        investimentoExistente.setValorAplicado(investimentoExistente.getValorAplicado().subtract(valor));

        repository.save(investimentoExistente);

         Registro registro = new Registro(
                TipoRegistro.RECEITA,
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


    private Investimento toEntity(InvestimentoRequest investimentoRequest) {

        LocalDate data;

        if (investimentoRequest.data() == null) {
            data = LocalDate.now();
        } else {
            data = investimentoRequest.data();
        }



        return new Investimento(
                investimentoRequest.descricao(),
                investimentoRequest.valorAplicado(),
                data,
                investimentoRequest.categoria(),
                TipoInvestimento.INVESTIMENTO
        );
    }

    private InvestimentoResponse toResponse(Investimento investimento) {

        return new InvestimentoResponse(
                investimento.getId(),
                investimento.getDescricao(),
                investimento.getValorAplicado(),
                investimento.getData(),
                investimento.getCategoria(),
                investimento.getTipo()
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
