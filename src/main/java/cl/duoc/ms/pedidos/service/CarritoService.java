package cl.duoc.ms.pedidos.service;


import cl.duoc.ms.pedidos.dto.ProductoCatalogoDTO;
import cl.duoc.ms.pedidos.dto.AgregarItemRequest;
import cl.duoc.ms.pedidos.dto.CarritoResponse;
import cl.duoc.ms.pedidos.dto.ItemCarritoDTO;
import cl.duoc.ms.pedidos.entity.CarritoEntity;
import cl.duoc.ms.pedidos.entity.DetalleCarritoEntity;
import cl.duoc.ms.pedidos.repository.CarritoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CarritoService {

    private final CarritoRepository carritoRepository;
    private final WebClient webClient;

    // Constructor explícito (reemplaza a @RequiredArgsConstructor)
    public CarritoService(CarritoRepository carritoRepository, WebClient webClient) {
        this.carritoRepository = carritoRepository;
        this.webClient = webClient;
    }
    @Transactional
    public CarritoResponse obtenerOCrearCarrito(Integer idCuenta) {
        CarritoEntity carrito = carritoRepository.findByIdCuentaExterno(idCuenta)
                .orElseGet(() -> carritoRepository.save(CarritoEntity.builder().idCuentaExterno(idCuenta).build()));

        return construirResponse(carrito);
    }

    @Transactional
    public CarritoResponse agregarProducto(Integer idCuenta, AgregarItemRequest request) {
        CarritoEntity carrito = carritoRepository.findByIdCuentaExterno(idCuenta)
                .orElseGet(() -> carritoRepository.save(CarritoEntity.builder().idCuentaExterno(idCuenta).build()));

        // Llamada directa a ms-catalogo usando WebClient
       ProductoCatalogoDTO prod = webClient.get()
        .uri("http://localhost:8081/api/v1/productos/{id}", request.getIdProducto())
        .retrieve()
        .bodyToMono(ProductoCatalogoDTO.class)
        .block();
      
        if (prod == null || !prod.getActivo()) {
            throw new RuntimeException("El producto no existe o está inactivo");
        }

        // Buscar si ya está en el carrito
        DetalleCarritoEntity detalle = carrito.getDetalles().stream()
                .filter(d -> d.getIdProductoExterno().equals(request.getIdProducto()))
                .findFirst()
                .orElse(null);

        if (detalle != null) {
            detalle.setCantidad(detalle.getCantidad() + request.getCantidad());
        } else {
            carrito.getDetalles().add(DetalleCarritoEntity.builder()
                    .carrito(carrito)
                    .idProductoExterno(request.getIdProducto())
                    .cantidad(request.getCantidad())
                    .build());
        }

        return construirResponse(carritoRepository.save(carrito));
    }

    @Transactional
    public CarritoResponse eliminarProducto(Integer idCuenta, Integer idProducto) {
        CarritoEntity carrito = carritoRepository.findByIdCuentaExterno(idCuenta)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        carrito.getDetalles().removeIf(d -> d.getIdProductoExterno().equals(idProducto));
        return construirResponse(carritoRepository.save(carrito));
    }

    private CarritoResponse construirResponse(CarritoEntity carrito) {
        List<ItemCarritoDTO> items = new ArrayList<>();
        BigDecimal subtotalGeneral = BigDecimal.ZERO;

        for (DetalleCarritoEntity detalle : carrito.getDetalles()) {
            // Consultar precio actualizado en ms-catalogo (no se guarda precio en carrito)
           ProductoCatalogoDTO prod = webClient.get()
        .uri("http://localhost:8081/api/v1/productos/{id}", detalle.getIdProductoExterno())
        .retrieve()
        .bodyToMono(ProductoCatalogoDTO.class)
        .block();
            BigDecimal subtotalLinea = prod.getPrecio().multiply(BigDecimal.valueOf(detalle.getCantidad()));
            subtotalGeneral = subtotalGeneral.add(subtotalLinea);

            items.add(ItemCarritoDTO.builder()
                    .idProducto(prod.getIdProducto())
                    .nombreProducto(prod.getNombre())
                    .precioUnitario(prod.getPrecio())
                    .cantidad(detalle.getCantidad())
                    .subtotalLinea(subtotalLinea)
                    .build());
        }

        return CarritoResponse.builder()
                .idCarrito(carrito.getId())
                .items(items)
                .subtotal(subtotalGeneral)
                .build();
    }
}