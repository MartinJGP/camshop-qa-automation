package com.camshop.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object Model para el catálogo de cámaras (/camaras).
 */
public class CatalogoPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By btnAgregarCarrito = By.xpath("//button[contains(text(),'Agregar') or contains(text(),'al carrito')]");
    private final By linkCarrito = By.cssSelector("a[routerLink='/carrito'], a[href*='carrito']");
    private final By toastExito = By.xpath("//*[contains(text(),'agregado') or contains(@class,'toast')]");

    public CatalogoPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public boolean estaEnCatalogo() {
        return driver.getCurrentUrl().contains("/camaras");
    }

    public CatalogoPage agregarPrimeraCamara() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(btnAgregarCarrito));
        btn.click();
        // Espera opcional a la notificación reactiva
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(toastExito));
        } catch (Exception ignored) {
        }
        return this;
    }

    public CarritoPage irAlCarrito() {
        WebElement link = wait.until(ExpectedConditions.elementToBeClickable(linkCarrito));
        link.click();
        wait.until(ExpectedConditions.urlContains("/carrito"));
        return new CarritoPage(driver);
    }
}
