package dev.Pedro.controle_gastos.api.service;

import dev.Pedro.controle_gastos.api.dto.InvestimentoRequest;
import dev.Pedro.controle_gastos.api.dto.InvestimentoResponse;
import dev.Pedro.controle_gastos.domain.entity.Investimento;
import dev.Pedro.controle_gastos.domain.repository.InvestimentoRepository;

import java.util.List;

public class InvestimentoService {

    private final InvestimentoRepository repository;

    public InvestimentoService(InvestimentoRepository ivestimentoRepository) {

        this.repository = ivestimentoRepository;
    }

    public InvestimentoResponse create(InvestimentoRequest investimentoRequest){

        Investimento investimento = toEntity(investimentoRequest);

        return toResponse(repository.save(investimento));

    }


    public InvestimentoResponse update (Long id , InvestimentoRequest investimentoRequest){

        Investimento investimentoExistente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro não encontrado"));


        investimentoExistente.setValorAplicado(investimentoRequest.valorAplicado());

        investimentoExistente.setDescricao(investimentoRequest.descricao());

        investimentoExistente.setData(investimentoRequest.data());

        investimentoExistente.setCategoria(investimentoRequest.categoria());

        return toResponse(repository.save(investimentoExistente));
    }


    public void delete(Long id){

        if(!repository.existsById(id)){
            throw new RuntimeException("Investimento não encontrada. id=" + id);
        }


         repository.deleteById(id);

    }






    public Investimento toEntity(InvestimentoRequest investimentoRequest){

        return new Investimento(
                investimentoRequest.descricao(),
                investimentoRequest.valorAplicado(),
                investimentoRequest.data(),
                investimentoRequest.categoria()
        );
    }

    public InvestimentoResponse toResponse(Investimento investimento){

        return new InvestimentoResponse(
                investimento.getId(),
                investimento.getDescricao(),
                investimento.getValorAplicado(),
                investimento.getData(),
                investimento.getCategoria()
        );
    }


}
