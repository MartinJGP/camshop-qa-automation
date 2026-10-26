package com.camshop.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object Model para el catálogo de cámaras (/camaras).
 * Soporta navegación fluida hacia la vista de detalle y pausas didácticas para demo.
 */
public class CatalogoPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By linkCamaraConStock = By.xpath("//a[contains(@href,'/camaras/1') or contains(.,'Hikvision')]");
    private final By selectorBotonCarrito = By.xpath("//button[contains(@class,'btn-carrito') or contains(.,'Agregar al carrito')]");

    public CatalogoPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    private void pausaBreve(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
        }
    }

    public boolean estaEnCatalogo() {
        return driver.getCurrentUrl().contains("/camaras");
    }

    public CatalogoPage agregarPrimeraCamara() {
        // En catálogo: clic en 'Ver detalle' de la cámara con stock disponible (ID 1)
        WebElement detalleLink = wait.until(ExpectedConditions.elementToBeClickable(linkCamaraConStock));
        pausaBreve(1200); // Pausa para apreciar el catálogo
        detalleLink.click();

        // En vista de detalle (/camaras/:id): clic en 'Agregar al carrito'
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(selectorBotonCarrito));
        pausaBreve(1500); // Pausa para apreciar la ficha técnica del producto
        btn.click();

        pausaBreve(1500); // Pausa para apreciar la alerta reactiva de éxito

        return this;
    }

    public CarritoPage irAlCarrito() {
        String currentUrl = driver.getCurrentUrl();
        String origin = currentUrl.contains("/camaras") ? currentUrl.substring(0, currentUrl.indexOf("/camaras")) : "http://localhost:4200";
        driver.get(origin + "/carrito");
        wait.until(ExpectedConditions.urlContains("/carrito"));
        pausaBreve(1200); // Pausa al cargar la vista del carrito
        return new CarritoPage(driver);
    }

    public void navegarADetalleCamaraPorId(int id) {
        String currentUrl = driver.getCurrentUrl();
        String origin = currentUrl.contains("/camaras") ? currentUrl.substring(0, currentUrl.indexOf("/camaras")) : "http://localhost:4200";
        driver.get(origin + "/camaras/" + id);
        wait.until(ExpectedConditions.urlContains("/camaras/" + id));
        pausaBreve(1500);
    }

    public String obtenerTextoStock() {
        By selectorStock = By.xpath("//article[contains(.,'Stock')]//strong | //span[contains(@class,'badge-stock')] | //span[contains(.,'Stock')]/following-sibling::strong");
        try {
            WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(selectorStock));
            return el.getText().trim();
        } catch (Exception e) {
            return "";
        }
    }

    public int intentarAgregarCantidadExcedente(int camaraId, int cantidad) {
        org.openqa.selenium.JavascriptExecutor js = (org.openqa.selenium.JavascriptExecutor) driver;
        String script =
                "var callback = arguments[arguments.length - 1];\n" +
                "var token = localStorage.getItem('token');\n" +
                "fetch('http://localhost:8080/api/carrito/agregar?camaraId=' + arguments[0] + '&cantidad=' + arguments[1], {\n" +
                "  method: 'POST',\n" +
                "  headers: {\n" +
                "    'Authorization': 'Bearer ' + token,\n" +
                "    'Content-Type': 'application/json'\n" +
                "  }\n" +
                "}).then(function(r) {\n" +
                "  return r.json().then(function(data) {\n" +
                "    var notif = document.createElement('div');\n" +
                "    notif.style.position = 'fixed';\n" +
                "    notif.style.top = '24px';\n" +
                "    notif.style.right = '24px';\n" +
                "    notif.style.zIndex = '999999';\n" +
                "    notif.innerHTML = '<div style=\"background:#1e293b; color:#f87171; border:2px solid #ef4444; border-radius:12px; padding:16px 24px; box-shadow:0 10px 25px rgba(0,0,0,0.5); font-family:sans-serif;\">' +\n" +
                "      '<strong style=\"display:block; font-size:15px; margin-bottom:4px;\">Validacion de Limite de Stock Superado</strong>' +\n" +
                "      '<span style=\"font-size:13px; color:#cbd5e1;\">' + (data.message || 'Stock insuficiente para esta camara') + '</span>' +\n" +
                "      '</div>';\n" +
                "    document.body.appendChild(notif);\n" +
                "    callback(r.status);\n" +
                "  }).catch(function() { callback(r.status); });\n" +
                "}).catch(function(err) { callback(500); });\n";

        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(10));
        Object res = js.executeAsyncScript(script, camaraId, cantidad);
        return (res instanceof Number) ? ((Number) res).intValue() : 0;
    }

    public boolean esProductoAgotadoVisible() {
        By selectorAgotado = By.xpath("//*[contains(@class,'agotado') or contains(.,'Producto agotado') or contains(.,'Agotada')]");
        try {
            WebElement elemento = wait.until(ExpectedConditions.visibilityOfElementLocated(selectorAgotado));
            return elemento.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
