package com.pequenospasos.backend.service;

import com.pequenospasos.backend.entity.Educador;
import com.pequenospasos.backend.entity.Usuario;
import com.pequenospasos.backend.enums.Role;
import com.pequenospasos.backend.repository.EducadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

@Service
public class EducadorService {

    @Autowired
    private EducadorRepository educadorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Obtener todos los educadores
    public List<Educador> getAllEducadores() {
        return educadorRepository.findAll();
    }

    // Buscar educador por ID
    public Optional<Educador> getEducadorById(Long id) {
        return educadorRepository.findEducadorById(id);
    }

    // Buscar educador por email
    public Optional<Educador> getEducadorByEmail(String email) {
        return educadorRepository.findByEmail(email);
    }

    // Guardar un nuevo educador (con validación de email único)
    public Educador saveEducador(Educador educador) {
        if (educadorRepository.existsByEmail(educador.getEmail())) {
            throw new RuntimeException("El email ya está registrado.");
        }
        educador.setTipoUsuario(Role.EDUCADOR); // Asegurar que se guarde correctamente
        educador.setPassword(passwordEncoder.encode(educador.getPassword()));
        return educadorRepository.save(educador);
    }

    // Actualizar educador existente, sin sobrescribir la contraseña si no se proporciona
    public Educador updateEducador(Long id, Educador educadorDetalles) {
        Optional<Educador> educadorOptional = educadorRepository.findById(id);
        if (educadorOptional.isPresent()) {
            Educador educador = educadorOptional.get();
            educador.setNombre(educadorDetalles.getNombre());
            educador.setApellidos(educadorDetalles.getApellidos());
            educador.setTelefono(educadorDetalles.getTelefono());
            educador.setEmail(educadorDetalles.getEmail());

            if (educadorDetalles.getPassword() != null && !educadorDetalles.getPassword().isEmpty()) {
                educador.setPassword(passwordEncoder.encode(educadorDetalles.getPassword()));
            }

            return educadorRepository.save(educador);
        } else {
            throw new RuntimeException("Educador no encontrado con id: " + id);
        }
    }

    // Eliminar educador por ID
    public void deleteEducador(Long id) {
        educadorRepository.deleteById(id);
    }
}