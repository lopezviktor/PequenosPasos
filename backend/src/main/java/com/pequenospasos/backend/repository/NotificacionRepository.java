package com.pequenospasos.backend.repository;

import com.pequenospasos.backend.entity.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {

    // Buscar notificaciones enviadas por un usuario específico
    List<Notificacion> findByEmisorId(Long emisorId);

    // Buscar notificaciones recibidas por un usuario específico
    List<Notificacion> findByReceptorId(Long receptorId);

    // Buscar notificaciones no leídas de un usuario
    List<Notificacion> findByReceptorIdAndEstado(Long receptorId, Notificacion.EstadoNotificacion estado);
}