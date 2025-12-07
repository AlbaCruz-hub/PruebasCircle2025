package com.calidad.funcionales;


import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.apache.commons.io.FileUtils;
import java.io.File;
import java.time.Duration;

public class BusquedaGoogleTest {
    private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();
  JavascriptExecutor js;
  @BeforeEach
  public void setUp() throws Exception {
    WebDriverManager.chromedriver().setup();
    driver = new ChromeDriver();
    baseUrl = "https://www.google.com/";
    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
    js = (JavascriptExecutor) driver;
  }

  @Test
  public void testBusqueda() throws Exception {
    driver.get("https://www.google.com/");
    driver.findElement(By.id("APjFqb")).clear();
    driver.findElement(By.id("APjFqb")).sendKeys("gatitos");
    driver.findElement(By.id("APjFqb")).click();
    driver.findElement(By.id("APjFqb")).clear();
    driver.findElement(By.id("APjFqb")).sendKeys("gatos wiki");
    driver.findElement(By.xpath("//div[@id='jZ2SBf']/div/span")).click();
    driver.findElement(By.id("_RIUSac_qHI_MkPIPuYiikAU_37")).click();
    driver.get("https://es.wikipedia.org/wiki/Felis_catus");
    assertEquals("Felis catus - Wikipedia, la enciclopedia libre", driver.getTitle());
    //ERROR: Caught exception [unknown command []]
    pause(5000);
    //ERROR: Caught exception [unknown command []]
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
  private void pause(long mils){
    try{
        Thread.sleep(mils);
    }
    catch(Exception e){
        e.printStackTrace();
    }
  }

}
