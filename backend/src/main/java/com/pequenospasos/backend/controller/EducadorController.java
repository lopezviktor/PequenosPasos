package com.pequenospasos.backend.controller;

import com.pequenospasos.backend.entity.Educador;
import com.pequenospasos.backend.service.EducadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/educadores")
public class EducadorController {

    @Autowired
    private EducadorService educadorService;

    // Obtener todos los educadores
    @GetMapping
    public List<Educador> getAllEducadores() {
        return educadorService.getAllEducadores();
    }

    // Obtener educador por ID
    @GetMapping("/{id}")
    public Optional<Educador> getEducadorById(@PathVariable Long id) {
        return educadorService.getEducadorById(id);
    }

    // Crear un nuevo educador
    @PostMapping
    public Educador createEducador(@RequestBody Educador educador) {
        return educadorService.saveEducador(educador);
    }

    // Actualizar un educador
    @PutMapping("/{id}")
    public Educador updateEducador(@PathVariable Long id, @RequestBody Educador educador) {
        return educadorService.updateEducador(id, educador);
    }

    // Eliminar un educador
    @DeleteMapping("/{id}")
    public void deleteEducador(@PathVariable Long id) {
        educadorService.deleteEducador(id);
    }
}