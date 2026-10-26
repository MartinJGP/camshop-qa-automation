# Pruebas de Seguridad DAST - CamShop (OWASP ZAP)

Este directorio contiene las políticas de escaneo, archivos de contexto y reportes de la auditoría de seguridad dinámica (DAST) ejecutada sobre el sistema CamShop contra el estándar internacional **OWASP Top 10**.

---

## Archivos Incluidos

- `CamShop_ZAP_Context.context`: Archivo de contexto formal para OWASP ZAP 2.17.0 que parametriza el alcance (*in-scope*), exclusiones y la inyección automatizada del token Bearer JWT.
- `OWASP_ZAP_Report_Local_Martin.html`: Reporte oficial de auditoría DAST generado con OWASP ZAP 2.17.0 sobre los servicios locales desplegados (1,468 peticiones de ataque sobre 38 endpoints).
- `OWASP_ZAP_Report_CamShop.html`: Copia sincronizada del reporte oficial para compatibilidad con las especificaciones del avance.
- `2026-10-26-ZAP-Report-localhost2/`: Directorio de recursos con las hojas de estilo (CSS) e iconos de OWASP ZAP necesarios para la visualización del reporte.

---

## Categorías Evaluadas del OWASP Top 10

1. **A01:2021 - Broken Access Control:**
   - Verificación de autorización de roles en endpoints administrativos.
   - Evaluación de Insecure Direct Object Reference (IDOR) en pedidos de compra (`/api/ordenes/**`).
2. **A02:2021 - Cryptographic Failures:**
   - Verificación de almacenamiento con hash seguro BCrypt (10 rondas de salting).
   - Firma criptográfica de tokens JWT con algoritmo HMAC-SHA256 y clave de 256 bits.
3. **A03:2021 - Injection:**
   - Auditoría de inyección SQL (SQLi) mediante parámetros de búsqueda en catálogo y login, validando sanitización con Spring Data JPA.
4. **A05:2021 - Security Misconfiguration:**
   - Detección y remediación de cabeceras de endurecimiento: Content-Security-Policy (CSP), X-Frame-Options (Clickjacking) y X-Content-Type-Options (MIME-sniffing).
5. **A07:2021 - Identification and Authentication Failures:**
   - Mitigación de ataques de fuerza bruta mediante política de contraseñas robustas y limitación de tasa (Rate Limiting).

---

## Instrucciones de Ejecución con OWASP ZAP

### Desde Interfaz Gráfica (GUI):
1. Abrir OWASP ZAP.
2. Ir a `File` -> `Import Context` y seleccionar `CamShop_ZAP_Context.context`.
3. Seleccionar el contexto importado y ejecutar `Automated Scan` sobre `http://localhost:8080`.
4. Para auditar la SPA Angular 20, ejecutar el **AJAX Spider** sobre `http://localhost:4200`.

### Desde Docker / Modo Headless:
```bash
docker run -v $(pwd):/zap/wrk/:rw -t ghcr.io/zaproxy/zaproxy:stable zap-full-scan.py \
    -t http://host.docker.internal:8080 \
    -c CamShop_ZAP_Context.context \
    -r OWASP_ZAP_Report_CamShop.html
```
