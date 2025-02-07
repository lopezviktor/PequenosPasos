package com.pequenospasos.backend.controller;

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
    @GetMapping
    public List<Mensaje> getAllMensajes() {
        return mensajeService.getAllMensajes();
    }

    // Obtener mensajes recibidos por un usuario
    @GetMapping("/receptor/{receptorId}")
    public List<Mensaje> getMensajesByReceptorId(@PathVariable Long receptorId) {
        return mensajeService.getMensajesByReceptorId(receptorId);
    }

    // Obtener mensajes no leídos de un usuario
    @GetMapping("/receptor/{receptorId}/no-leidos")
    public List<Mensaje> getMensajesNoLeidos(@PathVariable Long receptorId) {
        return mensajeService.getMensajesNoLeidos(receptorId);
    }

    // Obtener un mensaje por ID con validación
    @GetMapping("/{id}")
    public Mensaje getMensajeById(@PathVariable Long id) {
        return mensajeService.getMensajeById(id)
                .orElseThrow(() -> new RuntimeException("Mensaje no encontrado con id: " + id));
    }

    // Obtener mensajes entre dos usuarios específicos (chat entre padre y educador)
    @GetMapping("/chat")
    public List<Mensaje> getMensajesEntreUsuarios(@RequestParam Long emisorId, @RequestParam Long receptorId) {
        return mensajeService.getMensajesEntreUsuarios(emisorId, receptorId);
    }

    // Enviar un nuevo mensaje con validación de roles
    @PostMapping
    public Mensaje createMensaje(@RequestBody Mensaje mensaje) {
        if (!(mensaje.getEmisor().getTipoUsuario().equals("PADRE") || mensaje.getEmisor().getTipoUsuario().equals("EDUCADOR"))) {
            throw new RuntimeException("Solo PADRES y EDUCADORES pueden enviar mensajes.");
        }
        return mensajeService.saveMensaje(mensaje);
    }

    // Marcar un mensaje como leído con validación
    @PutMapping("/{id}/marcar-leido")
    public Mensaje marcarComoLeido(@PathVariable Long id) {
        return mensajeService.marcarMensajeComoLeido(id);
    }

    // Marcar todos los mensajes de un usuario como leídos con validación
    @PutMapping("/receptor/{receptorId}/marcar-todos-leidos")
    public void marcarTodosComoLeidos(@PathVariable Long receptorId) {
        mensajeService.marcarTodosComoLeidos(receptorId);
    }

    // Eliminar un mensaje con validación de existencia
    @DeleteMapping("/{id}")
    public void deleteMensaje(@PathVariable Long id) {
        mensajeService.getMensajeById(id)
                .orElseThrow(() -> new RuntimeException("Mensaje no encontrado con id: " + id));

        mensajeService.deleteMensaje(id);
    }
}