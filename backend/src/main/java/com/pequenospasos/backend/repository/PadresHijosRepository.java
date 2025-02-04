package com.pequenospasos.backend.repository;

import com.pequenospasos.backend.entity.PadresHijos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PadresHijosRepository extends JpaRepository<PadresHijos, Long> {

    // Obtener todas las relaciones de un padre con sus hijos
    List<PadresHijos> findByPadreId(Long padreId);

    // Obtener todas las relaciones de un niño con sus padres
    List<PadresHijos> findByNinoId(Long ninoId);

    // Obtener una relación específica entre un padre y un niño
    PadresHijos findByPadreIdAndNinoId(Long padreId, Long ninoId);
}