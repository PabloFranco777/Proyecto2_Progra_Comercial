package com.restaurante.tps.domain.model;
import lombok.Data;

@Data
public class Mesa {
    private Long id;
    private Integer numero;
    private EstadoMesa estado;
}