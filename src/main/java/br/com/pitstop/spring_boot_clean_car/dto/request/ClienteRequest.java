package br.com.pitstop.spring_boot_clean_car.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ClienteRequest(
        @NotBlank String nome,
        @NotBlank String telefone,
        @NotBlank String endereco
) {
}
