package com.pequenospasos.backend.controller;

import com.pequenospasos.backend.entity.ActividadNinos;
import com.pequenospasos.backend.service.ActividadNinosService;
import org.springframework.beans.factory.annotation.Autowired;
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
        return actividadNinosService.getAllActividadNinos();
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

    // Registrar una nueva actividad para un niño
    @PostMapping
    public ActividadNinos saveActividadNino(@RequestBody ActividadNinos actividadNinos) {
        return actividadNinosService.saveActividadNino(actividadNinos);
    }

    // Eliminar una relación actividad-niño
    @DeleteMapping("/{id}")
    public void deleteActividadNino(@PathVariable Long id) {
        actividadNinosService.deleteActividadNino(id);
    }
}