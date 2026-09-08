package com.pequenospasos.backend.repository;

import com.pequenospasos.backend.entity.Usuario;
import com.pequenospasos.backend.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    @Query("SELECT u FROM Usuario u WHERE u.email = :email AND u.tipoUsuario = :tipoUsuario")
    Optional<Usuario> findByEmailAndTipoUsuario(@Param("email") String email, @Param("tipoUsuario") Role tipoUsuario);

    List<Usuario> findByTipoUsuario(Role tipoUsuario);

    boolean existsByEmail(String email);
}