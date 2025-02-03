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

    // Constructor vacío
    public EventoNinos() {}

    // Constructor con parámetros
    public EventoNinos(Evento evento, Nino nino) {
        this.evento = evento;
        this.nino = nino;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public Nino getNino() {
        return nino;
    }

    public void setNino(Nino nino) {
        this.nino = nino;
    }
}