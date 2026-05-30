package dev.Pedro.controle_gastos.api.service;

import dev.Pedro.controle_gastos.api.dto.RegistroRequest;
import dev.Pedro.controle_gastos.api.dto.RegistroResponse;
import dev.Pedro.controle_gastos.domain.model.Registro;
import dev.Pedro.controle_gastos.domain.repository.RegistroRepository;
import dev.Pedro.controle_gastos.enums.Categorias;
import dev.Pedro.controle_gastos.enums.TipoRegistro;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional

public class RegistroService {

    //validar campos obrigatórios

    public void validaCamposObg (RegistroRequest registroRequest){

        if(registroRequest.tipoRegistro() == null){
            throw new RuntimeException("Tipo é um campo Obrigatório");
        }

        if(registroRequest.categoria() == null){
            throw new RuntimeException("Categoria é um campo Obrigatório");
        }


        if(registroRequest.valor()== null){
            throw new RuntimeException("Valor é um campo Obrigatório");
        }

    }

    //Valida se a Categoria de registro é compatível ao Tipo

    public void validarCategoria_Tipo(RegistroRequest registroRequest){

        //Verifica se o tipo pré definido na Categoria do enum bate com o tipo de registro escolhido

        if (!registroRequest.categoria().getTipo().equals(registroRequest.tipoRegistro())){
            throw new RuntimeException("Essa categoria é incompatível com o tipo de registro selecionado");
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

        Registro registro = toEntity(registroRequest);

        return toResponse(repository.save(registro));
    }

    public RegistroResponse update(Long id, RegistroRequest registroRequest){

        //Valida denovo , para não ocorrer erros
        validaCamposObg(registroRequest);
        validarCategoria_Tipo(registroRequest);

        //Valida se o registro existe e retorna o erro
        Registro registroExistente = repository.findById(id)
                .orElseThrow(()->new RuntimeException("Registro não encontrado"));

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
        Registro registro = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro não existe"));

        //Exclui
        repository.delete(registro);
    }


        //Readers

    public Registro buscaPorId(Long id){

        return repository.findById(id).orElseThrow(() -> new RuntimeException("Registro não encontrado"));

    }

    public List<Registro> buscaPorCategoria(Categorias categoria){

        return repository.findByCategoria(categoria);

    }

    public List<Registro> buscarPorPeriodo(LocalDate inicio, LocalDate fim) {

        return repository.findByDataBetween(inicio, fim);

    }

    public List<Registro> buscarPorTipo(TipoRegistro tipo) {

        return repository.findByTipoRegistro(tipo);
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





}
