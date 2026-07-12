package br.com.pitstop.spring_boot_clean_car.repository;

import br.com.pitstop.spring_boot_clean_car.entity.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IVeiculoRepository extends JpaRepository<Veiculo, UUID> {
    List<Veiculo> findByClienteId(UUID clienteId);
    Optional<Veiculo> findByPlaca(String placa);
    boolean existsByPlaca(String placa);
}
