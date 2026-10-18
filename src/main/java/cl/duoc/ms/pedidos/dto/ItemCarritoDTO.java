
package cl.duoc.ms.pedidos.dto;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data @Builder
@NoArgsConstructor 
public class ItemCarritoDTO {
    private Integer idProducto;
    private String nombreProducto;
    private BigDecimal precioUnitario;
    private Integer cantidad;
    private BigDecimal subtotalLinea;

// Constructor completo explícito con @Builder (reemplaza a @AllArgsConstructor)
    @Builder
    public ItemCarritoDTO(Integer idProducto, String nombreProducto, BigDecimal precioUnitario, 
                          Integer cantidad, BigDecimal subtotalLinea) {
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.precioUnitario = precioUnitario;
        this.cantidad = cantidad;
        this.subtotalLinea = subtotalLinea;
    }
}

