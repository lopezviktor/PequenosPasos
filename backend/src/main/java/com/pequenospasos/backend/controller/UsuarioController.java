package com.pequenospasos.backend.controller;

import com.pequenospasos.backend.entity.Usuario;
import com.pequenospasos.backend.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // Obtener todos los usuarios
    @GetMapping
    public List<Usuario> getAllUsuarios() {
        return usuarioService.getAllUsuarios();
    }

    // Obtener usuario por ID
    @GetMapping("/{id}")
    public Usuario getUsuarioById(@PathVariable Long id) {
        return usuarioService.getUsuarioById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
    }

    // Obtener usuario por email y tipoUsuario
    @GetMapping("/buscar")
    public Usuario getUsuarioByEmail(@RequestParam String email, @RequestParam String tipoUsuario) {
        return usuarioService.getUsuarioByEmail(email, tipoUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + email));
    }

    // Obtener usuario por tipoUsuario
    @GetMapping("/tipo")
    public List<Usuario> getUsuariosPorTipo(@RequestParam String tipoUsuario) {
        return usuarioService.getUsuariosPorTipo(tipoUsuario);
    }

    // Crear un nuevo usuario
    @PostMapping
    public Usuario createUsuario(@RequestBody Usuario usuario) {
        return usuarioService.saveUsuario(usuario);
    }

    // Actualizar usuario existente
    @PutMapping("/{id}")
    public Usuario updateUsuario(@PathVariable Long id, @RequestBody Usuario usuarioDetalles) {
        return usuarioService.updateUsuario(id, usuarioDetalles);
    }

    // Eliminar usuario por ID
    @DeleteMapping("/{id}")
    public void deleteUsuario(@PathVariable Long id) {
        usuarioService.deleteUsuario(id);
    }
}