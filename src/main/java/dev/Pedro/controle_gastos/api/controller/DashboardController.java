package dev.Pedro.controle_gastos.api.controller;


import dev.Pedro.controle_gastos.api.dto.DashboardResponse;
import dev.Pedro.controle_gastos.api.service.DashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/scontg/dashboard")

public class DashboardController {

    public final DashboardService service;

    public DashboardController(DashboardService service) {
        this.service = service;
    }

    @GetMapping
    public DashboardResponse dashboard() {
        return service.dashboard();
    }
}


