package com.InventoryManagmentUniandes.InventoryManagmentUniandes.service;

import com.InventoryManagmentUniandes.InventoryManagmentUniandes.model.*;
import com.InventoryManagmentUniandes.InventoryManagmentUniandes.repository.MarketRepository;
import com.InventoryManagmentUniandes.InventoryManagmentUniandes.repository.ProductRepository;
import com.InventoryManagmentUniandes.InventoryManagmentUniandes.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * Servicio principal que gestiona todas las operaciones del inventario
 * Maneja mercados y productos con Firestore
 */
@Service
public class InventoryManagementService {
    private final MarketRepository marketRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Autowired
    public InventoryManagementService(
            MarketRepository marketRepository,
            ProductRepository productRepository,
            UserRepository userRepository
    ) {
        this.marketRepository = marketRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    // ==================== OPERACIONES CON MERCADOS ====================

    /**
     * Crea un nuevo mercado
     */
    public Market createMarket(Market market) {
        try {
            if (market.getId() == null || market.getId().isBlank()) {
                market.setId(UUID.randomUUID().toString());
            }
            // Evitar duplicar productos dentro del documento de Market (los productos viven en "products")
            market.setProducts(null);
            return marketRepository.save(market);
        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("No se pudo crear el mercado en Firestore", e);
        }
    }

    /**
     * Obtiene un mercado por su ID
     */
    public Market getMarketById(String marketId) {
        try {
            Market market = marketRepository.findById(marketId);
            if (market == null) return null;
            List<Product> products = productRepository.findByMarketId(marketId);
            market.setProducts(products);
            return market;
        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("No se pudo obtener el mercado desde Firestore", e);
        }
    }

    /**
     * Obtiene todos los mercados
     */
    public Collection<Market> getAllMarkets() {
        try {
            // Por performance, no hidratamos productos aquí
            return marketRepository.findAll();
        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("No se pudo listar mercados desde Firestore", e);
        }
    }

    /**
     * Actualiza un mercado existente
     */
    public Market updateMarket(String marketId, Market updatedMarket) {
        try {
            Market existing = marketRepository.findById(marketId);
            if (existing == null) return null;

            updatedMarket.setId(marketId);
            updatedMarket.setProducts(null);
            return marketRepository.update(marketId, updatedMarket);
        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("No se pudo actualizar el mercado en Firestore", e);
        }
    }

    /**
     * Elimina un mercado
     */
    public boolean deleteMarket(String marketId) {
        try {
            Market existing = marketRepository.findById(marketId);
            if (existing == null) return false;
            marketRepository.delete(marketId);
            return true;
        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("No se pudo eliminar el mercado en Firestore", e);
        }
    }

    // ==================== OPERACIONES CON PRODUCTOS ====================

    /**
     * Crea un nuevo producto
     */
    public Product addProductToMarket(String marketId, Product product) {
        try {
            Market market = marketRepository.findById(marketId);
            if (market == null) {
                throw new NoSuchElementException("Market no existe: " + marketId);
            }

            if (product.getId() == null || product.getId().isBlank()) {
                product.setId(UUID.randomUUID().toString());
            }
            product.setMarketId(marketId);
            product.generateQR();
            return productRepository.save(product);
        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("No se pudo crear el producto en Firestore", e);
        }
    }

    /**
     * Obtiene un producto por su ID
     */
    public Product getProductById(String productId) {
        try {
            return productRepository.findById(productId);
        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("No se pudo obtener el producto desde Firestore", e);
        }
    }

    /**
     * Obtiene todos los productos
     */
    public Collection<Product> getAllProducts() {
        try {
            return productRepository.findAll();
        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("No se pudo listar productos desde Firestore", e);
        }
    }

    /**
     * Obtiene los productos de un mercado específico
     */
    public List<Product> getProductsByMarket(String marketId) {
        try {
            return productRepository.findByMarketId(marketId);
        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("No se pudo listar productos del mercado desde Firestore", e);
        }
    }

    /**
     * Actualiza un producto
     */
    public Product updateProduct(String productId, Product updatedProduct) {
        try {
            Product existing = productRepository.findById(productId);
            if (existing == null) return null;

            updatedProduct.setId(productId);
            if (updatedProduct.getMarketId() == null || updatedProduct.getMarketId().isBlank()) {
                updatedProduct.setMarketId(existing.getMarketId());
            }
            if (updatedProduct.getQr() == null || updatedProduct.getQr().isBlank()) {
                updatedProduct.setQr(existing.getQr());
            }
            return productRepository.update(productId, updatedProduct);
        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("No se pudo actualizar el producto en Firestore", e);
        }
    }

    /**
     * Elimina un producto de un mercado
     */
    public boolean removeProductFromMarket(String marketId, String productId) {
        try {
            Product product = productRepository.findById(productId);
            if (product == null) return false;
            if (product.getMarketId() == null || !product.getMarketId().equals(marketId)) return false;
            productRepository.delete(productId);
            return true;
        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("No se pudo eliminar el producto en Firestore", e);
        }
    }

    /**
     * Agrega un producto componente a un producto compuesto
     */
    public boolean addComponentProduct(String parentProductId, String componentProductId) {
        try {
            Product parent = productRepository.findById(parentProductId);
            Product component = productRepository.findById(componentProductId);

            if (parent == null || component == null) return false;
            parent.addComponentProduct(componentProductId);
            productRepository.update(parentProductId, parent);
            return true;
        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("No se pudo agregar componente en Firestore", e);
        }
    }

    /**
     * Elimina un producto componente de un producto compuesto
     */
    public boolean removeComponentProduct(String parentProductId, String componentProductId) {
        try {
            Product parent = productRepository.findById(parentProductId);
            if (parent == null) return false;
            parent.removeComponentProduct(componentProductId);
            productRepository.update(parentProductId, parent);
            return true;
        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("No se pudo remover componente en Firestore", e);
        }
    }

    /**
     * Obtiene un reporte consolidado del sistema
     */
    public Map<String, Object> getConsolidatedReport() {
        Map<String, Object> report = new HashMap<>();
        try {
            report.put("totalMarkets", marketRepository.count());
            report.put("totalProducts", productRepository.count());
            report.put("timestamp", System.currentTimeMillis());
            return report;
        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("No se pudo generar reporte desde Firestore", e);
        }
    }

    // ==================== UTILIDADES / SEED ====================

    public Map<String, Object> seedExampleData() {
        // Idempotente: usa IDs fijos (sobre-escribe si ya existen)
        
        // Crear usuarios de ejemplo
        try {
            User user1 = new User("adan", "123", "adan@example.com");
            user1.setId(UUID.randomUUID().toString());
            user1.setCreatedDate(System.currentTimeMillis());
            userRepository.save(user1);

            User user2 = new User("juan", "456", "juan@example.com");
            user2.setId(UUID.randomUUID().toString());
            user2.setCreatedDate(System.currentTimeMillis());
            userRepository.save(user2);

            User user3 = new User("maria", "789", "maria@example.com");
            user3.setId(UUID.randomUUID().toString());
            user3.setCreatedDate(System.currentTimeMillis());
            userRepository.save(user3);
        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("No se pudo crear usuarios de ejemplo", e);
        }
        
        // Crear mercados de ejemplo
        Market m1 = new Market("Mercado Central", "Cra 7 # 12-34", "Bogotá");
        m1.setId("market_bogota");
        createMarket(m1);

        Market m2 = new Market("La 14 Mini", "Av 5N # 21-10", "Cali");
        m2.setId("market_cali");
        createMarket(m2);

        addProductToMarket("market_bogota", new Product("Arroz 1kg", "Arroz blanco premium", 5200));
        addProductToMarket("market_bogota", new Product("Huevos x12", "Huevos AA", 9800));
        addProductToMarket("market_bogota", new Product("Leche 1L", "Leche entera", 4100));

        addProductToMarket("market_cali", new Product("Pan tajado", "Pan integral 500g", 7500));
        addProductToMarket("market_cali", new Product("Café 250g", "Café molido", 16500));

        Map<String, Object> out = new HashMap<>();
        out.put("seededUsers", List.of("adan", "juan", "maria"));
        out.put("seededMarkets", List.of("market_bogota", "market_cali"));
        out.put("timestamp", System.currentTimeMillis());
        return out;
    }
}
