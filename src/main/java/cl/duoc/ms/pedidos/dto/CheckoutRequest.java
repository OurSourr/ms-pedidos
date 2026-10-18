package cl.duoc.ms.pedidos.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class CheckoutRequest {
    @NotBlank(message = "El nombre del destinatario es obligatorio")
    private String nombreDestinatario;

    @NotBlank(message = "La calle es obligatoria")
    private String calle;

    @NotBlank(message = "El número es obligatorio")
    private String numero;

    private String deptoCasa;

    @NotBlank(message = "La comuna es obligatoria")
    private String comuna;

    @NotBlank(message = "La región es obligatoria")
    private String region;

    @NotBlank(message = "El teléfono es obligatorio")
    private String telefono;

    private String referencia;
}