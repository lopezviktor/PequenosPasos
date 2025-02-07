package com.pequenospasos.backend.service;

import com.pequenospasos.backend.entity.Actividad;
import com.pequenospasos.backend.entity.ActividadNinos;
import com.pequenospasos.backend.entity.Nino;
import com.pequenospasos.backend.repository.ActividadNinosRepository;
import com.pequenospasos.backend.repository.ActividadRepository;
import com.pequenospasos.backend.repository.NinoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ActividadNinosService {

    @Autowired
    private ActividadNinosRepository actividadNinosRepository;

    @Autowired
    private NinoRepository ninoRepository;

    @Autowired
    private ActividadRepository actividadRepository;

    // Obtener todas las relaciones actividad-niño
    public List<ActividadNinos> getAllActividadNinos() {
        return actividadNinosRepository.findAll();
    }

    // Obtener todas las actividades en las que participa un niño específico
    public List<ActividadNinos> getActividadesByNinoId(Long ninoId) {
        return actividadNinosRepository.findByNinoId(ninoId);
    }

    // Obtener todos los niños que participan en una actividad específica
    public List<ActividadNinos> getNinosByActividadId(Long actividadId) {
        return actividadNinosRepository.findByActividadId(actividadId);
    }

    // Guardar una nueva relación entre actividad y niño evitando duplicados
    public ActividadNinos saveActividadNino(ActividadNinos actividadNinos) {
        if (actividadNinos.getActividad() == null || actividadNinos.getNino() == null) {
            throw new RuntimeException("La actividad o el niño no pueden ser nulos.");
        }

        Optional<ActividadNinos> existente = actividadNinosRepository.findByActividadIdAndNinoId(
                actividadNinos.getActividad().getActividadId(),
                actividadNinos.getNino().getId()
        );

        if (existente.isPresent()) {
            throw new RuntimeException("El niño ya está registrado en esta actividad.");
        }

        return actividadNinosRepository.save(actividadNinos);
    }

    // Eliminar una relación actividad-niño por ID
    public void deleteActividadNino(Long id) {
        actividadNinosRepository.deleteById(id);
    }

    // Eliminar una relación actividad-niño por IDs de actividad y niño
    public void deleteActividadNinoByActividadAndNino(Long actividadId, Long ninoId) {
        actividadNinosRepository.deleteByActividadIdAndNinoId(actividadId, ninoId);
    }
}