package com.restaurante.tps.application.services;

import com.restaurante.tps.domain.model.EstadoMesa;
import com.restaurante.tps.domain.model.Mesa;
import com.restaurante.tps.domain.ports.in.MesaUseCase;
import com.restaurante.tps.domain.ports.out.MesaRepositoryPort;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MesaService implements MesaUseCase {

    private final MesaRepositoryPort mesaRepositoryPort;

    // INYECCIÓN DE DEPENDENCIAS por constructor (Requerimiento del proyecto)
    public MesaService(MesaRepositoryPort mesaRepositoryPort) {
        this.mesaRepositoryPort = mesaRepositoryPort;
    }

    @Override
    public List<Mesa> obtenerTodasLasMesas() {
        return mesaRepositoryPort.obtenerTodas();
    }

    @Override
    public Mesa liberarMesa(Long id) {
        Mesa mesa = mesaRepositoryPort.obtenerPorId(id)
                .orElseThrow(() -> new RuntimeException("Mesa no encontrada"));
        
        mesa.setEstado(EstadoMesa.LIBRE);
        return mesaRepositoryPort.guardar(mesa);
    }
}