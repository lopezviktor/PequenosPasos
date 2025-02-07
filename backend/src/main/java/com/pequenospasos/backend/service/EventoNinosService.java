package com.pequenospasos.backend.service;

import com.pequenospasos.backend.entity.EventoNinos;
import com.pequenospasos.backend.repository.EventoNinosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventoNinosService {

    @Autowired
    private EventoNinosRepository eventoNinosRepository;

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
        Optional<EventoNinos> existente = eventoNinosRepository.findByEventoIdAndNinoId(eventoNinos.getEvento().getId(), eventoNinos.getNino().getId());
        if (existente.isPresent()) {
            throw new RuntimeException("El niño ya está registrado en este evento.");
        }
        return eventoNinosRepository.save(eventoNinos);
    }

    // Eliminar una relación evento-niño por ID
    public void deleteEventoNino(Long id) {
        eventoNinosRepository.deleteById(id);
    }

    // Eliminar una relación evento-niño por IDs de evento y niño
    public void deleteEventoNinoByEventoAndNino(Long eventoId, Long ninoId) {
        eventoNinosRepository.deleteByEventoIdAndNinoId(eventoId, ninoId);
    }
}