package com.pequenospasos.backend.repository;

import com.pequenospasos.backend.entity.Padre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PadreRepository extends JpaRepository<Padre, Long> {

    // Buscar padre por email
    Padre findByEmail(String email);

    // Buscar padres por apellido
    List<Padre> findByApellidosContainingIgnoreCase(String apellidos);
}