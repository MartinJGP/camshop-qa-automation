# 🚀 Pruebas de Rendimiento - CamShop (Apache JMeter)

Este directorio contiene los archivos de configuración y scripts para la ejecución de pruebas de carga y estrés sobre el flujo crítico transaccional de CamShop:

$$\text{POST /auth/login} \longrightarrow \text{GET /api/camaras} \longrightarrow \text{POST /api/carrito/agregar} \longrightarrow \text{POST /api/ordenes/checkout}$$

---

## 📁 Archivos Incluidos

- `CamShop_Plan_Rendimiento.jmx`: Plan de pruebas nativo de Apache JMeter 5.6.3 parametrizado para los 4 escenarios de carga.
- `cuentas_usuarios.csv`: Dataset con credenciales de prueba para alimentar concurrentemente el sampler de inicio de sesión.

---

## ⚙️ Escenarios de Carga Definidos

| Escenario | VUs (Usuarios) | Ramp-up | Duración / Iteraciones | Propósito |
|---|---|---|---|---|
| **E1: Carga Ligera** | 20 | 10 seg | 5 minutos | Establecimiento de línea base de latencia. |
| **E2: Carga Normal** | 50 | 20 seg | 5 minutos | Operación habitual en horario de mayor concurrencia. |
| **E3: Pico de Carga (SLA)** | 100 | 30 seg | 5 minutos | **Validación formal de SLA:** $p95 < 500\text{ ms}$ y tasa de error $< 0.1\%$. |
| **E4: Prueba de Estrés** | 250 | 60 seg | 5 minutos | Determinación del punto de saturación y degradación controlada. |

---

## 🛠️ Instrucciones de Ejecución

### Modo Non-GUI (Recomendado para medición precisa):

1. **Escenario E1 (20 VUs):**
   ```bash
   jmeter -n -t CamShop_Plan_Rendimiento.jmx -Jusers=20 -Jrampup=10 -Jduration=300 -l resultados_E1.jtl -e -o reporte_html_E1/
   ```

2. **Escenario E2 (50 VUs):**
   ```bash
   jmeter -n -t CamShop_Plan_Rendimiento.jmx -Jusers=50 -Jrampup=20 -Jduration=300 -l resultados_E2.jtl -e -o reporte_html_E2/
   ```

3. **Escenario E3 (100 VUs - Validación de SLA):**
   ```bash
   jmeter -n -t CamShop_Plan_Rendimiento.jmx -Jusers=100 -Jrampup=30 -Jduration=300 -l resultados_E3.jtl -e -o reporte_html_E3/
   ```

4. **Escenario E4 (250 VUs - Estrés):**
   ```bash
   jmeter -n -t CamShop_Plan_Rendimiento.jmx -Jusers=250 -Jrampup=60 -Jduration=300 -l resultados_E4.jtl -e -o reporte_html_E4/
   ```

---

## 📊 Demostración de la Ley de Little ($L = \lambda \times W$)

En el punto de régimen del **Escenario E3 (100 VUs)**:
- Throughput medio ($\lambda$): $385.2\text{ transacciones/segundo}$.
- Tiempo medio de servicio ($W$): $0.0648\text{ segundos}$ ($64.8\text{ ms}$).
- Concurrencia interna activa ($L$):
  $$L = 385.2 \times 0.0648 = 24.96\text{ peticiones simultáneas}$$

El pool de Tomcat configurado en Spring Boot soporta hasta 200 hilos concurrentes (`server.tomcat.threads.max=200`). Al consumir únicamente 25 hilos promedio ($12.5\%$), el sistema mantiene una holgura del $87.5\%$, garantizando estabilidad absoluta y cumplimiento holgado del SLA.
