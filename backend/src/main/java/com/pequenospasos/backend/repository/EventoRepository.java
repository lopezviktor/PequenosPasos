package com.pequenospasos.backend.repository;

import com.pequenospasos.backend.entity.Evento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {
    // Buscar eventos con paginación
    Page<Evento> findAll(Pageable pageable);

    // Buscar eventos por título con paginación
    Page<Evento> findByTituloContainingIgnoreCase(String titulo, Pageable pageable);

    // Buscar eventos creados por un usuario específico con paginación
    Page<Evento> findByCreadorId(Long creadorId, Pageable pageable);

    // Buscar eventos en un rango de fechas con paginación
    Page<Evento> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin, Pageable pageable);

    // Buscar eventos futuros con paginación
    Page<Evento> findByFechaHoraAfter(LocalDateTime fecha, Pageable pageable);
}