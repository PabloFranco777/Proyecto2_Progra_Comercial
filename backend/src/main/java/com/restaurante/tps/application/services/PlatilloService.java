package com.restaurante.tps.application.services;

import com.restaurante.tps.domain.model.Platillo;
import com.restaurante.tps.domain.ports.in.PlatilloUseCase;
import com.restaurante.tps.domain.ports.out.PlatilloRepositoryPort;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PlatilloService implements PlatilloUseCase {
    
    private final PlatilloRepositoryPort port;
    
    public PlatilloService(PlatilloRepositoryPort port) { 
        this.port = port; 
    }
    
    @Override 
    public List<Platillo> obtenerMenu() { 
        return port.obtenerTodos(); 
    }
}