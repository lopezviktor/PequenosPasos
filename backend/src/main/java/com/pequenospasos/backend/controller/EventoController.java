package com.pequenospasos.backend.controller;

import com.pequenospasos.backend.entity.Evento;
import com.pequenospasos.backend.enums.Role;
import com.pequenospasos.backend.service.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/eventos")
public class EventoController {

    @Autowired
    private EventoService eventoService;

    // Obtener todos los eventos
    @PreAuthorize("hasAnyRole('PADRE', 'EDUCADOR', 'ADMIN')")
    @GetMapping
    public List<Evento> getAllEventos() {
        return eventoService.getAllEventos();
    }

    // Obtener eventos por título
    @PreAuthorize("hasAnyRole('PADRE', 'EDUCADOR', 'ADMIN')")
    @GetMapping("/buscar")
    public List<Evento> getEventosByTitulo(@RequestParam String titulo) {
        return eventoService.getEventosByTitulo(titulo);
    }

    // Obtener eventos creados por un usuario específico
    @PreAuthorize("hasAnyRole('EDUCADOR', 'ADMIN')")
    @GetMapping("/creador/{creadorId}")
    public List<Evento> getEventosByCreador(@PathVariable Long creadorId) {
        return eventoService.getEventosByCreador(creadorId);
    }

    // Obtener eventos en un rango de fechas
    @PreAuthorize("hasAnyRole('PADRE', 'EDUCADOR', 'ADMIN')")
    @GetMapping("/rango-fechas")
    public List<Evento> getEventosByFecha(@RequestParam LocalDateTime inicio, @RequestParam LocalDateTime fin) {
        return eventoService.getEventosByFecha(inicio, fin);
    }

    // Obtener un evento por ID con validación
    @PreAuthorize("hasAnyRole('PADRE', 'EDUCADOR', 'ADMIN')")
    @GetMapping("/{id}")
    public Evento getEventoById(@PathVariable Long id) {
        return eventoService.getEventoById(id)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado con id: " + id));
    }

    // Crear un nuevo evento asegurando que solo EDUCADORES y ADMIN puedan hacerlo
    @PreAuthorize("hasAnyRole('EDUCADOR', 'ADMIN')")
    @PostMapping
    public Evento createEvento(@RequestBody Evento evento) {
        if (!(evento.getCreador().getTipoUsuario() == Role.EDUCADOR || evento.getCreador().getTipoUsuario() == Role.ADMIN)) {
            throw new RuntimeException("Solo un EDUCADOR o ADMINISTRADOR puede crear eventos.");
        }
        return eventoService.saveEvento(evento);
    }

    // Actualizar un evento asegurando que solo el creador pueda modificarlo
    @PreAuthorize("hasAnyRole('EDUCADOR', 'ADMIN')")
    @PutMapping("/{id}")
    public Evento updateEvento(@PathVariable Long id, @RequestBody Evento eventoDetalles) {
        return eventoService.getEventoById(id).map(evento -> {
            if (!evento.getCreador().getId().equals(eventoDetalles.getCreador().getId())) {
                throw new RuntimeException("Solo el creador del evento puede actualizarlo.");
            }
            return eventoService.updateEvento(id, eventoDetalles);
        }).orElseThrow(() -> new RuntimeException("Evento no encontrado con id: " + id));
    }

    // Eliminar un evento
    @PreAuthorize("hasAnyRole('EDUCADOR', 'ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteEvento(@PathVariable Long id) {
        Evento evento = eventoService.getEventoById(id)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado con id: " + id));

        if (!(evento.getCreador().getTipoUsuario() == Role.EDUCADOR || evento.getCreador().getTipoUsuario() == Role.ADMIN)) {
            throw new RuntimeException("Solo un EDUCADOR o ADMINISTRADOR puede eliminar eventos.");
        }

        eventoService.deleteEvento(id);
    }
}