package com.pequenospasos.backend.controller;

import com.pequenospasos.backend.entity.Asistencia;
import com.pequenospasos.backend.service.AsistenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/asistencias")
public class AsistenciaController {

    @Autowired
    private AsistenciaService asistenciaService;

    // Obtener todas las asistencias
    @GetMapping
    public List<Asistencia> getAllAsistencias() {
        return asistenciaService.getAllAsistencias();
    }

    // Obtener asistencias de un niño específico
    @GetMapping("/nino/{ninoId}")
    public List<Asistencia> getAsistenciasByNinoId(@PathVariable Long ninoId) {
        return asistenciaService.getAsistenciasByNinoId(ninoId);
    }

    // Obtener asistencias registradas por un educador
    @GetMapping("/educador/{educadorId}")
    public List<Asistencia> getAsistenciasByEducadorId(@PathVariable Long educadorId) {
        return asistenciaService.getAsistenciasByEducadorId(educadorId);
    }

    // Obtener asistencias dentro de un rango de fechas
    @GetMapping("/rango-fechas")
    public List<Asistencia> getAsistenciasByFecha(@RequestParam LocalDateTime inicio, @RequestParam LocalDateTime fin) {
        return asistenciaService.getAsistenciasByFecha(inicio, fin);
    }

    // Obtener una asistencia por ID
    @GetMapping("/{id}")
    public Optional<Asistencia> getAsistenciaById(@PathVariable Long id) {
        return asistenciaService.getAsistenciaById(id);
    }

    // Registrar una nueva asistencia (entrada de un niño)
    @PostMapping
    public Asistencia createAsistencia(@RequestBody Asistencia asistencia) {
        return asistenciaService.saveAsistencia(asistencia);
    }

    // Actualizar asistencia (registrar salida)
    @PutMapping("/{id}")
    public Asistencia updateAsistencia(@PathVariable Long id, @RequestBody Asistencia asistenciaDetalles) {
        return asistenciaService.updateAsistencia(id, asistenciaDetalles);
    }

    // Eliminar asistencia
    @DeleteMapping("/{id}")
    public void deleteAsistencia(@PathVariable Long id) {
        asistenciaService.deleteAsistencia(id);
    }
}