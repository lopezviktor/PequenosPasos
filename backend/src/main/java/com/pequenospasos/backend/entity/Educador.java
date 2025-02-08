package com.pequenospasos.backend.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
public class Educador extends Usuario {

    public Educador() {
        super();
        this.setTipoUsuario("EDUCADOR");
    }

    public Educador(String nombre, String apellidos, String email, String password, String telefono) {
        super(nombre, apellidos, email, password, telefono, "EDUCADOR");
    }
}