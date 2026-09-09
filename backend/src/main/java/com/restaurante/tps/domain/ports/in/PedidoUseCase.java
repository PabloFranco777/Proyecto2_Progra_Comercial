package com.restaurante.tps.domain.ports.in;
import com.restaurante.tps.domain.model.Pedido;
import com.restaurante.tps.domain.model.EstadoPedido;
import java.util.List;

public interface PedidoUseCase {
    Pedido registrarPedido(Pedido pedido); // El mesero registra el pedido
    Pedido actualizarEstadoPedido(Long id, EstadoPedido nuevoEstado); // La cocina marca como listo
    List<Pedido> obtenerPedidosActivos(); // Vista en tiempo real
}