package cl.duoc.ms.pedidos.controller;

import cl.duoc.ms.pedidos.dto.AgregarItemRequest;
import cl.duoc.ms.pedidos.dto.CarritoResponse;
import cl.duoc.ms.pedidos.service.CarritoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/carrito")

public class CarritoController {

    private final CarritoService carritoService;

    // Constructor explícito (remplaza a @RequiredArgsConstructor de Lombok)
    public CarritoController(CarritoService carritoService) {
        this.carritoService = carritoService;
    }
//Este endpoint obtiene el carrito del usuario, si no existe, lo crea y devuelve
    @GetMapping
    public ResponseEntity<CarritoResponse> obtenerCarrito(@RequestHeader("X-User-Id") Integer idCuenta) {
        return ResponseEntity.ok(carritoService.obtenerOCrearCarrito(idCuenta));
    }

    @PostMapping("/items")
    public ResponseEntity<CarritoResponse> agregarItem(
            @RequestHeader("X-User-Id") Integer idCuenta,
            @RequestBody AgregarItemRequest request) {
        return ResponseEntity.ok(carritoService.agregarProducto(idCuenta, request));
    }

    @DeleteMapping("/items/{idProducto}")
    public ResponseEntity<CarritoResponse> eliminarItem(
            @RequestHeader("X-User-Id") Integer idCuenta,
            @PathVariable Integer idProducto) {
        return ResponseEntity.ok(carritoService.eliminarProducto(idCuenta, idProducto));
    }
}