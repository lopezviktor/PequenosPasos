package com.pequenospasos.backend.repository;

import com.pequenospasos.backend.entity.PadresHijos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PadresHijosRepository extends JpaRepository<PadresHijos, Long> {

    // Buscar relaciones por ID del padre
    List<PadresHijos> findByPadreId(Long padreId);

    // Buscar relaciones por ID del niño
    List<PadresHijos> findByNinoId(Long ninoId);
}