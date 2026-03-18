package com.InventoryManagmentUniandes.InventoryManagmentUniandes.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que representa una imagen en el sistema
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Image {
    private String id;
    private String url;
    private String fileName;
    private String contentType;
    private long size;
    private long uploadDate;

    /**
     * Constructor alternativo con parámetros básicos
     */
    public Image(String url, String fileName) {
        this.url = url;
        this.fileName = fileName;
        this.uploadDate = System.currentTimeMillis();
    }
}
