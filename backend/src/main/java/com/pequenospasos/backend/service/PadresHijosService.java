package com.pequenospasos.backend.service;

import com.pequenospasos.backend.entity.Nino;
import com.pequenospasos.backend.entity.PadresHijos;
import com.pequenospasos.backend.entity.Padre;
import com.pequenospasos.backend.repository.NinoRepository;
import com.pequenospasos.backend.repository.PadresHijosRepository;
import com.pequenospasos.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PadresHijosService {

    @Autowired
    private PadresHijosRepository padresHijosRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private NinoRepository ninoRepository;

    // Obtener los niños asociados a un padre
    public List<Nino> getNinosByPadreId(Long padreId) {
        return padresHijosRepository.findNinosByPadreId(padreId);
    }

    public List<Padre> getPadresByNinoId(Long ninoId) {
        return padresHijosRepository.findByNinoId(ninoId)
                .stream()
                .map(PadresHijos::getPadre)
                .collect(Collectors.toList());
    }

    // Asociar un niño a un padre
    public PadresHijos asignarNinoAPadre(Padre padre, Nino nino) {
        PadresHijos relacion = new PadresHijos(padre, nino);
        return padresHijosRepository.save(relacion);
    }

    // Eliminar la relación entre un padre y un niño
    public void eliminarRelacionPadreNino(Long padreId, Long ninoId) {
        if (!padresHijosRepository.findByPadreIdAndNinoId(padreId, ninoId).isPresent()) {
            throw new RuntimeException("No existe una relación entre el padre con ID " + padreId + " y el niño con ID " + ninoId);
        }
        padresHijosRepository.deleteByPadreIdAndNinoId(padreId, ninoId);
    }

    // Obtener Padre por ID
    public Padre obtenerPadrePorId(Long padreId) {
        return (Padre) usuarioRepository.findById(padreId)
                .orElseThrow(() -> new RuntimeException("Padre no encontrado con ID: " + padreId));
    }

    // Obtener Niño por ID
    public Nino obtenerNinoPorId(Long ninoId) {
        return ninoRepository.findById(ninoId)
                .orElseThrow(() -> new RuntimeException("Niño no encontrado con ID: " + ninoId));
    }
}