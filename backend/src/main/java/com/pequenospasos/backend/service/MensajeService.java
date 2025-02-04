package com.pequenospasos.backend.service;

import com.pequenospasos.backend.entity.Mensaje;
import com.pequenospasos.backend.repository.MensajeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MensajeService {

    @Autowired
    private MensajeRepository mensajeRepository;

    // Obtener todos los mensajes
    public List<Mensaje> getAllMensajes() {
        return mensajeRepository.findAll();
    }

    // Obtener mensajes enviados por un usuario específico
    public List<Mensaje> getMensajesByEmisorId(Long emisorId) {
        return mensajeRepository.findByEmisorId(emisorId);
    }

    // Obtener mensajes recibidos por un usuario específico
    public List<Mensaje> getMensajesByReceptorId(Long receptorId) {
        return mensajeRepository.findByReceptorId(receptorId);
    }

    // Obtener mensajes entre dos usuarios específicos (chat entre padre y educador)
    public List<Mensaje> getMensajesEntreUsuarios(Long emisorId, Long receptorId) {
        return mensajeRepository.findByEmisorIdAndReceptorId(emisorId, receptorId);
    }

    // Guardar un nuevo mensaje
    public Mensaje saveMensaje(Mensaje mensaje) {
        mensaje.setFechaHora(LocalDateTime.now()); // Registrar la fecha/hora actual
        mensaje.setEstado(Mensaje.EstadoMensaje.NO_LEIDO);
        return mensajeRepository.save(mensaje);
    }

    // Marcar un mensaje como leído
    public Mensaje marcarMensajeComoLeido(Long id) {
        Optional<Mensaje> mensajeOptional = mensajeRepository.findById(id);
        if (mensajeOptional.isPresent()) {
            Mensaje mensaje = mensajeOptional.get();
            mensaje.setEstado(Mensaje.EstadoMensaje.LEIDO);
            return mensajeRepository.save(mensaje);
        } else {
            throw new RuntimeException("Mensaje no encontrado con id: " + id);
        }
    }

    // Eliminar un mensaje por ID
    public void deleteMensaje(Long id) {
        mensajeRepository.deleteById(id);
    }
}