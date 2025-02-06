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

    // Guardar una nueva relación entre evento y niño
    public EventoNinos saveEventoNino(EventoNinos eventoNinos) {
        return eventoNinosRepository.save(eventoNinos);
    }

    // Eliminar una relación evento-niño
    public void deleteEventoNino(Long id) {
        eventoNinosRepository.deleteById(id);
    }

    public void deleteEventoNinoByEventoAndNino(Long eventoId, Long ninoId) {
        eventoNinosRepository.deleteByEventoIdAndNinoId(eventoId, ninoId);
    }
}