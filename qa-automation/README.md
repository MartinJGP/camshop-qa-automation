# Suite de Pruebas Funcionales Automatizadas - CamShop (Selenium WebDriver)

Este directorio contiene el proyecto de automatización de pruebas funcionales para la plataforma de comercio electrónico **CamShop**, desarrollado en **Java 17**, **Selenium WebDriver 4.18**, **JUnit 5** y **WebDriverManager** bajo el patrón arquitectónico **Page Object Model (POM)**.

---

## Alcance de la Suite (Flujo Crítico E2E)

Valida el ciclo de vida completo de una transacción de compra:

$$\text{1. Login (JWT)} \longrightarrow \text{2. Catálogo (Selección)} \longrightarrow \text{3. Carrito (Verificación)} \longrightarrow \text{4. Checkout (Pago y Orden)}$$

### Casos Implementados:
- **`CP-FUNC-01`**: Flujo E2E Completo - Compra Exitosa (Happy Path).
- **`CP-FUNC-02`**: Autenticación con Credenciales Inválidas (Negative Path - Error 401).
- **`CP-FUNC-04`**: Protección de Ruta `/carrito` sin Sesión Activa (`authGuard`).

---

## Arquitectura de Código (Page Object Model)

```
qa-automation/
├── pom.xml
└── src/test/java/com/camshop/qa/
    ├── base/
    │   └── BaseTest.java             # Setup y Teardown de ChromeDriver, timeouts y opciones
    ├── pages/
    │   ├── LoginPage.java            # Abstracción de formulario /login y captura de JWT
    │   ├── CatalogoPage.java         # Grilla reactiva de cámaras y adición a carrito
    │   ├── CarritoPage.java          # Items, selector de método de pago y confirmación
    │   └── CheckoutPage.java         # Validación de orden generada y estado PAGADO
    └── tests/
        └── CompraEndToEndTest.java   # Casos de prueba formales con aserciones JUnit 5
```

---

## Requisitos Previos y Ejecución

1. **Servicios de CamShop Activos:**
   - Backend en ejecución: `http://localhost:8080`
   - Frontend en ejecución: `http://localhost:4200`
2. **Ejecutar las pruebas mediante Maven:**
   ```bash
   mvn clean test
   ```
3. **Ejecutar en Modo Headless (para integración continua CI/CD):**
   En `BaseTest.java`, descomentar:
   ```java
   options.addArguments("--headless=new");
   ```
