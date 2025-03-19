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

    // Obtener comidas registradas por un educador (solo EDUCADORES)
    public List<Comida> getComidasByEducadorId(Long educadorId) {
        return comidaRepository.findByEducadorId(educadorId).stream()
                .filter(c -> c.getEducador().getTipoUsuario().equals("EDUCADOR"))
                .toList();
    }

    // Obtener comidas en un rango de fechas
    public List<Comida> getComidasByFecha(LocalDateTime inicio, LocalDateTime fin) {
        return comidaRepository.findByHoraComidaBetween(inicio, fin);
    }

    // Buscar comida por ID
    public Optional<Comida> getComidaById(Long id) {
        return comidaRepository.findById(id);
    }

    // Registrar una nueva comida (validando que solo un EDUCADOR puede hacerlo)
    public Comida saveComida(Comida comida) {
        if (comida == null) {
            throw new IllegalArgumentException("El objeto comida no puede ser nulo.");
        }

        if (comida.getEducador() == null) {
            throw new IllegalArgumentException("Debe asignar un educador para registrar la comida.");
        }

        if (!"EDUCADOR".equals(comida.getEducador().getTipoUsuario())) {
            throw new IllegalArgumentException("Solo un EDUCADOR puede registrar comidas.");
        }

        if (comida.getHoraComida() == null) {
            comida.setHoraComida(LocalDateTime.now());
        }

        // Verificar si ya existe una comida para el niño en el mismo día y con una diferencia menor a 1 hora
        LocalDateTime inicioDia = comida.getHoraComida().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime finDia = comida.getHoraComida().withHour(23).withMinute(59).withSecond(59);
        List<Comida> comidasExistentes = comidaRepository.findByNinoIdAndHoraComidaBetween(comida.getNino().getId(), inicioDia, finDia);

        for (Comida comidaExistente : comidasExistentes) {
            if (Math.abs(comidaExistente.getHoraComida().getHour() - comida.getHoraComida().getHour()) < 1) {
                throw new IllegalArgumentException("El niño ya tiene una comida registrada en esta franja horaria.");
            }
        }

        return comidaRepository.save(comida);
    }

    // Actualizar una comida (validando que solo un EDUCADOR puede hacerlo)
    public Comida updateComida(Long id, Comida comidaDetalles) {
        Optional<Comida> comidaOptional = comidaRepository.findById(id);
        if (comidaOptional.isPresent()) {
            Comida comida = comidaOptional.get();

            if (!comidaDetalles.getEducador().getTipoUsuario().equals("EDUCADOR")) {
                throw new RuntimeException("Solo un EDUCADOR puede actualizar comidas.");
            }

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