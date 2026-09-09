package com.restaurante.tps.domain.ports.out;
import com.restaurante.tps.domain.model.Mesa;
import java.util.List;
import java.util.Optional;

public interface MesaRepositoryPort {
    List<Mesa> obtenerTodas();
    Optional<Mesa> obtenerPorId(Long id);
    Mesa guardar(Mesa mesa);
}