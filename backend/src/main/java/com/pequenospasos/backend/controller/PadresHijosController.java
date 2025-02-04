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

    // Obtener los niños de un padre
    @GetMapping("/padre/{padreId}/ninos")
    public List<Nino> getNinosByPadreId(@PathVariable Long padreId) {
        return padresHijosService.getNinosByPadreId(padreId);
    }

    // Asignar un niño a un padre
    @PostMapping("/asignar")
    public PadresHijos asignarNinoAPadre(@RequestParam Long padreId, @RequestParam Long ninoId) {
        return padresHijosService.asignarNinoAPadre(padreId, ninoId);
    }

    // Eliminar la relación entre un padre y un niño
    @DeleteMapping("/eliminar")
    public void eliminarRelacionPadreHijo(@RequestParam Long padreId, @RequestParam Long ninoId) {
        padresHijosService.deleteRelacionByPadreAndNino(padreId, ninoId);
    }
}