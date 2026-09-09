package com.restaurante.tps.infrastructure.adapters.out.persistence;

import com.restaurante.tps.domain.model.EstadoMesa;
import com.restaurante.tps.domain.model.Mesa;
import com.restaurante.tps.domain.ports.out.MesaRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class MesaPersistenceAdapter implements MesaRepositoryPort {

    private final MesaJpaRepository repository;

    public MesaPersistenceAdapter(MesaJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Mesa> obtenerTodas() {
        return repository.findAll().stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<Mesa> obtenerPorId(Long id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public Mesa guardar(Mesa mesa) {
        MesaEntity entity = toEntity(mesa);
        return toDomain(repository.save(entity));
    }

    // Mappers manuales
    private Mesa toDomain(MesaEntity entity) {
        Mesa mesa = new Mesa();
        mesa.setId(entity.getId());
        mesa.setNumero(entity.getNumero());
        mesa.setEstado(EstadoMesa.valueOf(entity.getEstado()));
        return mesa;
    }

    private MesaEntity toEntity(Mesa mesa) {
        MesaEntity entity = new MesaEntity();
        entity.setId(mesa.getId());
        entity.setNumero(mesa.getNumero());
        entity.setEstado(mesa.getEstado().name());
        return entity;
    }
}