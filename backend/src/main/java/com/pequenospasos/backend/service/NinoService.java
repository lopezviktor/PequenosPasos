package com.pequenospasos.backend.service;

import com.pequenospasos.backend.entity.Nino;
import com.pequenospasos.backend.repository.NinoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NinoService {

    @Autowired
    private NinoRepository ninoRepository;

    // Obtener todos los niños
    public List<Nino> getAllNinos() {
        return ninoRepository.findAll();
    }

    // Buscar niño por ID
    public Optional<Nino> getNinoById(Long id) {
        return ninoRepository.findById(id);
    }

    // Buscar niño por nombre (ignorando mayúsculas y minúsculas)
    public List<Nino> getNinoByNombre(String nombre) {
        return ninoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    // Guardar un nuevo niño
    public Nino saveNino(Nino nino) {
        return ninoRepository.save(nino);
    }

    // Actualizar datos de un niño
    public Nino updateNino(Long id, Nino ninoDetalles) {
        Optional<Nino> ninoOptional = ninoRepository.findById(id);
        if (ninoOptional.isPresent()) {
            Nino nino = ninoOptional.get();
            nino.setNombre(ninoDetalles.getNombre());
            nino.setApellidos(ninoDetalles.getApellidos());
            nino.setFechaNacimiento(ninoDetalles.getFechaNacimiento());
            nino.setAlergias(ninoDetalles.getAlergias());
            nino.setCondicionesMedicas(ninoDetalles.getCondicionesMedicas());
            return ninoRepository.save(nino);
        } else {
            throw new RuntimeException("Niño no encontrado con id: " + id);
        }
    }

    // Eliminar niño por ID
    public void deleteNino(Long id) {
        ninoRepository.deleteById(id);
    }
}