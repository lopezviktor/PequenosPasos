package com.pequenospasos.backend.controller;

import com.pequenospasos.backend.entity.Siesta;
import com.pequenospasos.backend.service.SiestaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

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

    // Obtener siestas de un educador específico
    @GetMapping("/educador/{educadorId}")
    public List<Siesta> getSiestasByEducadorId(@PathVariable Long educadorId) {
        return siestaService.getSiestasByEducadorId(educadorId);
    }

    // Obtener siestas en un rango de fechas
    @GetMapping("/rango-fechas")
    public List<Siesta> getSiestasByFecha(@RequestParam LocalDateTime inicio, @RequestParam LocalDateTime fin) {
        return siestaService.getSiestasByFecha(inicio, fin);
    }

    // Obtener la última siesta de un niño con validación
    @GetMapping("/nino/{ninoId}/ultima")
    public Siesta getUltimaSiestaByNinoId(@PathVariable Long ninoId) {
        return siestaService.getUltimaSiestaByNinoId(ninoId)
                .orElseThrow(() -> new RuntimeException("No se encontró ninguna siesta registrada para este niño."));
    }

    // Obtener una siesta por ID con validación
    @GetMapping("/{id}")
    public Siesta getSiestaById(@PathVariable Long id) {
        return siestaService.getSiestaById(id)
                .orElseThrow(() -> new RuntimeException("Siesta no encontrada con id: " + id));
    }

    // Registrar una nueva siesta asegurando que solo EDUCADORES puedan hacerlo
    @PostMapping
    public Siesta createSiesta(@RequestBody Siesta siesta) {
        return siestaService.saveSiesta(siesta);
    }

    // Actualizar una siesta validando que solo el educador que la creó pueda modificarla
    @PutMapping("/{id}")
    public Siesta updateSiesta(@PathVariable Long id, @RequestBody Siesta siestaDetalles) {
        Siesta siesta = siestaService.getSiestaById(id)
                .orElseThrow(() -> new RuntimeException("Siesta no encontrada con id: " + id));

        if (!siesta.getEducador().getId().equals(siestaDetalles.getEducador().getId())) {
            throw new RuntimeException("Solo el educador que creó la siesta puede modificarla.");
        }

        return siestaService.updateSiesta(id, siestaDetalles);
    }

    // Eliminar una siesta con validación de existencia
    @DeleteMapping("/{id}")
    public void deleteSiesta(@PathVariable Long id) {
        siestaService.deleteSiesta(id);
    }
}