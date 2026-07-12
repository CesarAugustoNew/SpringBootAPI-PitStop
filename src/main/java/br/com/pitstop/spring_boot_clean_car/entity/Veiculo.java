package br.com.pitstop.spring_boot_clean_car.entity;

import br.com.pitstop.spring_boot_clean_car.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "veiculos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Veiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 10, unique = true)
    private String placa;

    @Column(nullable = false, length = 50)
    private String modelo;

    @Column(length = 50)
    private String marca;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
}
