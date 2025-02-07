package com.pequenospasos.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "siestas")
public class Siesta {

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
    private LocalDateTime inicioSiesta;

    @Column
    private LocalDateTime finSiesta;

    public Siesta() {}

    public Siesta(Nino nino, Usuario educador, LocalDateTime inicioSiesta, LocalDateTime finSiesta) {
        this.nino = nino;
        this.educador = educador;
        this.inicioSiesta = inicioSiesta;
        this.finSiesta = finSiesta;
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

    public LocalDateTime getInicioSiesta() {
        return inicioSiesta;
    }

    public void setInicioSiesta(LocalDateTime inicioSiesta) {
        this.inicioSiesta = inicioSiesta;
    }

    public LocalDateTime getFinSiesta() {
        return finSiesta;
    }

    public void setFinSiesta(LocalDateTime finSiesta) {
        this.finSiesta = finSiesta;
    }
}