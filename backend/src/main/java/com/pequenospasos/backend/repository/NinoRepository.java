package com.pequenospasos.backend.repository;

import com.pequenospasos.backend.entity.Nino;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NinoRepository extends JpaRepository<Nino, Long> {

    // Buscar niños por nombre (sin distinguir mayúsculas/minúsculas)
    List<Nino> findByNombreContainingIgnoreCase(String nombre);

    // Buscar niños por apellidos
    List<Nino> findByApellidosContainingIgnoreCase(String apellidos);

}