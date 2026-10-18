package cl.duoc.ms.pedidos.dto;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;


@Data @Builder
@NoArgsConstructor
public class CarritoResponse {
    private Integer idCarrito;
    private List<ItemCarritoDTO> items;
    private BigDecimal subtotal;


    // Constructor completo explícito con @Builder (reemplaza a @AllArgsConstructor)
    @Builder
    public CarritoResponse(Integer idCarrito, List<ItemCarritoDTO> items, BigDecimal subtotal) {
        this.idCarrito = idCarrito;
        this.items = items;
        this.subtotal = subtotal;
    }
}