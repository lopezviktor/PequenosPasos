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

    // Obtener todos los padres correctamente
    @GetMapping
    public List<Padre> getAllPadres() {
        return padreService.findAllPadres();
    }

    // Obtener padre por ID con validación
    @GetMapping("/{id}")
    public Padre getPadreById(@PathVariable Long id) {
        return padreService.findPadreById(id)
                .orElseThrow(() -> new RuntimeException("Padre no encontrado con id: " + id));
    }

    // Obtener niños asociados a un padre
    @GetMapping("/{padreId}/ninos")
    public List<Nino> getNinosByPadreId(@PathVariable Long padreId) {
        return padresHijosService.getNinosByPadreId(padreId);
    }

    // Buscar padre por email
    @GetMapping("/buscar")
    public Padre getPadreByEmail(@RequestParam String email) {
        return padreService.findPadreByEmail(email)
                .orElseThrow(() -> new RuntimeException("Padre no encontrado con email: " + email));
    }

    // Buscar padres por apellido
    @GetMapping("/buscarPorApellidos")
    public List<Padre> getPadresPorApellidos(@RequestParam String apellidos) {
        return padreService.findPadresPorApellidos(apellidos);
    }

    // Crear un nuevo padre con validación
    @PostMapping
    public Padre createPadre(@RequestBody Padre padre) {
        padre.setTipoUsuario("PADRE"); // 🔹 Asegurar que el usuario creado es un PADRE
        return padreService.savePadre(padre);
    }

    // Actualizar un padre existente sin sobrescribir contraseña si no se envía
    @PutMapping("/{id}")
    public Padre updatePadre(@PathVariable Long id, @RequestBody Padre padreDetalles) {
        return padreService.updatePadre(id, padreDetalles);
    }

    // Eliminar un padre con validación de existencia
    @DeleteMapping("/{id}")
    public void deletePadre(@PathVariable Long id) {
        padreService.deletePadre(id);
    }
}