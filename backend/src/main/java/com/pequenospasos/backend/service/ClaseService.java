package com.pequenospasos.backend.service;

import com.pequenospasos.backend.entity.Clase;
import com.pequenospasos.backend.entity.Educador;
import com.pequenospasos.backend.entity.Nino;
import com.pequenospasos.backend.entity.PadresHijos;
import com.pequenospasos.backend.entity.Padre;
import com.pequenospasos.backend.repository.ClaseRepository;
import com.pequenospasos.backend.repository.EducadorRepository;
import com.pequenospasos.backend.repository.NinoRepository;
import com.pequenospasos.backend.repository.PadresHijosRepository;
import com.pequenospasos.backend.service.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ClaseService {

    @Autowired
    private ClaseRepository claseRepository;

    @Autowired
    private EducadorRepository educadorRepository;

    @Autowired
    private NinoRepository ninoRepository;

    @Autowired
    private PadresHijosRepository padresHijosRepository;

    @Autowired
    private NotificacionService notificacionService;

    // Obtener todas las clases
    public List<Clase> getAllClases() {
        return claseRepository.findAll();
    }

    // Obtener clase por ID
    public Optional<Clase> getClaseById(Long id) {
        return claseRepository.findById(id);
    }

    // Crear una nueva clase
    public Clase crearClase(String nombre, Long educadorId) {
        Educador educador = educadorRepository.findById(educadorId)
                .orElseThrow(() -> new RuntimeException("Educador no encontrado"));

        Clase clase = new Clase(nombre, educador);
        return claseRepository.save(clase);
    }

    @Transactional
    public Clase actualizarClase(Long id, String nombre, Long educadorId) {
        Clase clase = claseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Clase no encontrada"));

        if (nombre != null && !nombre.isEmpty()) {
            clase.setNombre(nombre);
        }

        if (educadorId != null) {
            Educador educador = educadorRepository.findById(educadorId)
                    .orElseThrow(() -> new RuntimeException("Educador no encontrado"));
            clase.setEducador(educador);
        }

        return claseRepository.save(clase);
    }

    // Asignar un niño a una clase
    @Transactional
    public Clase asignarNinoAClase(Long claseId, Long ninoId) {
        Clase clase = claseRepository.findById(claseId)
                .orElseThrow(() -> new RuntimeException("Clase no encontrada"));

        Nino nino = ninoRepository.findById(ninoId)
                .orElseThrow(() -> new RuntimeException("Niño no encontrado"));

        // Asignar la clase al niño
        nino.setClase(clase);

        // Guardar niño para que se actualice en la BD
        ninoRepository.save(nino);

        // Notificar a los padres
        List<PadresHijos> relaciones = padresHijosRepository.findByNinoId(nino.getId());
        for (PadresHijos relacion : relaciones) {
            Padre padre = relacion.getPadre();
            String mensajeNotificacion = "Tu hijo/a " + nino.getNombre() + " ha sido asignado a la clase "
                                        + clase.getNombre() + " con el educador " + clase.getEducador().getNombre() + ".";

            notificacionService.crearNotificacion(clase.getEducador(), padre, mensajeNotificacion);
        }

        // Guardar clase para que se actualice en la BD
        return claseRepository.save(clase);
    }

    @Transactional
    public Clase eliminarNinoDeClase(Long claseId, Long ninoId) {
        Clase clase = claseRepository.findById(claseId)
                .orElseThrow(() -> new RuntimeException("Clase no encontrada"));

        Nino nino = ninoRepository.findById(ninoId)
                .orElseThrow(() -> new RuntimeException("Niño no encontrado"));

        if (clase.getNinos().contains(nino)) {
            clase.getNinos().remove(nino);
            nino.setClase(null); // Desasignar la clase del niño
            ninoRepository.save(nino);
            return claseRepository.save(clase);
        } else {
            throw new RuntimeException("El niño no está en esta clase");
        }
    }

    @Transactional
    public void eliminarClase(Long id) {
        Clase clase = claseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Clase no encontrada"));

        // Desasociar los niños de esta clase
        for (Nino nino : clase.getNinos()) {
            nino.setClase(null);
        }

        // Guardar los niños actualizados
        ninoRepository.saveAll(clase.getNinos());

        // Ahora sí, eliminar la clase
        claseRepository.delete(clase);
    }
}