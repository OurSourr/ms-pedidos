package cl.duoc.ms.pedidos.controller;

import cl.duoc.ms.pedidos.dto.CheckoutRequest;
import cl.duoc.ms.pedidos.dto.PedidoConfirmadoResponse;
import cl.duoc.ms.pedidos.service.CheckoutService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/checkout")

public class CheckoutController {

    private final CheckoutService checkoutService;
 // Constructor explícito (reemplaza a @RequiredArgsConstructor)
    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }
    @PostMapping("/procesar")
    public ResponseEntity<PedidoConfirmadoResponse> procesarCheckout(
            @RequestHeader("X-User-Id") Integer idCuenta,
            @RequestBody CheckoutRequest request) {
        return ResponseEntity.ok(checkoutService.procesarCompra(idCuenta, request));
    }
}