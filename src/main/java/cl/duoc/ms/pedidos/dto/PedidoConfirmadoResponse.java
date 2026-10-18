package cl.duoc.ms.pedidos.dto;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data 
@NoArgsConstructor 
public class PedidoConfirmadoResponse {
    private Integer idPedido;
    private String estado;
    private BigDecimal subtotal;
    private BigDecimal costoDespacho;
    private BigDecimal total;
    private String resultadoPago;
    private String referenciaPago;
    private LocalDateTime fechaCompra;


    // Constructor completo explícito con @Builder (reemplaza a @AllArgsConstructor)
    @Builder
    public PedidoConfirmadoResponse(Integer idPedido, String estado, BigDecimal subtotal, 
                                    BigDecimal costoDespacho, BigDecimal total, 
                                    String resultadoPago, String referenciaPago, 
                                    LocalDateTime fechaCompra) {
        this.idPedido = idPedido;
        this.estado = estado;
        this.subtotal = subtotal;
        this.costoDespacho = costoDespacho;
        this.total = total;
        this.resultadoPago = resultadoPago;
        this.referenciaPago = referenciaPago;
        this.fechaCompra = fechaCompra;
    }
}
