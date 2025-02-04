package com.pequenospasos.backend.controller;

import com.pequenospasos.backend.entity.Nino;
import com.pequenospasos.backend.service.NinoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

    // Obtener niño por ID
    @GetMapping("/{id}")
    public Optional<Nino> getNinoById(@PathVariable Long id) {
        return ninoService.getNinoById(id);
    }

    // Crear un nuevo niño
    @PostMapping
    public Nino createNino(@RequestBody Nino nino) {
        return ninoService.saveNino(nino);
    }

    // Actualizar un niño
    @PutMapping("/{id}")
    public Nino updateNino(@PathVariable Long id, @RequestBody Nino nino) {
        return ninoService.updateNino(id, nino);
    }

    // Eliminar un niño
    @DeleteMapping("/{id}")
    public void deleteNino(@PathVariable Long id) {
        ninoService.deleteNino(id);
    }
}