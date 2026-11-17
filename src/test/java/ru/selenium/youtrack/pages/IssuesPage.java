package ru.selenium.youtrack.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class IssuesPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    public IssuesPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(25));
    }

    public void open(String baseUrl) {
        driver.get(baseUrl + "/issues");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
    }

    public void createIssue(String projectKey, String summary, String description) {
        clickFirst(By.cssSelector("a[data-test='createIssueButton']"));

        // The issue list context already selects the target project.

        WebElement title = findFirst(By.cssSelector("textarea[data-test='summary']"));
        title.sendKeys(summary);
        driver.findElements(By.cssSelector("[data-test='wysiwyg-editor-content'][contenteditable='true']"))
                .stream().findFirst().ifPresent(element -> element.sendKeys(description));

        clickFirst(By.cssSelector("button[data-test='submit-button']"),
                By.xpath("//button[contains(normalize-space(.), 'Create') or contains(normalize-space(.), 'Submit')]") );
        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains(projectKey + "-"),
                ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("textarea[data-test='summary']"))));
    }

    public void search(String query) {
        WebElement search = findFirst(
                By.cssSelector("input[placeholder='Search for text or add a filter']"),
                By.xpath("//input[contains(@placeholder, 'Search') or contains(@aria-label, 'Search')]") );
        search.clear();
        search.sendKeys(query, Keys.ENTER);
    }

    public boolean showsIssue(String summary) {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
                    "//*[contains(normalize-space(.), " + xpathLiteral(summary) + ")]"))).isDisplayed();
        } catch (org.openqa.selenium.TimeoutException ignored) {
            return false;
        }
    }

    private WebElement findFirst(By... selectors) {
        return wait.until(currentDriver -> {
            for (By selector : selectors) {
                for (WebElement element : currentDriver.findElements(selector)) {
                    if (element.isDisplayed() && element.isEnabled()) return element;
                }
            }
            return null;
        });
    }

    private void clickFirst(By... selectors) {
        findFirst(selectors).click();
    }

    private String xpathLiteral(String value) {
        if (!value.contains("'")) return "'" + value + "'";
        if (!value.contains("\"")) return "\"" + value + "\"";
        return "concat('" + value.replace("'", "', \"'\", '") + "')";
    }
}
