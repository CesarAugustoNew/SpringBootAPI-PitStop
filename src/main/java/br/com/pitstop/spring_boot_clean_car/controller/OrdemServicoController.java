package br.com.pitstop.spring_boot_clean_car.controller;

import br.com.pitstop.spring_boot_clean_car.dto.request.AtualizarStatusRequest;
import br.com.pitstop.spring_boot_clean_car.dto.request.OrdemServicoRequest;
import br.com.pitstop.spring_boot_clean_car.dto.response.OrdemServicoResponse;
import br.com.pitstop.spring_boot_clean_car.enums.StatusOrdem;
import br.com.pitstop.spring_boot_clean_car.service.OrdemServicoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Ordens de Servico", description = "Registro do dia a dia de lavagens do lava-rapido")
@RestController
@CrossOrigin("*")
@RequestMapping("/api/ordens")
public class OrdemServicoController {

    @Autowired
    private OrdemServicoService ordemServicoService;

    @GetMapping
    @Operation(summary = "Listar ordens de servico", description = "Lista todas as ordens, ou filtra por status com ?status=")
    public ResponseEntity<List<OrdemServicoResponse>> listar(@RequestParam(required = false) StatusOrdem status) {
        List<OrdemServicoResponse> ordens = status != null
                ? ordemServicoService.listarPorStatus(status)
                : ordemServicoService.listar();

        return new ResponseEntity<>(ordens, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar ordem de servico por ID")
    public ResponseEntity<OrdemServicoResponse> buscarPorId(@PathVariable UUID id) {
        return new ResponseEntity<>(ordemServicoService.buscarPorId(id), HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Abrir ordem de servico", description = "Registra a entrada de um veiculo para lavagem; o funcionario e o usuario logado")
    public ResponseEntity<OrdemServicoResponse> criar(@Valid @RequestBody OrdemServicoRequest request, Authentication authentication) {
        OrdemServicoResponse response = ordemServicoService.criar(request, authentication.getName());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualizar status", description = "Avanca o status: RECEBIDO -> EM_LAVAGEM -> FINALIZADO -> ENTREGUE")
    public ResponseEntity<OrdemServicoResponse> atualizarStatus(@PathVariable UUID id, @Valid @RequestBody AtualizarStatusRequest request) {
        return new ResponseEntity<>(ordemServicoService.atualizarStatus(id, request.status()), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover ordem de servico")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        ordemServicoService.deletar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
