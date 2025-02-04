package com.pequenospasos.backend.service;

import com.pequenospasos.backend.entity.Higiene;
import com.pequenospasos.backend.repository.HigieneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class HigieneService {

    @Autowired
    private HigieneRepository higieneRepository;

    // Obtener todos los registros de higiene
    public List<Higiene> getAllHigiene() {
        return higieneRepository.findAll();
    }

    // Obtener registros de higiene de un niño específico
    public List<Higiene> getHigieneByNinoId(Long ninoId) {
        return higieneRepository.findByNinoId(ninoId);
    }

    // Obtener registros de higiene realizados por un educador específico
    public List<Higiene> getHigieneByEducadorId(Long educadorId) {
        return higieneRepository.findByEducadorId(educadorId);
    }

    // Obtener registros de higiene en un rango de fechas
    public List<Higiene> getHigieneByFecha(LocalDateTime inicio, LocalDateTime fin) {
        return higieneRepository.findByFechaHoraBetween(inicio, fin);
    }

    // Buscar un registro de higiene por ID
    public Optional<Higiene> getHigieneById(Long id) {
        return higieneRepository.findById(id);
    }

    // Guardar un nuevo registro de higiene
    public Higiene saveHigiene(Higiene higiene) {
        if (higiene.getFechaHora() == null) {
            higiene.setFechaHora(LocalDateTime.now());
        }
        return higieneRepository.save(higiene);
    }

    // Actualizar un registro de higiene
    public Higiene updateHigiene(Long id, Higiene higieneDetalles) {
        return higieneRepository.findById(id).map(higiene -> {
            higiene.setFechaHora(higieneDetalles.getFechaHora());
            higiene.setEstado(higieneDetalles.getEstado());
            higiene.setObservaciones(higieneDetalles.getObservaciones());
            higiene.setEducador(higieneDetalles.getEducador());
            return higieneRepository.save(higiene);
        }).orElseThrow(() -> new RuntimeException("Registro de higiene no encontrado con id: " + id));
    }

    // Eliminar un registro de higiene por ID
    public void deleteHigiene(Long id) {
        higieneRepository.deleteById(id);
    }
}