package com.pequenospasos.backend.controller;

import com.pequenospasos.backend.dto.ActividadClaseRequest;
import com.pequenospasos.backend.entity.ActividadNinos;
import com.pequenospasos.backend.service.ActividadNinosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/actividad-ninos")
public class ActividadNinosController {

    @Autowired
    private ActividadNinosService actividadNinosService;

    // Obtener todas las relaciones actividad-niño
    @GetMapping
    public List<ActividadNinos> getAllActividadNinos() {
        List<ActividadNinos> actividadNinos = actividadNinosService.getAllActividadNinos();
        if (actividadNinos.isEmpty()) {
            throw new RuntimeException("No hay registros de actividades asignadas a niños.");
        }
        return actividadNinos;
    }

    // Obtener todas las actividades en las que participa un niño
    @GetMapping("/nino/{ninoId}")
    public List<ActividadNinos> getActividadesByNinoId(@PathVariable Long ninoId) {
        return actividadNinosService.getActividadesByNinoId(ninoId);
    }

    // Obtener todos los niños que participan en una actividad específica
    @GetMapping("/actividad/{actividadId}")
    public List<ActividadNinos> getNinosByActividadId(@PathVariable Long actividadId) {
        return actividadNinosService.getNinosByActividadId(actividadId);
    }

    // Registrar una nueva actividad para un niño evitando duplicados
    @PostMapping
    public ActividadNinos saveActividadNino(@RequestBody ActividadNinos actividadNinos) {
        return actividadNinosService.saveActividadNino(actividadNinos);
    }

    // Registrar una actividad para una clase
    @PostMapping("/clase")
    public ResponseEntity<String> registrarActividadPorClase(@RequestBody ActividadClaseRequest request) {
        actividadNinosService.registrarActividadPorClase(request.getClaseId(), request.getActividad());
        return ResponseEntity.ok("Actividad registrada correctamente en todos los niños de la clase.");
    }

    // Eliminar una relación actividad-niño con validación
    @DeleteMapping("/{id}")
    public void deleteActividadNino(@PathVariable Long id) {
        actividadNinosService.getAllActividadNinos().stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Relación Actividad-Niño no encontrada con id: " + id));

        actividadNinosService.deleteActividadNino(id);
    }
}