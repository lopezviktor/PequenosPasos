package com.pequenospasos.backend.dto;

import com.pequenospasos.backend.entity.Notificacion;

import java.time.LocalDateTime;

public class NotificacionDTO {
    private Long id;
    private String mensaje;
    private LocalDateTime fechaHora;
    private String estado;
    private Long receptorId;
    private Long emisorId;

    public NotificacionDTO(Notificacion notificacion) {
        this.id = notificacion.getId();
        this.mensaje = notificacion.getMensaje();
        this.fechaHora = notificacion.getFechaHora();
        this.estado = notificacion.getEstado().name();
        this.receptorId = (notificacion.getReceptor() != null) ? notificacion.getReceptor().getId() : null;
        this.emisorId = (notificacion.getEmisor() != null) ? notificacion.getEmisor().getId() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Long getReceptorId() {
        return receptorId;
    }

    public void setReceptorId(Long receptorId) {
        this.receptorId = receptorId;
    }

    public Long getEmisorId() {
        return emisorId;
    }

    public void setEmisorId(Long emisorId) {
        this.emisorId = emisorId;
    }
}