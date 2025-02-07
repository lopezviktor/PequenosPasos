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

    // Obtener educador por ID con validación
    @GetMapping("/{id}")
    public Educador getEducadorById(@PathVariable Long id) {
        return educadorService.getEducadorById(id)
                .orElseThrow(() -> new RuntimeException("Educador no encontrado con id: " + id));
    }

    // Buscar educador por email
    @GetMapping("/buscar")
    public Educador getEducadorByEmail(@RequestParam String email) {
        return educadorService.getEducadorByEmail(email)
                .orElseThrow(() -> new RuntimeException("Educador no encontrado con email: " + email));
    }

    // Crear un nuevo educador asegurando que el tipo de usuario es "EDUCADOR"
    @PostMapping
    public Educador createEducador(@RequestBody Educador educador) {
        educador.setTipoUsuario("EDUCADOR"); // 🔹 Asegurar que el usuario creado es un EDUCADOR
        return educadorService.saveEducador(educador);
    }

    // Actualizar un educador existente sin sobrescribir la contraseña si no se envía
    @PutMapping("/{id}")
    public Educador updateEducador(@PathVariable Long id, @RequestBody Educador educadorDetalles) {
        return educadorService.updateEducador(id, educadorDetalles);
    }

    // Eliminar un educador con validación de existencia
    @DeleteMapping("/{id}")
    public void deleteEducador(@PathVariable Long id) {
        educadorService.deleteEducador(id);
    }
}