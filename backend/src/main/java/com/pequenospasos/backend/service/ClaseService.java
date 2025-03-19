package com.pequenospasos.backend.service;

import com.pequenospasos.backend.entity.Clase;
import com.pequenospasos.backend.entity.Educador;
import com.pequenospasos.backend.entity.Nino;
import com.pequenospasos.backend.repository.ClaseRepository;
import com.pequenospasos.backend.repository.EducadorRepository;
import com.pequenospasos.backend.repository.NinoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ClaseService {

    @Autowired
    private ClaseRepository claseRepository;

    @Autowired
    private EducadorRepository educadorRepository;

    @Autowired
    private NinoRepository ninoRepository;

    // Obtener todas las clases
    public List<Clase> getAllClases() {
        return claseRepository.findAll();
    }

    // Obtener clase por ID
    public Optional<Clase> getClaseById(Long id) {
        return claseRepository.findById(id);
    }

    // Crear una nueva clase
    public Clase crearClase(String nombre, Long educadorId) {
        Educador educador = educadorRepository.findById(educadorId)
                .orElseThrow(() -> new RuntimeException("Educador no encontrado"));

        Clase clase = new Clase(nombre, educador);
        return claseRepository.save(clase);
    }

    // Asignar un niño a una clase
    @Transactional
    public Clase asignarNinoAClase(Long claseId, Long ninoId) {
        Clase clase = claseRepository.findById(claseId)
                .orElseThrow(() -> new RuntimeException("Clase no encontrada"));

        Nino nino = ninoRepository.findById(ninoId)
                .orElseThrow(() -> new RuntimeException("Niño no encontrado"));

        // Asignar la clase al niño
        nino.setClase(clase);

        // Guardar niño para que se actualice en la BD
        ninoRepository.save(nino);

        // Guardar clase para que se actualice en la BD
        return claseRepository.save(clase);
    }
}