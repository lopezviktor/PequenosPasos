package com.pequenospasos.backend.controller;

import com.pequenospasos.backend.entity.Usuario;
import com.pequenospasos.backend.security.CustomUserDetails;
import com.pequenospasos.backend.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<Usuario> getAllUsuarios() {
        return usuarioService.getAllUsuarios();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EDUCADOR', 'PADRE')")
    @GetMapping("/{id}")
    public Usuario getUsuarioById(@PathVariable Long id) {
        return usuarioService.getUsuarioById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EDUCADOR')")
    @GetMapping("/buscar")
    public Usuario getUsuarioByEmail(@RequestParam String email, @RequestParam String tipoUsuario) {
        return usuarioService.getUsuarioByEmail(email, tipoUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + email));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/tipo")
    public List<Usuario> getUsuariosPorTipo(@RequestParam String tipoUsuario) {
        return usuarioService.getUsuariosPorTipo(tipoUsuario);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Usuario createUsuario(@RequestBody Usuario usuario) {
        return usuarioService.saveUsuario(usuario);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Usuario updateUsuario(@PathVariable Long id, @RequestBody Usuario usuarioDetalles) {
        return usuarioService.updateUsuario(id, usuarioDetalles);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteUsuario(@PathVariable Long id) {
        usuarioService.deleteUsuario(id);
    }

    @GetMapping("/me")
    public ResponseEntity<Usuario> getUsuarioActual(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Usuario usuario = usuarioService.getUsuarioById(userDetails.getUsuario().getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return ResponseEntity.ok(usuario);
    }
}