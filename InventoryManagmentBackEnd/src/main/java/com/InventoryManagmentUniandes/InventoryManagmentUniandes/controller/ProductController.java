package com.InventoryManagmentUniandes.InventoryManagmentUniandes.controller;

import com.InventoryManagmentUniandes.InventoryManagmentUniandes.model.*;
import com.InventoryManagmentUniandes.InventoryManagmentUniandes.service.InventoryManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 * Controlador REST para operaciones con productos
 */
@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {
    
    @Autowired
    private InventoryManagementService inventoryService;

    /**
     * GET - Obtiene todos los productos
     */
    @GetMapping
    public ResponseEntity<Collection<Product>> getAllProducts() {
        Collection<Product> products = inventoryService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    /**
     * GET - Obtiene un producto por ID
     */
    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable String productId) {
        Product product = inventoryService.getProductById(productId);
        if (product != null) {
            return ResponseEntity.ok(product);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * GET - Obtiene todos los productos de un mercado
     */
    @GetMapping("/market/{marketId}")
    public ResponseEntity<List<Product>> getProductsByMarket(@PathVariable String marketId) {
        List<Product> products = inventoryService.getProductsByMarket(marketId);
        return ResponseEntity.ok(products);
    }

    /**
     * POST - Agrega un nuevo producto a un mercado
     */
    @PostMapping("/market/{marketId}")
    public ResponseEntity<Product> addProductToMarket(
            @PathVariable String marketId,
            @RequestBody Product product) {
        Product createdProduct = inventoryService.addProductToMarket(marketId, product);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    /**
     * PUT - Actualiza un producto
     */
    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable String productId,
            @RequestBody Product product) {
        Product updatedProduct = inventoryService.updateProduct(productId, product);
        if (updatedProduct != null) {
            return ResponseEntity.ok(updatedProduct);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * DELETE - Elimina un producto de un mercado
     */
    @DeleteMapping("/{productId}/market/{marketId}")
    public ResponseEntity<Void> removeProductFromMarket(
            @PathVariable String marketId,
            @PathVariable String productId) {
        if (inventoryService.removeProductFromMarket(marketId, productId)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * POST - Agrega un producto componente a otro producto
     */
    @PostMapping("/{parentId}/add-component/{componentId}")
    public ResponseEntity<Boolean> addComponentProduct(
            @PathVariable String parentId,
            @PathVariable String componentId) {
        Product component = inventoryService.getProductById(componentId);
        if (component == null) {
            return ResponseEntity.notFound().build();
        }
        
        boolean success = inventoryService.addComponentProduct(parentId, componentId);
        return ResponseEntity.ok(success);
    }

    /**
     * DELETE - Elimina un producto componente
     */
    @DeleteMapping("/{parentId}/remove-component/{componentId}")
    public ResponseEntity<Boolean> removeComponentProduct(
            @PathVariable String parentId,
            @PathVariable String componentId) {
        boolean success = inventoryService.removeComponentProduct(parentId, componentId);
        return ResponseEntity.ok(success);
    }
}
