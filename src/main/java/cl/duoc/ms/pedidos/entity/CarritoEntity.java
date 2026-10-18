package cl.duoc.ms.pedidos.entity;


import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carrito")
@Getter @Setter
@Builder
public class CarritoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_carrito")
    private Integer id;

    @Column(name = "id_cuenta_externo", nullable = false, unique = true)
    private Integer idCuentaExterno;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<DetalleCarritoEntity> detalles = new ArrayList<>();

// 1. Constructor vacío manual (reemplaza solo a @NoArgsConstructor)
    public CarritoEntity() {
    }

    // 2. Constructor completo manual (reemplaza solo a @AllArgsConstructor)
    public CarritoEntity(Integer id, Integer idCuentaExterno, LocalDateTime createdAt, LocalDateTime updatedAt, List<DetalleCarritoEntity> detalles) {
        this.id = id;
        this.idCuentaExterno = idCuentaExterno;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.detalles = detalles;
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