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
    private String baseUrl;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();
    JavascriptExecutor js;

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

        baseUrl = "https://www.google.com/";
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
        js = (JavascriptExecutor) driver;
    }

      @Test
    public void testCRUD_Create() throws Exception {
        driver.get("https://mern-crud-mpfr.onrender.com/");
        driver.findElement(By.xpath("//div[@id='root']/div/div[2]/button")).click();
        driver.findElement(By.name("name")).click();
        driver.findElement(By.name("name")).clear();
        driver.findElement(By.name("name")).sendKeys("Vianey Cante Cab");
        driver.findElement(By.name("email")).click();
        driver.findElement(By.name("email")).clear();
        driver.findElement(By.name("email")).sendKeys("vianeycita1908@gmail.com.com");
        driver.findElement(By.name("age")).click();
        driver.findElement(By.name("age")).clear();
        driver.findElement(By.name("age")).sendKeys("22");
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Gender'])[2]/following::div[2]")).click();
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Male'])[2]/following::span[1]")).click();
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Woah!'])[1]/following::button[1]")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        boolean textoActual = wait.until(ExpectedConditions.textToBe(
                By.xpath("/html/body/div[3]/div/div[2]/form/div[4]/div/p"),
                "Successfully added!"
        ));

        assertTrue(textoActual);
    }


    @Test
    public void testCRUD_EmailError() throws Exception {
        driver.get("https://mern-crud-mpfr.onrender.com/");
        driver.findElement(By.xpath("//div[@id='root']/div/div[2]/button")).click();
        driver.findElement(By.name("name")).click();
        driver.findElement(By.name("name")).clear();
        driver.findElement(By.name("name")).sendKeys("Vianey Cante Cab");
        driver.findElement(By.name("email")).click();
        driver.findElement(By.name("email")).clear();
        driver.findElement(By.name("email")).sendKeys("vianeyyy@correo");
        driver.findElement(By.name("age")).click();
        driver.findElement(By.name("age")).clear();
        driver.findElement(By.name("age")).sendKeys("22");
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Gender'])[2]/following::div[2]")).click();
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Male'])[2]/following::span[1]")).click();
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Woah!'])[1]/following::button[1]")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        boolean textoActual = wait.until(ExpectedConditions.textToBe(
            By.xpath("/html/body/div[3]/div/div[2]/form/div[5]/div/p"),
            "Email must be valid."
        ));

        assertTrue(textoActual);
    }
    @Test
    public void testCRUD_Update() throws Exception {
        driver.get("https://mern-crud-mpfr.onrender.com/");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));

        driver.findElement(By.xpath("//div[@id='root']/div/div[2]/table/tbody/tr[1]/td[5]/button[1]")).click();
        
        driver.findElement(By.name("name")).click();
        driver.findElement(By.name("name")).clear();
        driver.findElement(By.name("name")).sendKeys("Vianey Cante");
        
        driver.findElement(By.name("email")).click();
        driver.findElement(By.name("email")).clear();
        String emailUnico = "vianey" + System.currentTimeMillis() + "@gmail.com";
        driver.findElement(By.name("email")).sendKeys(emailUnico);
        
        driver.findElement(By.name("age")).click();
        driver.findElement(By.name("age")).clear();
        driver.findElement(By.name("age")).sendKeys("23");
        
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Gender'])[2]/following::div[2]")).click();
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Male'])[1]/following::span[1]")).click();

        Thread.sleep(2000);

        WebElement botonUpdate = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/div/div[2]/form/button")));
        botonUpdate.click();

        WebElement mensajeExito = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(text(), 'Successfully updated!')]")
        ));

        assertTrue(mensajeExito.isDisplayed());
    }
@Test
public void testCRUD_Delete() throws Exception {
    driver.get("https://mern-crud-mpfr.onrender.com/");
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    
    String idAEliminar = driver.findElement(
                By.xpath("/html/body/div[2]/div/div[2]/table/tbody/tr[1]/td[1]")
    ).getText();
    
    int filasInicial = driver.findElements(
                By.xpath("/html/body/div[2]/div/div[2]/table/tbody/tr")
    ).size();
    
    driver.findElement(
                By.xpath("/html/body/div[2]/div/div[2]/table/tbody/tr[1]/td[5]/button[2]")
    ).click();
    
    driver.findElement(
                By.xpath("/html/body/div[3]/div/div[3]/button[1]")
    ).click();
    
    wait.until(driver -> {
        int filaActual = driver.findElements(
                        By.xpath("/html/body/div[2]/div/div[2]/table/tbody/tr")
        ).size();
        return filaActual == filasInicial - 1;
    });
    
    Thread.sleep(30000); 
    
    List<WebElement> ids = driver.findElements(
                By.xpath("/html/body/div[2]/div/div[2]/table/tbody/tr/td[1]")
    );
    
    boolean idEliminado = ids.stream()
                .noneMatch(elemento -> elemento.getText().equals(idAEliminar));
                
    assertTrue(idEliminado, "El registro no fue eliminado correctamente.");
}
  
     @AfterEach
    public void tearDown() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
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