package br.com.pitstop.spring_boot_clean_car.controller;

import br.com.pitstop.spring_boot_clean_car.dto.response.DashboardResponse;
import br.com.pitstop.spring_boot_clean_car.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/*
  Dashboard consumido pelo funcionario (e pelo admin) para ver o
  resultado do dia. O front-end que vai exibir isso ainda nao foi
  criado (conforme informado), mas o endpoint ja fica disponivel:

    GET /api/dashboard           -> resultado de hoje
    GET /api/dashboard?data=...  -> resultado de um dia especifico
*/
@Tag(name = "Dashboard", description = "Resultado do dia: ordens, status e faturamento")
@RestController
@CrossOrigin("*")
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping
    @Operation(summary = "Resultado do dia", description = "Totais de ordens, status e faturamento de um dia (hoje, por padrao)")
    public ResponseEntity<DashboardResponse> resultadoDoDia(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {

        DashboardResponse response = data != null
                ? dashboardService.resultadoDoDia(data)
                : dashboardService.resultadoDoDia();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
