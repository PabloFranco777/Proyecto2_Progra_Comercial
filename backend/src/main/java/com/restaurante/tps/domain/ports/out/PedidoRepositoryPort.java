package com.restaurante.tps.domain.ports.out;

import com.restaurante.tps.domain.model.Pedido;
import java.util.List;
import java.util.Optional;

public interface PedidoRepositoryPort {
    Pedido guardar(Pedido pedido);
    List<Pedido> obtenerActivos();
    Optional<Pedido> obtenerPorId(Long id);
    void actualizarEstado(Long id, String estado); // <-- Agrega este método
}