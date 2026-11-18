package ru.selenium.youtrack.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {
    private static final By USERNAME = By.xpath("//input[@placeholder='Username or Email' or @name='username' or @autocomplete='username']");
    private static final By PASSWORD = By.xpath("//input[@placeholder='Password' or @name='password' or @type='password']");
    private static final By SUBMIT = By.xpath("//button[normalize-space()='Log in'] | //input[@type='submit']");
    private static final By LOGIN_ERROR = By.xpath("//*[contains(normalize-space(.), 'Incorrect username or password')]");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage open(String baseUrl) {
        driver.get(baseUrl + "/login");
        return this;
    }

    public void login(String username, String password) {
        visible(USERNAME).sendKeys(username);
        visible(PASSWORD).sendKeys(password);
        clickable(SUBMIT).click();
    }

    public boolean hasLoginError() {
        try {
            return visible(LOGIN_ERROR).isDisplayed();
        } catch (org.openqa.selenium.TimeoutException ignored) {
            return false;
        }
    }
}
