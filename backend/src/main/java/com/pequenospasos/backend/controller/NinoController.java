package com.pequenospasos.backend.controller;

import com.pequenospasos.backend.entity.Nino;
import com.pequenospasos.backend.entity.Usuario;
import com.pequenospasos.backend.repository.UsuarioRepository;
import com.pequenospasos.backend.service.NinoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ninos")
public class NinoController {


    @Autowired
    private NinoService ninoService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Obtener todos los niños
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'EDUCADOR')")
    @GetMapping
    public List<Nino> getAllNinos() {
        return ninoService.getAllNinos();
    }

    // Obtener niño por ID con validación
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'EDUCADOR')")
    @GetMapping("/{id}")
    public Nino getNinoById(@PathVariable Long id) {
        return ninoService.getNinoById(id);
    }

    // Buscar niño por nombre
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'EDUCADOR')")
    @GetMapping("/buscar")
    public List<Nino> getNinoByNombre(@RequestParam String nombre) {
        return ninoService.getNinoByNombre(nombre);
    }

    // Obtener niños de su padre autenticado
    @PreAuthorize("hasRole('PADRE')")
    @GetMapping("/padre/{padreId}")
    public ResponseEntity<List<Nino>> getNinosByPadre(@PathVariable Long padreId) {
        List<Nino> ninos = ninoService.getNinosByPadreId(padreId);
        return ResponseEntity.ok(ninos);
    }

    // Obtener niños por ID de clase
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'EDUCADOR')")
    @GetMapping("/clase/{claseId}")
    public ResponseEntity<List<Nino>> getNinosByClaseId(@PathVariable Long claseId) {
        List<Nino> ninos = ninoService.getNinosByClaseId(claseId);
        return ResponseEntity.ok(ninos);
    }

    // Crear un nuevo niño
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'EDUCADOR')")
    @PostMapping
    public Nino createNino(@RequestBody Nino nino) {
        return ninoService.saveNino(nino);
    }

    // Actualizar un niño existente
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'EDUCADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<Nino> updateNino(@PathVariable Long id, @RequestBody Nino updatedNino) {
        Nino existingNino = ninoService.getNinoById(id); // Buscar el niño existente

        // Actualizar los demás campos
        existingNino.setNombre(updatedNino.getNombre());
        existingNino.setApellidos(updatedNino.getApellidos());
        existingNino.setFechaNacimiento(updatedNino.getFechaNacimiento());
        existingNino.setPrimerDia(updatedNino.getPrimerDia());
        existingNino.setAlergias(updatedNino.getAlergias());
        existingNino.setCondicionesMedicas(updatedNino.getCondicionesMedicas());
        existingNino.setFotoUrl(updatedNino.getFotoUrl());
        existingNino.setClase(updatedNino.getClase());
        return ResponseEntity.ok(ninoService.updateNino(id, existingNino));
    }

    // Eliminar un niño con validación de existencia
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'EDUCADOR')")
    @DeleteMapping("/{id}")
    public void deleteNino(@PathVariable Long id) {
        ninoService.deleteNino(id);
    }

}