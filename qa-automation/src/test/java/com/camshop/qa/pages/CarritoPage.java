package com.camshop.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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

    private final By btnProcederPago = By.xpath("//button[contains(.,'Proceder al pago') or contains(.,'Procesando pago')]");
    private final By mensajeExito = By.cssSelector(".bg-emerald-500\\/10, .text-emerald-300, .border-emerald-500\\/30");

    public CarritoPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    private void pausaBreve(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
        }
    }

    public boolean estaEnCarrito() {
        return driver.getCurrentUrl().contains("/carrito");
    }

    public CarritoPage seleccionarMetodoTarjeta() {
        pausaBreve(1000);
        try {
            // Invocar selección vía JS para asegurar activación en radio buttons con sr-only de Tailwind
            ((JavascriptExecutor) driver).executeScript(
                    "var r = document.querySelector(\"input[value='tarjeta']\");" +
                    "if(r) { r.click(); r.dispatchEvent(new Event('change', { bubbles: true })); }"
            );
        } catch (Exception ignored) {
        }
        pausaBreve(1500); // Pausa para apreciar el método de pago seleccionado
        return this;
    }

    public CheckoutPage procesarPago() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(btnProcederPago));
        pausaBreve(1000);
        btn.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(mensajeExito));
        return new CheckoutPage(driver);
    }
}
