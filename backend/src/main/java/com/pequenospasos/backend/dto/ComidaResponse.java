package com.pequenospasos.backend.dto;

public class ComidaResponse {
    private Long id;
    private String horaComida;
    private String descripcionComida;
    private String observaciones;
    private String educador;

    public ComidaResponse(Long id, String horaComida, String descripcionComida, String observaciones, String educador) {
        this.id = id;
        this.horaComida = horaComida;
        this.descripcionComida = descripcionComida;
        this.observaciones = observaciones;
        this.educador = educador;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHoraComida() {
        return horaComida;
    }

    public void setHoraComida(String horaComida) {
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

    public String getEducador() {
        return educador;
    }

    public void setEducador(String educador) {
        this.educador = educador;
    }
}