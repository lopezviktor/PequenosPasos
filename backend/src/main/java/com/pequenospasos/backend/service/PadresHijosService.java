package com.pequenospasos.backend.service;

import com.pequenospasos.backend.entity.Nino;
import com.pequenospasos.backend.entity.PadresHijos;
import com.pequenospasos.backend.entity.Padre;
import com.pequenospasos.backend.entity.Usuario;
import com.pequenospasos.backend.enums.Role;
import com.pequenospasos.backend.exception.PadreNotFoundException;
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
    private UsuarioRepository usuarioRepository;

    @Autowired
    private NinoRepository ninoRepository;

    public List<Nino> getNinosByPadreId(Long padreId) {
        return padresHijosRepository.findNinosByPadreId(padreId);
    }

    public List<Padre> getPadresByNinoId(Long ninoId) {
        return padresHijosRepository.findByNinoId(ninoId)
                .stream()
                .map(PadresHijos::getPadre)
                .collect(Collectors.toList());
    }

    public List<Nino> getNinosByUsuario(String username) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmailAndTipoUsuario(username, Role.PADRE);

        if (usuarioOptional.isPresent() && usuarioOptional.get() instanceof Padre) {
            Padre padre = (Padre) usuarioOptional.get();
            return getNinosByPadreId(padre.getId());
        } else {
            throw new PadreNotFoundException("Padre no encontrado con el nombre de usuario: " + username);
        }
    }

    public PadresHijos asignarNinoAPadre(Padre padre, Nino nino) {
        PadresHijos relacion = new PadresHijos(padre, nino);
        return padresHijosRepository.save(relacion);
    }

    public void eliminarRelacionPadreNino(Long padreId, Long ninoId) {
        if (!padresHijosRepository.findByPadreIdAndNinoId(padreId, ninoId).isPresent()) {
            throw new RuntimeException("No existe una relación entre el padre con ID " + padreId + " y el niño con ID " + ninoId);
        }
        padresHijosRepository.deleteByPadreIdAndNinoId(padreId, ninoId);
    }

    public Padre obtenerPadrePorId(Long padreId) {
        return (Padre) usuarioRepository.findById(padreId)
                .orElseThrow(() -> new RuntimeException("Padre no encontrado con ID: " + padreId));
    }

    public Nino obtenerNinoPorId(Long ninoId) {
        return ninoRepository.findById(ninoId)
                .orElseThrow(() -> new RuntimeException("Niño no encontrado con ID: " + ninoId));
    }
}