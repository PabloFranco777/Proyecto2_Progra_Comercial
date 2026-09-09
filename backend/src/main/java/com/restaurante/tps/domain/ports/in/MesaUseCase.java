package com.restaurante.tps.domain.ports.in;
import com.restaurante.tps.domain.model.Mesa;
import java.util.List;

public interface MesaUseCase {
    List<Mesa> obtenerTodasLasMesas();
    Mesa liberarMesa(Long id); // Parte del requerimiento: "sistema de cobros para liberar mesa"
}