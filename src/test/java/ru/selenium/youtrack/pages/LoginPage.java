package ru.selenium.youtrack.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(25));
    }

    public LoginPage open(String baseUrl) {
        driver.get(baseUrl + "/login");
        return this;
    }

    public void login(String username, String password) {
        input(By.cssSelector("input[placeholder='Username or Email']"),
                By.name("username"), By.cssSelector("input[autocomplete='username']"))
                .sendKeys(username);
        input(By.cssSelector("input[placeholder='Password']"),
                By.name("password"), By.cssSelector("input[type='password']"))
                .sendKeys(password);
        WebElement submit = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[normalize-space()='Log in'] | //input[@type='submit']")));
        submit.click();
    }

    public boolean hasLoginError() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
                    "//*[contains(normalize-space(.), 'Incorrect username or password')]"))).isDisplayed();
        } catch (org.openqa.selenium.TimeoutException ignored) {
            return false;
        }
    }

    private WebElement input(By... selectors) {
        for (By selector : selectors) {
            var matches = driver.findElements(selector);
            if (!matches.isEmpty()) {
                return wait.until(ExpectedConditions.visibilityOf(matches.get(0)));
            }
        }
        return wait.until(ExpectedConditions.visibilityOfElementLocated(selectors[0]));
    }
}
