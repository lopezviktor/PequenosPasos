package com.pequenospasos.backend.service;

import com.pequenospasos.backend.entity.Actividad;
import com.pequenospasos.backend.repository.ActividadNinosRepository;
import com.pequenospasos.backend.repository.ActividadRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ActividadService {

    @Autowired
    private ActividadRepository actividadRepository;

    @Autowired
    private ActividadNinosRepository actividadNinosRepository;

    // Obtener todas las actividades
    public List<Actividad> getAllActividades() {
        return actividadRepository.findAll();
    }

    // Buscar actividades por nombre (ignorando mayúsculas y minúsculas)
    public List<Actividad> getActividadesByNombre(String nombre) {
        return actividadRepository.findByNombreContainingIgnoreCase(nombre);
    }

    // Buscar actividad por ID
    public Optional<Actividad> getActividadById(Long actividadId) {
        return actividadRepository.findById(actividadId);
    }

    // Guardar una nueva actividad (validando nombre único)
    public Actividad saveActividad(Actividad actividad) {
        if (actividadRepository.findByNombreContainingIgnoreCase(actividad.getNombre()).size() > 0) {
            throw new RuntimeException("Ya existe una actividad con este nombre.");
        }
        return actividadRepository.save(actividad);
    }

    // Actualizar actividad existente (validando nombre único)
    public Actividad updateActividad(Long actividadId, Actividad actividadDetalles) {
        Optional<Actividad> actividadOptional = actividadRepository.findById(actividadId);
        if (actividadOptional.isPresent()) {
            Actividad actividad = actividadOptional.get();

            if (!actividad.getNombre().equalsIgnoreCase(actividadDetalles.getNombre()) &&
                    actividadRepository.findByNombreContainingIgnoreCase(actividadDetalles.getNombre()).size() > 0) {
                throw new RuntimeException("Ya existe otra actividad con este nombre.");
            }

            actividad.setNombre(actividadDetalles.getNombre());
            actividad.setDescripcion(actividadDetalles.getDescripcion());
            return actividadRepository.save(actividad);
        } else {
            throw new RuntimeException("Actividad no encontrada con id: " + actividadId);
        }
    }

    // Eliminar actividad por ID
    @Transactional
    public void deleteActividad(Long id) {
        // Eliminar todas las relaciones en la tabla actividad_ninos antes de eliminar la actividad
        actividadRepository.deleteAllByActividadId(id);

        // Eliminar la actividad después de eliminar las relaciones
        actividadRepository.deleteById(id);
    }

    @Transactional
    public void deleteAllNinosFromActividad(Long actividadId) {
        actividadRepository.deleteAllByActividadId(actividadId);
    }
}