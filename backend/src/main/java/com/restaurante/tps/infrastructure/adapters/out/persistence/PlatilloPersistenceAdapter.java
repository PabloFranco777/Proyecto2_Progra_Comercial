package com.restaurante.tps.infrastructure.adapters.out.persistence;

import com.restaurante.tps.domain.model.Platillo;
import com.restaurante.tps.domain.ports.out.PlatilloRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PlatilloPersistenceAdapter implements PlatilloRepositoryPort {

    private final PlatilloJpaRepository repository;

    public PlatilloPersistenceAdapter(PlatilloJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Platillo> obtenerTodos() {
        return repository.findAll().stream().map(this::toDomain).collect(Collectors.toList());
    }

    private Platillo toDomain(PlatilloEntity entity) {
        Platillo platillo = new Platillo();
        platillo.setId(entity.getId());
        platillo.setNombre(entity.getNombre());
        platillo.setPrecio(entity.getPrecio());
        platillo.setDescripcion(entity.getDescripcion());
        platillo.setCategoria(entity.getCategoria());
        return platillo;
    }
}