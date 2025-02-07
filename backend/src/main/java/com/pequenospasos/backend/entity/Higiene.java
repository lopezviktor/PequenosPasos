package com.pequenospasos.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "higiene")
public class Higiene {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "nino_id", nullable = false)
    private Nino nino;

    @ManyToOne
    @JoinColumn(name = "educador_id", nullable = false)
    private Usuario educador;

    @Column(nullable = false)
    private LocalDateTime fechaHora;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoHigiene estado;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    public enum EstadoHigiene {
        NORMAL, ESTREÑIDO, SUELTO
    }

    public Higiene() {}

    public Higiene(Nino nino, Usuario educador, LocalDateTime fechaHora, EstadoHigiene estado, String observaciones) {
        this.nino = nino;
        this.educador = educador;
        this.fechaHora = (fechaHora != null) ? fechaHora : LocalDateTime.now();
        this.estado = estado;
        this.observaciones = observaciones;
    }

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

    public Usuario getEducador() {
        return educador;
    }

    public void setEducador(Usuario educador) {
        this.educador = educador;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public EstadoHigiene getEstado() {
        return estado;
    }

    public void setEstado(EstadoHigiene estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}