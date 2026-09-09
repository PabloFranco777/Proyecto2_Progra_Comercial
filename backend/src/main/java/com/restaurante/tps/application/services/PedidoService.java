package com.restaurante.tps.application.services;

import com.restaurante.tps.domain.model.EstadoMesa;
import com.restaurante.tps.domain.model.EstadoPedido;
import com.restaurante.tps.domain.model.Mesa;
import com.restaurante.tps.domain.model.Pedido;
import com.restaurante.tps.domain.ports.in.PedidoUseCase;
import com.restaurante.tps.domain.ports.out.MesaRepositoryPort;
import com.restaurante.tps.domain.ports.out.PedidoRepositoryPort;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PedidoService implements PedidoUseCase {

    private final PedidoRepositoryPort pedidoRepositoryPort;
    private final MesaRepositoryPort mesaRepositoryPort;

    // INYECCIÓN DE DEPENDENCIAS múltiple
    public PedidoService(PedidoRepositoryPort pedidoRepositoryPort, MesaRepositoryPort mesaRepositoryPort) {
        this.pedidoRepositoryPort = pedidoRepositoryPort;
        this.mesaRepositoryPort = mesaRepositoryPort;
    }

    @Override
    public Pedido registrarPedido(Pedido pedido) {
        // 1. Validar que la mesa exista
        Mesa mesa = mesaRepositoryPort.obtenerPorId(pedido.getMesaId())
                .orElseThrow(() -> new RuntimeException("La mesa no existe"));

        // 2. Cambiar estado de la mesa a ocupada
        if (mesa.getEstado() == EstadoMesa.LIBRE) {
            mesa.setEstado(EstadoMesa.OCUPADA);
            mesaRepositoryPort.guardar(mesa);
        }

        // 3. Configurar pedido inicial
        pedido.setEstado(EstadoPedido.PENDIENTE);
        pedido.setFechaCreacion(LocalDateTime.now());
        
        return pedidoRepositoryPort.guardar(pedido);
    }

    @Override
    public Pedido actualizarEstadoPedido(Long id, EstadoPedido nuevoEstado) {
        Pedido pedido = pedidoRepositoryPort.obtenerPorId(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        
        pedido.setEstado(nuevoEstado);
        return pedidoRepositoryPort.guardar(pedido);
    }

    @Override
    public List<Pedido> obtenerPedidosActivos() {
        return pedidoRepositoryPort.obtenerActivos();
    }
}