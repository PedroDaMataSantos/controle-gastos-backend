package dev.Pedro.controle_gastos.api.service;

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

    public void validaCamposObg (Registro registro){

        if(registro.getTipoRegistro() == null){
            throw new RuntimeException("Tipo é um campo Obrigatório");
        }

        if(registro.getCategoria() == null){
            throw new RuntimeException("Categoria é um campo Obrigatório");
        }

        if(registro.getData() == null){
            throw new RuntimeException("Data é um campo Obrigatório");
        }

        if(registro.getValor()== null){
            throw new RuntimeException("Valor é um campo Obrigatório");
        }

    }

    //Valida se a Categoria de registro é compatível ao Tipo

    public void validarCategoria_Tipo(Registro registro){

        //Verifica se o tipo pré definido na Categoria do enum bate com o tipo de registro escolhido

        if (!registro.getCategoria().getTipo().equals(registro.getTipoRegistro())){
            throw new RuntimeException("Essa categoria é incompatível com o tipo de registro selecionado");
        }
    }

    private final RegistroRepository repository;

    public RegistroService(RegistroRepository repository) {
        this.repository = repository;
    }

    //Create

    public Registro create(Registro registro) {

        if (registro.getData() == null) {
            registro.setData(LocalDate.now());
        }

        validaCamposObg(registro);
        validarCategoria_Tipo(registro);

        return repository.save(registro);
    }

    public Registro update(Long id, Registro novoRegistro){

        //Valida se o registro existe e retorna o erro
        Registro registroExistente = repository.findById(id).orElseThrow(()->new RuntimeException("Registro não encontrado"));

        //Update
        registroExistente.setTipoRegistro(novoRegistro.getTipoRegistro());

        registroExistente.setCategoria(novoRegistro.getCategoria());

        registroExistente.setDescricao(novoRegistro.getDescricao());

        registroExistente.setValor(novoRegistro.getValor());

        registroExistente.setData(novoRegistro.getData());

        //Valida denovo , para não ocorrer erros
        validaCamposObg(registroExistente);
        validarCategoria_Tipo(registroExistente);

        //Salva as novas entradas
        return repository.save(registroExistente);

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


}
