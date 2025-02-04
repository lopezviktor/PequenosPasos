package com.pequenospasos.backend.repository;

import com.pequenospasos.backend.entity.Educador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EducadorRepository extends JpaRepository<Educador, Long> {

    // Buscar educador por email
    Educador findByEmail(String email);

    // Buscar educadores por apellido
    List<Educador> findByApellidosContainingIgnoreCase(String apellidos);
}