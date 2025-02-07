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

    // Buscar eventos creados por un usuario específico (solo EDUCADORES y ADMINISTRADORES)
    public List<Evento> getEventosByCreador(Long creadorId) {
        return eventoRepository.findByCreadorId(creadorId).stream()
                .filter(e -> e.getCreador().getTipoUsuario().equals("EDUCADOR") || e.getCreador().getTipoUsuario().equals("ADMIN"))
                .toList();
    }

    // Crear un nuevo evento (validando que solo un EDUCADOR o ADMIN pueda hacerlo)
    public Evento saveEvento(Evento evento) {
        if (!(evento.getCreador().getTipoUsuario().equals("EDUCADOR") || evento.getCreador().getTipoUsuario().equals("ADMIN"))) {
            throw new RuntimeException("Solo un EDUCADOR o ADMINISTRADOR puede crear eventos.");
        }
        if (evento.getFechaHora() == null) {
            evento.setFechaHora(LocalDateTime.now()); // Si no hay fecha, se asigna la actual
        }
        return eventoRepository.save(evento);
    }

    // Actualizar un evento existente (validando que solo el creador pueda modificarlo)
    public Evento updateEvento(Long id, Evento eventoDetalles) {
        Optional<Evento> eventoOptional = eventoRepository.findById(id);
        if (eventoOptional.isPresent()) {
            Evento evento = eventoOptional.get();

            if (!(evento.getCreador().getTipoUsuario().equals("EDUCADOR") || evento.getCreador().getTipoUsuario().equals("ADMIN"))) {
                throw new RuntimeException("Solo un EDUCADOR o ADMINISTRADOR puede actualizar eventos.");
            }

            evento.setTitulo(eventoDetalles.getTitulo());
            evento.setDescripcion(eventoDetalles.getDescripcion());
            evento.setFechaHora(eventoDetalles.getFechaHora());
            return eventoRepository.save(evento);
        }
        throw new RuntimeException("Evento no encontrado con id: " + id);
    }

    // Eliminar un evento por ID (validando que solo el creador pueda eliminarlo)
    public void deleteEvento(Long id) {
        Optional<Evento> eventoOptional = eventoRepository.findById(id);
        if (eventoOptional.isPresent()) {
            Evento evento = eventoOptional.get();
            if (!(evento.getCreador().getTipoUsuario().equals("EDUCADOR") || evento.getCreador().getTipoUsuario().equals("ADMIN"))) {
                throw new RuntimeException("Solo un EDUCADOR o ADMINISTRADOR puede eliminar eventos.");
            }
            eventoRepository.deleteById(id);
        } else {
            throw new RuntimeException("Evento no encontrado con id: " + id);
        }
    }
}