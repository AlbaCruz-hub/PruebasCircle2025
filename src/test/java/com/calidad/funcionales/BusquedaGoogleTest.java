package com.calidad.funcionales;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BusquedaGoogleTest {

    private WebDriver driver;
    private StringBuffer verificationErrors = new StringBuffer();

    @BeforeEach
    public void setUp() {
    
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); 
        options.addArguments("--disable-gpu"); 
        options.addArguments("--window-size=1920,1080"); 
        options.addArguments("--no-sandbox"); 
        options.addArguments("--disable-dev-shm-usage"); 
        driver = new ChromeDriver(options);
    }

    @Test
    public void testTituloFelisCatus() {
        try {
            // Abrimos la página
            driver.get("https://es.wikipedia.org/wiki/Gato_dom%C3%A9stico");

            // Espera explícita hasta que el título contenga "Felis catus"
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.titleContains("Felis catus"));

            // Obtenemos el título actual
            String tituloActual = driver.getTitle();

            // Comprobamos que el título contiene "Felis catus"
            assertTrue(tituloActual.contains("Felis catus"), 
                "El título no contiene 'Felis catus'");

        } catch (Exception e) {
            verificationErrors.append(e.toString());
        }
    }

    @Test
    public void testBotonEjemplo() {
        try {
            // Abrimos página de prueba para demo de botón
            driver.get("https://www.ejemplo.com");

            // Espera explícita a que el botón esté presente
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement boton = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='root']/div/div[2]/button"))
            );

            // Comprobamos que el botón esté habilitado
            assertTrue(boton.isEnabled(), "El botón no está habilitado");

        } catch (Exception e) {
            verificationErrors.append(e.toString());
        }
    }

    @AfterEach
    public void tearDown() {
        // Cerramos el navegador
        if (driver != null) {
            driver.quit();
        }

        // Verificamos errores acumulados
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }
}
