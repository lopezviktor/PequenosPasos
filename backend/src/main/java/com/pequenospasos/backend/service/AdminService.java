package com.pequenospasos.backend.service;

import com.pequenospasos.backend.entity.Admin;
import com.pequenospasos.backend.entity.Usuario;
import com.pequenospasos.backend.enums.Role;
import com.pequenospasos.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Obtener todos los administradores
    public List<Admin> getAllAdmins() {
        return usuarioRepository.findByTipoUsuario(Role.ADMIN).stream()
                .map(u -> (Admin) u)
                .toList();
    }

    // Buscar administrador por ID
    public Optional<Admin> getAdminById(Long id) {
        return usuarioRepository.findById(id)
                .filter(u -> u instanceof Admin)
                .map(u -> (Admin) u);
    }

    // Buscar administrador por email
    public Optional<Admin> getAdminByEmail(String email) {
        return usuarioRepository.findByEmailAndTipoUsuario(email, Role.ADMIN)
                .map(u -> (Admin) u);
    }

    // Guardar un nuevo administrador (con validación de email único)
    public Admin saveAdmin(Admin admin) {
        if (usuarioRepository.existsByEmail(admin.getEmail())) {
            throw new RuntimeException("El email ya está registrado.");
        }
        admin.setTipoUsuario(Role.ADMIN); // Asegurar que se guarde correctamente
        return usuarioRepository.save(admin);
    }

    // Actualizar administrador existente, sin sobrescribir la contraseña si no se proporciona
    public Admin updateAdmin(Long id, Admin adminDetalles) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        if (usuarioOptional.isPresent() && usuarioOptional.get() instanceof Admin admin) {
            admin.setNombre(adminDetalles.getNombre());
            admin.setApellidos(adminDetalles.getApellidos());
            admin.setEmail(adminDetalles.getEmail());

            if (adminDetalles.getPassword() != null && !adminDetalles.getPassword().isEmpty()) {
                admin.setPassword(adminDetalles.getPassword());
            }

            return usuarioRepository.save(admin);
        } else {
            throw new RuntimeException("Administrador no encontrado con id: " + id);
        }
    }

    // Eliminar administrador por ID
    public void deleteAdmin(Long id) {
        usuarioRepository.deleteById(id);
    }
}