package cl.duoc.ms.pedidos.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pago_simulado") // Ajusta el nombre de la tabla si es necesario
@Getter
@Setter
@NoArgsConstructor // Lombok genera el constructor vacío que requiere JPA
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

    // Constructor explícito completo con @Builder (reemplaza a @AllArgsConstructor)
    @Builder
    public PagoSimuladoEntity(Integer id, PedidoEntity pedido, String resultado, 
                              BigDecimal monto, String referenciaSimulada, 
                              LocalDateTime procesadoAt) {
        this.id = id;
        this.pedido = pedido;
        this.resultado = resultado;
        this.monto = monto;
        this.referenciaSimulada = referenciaSimulada;
        this.procesadoAt = procesadoAt;
    }

    @PrePersist
    protected void onCreate() {
        this.procesadoAt = LocalDateTime.now();
    }
}