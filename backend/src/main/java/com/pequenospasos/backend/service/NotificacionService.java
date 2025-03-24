package com.pequenospasos.backend.service;

import com.pequenospasos.backend.dto.NotificacionDTO;
import com.pequenospasos.backend.entity.Notificacion;
import com.pequenospasos.backend.entity.Usuario;
import com.pequenospasos.backend.repository.NotificacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NotificacionService {

    @Autowired
    private NotificacionRepository notificacionRepository;

    public void crearNotificacion(Usuario emisor, Usuario receptor, String mensaje) {
        if (!(receptor.getTipoUsuario().equals("PADRE") || receptor.getTipoUsuario().equals("EDUCADOR"))) {
            throw new RuntimeException("Solo PADRES y EDUCADORES pueden recibir notificaciones.");
        }

        Notificacion notificacion = new Notificacion();
        notificacion.setEmisor(emisor);
        notificacion.setReceptor(receptor);
        notificacion.setMensaje(mensaje);
        notificacion.setFechaHora(LocalDateTime.now());
        notificacion.setEstado(Notificacion.EstadoNotificacion.NO_LEIDO);

        notificacionRepository.save(notificacion);
    }

    // Obtener todas las notificaciones
    public List<NotificacionDTO> getAllNotificaciones() {
        List<Notificacion> notificaciones = notificacionRepository.findAll();
        return notificaciones.stream().map(NotificacionDTO::new).toList();
    }

    // Obtener una notificación por ID
    public Optional<Notificacion> getNotificacionById(Long id) {
        return notificacionRepository.findById(id);
    }

    // Obtener notificaciones de un usuario específico (solo PADRES y EDUCADORES)
    @Transactional(readOnly = true)
    public List<NotificacionDTO> getNotificacionesByReceptorId(Long receptorId) {
        List<Notificacion> notificaciones = notificacionRepository.findByReceptorIdWithUsuarios(receptorId);
        return notificaciones.stream().map(NotificacionDTO::new).toList();
    }

    // Obtener notificaciones no leídas de un usuario (solo PADRES y EDUCADORES)
    @Transactional(readOnly = true)
    public List<NotificacionDTO> getNotificacionesNoLeidas(Long receptorId) {
        List<Notificacion> notificaciones = notificacionRepository.findByReceptorIdWithUsuarios(receptorId).stream()
                .filter(n -> n.getEstado() == Notificacion.EstadoNotificacion.NO_LEIDO)
                .toList();
        return notificaciones.stream().map(NotificacionDTO::new).toList();
    }

    // Marcar una notificación como leída (solo PADRES y EDUCADORES)
    public NotificacionDTO marcarComoLeida(Long id) {
        Notificacion notificacion = notificacionRepository.findByIdWithReceptor(id)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada con id: " + id));

        if (!(notificacion.getReceptor().getTipoUsuario().equals("PADRE") || notificacion.getReceptor().getTipoUsuario().equals("EDUCADOR"))) {
            throw new RuntimeException("Solo PADRES y EDUCADORES pueden marcar notificaciones como leídas.");
        }

        notificacion.setEstado(Notificacion.EstadoNotificacion.LEIDO);
        notificacionRepository.save(notificacion);

        return new NotificacionDTO(notificacion);
    }

    // Marcar todas las notificaciones de un usuario como leídas (optimizado) y retornar DTOs
    public List<NotificacionDTO> marcarTodasComoLeidas(Long receptorId) {
        List<Notificacion> notificaciones = notificacionRepository.findByReceptorIdAndEstado(receptorId, Notificacion.EstadoNotificacion.NO_LEIDO).stream()
                .filter(n -> n.getReceptor().getTipoUsuario().equals("PADRE") || n.getReceptor().getTipoUsuario().equals("EDUCADOR"))
                .toList();

        if (!notificaciones.isEmpty()) {
            notificaciones.forEach(n -> n.setEstado(Notificacion.EstadoNotificacion.LEIDO));
            notificacionRepository.saveAll(notificaciones);
        }

        return notificaciones.stream().map(NotificacionDTO::new).toList();
    }

    // Guardar una nueva notificación (validando solo PADRES y EDUCADORES)
    public Notificacion saveNotificacion(Notificacion notificacion) {
        if (!(notificacion.getReceptor().getTipoUsuario().equals("PADRE") || notificacion.getReceptor().getTipoUsuario().equals("EDUCADOR"))) {
            throw new RuntimeException("Solo PADRES y EDUCADORES pueden recibir notificaciones.");
        }

        if (notificacion.getFechaHora() == null) {
            notificacion.setFechaHora(LocalDateTime.now());
        }
        return notificacionRepository.save(notificacion);
    }

    // Eliminar una notificación por ID
    public void deleteNotificacion(Long id) {
        notificacionRepository.deleteById(id);
    }
}