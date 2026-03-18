# Inventory Management System - Spring Boot Backend

## Descripción
Sistema de gestión de inventario para mercados desarrollado en Spring Boot. Permite crear mercados, gestionar productos (individuales y compuestos), crear órdenes de entrada/salida, facturas y reportes DIAN.

## Estructura del Proyecto

### Modelo de Datos (`model/`)
- **Image.java** - Representa imágenes/fotos de productos
- **Product.java** - Clase abstracta base para productos
- **IndividualProduct.java** - Producto individual
- **CompositeProduct.java** - Producto compuesto (contiene otros productos)
- **Market.java** - Representa un mercado con sus productos
- **User.java** - Representa un usuario del sistema
- **EntryOrder.java** - Orden de entrada de productos al inventario
- **ExitOrder.java** - Orden de salida de productos del inventario
- **Factura.java** - Factura de transacciones
- **DianReport.java** - Reporte DIAN (Dirección de Impuestos y Aduanas Nacionales)

### Servicio (`service/`)
- **InventoryManagementService.java** - Servicio principal que centraliza todas las operaciones:
  - Gestión de mercados (CRUD)
  - Gestión de productos (CRUD)
  - Órdenes de entrada y salida
  - Facturas
  - Reportes DIAN
  - Reportes generales del sistema

### Controladores (`controller/`)
- **MarketController.java** - Endpoints REST para mercados (`/api/markets`)
- **ProductController.java** - Endpoints REST para productos (`/api/products`)
- **EntryOrderController.java** - Endpoints REST para órdenes de entrada (`/api/entry-orders`)
- **ExitOrderController.java** - Endpoints REST para órdenes de salida (`/api/exit-orders`)
- **FacturaController.java** - Endpoints REST para facturas (`/api/facturas`)
- **DianReportController.java** - Endpoints REST para reportes DIAN (`/api/dian-reports`)
- **SystemController.java** - Endpoints generales del sistema (`/api/system`)

## Endpoints Disponibles

### Sistema
- `GET /api/system/health` - Health check
- `GET /api/system/info` - Información del sistema
- `GET /api/system/report` - Reporte consolidado

### Mercados
- `GET /api/markets` - Obtener todos los mercados
- `GET /api/markets/{marketId}` - Obtener mercado por ID
- `POST /api/markets` - Crear nuevo mercado
- `PUT /api/markets/{marketId}` - Actualizar mercado
- `DELETE /api/markets/{marketId}` - Eliminar mercado

### Productos
- `GET /api/products` - Obtener todos los productos
- `GET /api/products/{productId}` - Obtener producto por ID
- `GET /api/products/market/{marketId}` - Obtener productos de un mercado
- `POST /api/products/market/{marketId}` - Agregar producto a mercado
- `PUT /api/products/{productId}` - Actualizar producto
- `DELETE /api/products/{productId}/market/{marketId}` - Eliminar producto
- `POST /api/products/{compositeId}/add-product/{productId}` - Agregar producto a compuesto
- `GET /api/products/{productId}/price` - Obtener precio de producto

### Órdenes de Entrada
- `GET /api/entry-orders` - Obtener todas
- `GET /api/entry-orders/{orderId}` - Obtener por ID
- `POST /api/entry-orders` - Crear nueva orden

### Órdenes de Salida
- `GET /api/exit-orders` - Obtener todas
- `GET /api/exit-orders/{orderId}` - Obtener por ID
- `POST /api/exit-orders` - Crear nueva orden

### Facturas
- `GET /api/facturas` - Obtener todas
- `GET /api/facturas/{facturaId}` - Obtener por ID
- `POST /api/facturas` - Crear nueva factura

### Reportes DIAN
- `GET /api/dian-reports` - Obtener todos
- `GET /api/dian-reports/{reportId}` - Obtener por ID
- `GET /api/dian-reports/type/{reportType}` - Obtener por tipo
- `POST /api/dian-reports` - Crear nuevo reporte

## Características Principales

### 1. Productos Jerárquicos
- **IndividualProduct**: Producto simple con precio fijo
- **CompositeProduct**: Producto compuesto que contiene otros productos y calcula su precio total automáticamente

### 2. Gestión de Mercados
- Cada mercado puede tener múltiples productos
- Cada mercado tiene un propietario (User)
- Ubicación y dirección del mercado

### 3. Órdenes y Facturas
- Órdenes de entrada: registro de productos que entran
- Órdenes de salida: registro de productos que salen
- Facturas: para transacciones

### 4. Reportes
- Reportes DIAN para cumplimiento fiscal
- Reportes consolidados del sistema
- Reportes de ventas por mercado

### 5. Manejo de Imágenes
- Cada producto puede tener múltiples fotos
- Códigos QR automáticos para identificación

## Tecnologías Utilizadas

- **Framework**: Spring Boot 3.5.11
- **Language**: Java 17
- **Build**: Maven
- **ORM**: (Preparado para Firebase/Firestore)
- **Seguridad**: Spring Security
- **Anotaciones**: Lombok (reducción de código boilerplate)
- **API REST**: Spring Web

## Compilación y Ejecución

### Compilar
```bash
mvn clean compile
```

### Ejecutar pruebas
```bash
mvn test
```

### Empaquetar
```bash
mvn clean package
```

### Ejecutar aplicación
```bash
mvn spring-boot:run
```

O después de empaquetar:
```bash
java -jar target/inventory-management-backend-0.0.1-SNAPSHOT.jar
```

## Próximas Etapas (No Prioritarias en Momento)

Basándose en el UML, las siguientes clases pueden ser implementadas después:
- **Registry** - Registro de usuarios
- Integración completa con **Firebase** para persistencia
- DTOs para transferencia de datos
- Validaciones avanzadas
- Tests unitarios e integración
- Documentación con Swagger/OpenAPI
- Autenticación JWT
- Logs y monitoreo

## Notas para el Frontend (Flutter)

Todos los endpoints están habilitados con CORS (`@CrossOrigin(origins = "*")`), permitiendo consultas desde Flutter:
- URLs base: `http://localhost:8080/api/`
- Content-Type: `application/json`
- Los IDs se generan automáticamente (UUID)

## Ejemplo de Uso

### 1. Crear un mercado
```json
POST /api/markets
{
  "name": "Mercado Central",
  "address": "Calle Principal 123",
  "location": "Bogotá"
}
```

### 2. Crear un producto individual
```json
POST /api/products/market/{marketId}
{
  "name": "Manzanas",
  "fixedPrice": 5000
}
```

### 3. Crear un producto compuesto
```json
POST /api/products/market/{marketId}
{
  "name": "Cesta de Frutas",
  "fixedPrice": 2000,
  "type": "COMPOSITE",
  "products": []
}
```

### 4. Agregar producto a compuesto
```json
POST /api/products/{compositeId}/add-product/{productId}
```

## Contribuciones Futuras

El proyecto está estructurado para ser extensible. Próximas mejoras posibles:
- Persistencia en Firebase
- Reportes más detallados
- Sistema de notificaciones
- Historial de cambios
- Control de stock
- Integración con sistemas de pago
