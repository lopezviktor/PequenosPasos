package com.pequenospasos.backend.service;

import com.pequenospasos.backend.entity.*;
import com.pequenospasos.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    @Autowired
    private ClaseRepository claseRepository;

    @Autowired
    private PadresHijosRepository padresHijosRepository;

    @Autowired
    private NotificacionService notificacionService;

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
        return actividadNinosRepository.findByActividad_ActividadId(actividadId);
    }

    // Guardar una nueva relación entre actividad y niño evitando duplicados
    public ActividadNinos saveActividadNino(ActividadNinos actividadNinos) {
        if (actividadNinos.getActividad() == null || actividadNinos.getNino() == null) {
            throw new RuntimeException("La actividad o el niño no pueden ser nulos.");
        }

        Optional<ActividadNinos> existente = actividadNinosRepository.findByActividad_ActividadIdAndNinoId(
                actividadNinos.getActividad().getActividadId(),
                actividadNinos.getNino().getId()
        );

        if (existente.isPresent()) {
            throw new RuntimeException("El niño ya está registrado en esta actividad.");
        }

        // Log de la actividad antes de guardar
        System.out.println("Guardando actividad-niño: " + actividadNinos.toString());

        return actividadNinosRepository.save(actividadNinos);
    }

    public void registrarActividadPorClase(Long claseId, Actividad actividad) {
        Clase clase = claseRepository.findById(claseId)
                .orElseThrow(() -> new RuntimeException("Clase no encontrada con ID: " + claseId));

        if (actividad == null) {
            throw new RuntimeException("La actividad proporcionada es nula.");
        }

        Actividad actividadGuardada;

        if (actividad.getActividadId() != null) {
            actividadGuardada = actividadRepository.findById(actividad.getActividadId())
                    .orElseThrow(() -> new RuntimeException("Actividad no encontrada con ID: " + actividad.getActividadId()));
        } else {
            actividadGuardada = actividadRepository.save(actividad);
        }

        for (Nino nino : clase.getNinos()) {
            Optional<ActividadNinos> existente = actividadNinosRepository
                    .findByActividad_ActividadIdAndNinoId(actividadGuardada.getActividadId(), nino.getId());

            if (existente.isEmpty()) {
                ActividadNinos actividadNino = new ActividadNinos(nino, actividadGuardada, LocalDateTime.now());
                actividadNinosRepository.save(actividadNino);

                List<PadresHijos> asociaciones = padresHijosRepository.findByNino(nino);
                for (PadresHijos ph : asociaciones) {
                    Usuario padre = ph.getPadre();
                    if (padre != null) {
                        String mensaje = "Tu hijo/a " + nino.getNombre() +
                                " ha participado en la actividad: " + actividadGuardada.getNombre();
                        notificacionService.crearNotificacion(clase.getEducador(), padre, mensaje);
                    }
                }
            }
        }
    }

    // Eliminar una relación actividad-niño por ID
    public void deleteActividadNino(Long id) {
        actividadNinosRepository.deleteById(id);
    }

    // Eliminar una relación actividad-niño por IDs de actividad y niño
    public void deleteActividadNinoByActividadAndNino(Long actividadId, Long ninoId) {
        actividadNinosRepository.deleteByActividad_ActividadIdAndNinoId(actividadId, ninoId);
    }
}