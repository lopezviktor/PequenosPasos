package com.pequenospasos.backend.repository;

import com.pequenospasos.backend.entity.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MensajeRepository extends JpaRepository<Mensaje, Long> {

    @Query("SELECT m FROM Mensaje m WHERE m.emisor.id = :emisorId AND (m.emisor.tipoUsuario = 'PADRE' OR m.emisor.tipoUsuario = 'EDUCADOR')")
    List<Mensaje> findByEmisorId(@Param("emisorId") Long emisorId);

    @Query("SELECT m FROM Mensaje m WHERE m.receptor.id = :receptorId AND (m.receptor.tipoUsuario = 'PADRE' OR m.receptor.tipoUsuario = 'EDUCADOR')")
    List<Mensaje> findByReceptorId(@Param("receptorId") Long receptorId);

    @Query("SELECT m FROM Mensaje m WHERE m.emisor.id = :emisorId AND m.receptor.id = :receptorId " +
            "AND (m.emisor.tipoUsuario = 'PADRE' OR m.emisor.tipoUsuario = 'EDUCADOR') " +
            "AND (m.receptor.tipoUsuario = 'PADRE' OR m.receptor.tipoUsuario = 'EDUCADOR')")
    List<Mensaje> findByEmisorIdAndReceptorId(@Param("emisorId") Long emisorId, @Param("receptorId") Long receptorId);

    @Query("SELECT m FROM Mensaje m WHERE m.receptor.id = :receptorId " +
            "AND (m.receptor.tipoUsuario = 'PADRE' OR m.receptor.tipoUsuario = 'EDUCADOR') " +
            "ORDER BY m.fechaHora DESC")
    List<Mensaje> findTop5ByReceptorIdOrderByFechaHoraDesc(@Param("receptorId") Long receptorId);

    @Query("SELECT m FROM Mensaje m WHERE m.receptor.id = :receptorId AND m.estado = :estado " +
            "AND (m.receptor.tipoUsuario = 'PADRE' OR m.receptor.tipoUsuario = 'EDUCADOR')")
    List<Mensaje> findByReceptorIdAndEstado(@Param("receptorId") Long receptorId, @Param("estado") Mensaje.EstadoMensaje estado);
}