package com.pequenospasos.backend.service;

import com.pequenospasos.backend.entity.Nino;
import com.pequenospasos.backend.entity.Padre;
import com.pequenospasos.backend.entity.PadresHijos;
import com.pequenospasos.backend.repository.NinoRepository;
import com.pequenospasos.backend.repository.PadresHijosRepository;
import com.pequenospasos.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PadresHijosService {

    @Autowired
    private PadresHijosRepository padresHijosRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;  // Ahora usamos UsuarioRepository

    @Autowired
    private NinoRepository ninoRepository;

    // Obtener los niños de un padre (solo PADRES)
    public List<Nino> getNinosByPadreId(Long padreId) {
        return padresHijosRepository.findByPadreId(padreId).stream()
                .filter(ph -> ph.getPadre().getTipoUsuario().equals("PADRE"))
                .map(PadresHijos::getNino)
                .collect(Collectors.toList());
    }

    // Obtener un Padre por su ID
    public Padre findPadreById(Long padreId) {
        return usuarioRepository.findById(padreId)
                .filter(u -> u instanceof Padre)
                .map(u -> (Padre) u)
                .orElse(null);
    }

    // Obtener un Niño por su ID
    public Nino findNinoById(Long ninoId) {
        return ninoRepository.findById(ninoId).orElse(null);
    }

    // Asignar un niño a un padre (validando que sea PADRE)
    public PadresHijos asignarNinoAPadre(Long padreId, Long ninoId) {
        Padre padre = findPadreById(padreId);
        Nino nino = findNinoById(ninoId);

        if (padre == null || nino == null) {
            throw new RuntimeException("Padre o Niño no encontrado.");
        }

        if (!padre.getTipoUsuario().equals("PADRE")) {
            throw new RuntimeException("El usuario no es un padre válido.");
        }

        PadresHijos relacion = new PadresHijos(padre, nino);
        return padresHijosRepository.save(relacion);
    }

    // Eliminar una relación padre-hijo (solo PADRES pueden hacerlo)
    public void deleteRelacionByPadreAndNino(Long padreId, Long ninoId) {
        PadresHijos relacion = padresHijosRepository.findByPadreIdAndNinoId(padreId, ninoId)
                .filter(ph -> ph.getPadre().getTipoUsuario().equals("PADRE"))
                .orElse(null);

        if (relacion != null) {
            padresHijosRepository.delete(relacion);
        } else {
            throw new RuntimeException("Relación no encontrada o usuario no autorizado.");
        }
    }
}