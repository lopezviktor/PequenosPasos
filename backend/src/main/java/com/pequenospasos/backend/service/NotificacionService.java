package com.pequenospasos.backend.service;

import com.pequenospasos.backend.entity.Notificacion;
import com.pequenospasos.backend.repository.NotificacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NotificacionService {

    @Autowired
    private NotificacionRepository notificacionRepository;

    // Obtener todas las notificaciones
    public List<Notificacion> getAllNotificaciones() {
        return notificacionRepository.findAll();
    }

    // Obtener una notificacion por id especifico
    public Optional<Notificacion> getNotificacionById(Long id){
        return notificacionRepository.findById(id);
    }

    // Obtener notificaciones de un usuario específico
    public List<Notificacion> getNotificacionesByReceptorId(Long receptorId) {
        return notificacionRepository.findByReceptorId(receptorId);
    }

    // Obtener notificaciones no leídas de un usuario específico
    public List<Notificacion> getNotificacionesNoLeidas(Long receptorId) {
        return notificacionRepository.findByReceptorIdAndEstado(receptorId, Notificacion.EstadoNotificacion.NO_LEIDO);
    }

    // Marcar una notificación como leída
    public Notificacion marcarComoLeida(Long id) {
        Optional<Notificacion> notificacionOptional = notificacionRepository.findById(id);
        if (notificacionOptional.isPresent()) {
            Notificacion notificacion = notificacionOptional.get();
            notificacion.setEstado(Notificacion.EstadoNotificacion.LEIDO);
            return notificacionRepository.save(notificacion);
        } else {
            throw new RuntimeException("Notificación no encontrada con id: " + id);
        }
    }

    // Marcar todas las notificaciones de un usuario como leídas
    public void marcarTodasComoLeidas(Long receptorId) {
        List<Notificacion> notificaciones = notificacionRepository.findByReceptorIdAndEstado(receptorId, Notificacion.EstadoNotificacion.NO_LEIDO);
        for (Notificacion notificacion : notificaciones) {
            notificacion.setEstado(Notificacion.EstadoNotificacion.LEIDO);
        }
        notificacionRepository.saveAll(notificaciones);
    }

    // Guardar una nueva notificación
    public Notificacion saveNotificacion(Notificacion notificacion) {
        if (notificacion.getFechaHora() == null) {
            notificacion.setFechaHora(LocalDateTime.now()); // Asigna la fecha y hora actual si no se proporciona
        }
        return notificacionRepository.save(notificacion);
    }

    // Eliminar una notificación por ID
    public void deleteNotificacion(Long id) {
        notificacionRepository.deleteById(id);
    }
}