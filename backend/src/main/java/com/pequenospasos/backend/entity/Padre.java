package com.pequenospasos.backend.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
public class Padre extends Usuario {


    public Padre() {
        super();
        this.setTipoUsuario("PADRE");
    }

    public Padre(String nombre, String apellidos, String email, String password, String telefono) {
        super(nombre, apellidos, email, password, telefono, "PADRE");
    }
}