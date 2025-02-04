package com.pequenospasos.backend.service;

import com.pequenospasos.backend.entity.Educador;
import com.pequenospasos.backend.repository.EducadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EducadorService {

    @Autowired
    private EducadorRepository educadorRepository;

    // Obtener todos los educadores
    public List<Educador> getAllEducadores() {
        return educadorRepository.findAll();
    }

    // Obtener educador por ID
    public Optional<Educador> getEducadorById(Long id) {
        return educadorRepository.findById(id);
    }

    // Guardar un nuevo educador
    public Educador saveEducador(Educador educador) {
        return educadorRepository.save(educador);
    }

    // Actualizar un educador
    public Educador updateEducador(Long id, Educador educadorDetalles) {
        return educadorRepository.findById(id).map(educador -> {
            educador.setNombre(educadorDetalles.getNombre());
            educador.setApellidos(educadorDetalles.getApellidos());
            educador.setEmail(educadorDetalles.getEmail());
            return educadorRepository.save(educador);
        }).orElseThrow(() -> new RuntimeException("Educador no encontrado con id: " + id));
    }

    // Eliminar un educador
    public void deleteEducador(Long id) {
        educadorRepository.deleteById(id);
    }
}