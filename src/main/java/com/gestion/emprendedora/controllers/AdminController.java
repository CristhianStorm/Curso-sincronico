package com.gestion.emprendedora.controllers;

import com.gestion.emprendedora.dto.CreateUserDTO;
import com.gestion.emprendedora.entities.Usuario;
import com.gestion.emprendedora.services.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para manejar las peticiones de un usuario Administrador.
 * Este es el "jefe" que recibe las solicitudes web.
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    // El controlador le pide al servicio que haga el trabajo.
    @Autowired
    private AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<List<Usuario>> getAllUsers() {
        List<Usuario> usuarios = adminService.getAllUsers();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/pagos-pendientes")
    public ResponseEntity<List<Usuario>> getPendingPayments() {
        List<Usuario> usuariosPendientes = adminService.getPendingUsers();
        return ResponseEntity.ok(usuariosPendientes);
    }

    @PutMapping("/users/{userId}/activate")
    public ResponseEntity<Usuario> activateUser(@PathVariable Long userId) {
        Usuario usuarioActualizado = adminService.activateUser(userId);
        return ResponseEntity.ok(usuarioActualizado);
    }

    @PostMapping("/users")
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserDTO userDTO) {
        try {
            Usuario usuarioCreado = adminService.createUser(userDTO);
            return ResponseEntity.ok(usuarioCreado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

