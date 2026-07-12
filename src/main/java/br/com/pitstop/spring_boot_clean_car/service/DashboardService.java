package br.com.pitstop.spring_boot_clean_car.service;

import br.com.pitstop.spring_boot_clean_car.dto.response.DashboardResponse;
import br.com.pitstop.spring_boot_clean_car.entity.OrdemServico;
import br.com.pitstop.spring_boot_clean_car.enums.StatusOrdem;
import br.com.pitstop.spring_boot_clean_car.enums.TipoServico;
import br.com.pitstop.spring_boot_clean_car.repository.IOrdemServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/*
  Dashboard do resultado do dia: usado pelo funcionario para
  acompanhar quantas ordens entraram, em que status estao, e o
  faturamento do dia — o front-end que vai consumir isso ainda nao
  foi criado, mas o endpoint ja fica pronto para quando existir.
*/
@Service
public class DashboardService {

    @Autowired
    private IOrdemServicoRepository ordemServicoRepository;

    public DashboardResponse resultadoDoDia() {
        return resultadoDoDia(LocalDate.now());
    }

    public DashboardResponse resultadoDoDia(LocalDate data) {
        LocalDateTime inicioDia = data.atStartOfDay();
        LocalDateTime fimDia = inicioDia.plusDays(1);

        List<OrdemServico> ordensDoDia = ordemServicoRepository.findByDataEntradaBetween(inicioDia, fimDia);

        long totalOrdens = ordensDoDia.size();
        long totalRecebidas = contarPorStatus(ordensDoDia, StatusOrdem.RECEBIDO);
        long totalEmLavagem = contarPorStatus(ordensDoDia, StatusOrdem.EM_LAVAGEM);
        long totalFinalizadas = contarPorStatus(ordensDoDia, StatusOrdem.FINALIZADO);
        long totalEntregues = contarPorStatus(ordensDoDia, StatusOrdem.ENTREGUE);

        BigDecimal faturamentoTotal = ordensDoDia.stream()
                .map(OrdemServico::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<TipoServico, List<OrdemServico>> agrupadoPorTipo = new LinkedHashMap<>();
        for (OrdemServico ordem : ordensDoDia) {
            agrupadoPorTipo.computeIfAbsent(ordem.getTipoServico(), t -> new java.util.ArrayList<>()).add(ordem);
        }

        List<DashboardResponse.TotaisPorTipoServico> porTipoServico = agrupadoPorTipo.entrySet().stream()
                .map(entry -> new DashboardResponse.TotaisPorTipoServico(
                        entry.getKey(),
                        entry.getValue().size(),
                        entry.getValue().stream().map(OrdemServico::getValor).reduce(BigDecimal.ZERO, BigDecimal::add)
                ))
                .toList();

        return new DashboardResponse(
                data,
                totalOrdens,
                totalRecebidas,
                totalEmLavagem,
                totalFinalizadas,
                totalEntregues,
                faturamentoTotal,
                porTipoServico
        );
    }

    private long contarPorStatus(List<OrdemServico> ordens, StatusOrdem status) {
        return ordens.stream().filter(o -> o.getStatus() == status).count();
    }
}
