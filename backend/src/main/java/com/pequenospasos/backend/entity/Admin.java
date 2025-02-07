package com.pequenospasos.backend.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
public class Admin extends Usuario {

    public Admin() {
        super();
        setTipoUsuario("ADMIN");
    }

    public Admin(String nombre, String apellidos, String email, String password, String telefono) {
        super(nombre, apellidos, email, password, telefono, "ADMIN");
    }
}