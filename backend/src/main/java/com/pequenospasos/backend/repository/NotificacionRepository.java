package com.pequenospasos.backend.repository;

import com.pequenospasos.backend.entity.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {

    // Buscar notificaciones enviadas por un usuario específico (solo PADRES o EDUCADORES)
    @Query("SELECT n FROM Notificacion n WHERE n.emisor.id = :emisorId AND (n.emisor.tipoUsuario = 'PADRE' OR n.emisor.tipoUsuario = 'EDUCADOR')")
    List<Notificacion> findByEmisorId(@Param("emisorId") Long emisorId);

    // Buscar notificaciones recibidas por un usuario específico (solo PADRES o EDUCADORES)
    @Query("SELECT n FROM Notificacion n JOIN FETCH n.receptor WHERE n.receptor.id = :receptorId " +
            "AND (n.receptor.tipoUsuario = 'PADRE' OR n.receptor.tipoUsuario = 'EDUCADOR')")
    List<Notificacion> findByReceptorId(@Param("receptorId") Long receptorId);

    @Query("SELECT n FROM Notificacion n JOIN FETCH n.receptor WHERE n.id = :id")
    Optional<Notificacion> findByIdWithReceptor(@Param("id") Long id);

    // Buscar notificaciones no leídas de un usuario (solo PADRES o EDUCADORES)
    @Query("SELECT n FROM Notificacion n WHERE n.receptor.id = :receptorId AND n.estado = :estado " +
            "AND (n.receptor.tipoUsuario = 'PADRE' OR n.receptor.tipoUsuario = 'EDUCADOR')")
    List<Notificacion> findByReceptorIdAndEstado(@Param("receptorId") Long receptorId, @Param("estado") Notificacion.EstadoNotificacion estado);

    // Obtener las últimas 5 notificaciones (solo PADRES o EDUCADORES)
    @Query("SELECT n FROM Notificacion n WHERE n.receptor.id = :receptorId " +
            "AND (n.receptor.tipoUsuario = 'PADRE' OR n.receptor.tipoUsuario = 'EDUCADOR') " +
            "ORDER BY n.fechaHora DESC")
    List<Notificacion> findTop5ByReceptorIdOrderByFechaHoraDesc(@Param("receptorId") Long receptorId);
}