package com.InventoryManagmentUniandes.InventoryManagmentUniandes.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa un mercado en el sistema
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Market {
    private String id;
    private String name;
    private String address;
    private String location;
    private Image photo;
    private User owner;
    private List<Product> products;

    /**
     * Constructor con parámetros básicos
     */
    public Market(String name, String address, String location) {
        this.name = name;
        this.address = address;
        this.location = location;
        this.products = new ArrayList<>();
    }

    /**
     * Agrega un producto al mercado
     */
    public void addProduct(Product product) {
        if (this.products == null) {
            this.products = new ArrayList<>();
        }
        this.products.add(product);
    }

    /**
     * Elimina un producto del mercado
     */
    public void removeProduct(Product product) {
        if (this.products != null) {
            this.products.remove(product);
        }
    }

    /**
     * Actualiza la información de un producto
     */
    public void updateProduct(Product product) {
        if (this.products != null) {
            for (int i = 0; i < this.products.size(); i++) {
                if (this.products.get(i).getId().equals(product.getId())) {
                    this.products.set(i, product);
                    break;
                }
            }
        }
    }

    /**
     * Obtiene la cantidad total de productos en el mercado
     */
    public int getTotalProductCount() {
        return this.products != null ? this.products.size() : 0;
    }

    /**
     * Representación en string del mercado
     */
    @Override
    public String toString() {
        return "Market{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", location='" + location + '\'' +
                ", owner=" + (owner != null ? owner.getUserName() : "sin propietario") +
                ", productsCount=" + getTotalProductCount() +
                '}';
    }
}
