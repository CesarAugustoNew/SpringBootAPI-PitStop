package br.com.pitstop.spring_boot_clean_car.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record VeiculoRequest(
        @NotBlank String placa,
        @NotBlank String modelo,
        String marca,
        @NotNull UUID clienteId
) {
}
