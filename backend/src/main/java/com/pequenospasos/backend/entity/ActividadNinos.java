package com.pequenospasos.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "actividad_ninos", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"nino_id", "actividad_id"})
})
public class ActividadNinos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "nino_id", nullable = false)
    private Nino nino;

    @ManyToOne
    @JoinColumn(name = "actividad_id", nullable = false)
    private Actividad actividad;

    @Column(nullable = false)
    private LocalDateTime fechaRegistro;

    // Constructor vacío
    public ActividadNinos() {}

    // Constructor con parámetros (asegurando que fechaRegistro nunca sea null)
    public ActividadNinos(Nino nino, Actividad actividad, LocalDateTime fechaRegistro) {
        if (nino == null || actividad == null) {
            throw new IllegalArgumentException("El niño y la actividad no pueden ser nulos.");
        }
        this.nino = nino;
        this.actividad = actividad;
        this.fechaRegistro = (fechaRegistro != null) ? fechaRegistro : LocalDateTime.now();
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Nino getNino() {
        return nino;
    }

    public void setNino(Nino nino) {
        this.nino = nino;
    }

    public Actividad getActividad() {
        return actividad;
    }

    public void setActividad(Actividad actividad) {
        this.actividad = actividad;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = (fechaRegistro != null) ? fechaRegistro : LocalDateTime.now();
    }
}