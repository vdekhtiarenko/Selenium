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
        clickFirst(By.cssSelector("button[aria-label='New issue']"),
                By.xpath("//button[contains(normalize-space(.), 'New issue') or contains(normalize-space(.), 'Create issue')]") );

        WebElement project = findFirst(By.cssSelector("input[placeholder*='Project'], input[aria-label*='Project']"),
                By.xpath("//input[contains(@placeholder, 'Project') or contains(@aria-label, 'Project')]"));
        project.sendKeys(projectKey);
        project.sendKeys(Keys.ENTER);

        WebElement title = findFirst(By.cssSelector("input[placeholder='Summary'], input[aria-label='Summary'], input[name='summary']"),
                By.xpath("//input[contains(@placeholder, 'Summary') or contains(@aria-label, 'Summary')]"));
        title.sendKeys(summary);
        driver.findElements(By.cssSelector("textarea[placeholder*='Description'], textarea[aria-label*='Description']"))
                .stream().findFirst().ifPresent(element -> element.sendKeys(description));

        clickFirst(By.cssSelector("button[type='submit']"),
                By.xpath("//button[contains(normalize-space(.), 'Create') or contains(normalize-space(.), 'Submit')]") );
        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains(projectKey + "-"),
                ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("input[placeholder='Summary'], input[aria-label='Summary']"))));
    }

    public void search(String query) {
        WebElement search = findFirst(
                By.cssSelector("input[placeholder*='Search issues'], input[aria-label*='Search issues'], input[data-test='issue-search']"),
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
        for (By selector : selectors) {
            var matches = driver.findElements(selector);
            if (!matches.isEmpty()) return wait.until(ExpectedConditions.elementToBeClickable(matches.get(0)));
        }
        return wait.until(ExpectedConditions.elementToBeClickable(selectors[0]));
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
