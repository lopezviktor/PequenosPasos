package com.pequenospasos.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "padres_hijos")
public class PadresHijos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "padre_id", nullable = false)
    private Usuario padre;

    @ManyToOne
    @JoinColumn(name = "nino_id", nullable = false)
    private Nino nino;

    // Constructor vacío
    public PadresHijos() {}

    // Constructor con parámetros
    public PadresHijos(Usuario padre, Nino nino) {
        this.padre = padre;
        this.nino = nino;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getPadre() {
        return padre;
    }

    public void setPadre(Usuario padre) {
        this.padre = padre;
    }

    public Nino getNino() {
        return nino;
    }

    public void setNino(Nino nino) {
        this.nino = nino;
    }
}