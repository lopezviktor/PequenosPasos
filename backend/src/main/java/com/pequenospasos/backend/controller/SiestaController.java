package com.pequenospasos.backend.controller;

import com.pequenospasos.backend.entity.Siesta;
import com.pequenospasos.backend.service.SiestaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/siestas")
public class SiestaController {

    @Autowired
    private SiestaService siestaService;

    // Obtener todas las siestas registradas
    @GetMapping
    public List<Siesta> getAllSiestas() {
        return siestaService.getAllSiestas();
    }

    // Obtener siestas de un niño específico
    @GetMapping("/nino/{ninoId}")
    public List<Siesta> getSiestasByNinoId(@PathVariable Long ninoId) {
        return siestaService.getSiestasByNinoId(ninoId);
    }

    // Obtener siestas registradas por un educador
    @GetMapping("/educador/{educadorId}")
    public List<Siesta> getSiestasByEducadorId(@PathVariable Long educadorId) {
        return siestaService.getSiestasByEducadorId(educadorId);
    }

    // Obtener siestas en un rango de fechas
    @GetMapping("/rango-fechas")
    public List<Siesta> getSiestasByFecha(@RequestParam LocalDateTime inicio, @RequestParam LocalDateTime fin) {
        return siestaService.getSiestasByFecha(inicio, fin);
    }

    // Obtener la última siesta de un niño
    @GetMapping("/nino/{ninoId}/ultima")
    public Optional<Siesta> getUltimaSiestaByNinoId(@PathVariable Long ninoId) {
        return Optional.ofNullable(siestaService.getUltimaSiestaByNinoId(ninoId));
    }

    // Obtener una siesta por ID
    @GetMapping("/{id}")
    public Optional<Siesta> getSiestaById(@PathVariable Long id) {
        return siestaService.getSiestaById(id);
    }

    // Registrar una nueva siesta
    @PostMapping
    public Siesta createSiesta(@RequestBody Siesta siesta) {
        return siestaService.saveSiesta(siesta);
    }

    // Actualizar una siesta
    @PutMapping("/{id}")
    public Siesta updateSiesta(@PathVariable Long id, @RequestBody Siesta siesta) {
        return siestaService.updateSiesta(id, siesta);
    }

    // Eliminar una siesta
    @DeleteMapping("/{id}")
    public void deleteSiesta(@PathVariable Long id) {
        siestaService.deleteSiesta(id);
    }
}