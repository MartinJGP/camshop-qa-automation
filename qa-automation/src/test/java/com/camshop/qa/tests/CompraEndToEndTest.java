package com.camshop.qa.tests;

import com.camshop.qa.base.BaseTest;
import com.camshop.qa.pages.CarritoPage;
import com.camshop.qa.pages.CatalogoPage;
import com.camshop.qa.pages.CheckoutPage;
import com.camshop.qa.pages.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Suite de Pruebas Funcionales Automatizadas End-to-End para CamShop.
 * Valida el flujo crítico de compra: Login -> Catálogo -> Carrito -> Checkout.
 * Incluye pausas estratégicas para visualización en tiempo real durante
 * presentaciones.
 */
public class CompraEndToEndTest extends BaseTest {

    @Test
    @DisplayName("CP-FUNC-01: Flujo E2E Completo - Compra Exitosa (Happy Path)")
    public void testCompraEndToEndExitosa() {
        // 1. Iniciar sesión con usuario válido
        LoginPage loginPage = new LoginPage(driver);
        loginPage.navegar(BASE_URL);
        CatalogoPage catalogo = loginPage.iniciarSesionExitoso("juan.perez@email.com", "SecurityPass123!");

        // 2. Verificar navegación a catálogo y seleccionar cámara
        assertTrue(catalogo.estaEnCatalogo(), "El usuario autenticado debe ser redirigido a /camaras");
        catalogo.agregarPrimeraCamara();

        // 3. Ir a carrito y procesar pago simulado con tarjeta
        CarritoPage carrito = catalogo.irAlCarrito();
        assertTrue(carrito.estaEnCarrito(), "Debe acceder a la vista protegida /carrito");
        carrito.seleccionarMetodoTarjeta();

        // 4. Validar confirmación de la orden y estado PAGADO
        CheckoutPage checkout = carrito.procesarPago();
        assertTrue(checkout.esPagoExitoso(), "La orden debe registrarse exitosamente con estado PAGADO");

        // Pausa final de 3 segundos para que el jurado/docente aprecie la orden
        // generada y el estado PAGADO
        pausar(3000);
    }

    @Test
    @DisplayName("CP-FUNC-02: Autenticación con Credenciales Inválidas (Negative Path)")
    public void testLoginConCredencialesInvalidas() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.navegar(BASE_URL);
        loginPage.iniciarSesionInvalido("usuario.falso@email.com", "ClaveInexistente2026");

        assertTrue(loginPage.esMensajeErrorVisible(), "Debe mostrarse alerta visual de error de autenticación");

        // Pausa para apreciar la notificación de error en pantalla
        pausar(2500);
    }

    @Test
    @DisplayName("CP-FUNC-03: Control de Existencias en Carrito - Validacion de Limite de Stock BVA")
    public void testValidacionLimiteSuperiorStockEnCarrito() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.navegar(BASE_URL);
        CatalogoPage catalogo = loginPage.iniciarSesionExitoso("juan.perez@email.com", "SecurityPass123!");
        assertTrue(catalogo.estaEnCatalogo(), "El usuario autenticado debe visualizar el catalogo");

        // 1. Navegar a la camara con inventario acotado a 5 unidades: ID 2 Dahua
        catalogo.navegarADetalleCamaraPorId(2);

        // 2. Verificar que la ficha del producto indica 5 unidades en existencias
        String textoStock = catalogo.obtenerTextoStock();
        assertTrue(textoStock.contains("5"), "La ficha tecnica debe reflejar 5 unidades disponibles");

        // 3. Intentar agregar 6 unidades al carrito superando el limite disponible
        int codigoRespuesta = catalogo.intentarAgregarCantidadExcedente(2, 6);

        // 4. Validar rechazo del backend con codigo HTTP 400 Bad Request
        assertEquals(400, codigoRespuesta,
                "El sistema debe responder HTTP 400 Bad Request por stock insuficiente al solicitar 6 unidades con stock 5");

        // Pausa para apreciar la notificacion visual de stock insuficiente en pantalla
        pausar(2500);
    }

    @Test
    @DisplayName("CP-FUNC-04: Protección de Ruta /carrito sin Autenticación (authGuard)")
    public void testAccesoDirectoACarritoSinLoginRedirigeALogin() {
        // Navegar directamente a /carrito sin token previo en sesión
        driver.get(BASE_URL + "/carrito");

        // Validar que la guarda de Angular redirige a /login
        assertTrue(driver.getCurrentUrl().contains("/login"),
                "El authGuard debe rechazar el acceso anónimo y redirigir a /login");

        // Pausa para apreciar la redirección automática a /login
        pausar(2500);
    }
}
