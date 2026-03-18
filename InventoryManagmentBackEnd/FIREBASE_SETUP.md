# Firebase Setup - Guía para Configurar Firestore

## Pasos para Configurar Firebase en tu Proyecto

### 1. Crear Proyecto en Firebase Console

1. Ve a [Firebase Console](https://console.firebase.google.com/)
2. Haz clic en **"Agregar proyecto"**
3. Ingresa el nombre de tu proyecto: `InventoryManagement` (o el que prefieras)
4. Selecciona tu país
5. Acepta los términos y crea el proyecto

### 2. Generar Credenciales de Firebase

1. En Firebase Console, ve a **Project Settings** (ícono de engranaje arriba a la izquierda)
2. Selecciona la pestaña **"Service Accounts"**
3. Haz clic en **"Generate New Private Key"** (Generar nueva clave privada)
4. Se descargará un archivo JSON automáticamente

### 3. Colocar el Archivo de Credenciales

1. Abre el archivo JSON descargado
2. Cópialo completamente
3. En tu proyecto, ve a: `src/main/resources/`
4. Crea un archivo llamado: `firebase-credentials.json`
5. Pega el contenido del JSON descargado
6. **IMPORTANTE**: Este archivo NO se subirá a git (ya está en .gitignore)

### 4. Crear Colecciones en Firestore

Ahora necesitas crear las colecciones en Firestore:

#### Opción A: Crear colecciones manualmente desde Firebase Console

1. En Firebase Console, ve a **Firestore Database**
2. Haz clic en **"Create Database"**
3. Selecciona modo **"Start in production mode"**
4. Elige la ubicación más cercana
5. Una vez creada la base de datos, crea dos colecciones:
   - **`products`** - Para los productos
   - **`markets`** - Para los mercados

#### Opción B: Las colecciones se crearán automáticamente

Cuando tu aplicación realice las primeras inserciones, Firestore creará automáticamente las colecciones.

### 5. Configurar las Reglas de Seguridad de Firestore

En Firebase Console → Firestore Database → Rules, usa estas reglas (para desarrollo):

```firestore
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /{document=**} {
      allow read, write: if request.auth != null;
    }
  }
}
```

**⚠️ NOTA**: Estas reglas requieren autenticación. Para desarrollo inicial, puedes usar:

```firestore
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /{document=**} {
      allow read, write: if true;
    }
  }
}
```

## Estructura de Documentos en Firestore

### Colección: `products`
```json
{
  "id": "uuid-string",
  "marketId": "uuid-string",
  "name": "Manzanas",
  "descripcion": "Manzanas frescas",
  "fixedPrice": 5000,
  "isComposite": false,
  "componentProductIds": [],
  "photos": [],
  "qr": "QR_123456_timestamp"
}
```

### Colección: `markets`
```json
{
  "id": "uuid-string",
  "name": "Mercado Central",
  "address": "Calle Principal 123",
  "location": "Bogotá",
  "products": [],
  "owner": null,
  "photo": null
}
```

## Compilación y Ejecución

Una vez tengas el archivo de credenciales configurado:

```bash
# Compilar
mvn clean compile

# Ejecutar
mvn spring-boot:run
```

## Endpoints Disponibles

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
- `POST /api/products/market/{marketId}` - Crear producto en mercado
- `PUT /api/products/{productId}` - Actualizar producto
- `DELETE /api/products/{productId}/market/{marketId}` - Eliminar producto
- `POST /api/products/{parentId}/add-component/{componentId}` - Agregar componente
- `DELETE /api/products/{parentId}/remove-component/{componentId}` - Eliminar componente

## Clases Prioritarias Implementadas

✅ **Product** - Clase única para productos (individual y compuesto)
✅ **Market** - Mercados con propietario
✅ **Image** - Fotos de productos
✅ **InventoryManagementService** - Lógica de negocio
✅ **ProductRepository** - Acceso a Firestore para productos
✅ **MarketRepository** - Acceso a Firestore para mercados
✅ **FirebaseConfig** - Configuración automática de Firebase

## Ejemplo de Uso desde Flutter

### Crear un Mercado
```dart
final response = await http.post(
  Uri.parse('http://localhost:8080/api/markets'),
  headers: {'Content-Type': 'application/json'},
  body: jsonEncode({
    'name': 'Mercado Central',
    'address': 'Calle Principal 123',
    'location': 'Bogotá'
  }),
);
```

### Crear un Producto
```dart
final response = await http.post(
  Uri.parse('http://localhost:8080/api/products/market/{marketId}'),
  headers: {'Content-Type': 'application/json'},
  body: jsonEncode({
    'name': 'Manzanas',
    'descripcion': 'Manzanas frescas del mercado',
    'fixedPrice': 5000,
    'isComposite': false
  }),
);
```

### Obtener Productos de un Mercado
```dart
final response = await http.get(
  Uri.parse('http://localhost:8080/api/products/market/{marketId}'),
);
```

### Eliminar un Producto
```dart
final response = await http.delete(
  Uri.parse('http://localhost:8080/api/products/{productId}/market/{marketId}'),
);
```

## Resolución de Problemas

### Error: "firebase-credentials.json no se encuentra"
**Solución**: Asegúrate de que el archivo está en `src/main/resources/firebase-credentials.json`

### Error: "Firestore not detected"
**Solución**: En Firebase Console, verifica que Firestore esté creado y activado

### Datos no persisten
**Solución**: Verifica que las reglas de seguridad de Firestore lo permitan

## Próximos Pasos

Una vez que Firebase esté funcionando:
1. Integrar autenticación con Firebase Auth
2. Agregar más funcionalidades (órdenes, reportes)
3. Optimizar consultas a Firestore
4. Implementar caching local
