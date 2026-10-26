package com.camshop.qa.base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

import java.io.File;
import java.time.Duration;

/**
 * Clase base para todas las pruebas funcionales de CamShop.
 * Configura el ciclo de vida del WebDriver con detección inteligente de navegador:
 * utiliza Google Chrome si está disponible, o Microsoft Edge (Chromium) nativo de Windows.
 */
public abstract class BaseTest {

    protected WebDriver driver;
    protected static final String BASE_URL = "http://localhost:4200";

    @BeforeEach
    public void setUp() {
        boolean hasChrome = new File("C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe").exists()
                || new File("C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe").exists()
                || new File(System.getProperty("user.home") + "\\AppData\\Local\\Google\\Chrome\\Application\\chrome.exe").exists();

        if (hasChrome) {
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--remote-allow-origins=*");
            options.addArguments("--window-size=1920,1080");
            options.addArguments("--disable-gpu");
            // Descomentar para ejecución headless en CI/CD:
            // options.addArguments("--headless=new");
            driver = new ChromeDriver(options);
        } else {
            // Fallback transparente a Microsoft Edge (Chromium nativo en Windows)
            WebDriverManager.edgedriver().setup();
            EdgeOptions options = new EdgeOptions();
            options.addArguments("--remote-allow-origins=*");
            options.addArguments("--window-size=1920,1080");
            options.addArguments("--disable-gpu");
            // Descomentar para ejecución headless en CI/CD:
            // options.addArguments("--headless");
            driver = new EdgeDriver(options);
        }

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(15));
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
