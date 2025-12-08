package com.calidad.funcionales;


import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.junit.jupiter.api.Disabled;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BusquedaGoogleTest {
   private WebDriver driver;
    private StringBuffer verificationErrors = new StringBuffer();
    
    @BeforeEach
    public void setUp() throws Exception {
        WebDriverManager.chromedriver().setup();

        // Para que no aparezca el Captcha
        ChromeOptions options = new ChromeOptions();
        
        // 1. Quitar la barra "Un software automatizado de pruebas..."
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.setExperimentalOption("useAutomationExtension", false);

        // 2. Ocultar la bandera interna que le grita a Google "SOY UN ROBOT"
        options.addArguments("--disable-blink-features=AutomationControlled");

        // 3. Maximizar ventana para parecer un humano real
        options.addArguments("--start-maximized");

        // 4. Usar un User-Agent de navegador normal (importante)
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");

        driver = new ChromeDriver(options);
        

    }

    @Test
    @Disabled("Se deshabilita en CI porque Google detecta el bot y bloquea la conexion")
    public void testBusqueda() throws Exception {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        driver.get("https://www.google.com/");

        
        WebElement barraBusqueda = wait.until(ExpectedConditions.elementToBeClickable(By.name("q")));
        barraBusqueda.clear();
        barraBusqueda.sendKeys("gatitos");
        
        barraBusqueda.sendKeys(Keys.ENTER);

        // Esperamos resultados
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("search")));

        // Segunda búsqueda: Limpiamos y buscamos otra cosa
        // Nota: Al cambiar de página, hay que volver a buscar el elemento
        barraBusqueda = wait.until(ExpectedConditions.elementToBeClickable(By.name("q")));
        barraBusqueda.clear();
        barraBusqueda.sendKeys("Felis catus wikipedia");
        barraBusqueda.sendKeys(Keys.ENTER);

        // Clic en el resultado de Wikipedia (Buscamos por parte del texto del enlace)
        WebElement linkWiki = wait.until(ExpectedConditions.elementToBeClickable(
            By.partialLinkText("Felis catus - Wikipedia")
        ));
        linkWiki.click();

        // Verificación
        wait.until(ExpectedConditions.titleContains("Felis catus"));
        String tituloActual = driver.getTitle();
        
        // Usamos assertTrue con contains porque el título exacto puede variar ligeramente
        assertTrue(tituloActual.contains("Felis catus"), "El título no contiene 'Felis catus'");
    }

    @AfterEach
    public void tearDown() throws Exception {
        if (driver != null) {
            driver.quit();
        }
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }
}