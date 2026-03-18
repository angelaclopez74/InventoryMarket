# Login Real con Firebase - Guía de Implementación

## Cambios Realizados

He implementado un **login real** que verifica credenciales contra la base de datos Firebase (Firestore) en lugar de usar valores hardcodeados.

### 1. Crear Repositorio de Usuarios (`UserRepository.java`)

**Ubicación:** `src/main/java/.../repository/UserRepository.java`

Este repositorio maneja todas las operaciones de lectura/escritura de usuarios en Firestore:

- `findByUsername(username)` - Busca un usuario por su nombre de usuario
- `save(user)` - Guarda un nuevo usuario
- `update(username, user)` - Actualiza un usuario existente
- `delete(username)` - Elimina un usuario
- `exists(username)` - Verifica si un usuario existe

**Colección en Firestore:** `users`

Cada documento representa un usuario con su nombre de usuario como ID de documento:
```
Collection: users
├── adan (document)
│   ├── id: uuid
│   ├── userName: "adan"
│   ├── password: "123"
│   ├── email: "adan@example.com"
│   └── createdDate: timestamp
├── juan (document)
│   └── ...
└── maria (document)
    └── ...
```

### 2. Crear Servicio de Autenticación (`AuthenticationService.java`)

**Ubicación:** `src/main/java/.../service/AuthenticationService.java`

Contiene la **lógica de autenticación**:

- `authenticate(username, password)` - Valida credenciales contra Firestore
  - Busca el usuario en la base de datos
  - Verifica que la contraseña coincida
  - Lanza `AuthenticationException` si falla

- `createUser(username, password, email)` - Crea un nuevo usuario
  - Valida que el usuario no exista
  - Genera un ID único (UUID)

### 3. Modificar Controlador de Autenticación (`AuthController.java`)

**Cambio principal:** El `/api/auth/login` ahora:

**Antes (hardcodeado):**
```java
boolean ok = "adan".equals(request.username()) && "123".equals(request.password());
```

**Ahora (verifica contra Firestore):**
```java
User authenticatedUser = authenticationService.authenticate(
    request.username(),
    request.password()
);
```

- Valida credenciales contra la base de datos
- Retorna información del usuario (username, email)
- Genera un token único para la sesión

**Formatos de respuesta:**

✅ Login exitoso:
```json
{
  "ok": true,
  "username": "adan",
  "email": "adan@example.com",
  "token": "uuid-token"
}
```

❌ Credenciales inválidas:
```json
{
  "ok": false,
  "message": "Usuario no encontrado" o "Contraseña incorrecta"
}
```

### 4. Actualizar Servicio de Inventario (`InventoryManagementService.java`)

El método `seedExampleData()` ahora crea usuarios de ejemplo en Firestore:

```java
User user1 = new User("adan", "123", "adan@example.com");
User user2 = new User("juan", "456", "juan@example.com");
User user3 = new User("maria", "789", "maria@example.com");
```

**Usuarios de prueba disponibles:**

| Usuario | Contraseña | Email |
|---------|-----------|-------|
| adan | 123 | adan@example.com |
| juan | 456 | juan@example.com |
| maria | 789 | maria@example.com |

## Cómo Usar

### 1. Ejecutar el Backend

```bash
cd InventoryManagmentBackEnd
mvn spring-boot:run
```

### 2. Cargar Datos de Ejemplo (IMPORTANTE)

Primero, llama al endpoint de seed para crear los usuarios en Firebase:

```bash
curl -X POST http://localhost:8080/api/system/seed
```

Respuesta esperada:
```json
{
  "seededUsers": ["adan", "juan", "maria"],
  "seededMarkets": ["market_bogota", "market_cali"],
  "timestamp": 1234567890
}
```

### 3. Probar el Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "adan", "password": "123"}'
```

Respuesta:
```json
{
  "ok": true,
  "username": "adan",
  "email": "adan@example.com",
  "token": "some-uuid-token"
}
```

### 4. El Frontend ya está configurado

Tu Flutter app (`LoginScreen`) ya hace llamadas correctas al endpoint:

- Envía username y password al `/api/auth/login`
- Guarda el token y username en la sesión
- Redirige al dashboard si loginexitoso

## Mejoras Futuras (Recomendadas)

1. **Encriptación de contraseñas**: Usar bcrypt para hashear contraseñas
   ```java
   import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
   ```

2. **JWT Tokens**: Reemplazar tokens UUID por JWT con expiración
   ```java
   import io.jsonwebtoken.Jwts;
   ```

3. **Validación en frontend**: Usar el token para autorizar requests subsecuentes
   ```dart
   headers: {
     'Authorization': 'Bearer $token',
     'Content-Type': 'application/json'
   }
   ```

4. **Manejo de sesiones**: Validar token en cada request (Authorization header)

## Estructura de Archivos Creados/Modificados

```
InventoryManagmentBackEnd/
├── src/main/java/.../
│   ├── controller/
│   │   └── AuthController.java (MODIFICADO)
│   ├── service/
│   │   ├── AuthenticationService.java (NUEVO)
│   │   └── InventoryManagementService.java (MODIFICADO)
│   └── repository/
│       └── UserRepository.java (NUEVO)
└── pom.xml (no cambios - Firebase ya está configurado)
```

## Resumen

✅ El login ahora es **real** - verifica contra Firestore
✅ Las contraseñas se validan contra la base de datos
✅ Los usuarios se crean automáticamente al hacer seed
✅ Tu Flutter app no necesita cambios - todo funciona igual

¡El sistema está listo para usar!
