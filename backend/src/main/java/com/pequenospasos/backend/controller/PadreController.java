package com.pequenospasos.backend.controller;

import com.pequenospasos.backend.entity.Nino;
import com.pequenospasos.backend.entity.Padre;
import com.pequenospasos.backend.service.PadreService;
import com.pequenospasos.backend.service.PadresHijosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/padres")
public class PadreController {

    @Autowired
    private PadreService padreService;

    @Autowired
    private PadresHijosService padresHijosService;

    // Obtener todos los padres
    @GetMapping
    public List<Padre> getAllPadres() {
        return padreService.getAllPadres();
    }

    // Obtener padre por ID
    @GetMapping("/{id}")
    public Optional<Padre> getPadreById(@PathVariable Long id) {
        return padreService.getPadreById(id);
    }

    // Obtener los niños de un padre
    @GetMapping("/{padreId}/ninos")
    public List<Nino> getNinosByPadreId(@PathVariable Long padreId) {
        return padresHijosService.getNinosByPadreId(padreId);
    }

    // Crear un nuevo padre
    @PostMapping
    public Padre createPadre(@RequestBody Padre padre) {
        return padreService.savePadre(padre);
    }

    // Actualizar un padre
    @PutMapping("/{id}")
    public Padre updatePadre(@PathVariable Long id, @RequestBody Padre padre) {
        return padreService.updatePadre(id, padre);
    }

    // Eliminar un padre
    @DeleteMapping("/{id}")
    public void deletePadre(@PathVariable Long id) {
        padreService.deletePadre(id);
    }
}