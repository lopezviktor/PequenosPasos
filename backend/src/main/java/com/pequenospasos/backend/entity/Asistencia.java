package com.pequenospasos.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "asistencia")
public class Asistencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "nino_id", nullable = false)
    private Nino nino;

    @ManyToOne
    @JoinColumn(name = "padre_entrada_id", nullable = false)
    private Padre padreEntrega; // Padre que deja al niño en la entrada

    @ManyToOne
    @JoinColumn(name = "educador_entrada_id", nullable = false)
    private Educador educadorRecibe; // Educador que recibe al niño

    @ManyToOne
    @JoinColumn(name = "educador_salida_id")
    private Educador educadorEntrega; // Educador que entrega al niño en la salida

    @ManyToOne
    @JoinColumn(name = "padre_salida_id")
    private Padre padreRecoge; // Padre que recoge al niño

    @Column(nullable = false)
    private LocalDateTime horaEntrada;

    @Column
    private LocalDateTime horaSalida;

    // Constructor para registrar entrada (sin salida aún)
    public Asistencia(Nino nino, Padre padreEntrega, Educador educadorRecibe, LocalDateTime horaEntrada) {
        this.nino = nino;
        this.padreEntrega = padreEntrega;
        this.educadorRecibe = educadorRecibe;
        this.horaEntrada = horaEntrada;
        this.horaSalida = null;
        this.educadorEntrega = null;
        this.padreRecoge = null;
    }

    // Método para registrar la salida, con educador que entrega y padre que recoge
    public void registrarSalida(LocalDateTime horaSalida, Educador educadorEntrega, Padre padreRecoge) {
        this.horaSalida = horaSalida;
        this.educadorEntrega = educadorEntrega;
        this.padreRecoge = padreRecoge;
    }

    // Constructor vacío (requerido por JPA)
    public Asistencia() {}

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

    public Padre getPadreEntrega() {
        return padreEntrega;
    }

    public void setPadreEntrega(Padre padreEntrega) {
        this.padreEntrega = padreEntrega;
    }

    public Educador getEducadorRecibe() {
        return educadorRecibe;
    }

    public void setEducadorRecibe(Educador educadorRecibe) {
        this.educadorRecibe = educadorRecibe;
    }

    public Educador getEducadorEntrega() {
        return educadorEntrega;
    }

    public void setEducadorEntrega(Educador educadorEntrega) {
        this.educadorEntrega = educadorEntrega;
    }

    public Padre getPadreRecoge() {
        return padreRecoge;
    }

    public void setPadreRecoge(Padre padreRecoge) {
        this.padreRecoge = padreRecoge;
    }

    public LocalDateTime getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(LocalDateTime horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public LocalDateTime getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(LocalDateTime horaSalida) {
        this.horaSalida = horaSalida;
    }
}