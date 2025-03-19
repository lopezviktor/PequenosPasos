package com.pequenospasos.backend.repository;

import com.pequenospasos.backend.entity.Comida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ComidaRepository extends JpaRepository<Comida, Long> {

    // Buscar comidas registradas para un niño específico
    List<Comida> findByNinoId(Long ninoId);

    // Buscar comidas registradas por un educador específico (solo EDUCADORES)
    @Query("SELECT c FROM Comida c WHERE c.educador.id = :educadorId AND c.educador.tipoUsuario = 'EDUCADOR'")
    List<Comida> findByEducadorId(@Param("educadorId") Long educadorId);

    // Buscar comidas en un rango de fechas
    List<Comida> findByHoraComidaBetween(LocalDateTime inicio, LocalDateTime fin);

    List<Comida> findByNinoIdAndHoraComidaBetween(Long ninoId, LocalDateTime inicioDia, LocalDateTime finDia);

}