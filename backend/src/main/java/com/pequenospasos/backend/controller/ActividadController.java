package com.pequenospasos.backend.controller;

import com.pequenospasos.backend.entity.Actividad;
import com.pequenospasos.backend.service.ActividadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    // Buscar actividad por ID con validación
    @GetMapping("/{id}")
    public Actividad getActividadById(@PathVariable Long id) {
        return actividadService.getActividadById(id)
                .orElseThrow(() -> new RuntimeException("Actividad no encontrada con id: " + id));
    }

    // Crear nueva actividad validando nombre único
    @PostMapping
    public Actividad createActividad(@RequestBody Actividad actividad) {
        if (!actividadService.getActividadesByNombre(actividad.getNombre()).isEmpty()) {
            throw new RuntimeException("Ya existe una actividad con este nombre.");
        }
        return actividadService.saveActividad(actividad);
    }

    // Actualizar actividad con validación de existencia y nombre único
    @PutMapping("/{id}")
    public Actividad updateActividad(@PathVariable Long id, @RequestBody Actividad actividadDetalles) {
        return actividadService.getActividadById(id).map(actividad -> {
            if (!actividad.getNombre().equalsIgnoreCase(actividadDetalles.getNombre()) &&
                    !actividadService.getActividadesByNombre(actividadDetalles.getNombre()).isEmpty()) {
                throw new RuntimeException("Ya existe otra actividad con este nombre.");
            }
            return actividadService.updateActividad(id, actividadDetalles);
        }).orElseThrow(() -> new RuntimeException("Actividad no encontrada con id: " + id));
    }

    // Eliminar actividad con validación de existencia
    @DeleteMapping("/{id}")
    public void deleteActividad(@PathVariable Long id) {
        actividadService.getActividadById(id)
                .orElseThrow(() -> new RuntimeException("Actividad no encontrada con id: " + id));

        actividadService.deleteActividad(id);
    }
}