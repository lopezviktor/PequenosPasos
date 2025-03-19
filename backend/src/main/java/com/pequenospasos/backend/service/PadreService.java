package com.pequenospasos.backend.service;

import com.pequenospasos.backend.entity.Padre;
import com.pequenospasos.backend.entity.Usuario;
import com.pequenospasos.backend.repository.UsuarioRepository;
import com.pequenospasos.backend.repository.PadreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PadreService {

    @Autowired
    private PadreRepository padreRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Obtener todos los padres
    public List<Padre> findAllPadres() {
        return usuarioRepository.findByTipoUsuario("PADRE").stream()
                .map(u -> (Padre) u)
                .toList();
    }

    // Buscar padre por ID
    public Optional<Padre> findPadreById(Long id) {
        return usuarioRepository.findById(id)
                .filter(u -> u instanceof Padre)
                .map(u -> (Padre) u);
    }

    // Buscar padre por email
    public Optional<Padre> findPadreByEmail(String email) {
        return usuarioRepository.findByEmailAndTipoUsuario(email, "PADRE")
                .map(u -> (Padre) u);
    }

    // Buscar padre por apellido
    public List<Padre> findPadresPorApellidos(String apellidos) {
        return padreRepository.findByApellidosContainingIgnoreCase(apellidos);
    }

    // Guardar un nuevo padre (con validación de email único)
    public Padre savePadre(Padre padre) {
        if (usuarioRepository.existsByEmail(padre.getEmail())) {
            throw new RuntimeException("El email ya está registrado.");
        }
        padre.setTipoUsuario("PADRE"); // Asegurar que se guarde correctamente
        return usuarioRepository.save(padre);
    }

    // Actualizar padre existente, sin sobrescribir la contraseña si no se proporciona
    public Padre updatePadre(Long id, Padre padreDetalles) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        if (usuarioOptional.isPresent() && usuarioOptional.get() instanceof Padre padre) {
            padre.setNombre(padreDetalles.getNombre());
            padre.setApellidos(padreDetalles.getApellidos());
            padre.setEmail(padreDetalles.getEmail());
            padre.setTelefono(padreDetalles.getTelefono());

            if (padreDetalles.getPassword() != null && !padreDetalles.getPassword().isEmpty()) {
                padre.setPassword(padreDetalles.getPassword());
            }

            return usuarioRepository.save(padre);
        } else {
            throw new RuntimeException("Padre no encontrado con id: " + id);
        }
    }

    // Eliminar padre por ID
    public void deletePadre(Long id) {
        usuarioRepository.deleteById(id);
    }
}