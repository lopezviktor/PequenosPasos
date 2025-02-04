package com.pequenospasos.backend.service;

import com.pequenospasos.backend.entity.Actividad;
import com.pequenospasos.backend.repository.ActividadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ActividadService {

    @Autowired
    private ActividadRepository actividadRepository;

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

    // Guardar una nueva actividad
    public Actividad saveActividad(Actividad actividad) {
        return actividadRepository.save(actividad);
    }

    // Actualizar actividad existente
    public Actividad updateActividad(Long actividadId, Actividad actividadDetalles) {
        Optional<Actividad> actividadOptional = actividadRepository.findById(actividadId);
        if (actividadOptional.isPresent()) {
            Actividad actividad = actividadOptional.get();
            actividad.setNombre(actividadDetalles.getNombre());
            actividad.setDescripcion(actividadDetalles.getDescripcion());
            return actividadRepository.save(actividad);
        } else {
            throw new RuntimeException("Actividad no encontrada con id: " + actividadId);
        }
    }

    // Eliminar actividad por ID
    public void deleteActividad(Long actividadId) {
        actividadRepository.deleteById(actividadId);
    }
}