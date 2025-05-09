package com.pequenospasos.backend.service;

import com.pequenospasos.backend.dto.HigieneResponse;
import com.pequenospasos.backend.entity.Educador;
import com.pequenospasos.backend.entity.Higiene;
import com.pequenospasos.backend.entity.Nino;
import com.pequenospasos.backend.entity.Padre;
import com.pequenospasos.backend.entity.PadresHijos;
import com.pequenospasos.backend.repository.EducadorRepository;
import com.pequenospasos.backend.repository.HigieneRepository;
import com.pequenospasos.backend.repository.NinoRepository;
import com.pequenospasos.backend.repository.PadresHijosRepository;
import com.pequenospasos.backend.service.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HigieneService {

    @Autowired
    private HigieneRepository higieneRepository;

    @Autowired
    private NinoRepository ninoRepository;

    @Autowired
    private EducadorRepository educadorRepository;

    @Autowired
    private PadresHijosRepository padresHijosRepository;

    @Autowired
    private NotificacionService notificacionService;

    // Obtener todos los registros de higiene
    public List<Higiene> getAllHigiene() {
        return higieneRepository.findAll();
    }

    // Obtener registros de higiene de un niño específico
    public List<HigieneResponse> getHigieneByNinoId(Long ninoId) {
        boolean existeNino = ninoRepository.existsById(ninoId);
        if (!existeNino) {
            throw new RuntimeException("Niño no encontrado con id: " + ninoId);
        }

        List<Higiene> higieneList = higieneRepository.findByNinoId(ninoId);

        return higieneList.stream().map(higiene -> new HigieneResponse(
                higiene.getId(),
                higiene.getFechaHora().toString(),
                higiene.getEstado().toString(),
                higiene.getObservaciones(),
                higiene.getEducador().getNombre() + " " + higiene.getEducador().getApellidos(),
                higiene.getNino().getClase().getNombre()
        )).collect(Collectors.toList());
    }

    // Obtener registros de higiene realizados por un educador específico (solo EDUCADORES)
    public List<Higiene> getHigieneByEducadorId(Long educadorId) {
        return higieneRepository.findByEducadorId(educadorId).stream()
                .filter(h -> h.getEducador() != null && "EDUCADOR".equals(h.getEducador().getTipoUsuario()))
                .toList();
    }

    // Obtener registros de higiene en un rango de fechas
    public List<Higiene> getHigieneByFecha(LocalDateTime inicio, LocalDateTime fin) {
        return higieneRepository.findByFechaHoraBetween(inicio, fin);
    }

    // Obtener el último registro de higiene de un niño
    public Higiene getUltimaHigieneByNinoId(Long ninoId) {
        return higieneRepository.findTopByNinoIdOrderByFechaHoraDesc(ninoId);
    }

    // Buscar un registro de higiene por ID
    public Optional<Higiene> getHigieneById(Long id) {
        return higieneRepository.findById(id);
    }

    // Guardar un nuevo registro de higiene (validando que solo un EDUCADOR puede hacerlo)
    public Higiene saveHigiene(Higiene higiene) {
        Educador educador = educadorRepository.findById(higiene.getEducador().getId())
                .orElseThrow(() -> new RuntimeException("Educador no encontrado con id: " + higiene.getEducador().getId()));
        if (!"EDUCADOR".equals(educador.getTipoUsuario())) {
            throw new RuntimeException("Solo un EDUCADOR puede registrar registros de higiene.");
        }

        Nino nino = ninoRepository.findById(higiene.getNino().getId())
                .orElseThrow(() -> new RuntimeException("Niño no encontrado con id: " + higiene.getNino().getId()));

        higiene.setNino(nino);
        higiene.setEducador(educador);

        if (higiene.getFechaHora() == null) {
            higiene.setFechaHora(LocalDateTime.now());
        }

        Higiene savedHigiene = higieneRepository.save(higiene);

        // Obtener los padres del niño
        List<PadresHijos> relaciones = padresHijosRepository.findByNinoId(nino.getId());

        // Enviar notificación a cada padre
        for (PadresHijos relacion : relaciones) {
            Padre padre = relacion.getPadre();
            String mensajeNotificacion = "Tu hijo/a ha realizado una higiene: " + higiene.getEstado();

            if (higiene.getObservaciones() != null && !higiene.getObservaciones().isEmpty()) {
                mensajeNotificacion += " Observaciones: " + higiene.getObservaciones();
            }

            notificacionService.crearNotificacion(educador, padre, mensajeNotificacion);
        }

        return savedHigiene;
    }

    // Actualizar un registro de higiene (validando que solo un EDUCADOR puede hacerlo)
    public Higiene updateHigiene(Long id, Higiene higiene) {
        return higieneRepository.findById(id).map(existingHigiene -> {
            Educador educador = educadorRepository.findById(higiene.getEducador().getId())
                    .orElseThrow(() -> new RuntimeException("Educador no encontrado con id: " + higiene.getEducador().getId()));
            if (!"EDUCADOR".equals(educador.getTipoUsuario())) {
                throw new RuntimeException("Solo un EDUCADOR puede actualizar registros de higiene.");
            }

            Nino nino = ninoRepository.findById(higiene.getNino().getId())
                    .orElseThrow(() -> new RuntimeException("Niño no encontrado con id: " + higiene.getNino().getId()));

            existingHigiene.setFechaHora(higiene.getFechaHora());
            existingHigiene.setEstado(higiene.getEstado());
            existingHigiene.setObservaciones(higiene.getObservaciones());
            existingHigiene.setEducador(educador);
            existingHigiene.setNino(nino);
            return higieneRepository.save(existingHigiene);
        }).orElseThrow(() -> new RuntimeException("Registro de higiene no encontrado con id: " + id));
    }

    // Eliminar un registro de higiene por ID
    public void deleteHigiene(Long id) {
        higieneRepository.deleteById(id);
    }
}