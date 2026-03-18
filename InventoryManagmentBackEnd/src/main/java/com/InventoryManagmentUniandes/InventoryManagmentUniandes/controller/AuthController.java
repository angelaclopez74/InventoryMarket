package com.InventoryManagmentUniandes.InventoryManagmentUniandes.controller;

import com.InventoryManagmentUniandes.InventoryManagmentUniandes.model.User;
import com.InventoryManagmentUniandes.InventoryManagmentUniandes.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Controlador de autenticación
 * Valida credenciales contra la base de datos Firebase (Firestore)
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    private final AuthenticationService authenticationService;

    @Autowired
    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    public record LoginRequest(String username, String password) {}

    /**
     * POST /api/auth/login
     * Autentica un usuario verificando contra Firestore
     * 
     * @param request contiene username y password
     * @return respuesta con token y datos del usuario si es exitoso
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest request) {
        if (request == null || request.username() == null || request.password() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        try {
            // Autenticar contra Firestore
            User authenticatedUser = authenticationService.authenticate(
                    request.username(),
                    request.password()
            );

            // Login exitoso
            Map<String, Object> response = new HashMap<>();
            response.put("ok", true);
            response.put("username", authenticatedUser.getUserName());
            response.put("email", authenticatedUser.getEmail());
            response.put("token", generateToken(authenticatedUser.getId())); // Token simple basado en ID
            
            return ResponseEntity.ok(response);

        } catch (AuthenticationService.AuthenticationException e) {
            // Credenciales inválidas
            Map<String, Object> response = new HashMap<>();
            response.put("ok", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } catch (Exception e) {
            // Error del servidor
            Map<String, Object> response = new HashMap<>();
            response.put("ok", false);
            response.put("message", "Error en el servidor al procesar la autenticación");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Genera un token simple para la sesión del usuario
     * En producción, deberías usar JWT
     */
    private String generateToken(String userId) {
        return UUID.randomUUID().toString();
    }
}

