package com.pequenospasos.backend.dto;

public class SiestaResponse {
    private Long id;
    private String inicioSiesta;
    private String finSiesta;
    private String educador;
    private String observaciones;

    public SiestaResponse(Long id, String inicioSiesta, String finSiesta, String educador, String observaciones) {
        this.id = id;
        this.inicioSiesta = inicioSiesta;
        this.finSiesta = finSiesta;
        this.educador = educador;
        this.observaciones = observaciones;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getInicioSiesta() {
        return inicioSiesta;
    }
    public void setInicioSiesta(String inicioSiesta) {
        this.inicioSiesta = inicioSiesta;
    }
    public String getFinSiesta() {
        return finSiesta;
    }
    public void setFinSiesta(String finSiesta) {
        this.finSiesta = finSiesta;
    }
    public String getEducador() {
        return educador;
    }
    public void setEducador(String educador) {
        this.educador = educador;
    }
    public String getObservaciones() {
        return observaciones;
    }
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}