package ru.selenium.youtrack.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class HomePage extends BasePage {
    private static final By PROJECTS_CONTENT = By.xpath("//*[@data-test='project-list']");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        try {
            return wait.until(currentDriver -> ((String) ((JavascriptExecutor) currentDriver)
                    .executeScript("return document.body ? document.body.innerText : ''"))
                    .contains("Issues"));
        } catch (org.openqa.selenium.TimeoutException ignored) {
            return false;
        }
    }

    public void openProjects(String baseUrl) {
        driver.get(baseUrl + "/projects");
        wait.until(ExpectedConditions.presenceOfElementLocated(PROJECTS_CONTENT));
    }

    public boolean containsProject(String projectName) {
        try {
            By project = By.xpath("//*[@data-test='project' and @data-test-name="
                    + xpathLiteral(projectName) + "]");
            return visible(project).isDisplayed();
        } catch (org.openqa.selenium.TimeoutException ignored) {
            return false;
        }
    }
}
