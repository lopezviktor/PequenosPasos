package com.pequenospasos.backend.repository;

import com.pequenospasos.backend.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Buscar usuario por email (para login o validaciones)
    Optional<Usuario> findByEmail(String email);
}