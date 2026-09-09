package com.restaurante.tps.infrastructure.adapters.in.web;

import com.restaurante.tps.domain.model.EstadoPedido;
import com.restaurante.tps.domain.model.Pedido;
import com.restaurante.tps.domain.ports.in.PedidoUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "*")
public class PedidoController {

    private final PedidoUseCase pedidoUseCase;
    private final SimpMessagingTemplate messagingTemplate; // Inyección de WebSocket

    public PedidoController(PedidoUseCase pedidoUseCase, SimpMessagingTemplate messagingTemplate) {
        this.pedidoUseCase = pedidoUseCase;
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping
    public ResponseEntity<Pedido> registrarPedido(@RequestBody Pedido pedido) {
        Pedido nuevoPedido = pedidoUseCase.registrarPedido(pedido);
        
        // Notifica a la cocina que hay un nuevo pedido en tiempo real
        messagingTemplate.convertAndSend("/topic/cocina", nuevoPedido);
        
        return ResponseEntity.ok(nuevoPedido);
    }

    @GetMapping("/activos")
    public ResponseEntity<List<Pedido>> obtenerPedidosActivos() {
        return ResponseEntity.ok(pedidoUseCase.obtenerPedidosActivos());
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Pedido> actualizarEstado(@PathVariable Long id, @RequestParam EstadoPedido estado) {
        Pedido pedidoActualizado = pedidoUseCase.actualizarEstadoPedido(id, estado);
        
        // Si la cocina marca el pedido como LISTO, notifica a la vista del mesero
        if (estado == EstadoPedido.LISTO) {
            String mensaje = "El pedido de la mesa " + pedidoActualizado.getMesaId() + " está LISTO para entregar.";
            messagingTemplate.convertAndSend("/topic/mesero", mensaje);
        }
        
        return ResponseEntity.ok(pedidoActualizado);
    }
}