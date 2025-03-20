package com.pequenospasos.backend.service;

import com.pequenospasos.backend.entity.Notificacion;
import com.pequenospasos.backend.entity.PadresHijos;
import com.pequenospasos.backend.entity.Siesta;
import com.pequenospasos.backend.entity.Usuario;
import com.pequenospasos.backend.repository.SiestaRepository;
import com.pequenospasos.backend.repository.PadresHijosRepository;
import com.pequenospasos.backend.service.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SiestaService {

    @Autowired
    private SiestaRepository siestaRepository;
    @Autowired
    private PadresHijosRepository padresHijosRepository;
    @Autowired
    private NotificacionService notificacionService;

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
        if (siesta == null) {
            throw new IllegalArgumentException("El objeto siesta no puede ser nulo.");
        }

        if (siesta.getEducador() == null) {
            throw new IllegalArgumentException("Debe asignar un educador para registrar la siesta.");
        }

        if (!"EDUCADOR".equals(siesta.getEducador().getTipoUsuario())) {
            throw new IllegalArgumentException("Solo un EDUCADOR puede registrar siestas.");
        }

        if (siesta.getInicioSiesta() == null) {
            siesta.setInicioSiesta(LocalDateTime.now());
        }

        // Verificar que no haya una siesta en curso sin hora de fin
        if (siestaRepository.existsByNinoIdAndFinSiestaIsNull(siesta.getNino().getId())) {
            throw new IllegalArgumentException("El niño debe registrar la hora de salida de la siesta anterior antes de registrar una nueva.");
        }

        // Validar que la hora de fin no sea anterior a la hora de inicio
        if (siesta.getFinSiesta() != null && siesta.getFinSiesta().isBefore(siesta.getInicioSiesta())) {
            throw new IllegalArgumentException("La hora de fin de la siesta no puede ser anterior a la hora de inicio.");
        }

        Siesta nuevaSiesta = siestaRepository.save(siesta);

        List<PadresHijos> relaciones = padresHijosRepository.findByNino(siesta.getNino());

        for (PadresHijos relacion : relaciones) {
            Usuario padre = relacion.getPadre();
            String mensajeNotificacion = "Tu hijo/a ha iniciado una siesta a las " + siesta.getInicioSiesta();

            if (siesta.getFinSiesta() != null) {
                long minutosDormidos = java.time.Duration.between(siesta.getInicioSiesta(), siesta.getFinSiesta()).toMinutes();
                mensajeNotificacion += " y se despertó a las " + siesta.getFinSiesta() + ". Duración: " + minutosDormidos + " minutos.";
            }

            if (siesta.getObservaciones() != null && !siesta.getObservaciones().isEmpty()) {
                mensajeNotificacion += " Observaciones: " + siesta.getObservaciones();
            }

            notificacionService.crearNotificacion(
                    siesta.getEducador(),
                    padre,
                    mensajeNotificacion
            );
        }

        return nuevaSiesta;
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