package com.pequenospasos.backend.service;

import com.pequenospasos.backend.entity.Nino;
import com.pequenospasos.backend.entity.Padre;
import com.pequenospasos.backend.entity.PadresHijos;
import com.pequenospasos.backend.repository.NinoRepository;
import com.pequenospasos.backend.repository.PadreRepository;
import com.pequenospasos.backend.repository.PadresHijosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PadresHijosService {

    @Autowired
    private PadresHijosRepository padresHijosRepository;

    @Autowired
    private PadreRepository padreRepository;  // Repositorio de Padres

    @Autowired
    private NinoRepository ninoRepository;    // Repositorio de Niños

    // Obtener los niños de un padre
    public List<Nino> getNinosByPadreId(Long padreId) {
        List<PadresHijos> relaciones = padresHijosRepository.findByPadreId(padreId);
        return relaciones.stream().map(PadresHijos::getNino).collect(Collectors.toList());
    }

    // Obtener un Padre por su ID
    public Padre findPadreById(Long padreId) {
        return padreRepository.findById(padreId).orElse(null);
    }

    // Obtener un Niño por su ID
    public Nino findNinoById(Long ninoId) {
        return ninoRepository.findById(ninoId).orElse(null);
    }

    // Asignar un niño a un padre
    public PadresHijos asignarNinoAPadre(Long padreId, Long ninoId) {
        Padre padre = findPadreById(padreId);
        Nino nino = findNinoById(ninoId);

        if (padre == null || nino == null) {
            throw new RuntimeException("Padre o Niño no encontrado.");
        }

        PadresHijos relacion = new PadresHijos(padre, nino);
        return padresHijosRepository.save(relacion);
    }

    // Eliminar una relación padre-hijo
    public void deleteRelacionByPadreAndNino(Long padreId, Long ninoId) {
        PadresHijos relacion = padresHijosRepository.findByPadreIdAndNinoId(padreId, ninoId);
        if (relacion != null) {
            padresHijosRepository.delete(relacion);
        } else {
            throw new RuntimeException("Relación no encontrada.");
        }
    }
}