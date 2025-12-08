package com.calidad.funcionales;

import static org.junit.jupiter.api.Assertions.fail;

import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

public class UadyFallidoTest {

    private WebDriver driver;
    private StringBuffer verificationErrors = new StringBuffer();
    private JavascriptExecutor js;
    private boolean acceptNextAlert = true;

    @BeforeEach
    public void setUp() throws Exception {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--remote-allow-origins=*");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        js = (JavascriptExecutor) driver;
    }

    @Test
    public void testAccesoUADY() {
        try {
            driver.get("https://es.uadyvirtual.uady.mx/login/index.php");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

            // Espera a que el campo de contraseña sea visible y luego envía la primera clave
            WebElement passwordInput = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.id("password"))
            );
            passwordInput.clear();
            passwordInput.sendKeys("V1n9y8jr");

            // Si necesitas interactuar con otro elemento antes de enviar login
            WebElement regionBox = wait.until(
                    ExpectedConditions.elementToBeClickable(By.id("region-main-box"))
            );
            regionBox.click();

            // Espera a que el botón de login sea clickeable
            WebElement loginBtn = wait.until(
                    ExpectedConditions.elementToBeClickable(By.id("loginbtn"))
            );

            // Cambiar contraseña antes de enviar login
            passwordInput.clear();
            passwordInput.sendKeys("1234gdg");

            loginBtn.click();

            // Aquí puedes agregar validación si la página de inicio carga correctamente
            wait.until(ExpectedConditions.urlContains("dashboard")); // ejemplo

        } catch (Exception e) {
            verificationErrors.append("testAccesoUADY: " + e.toString() + "\n");
        }
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
        String verificationErrorString = verificationErrors.toString();
        if (!verificationErrorString.isEmpty()) {
            fail(verificationErrorString);
        }
    }

    private boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private boolean isAlertPresent() {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    private String closeAlertAndGetItsText() {
        try {
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            if (acceptNextAlert) {
                alert.accept();
            } else {
                alert.dismiss();
            }
            return alertText;
        } finally {
            acceptNextAlert = true;
        }
    }
}
