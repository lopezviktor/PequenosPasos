package com.pequenospasos.backend.controller;

import com.pequenospasos.backend.dto.ComidaResponse;
import com.pequenospasos.backend.entity.Comida;
import com.pequenospasos.backend.service.ComidaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/comidas")
public class ComidaController {

    @Autowired
    private ComidaService comidaService;

    // Obtener todas las comidas (acceso solo para ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<Comida> getAllComidas() {
        return comidaService.getAllComidas();
    }

    // Obtener todas las comidas de un niño por id
    @PreAuthorize("hasAnyRole('PADRE', 'EDUCADOR')")
    @GetMapping("/nino/{ninoId}")
    public List<ComidaResponse> getComidasByNinoId(@PathVariable Long ninoId) {
        return comidaService.getComidasByNinoId(ninoId);
    }

    // Obtener comidas registradas por un educador (solo EDUCADOR)
    @PreAuthorize("hasRole('EDUCADOR')")
    @GetMapping("/educador/{educadorId}")
    public List<Comida> getComidasByEducadorId(@PathVariable Long educadorId) {
        return comidaService.getComidasByEducadorId(educadorId);
    }

    // Obtener comidas en un rango de fechas (solo EDUCADOR)
    @PreAuthorize("hasRole('EDUCADOR')")
    @GetMapping("/rango-fechas")
    public List<ComidaResponse> getComidasByFecha(@RequestParam String inicio, @RequestParam String fin) {
        LocalDateTime fechaInicio = LocalDateTime.parse(inicio);
        LocalDateTime fechaFin = LocalDateTime.parse(fin);
        return comidaService.getComidasByFecha(fechaInicio, fechaFin).stream()
                .map(comida -> new ComidaResponse(
                        comida.getId(),
                        comida.getHoraComida().toString(),
                        comida.getDescripcionComida(),
                        comida.getObservaciones(),
                        comida.getEducador().getNombre() + " " + comida.getEducador().getApellidos()
                )).collect(Collectors.toList());
    }

    // Obtener comidas en un rango de fechas con datos completos (solo EDUCADOR) — para dashboard
    @PreAuthorize("hasRole('EDUCADOR')")
    @GetMapping("/rango-fechas/entidad")
    public List<Comida> getComidasEntidadByFecha(@RequestParam String inicio, @RequestParam String fin) {
        LocalDateTime fechaInicio = LocalDateTime.parse(inicio);
        LocalDateTime fechaFin = LocalDateTime.parse(fin);
        return comidaService.getComidasByFecha(fechaInicio, fechaFin);
    }

    // Obtener una comida por ID (PADRE o EDUCADOR)
    @PreAuthorize("hasAnyRole('PADRE', 'EDUCADOR')")
    @GetMapping("/{id}")
    public Comida getComidaById(@PathVariable Long id) {
        return comidaService.getComidaById(id)
                .orElseThrow(() -> new RuntimeException("Comida no encontrada con id: " + id));
    }

    // Registrar una nueva comida (solo EDUCADOR)
    @PreAuthorize("hasRole('EDUCADOR')")
    @PostMapping
    public Comida createComida(@RequestBody Comida comida) {
        if (comida.getEducador() == null) {
            throw new IllegalArgumentException("Debe asignar un educador para registrar la comida.");
        }

        if (!"EDUCADOR".equals(comida.getEducador().getTipoUsuario())) {
            throw new IllegalArgumentException("Solo un EDUCADOR puede registrar comidas.");
        }

        return comidaService.saveComida(comida);
    }

    // Actualizar una comida (solo EDUCADOR)
    @PreAuthorize("hasRole('EDUCADOR')")
    @PutMapping("/{id}")
    public Comida updateComida(@PathVariable Long id, @RequestBody Comida comidaDetalles) {
        return comidaService.getComidaById(id).map(comida -> {
            if (!comidaDetalles.getEducador().getTipoUsuario().equals("EDUCADOR")) {
                throw new RuntimeException("Solo un EDUCADOR puede modificar comidas.");
            }
            return comidaService.updateComida(id, comidaDetalles);
        }).orElseThrow(() -> new RuntimeException("Comida no encontrada con id: " + id));
    }

    // Eliminar comida (solo EDUCADOR)
    @PreAuthorize("hasRole('EDUCADOR')")
    @DeleteMapping("/{id}")
    public void deleteComida(@PathVariable Long id) {
        comidaService.getComidaById(id)
                .orElseThrow(() -> new RuntimeException("Comida no encontrada con id: " + id));

        comidaService.deleteComida(id);
    }
}