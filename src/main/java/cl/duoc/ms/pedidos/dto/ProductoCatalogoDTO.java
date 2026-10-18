package cl.duoc.ms.pedidos.dto;


import lombok.Data;
import java.math.BigDecimal;

@Data

public class ProductoCatalogoDTO {
    private Integer idProducto;
    private String nombre;
    private BigDecimal precio;
    private Integer stock;
    private Boolean activo;
}