package com.pequenospasos.backend.repository;

import com.pequenospasos.backend.entity.Nino;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NinoRepository extends JpaRepository<Nino, Long> {

    // Buscar niños por nombre (sin distinguir mayúsculas/minúsculas)
    List<Nino> findByNombreContainingIgnoreCase(String nombre);

    // Buscar niños por apellidos
    List<Nino> findByApellidosContainingIgnoreCase(String apellidos);

    // Buscar un niño por su ID
    Optional<Nino> findById(Long id);

    // Buscar niños por ID de Padre

    // Buscar niños asegurando que el padre sea realmente un PADRE
    @Query("SELECT n FROM Nino n JOIN PadresHijos ph ON n.id = ph.nino.id WHERE ph.padre.id = :padreId")
    List<Nino> findNinosByPadreId(@Param("padreId") Long padreId);

    @Query("SELECT n FROM Nino n JOIN PadresHijos ph ON n.id = ph.nino.id WHERE n.id = :id")
    Optional<Nino> findByIdWithPadre(@Param("id") Long id);

    // Buscar niños por ID de clase
    @Query("SELECT n FROM Nino n WHERE n.clase.id = :claseId")
    List<Nino> findNinosByClaseId(@Param("claseId") Long claseId);
}