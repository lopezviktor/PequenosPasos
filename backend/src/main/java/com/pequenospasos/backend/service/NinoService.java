package com.pequenospasos.backend.service;

import com.pequenospasos.backend.entity.Nino;
import com.pequenospasos.backend.entity.Padre;
import com.pequenospasos.backend.repository.NinoRepository;
import com.pequenospasos.backend.repository.PadreRepository;
import com.pequenospasos.backend.repository.PadresHijosRepository; // Añadir la importación de PadresHijosRepository
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

    @Autowired
    private PadresHijosRepository padresHijosRepository; // Añadir la inyección de PadresHijosRepository

    // Obtener todos los niños
    @Transactional(readOnly = true)
    public List<Nino> getAllNinos() {
        return ninoRepository.findAll();
    }

    // Buscar niño por ID
    public Nino getNinoById(Long id) {
        return ninoRepository.findById(id)
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
        return padresHijosRepository.findNinosByPadreId(padreId); // Cambiar a padresHijosRepository
    }

    // Guardar un nuevo niño
    public Nino saveNino(Nino nino) {
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

        return ninoRepository.save(existingNino);
    }

    // Eliminar niño por ID
    public void deleteNino(Long id) {
        ninoRepository.deleteById(id);
    }
}