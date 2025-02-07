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
    @JoinColumn(name = "padre_entrega_id", nullable = false)
    private Usuario padreEntrega;

    @ManyToOne
    @JoinColumn(name = "educador_recibe_id", nullable = false)
    private Usuario educadorRecibe;

    @ManyToOne
    @JoinColumn(name = "educador_entrega_id")
    private Usuario educadorEntrega;

    @ManyToOne
    @JoinColumn(name = "padre_recoge_id")
    private Usuario padreRecoge;

    @Column(nullable = false)
    private LocalDateTime horaEntrada;

    @Column(nullable = true)
    private LocalDateTime horaSalida;

    // Constructor para registrar entrada (sin salida aún)
    public Asistencia(Nino nino, Usuario padreEntrega, Usuario educadorRecibe, LocalDateTime horaEntrada) {
        this.nino = nino;
        this.padreEntrega = padreEntrega;
        this.educadorRecibe = educadorRecibe;
        this.horaEntrada = horaEntrada;
    }

    // Método para registrar la salida, con educador que entrega y padre que recoge
    public void registrarSalida(LocalDateTime horaSalida, Usuario educadorEntrega, Usuario padreRecoge) {
        if (this.horaSalida != null) {
            throw new IllegalStateException("La salida ya ha sido registrada para este niño.");
        }
        this.horaSalida = horaSalida;
        this.educadorEntrega = educadorEntrega;
        this.padreRecoge = padreRecoge;
    }

    // Constructor vacío (requerido por JPA)
    public Asistencia() {
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

    // Getter para el padre que deja al niño en la entrada
    public Usuario getPadreEntrega() {
        return padreEntrega;
    }

    // Setter para el padre que deja al niño en la entrada
    public void setPadreEntrega(Usuario padreEntrega) {
        this.padreEntrega = padreEntrega;
    }

    // Getter para el educador que recibe al niño en la entrada
    public Usuario getEducadorRecibe() {
        return educadorRecibe;
    }

    // Setter para el educador que recibe al niño en la entrada
    public void setEducadorRecibe(Usuario educadorRecibe) {
        this.educadorRecibe = educadorRecibe;
    }

    public Usuario getEducadorEntrega() {
        return educadorEntrega;
    }

    public void setEducadorEntrega(Usuario educadorEntrega) {
        this.educadorEntrega = educadorEntrega;
    }

    public Usuario getPadreRecoge() {
        return padreRecoge;
    }

    public void setPadreRecoge(Usuario padreRecoge) {
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