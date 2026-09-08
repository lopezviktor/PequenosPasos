package com.pequenospasos.backend.entity;

import com.pequenospasos.backend.enums.Role;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
public class Padre extends Usuario {


    public Padre() {
        super();
        this.setTipoUsuario(Role.PADRE);
    }

    public Padre(String nombre, String apellidos, String email, String password, String telefono) {
        super(nombre, apellidos, email, password, telefono, Role.PADRE);
    }
}