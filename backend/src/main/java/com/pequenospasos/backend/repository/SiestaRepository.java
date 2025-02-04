package com.pequenospasos.backend.repository;

import com.pequenospasos.backend.entity.Siesta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SiestaRepository extends JpaRepository<Siesta, Long> {

    // Buscar siestas de un niño específico
    List<Siesta> findByNinoId(Long ninoId);

    // Buscar siestas registradas por un educador específico
    List<Siesta> findByEducadorId(Long educadorId);

    // Buscar siestas en un rango de fechas
    List<Siesta> findByHoraInicioBetween(LocalDateTime inicio, LocalDateTime fin);

    // Ultima siesta del niño
    Siesta findTopByNinoIdOrderByHoraInicioDesc(Long ninoId);
}