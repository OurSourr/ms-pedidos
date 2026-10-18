package cl.duoc.ms.pedidos.entity;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pago_simulado")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class PagoSimuladoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pedido", nullable = false, unique = true)
    private PedidoEntity pedido;

    @Column(name = "resultado", nullable = false, length = 20)
    private String resultado; // APROBADO, RECHAZADO

    @Column(name = "monto", nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @Column(name = "referencia_simulada", nullable = false, unique = true, length = 80)
    private String referenciaSimulada;

    @Column(name = "procesado_at", nullable = false)
    private LocalDateTime procesadoAt;

    @PrePersist
    protected void onCreate() {
        this.procesadoAt = LocalDateTime.now();
    }
}