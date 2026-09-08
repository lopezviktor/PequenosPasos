package com.pequenospasos.backend.controller;

import com.pequenospasos.backend.entity.Nino;
import com.pequenospasos.backend.entity.Padre;
import com.pequenospasos.backend.enums.Role;
import com.pequenospasos.backend.service.PadreService;
import com.pequenospasos.backend.service.PadresHijosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<Padre> getAllPadres() {
        return padreService.findAllPadres();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EDUCADOR')")
    @GetMapping("/{id}")
    public Padre getPadreById(@PathVariable Long id) {
        return padreService.findPadreById(id)
                .orElseThrow(() -> new RuntimeException("Padre no encontrado con id: " + id));
    }

    @GetMapping("/mis-ninos")
    @PreAuthorize("hasRole('PADRE')")
    public List<Nino> getNinosByPadre(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        return padresHijosService.getNinosByUsuario(username);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EDUCADOR')")
    @GetMapping("/{padreId}/ninos")
    public List<Nino> getNinosByPadreId(@PathVariable Long padreId) {
        return padresHijosService.getNinosByPadreId(padreId);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EDUCADOR')")
    @GetMapping("/buscar")
    public Padre getPadreByEmail(@RequestParam String email) {
        return padreService.findPadreByEmail(email)
                .orElseThrow(() -> new RuntimeException("Padre no encontrado con email: " + email));
    }

    @GetMapping("/buscarPorApellidos")
    public List<Padre> getPadresPorApellidos(@RequestParam String apellidos) {
        return padreService.findPadresPorApellidos(apellidos);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Padre createPadre(@RequestBody Padre padre) {
        padre.setTipoUsuario(Role.PADRE);
        return padreService.savePadre(padre);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Padre updatePadre(@PathVariable Long id, @RequestBody Padre padreDetalles) {
        return padreService.updatePadre(id, padreDetalles);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deletePadre(@PathVariable Long id) {
        padreService.deletePadre(id);
    }
}