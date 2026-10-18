package cl.duoc.ms.pedidos.repository;

import cl.duoc.ms.pedidos.entity.CarritoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CarritoRepository extends JpaRepository<CarritoEntity, Integer> {
    Optional<CarritoEntity> findByIdCuentaExterno(Integer idCuentaExterno);
}