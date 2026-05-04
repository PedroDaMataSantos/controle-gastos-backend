package com.damatapedro.controle_gastos.api.service;

import com.damatapedro.controle_gastos.domain.model.Registro;
import com.damatapedro.controle_gastos.domain.repository.RegistroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public Registro create(Registro registro) {
        validaCamposObg(registro);
        validarCategoria_Tipo(registro);

        return repository.save(registro);
    }


}
