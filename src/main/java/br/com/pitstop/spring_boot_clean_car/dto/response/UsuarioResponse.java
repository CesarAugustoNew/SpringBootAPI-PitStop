package br.com.pitstop.spring_boot_clean_car.dto.response;

import br.com.pitstop.spring_boot_clean_car.enums.Role;

import java.util.UUID;

public record UsuarioResponse(
        UUID id,
        String nome,
        String email,
        Role role
) {
}
