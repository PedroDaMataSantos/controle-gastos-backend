package dev.Pedro.controle_gastos.api.controller;


import dev.Pedro.controle_gastos.api.dto.RegistroRequest;
import dev.Pedro.controle_gastos.api.dto.RegistroResponse;
import dev.Pedro.controle_gastos.api.service.RegistroService;
import dev.Pedro.controle_gastos.enums.CategoriaRegistro;
import dev.Pedro.controle_gastos.enums.TipoRegistro;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/scontg/registros")

public class RegistroController {

    private final RegistroService service;

    public RegistroController(RegistroService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RegistroResponse create(@RequestBody RegistroRequest registroRequest) {

        return service.create(registroRequest);
    }

    @GetMapping
    public List<RegistroResponse> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public RegistroResponse getId(@PathVariable Long id){
        return service.buscaPorId(id);

    }

    @GetMapping("/categoria/{categoria}")
     public List <RegistroResponse> getCategoria(@PathVariable CategoriaRegistro categoria){
        return service.buscaPorCategoria(categoria);

    }

    @GetMapping("/tipo/{tipo}")
    public List <RegistroResponse> getTipo(@PathVariable TipoRegistro tipo){
        return service.buscarPorTipo(tipo);

    }

    @GetMapping("/periodo")
    public List<RegistroResponse> getPeriodo(
            @RequestParam LocalDate inicio,
            @RequestParam LocalDate fim) {

        return service.buscarPorPeriodo(inicio, fim);
    }

    @PutMapping("/{id}")
    public RegistroResponse update(@PathVariable Long id ,@RequestBody RegistroRequest req){
        return service.update(id,req);

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)

    public void  delete(@PathVariable Long id){
         service.delete(id);

    }






}
