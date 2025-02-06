package com.pequenospasos.backend.controller;

import com.pequenospasos.backend.entity.Notificacion;
import com.pequenospasos.backend.service.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    @Autowired
    private NotificacionService notificacionService;

    // Obtener todas las notificaciones
    @GetMapping
    public List<Notificacion> getAllNotificaciones() {
        return notificacionService.getAllNotificaciones();
    }

    // Obtener notificaciones recibidas por un usuario
    @GetMapping("/receptor/{receptorId}")
    public List<Notificacion> getNotificacionesByReceptorId(@PathVariable Long receptorId) {
        return notificacionService.getNotificacionesByReceptorId(receptorId);
    }

    // Obtener notificaciones no leídas de un usuario
    @GetMapping("/receptor/{receptorId}/no-leidas")
    public List<Notificacion> getNotificacionesNoLeidas(@PathVariable Long receptorId) {
        return notificacionService.getNotificacionesNoLeidas(receptorId);
    }

    // Obtener una notificación por ID
    @GetMapping("/{id}")
    public Optional<Notificacion> getNotificacionById(@PathVariable Long id) {
        return notificacionService.getNotificacionById(id);
    }

    // Crear una nueva notificación
    @PostMapping
    public Notificacion createNotificacion(@RequestBody Notificacion notificacion) {
        return notificacionService.saveNotificacion(notificacion);
    }

    // Marcar una notificación como leída
    @PutMapping("/{id}/marcar-leida")
    public Notificacion marcarComoLeida(@PathVariable Long id) {
        return notificacionService.marcarComoLeida(id);
    }

    // Marcar todas las notificaciones de un usuario como leídas
    @PutMapping("/receptor/{receptorId}/marcar-todas-leidas")
    public void marcarTodasComoLeidas(@PathVariable Long receptorId) {
        notificacionService.marcarTodasComoLeidas(receptorId);
    }

    // Eliminar una notificación
    @DeleteMapping("/{id}")
    public void deleteNotificacion(@PathVariable Long id) {
        notificacionService.deleteNotificacion(id);
    }
}