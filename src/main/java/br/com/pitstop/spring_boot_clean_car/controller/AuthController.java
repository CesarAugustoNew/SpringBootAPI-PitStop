package br.com.pitstop.spring_boot_clean_car.controller;

import br.com.pitstop.spring_boot_clean_car.dto.request.LoginRequest;
import br.com.pitstop.spring_boot_clean_car.dto.response.LoginResponse;
import br.com.pitstop.spring_boot_clean_car.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Autenticacao", description = "Login de ADMIN e FUNCIONARIO")
@RestController
@CrossOrigin("*")
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Autentica um usuario (admin ou funcionario) e retorna um token JWT")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return new ResponseEntity<>(authService.login(request), HttpStatus.OK);
    }
}
