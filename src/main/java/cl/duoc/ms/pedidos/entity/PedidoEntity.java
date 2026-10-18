package cl.duoc.ms.pedidos.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedido")
@Getter 
@Setter
public class PedidoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido")
    private Integer id;

    @Column(name = "id_cuenta_externo", nullable = false)
    private Integer idCuentaExterno;

    @Column(name = "estado", nullable = false, length = 30)
    private String estado; // PENDIENTE_PAGO, CONFIRMADO, RECHAZADO, EN_PREPARACION, DESPACHADO

    @Column(name = "nombre_destinatario", nullable = false, length = 140)
    private String nombreDestinatario;

    @Column(name = "calle", nullable = false, length = 160)
    private String calle;

    @Column(name = "numero", nullable = false, length = 20)
    private String numero;

    @Column(name = "depto_casa", length = 40)
    private String deptoCasa;

    @Column(name = "comuna", nullable = false, length = 100)
    private String comuna;

    @Column(name = "region", nullable = false, length = 100)
    private String region;

    @Column(name = "telefono", nullable = false, length = 30)
    private String telefono;

    @Column(name = "referencia", length = 250)
    private String referencia;

    @Column(name = "subtotal", nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "costo_despacho", nullable = false, precision = 10, scale = 2)
    private BigDecimal costoDespacho;

    @Column(name = "total", nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<DetallePedidoEntity> detalles = new ArrayList<>();

    @OneToOne(mappedBy = "pedido", cascade = CascadeType.ALL)
    private PagoSimuladoEntity pagoSimulado;

    // 1. Constructor vacío manual (reemplaza a @NoArgsConstructor, obligatorio para JPA)
    public PedidoEntity() {
    }

    // 2. Constructor completo explícito con @Builder (reemplaza a @AllArgsConstructor y @Builder.Default)
    @Builder
    public PedidoEntity(Integer id, Integer idCuentaExterno, String estado, String nombreDestinatario,
                        String calle, String numero, String deptoCasa, String comuna, String region,
                        String telefono, String referencia, BigDecimal subtotal, BigDecimal costoDespacho,
                        BigDecimal total, LocalDateTime createdAt, LocalDateTime updatedAt,
                        List<DetallePedidoEntity> detalles, PagoSimuladoEntity pagoSimulado) {
        this.id = id;
        this.idCuentaExterno = idCuentaExterno;
        this.estado = estado;
        this.nombreDestinatario = nombreDestinatario;
        this.calle = calle;
        this.numero = numero;
        this.deptoCasa = deptoCasa;
        this.comuna = comuna;
        this.region = region;
        this.telefono = telefono;
        this.referencia = referencia;
        this.subtotal = subtotal;
        this.costoDespacho = costoDespacho;
        this.total = total;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.detalles = detalles != null ? detalles : new ArrayList<>();
        this.pagoSimulado = pagoSimulado;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}