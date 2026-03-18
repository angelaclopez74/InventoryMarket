package com.InventoryManagmentUniandes.InventoryManagmentUniandes.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que representa una orden de entrada (registro de productos que entran al inventario)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntryOrder {
    private String id;
    private String attribute1Type;
    private String attribute2Type;
    private String attribute3Type;

    /**
     * Método genérico que procesa una operación
     */
    public void operation1(String params) {
        // Implementar lógica específica
    }

    /**
     * Método genérico que procesa una operación
     */
    public Object operation2(String params) {
        // Implementar lógica específica
        return null;
    }

    /**
     * Método sin parámetros para operación
     */
    public void operation3() {
        // Implementar lógica específica
    }

    /**
     * Representación en string de la orden de entrada
     */
    @Override
    public String toString() {
        return "EntryOrder{" +
                "id='" + id + '\'' +
                ", attribute1Type='" + attribute1Type + '\'' +
                ", attribute2Type='" + attribute2Type + '\'' +
                ", attribute3Type='" + attribute3Type + '\'' +
                '}';
    }
}
