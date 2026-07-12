package br.com.pitstop.spring_boot_clean_car.controller;

import br.com.pitstop.spring_boot_clean_car.dto.request.ClienteRequest;
import br.com.pitstop.spring_boot_clean_car.dto.response.ClienteResponse;
import br.com.pitstop.spring_boot_clean_car.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Clientes", description = "Cadastro de clientes do lava-rapido")
@RestController
@CrossOrigin("*")
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    @Operation(summary = "Listar clientes")
    public ResponseEntity<List<ClienteResponse>> listar() {
        return new ResponseEntity<>(clienteService.listar(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar cliente por ID")
    public ResponseEntity<ClienteResponse> buscarPorId(@PathVariable UUID id) {
        return new ResponseEntity<>(clienteService.buscarPorId(id), HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Cadastrar cliente")
    public ResponseEntity<ClienteResponse> criar(@Valid @RequestBody ClienteRequest request) {
        return new ResponseEntity<>(clienteService.criar(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar cliente")
    public ResponseEntity<ClienteResponse> atualizar(@PathVariable UUID id, @Valid @RequestBody ClienteRequest request) {
        return new ResponseEntity<>(clienteService.atualizar(id, request), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover cliente")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        clienteService.deletar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
