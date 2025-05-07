package com.pequenospasos.backend.repository;

import com.pequenospasos.backend.entity.Nino;
import com.pequenospasos.backend.entity.PadresHijos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface PadresHijosRepository extends JpaRepository<PadresHijos, Long> {

    // Obtener todas las relaciones de un padre con sus hijos (solo si es un PADRE)
    @Query("SELECT ph.nino FROM PadresHijos ph WHERE ph.padre.id = :padreId AND ph.padre.tipoUsuario = 'PADRE'")
    List<Nino> findNinosByPadreId(@Param("padreId") Long padreId);

    // Obtener todas las relaciones de un niño con sus padres
    List<PadresHijos> findByNinoId(Long ninoId);

    //Obtener niño, no solo el id como el anterior
    List<PadresHijos> findByNino(Nino nino);

    // Obtener una relación específica entre un padre y un niño (asegurando que sea un PADRE)
    @Query("SELECT ph FROM PadresHijos ph WHERE ph.padre.id = :padreId AND ph.nino.id = :ninoId AND ph.padre.tipoUsuario = 'PADRE'")
    Optional<PadresHijos> findByPadreIdAndNinoId(@Param("padreId") Long padreId, @Param("ninoId") Long ninoId);

    // Eliminar una relación entre un padre y un niño
    @Modifying
    @Transactional
    @Query("DELETE FROM PadresHijos ph WHERE ph.padre.id = :padreId AND ph.nino.id = :ninoId")
    void deleteByPadreIdAndNinoId(@Param("padreId") Long padreId, @Param("ninoId") Long ninoId);

}