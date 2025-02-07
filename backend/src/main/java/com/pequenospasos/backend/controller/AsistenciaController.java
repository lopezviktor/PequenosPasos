package com.pequenospasos.backend.controller;

import com.pequenospasos.backend.entity.Asistencia;
import com.pequenospasos.backend.service.AsistenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

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

    // Obtener una asistencia por ID con validación
    @GetMapping("/{id}")
    public Asistencia getAsistenciaById(@PathVariable Long id) {
        return asistenciaService.getAsistenciaById(id)
                .orElseThrow(() -> new RuntimeException("Asistencia no encontrada con id: " + id));
    }

    // Registrar una nueva asistencia (entrada de un niño) con validación
    @PostMapping
    public Asistencia createAsistencia(@RequestBody Asistencia asistencia) {
        if (!asistencia.getEducadorRecibe().getTipoUsuario().equals("EDUCADOR")) {
            throw new RuntimeException("Solo un EDUCADOR puede registrar asistencias.");
        }
        return asistenciaService.saveAsistencia(asistencia);
    }

    // Actualizar asistencia (registrar salida) con validación
    @PutMapping("/{id}")
    public Asistencia updateAsistencia(@PathVariable Long id, @RequestBody Asistencia asistenciaDetalles) {
        if (asistenciaDetalles.getEducadorEntrega() != null &&
                !asistenciaDetalles.getEducadorEntrega().getTipoUsuario().equals("EDUCADOR")) {
            throw new RuntimeException("Solo un EDUCADOR puede registrar la salida del niño.");
        }
        return asistenciaService.updateAsistencia(id, asistenciaDetalles);
    }

    // Eliminar asistencia con validación de existencia
    @DeleteMapping("/{id}")
    public void deleteAsistencia(@PathVariable Long id) {
        asistenciaService.getAsistenciaById(id)
                .orElseThrow(() -> new RuntimeException("Asistencia no encontrada con id: " + id));

        asistenciaService.deleteAsistencia(id);
    }
}