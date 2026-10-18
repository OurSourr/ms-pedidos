package cl.duoc.ms.pedidos.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "detalle_carrito")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class DetalleCarritoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle_carrito")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_carrito", nullable = false)
    private CarritoEntity carrito;

    @Column(name = "id_producto_externo", nullable = false)
    private Integer idProductoExterno;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;
}