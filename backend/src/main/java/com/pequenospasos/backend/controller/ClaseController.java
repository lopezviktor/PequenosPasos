package com.pequenospasos.backend.controller;

import com.pequenospasos.backend.dto.AsignarNinoRequest;
import com.pequenospasos.backend.entity.Clase;
import com.pequenospasos.backend.service.ClaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clases")
@CrossOrigin(origins = "*")
public class ClaseController {

    @Autowired
    private ClaseService claseService;

    // Obtener todas las clases
    @GetMapping
    public List<Clase> getAllClases() {
        return claseService.getAllClases();
    }

    // Obtener una clase por ID
    @GetMapping("/{id}")
    public Optional<Clase> getClaseById(@PathVariable("id") Long id) {
        return claseService.getClaseById(id);
    }

    // Crear una nueva clase
    @PostMapping
    public Clase crearClase(@RequestBody Clase nuevaClase) {
        return claseService.crearClase(nuevaClase.getNombre(), nuevaClase.getEducador().getId());
    }

    // Asignar un niño a una clase
    @PostMapping("/asignar-nino")
    public Clase asignarNinoAClase(@RequestBody AsignarNinoRequest request) {
        return claseService.asignarNinoAClase(request.getClaseId(), request.getNinoId());
    }
}