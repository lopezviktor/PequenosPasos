package com.pequenospasos.backend.service;

import com.pequenospasos.backend.entity.Asistencia;
import com.pequenospasos.backend.repository.AsistenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AsistenciaService {

    @Autowired
    private AsistenciaRepository asistenciaRepository;

    // Obtener todas las asistencias
    public List<Asistencia> getAllAsistencias() {
        return asistenciaRepository.findAll();
    }

    // Obtener asistencias de un niño específico
    public List<Asistencia> getAsistenciasByNinoId(Long ninoId) {
        return asistenciaRepository.findByNinoId(ninoId);
    }

    // Obtener asistencias registradas por un educador
    public List<Asistencia> getAsistenciasByEducadorId(Long educadorId) {
        return asistenciaRepository.findByEducadorRecibeId(educadorId);
    }

    // Obtener asistencias en un rango de fechas
    public List<Asistencia> getAsistenciasByFecha(LocalDateTime inicio, LocalDateTime fin) {
        return asistenciaRepository.findByHoraEntradaBetween(inicio, fin);
    }

    // Buscar asistencia por ID
    public Optional<Asistencia> getAsistenciaById(Long id) {
        return asistenciaRepository.findById(id);
    }

    // Registrar una nueva asistencia
    public Asistencia saveAsistencia(Asistencia asistencia) {
        return asistenciaRepository.save(asistencia);
    }

    // Actualizar una asistencia
    public Asistencia updateAsistencia(Long id, Asistencia asistenciaDetalles) {
        Optional<Asistencia> asistenciaOptional = asistenciaRepository.findById(id);
        if (asistenciaOptional.isPresent()) {
            Asistencia asistencia = asistenciaOptional.get();
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