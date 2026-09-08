package com.pequenospasos.backend.controller;

import com.pequenospasos.backend.entity.Admin;
import com.pequenospasos.backend.enums.Role;
import com.pequenospasos.backend.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("/api/admins")
public class AdminController {

    @Autowired
    private AdminService adminService;

    // Obtener todos los administradores
    @GetMapping
    public List<Admin> getAllAdmins() {
        return adminService.getAllAdmins();
    }

    // Obtener un administrador por ID
    @GetMapping("/{id}")
    public Admin getAdminById(@PathVariable Long id) {
        return adminService.getAdminById(id)
                .orElseThrow(() -> new RuntimeException("Administrador no encontrado con id: " + id));
    }

    // Buscar un administrador por email
    @GetMapping("/buscar")
    public Admin getAdminByEmail(@RequestParam String email) {
        return adminService.getAdminByEmail(email)
                .orElseThrow(() -> new RuntimeException("Administrador no encontrado con email: " + email));
    }

    // Crear un nuevo administrador asegurando que el tipo de usuario sea Admin
    @PostMapping
    public Admin createAdmin(@RequestBody Admin admin) {
        admin.setTipoUsuario(Role.ADMIN); // Asegurar que el usuario creado es un ADMIN
        return adminService.saveAdmin(admin);
    }

    // Actualizar un administrador existente sin sobrescribir la contraseña si no se envía
    @PutMapping("/{id}")
    public Admin updateAdmin(@PathVariable Long id, @RequestBody Admin adminDetalles) {
        return adminService.updateAdmin(id, adminDetalles);
    }

    // Eliminar un administrador con validación de existencia
    @DeleteMapping("/{id}")
    public void deleteAdmin(@PathVariable Long id) {
        adminService.deleteAdmin(id);
    }
}