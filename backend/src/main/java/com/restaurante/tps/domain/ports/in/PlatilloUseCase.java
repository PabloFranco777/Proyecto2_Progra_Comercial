package com.restaurante.tps.domain.ports.in;

import com.restaurante.tps.domain.model.Platillo;
import java.util.List;

public interface PlatilloUseCase {
    List<Platillo> obtenerMenu();
}