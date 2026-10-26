package com.camshop.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object Model para la página de autenticación (/login).
 */
public class LoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By inputEmail = By.id("username");
    private final By inputPassword = By.id("password");
    private final By btnIniciarSesion = By.cssSelector("button[type='submit']");
    private final By alertaError = By.cssSelector(".bg-rose-500\\/10, .text-rose-300");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public LoginPage navegar(String baseUrl) {
        driver.get(baseUrl + "/login");
        wait.until(ExpectedConditions.visibilityOfElementLocated(btnIniciarSesion));
        return this;
    }

    public void ingresarCredenciales(String email, String password) {
        WebElement emailElem = wait.until(ExpectedConditions.visibilityOfElementLocated(inputEmail));
        emailElem.clear();
        emailElem.sendKeys(email);

        WebElement passElem = wait.until(ExpectedConditions.visibilityOfElementLocated(inputPassword));
        passElem.clear();
        passElem.sendKeys(password);
    }

    public CatalogoPage iniciarSesionExitoso(String email, String password) {
        ingresarCredenciales(email, password);
        driver.findElement(btnIniciarSesion).click();
        wait.until(ExpectedConditions.urlContains("/camaras"));
        return new CatalogoPage(driver);
    }

    public LoginPage iniciarSesionInvalido(String email, String password) {
        ingresarCredenciales(email, password);
        driver.findElement(btnIniciarSesion).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(alertaError));
        return this;
    }

    public boolean esMensajeErrorVisible() {
        try {
            return driver.findElement(alertaError).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
