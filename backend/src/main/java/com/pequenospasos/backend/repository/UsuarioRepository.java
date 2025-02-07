package com.pequenospasos.backend.repository;

import com.pequenospasos.backend.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Buscar usuario por email (para login o validaciones)
    Optional<Usuario> findByEmail(String email);

    // Buscar usuario por email y tipoUsuario (para asegurar que es del tipo correcto)
    Optional<Usuario> findByEmailAndTipoUsuario(String email, String tipoUsuario);

    // Obtener todos los usuarios de un tipo específico (PADRE, EDUCADOR, ADMIN)
    List<Usuario> findByTipoUsuario(String tipoUsuario);

    // Verificar si un email ya está en uso
    boolean existsByEmail(String email);
}