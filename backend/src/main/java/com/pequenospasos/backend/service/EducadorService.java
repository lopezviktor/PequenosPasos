package com.pequenospasos.backend.service;

import com.pequenospasos.backend.entity.Educador;
import com.pequenospasos.backend.entity.Usuario;
import com.pequenospasos.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EducadorService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Obtener todos los educadores
    public List<Educador> getAllEducadores() {
        return usuarioRepository.findByTipoUsuario("EDUCADOR").stream()
                .map(u -> (Educador) u)
                .toList();
    }

    // Buscar educador por ID
    public Optional<Educador> getEducadorById(Long id) {
        return usuarioRepository.findById(id)
                .filter(u -> u instanceof Educador)
                .map(u -> (Educador) u);
    }

    // Buscar educador por email
    public Optional<Educador> getEducadorByEmail(String email) {
        return usuarioRepository.findByEmailAndTipoUsuario(email, "EDUCADOR")
                .map(u -> (Educador) u);
    }

    // Guardar un nuevo educador (con validación de email único)
    public Educador saveEducador(Educador educador) {
        if (usuarioRepository.existsByEmail(educador.getEmail())) {
            throw new RuntimeException("El email ya está registrado.");
        }
        educador.setTipoUsuario("EDUCADOR"); // Asegurar que se guarde correctamente
        return usuarioRepository.save(educador);
    }

    // Actualizar educador existente, sin sobrescribir la contraseña si no se proporciona
    public Educador updateEducador(Long id, Educador educadorDetalles) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        if (usuarioOptional.isPresent() && usuarioOptional.get() instanceof Educador educador) {
            educador.setNombre(educadorDetalles.getNombre());
            educador.setApellidos(educadorDetalles.getApellidos());
            educador.setEmail(educadorDetalles.getEmail());

            if (educadorDetalles.getPassword() != null && !educadorDetalles.getPassword().isEmpty()) {
                educador.setPassword(educadorDetalles.getPassword());
            }

            return usuarioRepository.save(educador);
        } else {
            throw new RuntimeException("Educador no encontrado con id: " + id);
        }
    }

    // Eliminar educador por ID
    public void deleteEducador(Long id) {
        usuarioRepository.deleteById(id);
    }
}