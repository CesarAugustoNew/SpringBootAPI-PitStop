package br.com.pitstop.spring_boot_clean_car.dto.response;

import java.util.UUID;

public record VeiculoResponse(
        UUID id,
        String placa,
        String modelo,
        String marca,
        ClienteResponse cliente
) {
}
