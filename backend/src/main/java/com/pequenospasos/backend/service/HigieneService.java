package com.pequenospasos.backend.service;

import com.pequenospasos.backend.entity.Educador;
import com.pequenospasos.backend.entity.Higiene;
import com.pequenospasos.backend.entity.Nino;
import com.pequenospasos.backend.repository.EducadorRepository;
import com.pequenospasos.backend.repository.HigieneRepository;
import com.pequenospasos.backend.repository.NinoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class HigieneService {

    @Autowired
    private HigieneRepository higieneRepository;

    @Autowired
    private NinoRepository ninoRepository;

    @Autowired
    private EducadorRepository educadorRepository;

    // Obtener todos los registros de higiene
    public List<Higiene> getAllHigiene() {
        return higieneRepository.findAll();
    }

    // Obtener registros de higiene de un niño específico
    public List<Higiene> getHigieneByNinoId(Long ninoId) {
        boolean existeNino = ninoRepository.existsById(ninoId);
        if (!existeNino) {
            throw new RuntimeException("Niño no encontrado con id: " + ninoId);
        }
        return higieneRepository.findByNinoId(ninoId);
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

        return higieneRepository.save(higiene);
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