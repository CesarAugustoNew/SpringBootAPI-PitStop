package br.com.pitstop.spring_boot_clean_car.entity;

import br.com.pitstop.spring_boot_clean_car.enums.StatusOrdem;
import br.com.pitstop.spring_boot_clean_car.enums.TipoServico;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "ordens_servico")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdemServico {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "veiculo_id", nullable = false)
    private Veiculo veiculo;

    @ManyToOne
    @JoinColumn(name = "funcionario_id", nullable = false)
    private Usuario funcionario;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_servico", nullable = false)
    private TipoServico tipoServico;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusOrdem status;

    @Column(name = "objetos_valor", length = 255)
    private String objetosValor;

    @Column(nullable = false)
    private BigDecimal valor;

    @Column(name = "data_entrada", nullable = false)
    private LocalDateTime dataEntrada;
}