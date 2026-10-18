package cl.duoc.ms.pedidos.repository;


import cl.duoc.ms.pedidos.entity.PedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PedidoRepository extends JpaRepository<PedidoEntity, Integer> {
    List<PedidoEntity> findByIdCuentaExternoOrderByCreatedAtDesc(Integer idCuentaExterno);
}