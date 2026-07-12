package br.com.pitstop.spring_boot_clean_car.repository;

import br.com.pitstop.spring_boot_clean_car.entity.OrdemServico;
import br.com.pitstop.spring_boot_clean_car.enums.StatusOrdem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface IOrdemServicoRepository extends JpaRepository<OrdemServico, UUID> {
    List<OrdemServico> findByDataEntradaBetween(LocalDateTime inicio, LocalDateTime fim);
    List<OrdemServico> findByStatus(StatusOrdem status);
    List<OrdemServico> findByVeiculoId(UUID veiculoId);
    List<OrdemServico> findByClienteId(UUID clienteId);
}
