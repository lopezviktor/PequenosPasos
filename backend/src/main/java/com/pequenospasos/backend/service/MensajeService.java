package com.pequenospasos.backend.service;

import com.pequenospasos.backend.entity.Mensaje;
import com.pequenospasos.backend.entity.Usuario; // Asegúrate de que esta importación sea correcta
import com.pequenospasos.backend.repository.MensajeRepository;
import com.pequenospasos.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class MensajeService {

    @Autowired
    private MensajeRepository mensajeRepository;

    @Autowired
    private NotificacionService notificacionService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Obtener todos los mensajes
    public List<Mensaje> getAllMensajes() {
        return mensajeRepository.findAll();
    }

    // Obtener un mensaje por ID
    public Optional<Mensaje> getMensajeById(Long id) {
        return mensajeRepository.findById(id);
    }

    // Obtener mensajes enviados por un usuario específico (solo PADRES y EDUCADORES)
    public List<Mensaje> getMensajesByEmisorId(Long emisorId) {
        return mensajeRepository.findByEmisorId(emisorId).stream()
                .filter(m -> m.getEmisor().getTipoUsuario().equals("PADRE") || m.getEmisor().getTipoUsuario().equals("EDUCADOR"))
                .toList();
    }

    // Obtener mensajes recibidos por un usuario específico (solo PADRES y EDUCADORES)
    public List<Mensaje> getMensajesByReceptorId(Long receptorId) {
        return mensajeRepository.findByReceptorId(receptorId).stream()
                .filter(m -> m.getReceptor().getTipoUsuario().equals("PADRE") || m.getReceptor().getTipoUsuario().equals("EDUCADOR"))
                .toList();
    }

    // Obtener mensajes entre dos usuarios específicos (solo PADRES y EDUCADORES)
    public List<Mensaje> getMensajesEntreUsuarios(Long emisorId, Long receptorId) {
        return mensajeRepository.findByEmisorIdAndReceptorId(emisorId, receptorId).stream()
                .filter(m -> (m.getEmisor().getTipoUsuario().equals("PADRE") || m.getEmisor().getTipoUsuario().equals("EDUCADOR")) &&
                        (m.getReceptor().getTipoUsuario().equals("PADRE") || m.getReceptor().getTipoUsuario().equals("EDUCADOR")))
                .toList();
    }

    public List<Mensaje> getConversacionCompleta(Long usuario1Id, Long usuario2Id) {
        List<Mensaje> mensajes1 = getMensajesEntreUsuarios(usuario1Id, usuario2Id);
        List<Mensaje> mensajes2 = getMensajesEntreUsuarios(usuario2Id, usuario1Id);

        // Unimos y ordenamos por fecha
        return Stream.concat(mensajes1.stream(), mensajes2.stream())
                .sorted(Comparator.comparing(Mensaje::getFechaHora))
                .toList();
    }

    // Guardar un nuevo mensaje (validando solo PADRES y EDUCADORES)
    public Mensaje saveMensaje(Mensaje mensaje) {
        if (!esUsuarioValido(mensaje.getEmisor())) {
            throw new RuntimeException("Solo PADRES y EDUCADORES pueden enviar mensajes.");
        }
        mensaje.setFechaHora(LocalDateTime.now());
        mensaje.setEstado(Mensaje.EstadoMensaje.NO_LEIDO);

        Usuario emisorCompleto = mensaje.getEmisor();
        if (emisorCompleto.getNombre() == null) {
            emisorCompleto = usuarioRepository.findById(emisorCompleto.getId())
                    .orElseThrow(() -> new RuntimeException("No se pudo cargar el emisor"));
        }

        String mensajeNotificacion = "Tienes un nuevo mensaje de " + emisorCompleto.getNombre();
        notificacionService.crearNotificacion(mensaje.getEmisor(), mensaje.getReceptor(), mensajeNotificacion);

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

    // Obtener mensajes no leídos de un usuario
    public List<Mensaje> getMensajesNoLeidos(Long receptorId) {
        return mensajeRepository.findByReceptorIdAndEstado(receptorId, Mensaje.EstadoMensaje.NO_LEIDO);
    }

    // Marcar todos los mensajes de un usuario como leídos (optimizado)
    public void marcarTodosComoLeidos(Long receptorId) {
        List<Mensaje> mensajesNoLeidos = mensajeRepository.findByReceptorIdAndEstado(receptorId, Mensaje.EstadoMensaje.NO_LEIDO);
        if (!mensajesNoLeidos.isEmpty()) {
            mensajesNoLeidos.forEach(m -> m.setEstado(Mensaje.EstadoMensaje.LEIDO));
            mensajeRepository.saveAll(mensajesNoLeidos);
        }
    }

    // Eliminar un mensaje por ID
    public void deleteMensaje(Long id) {
        mensajeRepository.deleteById(id);
    }

    public boolean esUsuarioValido(Usuario usuario) {
        return usuario != null &&
               ("PADRE".equals(usuario.getTipoUsuario()) || "EDUCADOR".equals(usuario.getTipoUsuario()));
    }

    public List<Mensaje> getMensajesDeUsuario(Long usuarioId) {
        return mensajeRepository.findByUsuarioId(usuarioId);
    }
}