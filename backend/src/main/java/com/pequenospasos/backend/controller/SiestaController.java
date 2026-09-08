package com.pequenospasos.backend.controller;

import com.pequenospasos.backend.dto.SiestaResponse;
import com.pequenospasos.backend.entity.Siesta;
import com.pequenospasos.backend.service.SiestaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/siestas")
public class SiestaController {

    @Autowired
    private SiestaService siestaService;

    @PreAuthorize("hasAnyRole('ADMIN', 'EDUCADOR')")
    @GetMapping
    public List<Siesta> getAllSiestas() {
        return siestaService.getAllSiestas();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EDUCADOR', 'PADRE')")
    @GetMapping("/nino/{ninoId}")
    public List<SiestaResponse> getSiestasByNinoId(@PathVariable Long ninoId) {
        return siestaService.getSiestasByNinoId(ninoId);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EDUCADOR')")
    @GetMapping("/educador/{educadorId}")
    public List<Siesta> getSiestasByEducadorId(@PathVariable Long educadorId) {
        return siestaService.getSiestasByEducadorId(educadorId);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EDUCADOR')")
    @GetMapping("/rango-fechas")
    public List<SiestaResponse> getSiestasByFecha(@RequestParam LocalDateTime inicio, @RequestParam LocalDateTime fin) {
        return siestaService.getSiestasByFecha(inicio, fin).stream()
                .map(siesta -> new SiestaResponse(
                        siesta.getId(),
                        siesta.getInicioSiesta().toString(),
                        siesta.getFinSiesta() != null ? siesta.getFinSiesta().toString() : null,
                        siesta.getEducador().getNombre() + " " + siesta.getEducador().getApellidos(),
                        siesta.getObservaciones()
                )).collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('EDUCADOR')")
    @GetMapping("/rango-fechas/entidad")
    public List<Siesta> getSiestasEntidadByFecha(@RequestParam String inicio, @RequestParam String fin) {
        LocalDateTime fechaInicio = LocalDateTime.parse(inicio.trim());
        LocalDateTime fechaFin = LocalDateTime.parse(fin.trim());
        return siestaService.getSiestasByFecha(fechaInicio, fechaFin);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EDUCADOR', 'PADRE')")
    @GetMapping("/nino/{ninoId}/ultima")
    public SiestaResponse getUltimaSiestaByNinoId(@PathVariable Long ninoId) {
        Siesta siesta = siestaService.getUltimaSiestaByNinoId(ninoId)
                .orElseThrow(() -> new RuntimeException("No se encontró ninguna siesta registrada para este niño."));
        return new SiestaResponse(
                siesta.getId(),
                siesta.getInicioSiesta().toString(),
                siesta.getFinSiesta() != null ? siesta.getFinSiesta().toString() : null,
                siesta.getEducador().getNombre() + " " + siesta.getEducador().getApellidos(),
                siesta.getObservaciones()
        );
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EDUCADOR', 'PADRE')")
    @GetMapping("/{id}")
    public Siesta getSiestaById(@PathVariable Long id) {
        return siestaService.getSiestaById(id)
                .orElseThrow(() -> new RuntimeException("Siesta no encontrada con id: " + id));
    }

    @PreAuthorize("hasRole('EDUCADOR')")
    @PostMapping
    public Siesta createSiesta(@RequestBody Siesta siesta) {
        return siestaService.saveSiesta(siesta);
    }

    @PreAuthorize("hasRole('EDUCADOR')")
    @PutMapping("/{id}")
    public Siesta updateSiesta(@PathVariable Long id, @RequestBody Siesta siestaDetalles) {
        Siesta siesta = siestaService.getSiestaById(id)
                .orElseThrow(() -> new RuntimeException("Siesta no encontrada con id: " + id));

        if (!siesta.getEducador().getId().equals(siestaDetalles.getEducador().getId())) {
            throw new RuntimeException("Solo el educador que creó la siesta puede modificarla.");
        }

        return siestaService.updateSiesta(id, siestaDetalles);
    }

    @PreAuthorize("hasRole('EDUCADOR')")
    @DeleteMapping("/{id}")
    public void deleteSiesta(@PathVariable Long id) {
        siestaService.deleteSiesta(id);
    }
}