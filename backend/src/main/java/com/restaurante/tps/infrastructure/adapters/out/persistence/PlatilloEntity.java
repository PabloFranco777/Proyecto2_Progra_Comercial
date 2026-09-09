package com.restaurante.tps.infrastructure.adapters.out.persistence;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "platillos")
public class PlatilloEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String categoria;
    private BigDecimal precio;
    private String descripcion;
    private Integer stock;
}