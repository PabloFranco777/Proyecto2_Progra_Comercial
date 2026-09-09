package com.restaurante.tps.domain.ports.out;

import com.restaurante.tps.domain.model.Platillo;
import java.util.List;

public interface PlatilloRepositoryPort {
    List<Platillo> obtenerTodos();
}