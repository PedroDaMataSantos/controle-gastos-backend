package dev.Pedro.controle_gastos.api.service;

import dev.Pedro.controle_gastos.api.dto.InvestimentoRequest;
import dev.Pedro.controle_gastos.api.dto.InvestimentoResponse;
import dev.Pedro.controle_gastos.domain.entity.Investimento;
import dev.Pedro.controle_gastos.domain.repository.InvestimentoRepository;
import dev.Pedro.controle_gastos.enums.CategoriaInvestimento;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional

public class InvestimentoService {

    public void validaCampoObg(InvestimentoRequest investimentoRequest) {

        if (investimentoRequest.valorAplicado() == null) {
            throw new RuntimeException("Valor Aplicado é um campo Obrigatório");
        }

        if (investimentoRequest.categoria() == null) {
            throw new RuntimeException("Categoria é um campo Obrigatório");
        }

    }


    private final InvestimentoRepository repository;

    public InvestimentoService(InvestimentoRepository investimentoRepository) {

        this.repository = investimentoRepository;
    }

    public InvestimentoResponse create(InvestimentoRequest investimentoRequest) {

        validaCampoObg(investimentoRequest);

        Investimento investimento = toEntity(investimentoRequest);

        return toResponse(repository.save(investimento));

    }


    public InvestimentoResponse update(Long id, InvestimentoRequest investimentoRequest) {

        validaCampoObg(investimentoRequest);

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

    public InvestimentoResponse buscarId(Long id) {

        Investimento investimento = repository.findById(id).
                orElseThrow(() -> new RuntimeException("Investimento não encontrado"));
        ;

        return toResponse(investimento);
    }

    public List<InvestimentoResponse> buscarCategoria(CategoriaInvestimento categoria) {

        return listResponse(repository.findByCategoria(categoria));

    }

    public List<InvestimentoResponse> buscarEntre(LocalDate inicio, LocalDate fim) {

        return listResponse(repository.findByDataBetween(inicio, fim));
    }

    public List<InvestimentoResponse> listarTodos() {

        return listResponse(repository.findAll());
    }


    public Investimento toEntity(InvestimentoRequest investimentoRequest) {

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
                investimentoRequest.categoria()
        );
    }

    public InvestimentoResponse toResponse(Investimento investimento) {

        return new InvestimentoResponse(
                investimento.getId(),
                investimento.getDescricao(),
                investimento.getValorAplicado(),
                investimento.getData(),
                investimento.getCategoria()
        );
    }

    public List<InvestimentoResponse> listResponse(List<Investimento> investimentos) {

        List<InvestimentoResponse> responses = new ArrayList<>();

        for (Investimento investimento : investimentos) {

            responses.add(toResponse(investimento));
        }

        return responses;
    }


}
