package com.pequenospasos.backend.controller;

import com.pequenospasos.backend.dto.NotificacionDTO;
import com.pequenospasos.backend.entity.Notificacion;
import com.pequenospasos.backend.enums.Role;
import com.pequenospasos.backend.service.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    @Autowired
    private NotificacionService notificacionService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<NotificacionDTO> getAllNotificaciones() {
        return notificacionService.getAllNotificaciones();
    }

    @PreAuthorize("hasAnyRole('PADRE', 'EDUCADOR')")
    @GetMapping("/receptor/{receptorId}")
    public List<NotificacionDTO> getNotificacionesByReceptorId(@PathVariable Long receptorId) {
        return notificacionService.getNotificacionesByReceptorId(receptorId);
    }

    @PreAuthorize("hasAnyRole('PADRE', 'EDUCADOR')")
    @GetMapping("/receptor/{receptorId}/no-leidas")
    public List<NotificacionDTO> getNotificacionesNoLeidas(@PathVariable Long receptorId) {
        return notificacionService.getNotificacionesNoLeidas(receptorId);
    }

    @PreAuthorize("hasAnyRole('PADRE', 'EDUCADOR')")
    @GetMapping("/{id}")
    public Notificacion getNotificacionById(@PathVariable Long id) {
        return notificacionService.getNotificacionById(id)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada con id: " + id));
    }

    @PreAuthorize("hasAnyRole('EDUCADOR', 'ADMIN')")
    @PostMapping
    public Notificacion createNotificacion(@RequestBody Notificacion notificacion) {
        Role rol = notificacion.getReceptor().getTipoUsuario();
        if (rol != Role.PADRE && rol != Role.EDUCADOR) {
            throw new RuntimeException("Solo PADRES y EDUCADORES pueden recibir notificaciones.");
        }
        return notificacionService.saveNotificacion(notificacion);
    }

    @PreAuthorize("hasAnyRole('PADRE', 'EDUCADOR')")
    @PutMapping("/{id}/marcar-leida")
    public NotificacionDTO marcarComoLeida(@PathVariable Long id) {
        return notificacionService.marcarComoLeida(id);
    }

    @PreAuthorize("hasAnyRole('PADRE', 'EDUCADOR')")
    @PutMapping("/receptor/{receptorId}/marcar-todas-leidas")
    public List<NotificacionDTO> marcarTodasComoLeidas(@PathVariable Long receptorId) {
        return notificacionService.marcarTodasComoLeidas(receptorId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteNotificacion(@PathVariable Long id) {
        notificacionService.getNotificacionById(id)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada con id: " + id));
        notificacionService.deleteNotificacion(id);
    }
}