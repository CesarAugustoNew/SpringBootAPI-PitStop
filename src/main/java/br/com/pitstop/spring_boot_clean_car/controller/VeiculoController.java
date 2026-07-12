package br.com.pitstop.spring_boot_clean_car.controller;

import br.com.pitstop.spring_boot_clean_car.dto.request.VeiculoRequest;
import br.com.pitstop.spring_boot_clean_car.dto.response.VeiculoResponse;
import br.com.pitstop.spring_boot_clean_car.service.VeiculoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/*
  Cadastro de veiculos do lava-rapido — o pedido principal do projeto.
  Este controller nao existia no esqueleto original.
*/
@Tag(name = "Veiculos", description = "Cadastro de veiculos do lava-rapido")
@RestController
@CrossOrigin("*")
@RequestMapping("/api/veiculos")
public class VeiculoController {

    @Autowired
    private VeiculoService veiculoService;

    @GetMapping
    @Operation(summary = "Listar veiculos", description = "Lista todos os veiculos, ou filtra por cliente com ?clienteId=")
    public ResponseEntity<List<VeiculoResponse>> listar(@RequestParam(required = false) UUID clienteId) {
        List<VeiculoResponse> veiculos = clienteId != null
                ? veiculoService.listarPorCliente(clienteId)
                : veiculoService.listar();

        return new ResponseEntity<>(veiculos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar veiculo por ID")
    public ResponseEntity<VeiculoResponse> buscarPorId(@PathVariable UUID id) {
        return new ResponseEntity<>(veiculoService.buscarPorId(id), HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Cadastrar veiculo", description = "Cadastra um veiculo vinculado a um cliente ja existente")
    public ResponseEntity<VeiculoResponse> criar(@Valid @RequestBody VeiculoRequest request) {
        return new ResponseEntity<>(veiculoService.criar(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar veiculo")
    public ResponseEntity<VeiculoResponse> atualizar(@PathVariable UUID id, @Valid @RequestBody VeiculoRequest request) {
        return new ResponseEntity<>(veiculoService.atualizar(id, request), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover veiculo")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        veiculoService.deletar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
