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

    // Obtener todos los usuarios
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping
    public List<Usuario> getAllUsuarios() {
        return usuarioService.getAllUsuarios();
    }

    // Obtener usuario por ID
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'EDUCADOR', 'PADRE')")
    @GetMapping("/{id}")
    public Usuario getUsuarioById(@PathVariable Long id) {
        return usuarioService.getUsuarioById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
    }

    // Obtener usuario por email y tipoUsuario
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'EDUCADOR')")
    @GetMapping("/buscar")
    public Usuario getUsuarioByEmail(@RequestParam String email, @RequestParam String tipoUsuario) {
        return usuarioService.getUsuarioByEmail(email, tipoUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + email));
    }

    // Obtener usuario por tipoUsuario
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/tipo")
    public List<Usuario> getUsuariosPorTipo(@RequestParam String tipoUsuario) {
        return usuarioService.getUsuariosPorTipo(tipoUsuario);
    }

    // Crear un nuevo usuario
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping
    public Usuario createUsuario(@RequestBody Usuario usuario) {
        return usuarioService.saveUsuario(usuario);
    }

    // Actualizar usuario existente
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping("/{id}")
    public Usuario updateUsuario(@PathVariable Long id, @RequestBody Usuario usuarioDetalles) {
        return usuarioService.updateUsuario(id, usuarioDetalles);
    }

    // Eliminar usuario por ID
    @PreAuthorize("hasRole('ADMINISTRADOR')")
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