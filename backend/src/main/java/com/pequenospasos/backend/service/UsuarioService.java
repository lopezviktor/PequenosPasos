package com.pequenospasos.backend.service;

import com.pequenospasos.backend.entity.Admin;
import com.pequenospasos.backend.entity.Educador;
import com.pequenospasos.backend.entity.Padre;
import com.pequenospasos.backend.entity.Usuario;
import com.pequenospasos.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Obtener todos los usuarios
    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    // Buscar usuario por ID
    public Optional<Usuario> getUsuarioById(Long id) {
        return usuarioRepository.findById(id);
    }

    // Buscar usuario por email y tipoUsuario (para filtrar PADRE, EDUCADOR o ADMIN)
    public Optional<Usuario> getUsuarioByEmail(String email, String tipoUsuario) {
        return usuarioRepository.findByEmailAndTipoUsuario(email, tipoUsuario);
    }

    // Guardar un nuevo usuario con validación de email único
    public Usuario saveUsuario(Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("El email ya está registrado.");
        }

        // 🔹 Verificar el tipo de usuario antes de guardar
        if (usuario instanceof Padre) {
            usuario.setTipoUsuario("PADRE");
        } else if (usuario instanceof Educador) {
            usuario.setTipoUsuario("EDUCADOR");
        } else if (usuario instanceof Admin) {
            usuario.setTipoUsuario("ADMIN");
        }

        if (usuario.getPassword() != null && !usuario.getPassword().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }

        return usuarioRepository.save(usuario);
    }

    // Actualizar usuario existente, sin sobrescribir la contraseña si no se proporciona
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

    // Obtener usuarios por tipo
    public List<Usuario> getUsuariosPorTipo(String tipoUsuario) {
        return usuarioRepository.findByTipoUsuario(tipoUsuario);
    }

    // Eliminar usuario por ID
    public void deleteUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }
}
