package com.pequenospasos.backend.repository;

import com.pequenospasos.backend.entity.EventoNinos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventoNinosRepository extends JpaRepository<EventoNinos, Long> {

    // Buscar todos los eventos en los que participa un niño específico
    List<EventoNinos> findByNinoId(Long ninoId);

    // Buscar todos los niños que participan en un evento específico
    List<EventoNinos> findByEventoId(Long eventoId);
}