package com.InventoryManagmentUniandes.InventoryManagmentUniandes.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa un producto en el sistema
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private String id;
    private String marketId;  // ID del mercado al que pertenece
    private String name;
    private String descripcion;
    private double fixedPrice;
    private List<Image> photos;
    private String qr;
    private boolean isComposite;  // true si es compuesto, false si es individual
    private List<String> componentProductIds;  // IDs de productos que componen este (si es compuesto)

    /**
     * Constructor básico
     */
    public Product(String name, String descripcion, double fixedPrice) {
        this.name = name;
        this.descripcion = descripcion;
        this.fixedPrice = fixedPrice;
        this.photos = new ArrayList<>();
        this.isComposite = false;
        this.componentProductIds = new ArrayList<>();
    }

    /**
     * Agrega una foto al producto
     */
    public void addPhoto(Image photo) {
        if (this.photos == null) {
            this.photos = new ArrayList<>();
        }
        this.photos.add(photo);
    }

    /**
     * Agrega un producto componente (para productos compuestos)
     */
    public void addComponentProduct(String productId) {
        if (this.componentProductIds == null) {
            this.componentProductIds = new ArrayList<>();
        }
        this.componentProductIds.add(productId);
        this.isComposite = true;
    }

    /**
     * Elimina un producto componente
     */
    public void removeComponentProduct(String productId) {
        if (this.componentProductIds != null) {
            this.componentProductIds.remove(productId);
            if (this.componentProductIds.isEmpty()) {
                this.isComposite = false;
            }
        }
    }

    /**
     * Genera un código QR para el producto
     */
    public void generateQR() {
        this.qr = "QR_" + this.id + "_" + System.currentTimeMillis();
    }

    /**
     * Representación en string del producto
     */
    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", fixedPrice=" + fixedPrice +
                ", isComposite=" + isComposite +
                ", qr='" + qr + '\'' +
                '}';
    }
}
