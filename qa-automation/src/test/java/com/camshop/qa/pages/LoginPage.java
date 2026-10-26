package com.camshop.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object Model para la página de autenticación (/login).
 * Compatible con formularios reactivos de Angular, alertas dinámicas SweetAlert2 y pausas para demo.
 */
public class LoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By inputEmail = By.cssSelector("input[formControlName='username'], input[type='email'], #username");
    private final By inputPassword = By.cssSelector("input[formControlName='password'], input[type='password'], #password");
    private final By btnIniciarSesion = By.cssSelector("button[type='submit']");
    private final By alertaError = By.cssSelector(".bg-rose-50, .text-rose-700, .border-rose-200, .bg-rose-500\\/10, .text-rose-300");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    private void pausaBreve(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
        }
    }

    public LoginPage navegar(String baseUrl) {
        driver.get(baseUrl + "/login");
        wait.until(ExpectedConditions.visibilityOfElementLocated(btnIniciarSesion));
        pausaBreve(1000);
        return this;
    }

    public void ingresarCredenciales(String email, String password) {
        WebElement emailElem = wait.until(ExpectedConditions.visibilityOfElementLocated(inputEmail));
        emailElem.clear();
        emailElem.sendKeys(email);
        pausaBreve(800);

        WebElement passElem = wait.until(ExpectedConditions.visibilityOfElementLocated(inputPassword));
        passElem.clear();
        passElem.sendKeys(password);
        pausaBreve(800);
    }

    public CatalogoPage iniciarSesionExitoso(String email, String password) {
        ingresarCredenciales(email, password);
        driver.findElement(btnIniciarSesion).click();

        // Si aparece el modal de SweetAlert2, esperar y confirmar para continuar
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(4));
            WebElement confirmBtn = shortWait.until(
                    ExpectedConditions.elementToBeClickable(By.cssSelector(".swal2-confirm, button.swal2-confirm"))
            );
            pausaBreve(1500); // Permite ver el SweetAlert de confirmación
            confirmBtn.click();
        } catch (Exception ignored) {
        }

        // Navegar al catálogo de cámaras
        String origin = driver.getCurrentUrl().replaceAll("/login.*", "");
        if (origin.isEmpty() || !origin.startsWith("http")) {
            origin = "http://localhost:4200";
        }
        driver.get(origin + "/camaras");
        wait.until(ExpectedConditions.urlContains("/camaras"));
        pausaBreve(1200);

        return new CatalogoPage(driver);
    }

    public LoginPage iniciarSesionInvalido(String email, String password) {
        ingresarCredenciales(email, password);
        driver.findElement(btnIniciarSesion).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(alertaError));
        pausaBreve(1500);
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
