package com.restaurante.tps.infrastructure.adapters.in.web;

import com.restaurante.tps.domain.model.Mesa;
import com.restaurante.tps.domain.ports.in.MesaUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mesas")
@CrossOrigin(origins = "*") // Habilita peticiones desde el frontend en Vite
public class MesaController {

    private final MesaUseCase mesaUseCase;

    public MesaController(MesaUseCase mesaUseCase) {
        this.mesaUseCase = mesaUseCase;
    }

    @GetMapping
    public ResponseEntity<List<Mesa>> obtenerMesas() {
        return ResponseEntity.ok(mesaUseCase.obtenerTodasLasMesas());
    }

    // Endpoint para el sistema de cobros (Libera la mesa)
    @PutMapping("/{id}/liberar")
    public ResponseEntity<Mesa> liberarMesa(@PathVariable Long id) {
        return ResponseEntity.ok(mesaUseCase.liberarMesa(id));
    }
}