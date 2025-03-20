package com.pequenospasos.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "evento_ninos")
public class EventoNinos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "evento_id", nullable = false)
    private Evento evento;

    @ManyToOne
    @JoinColumn(name = "nino_id", nullable = false)
    private Nino nino;

    @Column(nullable = false)
    private Boolean asistio = false; // Por defecto, el niño aún no ha asistido

    // Constructor vacío
    public EventoNinos() {}

    // Constructor con parámetros
    public EventoNinos(Evento evento, Nino nino, Boolean asistio) {
        this.evento = evento;
        this.nino = nino;
        this.asistio = asistio;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public Evento getEvento() { return evento; }
    public Nino getNino() { return nino; }
    public Boolean getAsistio() { return asistio; }

    public void setId(Long id) { this.id = id; }
    public void setEvento(Evento evento) { this.evento = evento; }
    public void setNino(Nino nino) { this.nino = nino; }
    public void setAsistio(Boolean asistio) { this.asistio = asistio; }
}