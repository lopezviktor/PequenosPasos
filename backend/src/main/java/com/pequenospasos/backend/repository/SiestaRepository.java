package com.pequenospasos.backend.repository;

import com.pequenospasos.backend.entity.Siesta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SiestaRepository extends JpaRepository<Siesta, Long> {

    // Buscar siestas de un niño específico
    List<Siesta> findByNinoId(Long ninoId);

    // Buscar siestas registradas por un educador específico (solo EDUCADORES)
    @Query("SELECT s FROM Siesta s WHERE s.educador.id = :educadorId AND s.educador.tipoUsuario = 'EDUCADOR'")
    List<Siesta> findByEducadorId(@Param("educadorId") Long educadorId);

    // Buscar siestas en un rango de fechas
    List<Siesta> findByInicioSiestaBetween(LocalDateTime inicio, LocalDateTime fin);

    // Última siesta de un niño
    @Query("SELECT s FROM Siesta s WHERE s.nino.id = :ninoId AND s.finSiesta IS NOT NULL ORDER BY s.inicioSiesta DESC")
    Optional<Siesta> findTopByNinoIdOrderByInicioSiestaDesc(@Param("ninoId") Long ninoId);

    // Verificar si hay una siesta sin hora de fin antes de registrar otra
    @Query("SELECT COUNT(s) > 0 FROM Siesta s WHERE s.nino.id = :ninoId AND s.finSiesta IS NULL")
    boolean existsSiestaSinHoraFin(@Param("ninoId") Long ninoId);

    boolean existsByNinoIdAndFinSiestaIsNull(Long ninoId);

}