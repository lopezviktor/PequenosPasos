package com.pequenospasos.backend.controller;

import com.pequenospasos.backend.dto.ActividadClaseRequest;
import com.pequenospasos.backend.entity.ActividadNinos;
import com.pequenospasos.backend.service.ActividadNinosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/actividad-ninos")
public class ActividadNinosController {

    @Autowired
    private ActividadNinosService actividadNinosService;

    // Obtener todas las relaciones actividad-niño
    @PreAuthorize("hasRole('EDUCADOR')")
    @GetMapping
    public List<ActividadNinos> getAllActividadNinos() {
        List<ActividadNinos> actividadNinos = actividadNinosService.getAllActividadNinos();
        if (actividadNinos.isEmpty()) {
            throw new RuntimeException("No hay registros de actividades asignadas a niños.");
        }
        return actividadNinos;
    }

    // Obtener todas las actividades en las que participa un niño
    @PreAuthorize("hasAnyRole('EDUCADOR', 'PADRE')")
    @GetMapping("/nino/{ninoId}")
    public List<ActividadNinos> getActividadesByNinoId(@PathVariable Long ninoId) {
        return actividadNinosService.getActividadesByNinoId(ninoId);
    }

    // Obtener todos los niños que participan en una actividad específica
    @PreAuthorize("hasRole('EDUCADOR')")
    @GetMapping("/actividad/{actividadId}")
    public List<ActividadNinos> getNinosByActividadId(@PathVariable Long actividadId) {
        return actividadNinosService.getNinosByActividadId(actividadId);
    }

    // Registrar una nueva actividad para un niño evitando duplicados
    @PreAuthorize("hasRole('EDUCADOR')")
    @PostMapping
    public ActividadNinos saveActividadNino(@RequestBody ActividadNinos actividadNinos) {
        return actividadNinosService.saveActividadNino(actividadNinos);
    }

    // Registrar una actividad para una clase
    @PreAuthorize("hasRole('EDUCADOR')")
    @PostMapping("/clase")
    public ResponseEntity<Map<String, String>> registrarActividadPorClase(@RequestBody ActividadClaseRequest request) {
        actividadNinosService.registrarActividadPorClase(request.getClaseId(), request.getActividad());
        Map<String, String> response = new HashMap<>();
        response.put("message", "Actividad registrada correctamente en todos los niños de la clase.");
        return ResponseEntity.ok(response);
    }

    // Eliminar una relación actividad-niño con validación
    @PreAuthorize("hasRole('EDUCADOR')")
    @DeleteMapping("/{id}")
    public void deleteActividadNino(@PathVariable Long id) {
        actividadNinosService.getAllActividadNinos().stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Relación Actividad-Niño no encontrada con id: " + id));

        actividadNinosService.deleteActividadNino(id);
    }
}