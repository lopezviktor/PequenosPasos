package com.pequenospasos.backend.service;

import com.pequenospasos.backend.entity.ActividadNinos;
import com.pequenospasos.backend.repository.ActividadNinosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ActividadNinosService {

    @Autowired
    private ActividadNinosRepository actividadNinosRepository;

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

    // Guardar una nueva relación entre actividad y niño
    public ActividadNinos saveActividadNino(ActividadNinos actividadNinos) {
        return actividadNinosRepository.save(actividadNinos);
    }

    // Eliminar una relación actividad-niño
    public void deleteActividadNino(Long id) {
        actividadNinosRepository.deleteById(id);
    }
}