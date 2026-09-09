package com.restaurante.tps.domain.model;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class Platillo {
    private Long id;
    private String nombre;
    private String categoria;
    private BigDecimal precio;
    private String descripcion;
}