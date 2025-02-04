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

    // Obtener eventos por título (ignorando mayúsculas y minúsculas)
    public List<Evento> getEventosByTitulo(String titulo) {
        return eventoRepository.findByTituloContainingIgnoreCase(titulo);
    }

    // Obtener eventos creados por un usuario específico (educador o administrador)
    public List<Evento> getEventosByCreadorId(Long creadorId) {
        return eventoRepository.findByCreadorId(creadorId);
    }

    // Obtener eventos en un rango de fechas
    public List<Evento> getEventosByFecha(LocalDateTime inicio, LocalDateTime fin) {
        return eventoRepository.findByFechaHoraBetween(inicio, fin);
    }

    // Buscar evento por ID
    public Optional<Evento> getEventoById(Long id) {
        return eventoRepository.findById(id);
    }

    // Guardar un nuevo evento
    public Evento saveEvento(Evento evento) {
        evento.setFechaHora(LocalDateTime.now()); // Registrar la fecha/hora actual si no se proporciona
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
            evento.setCreador(eventoDetalles.getCreador());
            return eventoRepository.save(evento);
        } else {
            throw new RuntimeException("Evento no encontrado con id: " + id);
        }
    }

    // Eliminar un evento por ID
    public void deleteEvento(Long id) {
        eventoRepository.deleteById(id);
    }
}