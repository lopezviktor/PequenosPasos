package com.pequenospasos.backend.service;

import com.pequenospasos.backend.enums.Role;
import com.pequenospasos.backend.entity.Admin;
import com.pequenospasos.backend.entity.Educador;
import com.pequenospasos.backend.entity.Padre;
import com.pequenospasos.backend.entity.Usuario;
import com.pequenospasos.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> getUsuarioById(Long id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> getUsuarioByEmail(String email, String tipoUsuario) {
        Role role = Role.valueOf(tipoUsuario.toUpperCase());
        return usuarioRepository.findByEmailAndTipoUsuario(email, role);
    }

    public Usuario saveUsuario(Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("El email ya está registrado.");
        }

        if (usuario instanceof Padre) {
            usuario.setTipoUsuario(Role.PADRE);
        } else if (usuario instanceof Educador) {
            usuario.setTipoUsuario(Role.EDUCADOR);
        } else if (usuario instanceof Admin) {
            usuario.setTipoUsuario(Role.ADMIN);
        }

        if (usuario.getPassword() != null && !usuario.getPassword().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }

        return usuarioRepository.save(usuario);
    }

    public Usuario updateUsuario(Long id, Usuario usuarioDetalles) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            usuario.setNombre(usuarioDetalles.getNombre());
            usuario.setApellidos(usuarioDetalles.getApellidos());
            usuario.setEmail(usuarioDetalles.getEmail());
            usuario.setTelefono(usuarioDetalles.getTelefono());

            if (usuarioDetalles.getPassword() != null && !usuarioDetalles.getPassword().isEmpty()) {
                usuario.setPassword(passwordEncoder.encode(usuarioDetalles.getPassword()));
            }

            return usuarioRepository.save(usuario);
        } else {
            throw new RuntimeException("Usuario no encontrado con id: " + id);
        }
    }

    public List<Usuario> getUsuariosPorTipo(String tipoUsuario) {
        Role role = Role.valueOf(tipoUsuario.toUpperCase());
        return usuarioRepository.findByTipoUsuario(role);
    }

    public void deleteUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }
}