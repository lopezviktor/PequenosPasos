package com.pequenospasos.backend.controller;

import com.pequenospasos.backend.dto.AsignarNinoRequest;
import com.pequenospasos.backend.dto.EliminarNinoRequest;
import com.pequenospasos.backend.entity.Clase;
import com.pequenospasos.backend.service.ClaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('EDUCADOR')")
    @GetMapping
    public List<Clase> getAllClases() {
        return claseService.getAllClases();
    }

    // Obtener una clase por ID
    @PreAuthorize("hasRole('EDUCADOR') or hasRole('PADRE')")
    @GetMapping("/{id}")
    public Optional<Clase> getClaseById(@PathVariable("id") Long id) {
        return claseService.getClaseById(id);
    }

    // Crear una nueva clase
    @PreAuthorize("hasRole('EDUCADOR')")
    @PostMapping
    public Clase crearClase(@RequestBody Clase nuevaClase) {
        return claseService.crearClase(nuevaClase.getNombre(), nuevaClase.getEducador().getId());
    }

    // Actualizar una clase existente
    @PreAuthorize("hasRole('EDUCADOR')")
    @PutMapping("/{id}")
    public Clase actualizarClase(@PathVariable Long id, @RequestBody Clase nuevaClase) {
    Long educadorId = (nuevaClase.getEducador() != null) ? nuevaClase.getEducador().getId() : null;
    return claseService.actualizarClase(id, nuevaClase.getNombre(), educadorId);
    }

    // Asignar un niño a una clase
    @PreAuthorize("hasRole('EDUCADOR')")
    @PostMapping("/asignar-nino")
    public Clase asignarNinoAClase(@RequestBody AsignarNinoRequest request) {
        return claseService.asignarNinoAClase(request.getClaseId(), request.getNinoId());
    }

    // Eliminar un niño de una clase
    @PreAuthorize("hasRole('EDUCADOR')")
    @DeleteMapping("/eliminar-nino")
    public Clase eliminarNinoDeClase(@RequestBody EliminarNinoRequest request) {
        return claseService.eliminarNinoDeClase(request.getClaseId(), request.getNinoId());
    }
}