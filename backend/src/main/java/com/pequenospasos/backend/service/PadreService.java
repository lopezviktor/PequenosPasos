package com.pequenospasos.backend.service;

import com.pequenospasos.backend.entity.Padre;
import com.pequenospasos.backend.repository.PadreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PadreService {

    @Autowired
    private PadreRepository padreRepository;

    // Obtener todos los padres
    public List<Padre> getAllPadres() {
        return padreRepository.findAll();
    }

    // Obtener padre por ID
    public Optional<Padre> getPadreById(Long id) {
        return padreRepository.findById(id);
    }

    // Guardar un nuevo padre
    public Padre savePadre(Padre padre) {
        return padreRepository.save(padre);
    }

    // Actualizar un padre
    public Padre updatePadre(Long id, Padre padreDetalles) {
        return padreRepository.findById(id).map(padre -> {
            padre.setNombre(padreDetalles.getNombre());
            padre.setApellidos(padreDetalles.getApellidos());
            padre.setEmail(padreDetalles.getEmail());
            return padreRepository.save(padre);
        }).orElseThrow(() -> new RuntimeException("Padre no encontrado con id: " + id));
    }

    // Eliminar un padre
    public void deletePadre(Long id) {
        padreRepository.deleteById(id);
    }
}