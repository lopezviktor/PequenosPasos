package com.pequenospasos.backend.service;

import com.pequenospasos.backend.entity.Siesta;
import com.pequenospasos.backend.repository.SiestaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SiestaService {

    @Autowired
    private SiestaRepository siestaRepository;

    // Obtener todas las siestas registradas
    public List<Siesta> getAllSiestas() {
        return siestaRepository.findAll();
    }

    // Obtener siestas de un niño específico
    public List<Siesta> getSiestasByNinoId(Long ninoId) {
        return siestaRepository.findByNinoId(ninoId);
    }

    // Obtener siestas registradas por un educador específico (solo EDUCADORES)
    public List<Siesta> getSiestasByEducadorId(Long educadorId) {
        return siestaRepository.findByEducadorId(educadorId).stream()
                .filter(s -> s.getEducador().getTipoUsuario().equals("EDUCADOR"))
                .toList();
    }

    // Obtener siestas en un rango de fechas
    public List<Siesta> getSiestasByFecha(LocalDateTime inicio, LocalDateTime fin) {
        return siestaRepository.findByInicioSiestaBetween(inicio, fin);
    }

    // Obtener la última siesta de un niño
    public Optional<Siesta> getUltimaSiestaByNinoId(Long ninoId) {
        return siestaRepository.findTopByNinoIdOrderByInicioSiestaDesc(ninoId);
    }

    // Buscar una siesta por ID
    public Optional<Siesta> getSiestaById(Long id) {
        return siestaRepository.findById(id);
    }

    // Guardar una nueva siesta (validando que solo un EDUCADOR puede hacerlo)
    public Siesta saveSiesta(Siesta siesta) {
        // Validar si ya hay una siesta en curso sin hora de fin
        if (siestaRepository.existsByNinoIdAndFinSiestaIsNull(siesta.getNino().getId())) {
            throw new RuntimeException("El niño debe registrar la hora de salida de la siesta anterior antes de registrar una nueva.");
        }

        // Obtener la última siesta registrada para el niño
        Optional<Siesta> ultimaSiestaOptional = siestaRepository.findTopByNinoIdOrderByInicioSiestaDesc(siesta.getNino().getId());
        if (ultimaSiestaOptional.isPresent()) {
            Siesta ultimaSiesta = ultimaSiestaOptional.get();

            // Validar que la nueva siesta no inicie antes de que finalice la última siesta registrada
            if (ultimaSiesta.getFinSiesta() != null && siesta.getInicioSiesta().isBefore(ultimaSiesta.getFinSiesta())) {
                throw new RuntimeException("No se puede registrar una nueva siesta antes de que finalice la anterior.");
            }
        }

        // Si la siesta no tiene fin, se deja en null
        if (siesta.getFinSiesta() == null) {
            siesta.setFinSiesta(null);
        } else {
            // Validar que la hora de fin no sea antes de la hora de inicio
            if (siesta.getFinSiesta().isBefore(siesta.getInicioSiesta())) {
                throw new RuntimeException("La hora de fin de la siesta no puede ser anterior a la hora de inicio.");
            }
        }

        return siestaRepository.save(siesta);
    }

    // Actualizar una siesta (validando que solo un EDUCADOR puede hacerlo)
    public Siesta updateSiesta(Long id, Siesta siestaDetalles) {
        Optional<Siesta> siestaOptional = siestaRepository.findById(id);

        if (siestaOptional.isPresent()) {
            Siesta siesta = siestaOptional.get();

            if (siesta.getEducador() == null || siestaDetalles.getEducador() == null) {
                throw new RuntimeException("No se puede modificar la siesta sin un educador asignado.");
            }

            if (!siesta.getEducador().getId().equals(siestaDetalles.getEducador().getId())) {
                throw new RuntimeException("Solo el educador que creó la siesta puede modificarla.");
            }

            // Solo actualizar la hora de fin si se proporciona en la solicitud
            if (siestaDetalles.getFinSiesta() != null) {
                if (siesta.getInicioSiesta() != null && siestaDetalles.getFinSiesta().isBefore(siesta.getInicioSiesta())) {
                    throw new RuntimeException("La hora de finalización no puede ser antes de la hora de inicio.");
                }
                siesta.setFinSiesta(siestaDetalles.getFinSiesta());
            }

            // No modificar la hora de inicio si no se envía en la solicitud
            if (siestaDetalles.getInicioSiesta() != null) {
                siesta.setInicioSiesta(siestaDetalles.getInicioSiesta());
            }

            // Actualizar observaciones solo si se proporciona un nuevo valor
            if (siestaDetalles.getObservaciones() != null) {
                siesta.setObservaciones(siestaDetalles.getObservaciones());
            }

            return siestaRepository.save(siesta);
        } else {
            throw new RuntimeException("Siesta no encontrada con id: " + id);
        }
    }


    // Eliminar una siesta por ID
    public void deleteSiesta(Long id) {
        siestaRepository.deleteById(id);
    }
}