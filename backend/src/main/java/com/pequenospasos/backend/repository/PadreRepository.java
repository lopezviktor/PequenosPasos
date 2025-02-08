package com.pequenospasos.backend.repository;

import com.pequenospasos.backend.entity.Padre;

import com.pequenospasos.backend.entity.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PadreRepository extends UsuarioRepository {

    // Buscar padres por apellido (JPA ya filtra automáticamente por la clase Padre)
    List<Padre> findByApellidosContainingIgnoreCase(String apellidos);
}