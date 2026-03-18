package com.InventoryManagmentUniandes.InventoryManagmentUniandes.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.io.IOException;
import java.io.InputStream;

/**
 * Configuración de Firebase para la aplicación
 * Inicializa Firestore y lo pone disponible para inyección de dependencias
 */
@Configuration
public class FirebaseConfig {

    /**
     * Inicializa Firebase y retorna la instancia de Firestore
     */
    @Bean
    public Firestore firestore() throws IOException {
        // Cargar credenciales desde el archivo JSON
        InputStream inputStream = getClass().getClassLoader()
                .getResourceAsStream("firebase-credentials.json");

        // Verificar que el archivo existe
        if (inputStream == null) {
            throw new IOException("El archivo firebase-credentials.json no se encuentra en src/main/resources/");
        }

        // Crear GoogleCredentials desde el archivo
        GoogleCredentials credentials = GoogleCredentials.fromStream(inputStream);

        // Configurar opciones de Firebase
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(credentials)
                .build();

        // Inicializar la aplicación si no está ya inicializada
        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }

        // Retornar la instancia de Firestore
        return FirestoreClient.getFirestore();
    }
}
