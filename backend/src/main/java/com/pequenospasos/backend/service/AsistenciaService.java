package com.pequenospasos.backend.service;

import com.pequenospasos.backend.entity.Asistencia;
import com.pequenospasos.backend.entity.Nino;
import com.pequenospasos.backend.repository.AsistenciaRepository;
import com.pequenospasos.backend.repository.NinoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class AsistenciaService {

    @Autowired
    private AsistenciaRepository asistenciaRepository;
    @Autowired
    private NinoRepository ninoRepository;

    // Obtener todas las asistencias
    public List<Asistencia> getAllAsistencias() {
        return asistenciaRepository.findAll();
    }

    // Obtener asistencias de un niño específico
    public List<Asistencia> getAsistenciasByNinoId(Long ninoId) {
        Optional<Nino> nino = ninoRepository.findById(ninoId);
        return nino.map(asistenciaRepository::findByNino).orElse(Collections.emptyList());
    }

    // Obtener asistencias registradas por un educador (solo EDUCADORES)
    public List<Asistencia> getAsistenciasByEducadorId(Long educadorId) {
        return asistenciaRepository.findByEducadorRecibeId(educadorId).stream()
                .filter(a -> a.getEducadorRecibe().getTipoUsuario().equals("EDUCADOR"))
                .toList();
    }

    // Obtener asistencias donde un padre entregó al niño (solo PADRES)
    public List<Asistencia> getAsistenciasByPadreEntregaId(Long padreId) {
        return asistenciaRepository.findByPadreEntregaId(padreId).stream()
                .filter(a -> a.getPadreEntrega().getTipoUsuario().equals("PADRE"))
                .toList();
    }

    // Obtener asistencias donde un padre recogió al niño (solo PADRES)
    public List<Asistencia> getAsistenciasByPadreRecogeId(Long padreId) {
        return asistenciaRepository.findByPadreRecogeId(padreId).stream()
                .filter(a -> a.getPadreRecoge().getTipoUsuario().equals("PADRE"))
                .toList();
    }

    // Obtener asistencias en un rango de fechas
    public List<Asistencia> getAsistenciasByFecha(LocalDateTime inicio, LocalDateTime fin) {
        return asistenciaRepository.findByHoraEntradaBetween(inicio, fin);
    }

    // Buscar asistencia por ID
    public Optional<Asistencia> getAsistenciaById(Long id) {
        return asistenciaRepository.findById(id);
    }

    // Registrar una nueva asistencia (validando que solo un EDUCADOR puede hacerlo)
    public Asistencia saveAsistencia(Asistencia asistencia) {
        if (!asistencia.getEducadorRecibe().getTipoUsuario().equals("EDUCADOR")) {
            throw new RuntimeException("Solo un EDUCADOR puede registrar asistencias.");
        }
        return asistenciaRepository.save(asistencia);
    }

    // Actualizar una asistencia (validando que solo un EDUCADOR puede hacerlo)
    public Asistencia updateAsistencia(Long id, Asistencia asistenciaDetalles) {
        Optional<Asistencia> asistenciaOptional = asistenciaRepository.findById(id);
        if (asistenciaOptional.isPresent()) {
            Asistencia asistencia = asistenciaOptional.get();

            if (asistenciaDetalles.getEducadorEntrega() != null &&
                    !asistenciaDetalles.getEducadorEntrega().getTipoUsuario().equals("EDUCADOR")) {
                throw new RuntimeException("Solo un EDUCADOR puede actualizar asistencias.");
            }

            asistencia.setHoraSalida(asistenciaDetalles.getHoraSalida());
            asistencia.setEducadorEntrega(asistenciaDetalles.getEducadorEntrega());
            asistencia.setPadreRecoge(asistenciaDetalles.getPadreRecoge());

            return asistenciaRepository.save(asistencia);
        } else {
            throw new RuntimeException("Asistencia no encontrada con id: " + id);
        }
    }

    // Eliminar asistencia por ID
    public void deleteAsistencia(Long id) {
        asistenciaRepository.deleteById(id);
    }
}