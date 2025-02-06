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
        Optional<Nino> nino = ninoRepository.findById(ninoId);
        return nino.map(actividadNinosRepository::findByNino).orElse(Collections.emptyList());
    }

    // Obtener todos los niños que participan en una actividad específica
    public List<ActividadNinos> getNinosByActividadId(Long actividadId) {
        Optional<Actividad> actividad = actividadRepository.findById(actividadId);
        return actividad.map(actividadNinosRepository::findByActividad).orElse(Collections.emptyList());
    }

    // Guardar una nueva relación entre actividad y niño
    public ActividadNinos saveActividadNino(ActividadNinos actividadNinos) {
        return actividadNinosRepository.save(actividadNinos);
    }

    // Eliminar una relación actividad-niño
    public void deleteActividadNino(Long id) {
        actividadNinosRepository.deleteById(id);
    }
}