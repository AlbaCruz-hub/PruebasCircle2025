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
        options.addArguments("--headless"); // Ejecutar sin GUI
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage"); // Estabilidad en Linux CI
        driver = new ChromeDriver(options);
    }

    @Test
    public void testTituloFelisCatus() {
        try {
            driver.get("https://es.wikipedia.org/wiki/Gato_dom%C3%A9stico");

            // Espera explícita hasta que el título contenga "Felis catus"
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
            wait.until(ExpectedConditions.titleContains("Felis catus"));

            String tituloActual = driver.getTitle();
            assertTrue(tituloActual.contains("Felis catus"), 
                "El título no contiene 'Felis catus'");

        } catch (Exception e) {
            verificationErrors.append("testTituloFelisCatus: " + e.toString() + "\n");
        }
    }

    @Test
    public void testBotonEjemplo() {
        try {
            // Abrimos página de prueba
            driver.get("https://www.ejemplo.com");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(90));

            // Cambié el XPath a un CSS selector más estable; si no funciona, ajusta según tu DOM
            WebElement boton = wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("div#root > div > div:nth-child(2) > button"))
            );

            // Comprobamos que el botón esté habilitado
            assertTrue(boton.isEnabled(), "El botón no está habilitado");

        } catch (Exception e) {
            verificationErrors.append("testBotonEjemplo: " + e.toString() + "\n");
        }
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }

        // Si hay errores acumulados, fallamos el test
        String verificationErrorString = verificationErrors.toString();
        if (!verificationErrorString.isEmpty()) {
            fail(verificationErrorString);
        }
    }
}
