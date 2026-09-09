package com.restaurante.tps.infrastructure.adapters.out.persistence;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "pedidos")
public class PedidoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "mesa_id")
    private Long mesaId;
    
    @Column(name = "instrucciones_especiales")
    private String instruccionesEspeciales;
    
    private String estado;
    
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(
        name = "pedido_platillos",
        joinColumns = @JoinColumn(name = "pedido_id"),
        inverseJoinColumns = @JoinColumn(name = "platillo_id")
    )
    private List<PlatilloEntity> platillos;
}