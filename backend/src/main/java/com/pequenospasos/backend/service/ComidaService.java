package com.pequenospasos.backend.service;

import com.pequenospasos.backend.entity.Comida;
import com.pequenospasos.backend.repository.ComidaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ComidaService {

    @Autowired
    private ComidaRepository comidaRepository;

    // Obtener todas las comidas registradas
    public List<Comida> getAllComidas() {
        return comidaRepository.findAll();
    }

    // Obtener comidas de un niño específico
    public List<Comida> getComidasByNinoId(Long ninoId) {
        return comidaRepository.findByNinoId(ninoId);
    }

    // Obtener comidas registradas por un educador
    public List<Comida> getComidasByEducadorId(Long educadorId) {
        return comidaRepository.findByEducadorId(educadorId);
    }

    // Obtener comidas en un rango de fechas
    public List<Comida> getComidasByFecha(LocalDateTime inicio, LocalDateTime fin) {
        return comidaRepository.findByHoraComidaBetween(inicio, fin);
    }

    // Buscar comida por ID
    public Optional<Comida> getComidaById(Long id) {
        return comidaRepository.findById(id);
    }

    // Registrar una nueva comida
    public Comida saveComida(Comida comida) {
        if (comida.getHoraComida() == null) {
            comida.setHoraComida(LocalDateTime.now()); // Asigna la hora actual si no se proporciona
        }
        return comidaRepository.save(comida);
    }

    // Actualizar una comida
    public Comida updateComida(Long id, Comida comidaDetalles) {
        Optional<Comida> comidaOptional = comidaRepository.findById(id);
        if (comidaOptional.isPresent()) {
            Comida comida = comidaOptional.get();
            comida.setHoraComida(comidaDetalles.getHoraComida());
            comida.setDescripcionComida(comidaDetalles.getDescripcionComida());
            comida.setEducador(comidaDetalles.getEducador());
            return comidaRepository.save(comida);
        } else {
            throw new RuntimeException("Comida no encontrada con id: " + id);
        }
    }

    // Eliminar comida por ID
    public void deleteComida(Long id) {
        comidaRepository.deleteById(id);
    }
}