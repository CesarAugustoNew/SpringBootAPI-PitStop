package br.com.pitstop.spring_boot_clean_car.dto.response;

import java.util.UUID;

public record ClienteResponse(
        UUID id,
        String nome,
        String telefone,
        String endereco
) {
}
