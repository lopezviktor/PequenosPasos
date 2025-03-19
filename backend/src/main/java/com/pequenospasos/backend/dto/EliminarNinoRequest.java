package com.pequenospasos.backend.dto;

public class EliminarNinoRequest {
    private Long claseId;
    private Long ninoId;

    public Long getClaseId() { return claseId; }
    public void setClaseId(Long claseId) { this.claseId = claseId; }

    public Long getNinoId() { return ninoId; }
    public void setNinoId(Long ninoId) { this.ninoId = ninoId; }
}