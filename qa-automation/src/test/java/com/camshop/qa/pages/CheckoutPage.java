package com.camshop.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object Model para la verificación del estado de orden y confirmación de pago.
 */
public class CheckoutPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By mensajeExito = By.cssSelector(".bg-emerald-500\\/10, .text-emerald-300, .border-emerald-500\\/30");

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public boolean esPagoExitoso() {
        try {
            WebElement exito = wait.until(ExpectedConditions.visibilityOfElementLocated(mensajeExito));
            String texto = exito.getText();
            return texto.contains("Orden #") || texto.contains("registrada") || texto.contains("PAGADO") || texto.contains("Pago");
        } catch (Exception e) {
            return false;
        }
    }

    public String obtenerMensajeExito() {
        try {
            return driver.findElement(mensajeExito).getText();
        } catch (Exception e) {
            return "";
        }
    }
}
