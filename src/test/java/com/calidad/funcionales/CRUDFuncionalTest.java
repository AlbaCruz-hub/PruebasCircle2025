package com.calidad.funcionales;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.chrome.ChromeOptions;

import io.github.bonigarcia.wdm.WebDriverManager;

public class CRUDFuncionalTest {

    private WebDriver driver;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();
    private JavascriptExecutor js;

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
        // Se mantiene el implicit wait en 10s, los waits explícitos controlan la sincronización.
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10)); 
        js = (JavascriptExecutor) driver;
    }

    @Test
    public void testCRUD_Create() throws Exception {
        driver.get("https://mern-crud-mpfr.onrender.com/");

        driver.findElement(By.xpath("//div[@id='root']/div/div[2]/button")).click();
        driver.findElement(By.name("name")).clear();
        driver.findElement(By.name("name")).sendKeys("Vianey Cante Cab");
        driver.findElement(By.name("email")).clear();
        driver.findElement(By.name("email")).sendKeys("vianeycita1908@gmail.com");
        driver.findElement(By.name("age")).clear();
        driver.findElement(By.name("age")).sendKeys("22");
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Gender'])[2]/following::div[2]")).click();
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Male'])[2]/following::span[1]")).click();
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Woah!'])[1]/following::button[1]")).click();

        // CORRECCIÓN: Aumentar el tiempo de espera a 30 segundos
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        WebElement mensajeExito = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(text(),'Successfully added!')]")
        ));

        assertTrue(mensajeExito.isDisplayed());
    }

    @Test
    public void testCRUD_EmailError() throws Exception {
        driver.get("https://mern-crud-mpfr.onrender.com/");

        driver.findElement(By.xpath("//div[@id='root']/div/div[2]/button")).click();
        driver.findElement(By.name("name")).clear();
        driver.findElement(By.name("name")).sendKeys("Vianey Cante Cab");
        driver.findElement(By.name("email")).clear();
        driver.findElement(By.name("email")).sendKeys("vianeyyy@correo");
        driver.findElement(By.name("age")).clear();
        driver.findElement(By.name("age")).sendKeys("22");
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Gender'])[2]/following::div[2]")).click();
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Male'])[2]/following::span[1]")).click();
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Woah!'])[1]/following::button[1]")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        WebElement mensajeError = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(text(),'Email must be valid.')]")
        ));

        assertTrue(mensajeError.isDisplayed());
    }

    @Test
    public void testCRUD_Update() throws Exception {
        driver.get("https://mern-crud-mpfr.onrender.com/");
        // CORRECCIÓN: Aumentar el tiempo de espera a 45 segundos para ser más robusto
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(45));

        driver.findElement(By.xpath("//table/tbody/tr[1]/td[5]/button[1]")).click();

        driver.findElement(By.name("name")).clear();
        driver.findElement(By.name("name")).sendKeys("Vianey Cante");
        String emailUnico = "vianey" + System.currentTimeMillis() + "@gmail.com";
        driver.findElement(By.name("email")).clear();
        driver.findElement(By.name("email")).sendKeys(emailUnico);
        driver.findElement(By.name("age")).clear();
        driver.findElement(By.name("age")).sendKeys("23");

        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Gender'])[2]/following::div[2]")).click();
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Male'])[1]/following::span[1]")).click();

        // Uso de la espera larga (45s) para el botón
        WebElement botonUpdate = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//form//button[contains(text(),'Update')]")));
        botonUpdate.click();

        // Uso de la espera larga (45s) para el mensaje de éxito
        WebElement mensajeExito = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(text(),'Successfully updated!')]")
        ));

        assertTrue(mensajeExito.isDisplayed());
    }

    @Test
    public void testCRUD_Delete() throws Exception {
        driver.get("https://mern-crud-mpfr.onrender.com/");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        // Obtener ID de la primera fila
        String idAEliminar = driver.findElement(By.xpath("//table/tbody/tr[1]/td[1]")).getText();

        // Hacer click en botón de delete
        driver.findElement(By.xpath("//table/tbody/tr[1]/td[5]/button[2]")).click();

        // CORRECCIÓN: Esperar a que el modal aparezca antes de intentar hacer clic en el botón (resuelve NoSuchElementException)
        By modalFooter = By.xpath("//div[contains(@class,'modal-footer')]");
        wait.until(ExpectedConditions.visibilityOfElementLocated(modalFooter));
        
        // Confirmar modal de eliminación
        driver.findElement(By.xpath("//div[contains(@class,'modal-footer')]/button[1]")).click();

        // Esperar hasta que la fila desaparezca
        wait.until(d -> driver.findElements(By.xpath("//table/tbody/tr/td[1]"))
                .stream().noneMatch(el -> el.getText().equals(idAEliminar)));

        // Verificación final
        List<WebElement> ids = driver.findElements(By.xpath("//table/tbody/tr/td[1]"));
        boolean idEliminado = ids.stream().noneMatch(el -> el.getText().equals(idAEliminar));
        assertTrue(idEliminado, "El registro no fue eliminado correctamente.");
    }

    @AfterEach
    public void tearDown() throws Exception {
        if (driver != null) {
            driver.quit();
        }
        String verificationErrorString = verificationErrors.toString();
        if (!verificationErrorString.isEmpty()) {
            fail(verificationErrorString);
        }
    }

    // ==================== MÉTODOS AUXILIARES ====================
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