package com.InventoryManagmentUniandes.InventoryManagmentUniandes.service;

import com.InventoryManagmentUniandes.InventoryManagmentUniandes.model.User;
import com.InventoryManagmentUniandes.InventoryManagmentUniandes.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * Servicio de autenticación de usuarios
 * Valida credenciales contra Firestore
 */
@Service
public class AuthenticationService {
    private final UserRepository userRepository;

    @Autowired
    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Autentica un usuario verificando username y password contra Firestore
     * 
     * @param username el nombre de usuario
     * @param password la contraseña sin encriptar
     * @return el User si las credenciales son válidas
     * @throws AuthenticationException si las credenciales son inválidas
     */
    public User authenticate(String username, String password) throws ExecutionException, InterruptedException {
        if (username == null || username.isBlank()) {
            throw new AuthenticationException("El nombre de usuario es requerido");
        }
        if (password == null || password.isBlank()) {
            throw new AuthenticationException("La contraseña es requerida");
        }

        // Buscar el usuario en Firestore
        User user = userRepository.findByUsername(username);
        
        if (user == null) {
            throw new AuthenticationException("Usuario no encontrado");
        }

        // Validar la contraseña
        if (!user.getPassword().equals(password)) {
            throw new AuthenticationException("Contraseña incorrecta");
        }

        return user;
    }

    /**
     * Crea un nuevo usuario
     * 
     * @param username el nombre de usuario (único)
     * @param password la contraseña
     * @param email el email del usuario
     * @return el usuario creado
     */
    public User createUser(String username, String password, String email) throws ExecutionException, InterruptedException {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("El nombre de usuario no puede estar vacío");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía");
        }

        // Verificar que el usuario no exista
        if (userRepository.exists(username)) {
            throw new IllegalArgumentException("El usuario '" + username + "' ya existe");
        }

        User newUser = new User(username, password, email);
        newUser.setId(UUID.randomUUID().toString());
        
        return userRepository.save(newUser);
    }

    /**
     * Obtiene un usuario por su username
     */
    public User getUserByUsername(String username) throws ExecutionException, InterruptedException {
        return userRepository.findByUsername(username);
    }

    /**
     * Excepción personalizada para errores de autenticación
     */
    public static class AuthenticationException extends RuntimeException {
        public AuthenticationException(String message) {
            super(message);
        }
    }
}
