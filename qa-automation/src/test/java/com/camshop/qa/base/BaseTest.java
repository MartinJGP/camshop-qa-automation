package com.camshop.qa.base;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.time.Duration;

/**
 * Clase base para todas las pruebas funcionales de CamShop.
 * Soporta auto-detección de navegadores Chromium (Chrome / Brave / Edge)
 * y pausas configurables para demostración visual ante jurados/docentes.
 */
public abstract class BaseTest {

    protected WebDriver driver;
    protected static final String BASE_URL = "http://localhost:4200";

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--window-size=1400,900");
        options.addArguments("--disable-gpu");
        // Nota: Dejar comentada la siguiente línea para observar el navegador visualmente durante la sustentación:
        // options.addArguments("--headless=new");

        // Detección automática del binario Chromium instalado (Chrome, Brave o Edge)
        String[] possibleBinaries = {
                "C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe",
                "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe",
                System.getProperty("user.home") + "\\AppData\\Local\\Google\\Chrome\\Application\\chrome.exe",
                "C:\\Program Files\\BraveSoftware\\Brave-Browser\\Application\\brave.exe",
                "C:\\Program Files (x86)\\Microsoft\\Edge\\Application\\msedge.exe"
        };

        for (String binaryPath : possibleBinaries) {
            File f = new File(binaryPath);
            if (f.exists()) {
                options.setBinary(f);
                break;
            }
        }

        // Búsqueda de chromedriver en cache local de Selenium
        File cacheDir = new File(System.getProperty("user.home") + "\\.cache\\selenium\\chromedriver\\win64");
        if (cacheDir.exists() && cacheDir.isDirectory()) {
            File[] versions = cacheDir.listFiles();
            if (versions != null && versions.length > 0) {
                File localDriver = new File(versions[0], "chromedriver.exe");
                if (localDriver.exists()) {
                    System.setProperty("webdriver.chrome.driver", localDriver.getAbsolutePath());
                }
            }
        }

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(15));
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * Pausa visual estratégica para permitir que evaluadores y docentes
     * aprecien con claridad cada interacción durante la sustentación.
     */
    protected void pausar(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
        }
    }
}
