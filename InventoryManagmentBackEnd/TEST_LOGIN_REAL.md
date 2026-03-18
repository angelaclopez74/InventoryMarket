# Ejemplos de Prueba - Login Real

## 1. Iniciar el Servidor

```bash
# Opción 1: Con Maven (desarrollo)
cd InventoryManagmentBackEnd
mvn spring-boot:run

# Opción 2: Con JAR (producción)
java -jar target/inventory-management-backend-0.0.1-SNAPSHOT.jar
```

El servidor estará disponible en: `http://localhost:8080`

---

## 2. Cargar Usuarios de Ejemplo en Firestore

Primero, debes **poblar la base de datos** con usuarios de prueba:

```bash
curl -X POST http://localhost:8080/api/system/seed \
  -H "Content-Type: application/json"
```

**Respuesta esperada:**
```json
{
  "seededUsers": ["adan", "juan", "maria"],
  "seededMarkets": ["market_bogota", "market_cali"],
  "timestamp": 1710731500000
}
```

---

## 3. Probar Login con Credenciales Válidas

### ✅ Login Exitoso - Usuario "adan"

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "adan",
    "password": "123"
  }'
```

**Respuesta (200 OK):**
```json
{
  "ok": true,
  "username": "adan",
  "email": "adan@example.com",
  "token": "a1b2c3d4-e5f6-47g8-h9i0-j1k2l3m4n5o6"
}
```

### ✅ Login Exitoso - Usuario "juan"

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "juan",
    "password": "456"
  }'
```

**Respuesta (200 OK):**
```json
{
  "ok": true,
  "username": "juan",
  "email": "juan@example.com",
  "token": "x1y2z3a4-b5c6-47d8-e9f0-g1h2i3j4k5l6"
}
```

### ✅ Login Exitoso - Usuario "maria"

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "maria",
    "password": "789"
  }'
```

---

## 4. Probar Login con Credenciales Inválidas

### ❌ Usuario No Existe

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "usuarioNoExiste",
    "password": "123"
  }'
```

**Respuesta (401 Unauthorized):**
```json
{
  "ok": false,
  "message": "Usuario no encontrado"
}
```

### ❌ Contraseña Incorrecta

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "adan",
    "password": "contraseñaIncorrecta"
  }'
```

**Respuesta (401 Unauthorized):**
```json
{
  "ok": false,
  "message": "Contraseña incorrecta"
}
```

### ❌ Missing Required Fields

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "adan"
  }'
```

**Respuesta (400 Bad Request):**
```html
<!-- Respuesta vacía, solo código HTTP 400 -->
```

---

## 5. Verificar en Firestore Console

1. Ve a [Firebase Console](https://console.firebase.google.com)
2. Selecciona tu proyecto: `inventorymanagment-131df`
3. En el menú lateral, ve a **Firestore Database**
4. Verifica que existe la colección `users` con documentos:
   - `adan` 
   - `juan`
   - `maria`

Cada documento debe tener:
```json
{
  "id": "uuid-aquí",
  "userName": "adan",
  "password": "123",
  "email": "adan@example.com",
  "createdDate": 1710731500000
}
```

---

## 6. Flujo Completo - Frontend + Backend

### En tu Flutter App:

1. **Navegar a LoginScreen**
   - Ya está preconfigurada en `lib/src/screens/login_screen.dart`
   - Valores por defecto: usuario="adan", contraseña="123"

2. **Ingresar credenciales** (o usar valores por defecto)
   - Username: `adan`
   - Password: `123`

3. **Presionar "Entrar"**
   - La app hace POST a `http://localhost:8080/api/auth/login`
   - Recibe token del servidor
   - Guarda token en `Session.instance`

4. **Redirige al Dashboard**
   - Muestra `Bienvenido, adan`
   - Puede navegar a Mercados, Ver Productos, etc.

5. **Logout**
   - Presionar el botón de logout (icono en la esquina)
   - Limpia la sesión
   - Regresa a LoginScreen

---

## 7. Probar con Postman o Insomnia

### Importar en Postman:

**Crear Nueva Request:**
- Method: `POST`
- URL: `http://localhost:8080/api/auth/login`
- Headers: `Content-Type: application/json`
- Body (raw JSON):
```json
{
  "username": "adan",
  "password": "123"
}
```

---

## 8. Solucionar Problemas

### Error: "Error haciendo seed" en Flutter

**Causa:** El backend no está corriendo
**Solución:** Inicia el servidor con `mvn spring-boot:run`

### Error: "No se pudo conectar al backend"

**Causa:** La URL es incorrecta o el servidor no responde
**Solución:** Verifica:
- Backend corriendo en `http://localhost:8080`
- En Android emulador: usar `http://10.0.2.2:8080` en lugar de `localhost`

### Credenciales siguen rechazándose

**Cause:** Los usuarios no fueron cargados
**Solución:** 
1. Llamar al endpoint `/api/system/seed`
2. Verificar en Firestore que exista la colección `users`

### Tokens no se están usando (avanzado)

El token se guarda pero **actualmente no se valida en requests subsecuentes**.
Para implementarlo:
1. Agregar un middleware de autenticación en el backend
2. Validar el token en cada request que lo requiera
3. Usar el header `Authorization: Bearer {token}` en las requests del frontend

---

## Resumen de Cambios

| Elemento | Antes | Ahora |
|----------|-------|-------|
| **Login** | Hardcoded ("adan"/"123") | Valida contra Firestore ✅ |
| **Usuarios** | 1 usuario fijo | 3 usuarios creados por seed |
| **Validación** | String equals | Consulta a Firestore en colección `users` |
| **Respuesta Error** | "Credenciales inválidas" | "Usuario no encontrado" o "Contraseña incorrecta" |
| **Base de datos** | No se usaba | Ahora persiste usuarios en Firestore |

¡Tu sistema de login ahora es real! 🎉
