package com.pequenospasos.backend.repository;

import com.pequenospasos.backend.entity.Educador;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EducadorRepository extends UsuarioRepository {

    // Buscar educadores por apellido, asegurando que sean EDUCADORES
    @Query("SELECT e FROM Educador e WHERE e.apellidos LIKE %:apellidos% AND TYPE(e) = Educador")
    List<Educador> findByApellidosContainingIgnoreCase(@Param("apellidos") String apellidos);
}