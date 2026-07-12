package br.com.pitstop.spring_boot_clean_car.dto.request;

import br.com.pitstop.spring_boot_clean_car.enums.StatusOrdem;
import jakarta.validation.constraints.NotNull;

public record AtualizarStatusRequest(
        @NotNull StatusOrdem status
) {
}
