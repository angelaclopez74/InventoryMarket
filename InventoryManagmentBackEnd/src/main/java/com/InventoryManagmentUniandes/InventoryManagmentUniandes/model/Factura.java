package com.InventoryManagmentUniandes.InventoryManagmentUniandes.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que representa una factura en el sistema
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Factura {
    private String id;
    private String attribute1Type;
    private String attribute2Type;
    private String attribute3Type;
    private long createdDate;

    /**
     * Constructor con parámetros básicos
     */
    public Factura(String attribute1Type, String attribute2Type, String attribute3Type) {
        this.attribute1Type = attribute1Type;
        this.attribute2Type = attribute2Type;
        this.attribute3Type = attribute3Type;
        this.createdDate = System.currentTimeMillis();
    }

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
     * Representación en string de la factura
     */
    @Override
    public String toString() {
        return "Factura{" +
                "id='" + id + '\'' +
                ", attribute1Type='" + attribute1Type + '\'' +
                ", attribute2Type='" + attribute2Type + '\'' +
                ", attribute3Type='" + attribute3Type + '\'' +
                ", createdDate=" + createdDate +
                '}';
    }
}
