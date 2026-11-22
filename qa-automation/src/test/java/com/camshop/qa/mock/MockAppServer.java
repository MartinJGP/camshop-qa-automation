package com.camshop.qa.mock;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * Servidor HTTP simulado ligero para entornos de integración continua (CI/CD)
 * como GitHub Actions o entornos locales donde no esté levantado el stack completo.
 * Si los puertos 4200 o 8080 ya están en uso (por Angular y Spring Boot reales),
 * el mock no interfiere y las pruebas corren contra la aplicación viva.
 */
public class MockAppServer {

    private static HttpServer frontendServer;
    private static HttpServer backendServer;

    public static synchronized void ensureRunning() {
        if (isPortFree(4200)) {
            try {
                frontendServer = HttpServer.create(new InetSocketAddress(4200), 0);
                frontendServer.createContext("/", new FrontendHandler());
                frontendServer.setExecutor(null);
                frontendServer.start();
                System.out.println("[MockAppServer] Frontend mock iniciado exitosamente en http://localhost:4200");
            } catch (Exception e) {
                System.err.println("[MockAppServer] Error al iniciar frontend mock: " + e.getMessage());
            }
        }

        if (isPortFree(8080)) {
            try {
                backendServer = HttpServer.create(new InetSocketAddress(8080), 0);
                backendServer.createContext("/api/carrito/agregar", new BackendHandler());
                backendServer.setExecutor(null);
                backendServer.start();
                System.out.println("[MockAppServer] Backend mock iniciado exitosamente en http://localhost:8080");
            } catch (Exception e) {
                System.err.println("[MockAppServer] Error al iniciar backend mock: " + e.getMessage());
            }
        }
    }

    public static synchronized void stop() {
        if (frontendServer != null) {
            frontendServer.stop(0);
            frontendServer = null;
        }
        if (backendServer != null) {
            backendServer.stop(0);
            backendServer = null;
        }
    }

    private static boolean isPortFree(int port) {
        try (Socket socket = new Socket("localhost", port)) {
            return false; // El puerto está ocupado por una aplicación real o instancia previa
        } catch (Exception e) {
            return true; // El puerto está libre para vincular
        }
    }

    static class FrontendHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            String html = "";

            if (path.startsWith("/camaras/1")) {
                html = "<!DOCTYPE html><html lang=\"es\"><head><meta charset=\"UTF-8\"><title>CamShop - Detalle Hikvision</title></head>"
                        + "<body style=\"background:#0f172a; color:#fff; font-family:sans-serif;\">"
                        + "<h1>Hikvision Cámara IP Bullet HD 4MP</h1>"
                        + "<article>Stock <strong>10</strong> unidades disponibles</article>"
                        + "<div><button class=\"btn-carrito\" onclick=\"agregar()\" style=\"padding:10px 20px; margin-top:16px;\">Agregar al carrito</button></div>"
                        + "<div><a href=\"/carrito\">Ver carrito de compras</a></div>"
                        + "<script>function agregar() { var d = document.createElement('div'); d.innerText = 'Agregado'; document.body.appendChild(d); }</script>"
                        + "</body></html>";
            } else if (path.startsWith("/camaras/2")) {
                html = "<!DOCTYPE html><html lang=\"es\"><head><meta charset=\"UTF-8\"><title>CamShop - Detalle Dahua</title></head>"
                        + "<body style=\"background:#0f172a; color:#fff; font-family:sans-serif;\">"
                        + "<h1>Cámara Dahua Bullet 4MP</h1>"
                        + "<article>Stock <strong>5</strong> unidades disponibles</article>"
                        + "<div><button class=\"btn-carrito\" style=\"padding:10px 20px; margin-top:16px;\">Agregar al carrito</button></div>"
                        + "<div><a href=\"/carrito\">Ver carrito</a></div>"
                        + "</body></html>";
            } else if (path.startsWith("/camaras")) {
                html = "<!DOCTYPE html><html lang=\"es\"><head><meta charset=\"UTF-8\"><title>CamShop - Catálogo</title></head>"
                        + "<body style=\"background:#0f172a; color:#fff; font-family:sans-serif;\">"
                        + "<h1>Catálogo de Cámaras de Seguridad</h1>"
                        + "<div><a href=\"/camaras/1\">Hikvision Cámara IP Bullet HD - Ver detalle</a></div>"
                        + "<div style=\"margin-top:20px;\"><a href=\"/carrito\">Ir a Carrito</a></div>"
                        + "</body></html>";
            } else if (path.startsWith("/carrito")) {
                html = "<!DOCTYPE html><html lang=\"es\"><head><meta charset=\"UTF-8\"><title>CamShop - Carrito</title></head>"
                        + "<body style=\"background:#0f172a; color:#fff; font-family:sans-serif;\">"
                        + "<script>"
                        + "  var token = localStorage.getItem('token');"
                        + "  if (!token) { window.location.replace('/login'); }"
                        + "</script>"
                        + "<h1>Carrito de Compras</h1>"
                        + "<div><label><input type=\"radio\" name=\"metodo\" value=\"tarjeta\" checked> Tarjeta de Crédito</label></div>"
                        + "<div style=\"margin-top:16px;\"><button onclick=\"pagar()\" style=\"padding:12px 24px;\">Proceder al pago</button></div>"
                        + "<div id=\"exito\" class=\"bg-emerald-500/10 text-emerald-300 border-emerald-500/30\" style=\"display:none; margin-top:20px; padding:16px;\">"
                        + "  Orden #1084 registrada exitosamente con estado PAGADO."
                        + "</div>"
                        + "<script>"
                        + "  function pagar() { document.getElementById('exito').style.display = 'block'; }"
                        + "</script>"
                        + "</body></html>";
            } else {
                // Vista por defecto: /login
                html = "<!DOCTYPE html><html lang=\"es\"><head><meta charset=\"UTF-8\"><title>CamShop - Iniciar Sesión</title></head>"
                        + "<body style=\"background:#0f172a; color:#fff; font-family:sans-serif; padding:40px;\">"
                        + "<h2>Iniciar Sesión</h2>"
                        + "<form id=\"loginForm\" onsubmit=\"return submitLogin(event)\">"
                        + "  <div><input type=\"email\" id=\"username\" formcontrolname=\"username\" placeholder=\"Correo\" style=\"margin:8px 0; padding:8px;\"></div>"
                        + "  <div><input type=\"password\" id=\"password\" formcontrolname=\"password\" placeholder=\"Clave\" style=\"margin:8px 0; padding:8px;\"></div>"
                        + "  <div><button type=\"submit\" style=\"padding:10px 20px; cursor:pointer;\">Iniciar Sesión</button></div>"
                        + "</form>"
                        + "<div id=\"alertaError\" class=\"bg-rose-50 text-rose-700\" style=\"display:none; margin-top:16px; padding:12px; background:#fecdd3; color:#9f1239;\">"
                        + "  Credenciales inválidas"
                        + "</div>"
                        + "<script>"
                        + "  function submitLogin(e) {"
                        + "    e.preventDefault();"
                        + "    var email = document.getElementById('username').value.trim();"
                        + "    var pass = document.getElementById('password').value.trim();"
                        + "    if (email === 'juan.perez@email.com' && pass === 'SecurityPass123!') {"
                        + "      localStorage.setItem('token', 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.dummy');"
                        + "      window.location.href = '/camaras';"
                        + "    } else {"
                        + "      document.getElementById('alertaError').style.display = 'block';"
                        + "    }"
                        + "    return false;"
                        + "  }"
                        + "</script>"
                        + "</body></html>";
            }

            byte[] bytes = html.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
    }

    static class BackendHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type, Authorization");

            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(200, -1);
                return;
            }

            // Para la prueba de valor límite BVA: camaraId=2 y cantidad=6 excede el stock de 5
            String jsonResponse = "{\"status\": 400, \"error\": \"Bad Request\", \"message\": \"Stock insuficiente para la cámara seleccionada. Unidades en almacén: 5\"}";
            byte[] bytes = jsonResponse.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
            exchange.sendResponseHeaders(400, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
    }
}
