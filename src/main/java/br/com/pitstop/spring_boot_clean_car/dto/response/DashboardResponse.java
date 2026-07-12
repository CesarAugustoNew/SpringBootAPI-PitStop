package br.com.pitstop.spring_boot_clean_car.dto.response;

import br.com.pitstop.spring_boot_clean_car.enums.TipoServico;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record DashboardResponse(
        LocalDate data,
        long totalOrdens,
        long totalRecebidas,
        long totalEmLavagem,
        long totalFinalizadas,
        long totalEntregues,
        BigDecimal faturamentoTotal,
        List<TotaisPorTipoServico> porTipoServico
) {
    public record TotaisPorTipoServico(
            TipoServico tipoServico,
            long quantidade,
            BigDecimal total
    ) {
    }
}
