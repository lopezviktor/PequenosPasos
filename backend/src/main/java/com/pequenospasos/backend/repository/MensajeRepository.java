package com.pequenospasos.backend.repository;

import com.pequenospasos.backend.entity.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MensajeRepository extends JpaRepository<Mensaje, Long> {
    List<Mensaje> findByEmisorId(Long emisorId);
    List<Mensaje> findByReceptorId(Long receptorId);
    List<Mensaje> findByEmisorIdAndReceptorId(Long emisorId, Long receptorId);
}