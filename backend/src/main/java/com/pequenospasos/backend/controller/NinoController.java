package com.pequenospasos.backend.controller;

import com.pequenospasos.backend.entity.Nino;
import com.pequenospasos.backend.service.NinoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ninos")
public class NinoController {

    @Autowired
    private NinoService ninoService;

    // Obtener todos los niños
    @GetMapping
    public List<Nino> getAllNinos() {
        return ninoService.getAllNinos();
    }

    // Obtener niño por ID con validación
    @GetMapping("/{id}")
    public Nino getNinoById(@PathVariable Long id) {
        return ninoService.getNinoById(id)
                .orElseThrow(() -> new RuntimeException("Niño no encontrado con id: " + id));
    }

    // Buscar niño por nombre
    @GetMapping("/buscar")
    public List<Nino> getNinoByNombre(@RequestParam String nombre) {
        return ninoService.getNinoByNombre(nombre);
    }

    // Crear un nuevo niño
    @PostMapping
    public Nino createNino(@RequestBody Nino nino) {
        return ninoService.saveNino(nino);
    }

    // Actualizar un niño existente
    @PutMapping("/{id}")
    public Nino updateNino(@PathVariable Long id, @RequestBody Nino nino) {
        return ninoService.updateNino(id, nino);
    }

    // Eliminar un niño con validación de existencia
    @DeleteMapping("/{id}")
    public void deleteNino(@PathVariable Long id) {
        ninoService.deleteNino(id);
    }
}