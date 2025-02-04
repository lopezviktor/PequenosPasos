package com.pequenospasos.backend.controller;

import com.pequenospasos.backend.entity.Actividad;
import com.pequenospasos.backend.service.ActividadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/actividades")
public class ActividadController {

    @Autowired
    private ActividadService actividadService;

    // Obtener todas las actividades
    @GetMapping
    public List<Actividad> getAllActividades() {
        return actividadService.getAllActividades();
    }

    // Buscar actividades por nombre
    @GetMapping("/buscar")
    public List<Actividad> getActividadesByNombre(@RequestParam String nombre) {
        return actividadService.getActividadesByNombre(nombre);
    }

    // Buscar actividad por ID
    @GetMapping("/{id}")
    public Optional<Actividad> getActividadById(@PathVariable Long id) {
        return actividadService.getActividadById(id);
    }

    // Crear nueva actividad
    @PostMapping
    public Actividad createActividad(@RequestBody Actividad actividad) {
        return actividadService.saveActividad(actividad);
    }

    // Actualizar actividad
    @PutMapping("/{id}")
    public Actividad updateActividad(@PathVariable Long id, @RequestBody Actividad actividad) {
        return actividadService.updateActividad(id, actividad);
    }

    // Eliminar actividad
    @DeleteMapping("/{id}")
    public void deleteActividad(@PathVariable Long id) {
        actividadService.deleteActividad(id);
    }
}