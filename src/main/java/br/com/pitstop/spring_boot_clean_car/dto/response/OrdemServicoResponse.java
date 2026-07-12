package br.com.pitstop.spring_boot_clean_car.dto.response;

import br.com.pitstop.spring_boot_clean_car.enums.StatusOrdem;
import br.com.pitstop.spring_boot_clean_car.enums.TipoServico;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record OrdemServicoResponse(
        UUID id,
        ClienteResponse cliente,
        VeiculoResponse veiculo,
        String funcionarioNome,
        TipoServico tipoServico,
        StatusOrdem status,
        String objetosValor,
        BigDecimal valor,
        LocalDateTime dataEntrada
) {
}
