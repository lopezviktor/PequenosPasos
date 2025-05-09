package com.pequenospasos.backend.dto;

public class HigieneResponse {
    private Long id;
    private String fechaHora;
    private String estado;
    private String observaciones;
    private String educador;
    private String clase;

    public HigieneResponse(Long id, String fechaHora, String estado, String observaciones, String educador, String clase) {
        this.id = id;
        this.fechaHora = fechaHora;
        this.estado = estado;
        this.observaciones = observaciones;
        this.educador = educador;
        this.clase = clase;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getFechaHora() {
        return fechaHora;
    }
    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
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
    public String getClase() {
        return clase;
    }
    public void setClase(String clase) {
        this.clase = clase;
    }
}