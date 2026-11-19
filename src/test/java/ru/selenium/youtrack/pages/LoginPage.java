package ru.selenium.youtrack.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {
    private static final By USERNAME = By.xpath("//input[@data-test='username-field']");
    private static final By PASSWORD = By.xpath("//input[@data-test='password-field']");
    private static final By SUBMIT = By.xpath("//button[@data-test='login-button']");
    private static final By LOGIN_ERROR = By.xpath("//*[@data-test='error-message']//*[normalize-space(text()) = 'Incorrect username or password.']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage open(String baseUrl) {
        driver.get(baseUrl + "/login");
        return this;
    }

    public void login(String username, String password) {
        var usernameField = visible(USERNAME);
        usernameField.clear();
        usernameField.sendKeys(username);

        var passwordField = visible(PASSWORD);
        passwordField.clear();
        passwordField.sendKeys(password);
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
