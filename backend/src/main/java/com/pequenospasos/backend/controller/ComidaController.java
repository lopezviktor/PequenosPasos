package com.pequenospasos.backend.controller;

import com.pequenospasos.backend.entity.Comida;
import com.pequenospasos.backend.service.ComidaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/comidas")
public class ComidaController {

    @Autowired
    private ComidaService comidaService;

    // Obtener todas las comidas
    @GetMapping
    public List<Comida> getAllComidas() {
        return comidaService.getAllComidas();
    }

    // Obtener comidas de un niño específico
    @GetMapping("/nino/{ninoId}")
    public List<Comida> getComidasByNinoId(@PathVariable Long ninoId) {
        return comidaService.getComidasByNinoId(ninoId);
    }

    // Obtener comidas registradas por un educador
    @GetMapping("/educador/{educadorId}")
    public List<Comida> getComidasByEducadorId(@PathVariable Long educadorId) {
        return comidaService.getComidasByEducadorId(educadorId);
    }

    // Obtener comidas en un rango de fechas
    @GetMapping("/rango-fechas")
    public List<Comida> getComidasByFecha(@RequestParam LocalDateTime inicio, @RequestParam LocalDateTime fin) {
        return comidaService.getComidasByFecha(inicio, fin);
    }

    // Obtener una comida por ID
    @GetMapping("/{id}")
    public Optional<Comida> getComidaById(@PathVariable Long id) {
        return comidaService.getComidaById(id);
    }

    // Registrar una nueva comida
    @PostMapping
    public Comida createComida(@RequestBody Comida comida) {
        return comidaService.saveComida(comida);
    }

    // Actualizar una comida
    @PutMapping("/{id}")
    public Comida updateComida(@PathVariable Long id, @RequestBody Comida comida) {
        return comidaService.updateComida(id, comida);
    }

    // Eliminar comida
    @DeleteMapping("/{id}")
    public void deleteComida(@PathVariable Long id) {
        comidaService.deleteComida(id);
    }
}