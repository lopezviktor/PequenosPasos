package com.pequenospasos.backend.repository;

import com.pequenospasos.backend.entity.Admin;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminRepository extends UsuarioRepository {

    // Buscar Admins por apellido, asegurando que sean ADMIN
    @Query("SELECT a FROM Admin a WHERE a.apellidos LIKE %:apellidos% AND TYPE(a) = Admin")
    List<Admin> findByApellidosContainingIgnoreCase(@Param("apellidos") String apellidos);
}