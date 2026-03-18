# 🚀 GUÍA RÁPIDA - Inventory Management System

## ✅ Estado Actual

- ✅ Backend Spring Boot configurado
- ✅ Firebase Firestore integrado
- ✅ Proyecto compilado exitosamente
- ✅ Servidor corriendo en `http://localhost:8080`

## 🔧 Estructura Simplificada

### Clases Prioritarias Implementadas
```
Model/
├── Product.java          ← Única clase producto
├── Market.java           ← Mercados
└── Image.java            ← Fotos

Service/
└── InventoryManagementService.java  ← Toda la lógica

Repository/
├── ProductRepository.java    ← Acceso a Firestore
└── MarketRepository.java   

Controller/
├── ProductController.java        ← /api/products
├── MarketController.java         ← /api/markets
└── SystemController.java         ← /api/system

Config/
└── FirebaseConfig.java      ← Conexión automática
```

## 📱 Tabla de Endpoints

### PRODUCTOS
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/products` | Todos los productos |
| GET | `/api/products/{id}` | Producto por ID |
| POST | `/api/products/market/{marketId}` | Crear producto |
| PUT | `/api/products/{id}` | Editar producto |
| DELETE | `/api/products/{id}/market/{marketId}` | Borrar producto |
| POST | `/api/products/{parentId}/add-component/{childId}` | Agregar componente |
| DELETE | `/api/products/{parentId}/remove-component/{childId}` | Quitar componente |

### MERCADOS
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/markets` | Todos los mercados |
| GET | `/api/markets/{id}` | Mercado por ID |
| POST | `/api/markets` | Crear mercado |
| PUT | `/api/markets/{id}` | Editar mercado |
| DELETE | `/api/markets/{id}` | Borrar mercado |

### SISTEMA
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/system/health` | Estado del servidor |
| GET | `/api/system/report` | Reporte consolidado |

## 🎯 Cómo Ejecutar

### Opción 1: Con Maven (desarrollo)
```bash
cd InventoryManagmentBackEnd
mvn spring-boot:run
```
Luego accede a: `http://localhost:8080`

### Opción 2: Empaquetar y ejecutar
```bash
mvn clean package
java -jar target/inventory-management-backend-0.0.1-SNAPSHOT.jar
```

## 📊 Atributos de Producto

```json
{
  "id": "uuid-string",
  "marketId": "uuid-string",
  "name": "Nombre del producto",
  "descripcion": "Descripción del producto",
  "fixedPrice": 5000.0,
  "isComposite": false,
  "componentProductIds": [],
  "photos": [],
  "qr": "QR_123456_timestamp"
}
```

## 📊 Atributos de Mercado

```json
{
  "id": "uuid-string",
  "name": "Nombre Mercado",
  "address": "Dirección",
  "location": "Ciudad",
  "products": [],
  "owner": null,
  "photo": null
}
```

## 🗄️ Base de Datos (Firestore)

**Colecciones creadas automáticamente:**
- `products` - Almacena todos los productos
- `markets` - Almacena todos los mercados

## 💡 Ejemplos de Uso

### Crear Mercado
```bash
curl -X POST http://localhost:8080/api/markets \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Mercado Central",
    "address": "Calle Principal 123",
    "location": "Bogotá"
  }'
```

### Crear Producto
```bash
curl -X POST http://localhost:8080/api/products/market/{MARKET_ID} \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Manzanas",
    "descripcion": "Manzanas frescas",
    "fixedPrice": 5000,
    "isComposite": false
  }'
```

### Obtener Productos de Mercado
```bash
curl http://localhost:8080/api/products/market/{MARKET_ID}
```

### Eliminar Producto
```bash
curl -X DELETE http://localhost:8080/api/products/{PRODUCT_ID}/market/{MARKET_ID}
```

## 🔗 Integración Flutter

### Headers Requeridos
```dart
headers: {
  'Content-Type': 'application/json',
}
```

### Ejemplo: Obtener Productos
```dart
final response = await http.get(
  Uri.parse('http://localhost:8080/api/products/market/{marketId}'),
);

if (response.statusCode == 200) {
  final List<dynamic> products = jsonDecode(response.body);
  // Procesar productos
}
```

### Ejemplo: Crear Producto
```dart
final response = await http.post(
  Uri.parse('http://localhost:8080/api/products/market/{marketId}'),
  headers: {'Content-Type': 'application/json'},
  body: jsonEncode({
    'name': 'Producto ejemplo',
    'descripcion': 'Descripción',
    'fixedPrice': 5000,
    'isComposite': false
  }),
);
```

## 🐛 Troubleshooting

### "Error: firebase-credentials.json no encontrado"
**Solución**: Verifica que el archivo existe en `src/main/resources/firebase-credentials.json`

### "Conexión a Firestore rechazada"
**Solución**: Comprueba en Firebase Console que las reglas de seguridad lo permitan

### "Puerto 8080 ya en uso"
**Solución**: Cambia el puerto en `application.properties`:
```properties
server.port=8081
```

## 📝 Próximos Pasos (v2)

- [ ] Agregar autenticación JWT
- [ ] Crear órdenes de entrada/salida
- [ ] Implementar reportes DIAN
- [ ] Facturas
- [ ] Tests unitarios e integración
- [ ] Documentación Swagger/OpenAPI

## 📞 Arquitectura

```
Flutter App
    ↓ (HTTP requests)
Spring Boot Backend (PORT 8080)
    ↓
Firebase Firestore (Cloud)
    ↓
Collections: products, markets
```

## ⚙️ Tecnologías

- **Framework**: Spring Boot 3.5.11
- **Java**: 17
- **Database**: Firebase Firestore
- **Build**: Maven
- **Security**: Spring Security
- **Librerías**: Lombok, Firebase Admin SDK

---

**Versión**: 1.0.0 - Prioridades (Product, Market, Image)  
**Estado**: ✅ Producción lista para Flask/Flutter
