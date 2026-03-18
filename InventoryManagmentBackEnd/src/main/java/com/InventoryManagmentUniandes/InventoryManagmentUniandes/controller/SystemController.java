package com.InventoryManagmentUniandes.InventoryManagmentUniandes.controller;

import com.InventoryManagmentUniandes.InventoryManagmentUniandes.service.InventoryManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 * Controlador REST para operaciones generales del sistema
 */
@RestController
@RequestMapping("/api/system")
@CrossOrigin(origins = "*")
public class SystemController {
    
    @Autowired
    private InventoryManagementService inventoryService;

    /**
     * GET - Obtiene un reporte consolidado del sistema
     */
    @GetMapping("/report")
    public ResponseEntity<Map<String, Object>> getConsolidatedReport() {
        Map<String, Object> report = inventoryService.getConsolidatedReport();
        return ResponseEntity.ok(report);
    }

    /**
     * GET - Health check del sistema
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "Inventory Management System is running");
        response.put("timestamp", String.valueOf(System.currentTimeMillis()));
        return ResponseEntity.ok(response);
    }

    /**
     * GET - Información del sistema
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, String>> getSystemInfo() {
        Map<String, String> info = new HashMap<>();
        info.put("name", "Inventory Management System");
        info.put("version", "1.0.0");
        info.put("description", "Sistema de gestión de inventario para mercados");
        info.put("backend", "Spring Boot");
        info.put("database", "Firebase");
        info.put("frontend", "Flutter");
        return ResponseEntity.ok(info);
    }

    /**
     * POST - Carga datos de ejemplo en Firestore (idempotente)
     */
    @PostMapping("/seed")
    public ResponseEntity<Map<String, Object>> seed() {
        return ResponseEntity.ok(inventoryService.seedExampleData());
    }
}
