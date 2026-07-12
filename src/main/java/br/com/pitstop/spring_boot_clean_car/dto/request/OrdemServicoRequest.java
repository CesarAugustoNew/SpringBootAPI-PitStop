package br.com.pitstop.spring_boot_clean_car.dto.request;

import br.com.pitstop.spring_boot_clean_car.enums.TipoServico;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record OrdemServicoRequest(
        @NotNull UUID clienteId,
        @NotNull UUID veiculoId,
        @NotNull TipoServico tipoServico,
        String objetosValor,
        @NotNull @DecimalMin(value = "0.0", inclusive = true) BigDecimal valor
) {
}
