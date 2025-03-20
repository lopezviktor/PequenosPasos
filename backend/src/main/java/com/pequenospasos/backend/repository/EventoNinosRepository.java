package com.pequenospasos.backend.repository;
import org.springframework.data.jpa.repository.Query;

import com.pequenospasos.backend.entity.EventoNinos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventoNinosRepository extends JpaRepository<EventoNinos, Long> {

    // Buscar todos los eventos en los que participa un niño específico
    List<EventoNinos> findByNinoId(Long ninoId);

    // Buscar todos los niños que participan en un evento específico
    List<EventoNinos> findByEventoId(Long eventoId);

    void deleteByEventoIdAndNinoId(Long eventoId, Long ninoId);

    List<EventoNinos> findByEventoIdAndAsistioTrue(Long eventoId);

    @Query("SELECT e FROM EventoNinos e WHERE e.evento.id = ?1 AND e.nino.id = ?2")
    Optional<EventoNinos> findByEventoIdAndNinoId(Long eventoId, Long ninoId);
}