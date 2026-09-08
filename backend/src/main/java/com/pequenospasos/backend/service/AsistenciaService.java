package com.pequenospasos.backend.service;

import com.pequenospasos.backend.entity.Asistencia;
import com.pequenospasos.backend.entity.Nino;
import com.pequenospasos.backend.enums.Role;
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
                .filter(a -> a.getEducadorRecibe().getTipoUsuario() == Role.EDUCADOR)
                .toList();
    }

    // Obtener asistencias donde un padre entregó al niño (solo PADRES)
    public List<Asistencia> getAsistenciasByPadreEntregaId(Long padreId) {
        return asistenciaRepository.findByPadreEntregaId(padreId).stream()
                .filter(a -> a.getPadreEntrega().getTipoUsuario() == Role.PADRE)
                .toList();
    }

    // Obtener asistencias donde un padre recogió al niño (solo PADRES)
    public List<Asistencia> getAsistenciasByPadreRecogeId(Long padreId) {
        return asistenciaRepository.findByPadreRecogeId(padreId).stream()
                .filter(a -> a.getPadreRecoge().getTipoUsuario() == Role.PADRE)
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
        // Obtener asistencias activas solo del día actual
        LocalDateTime inicioDelDia = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime finDelDia = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);

        List<Asistencia> asistenciasHoy = asistenciaRepository.findByNino(asistencia.getNino())
                .stream()
                .filter(a -> a.getHoraEntrada().isAfter(inicioDelDia) && a.getHoraEntrada().isBefore(finDelDia))
                .toList();

        // Verificar si ya existe una asistencia sin hora de salida en el día actual
        boolean tieneAsistenciaActiva = asistenciasHoy.stream().anyMatch(a -> a.getHoraSalida() == null);

        if (tieneAsistenciaActiva) {
            throw new RuntimeException("Este niño ya tiene una asistencia activa sin salida registrada hoy.");
        }

        // Validar que solo un EDUCADOR pueda registrar la asistencia
        if (!(asistencia.getEducadorRecibe().getTipoUsuario() == Role.EDUCADOR)) {
            throw new RuntimeException("Solo un EDUCADOR puede registrar asistencias.");
        }

        // Validar que la hora de salida no sea anterior a la hora de entrada
        if (asistencia.getHoraSalida() != null &&
            asistencia.getHoraSalida().isBefore(asistencia.getHoraEntrada())) {
            throw new RuntimeException("La hora de salida no puede ser anterior a la hora de entrada.");
        }

        // Validar que la hora de salida no sea posterior a la hora actual
        if (asistencia.getHoraSalida() != null &&
            asistencia.getHoraSalida().isAfter(LocalDateTime.now())) {
            throw new RuntimeException("La hora de salida no puede ser posterior a la hora actual.");
        }

        return asistenciaRepository.save(asistencia);
    }

    // Actualizar una asistencia (validando que solo un EDUCADOR puede hacerlo)
    public Asistencia updateAsistencia(Long id, Asistencia asistenciaDetalles) {
        Asistencia asistencia = asistenciaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asistencia no encontrada con id: " + id));

        // No permitir modificar la hora de salida si ya ha sido registrada
        if (asistencia.getHoraSalida() != null) {
            throw new RuntimeException("No se puede modificar la hora de salida, ya ha sido registrada.");
        }

        if (asistenciaDetalles.getEducadorEntrega() != null &&
                !(asistenciaDetalles.getEducadorEntrega().getTipoUsuario() == Role.EDUCADOR)) {
            throw new RuntimeException("Solo un EDUCADOR puede registrar la salida del niño.");
        }

        asistencia.setHoraSalida(asistenciaDetalles.getHoraSalida());
        asistencia.setEducadorEntrega(asistenciaDetalles.getEducadorEntrega());
        asistencia.setPadreRecoge(asistenciaDetalles.getPadreRecoge());

        return asistenciaRepository.save(asistencia);
    }

    // Eliminar asistencia por ID
    public void deleteAsistencia(Long id) {
        asistenciaRepository.deleteById(id);
    }
}