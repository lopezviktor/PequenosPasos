package com.pequenospasos.backend.dto;

import com.pequenospasos.backend.entity.Actividad;

public class ActividadClaseRequest {
    private Long claseId;
    private Actividad actividad;
    private Long actividadId; // Añadido campo actividadId

    public Long getClaseId() {
        return claseId;
    }

    public void setClaseId(Long claseId) {
        this.claseId = claseId;
    }

    public Actividad getActividad() {
        return actividad;
    }

    public void setActividad(Actividad actividad) {
        this.actividad = actividad;
    }

    public Long getActividadId() { // Añadido método getActividadId
        return actividadId;
    }

    public void setActividadId(Long actividadId) { // Añadido método setActividadId
        this.actividadId = actividadId;
    }
}