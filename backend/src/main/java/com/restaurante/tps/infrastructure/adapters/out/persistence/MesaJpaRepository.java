package com.restaurante.tps.infrastructure.adapters.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MesaJpaRepository extends JpaRepository<MesaEntity, Long> {
}