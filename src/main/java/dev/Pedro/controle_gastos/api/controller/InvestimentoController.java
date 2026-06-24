package dev.Pedro.controle_gastos.api.controller;

import dev.Pedro.controle_gastos.api.dto.InvestimentoRequest;
import dev.Pedro.controle_gastos.api.dto.InvestimentoResponse;
import dev.Pedro.controle_gastos.api.service.InvestimentoService;
import dev.Pedro.controle_gastos.enums.CategoriaInvestimento;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/scontg/investimentos")

public class InvestimentoController {

    private final InvestimentoService service;

    public InvestimentoController(InvestimentoService service) {
        this.service = service;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InvestimentoResponse create(@RequestBody InvestimentoRequest investimentoRequest) {

        return service.create(investimentoRequest);
    }

    @PutMapping("/{id}")
    public InvestimentoResponse update(@PathVariable Long id, @RequestBody InvestimentoRequest investimentoRequest) {

        return service.update(id, investimentoRequest);
    }

    @GetMapping("/{id}")
    public InvestimentoResponse getId(@PathVariable Long id) {

        return service.buscarId(id);


    }

    @GetMapping("/categoria/{categoria}")
    public List<InvestimentoResponse> getCategoria(@PathVariable CategoriaInvestimento categoria) {

        return service.buscarCategoria(categoria);

    }

    @GetMapping("/periodo")
    public List<InvestimentoResponse> getPeriodo(
            @RequestParam LocalDate inicio,
            @RequestParam LocalDate fim
    ) {

        return service.buscarEntre(inicio, fim);
    }

    @GetMapping
    public List<InvestimentoResponse> findAll() {

        return service.listarTodos();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)


    public void delete(@PathVariable Long id) {

        service.delete(id);
    }


}
