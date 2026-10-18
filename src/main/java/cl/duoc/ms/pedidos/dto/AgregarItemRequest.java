package cl.duoc.ms.pedidos.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AgregarItemRequest {
    @NotNull(message = "El ID de producto es obligatorio")
    private Integer idProducto;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Integer cantidad;
}