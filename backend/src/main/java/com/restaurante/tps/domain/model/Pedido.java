package com.restaurante.tps.domain.model;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class Pedido {
    private Long id;
    private Long mesaId;
    private String instruccionesEspeciales;
    private EstadoPedido estado;
    private LocalDateTime fechaCreacion;
    private List<Platillo> platillos;
}