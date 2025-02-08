package com.pequenospasos.backend.service;

import com.pequenospasos.backend.entity.Nino;
import com.pequenospasos.backend.entity.Padre;
import com.pequenospasos.backend.repository.NinoRepository;
import com.pequenospasos.backend.repository.PadreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class NinoService {

    @Autowired
    private NinoRepository ninoRepository;

    @Autowired
    private PadreRepository padreRepository;

    // Obtener todos los niños
    @Transactional(readOnly = true)
    public List<Nino> getAllNinos() {
        return ninoRepository.findAllWithPadre();
    }

    // Buscar niño por ID
    public Nino getNinoById(Long id) {
        return ninoRepository.findByIdWithPadre(id)
                .orElseThrow(() -> new RuntimeException("Niño no encontrado"));
    }

    // Buscar niño por nombre (ignorando mayúsculas y minúsculas)
    public List<Nino> getNinoByNombre(String nombre) {
        return ninoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    // Buscar niño por apellidos
    public List<Nino> getNinoByApellidos(String apellidos) {
        return ninoRepository.findByApellidosContainingIgnoreCase(apellidos);
    }

    // Buscar niños por ID de padre (filtrando solo PADRES)
    public List<Nino> getNinosByPadreId(Long padreId) {
        return ninoRepository.findByPadreIdFiltered(padreId);
    }

    // Guardar un nuevo niño
    public Nino saveNino(Nino nino) {
        if (nino.getPadre() == null || nino.getPadre().getId() == null) {
            throw new IllegalArgumentException("El padre debe tener un ID válido");
        }

        Padre padre = (Padre) padreRepository.findById(nino.getPadre().getId())
                .orElseThrow(() -> new RuntimeException("Padre no encontrado con ID: " + nino.getPadre().getId()));

        nino.setPadre(padre);
        return ninoRepository.save(nino);
    }

    // Actualizar datos de un niño
    @Transactional
    public Nino updateNino(Long id, Nino updatedNino) {
        Nino existingNino = ninoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Niño no encontrado"));

        existingNino.setNombre(updatedNino.getNombre());
        existingNino.setApellidos(updatedNino.getApellidos());
        existingNino.setFechaNacimiento(updatedNino.getFechaNacimiento());
        existingNino.setPrimerDia(updatedNino.getPrimerDia());
        existingNino.setAlergias(updatedNino.getAlergias());
        existingNino.setCondicionesMedicas(updatedNino.getCondicionesMedicas());
        existingNino.setFotoUrl(updatedNino.getFotoUrl());
        existingNino.setPadre(updatedNino.getPadre()); // Asegura que el padre es persistido correctamente

        return existingNino;
    }

    // Eliminar niño por ID
    public void deleteNino(Long id) {
        ninoRepository.deleteById(id);
    }
}