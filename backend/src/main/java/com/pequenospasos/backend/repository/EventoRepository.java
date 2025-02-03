package com.pequenospasos.backend.repository;

import com.pequenospasos.backend.entity.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {

    // Buscar eventos por título (ignorando mayúsculas y minúsculas)
    List<Evento> findByTituloContainingIgnoreCase(String titulo);

    // Buscar eventos creados por un usuario específico (educador o administrador)
    List<Evento> findByCreadorId(Long creadorId);

    // Buscar eventos en un rango de fechas
    List<Evento> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin);
}