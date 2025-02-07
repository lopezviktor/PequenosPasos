package com.pequenospasos.backend.repository;

import com.pequenospasos.backend.entity.PadresHijos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PadresHijosRepository extends JpaRepository<PadresHijos, Long> {

    // Obtener todas las relaciones de un padre con sus hijos (solo si es un PADRE)
    @Query("SELECT ph FROM PadresHijos ph WHERE ph.padre.id = :padreId AND ph.padre.tipoUsuario = 'PADRE'")
    List<PadresHijos> findByPadreId(@Param("padreId") Long padreId);

    // Obtener todas las relaciones de un niño con sus padres
    List<PadresHijos> findByNinoId(Long ninoId);

    // Obtener una relación específica entre un padre y un niño (asegurando que sea un PADRE)
    @Query("SELECT ph FROM PadresHijos ph WHERE ph.padre.id = :padreId AND ph.nino.id = :ninoId AND ph.padre.tipoUsuario = 'PADRE'")
    Optional<PadresHijos> findByPadreIdAndNinoId(@Param("padreId") Long padreId, @Param("ninoId") Long ninoId);
}