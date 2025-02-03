package com.pequenospasos.backend.repository;

import com.pequenospasos.backend.entity.Comida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ComidaRepository extends JpaRepository<Comida, Long> {

    // Buscar comidas registradas para un niño específico
    List<Comida> findByNinoId(Long ninoId);

    // Buscar comidas registradas por un educador específico
    List<Comida> findByEducadorId(Long educadorId);

    // Buscar comidas en un rango de fechas
    List<Comida> findByHoraComidaBetween(LocalDateTime inicio, LocalDateTime fin);
}