package com.pequenospasos.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comidas")
public class Comida {

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
    private LocalDateTime horaComida;

    @Column(nullable = false)
    private String descripcionComida;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    public Comida() {}

    public Comida(Nino nino, Usuario educador, LocalDateTime horaComida, String descripcionComida, String observaciones) {
        this.nino = nino;
        this.educador = educador;
        this.horaComida = horaComida;
        this.descripcionComida = descripcionComida;
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

    public LocalDateTime getHoraComida() {
        return horaComida;
    }

    public void setHoraComida(LocalDateTime horaComida) {
        this.horaComida = horaComida;
    }

    public String getDescripcionComida() {
        return descripcionComida;
    }

    public void setDescripcionComida(String descripcionComida) {
        this.descripcionComida = descripcionComida;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}