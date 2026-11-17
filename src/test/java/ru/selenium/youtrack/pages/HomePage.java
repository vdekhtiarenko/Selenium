package ru.selenium.youtrack.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomePage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(25));
    }

    public boolean isLoaded() {
        try {
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.urlContains("/dashboard"),
                    ExpectedConditions.presenceOfElementLocated(By.cssSelector("[data-test='global-nav'], nav")),
                    ExpectedConditions.titleContains("YouTrack")));
            return !driver.getCurrentUrl().contains("/login");
        } catch (org.openqa.selenium.TimeoutException ignored) {
            return false;
        }
    }

    public void openProjects(String baseUrl) {
        driver.get(baseUrl + "/projects");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
    }

    public boolean containsProject(String projectKey) {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
                    "//*[self::a or self::div or self::span or self::td][contains(normalize-space(.), "
                            + xpathLiteral(projectKey) + ")]"))).isDisplayed();
        } catch (org.openqa.selenium.TimeoutException ignored) {
            return false;
        }
    }

    private String xpathLiteral(String value) {
        if (!value.contains("'")) return "'" + value + "'";
        if (!value.contains("\"")) return "\"" + value + "\"";
        return "concat('" + value.replace("'", "', \"'\", '") + "')";
    }
}
