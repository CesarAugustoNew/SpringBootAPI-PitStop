package br.com.pitstop.spring_boot_clean_car.repository;

import br.com.pitstop.spring_boot_clean_car.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IClienteRepository extends JpaRepository<Cliente, UUID> {
}
