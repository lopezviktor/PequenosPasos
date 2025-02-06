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

    // Obtener todas las relaciones evento-niño
    @GetMapping
    public List<EventoNinos> getAllEventoNinos() {
        return eventoNinosService.getAllEventoNinos();
    }

    // Obtener todos los eventos en los que participa un niño
    @GetMapping("/nino/{ninoId}")
    public List<EventoNinos> getEventosByNinoId(@PathVariable Long ninoId) {
        return eventoNinosService.getEventosByNinoId(ninoId);
    }

    // Obtener todos los niños que participan en un evento específico
    @GetMapping("/evento/{eventoId}")
    public List<EventoNinos> getNinosByEventoId(@PathVariable Long eventoId) {
        return eventoNinosService.getNinosByEventoId(eventoId);
    }

    // Registrar un niño en un evento
    @PostMapping
    public EventoNinos saveEventoNino(@RequestBody EventoNinos eventoNinos) {
        return eventoNinosService.saveEventoNino(eventoNinos);
    }

    // Eliminar una relación evento-niño por ID
    @DeleteMapping("/{id}")
    public void deleteEventoNino(@PathVariable Long id) {
        eventoNinosService.deleteEventoNino(id);
    }

    // Eliminar una relación evento-niño por evento y niño
    @DeleteMapping
    public void deleteEventoNinoByEventoAndNino(@RequestParam Long eventoId, @RequestParam Long ninoId) {
        eventoNinosService.deleteEventoNinoByEventoAndNino(eventoId, ninoId);
    }
}