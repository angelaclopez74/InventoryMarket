package com.InventoryManagmentUniandes.InventoryManagmentUniandes.repository;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.InventoryManagmentUniandes.InventoryManagmentUniandes.model.Market;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Repositorio para operaciones CRUD de mercados en Firestore
 */
@Repository
public class MarketRepository {

    private static final String COLLECTION_NAME = "markets";

    @Autowired
    private Firestore firestore;

    /**
     * Guarda un nuevo mercado en Firestore
     */
    public Market save(Market market) throws ExecutionException, InterruptedException {
        firestore.collection(COLLECTION_NAME)
                .document(market.getId())
                .set(market)
                .get();
        return market;
    }

    /**
     * Obtiene un mercado por su ID
     */
    public Market findById(String marketId) throws ExecutionException, InterruptedException {
        DocumentSnapshot doc = firestore.collection(COLLECTION_NAME)
                .document(marketId)
                .get()
                .get();

        if (doc.exists()) {
            return doc.toObject(Market.class);
        }
        return null;
    }

    /**
     * Obtiene todos los mercados
     */
    public List<Market> findAll() throws ExecutionException, InterruptedException {
        QuerySnapshot qs = firestore.collection(COLLECTION_NAME)
                .get()
                .get();

        List<Market> markets = new ArrayList<>();
        for (DocumentSnapshot doc : qs.getDocuments()) {
            Market market = doc.toObject(Market.class);
            markets.add(market);
        }
        return markets;
    }

    /**
     * Actualiza un mercado existente
     */
    public Market update(String marketId, Market market) throws ExecutionException, InterruptedException {
        market.setId(marketId);
        firestore.collection(COLLECTION_NAME)
                .document(marketId)
                .set(market)
                .get();
        return market;
    }

    /**
     * Elimina un mercado por su ID
     */
    public void delete(String marketId) throws ExecutionException, InterruptedException {
        firestore.collection(COLLECTION_NAME)
                .document(marketId)
                .delete()
                .get();
    }

    /**
     * Cuenta el total de mercados
     */
    public long count() throws ExecutionException, InterruptedException {
        return firestore.collection(COLLECTION_NAME)
                .get()
                .get()
                .size();
    }
}
