package com.camshop.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object Model para el carrito de compras y checkout (/carrito).
 */
public class CarritoPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By radioTarjeta = By.cssSelector("input[value='tarjeta']");
    private final By btnProcederPago = By.xpath("//button[contains(text(),'Proceder al pago')]");
    private final By mensajeExito = By.cssSelector(".bg-emerald-500\\/10, .text-emerald-300");
    private final By labelCantidadResumen = By.cssSelector("header h1, .text-xl.font-extrabold");

    public CarritoPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public boolean estaEnCarrito() {
        return driver.getCurrentUrl().contains("/carrito");
    }

    public CarritoPage seleccionarMetodoTarjeta() {
        try {
            WebElement radio = driver.findElement(radioTarjeta);
            if (!radio.isSelected()) {
                // Si es un radio oculto estilizado, hacemos click sobre su label padre
                WebElement label = radio.findElement(By.xpath("./.."));
                label.click();
            }
        } catch (Exception e) {
            // Alternativa por JS si Tailwind usa sr-only
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                    "document.querySelector(\"input[value='tarjeta']\").checked = true;"
            );
        }
        return this;
    }

    public CheckoutPage procesarPago() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(btnProcederPago));
        btn.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(mensajeExito));
        return new CheckoutPage(driver);
    }
}
