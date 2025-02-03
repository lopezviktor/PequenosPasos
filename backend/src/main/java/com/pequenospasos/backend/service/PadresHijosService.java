package com.pequenospasos.backend.service;

import com.pequenospasos.backend.entity.PadresHijos;
import com.pequenospasos.backend.repository.PadresHijosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PadresHijosService {

    @Autowired
    private PadresHijosRepository padresHijosRepository;

    // Obtener todas las relaciones padres-hijos
    public List<PadresHijos> getAllRelaciones() {
        return padresHijosRepository.findAll();
    }

    // Obtener todos los hijos de un padre específico
    public List<PadresHijos> getHijosByPadreId(Long padreId) {
        return padresHijosRepository.findByPadreId(padreId);
    }

    // Obtener todos los padres de un niño específico
    public List<PadresHijos> getPadresByNinoId(Long ninoId) {
        return padresHijosRepository.findByNinoId(ninoId);
    }

    // Guardar una nueva relación padre-hijo
    public PadresHijos saveRelacion(PadresHijos padresHijos) {
        return padresHijosRepository.save(padresHijos);
    }

    // Eliminar una relación padre-hijo
    public void deleteRelacion(Long id) {
        padresHijosRepository.deleteById(id);
    }
}