package com.restaurante.tps.infrastructure.adapters.out.persistence;

import com.restaurante.tps.domain.model.EstadoPedido;
import com.restaurante.tps.domain.model.Pedido;
import com.restaurante.tps.domain.ports.out.PedidoRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PedidoPersistenceAdapter implements PedidoRepositoryPort {

    private final PedidoJpaRepository repository;

    public PedidoPersistenceAdapter(PedidoJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Pedido guardar(Pedido pedido) {
        PedidoEntity entity = toEntity(pedido);
        return toDomain(repository.save(entity));
    }

    @Override
    public List<Pedido> obtenerActivos() {
        return repository.findByEstadoNot("PAGADO")
                .stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<Pedido> obtenerPorId(Long id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public void actualizarEstado(Long id, String estado) {
        // Ejecuta un UPDATE directo en SQL sin tocar la tabla intermedia de platillos
        repository.actualizarEstadoPedido(id, estado);
    }

    private Pedido toDomain(PedidoEntity entity) {
        Pedido pedido = new Pedido();
        pedido.setId(entity.getId());
        pedido.setMesaId(entity.getMesaId());
        pedido.setInstruccionesEspeciales(entity.getInstruccionesEspeciales());
        pedido.setEstado(EstadoPedido.valueOf(entity.getEstado()));
        pedido.setFechaCreacion(entity.getFechaCreacion());
        
        if (entity.getPlatillos() != null) {
            pedido.setPlatillos(entity.getPlatillos().stream().map(pe -> {
                com.restaurante.tps.domain.model.Platillo p = new com.restaurante.tps.domain.model.Platillo();
                p.setId(pe.getId());
                p.setNombre(pe.getNombre());
                p.setCategoria(pe.getCategoria());
                return p;
            }).collect(Collectors.toList()));
        }
        return pedido;
    }

    private PedidoEntity toEntity(Pedido pedido) {
        PedidoEntity entity = new PedidoEntity();
        entity.setId(pedido.getId());
        entity.setMesaId(pedido.getMesaId());
        entity.setInstruccionesEspeciales(pedido.getInstruccionesEspeciales());
        entity.setEstado(pedido.getEstado().name());
        entity.setFechaCreacion(pedido.getFechaCreacion());
        
        if (pedido.getPlatillos() != null) {
            entity.setPlatillos(pedido.getPlatillos().stream().map(p -> {
                PlatilloEntity pe = new PlatilloEntity();
                pe.setId(p.getId());
                return pe;
            }).collect(Collectors.toList()));
        }
        return entity;
    }
}