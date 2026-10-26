# CamShop - Plataforma E-Commerce y Suite de Aseguramiento de Calidad (QA)

[![Java 17](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot 3](https://img.shields.io/badge/Spring%20Boot-3.2-green.svg)](https://spring.io/projects/spring-boot)
[![Angular 20](https://img.shields.io/badge/Angular-20-red.svg)](https://angular.dev/)
[![Selenium WebDriver](https://img.shields.io/badge/Selenium-4.18-brightgreen.svg)](https://www.selenium.dev/)
[![Apache JMeter](https://img.shields.io/badge/Apache%20JMeter-5.6.3-red.svg)](https://jmeter.apache.org/)
[![OWASP ZAP](https://img.shields.io/badge/OWASP%20ZAP-2.15-blue.svg)](https://www.zaproxy.org/)
[![UTP](https://img.shields.io/badge/Universidad-UTP-red.svg)](https://www.utp.edu.pe/)

Repositorio oficial de código fuente y pruebas de software para la empresa **CamShop Seguridad & Tecnología S.A.C.** (RUC: 20609823411), desarrollado para la asignatura **Pruebas de Software (100000S13F)** en la **Universidad Tecnológica del Perú (UTP)**.

---

## Equipo de Calidad (QA) y Autores

- **Lujan Aderiano, Arnold Steven** — Código: `U18207053`
- **Gonzales Pocco, Jesus Martin** — Código: `U24308236`
- **Docente:** Ing. Juan Manuel Iquise Peralta

---

## Flujo Crítico Evaluado (End-to-End)

El alcance del proyecto concentra el 100% de la verificación en el flujo troncal transaccional del negocio:

$$\mathbf{Login \ (JWT)} \longrightarrow \mathbf{Catálogo} \longrightarrow \mathbf{Carrito} \longrightarrow \mathbf{Checkout \ Transaccional \ (PAGADO)}$$

1. **Autenticación:** `POST /auth/login` con validación BCrypt y generación de token Bearer JWT.
2. **Catálogo:** `GET /api/camaras` con renderizado reactivo de productos.
3. **Carrito de Compras:** `POST /api/carrito/agregar` con validación de stock y cálculo de totales.
4. **Checkout:** `POST /api/ordenes/checkout?metodo=tarjeta` bajo transacción atómica `@Transactional`, deducción de stock y liquidación de pedido.

---

## Estructura del Repositorio

```
camshop-qa-automation/
├── Backend/CamaraWebBackend/     # Microservicio REST en Spring Boot 3 + Spring Security 6 + JPA
├── Fronted/                      # Aplicación SPA en Angular 20 Standalone + Signals + Tailwind CSS
├── qa-automation/                # Suite de Pruebas Funcionales Automatizadas (Selenium WebDriver + POM)
│   ├── pom.xml                   # Dependencias Maven (Selenium 4.18, JUnit 5, WebDriverManager)
│   ├── README.md                 # Guía de ejecución con comandos Maven y modo Headless
│   └── src/test/java/com/camshop/qa/
│       ├── base/BaseTest.java    # Configuración de WebDriver, esperas y timeouts
│       ├── pages/                # Page Object Model (LoginPage, CatalogoPage, CarritoPage, CheckoutPage)
│       └── tests/CompraEndToEndTest.java # Casos de prueba automatizados E2E
├── qa-performance/               # Pruebas de Carga y Estrés (Apache JMeter)
│   ├── CamShop_Plan_Rendimiento.jmx  # Plan de pruebas con los 4 escenarios de carga (E1..E4)
│   ├── cuentas_usuarios.csv      # Dataset de usuarios concurrentes
│   └── README_RENDIMIENTO.md     # Guía de ejecución Non-GUI y análisis de la Ley de Little
├── qa-security/                  # Auditoría de Seguridad Dinámica DAST (OWASP ZAP)
│   ├── CamShop_ZAP_Context.context   # Contexto de escaneo y reglas OWASP Top 10
│   ├── OWASP_ZAP_Report_CamShop.html # Reporte visual de vulnerabilidades y mitigación
│   └── README_SEGURIDAD.md       # Guía de escaneo activo/pasivo
├── .gitignore                    # Exclusión de dependencias, temporales y documentos locales
└── README.md                     # Documentación principal del repositorio
```

> **Nota sobre los informes académicos:** Conforme a los lineamientos de entrega de la UTP, los documentos formales en Word y PDF (APF1 y APF2) y las diapositivas de exposición se entregan directamente a través de la plataforma virtual Canvas.

---

## Despliegue del Sistema Bajo Prueba (SUT)

### 1. Iniciar el Backend (Spring Boot 3)
```bash
cd Backend/CamaraWebBackend
./mvnw spring-boot:run
```
*El servicio REST iniciará en `http://localhost:8080` (Base de datos H2 en memoria).*

### 2. Iniciar el Frontend (Angular 20)
```bash
cd Fronted
npm install
npm start
```
*La aplicación web estará disponible en `http://localhost:4200`.*

---

## Ejecución de Pruebas

### 1. Pruebas Funcionales Automatizadas (Selenium WebDriver)
```bash
cd qa-automation
mvn clean test
```
*Para ejecutar en modo headless (integración continua), descomentar `--headless=new` en `BaseTest.java`.*

### 2. Pruebas de Rendimiento (Apache JMeter)
Ejecutar el plan en modo Non-GUI con 100 usuarios concurrentes (Validación de SLA):
```bash
cd qa-performance
jmeter -n -t CamShop_Plan_Rendimiento.jmx -Jusers=100 -Jrampup=30 -Jduration=300 -l resultados_E3.jtl -e -o reporte_html_E3/
```
*SLA Cumplido: $p95 = 245\text{ ms}$ (umbral máximo permitido $< 500\text{ ms}$) y tasa de error del $0.02\%$.*

#### Modelo de Capacidad - Ley de Little:
$$L = \lambda \times W = 385.2\text{ req/s} \times 0.0648\text{ s} = 24.96\text{ peticiones simultáneas}$$
*Ocupación promedio de 25 hilos de un total de 200 hilos disponibles en Tomcat ($12.5\%$).*

### 3. Pruebas de Seguridad DAST (OWASP ZAP)
Importar el archivo `qa-security/CamShop_ZAP_Context.context` en OWASP ZAP 2.15 o ejecutar mediante contenedor:
```bash
cd qa-security
docker run -v $(pwd):/zap/wrk/:rw -t ghcr.io/zaproxy/zaproxy:stable zap-full-scan.py \
    -t http://host.docker.internal:8080 \
    -c CamShop_ZAP_Context.context \
    -r OWASP_ZAP_Report_CamShop.html
```
