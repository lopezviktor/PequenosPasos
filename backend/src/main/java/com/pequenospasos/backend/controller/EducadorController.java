package com.pequenospasos.backend.controller;

import com.pequenospasos.backend.entity.Educador;
import com.pequenospasos.backend.service.EducadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/api/educadores")
public class EducadorController {

    @Autowired
    private EducadorService educadorService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<Educador> getAllEducadores() {
        return educadorService.getAllEducadores();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EDUCADOR')")
    @GetMapping("/{id}")
    public Educador getEducadorById(@PathVariable Long id) {
        return educadorService.getEducadorById(id)
                .orElseThrow(() -> new RuntimeException("Educador no encontrado con id: " + id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EDUCADOR')")
    @GetMapping("/buscar")
    public Educador getEducadorByEmail(@RequestParam String email) {
        return educadorService.getEducadorByEmail(email)
                .orElseThrow(() -> new RuntimeException("Educador no encontrado con email: " + email));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Educador createEducador(@RequestBody Educador educador) {
        return educadorService.saveEducador(educador);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Educador updateEducador(@PathVariable Long id, @RequestBody Educador educadorDetalles) {
        return educadorService.updateEducador(id, educadorDetalles);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteEducador(@PathVariable Long id) {
        educadorService.deleteEducador(id);
    }
}