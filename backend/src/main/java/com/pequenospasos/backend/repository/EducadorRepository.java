package com.pequenospasos.backend.repository;

import com.pequenospasos.backend.entity.Educador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EducadorRepository extends JpaRepository<Educador, Long> {

    // Buscar educadores por apellido, asegurando que sean EDUCADORES
    @Query("SELECT e FROM Educador e WHERE e.apellidos LIKE %:apellidos% AND TYPE(e) = Educador")
    List<Educador> findByApellidosContainingIgnoreCase(@Param("apellidos") String apellidos);

    // Buscar educador por ID asegurando que sea del tipo EDUCADOR
    @Query("SELECT e FROM Educador e WHERE e.id = :id")
    Optional<Educador> findEducadorById(@Param("id") Long id);

    // Verificar si un email ya está registrado
    boolean existsByEmail(String email);
    // Buscar educador por email
    Optional<Educador> findByEmail(String email);
}