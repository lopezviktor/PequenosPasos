package com.pequenospasos.backend.controller;

import com.pequenospasos.backend.entity.Nino;
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

    @PreAuthorize("hasAnyRole('ADMIN', 'EDUCADOR')")
    @GetMapping
    public List<Nino> getAllNinos() {
        return ninoService.getAllNinos();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EDUCADOR')")
    @GetMapping("/{id}")
    public Nino getNinoById(@PathVariable Long id) {
        return ninoService.getNinoById(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EDUCADOR')")
    @GetMapping("/buscar")
    public List<Nino> getNinoByNombre(@RequestParam String nombre) {
        return ninoService.getNinoByNombre(nombre);
    }

    @PreAuthorize("hasRole('PADRE')")
    @GetMapping("/padre/{padreId}")
    public ResponseEntity<List<Nino>> getNinosByPadre(@PathVariable Long padreId) {
        List<Nino> ninos = ninoService.getNinosByPadreId(padreId);
        return ResponseEntity.ok(ninos);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EDUCADOR')")
    @GetMapping("/clase/{claseId}")
    public ResponseEntity<List<Nino>> getNinosByClaseId(@PathVariable Long claseId) {
        List<Nino> ninos = ninoService.getNinosByClaseId(claseId);
        return ResponseEntity.ok(ninos);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EDUCADOR')")
    @PostMapping
    public Nino createNino(@RequestBody Nino nino) {
        return ninoService.saveNino(nino);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EDUCADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<Nino> updateNino(@PathVariable Long id, @RequestBody Nino updatedNino) {
        Nino existingNino = ninoService.getNinoById(id);
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

    @PreAuthorize("hasAnyRole('ADMIN', 'EDUCADOR')")
    @DeleteMapping("/{id}")
    public void deleteNino(@PathVariable Long id) {
        ninoService.deleteNino(id);
    }
}