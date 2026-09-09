package com.restaurante.tps.infrastructure.adapters.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface PedidoJpaRepository extends JpaRepository<PedidoEntity, Long> {

    List<PedidoEntity> findByEstadoNot(String estado);

    @Modifying
    @Transactional
    @Query("UPDATE PedidoEntity p SET p.estado = :estado WHERE p.id = :id")
    void actualizarEstadoPedido(@Param("id") Long id, @Param("estado") String estado);
}