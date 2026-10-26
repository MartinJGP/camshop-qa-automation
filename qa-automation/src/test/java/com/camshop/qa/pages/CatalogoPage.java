package com.camshop.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object Model para el catálogo de cámaras (/camaras).
 * Soporta navegación fluida hacia la vista de detalle y adición reactiva al carrito.
 */
public class CatalogoPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By linkVerDetalle = By.xpath("//a[contains(@href,'/camaras/') or contains(.,'Ver detalle')]");
    private final By selectorBotonCarrito = By.xpath("//button[contains(@class,'btn-carrito') or contains(.,'Agregar al carrito')]");

    public CatalogoPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public boolean estaEnCatalogo() {
        return driver.getCurrentUrl().contains("/camaras");
    }

    public CatalogoPage agregarPrimeraCamara() {
        // En catálogo: clic en 'Ver detalle' de la primera tarjeta disponible
        WebElement detalleLink = wait.until(ExpectedConditions.elementToBeClickable(linkVerDetalle));
        detalleLink.click();

        // En vista de detalle (/camaras/:id): clic en 'Agregar al carrito'
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(selectorBotonCarrito));
        btn.click();

        // Pausa breve para permitir la sincronización reactiva del carrito
        try {
            Thread.sleep(700);
        } catch (InterruptedException ignored) {
        }

        return this;
    }

    public CarritoPage irAlCarrito() {
        String currentUrl = driver.getCurrentUrl();
        String origin = currentUrl.contains("/camaras") ? currentUrl.substring(0, currentUrl.indexOf("/camaras")) : "http://localhost:4200";
        driver.get(origin + "/carrito");
        wait.until(ExpectedConditions.urlContains("/carrito"));
        return new CarritoPage(driver);
    }
}
