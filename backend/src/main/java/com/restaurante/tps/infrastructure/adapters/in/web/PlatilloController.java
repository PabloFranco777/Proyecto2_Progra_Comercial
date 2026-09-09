package com.restaurante.tps.infrastructure.adapters.in.web;
import com.restaurante.tps.domain.model.Platillo;
import com.restaurante.tps.domain.ports.in.PlatilloUseCase;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/platillos")
@CrossOrigin("*")
public class PlatilloController {
    private final PlatilloUseCase useCase;
    public PlatilloController(PlatilloUseCase useCase) { this.useCase = useCase; }
    @GetMapping public List<Platillo> obtenerMenu() { return useCase.obtenerMenu(); }
}