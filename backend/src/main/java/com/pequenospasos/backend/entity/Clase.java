package com.pequenospasos.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class Clase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @ManyToOne
    @JoinColumn(name = "educador_id", nullable = false)
    private Educador educador;

    @OneToMany(mappedBy = "clase", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JsonIgnoreProperties("clase")
    private List<Nino> ninos;

    // Constructores
    public Clase() {}

    public Clase(String nombre, Educador educador) {
        this.nombre = nombre;
        this.educador = educador;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Educador getEducador() { return educador; }
    public void setEducador(Educador educador) { this.educador = educador; }

    public List<Nino> getNinos() { return ninos; }
    public void setNinos(List<Nino> ninos) { this.ninos = ninos; }
}