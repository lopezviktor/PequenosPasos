package com.pequenospasos.backend.service;

import com.pequenospasos.backend.entity.Evento;
import com.pequenospasos.backend.repository.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EventoService {

    @Autowired
    private EventoRepository eventoRepository;

    // Obtener todos los eventos
    public List<Evento> getAllEventos() {
        return eventoRepository.findAll();
    }

    // Obtener un evento por ID
    public Optional<Evento> getEventoById(Long id) {
        return eventoRepository.findById(id);
    }

    // Buscar eventos por título (ignorando mayúsculas y minúsculas)
    public List<Evento> getEventosByTitulo(String titulo) {
        return eventoRepository.findByTituloContainingIgnoreCase(titulo);
    }

    // Buscar eventos en un rango de fechas
    public List<Evento> getEventosByFecha(LocalDateTime inicio, LocalDateTime fin) {
        return eventoRepository.findByFechaHoraBetween(inicio, fin);
    }

    // Buscar eventos creados por un usuario específico (educador o administrador)
    public List<Evento> getEventosByCreador(Long creadorId) {
        return eventoRepository.findByCreadorId(creadorId);
    }

    // Crear un nuevo evento
    public Evento saveEvento(Evento evento) {
        if (evento.getFechaHora() == null) {
            evento.setFechaHora(LocalDateTime.now()); // Si no hay fecha, se asigna la actual
        }
        return eventoRepository.save(evento);
    }

    // Actualizar un evento existente
    public Evento updateEvento(Long id, Evento eventoDetalles) {
        Optional<Evento> eventoOptional = eventoRepository.findById(id);
        if (eventoOptional.isPresent()) {
            Evento evento = eventoOptional.get();
            evento.setTitulo(eventoDetalles.getTitulo());
            evento.setDescripcion(eventoDetalles.getDescripcion());
            evento.setFechaHora(eventoDetalles.getFechaHora());
            return eventoRepository.save(evento);
        }
        throw new RuntimeException("Evento no encontrado con id: " + id);
    }

    // Eliminar un evento por ID
    public void deleteEvento(Long id) {
        eventoRepository.deleteById(id);
    }
}