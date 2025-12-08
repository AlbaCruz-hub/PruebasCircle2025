
package com.calidad.funcionales;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

public class CRUDFuncionalTest {

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
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"); 
        options.addArguments("--ignore-certificate-errors");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5)); // breve espera implícita
        js = (JavascriptExecutor) driver;
    }

    // ================== TESTS ===================

    @Test
    public void testCRUD_Create() {
        try {
            driver.get("https://mern-crud-mpfr.onrender.com/");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

            // Abrir modal de creación
            WebElement botonNuevo = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[@id='root']/div/div[2]/button")
            ));
            botonNuevo.click();

            // Completar formulario
            driver.findElement(By.name("name")).sendKeys("Vianey Cante Cab");
            driver.findElement(By.name("email")).sendKeys("vianeycita1908@gmail.com");
            driver.findElement(By.name("age")).sendKeys("22");

            // Selección de género
            driver.findElement(By.xpath("(.//*[normalize-space(text())='Gender'])[2]/following::div[2]")).click();
            driver.findElement(By.xpath("(.//*[normalize-space(text())='Male'])[2]/following::span[1]")).click();

            // Guardar registro
            driver.findElement(By.xpath("(.//*[normalize-space(text())='Woah!'])[1]/following::button[1]")).click();

            // Espera y validación del mensaje de éxito
            WebElement mensaje = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//p[contains(text(),'Successfully added!')]")
            ));
            assertTrue(mensaje.isDisplayed());

        } catch (Exception e) {
            takeScreenshot("testCRUD_Create");
            verificationErrors.append("testCRUD_Create: " + e.toString() + "\n");
        }
    }

    @Test
    public void testCRUD_Update() {
        try {
            driver.get("https://mern-crud-mpfr.onrender.com/");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

            WebElement botonEditar = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[@id='root']/div/div[2]/table/tbody/tr[1]/td[5]/button[1]")
            ));
            botonEditar.click();

            driver.findElement(By.name("name")).clear();
            driver.findElement(By.name("name")).sendKeys("Vianey Cante");

            String emailUnico = "vianey" + System.currentTimeMillis() + "@gmail.com";
            driver.findElement(By.name("email")).clear();
            driver.findElement(By.name("email")).sendKeys(emailUnico);

            driver.findElement(By.name("age")).clear();
            driver.findElement(By.name("age")).sendKeys("23");

            driver.findElement(By.xpath("(.//*[normalize-space(text())='Gender'])[2]/following::div[2]")).click();
            driver.findElement(By.xpath("(.//*[normalize-space(text())='Male'])[1]/following::span[1]")).click();

            WebElement botonUpdate = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//form//button[contains(text(),'Update')]")
            ));
            botonUpdate.click();

            WebElement mensajeExito = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[contains(text(), 'Successfully updated!')]")
            ));
            assertTrue(mensajeExito.isDisplayed());

        } catch (Exception e) {
            takeScreenshot("testCRUD_Update");
            verificationErrors.append("testCRUD_Update: " + e.toString() + "\n");
        }
    }

    @Test
    public void testCRUD_Delete() {
        try {
            driver.get("https://mern-crud-mpfr.onrender.com/");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

            String idAEliminar = driver.findElement(By.xpath("//table/tbody/tr[1]/td[1]")).getText();
            int filasInicial = driver.findElements(By.xpath("//table/tbody/tr")).size();

            driver.findElement(By.xpath("//table/tbody/tr[1]/td[5]/button[2]")).click();

            WebElement botonConfirmar = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[contains(@class,'modal-footer')]/button[1]")
            ));
            botonConfirmar.click();

            wait.until(d -> driver.findElements(By.xpath("//table/tbody/tr")).size() == filasInicial - 1);

            List<WebElement> ids = driver.findElements(By.xpath("//table/tbody/tr/td[1]"));
            boolean idEliminado = ids.stream().noneMatch(el -> el.getText().equals(idAEliminar));
            assertTrue(idEliminado, "El registro no fue eliminado correctamente.");

        } catch (Exception e) {
            takeScreenshot("testCRUD_Delete");
            verificationErrors.append("testCRUD_Delete: " + e.toString() + "\n");
        }
    }

    // ================== TEARDOWN ===================

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

    // ================== MÉTODOS AUXILIARES ===================

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

    // ================== CAPTURA DE SCREENSHOT ===================
    private void takeScreenshot(String testName) {
        try {
            File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File destFile = new File("target/screenshots/" + testName + ".png");
            destFile.getParentFile().mkdirs();
            scrFile.renameTo(destFile);
        } catch (Exception e) {
            System.out.println("No se pudo capturar screenshot: " + e.toString());
        }
    }
}
