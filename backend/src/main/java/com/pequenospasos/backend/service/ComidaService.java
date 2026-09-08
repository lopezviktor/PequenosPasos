package com.pequenospasos.backend.service;

import com.pequenospasos.backend.dto.ComidaResponse;
import com.pequenospasos.backend.entity.Comida;
import com.pequenospasos.backend.entity.Padre;
import com.pequenospasos.backend.entity.PadresHijos;
import com.pequenospasos.backend.enums.Role;
import com.pequenospasos.backend.repository.ComidaRepository;
import com.pequenospasos.backend.repository.PadresHijosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ComidaService {

    @Autowired
    private ComidaRepository comidaRepository;

    @Autowired
    private NotificacionService notificacionService;

    @Autowired
    private PadresHijosRepository padresHijosRepository;

    // Obtener todas las comidas registradas
    public List<Comida> getAllComidas() {
        return comidaRepository.findAll();
    }

    // Obtener comidas de un niño específico
    public List<ComidaResponse> getComidasByNinoId(Long ninoId) {
        List<Comida> comidaList = comidaRepository.findByNinoId(ninoId);

        return comidaList.stream().map(comida -> new ComidaResponse(
                comida.getId(),
                comida.getHoraComida().toString(),
                comida.getDescripcionComida(),
                comida.getObservaciones(),
                comida.getEducador().getNombre() + " " + comida.getEducador().getApellidos()
        )).collect(Collectors.toList());
    }

    // Obtener comidas registradas por un educador (solo EDUCADORES)
    public List<Comida> getComidasByEducadorId(Long educadorId) {
        return comidaRepository.findByEducadorId(educadorId).stream()
                .filter(c -> c.getEducador().getTipoUsuario() == Role.EDUCADOR)
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

        if (!(comida.getEducador().getTipoUsuario() == Role.EDUCADOR)) {
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

        Comida nuevaComida = comidaRepository.save(comida);

        List<PadresHijos> relaciones = padresHijosRepository.findByNino(comida.getNino());

        for (PadresHijos relacion : relaciones) {
            Padre padre = relacion.getPadre();
            String mensajeNotificacion = "Tu hijo/a ha comido: " + comida.getDescripcionComida();
            if (comida.getObservaciones() != null && !comida.getObservaciones().isEmpty()) {
                mensajeNotificacion += " Observaciones: " + comida.getObservaciones();
            }

            notificacionService.crearNotificacion(
                    comida.getEducador(),
                    padre,
                    mensajeNotificacion
            );
        }

        return nuevaComida;
    }

    // Actualizar una comida (validando que solo un EDUCADOR puede hacerlo)
    public Comida updateComida(Long id, Comida comidaDetalles) {
        Optional<Comida> comidaOptional = comidaRepository.findById(id);
        if (comidaOptional.isPresent()) {
            Comida comida = comidaOptional.get();

            if (!(comidaDetalles.getEducador().getTipoUsuario() == Role.EDUCADOR)) {
                throw new RuntimeException("Solo un EDUCADOR puede actualizar comidas.");
            }

            comida.setHoraComida(comidaDetalles.getHoraComida());
            comida.setDescripcionComida(comidaDetalles.getDescripcionComida());
            comida.setEducador(comidaDetalles.getEducador());
            comida.setObservaciones(comidaDetalles.getObservaciones());

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