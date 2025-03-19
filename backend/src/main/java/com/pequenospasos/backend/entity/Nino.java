package com.pequenospasos.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "ninos")
public class Nino {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellidos;

    @Column(nullable = false)
    private LocalDate fechaNacimiento;

    @Column(nullable = false)
    private LocalDate primerDia;

    private String alergias;

    private String condicionesMedicas;

    private String fotoUrl;

    public Clase getClase() {
        return clase;
    }

    public void setClase(Clase clase) {
        this.clase = clase;
    }

    @ManyToOne
    @JoinColumn(name = "clase_id")
    @JsonBackReference // Evita bucles en JSON
    private Clase clase;

    // Constructor vacío
    public Nino() {}

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {

        return nombre;
    }

    public void setNombre(String nombre) {

        this.nombre = nombre;
    }

    public String getApellidos() {

        return apellidos;
    }

    public void setApellidos(String apellidos) {

        this.apellidos = apellidos;
    }

    public LocalDate getFechaNacimiento() {

        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {

        this.fechaNacimiento = fechaNacimiento;
    }

    public LocalDate getPrimerDia() {

        return primerDia;
    }

    public void setPrimerDia(LocalDate primerDia) {

        this.primerDia = primerDia;
    }

    public String getAlergias() {

        return alergias;
    }

    public void setAlergias(String alergias) {

        this.alergias = alergias;
    }

    public String getCondicionesMedicas() {

        return condicionesMedicas;
    }

    public void setCondicionesMedicas(String condicionesMedicas) {

        this.condicionesMedicas = condicionesMedicas;
    }

    public String getFotoUrl() {

        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {

        this.fotoUrl = fotoUrl;
    }

}
