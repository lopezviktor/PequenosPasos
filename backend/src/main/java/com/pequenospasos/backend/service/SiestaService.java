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
    public Siesta getUltimaSiestaByNinoId(Long ninoId) {
        return siestaRepository.findTopByNinoIdOrderByInicioSiestaDesc(ninoId);
    }

    // Buscar una siesta por ID
    public Optional<Siesta> getSiestaById(Long id) {
        return siestaRepository.findById(id);
    }

    // Guardar una nueva siesta (validando que solo un EDUCADOR puede hacerlo)
    public Siesta saveSiesta(Siesta siesta) {
        if (!siesta.getEducador().getTipoUsuario().equals("EDUCADOR")) {
            throw new RuntimeException("Solo un EDUCADOR puede registrar siestas.");
        }

        if (siesta.getFinSiesta() == null) {
            siesta.setFinSiesta(siesta.getInicioSiesta()); // Si no se proporciona, se pone igual a la hora de inicio
        }

        return siestaRepository.save(siesta);
    }

    // Actualizar una siesta (validando que solo un EDUCADOR puede hacerlo)
    public Siesta updateSiesta(Long id, Siesta siestaDetalles) {
        Optional<Siesta> siestaOptional = siestaRepository.findById(id);
        if (siestaOptional.isPresent()) {
            Siesta siesta = siestaOptional.get();

            if (!siestaDetalles.getEducador().getTipoUsuario().equals("EDUCADOR")) {
                throw new RuntimeException("Solo un EDUCADOR puede actualizar siestas.");
            }

            siesta.setInicioSiesta(siestaDetalles.getInicioSiesta());

            if (siestaDetalles.getFinSiesta().isBefore(siestaDetalles.getInicioSiesta())) {
                throw new IllegalArgumentException("La hora de finalización no puede ser antes de la hora de inicio.");
            }
            siesta.setFinSiesta(siestaDetalles.getFinSiesta());

            siesta.setObservaciones(siestaDetalles.getObservaciones());
            siesta.setEducador(siestaDetalles.getEducador());

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