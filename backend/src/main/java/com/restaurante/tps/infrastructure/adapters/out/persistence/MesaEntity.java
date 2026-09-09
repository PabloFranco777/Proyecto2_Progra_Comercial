package com.restaurante.tps.infrastructure.adapters.out.persistence;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "mesas")
public class MesaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer numero;
    private String estado;
}