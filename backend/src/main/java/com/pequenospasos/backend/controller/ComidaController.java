package com.pequenospasos.backend.controller;

import com.pequenospasos.backend.entity.Comida;
import com.pequenospasos.backend.service.ComidaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

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

    // Obtener una comida por ID con validación
    @GetMapping("/{id}")
    public Comida getComidaById(@PathVariable Long id) {
        return comidaService.getComidaById(id)
                .orElseThrow(() -> new RuntimeException("Comida no encontrada con id: " + id));
    }

    // Registrar una nueva comida asegurando que solo EDUCADORES puedan hacerlo
    @PostMapping
    public Comida createComida(@RequestBody Comida comida) {
        if (!comida.getEducador().getTipoUsuario().equals("EDUCADOR")) {
            throw new RuntimeException("Solo un EDUCADOR puede registrar comidas.");
        }
        return comidaService.saveComida(comida);
    }

    // Actualizar una comida validando que solo EDUCADORES puedan modificarla
    @PutMapping("/{id}")
    public Comida updateComida(@PathVariable Long id, @RequestBody Comida comidaDetalles) {
        return comidaService.getComidaById(id).map(comida -> {
            if (!comidaDetalles.getEducador().getTipoUsuario().equals("EDUCADOR")) {
                throw new RuntimeException("Solo un EDUCADOR puede modificar comidas.");
            }
            return comidaService.updateComida(id, comidaDetalles);
        }).orElseThrow(() -> new RuntimeException("Comida no encontrada con id: " + id));
    }

    // Eliminar comida con validación de existencia
    @DeleteMapping("/{id}")
    public void deleteComida(@PathVariable Long id) {
        comidaService.getComidaById(id)
                .orElseThrow(() -> new RuntimeException("Comida no encontrada con id: " + id));

        comidaService.deleteComida(id);
    }
}