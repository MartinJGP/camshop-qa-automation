# 📌 EJEMPLOS POSTMAN - ENDPOINTS PRINCIPALES

---

## 🔐 AUTH CONTROLLER

### 1. **REGISTRO DE USUARIO**
Crea una nueva cuenta y obtiene token JWT para acceso.

```http
POST http://localhost:8080/auth/register
Content-Type: application/json

{
  "username": "juan.perez@email.com",
  "password": "SecurityPass123!"
}
```

**Respuesta (201):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqdWFuLnBlcmV6QGVtYWlsLmNvbSIsImlhdCI6MTc0NDM0MjI0NSwiZXhwIjoxNzQ0NDI4NjQ1fQ.abc123..."
}
```

---

### 2. **LOGIN DE USUARIO**
Inicia sesión con credenciales existentes y obtiene nuevo token.

```http
POST http://localhost:8080/auth/login
Content-Type: application/json

{
  "username": "juan.perez@email.com",
  "password": "SecurityPass123!"
}
```

**Respuesta (200):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqdWFuLnBlcmV6QGVtYWlsLmNvbSIsImlhdCI6MTc0NDM0MjI0NSwiZXhwIjoxNzQ0NDI4NjQ1fQ.xyz789..."
}
```

---

## 📷 CAMARA CONTROLLER

### 3. **CREAR CÁMARA**
Agrega un nuevo producto a la base de datos con stock inicial.

```http
POST http://localhost:8080/api/camaras
Content-Type: application/json
Authorization: Bearer {TOKEN}

{
  "nombre": "Cámara Domo 4K",
  "marca": "Hikvision",
  "modelo": "DS-2CD2143G2-I",
  "resolucion": "4K",
  "precio": 349.99,
  "stock": 15
}
```

**Respuesta (200):**
```json
{
  "id": 1,
  "nombre": "Cámara Domo 4K",
  "marca": "Hikvision",
  "modelo": "DS-2CD2143G2-I",
  "resolucion": "4K",
  "precio": 349.99,
  "stock": 15,
  "imagenes": []
}
```

---

### 4. **OBTENER CÁMARA POR ID**
Recupera los detalles completos de una cámara específica.

```http
GET http://localhost:8080/api/camaras/1
Authorization: Bearer {TOKEN}
```

**Respuesta (200):**
```json
{
  "id": 1,
  "nombre": "Cámara Domo 4K",
  "marca": "Hikvision",
  "modelo": "DS-2CD2143G2-I",
  "resolucion": "4K",
  "precio": 349.99,
  "stock": 15,
  "imagenes": []
}
```

---

## 🛒 CARRITO CONTROLLER

### 5. **AGREGAR PRODUCTO AL CARRITO**
Añade un producto al carrito del usuario autenticado. Si ya existe, incrementa la cantidad.

```http
POST http://localhost:8080/api/carrito/agregar?camaraId=1&cantidad=2
Authorization: Bearer {TOKEN}
```

**Respuesta (200):**
```json
{
  "id": 1,
  "usuario": {
    "id": 1,
    "username": "juan.perez@email.com"
  },
  "items": [
    {
      "id": 1,
      "camara": {
        "id": 1,
        "nombre": "Cámara Domo 4K",
        "precio": 349.99
      },
      "cantidad": 2,
      "precioUnitario": 349.99,
      "subtotal": 699.98
    }
  ],
  "total": 699.98
}
```

---

## 👥 CLIENTE CONTROLLER

### 6. **CREAR CLIENTE**
Registra los datos personales del cliente vinculado a un usuario.

```http
POST http://localhost:8080/clientes
Content-Type: application/json
Authorization: Bearer {TOKEN}

{
  "nombre": "Juan Pérez García",
  "email": "juan.perez@email.com",
  "direccion": "Calle Principal 123, Apartamento 4A",
  "telefono": "+34-555-0123",
  "user": {
    "id": 1
  }
}
```

**Respuesta (200):**
```json
{
  "id": 1,
  "nombre": "Juan Pérez García",
  "email": "juan.perez@email.com",
  "direccion": "Calle Principal 123, Apartamento 4A",
  "telefono": "+34-555-0123",
  "user": {
    "id": 1,
    "username": "juan.perez@email.com"
  }
}
```

---

### 7. **OBTENER CLIENTE POR ID**
Recupera la información completa de un cliente específico.

```http
GET http://localhost:8080/clientes/1
Authorization: Bearer {TOKEN}
```

**Respuesta (200):**
```json
{
  "id": 1,
  "nombre": "Juan Pérez García",
  "email": "juan.perez@email.com",
  "direccion": "Calle Principal 123, Apartamento 4A",
  "telefono": "+34-555-0123",
  "user": {
    "id": 1,
    "username": "juan.perez@email.com"
  }
}
```

---

## 💳 ORDEN CONTROLLER

### 8. **CHECKOUT - CREAR ORDEN Y PAGAR**
Convierte el carrito en una orden, genera el pago y marca como completada.

```http
POST http://localhost:8080/api/ordenes/checkout?metodo=tarjeta
Authorization: Bearer {TOKEN}
```

**Respuesta (200):**
```json
{
  "id": 1,
  "total": 699.98,
  "estado": "PAGADO",
  "user": {
    "id": 1,
    "username": "juan.perez@email.com"
  },
  "carrito": {
    "id": 1,
    "items": [
      {
        "id": 1,
        "cantidad": 2,
        "precioUnitario": 349.99,
        "subtotal": 699.98
      }
    ]
  }
}
```

---

## ⭐ RESENA CONTROLLER

### 9. **CREAR RESEÑA**
Deja una calificación y comentario sobre una cámara (máximo 5 estrellas).

```http
POST http://localhost:8080/api/resenas/camara/1
Content-Type: application/json
Authorization: Bearer {TOKEN}

{
  "calificacion": 5,
  "comentario": "Excelente cámara, muy buena calidad de imagen"
}
```

**Respuesta (200):**
```json
{
  "id": 1,
  "calificacion": 5,
  "comentario": "Excelente cámara, muy buena calidad de imagen",
  "fechaCreacion": "2026-05-28T14:30:45.123456",
  "camara": {
    "id": 1,
    "nombre": "Cámara Domo 4K"
  },
  "cliente": {
    "id": 1,
    "nombre": "Juan Pérez García"
  }
}
```

---

### 10. **OBTENER RESEÑAS POR CÁMARA**
Lista todas las calificaciones y comentarios de una cámara específica.

```http
GET http://localhost:8080/api/resenas/camara/1
Authorization: Bearer {TOKEN}
```

**Respuesta (200):**
```json
[
  {
    "id": 1,
    "calificacion": 5,
    "comentario": "Excelente cámara, muy buena calidad de imagen",
    "fechaCreacion": "2026-05-28T14:30:45.123456",
    "camara": {
      "id": 1,
      "nombre": "Cámara Domo 4K"
    },
    "cliente": {
      "id": 1,
      "nombre": "Juan Pérez García"
    }
  }
]
```

---

## 🖼️ CAMARA IMAGEN CONTROLLER

### 11. **AGREGAR IMAGEN A CÁMARA**
Sube una foto del producto asociada a una cámara. Puede marcarse como principal.

```http
POST http://localhost:8080/api/camaras/1/imagenes
Content-Type: application/json
Authorization: Bearer {TOKEN}

{
  "url": "https://cdn.example.com/hikvision-4k-principal.jpg",
  "numImagen": 1,
  "esPrincipal": true
}
```

**Respuesta (200):**
```json
{
  "id": 1,
  "url": "https://cdn.example.com/hikvision-4k-principal.jpg",
  "numImagen": 1,
  "esPrincipal": true,
  "camara": {
    "id": 1,
    "nombre": "Cámara Domo 4K"
  }
}
```

---

### 12. **LISTAR IMÁGENES DE UNA CÁMARA**
Obtiene todas las fotos asociadas a un producto ordenadas por número.

```http
GET http://localhost:8080/api/camaras/1/imagenes
Authorization: Bearer {TOKEN}
```

**Respuesta (200):**
```json
[
  {
    "id": 1,
    "url": "https://cdn.example.com/hikvision-4k-principal.jpg",
    "numImagen": 1,
    "esPrincipal": true,
    "camara": {
      "id": 1,
      "nombre": "Cámara Domo 4K"
    }
  },
  {
    "id": 2,
    "url": "https://cdn.example.com/hikvision-4k-detalle.jpg",
    "numImagen": 2,
    "esPrincipal": false,
    "camara": {
      "id": 1,
      "nombre": "Cámara Domo 4K"
    }
  }
]
```

---

## 🚀 FLUJO COMPLETO RECOMENDADO

Para probar toda la aplicación de forma integrada:

```
1. POST /auth/register          → Crear usuario y obtener TOKEN
2. POST /api/camaras            → Crear productos
3. POST /api/camaras/{id}/imágenes → Agregar fotos
4. POST /api/carrito/agregar    → Añadir al carrito
5. POST /clientes               → Crear perfil del cliente
6. POST /api/ordenes/checkout   → Realizar compra
7. POST /api/resenas/camara/{id} → Dejar reseña
8. GET /api/resenas/camara/{id}  → Ver reseñas
```

**Nota:** Reemplaza `{TOKEN}` con el token JWT obtenido en el registro/login.

