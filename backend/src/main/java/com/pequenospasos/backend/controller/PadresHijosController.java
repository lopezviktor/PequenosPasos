package com.pequenospasos.backend.controller;

import com.pequenospasos.backend.entity.Nino;
import com.pequenospasos.backend.entity.Padre;
import com.pequenospasos.backend.entity.PadresHijos;
import com.pequenospasos.backend.service.PadresHijosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/padres-hijos")
public class PadresHijosController {

    @Autowired
    private PadresHijosService padresHijosService;

    // Obtener los niños de un padre con validación
    @GetMapping("/padre/{padreId}/ninos")
    public List<Nino> getNinosByPadreId(@PathVariable Long padreId) {
        List<Nino> ninos = padresHijosService.getNinosByPadreId(padreId);
        if (ninos.isEmpty()) {
            throw new RuntimeException("No se encontraron niños para el padre con id: " + padreId);
        }
        return ninos;
    }

    @GetMapping("/nino/{ninoId}/padres")
    public List<Padre> getPadresByNinoId(@PathVariable Long ninoId) {
        return padresHijosService.getPadresByNinoId(ninoId);
    }

    // Asignar un niño a un padre con validación
    @PostMapping("/asignar")
    public PadresHijos asignarNinoAPadre(@RequestParam Long padreId, @RequestParam Long ninoId) {
        if (padreId == null || ninoId == null) {
            throw new RuntimeException("Los IDs de padre y niño no pueden ser nulos.");
        }
        Padre padre = padresHijosService.obtenerPadrePorId(padreId);
        Nino nino = padresHijosService.obtenerNinoPorId(ninoId);
        return padresHijosService.asignarNinoAPadre(padre, nino);
    }

    // Eliminar la relación entre un padre y un niño con validación
    @DeleteMapping("/eliminar")
    public void eliminarRelacionPadreHijo(@RequestParam Long padreId, @RequestParam Long ninoId) {
        if (padreId == null || ninoId == null) {
            throw new RuntimeException("Los IDs de padre y niño no pueden ser nulos.");
        }

        Padre padre = padresHijosService.obtenerPadrePorId(padreId);
        Nino nino = padresHijosService.obtenerNinoPorId(ninoId);
        padresHijosService.eliminarRelacionPadreNino(padre.getId(), nino.getId());
    }
}