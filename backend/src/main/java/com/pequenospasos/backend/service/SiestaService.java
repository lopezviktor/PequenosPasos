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

    // Obtener siestas registradas por un educador específico
    public List<Siesta> getSiestasByEducadorId(Long educadorId) {
        return siestaRepository.findByEducadorId(educadorId);
    }

    // Obtener siestas en un rango de fechas
    public List<Siesta> getSiestasByFecha(LocalDateTime inicio, LocalDateTime fin) {
        return siestaRepository.findByHoraInicioBetween(inicio, fin);
    }

    // Buscar una siesta por ID
    public Optional<Siesta> getSiestaById(Long id) {
        return siestaRepository.findById(id);
    }

    // Guardar una nueva siesta
    public Siesta saveSiesta(Siesta siesta) {
        if (siesta.getHoraFin() == null) {
            siesta.setHoraFin(siesta.getHoraInicio()); // Si no se proporciona, se pone igual a la hora de inicio
        }
        return siestaRepository.save(siesta);
    }

    // Actualizar una siesta
    public Siesta updateSiesta(Long id, Siesta siestaDetalles) {
        Optional<Siesta> siestaOptional = siestaRepository.findById(id);
        if (siestaOptional.isPresent()) {
            Siesta siesta = siestaOptional.get();
            siesta.setHoraInicio(siestaDetalles.getHoraInicio());

            if (siestaDetalles.getHoraFin().isBefore(siestaDetalles.getHoraInicio())) {
                throw new IllegalArgumentException("La hora de finalización no puede ser antes de la hora de inicio.");
            }
            siesta.setHoraFin(siestaDetalles.getHoraFin());

            siesta.setObservaciones(siestaDetalles.getObservaciones());
            siesta.setEducador(siestaDetalles.getEducador());

            // Imprimir duración en consola (para pruebas)
            System.out.println("Duración de la siesta: " + siesta.getDuracion() + " minutos");

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