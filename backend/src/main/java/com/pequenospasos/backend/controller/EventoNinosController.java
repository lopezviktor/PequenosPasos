package com.pequenospasos.backend.controller;

import com.pequenospasos.backend.entity.EventoNinos;
import com.pequenospasos.backend.service.EventoNinosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/evento-ninos")
public class EventoNinosController {

    @Autowired
    private EventoNinosService eventoNinosService;

    // Obtener todas las relaciones evento-niño con validación de existencia
    @GetMapping
    public List<EventoNinos> getAllEventoNinos() {
        List<EventoNinos> eventoNinos = eventoNinosService.getAllEventoNinos();
        if (eventoNinos.isEmpty()) {
            throw new RuntimeException("No hay niños registrados en eventos.");
        }
        return eventoNinos;
    }

    // Obtener todos los eventos en los que participa un niño
    @GetMapping("/nino/{ninoId}")
    public List<EventoNinos> getEventosByNinoId(@PathVariable Long ninoId) {
        List<EventoNinos> eventos = eventoNinosService.getEventosByNinoId(ninoId);
        if (eventos.isEmpty()) {
            throw new RuntimeException("No se encontraron eventos para el niño con id: " + ninoId);
        }
        return eventos;
    }

    // Obtener todos los niños que participan en un evento específico
    @GetMapping("/evento/{eventoId}")
    public List<EventoNinos> getNinosByEventoId(@PathVariable Long eventoId) {
        return eventoNinosService.getNinosByEventoId(eventoId);
    }

    // Registrar un niño en un evento evitando duplicados
    @PostMapping
    public EventoNinos saveEventoNino(@RequestBody EventoNinos eventoNinos) {
        return eventoNinosService.saveEventoNino(eventoNinos);
    }

    // Eliminar una relación evento-niño por ID con validación
    @DeleteMapping("/{id}")
    public void deleteEventoNino(@PathVariable Long id) {
        eventoNinosService.getAllEventoNinos().stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Relación Evento-Niño no encontrada con id: " + id));

        eventoNinosService.deleteEventoNino(id);
    }

    // Eliminar una relación evento-niño por evento y niño con validación
    @DeleteMapping
    public void deleteEventoNinoByEventoAndNino(@RequestParam Long eventoId, @RequestParam Long ninoId) {
        List<EventoNinos> relaciones = eventoNinosService.getEventosByNinoId(ninoId);
        boolean existe = relaciones.stream().anyMatch(e -> e.getEvento().getId().equals(eventoId));

        if (!existe) {
            throw new RuntimeException("No existe una relación entre el evento y el niño con esos IDs.");
        }

        eventoNinosService.deleteEventoNinoByEventoAndNino(eventoId, ninoId);
    }

    @PutMapping("/confirmar-asistencia")
    public EventoNinos confirmarAsistencia(@RequestBody ConfirmarAsistenciaRequest request) {
        return eventoNinosService.confirmarAsistencia(request.getEventoId(), request.getNinoId());
    }
    static class ConfirmarAsistenciaRequest {
        private Long eventoId;
        private Long ninoId;

        public Long getEventoId() {
            return eventoId;
        }

        public void setEventoId(Long eventoId) {
            this.eventoId = eventoId;
        }

        public Long getNinoId() {
            return ninoId;
        }

        public void setNinoId(Long ninoId) {
            this.ninoId = ninoId;
        }
    }
}