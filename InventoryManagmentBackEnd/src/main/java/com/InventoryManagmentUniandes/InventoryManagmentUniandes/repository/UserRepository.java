package com.InventoryManagmentUniandes.InventoryManagmentUniandes.repository;

import com.InventoryManagmentUniandes.InventoryManagmentUniandes.model.User;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ExecutionException;

/**
 * Repositorio para operaciones de User en Firestore
 * Colección: "users"
 */
@Repository
public class UserRepository {
    private final Firestore firestore;
    private static final String COLLECTION = "users";

    @Autowired
    public UserRepository(Firestore firestore) {
        this.firestore = firestore;
    }

    /**
     * Busca un usuario por su nombre de usuario (username)
     * @param username el nombre de usuario
     * @return el User si existe, null si no existe
     */
    public User findByUsername(String username) throws ExecutionException, InterruptedException {
        DocumentSnapshot doc = firestore
                .collection(COLLECTION)
                .document(username)
                .get()
                .get();

        if (!doc.exists()) {
            return null;
        }

        return doc.toObject(User.class);
    }

    /**
     * Guarda un nuevo usuario
     * @param user el usuario a guardar
     * @return el usuario guardado
     */
    public User save(User user) throws ExecutionException, InterruptedException {
        if (user.getUserName() == null || user.getUserName().isBlank()) {
            throw new IllegalArgumentException("El nombre de usuario no puede estar vacío");
        }
        
        firestore
                .collection(COLLECTION)
                .document(user.getUserName())
                .set(user)
                .get();

        return user;
    }

    /**
     * Actualiza un usuario existente
     * @param username el nombre de usuario
     * @param user los datos actualizados
     * @return el usuario actualizado
     */
    public User update(String username, User user) throws ExecutionException, InterruptedException {
        user.setUserName(username);
        firestore
                .collection(COLLECTION)
                .document(username)
                .set(user)
                .get();

        return user;
    }

    /**
     * Elimina un usuario
     * @param username el nombre de usuario
     */
    public void delete(String username) throws ExecutionException, InterruptedException {
        firestore
                .collection(COLLECTION)
                .document(username)
                .delete()
                .get();
    }

    /**
     * Verifica si un usuario existe
     * @param username el nombre de usuario
     * @return true si existe, false si no
     */
    public boolean exists(String username) throws ExecutionException, InterruptedException {
        DocumentSnapshot doc = firestore
                .collection(COLLECTION)
                .document(username)
                .get()
                .get();

        return doc.exists();
    }
}
