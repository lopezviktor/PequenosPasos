package com.pequenospasos.backend.controller;

import com.pequenospasos.backend.entity.Higiene;
import com.pequenospasos.backend.service.HigieneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/higiene")
public class HigieneController {

    @Autowired
    private HigieneService higieneService;

    // Obtener todos los registros de higiene
    @GetMapping
    public List<Higiene> getAllHigiene() {
        return higieneService.getAllHigiene();
    }

    // Obtener registros de higiene de un niño específico
    @GetMapping("/nino/{ninoId}")
    public List<Higiene> getHigieneByNinoId(@PathVariable Long ninoId) {
        return higieneService.getHigieneByNinoId(ninoId);
    }

    // Obtener registros de higiene hechos por un educador
    @GetMapping("/educador/{educadorId}")
    public List<Higiene> getHigieneByEducadorId(@PathVariable Long educadorId) {
        return higieneService.getHigieneByEducadorId(educadorId);
    }

    // Obtener registros de higiene en un rango de fechas
    @GetMapping("/rango-fechas")
    public List<Higiene> getHigieneByFecha(@RequestParam LocalDateTime inicio, @RequestParam LocalDateTime fin) {
        return higieneService.getHigieneByFecha(inicio, fin);
    }

    // Obtener el último registro de higiene de un niño
    @GetMapping("/nino/{ninoId}/ultima")
    public Optional<Higiene> getUltimaHigieneByNinoId(@PathVariable Long ninoId) {
        return Optional.ofNullable(higieneService.getUltimaHigieneByNinoId(ninoId));
    }

    // Obtener un registro de higiene por ID
    @GetMapping("/{id}")
    public Optional<Higiene> getHigieneById(@PathVariable Long id) {
        return higieneService.getHigieneById(id);
    }

    // Registrar un nuevo registro de higiene
    @PostMapping
    public Higiene createHigiene(@RequestBody Higiene higiene) {
        return higieneService.saveHigiene(higiene);
    }

    // Actualizar un registro de higiene
    @PutMapping("/{id}")
    public Higiene updateHigiene(@PathVariable Long id, @RequestBody Higiene higiene) {
        return higieneService.updateHigiene(id, higiene);
    }

    // Eliminar un registro de higiene
    @DeleteMapping("/{id}")
    public void deleteHigiene(@PathVariable Long id) {
        higieneService.deleteHigiene(id);
    }
}