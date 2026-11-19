package ru.selenium.youtrack.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

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
        replaceValue(visible(USERNAME), username, "username");
        replaceValue(visible(PASSWORD), password, "password");
        clickable(SUBMIT).click();
    }

    private void replaceValue(WebElement field, String value, String fieldName) {
        field.click();
        field.sendKeys(Keys.CONTROL, "a", Keys.BACK_SPACE);
        field.sendKeys(value);

        String actualValue = field.getDomProperty("value");
        if (!value.equals(actualValue)) {
            throw new IllegalStateException("The " + fieldName + " field did not retain the entered value"
                    + " (expected length " + value.length() + ", actual length "
                    + (actualValue == null ? 0 : actualValue.length()) + ")");
        }
    }

    public boolean hasLoginError() {
        try {
            return visible(LOGIN_ERROR).isDisplayed();
        } catch (org.openqa.selenium.TimeoutException ignored) {
            return false;
        }
    }
}
