package cl.duoc.ms.pedidos.entity;


import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "detalle_pedido")
@Getter @Setter
@NoArgsConstructor

public class DetallePedidoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle_pedido")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pedido", nullable = false)
    private PedidoEntity pedido;

    @Column(name = "id_producto_externo", nullable = false)
    private Integer idProductoExterno;

    @Column(name = "nombre_producto", nullable = false, length = 140)
    private String nombreProducto; // Snapshot

    @Column(name = "precio_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario; // Snapshot

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "subtotal_linea", nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotalLinea;


// Constructor completo explícito (reemplaza únicamente a @AllArgsConstructor)
  @Builder 
public DetallePedidoEntity(Integer id, PedidoEntity pedido, Integer idProductoExterno, 
                               String nombreProducto, BigDecimal precioUnitario, 
                               Integer cantidad, BigDecimal subtotalLinea) {
        this.id = id;
        this.pedido = pedido;
        this.idProductoExterno = idProductoExterno;
        this.nombreProducto = nombreProducto;
        this.precioUnitario = precioUnitario;
        this.cantidad = cantidad;
        this.subtotalLinea = subtotalLinea;
    }
}
