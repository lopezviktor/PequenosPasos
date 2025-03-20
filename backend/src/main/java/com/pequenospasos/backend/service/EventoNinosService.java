package com.pequenospasos.backend.service;

import com.pequenospasos.backend.entity.Evento;
import com.pequenospasos.backend.entity.EventoNinos;
import com.pequenospasos.backend.repository.EventoNinosRepository;
import com.pequenospasos.backend.repository.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EventoNinosService {

    @Autowired
    private EventoNinosRepository eventoNinosRepository;

    @Autowired
    private EventoRepository eventoRepository;

    // Obtener todas las relaciones evento-niños
    public List<EventoNinos> getAllEventoNinos() {
        return eventoNinosRepository.findAll();
    }

    // Obtener todos los eventos en los que participa un niño específico
    public List<EventoNinos> getEventosByNinoId(Long ninoId) {
        return eventoNinosRepository.findByNinoId(ninoId);
    }

    // Obtener todos los niños que participan en un evento específico
    public List<EventoNinos> getNinosByEventoId(Long eventoId) {
        return eventoNinosRepository.findByEventoId(eventoId);
    }

    // Guardar una nueva relación entre evento y niño (evitando duplicados)
    public EventoNinos saveEventoNino(EventoNinos eventoNinos) {
        if (eventoNinos.getEvento() == null || eventoNinos.getEvento().getId() == null) {
            throw new RuntimeException("El evento proporcionado es nulo o no tiene un ID válido.");
        }

        Evento evento = eventoRepository.findById(eventoNinos.getEvento().getId())
                .orElseThrow(() -> new RuntimeException("No se encontró el evento con ID: " + eventoNinos.getEvento().getId()));

        // Asignamos el evento recuperado a la entidad eventoNinos
        eventoNinos.setEvento(evento);

        if (evento.getFechaHora() == null) {
            throw new RuntimeException("El evento no tiene una fecha asignada.");
        }

        if (evento.getFechaHora().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("No se pueden inscribir niños en eventos pasados.");
        }

        Optional<EventoNinos> existenteOpt = eventoNinosRepository.findByEventoIdAndNinoId(evento.getId(), eventoNinos.getNino().getId());

        if (existenteOpt.isPresent()) {
            EventoNinos existente = existenteOpt.get();
            existente.setAsistio(true);
            return eventoNinosRepository.save(existente);
        } else {
            return eventoNinosRepository.save(eventoNinos);
        }
    }

    // Eliminar una relación evento-niño por ID
    public void deleteEventoNino(Long id) {
        eventoNinosRepository.deleteById(id);
    }

    // Eliminar una relación evento-niño por IDs de evento y niño
    public void deleteEventoNinoByEventoAndNino(Long eventoId, Long ninoId) {
        eventoNinosRepository.deleteByEventoIdAndNinoId(eventoId, ninoId);
    }

    public EventoNinos confirmarAsistencia(Long eventoId, Long ninoId) {
        EventoNinos eventoNino = eventoNinosRepository.findByEventoIdAndNinoId(eventoId, ninoId)
                .orElseThrow(() -> new RuntimeException("No se encontró la relación evento-niño."));

        if (eventoNino.getEvento().getFechaHora().isAfter(LocalDateTime.now())) {
            throw new RuntimeException("El evento aún no ha ocurrido, no se puede confirmar asistencia.");
        }
        if (eventoNino.getAsistio()) {
            throw new IllegalStateException("La asistencia ya ha sido confirmada.");
        }

        eventoNino.setAsistio(true);
        return eventoNinosRepository.save(eventoNino);
    }
}