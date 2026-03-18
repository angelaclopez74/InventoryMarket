package com.InventoryManagmentUniandes.InventoryManagmentUniandes.repository;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;
import com.InventoryManagmentUniandes.InventoryManagmentUniandes.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Repositorio para operaciones CRUD de productos en Firestore
 */
@Repository
public class ProductRepository {

    private static final String COLLECTION_NAME = "products";

    @Autowired
    private Firestore firestore;

    /**
     * Guarda un nuevo producto en Firestore
     */
    public Product save(Product product) throws ExecutionException, InterruptedException {
        firestore.collection(COLLECTION_NAME)
                .document(product.getId())
                .set(product)
                .get();
        return product;
    }

    /**
     * Obtiene un producto por su ID
     */
    public Product findById(String productId) throws ExecutionException, InterruptedException {
        DocumentSnapshot doc = firestore.collection(COLLECTION_NAME)
                .document(productId)
                .get()
                .get();

        if (doc.exists()) {
            return doc.toObject(Product.class);
        }
        return null;
    }

    /**
     * Obtiene todos los productos
     */
    public List<Product> findAll() throws ExecutionException, InterruptedException {
        QuerySnapshot qs = firestore.collection(COLLECTION_NAME)
                .get()
                .get();

        List<Product> products = new ArrayList<>();
        for (DocumentSnapshot doc : qs.getDocuments()) {
            Product product = doc.toObject(Product.class);
            products.add(product);
        }
        return products;
    }

    /**
     * Actualiza un producto existente
     */
    public Product update(String productId, Product product) throws ExecutionException, InterruptedException {
        product.setId(productId);
        firestore.collection(COLLECTION_NAME)
                .document(productId)
                .set(product)
                .get();
        return product;
    }

    /**
     * Elimina un producto por su ID
     */
    public void delete(String productId) throws ExecutionException, InterruptedException {
        firestore.collection(COLLECTION_NAME)
                .document(productId)
                .delete()
                .get();
    }

    /**
     * Obtiene productos de un mercado específico
     */
    public List<Product> findByMarketId(String marketId) throws ExecutionException, InterruptedException {
        // Nota: Requiere un campo "marketId" en cada documento de producto
        Query query = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("marketId", marketId);

        QuerySnapshot qs = query.get().get();
        List<Product> products = new ArrayList<>();

        for (DocumentSnapshot doc : qs.getDocuments()) {
            Product product = doc.toObject(Product.class);
            products.add(product);
        }
        return products;
    }

    /**
     * Cuenta el total de productos
     */
    public long count() throws ExecutionException, InterruptedException {
        return firestore.collection(COLLECTION_NAME)
                .get()
                .get()
                .size();
    }
}
