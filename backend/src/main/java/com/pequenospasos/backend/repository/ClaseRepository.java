package com.pequenospasos.backend.repository;

import com.pequenospasos.backend.entity.Clase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClaseRepository extends JpaRepository<Clase, Long> {
    Optional<Clase> findById(Long id);
}