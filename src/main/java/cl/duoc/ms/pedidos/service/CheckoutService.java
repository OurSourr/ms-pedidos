package cl.duoc.ms.pedidos.service;



//import cl.duoc.ms.pedidos.client.dto.ProductoCatalogoDTO;
import cl.duoc.ms.pedidos.dto.CheckoutRequest;
import cl.duoc.ms.pedidos.dto.PedidoConfirmadoResponse;
import cl.duoc.ms.pedidos.dto.ProductoCatalogoDTO;
import cl.duoc.ms.pedidos.entity.*;
import cl.duoc.ms.pedidos.repository.CarritoRepository;
import cl.duoc.ms.pedidos.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import java.math.BigDecimal;
import java.util.UUID;

@Service

public class CheckoutService {

private final CarritoRepository carritoRepository;
    private final PedidoRepository pedidoRepository;
    private final WebClient webClient;

// Constructor explícito (reemplaza a @RequiredArgsConstructor)
    public CheckoutService(CarritoRepository carritoRepository, 
                           PedidoRepository pedidoRepository, 
                           WebClient webClient) {
        this.carritoRepository = carritoRepository;
        this.pedidoRepository = pedidoRepository;
        this.webClient = webClient;
    }
    @Transactional
    public PedidoConfirmadoResponse procesarCompra(Integer idCuenta, CheckoutRequest request) {
        CarritoEntity carrito = carritoRepository.findByIdCuentaExterno(idCuenta)
                .orElseThrow(() -> new RuntimeException("No existe un carrito activo para este usuario"));

        if (carrito.getDetalles().isEmpty()) {
            throw new RuntimeException("El carrito se encuentra vacío");
        }

        BigDecimal subtotal = BigDecimal.ZERO;

        // 1. Crear la orden de Pedido en estado PENDIENTE_PAGO
        PedidoEntity pedido = PedidoEntity.builder()
                .idCuentaExterno(idCuenta)
                .estado("PENDIENTE_PAGO")
                .nombreDestinatario(request.getNombreDestinatario())
                .calle(request.getCalle())
                .numero(request.getNumero())
                .deptoCasa(request.getDeptoCasa())
                .comuna(request.getComuna())
                .region(request.getRegion())
                .telefono(request.getTelefono())
                .referencia(request.getReferencia())
                .subtotal(BigDecimal.ZERO)
                .costoDespacho(BigDecimal.ZERO)
                .total(BigDecimal.ZERO)
                .build();

        // 2. Crear los Snapshots del detalle del pedido revalidando stock
        for (DetalleCarritoEntity item : carrito.getDetalles()) {
            ProductoCatalogoDTO prod = webClient.get()
                    .uri("/api/v1/productos/{id}", item.getIdProductoExterno())
                    .retrieve()
                    .bodyToMono(ProductoCatalogoDTO.class)
                    .block();

            if (prod == null || !prod.getActivo()) {
                throw new RuntimeException("El producto ID " + item.getIdProductoExterno() + " no está disponible");
            }

            if (prod.getStock() < item.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para: " + prod.getNombre());
            }

            BigDecimal subtotalLinea = prod.getPrecio().multiply(BigDecimal.valueOf(item.getCantidad()));
            subtotal = subtotal.add(subtotalLinea);

            DetallePedidoEntity detallePedido = DetallePedidoEntity.builder()
                    .pedido(pedido)
                    .idProductoExterno(prod.getIdProducto())
                    .nombreProducto(prod.getNombre())      // Snapshot inmutable
                    .precioUnitario(prod.getPrecio())       // Snapshot inmutable
                    .cantidad(item.getCantidad())
                    .subtotalLinea(subtotalLinea)
                    .build();

            pedido.getDetalles().add(detallePedido);
        }

        // 3. Regla de negocio para cobro de despacho
        BigDecimal costoDespacho = calcularCostoDespacho(request.getRegion(), subtotal);
        BigDecimal total = subtotal.add(costoDespacho);

        pedido.setSubtotal(subtotal);
        pedido.setCostoDespacho(costoDespacho);
        pedido.setTotal(total);

        // 4. Simulación de Pago 
        boolean pagoExitoso = Math.random() < 0.90;

        PagoSimuladoEntity pago = PagoSimuladoEntity.builder()
                .pedido(pedido)
                .monto(total)
                .resultado(pagoExitoso ? "APROBADO" : "RECHAZADO")
                .referenciaSimulada("REF-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .build();

        pedido.setPagoSimulado(pago);

        if (pagoExitoso) {
            pedido.setEstado("CONFIRMADO");
// Descontar stock en ms-catalogo mediante POST con WebClient
            for (DetalleCarritoEntity item : carrito.getDetalles()) {
                webClient.post()
                        .uri(uriBuilder -> uriBuilder
                                .path("/api/v1/productos/{id}/descontar-stock")
                                .queryParam("cantidad", item.getCantidad())
                                .build(item.getIdProductoExterno()))
                        .retrieve()
                        .bodyToMono(Void.class)
                        .block();
            }

            // Vaciar las líneas del carrito
            carrito.getDetalles().clear();
            carritoRepository.save(carrito);
        } else {
            pedido.setEstado("RECHAZADO");
        }

        PedidoEntity pedidoGuardado = pedidoRepository.save(pedido);

        return PedidoConfirmadoResponse.builder()
                .idPedido(pedidoGuardado.getId())
                .estado(pedidoGuardado.getEstado())
                .subtotal(subtotal)
                .costoDespacho(costoDespacho)
                .total(total)
                .resultadoPago(pago.getResultado())
                .referenciaPago(pago.getReferenciaSimulada())
                .fechaCompra(pedidoGuardado.getCreatedAt())
                .build();
    }

    private BigDecimal calcularCostoDespacho(String region, BigDecimal subtotal) {
        // Envío gratuito para compras de $50.000 o superiores
        if (subtotal.compareTo(new BigDecimal("50000")) >= 0) {
            return BigDecimal.ZERO;
        }

        if (region.equalsIgnoreCase("Región Metropolitana") || region.equalsIgnoreCase("RM")) {
            return new BigDecimal("3990");
        } else {
            return new BigDecimal("6990");
        }
    }
}