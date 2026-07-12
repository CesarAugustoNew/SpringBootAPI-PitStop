package br.com.pitstop.spring_boot_clean_car.controller;

import br.com.pitstop.spring_boot_clean_car.dto.request.UsuarioRequest;
import br.com.pitstop.spring_boot_clean_car.dto.response.UsuarioResponse;
import br.com.pitstop.spring_boot_clean_car.service.UsuarioService;
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
  Gerenciamento das contas de ADMIN e FUNCIONARIO.
  Acesso restrito a ADMIN (ver SecurityConfig: /api/usuarios/** -> hasRole('ADMIN')).
*/
@Tag(name = "Usuarios", description = "Gerenciamento de contas de admin e funcionario (somente ADMIN)")
@RestController
@CrossOrigin("*")
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    @Operation(summary = "Listar usuarios")
    public ResponseEntity<List<UsuarioResponse>> listar() {
        return new ResponseEntity<>(usuarioService.listar(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuario por ID")
    public ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable UUID id) {
        return new ResponseEntity<>(usuarioService.buscarPorId(id), HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Criar usuario", description = "Cria uma conta de ADMIN ou FUNCIONARIO")
    public ResponseEntity<UsuarioResponse> criar(@Valid @RequestBody UsuarioRequest request) {
        return new ResponseEntity<>(usuarioService.criar(request), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover usuario")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        usuarioService.deletar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
