package com.InventoryManagmentUniandes.InventoryManagmentUniandes.controller;

import com.InventoryManagmentUniandes.InventoryManagmentUniandes.model.*;
import com.InventoryManagmentUniandes.InventoryManagmentUniandes.service.InventoryManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 * Controlador REST para operaciones de mercados
 */
@RestController
@RequestMapping("/api/markets")
@CrossOrigin(origins = "*")
public class MarketController {
    
    @Autowired
    private InventoryManagementService inventoryService;

    /**
     * GET - Obtiene todos los mercados
     */
    @GetMapping
    public ResponseEntity<Collection<Market>> getAllMarkets() {
        Collection<Market> markets = inventoryService.getAllMarkets();
        return ResponseEntity.ok(markets);
    }

    /**
     * GET - Obtiene un mercado por ID
     */
    @GetMapping("/{marketId}")
    public ResponseEntity<Market> getMarketById(@PathVariable String marketId) {
        Market market = inventoryService.getMarketById(marketId);
        if (market != null) {
            return ResponseEntity.ok(market);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * POST - Crea un nuevo mercado
     */
    @PostMapping
    public ResponseEntity<Market> createMarket(@RequestBody Market market) {
        Market createdMarket = inventoryService.createMarket(market);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMarket);
    }

    /**
     * PUT - Actualiza un mercado existente
     */
    @PutMapping("/{marketId}")
    public ResponseEntity<Market> updateMarket(
            @PathVariable String marketId,
            @RequestBody Market market) {
        Market updatedMarket = inventoryService.updateMarket(marketId, market);
        if (updatedMarket != null) {
            return ResponseEntity.ok(updatedMarket);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * DELETE - Elimina un mercado
     */
    @DeleteMapping("/{marketId}")
    public ResponseEntity<Void> deleteMarket(@PathVariable String marketId) {
        if (inventoryService.deleteMarket(marketId)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
