package com.pequenospasos.backend.controller;
import org.springframework.security.access.prepost.PreAuthorize;
import com.pequenospasos.backend.dto.MensajeDTO;
import java.util.stream.Collectors;

import com.pequenospasos.backend.entity.Mensaje;
import com.pequenospasos.backend.service.MensajeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mensajes")
public class MensajeController {

    @Autowired
    private MensajeService mensajeService;

    // Obtener todos los mensajes
    @PreAuthorize("hasAnyRole('PADRE', 'EDUCADOR')")
    @GetMapping
    public List<MensajeDTO> getAllMensajes() {
        return mensajeService.getAllMensajes().stream().map(this::toDTO).collect(Collectors.toList());
    }

    // Obtener mensajes recibidos por un usuario
    @PreAuthorize("hasAnyRole('PADRE', 'EDUCADOR')")
    @GetMapping("/receptor/{receptorId}")
    public List<MensajeDTO> getMensajesByReceptorId(@PathVariable Long receptorId) {
        return mensajeService.getMensajesByReceptorId(receptorId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    // Obtener mensajes no leídos de un usuario
    @PreAuthorize("hasAnyRole('PADRE', 'EDUCADOR')")
    @GetMapping("/receptor/{receptorId}/no-leidos")
    public List<MensajeDTO> getMensajesNoLeidos(@PathVariable Long receptorId) {
        return mensajeService.getMensajesNoLeidos(receptorId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    // Obtener un mensaje por ID con validación
    @PreAuthorize("hasAnyRole('PADRE', 'EDUCADOR')")
    @GetMapping("/{id}")
    public MensajeDTO getMensajeById(@PathVariable Long id) {
        return toDTO(mensajeService.getMensajeById(id)
                .orElseThrow(() -> new RuntimeException("Mensaje no encontrado con id: " + id)));
    }

    // Obtener mensajes entre dos usuarios específicos (chat entre padre y educador)
    @PreAuthorize("hasAnyRole('PADRE', 'EDUCADOR')")
    @GetMapping("/chat")
    public List<MensajeDTO> getMensajesEntreUsuarios(@RequestParam Long emisorId, @RequestParam Long receptorId) {
        return mensajeService.getMensajesEntreUsuarios(emisorId, receptorId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    // Obtener el chat completo entre dos usuarios
    @PreAuthorize("hasAnyRole('PADRE', 'EDUCADOR')")
    @GetMapping("/chat/completo")
    public List<MensajeDTO> getConversacionCompleta(@RequestParam Long usuario1, @RequestParam Long usuario2) {
        return mensajeService.getConversacionCompleta(usuario1, usuario2).stream()
                .map(this::toDTO)
                .toList();
    }

    // Enviar un nuevo mensaje con validación de roles
    @PreAuthorize("hasAnyRole('PADRE', 'EDUCADOR')")
    @PostMapping
    public MensajeDTO createMensaje(@RequestBody Mensaje mensaje) {
        if (mensaje.getEmisor() == null || !mensajeService.esUsuarioValido(mensaje.getEmisor())) {
            throw new RuntimeException("Solo PADRES y EDUCADORES pueden enviar mensajes.");
        }
        return toDTO(mensajeService.saveMensaje(mensaje));
    }

    // Marcar un mensaje como leído con validación
    @PreAuthorize("hasAnyRole('PADRE', 'EDUCADOR')")
    @PutMapping("/{id}/marcar-leido")
    public MensajeDTO marcarComoLeido(@PathVariable Long id) {
        return toDTO(mensajeService.marcarMensajeComoLeido(id));
    }

    // Marcar todos los mensajes de un usuario como leídos con validación
    @PreAuthorize("hasAnyRole('PADRE', 'EDUCADOR')")
    @PutMapping("/receptor/{receptorId}/marcar-todos-leidos")
    public void marcarTodosComoLeidos(@PathVariable Long receptorId) {
        mensajeService.marcarTodosComoLeidos(receptorId);
    }

    // Eliminar un mensaje con validación de existencia
    @PreAuthorize("hasAnyRole('PADRE', 'EDUCADOR')")
    @DeleteMapping("/{id}")
    public void deleteMensaje(@PathVariable Long id) {
        mensajeService.getMensajeById(id)
                .orElseThrow(() -> new RuntimeException("Mensaje no encontrado con id: " + id));

        mensajeService.deleteMensaje(id);
    }

    private MensajeDTO toDTO(Mensaje mensaje) {
        MensajeDTO dto = new MensajeDTO();
        dto.setId(mensaje.getId());
        dto.setContenido(mensaje.getContenido());
        dto.setFechaHora(mensaje.getFechaHora());
        dto.setEstado(mensaje.getEstado().toString());
        dto.setEmisorId(mensaje.getEmisor().getId());
        dto.setEmisorNombre(mensaje.getEmisor().getNombre());
        dto.setReceptorId(mensaje.getReceptor().getId());
        dto.setReceptorNombre(mensaje.getReceptor().getNombre());
        return dto;
    }
}