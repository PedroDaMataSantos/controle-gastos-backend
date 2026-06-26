package dev.Pedro.controle_gastos.api.service;


import dev.Pedro.controle_gastos.api.dto.RegistroRequest;
import dev.Pedro.controle_gastos.api.dto.RegistroResponse;
import dev.Pedro.controle_gastos.domain.entity.Registro;
import dev.Pedro.controle_gastos.domain.repository.RegistroRepository;
import dev.Pedro.controle_gastos.enums.CategoriaRegistro;
import dev.Pedro.controle_gastos.enums.TipoRegistro;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional

public class RegistroService {

    //validar campos obrigatórios

    public void validaCamposObg(RegistroRequest registroRequest) {

        if (registroRequest.tipoRegistro() == null) {
            throw new RuntimeException("Tipo é um campo Obrigatório");
        }

        if (registroRequest.categoria() == null) {
            throw new RuntimeException("Categoria é um campo Obrigatório");
        }


        if (registroRequest.valor() == null) {
            throw new RuntimeException("Valor é um campo Obrigatório");
        }

    }

    //Valida se a Categoria de registro é compatível ao Tipo

    public void validarCategoria_Tipo(RegistroRequest registroRequest) {

        //Verifica se o tipo pré definido na Categoria do enum bate com o tipo de registro escolhido

        if (!registroRequest.categoria().getTipo().equals(registroRequest.tipoRegistro())) {
            throw new RuntimeException("Essa categoria é incompatível com o tipo de registro selecionado");
        }
    }

    public void validaValor(RegistroRequest registroRequest){

        if (registroRequest.valor().compareTo(BigDecimal.ZERO)<=0){
            throw new RuntimeException("O valor deve ser um número positivo e maior que 0");
        }

    }

    private final RegistroRepository repository;

    public RegistroService(RegistroRepository repository) {
        this.repository = repository;
    }

    //Create

    public RegistroResponse create(RegistroRequest registroRequest) {

        validaCamposObg(registroRequest);
        validarCategoria_Tipo(registroRequest);
        validaValor(registroRequest);

        Registro registro = toEntity(registroRequest);

        return toResponse(repository.save(registro));
    }

    public RegistroResponse update(Long id, RegistroRequest registroRequest) {

        //Valida denovo , para não ocorrer erros
        validaCamposObg(registroRequest);
        validarCategoria_Tipo(registroRequest);
        validaValor(registroRequest);

        //Valida se o registro existe e retorna o erro
        Registro registroExistente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro não encontrado"));

        //Update
        registroExistente.setTipoRegistro(registroRequest.tipoRegistro());

        registroExistente.setCategoria(registroRequest.categoria());

        registroExistente.setDescricao(registroRequest.descricao());

        registroExistente.setValor(registroRequest.valor());

        //Impede que a data seja apagada e passada uma data null
        if (registroRequest.data() != null) {
            registroExistente.setData(registroRequest.data());
        }

        //Salva as novas entradas
        return toResponse(repository.save(registroExistente));

    }

    public void delete(Long id) {

        //Verifica se existe
        if (!repository.existsById(id)) {
            throw new RuntimeException("Registro não encontrada. id=" + id);
        }
        repository.deleteById(id);
    }


    //Readers
    public RegistroResponse findById(Long id) {

        Registro registro = repository.findById(id).
                orElseThrow(() -> new RuntimeException("Registro não encontrado"));

        return toResponse(registro);

    }

    public List<RegistroResponse> findByCategoria(CategoriaRegistro categoria) {

        return listResponse(repository.findByCategoria(categoria));

    }

    public List<RegistroResponse> findByPeriodo(LocalDate inicio, LocalDate fim) {

        return listResponse(repository.findByDataBetween(inicio, fim));

    }

    public List<RegistroResponse> findByTipo(TipoRegistro tipo) {

        return listResponse(repository.findByTipoRegistro(tipo));
    }


    public List<RegistroResponse> findAll() {

        return listResponse(repository.findAll());

    }


    public  List<RegistroResponse> findByDescricao(String descricao){

        return listResponse(repository.findByDescricaoContainingIgnoreCase(descricao));
    }


    //Transforma a entrada (Request) em entity para a operação no DB
    private Registro toEntity(RegistroRequest registroRequest) {

        LocalDate data;

        if (registroRequest.data() == null) {
            data = LocalDate.now();
        } else {
            data = registroRequest.data();
        }

        return new Registro(
                registroRequest.tipoRegistro(),
                registroRequest.categoria(),
                registroRequest.descricao(),
                registroRequest.valor(),
                data
        );
    }

    //Transforma a saída em Response para não devolver na forma de entity
    private RegistroResponse toResponse(Registro registro) {

        return new RegistroResponse(
                registro.getId(),
                registro.getTipoRegistro(),
                registro.getCategoria(),
                registro.getDescricao(),
                registro.getValor(),
                registro.getData()
        );

    }

    private List<RegistroResponse> listResponse(List<Registro> registros) {

        List<RegistroResponse> responses = new ArrayList<>();

        for (Registro registro : registros) {

            responses.add(toResponse(registro));
        }
        return responses;
    }


}
