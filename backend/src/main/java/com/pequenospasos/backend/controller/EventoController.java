package com.pequenospasos.backend.controller;

import com.pequenospasos.backend.entity.Evento;
import com.pequenospasos.backend.service.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/eventos")
public class EventoController {

    @Autowired
    private EventoService eventoService;

    // Obtener todos los eventos
    @GetMapping
    public List<Evento> getAllEventos() {
        return eventoService.getAllEventos();
    }

    // Obtener eventos por título
    @GetMapping("/buscar")
    public List<Evento> getEventosByTitulo(@RequestParam String titulo) {
        return eventoService.getEventosByTitulo(titulo);
    }

    // Obtener eventos por creador
    @GetMapping("/creador/{creadorId}")
    public List<Evento> getEventosByCreador(@PathVariable Long creadorId) {
        return eventoService.getEventosByCreador(creadorId);
    }

    // Obtener eventos en un rango de fechas
    @GetMapping("/rango-fechas")
    public List<Evento> getEventosByFecha(@RequestParam LocalDateTime inicio, @RequestParam LocalDateTime fin) {
        return eventoService.getEventosByFecha(inicio, fin);
    }

    // Obtener un evento por ID
    @GetMapping("/{id}")
    public Optional<Evento> getEventoById(@PathVariable Long id) {
        return eventoService.getEventoById(id);
    }

    // Crear un nuevo evento
    @PostMapping
    public Evento createEvento(@RequestBody Evento evento) {
        return eventoService.saveEvento(evento);
    }

    // Actualizar un evento
    @PutMapping("/{id}")
    public Evento updateEvento(@PathVariable Long id, @RequestBody Evento evento) {
        return eventoService.updateEvento(id, evento);
    }

    // Eliminar un evento
    @DeleteMapping("/{id}")
    public void deleteEvento(@PathVariable Long id) {
        eventoService.deleteEvento(id);
    }
}