package com.pequenospasos.backend.controller;

import com.pequenospasos.backend.dto.NotificacionDTO;
import com.pequenospasos.backend.entity.Notificacion;
import com.pequenospasos.backend.service.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public List<NotificacionDTO> getNotificacionesByReceptorId(@PathVariable Long receptorId) {
        return notificacionService.getNotificacionesByReceptorId(receptorId);
    }

    // Obtener notificaciones no leídas de un usuario
    @GetMapping("/receptor/{receptorId}/no-leidas")
    public List<NotificacionDTO> getNotificacionesNoLeidas(@PathVariable Long receptorId) {
        return notificacionService.getNotificacionesNoLeidas(receptorId);
    }

    // Obtener una notificación por ID con validación
    @GetMapping("/{id}")
    public Notificacion getNotificacionById(@PathVariable Long id) {
        return notificacionService.getNotificacionById(id)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada con id: " + id));
    }

    // Crear una nueva notificación asegurando que el receptor es un PADRE o EDUCADOR
    @PostMapping
    public Notificacion createNotificacion(@RequestBody Notificacion notificacion) {
        if (!(notificacion.getReceptor().getTipoUsuario().equals("PADRE") ||
                notificacion.getReceptor().getTipoUsuario().equals("EDUCADOR"))) {
            throw new RuntimeException("Solo PADRES y EDUCADORES pueden recibir notificaciones.");
        }
        return notificacionService.saveNotificacion(notificacion);
    }

    // Marcar una notificación como leída con validación
    @PutMapping("/{id}/marcar-leida")
    public NotificacionDTO marcarComoLeida(@PathVariable Long id) {
        return notificacionService.marcarComoLeida(id);
    }

    // Marcar todas las notificaciones de un usuario como leídas con validación
    @PutMapping("/receptor/{receptorId}/marcar-todas-leidas")
    public List<NotificacionDTO> marcarTodasComoLeidas(@PathVariable Long receptorId) {
        return notificacionService.marcarTodasComoLeidas(receptorId);
    }

    // Eliminar una notificación con validación de existencia
    @DeleteMapping("/{id}")
    public void deleteNotificacion(@PathVariable Long id) {
        notificacionService.getNotificacionById(id)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada con id: " + id));

        notificacionService.deleteNotificacion(id);
    }
}