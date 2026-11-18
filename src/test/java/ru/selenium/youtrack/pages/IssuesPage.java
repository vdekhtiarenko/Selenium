package ru.selenium.youtrack.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class IssuesPage extends BasePage {
    private static final By CREATE_ISSUE = By.xpath("//a[@data-test='createIssueButton']");
    private static final By SUMMARY = By.xpath("//textarea[@data-test='summary']");
    private static final By DESCRIPTION = By.xpath("//*[@data-test='wysiwyg-editor-content' and @contenteditable='true']");
    private static final By SUBMIT = By.xpath("//button[@data-test='submit-button' or ((normalize-space(.)='Create' or normalize-space(.)='Submit') and not(@data-test='ring-link'))]");
    private static final By SEARCH = By.xpath("//input[@placeholder='Search for text or add a filter' or contains(@placeholder, 'Search') or contains(@aria-label, 'Search')]");
    private static final String ISSUE_SUMMARY = "//*[contains(normalize-space(.), %s)]";

    public IssuesPage(WebDriver driver) {
        super(driver);
    }

    public void open(String baseUrl) {
        driver.get(baseUrl + "/issues");
        wait.until(ExpectedConditions.presenceOfElementLocated(BODY));
    }

    public void createIssue(String projectKey, String summary, String description) {
        clickable(CREATE_ISSUE).click();
        visible(SUMMARY).sendKeys(summary);
        for (WebElement editor : driver.findElements(DESCRIPTION)) {
            if (editor.isDisplayed() && editor.isEnabled()) {
                editor.sendKeys(description);
                break;
            }
        }
        clickable(SUBMIT).click();
        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains(projectKey + "-"),
                ExpectedConditions.invisibilityOfElementLocated(SUMMARY)));
    }

    public void search(String query) {
        WebElement search = visible(SEARCH);
        search.clear();
        search.sendKeys(query, Keys.ENTER);
    }

    public boolean showsIssue(String summary) {
        try {
            return visible(By.xpath(ISSUE_SUMMARY.formatted(xpathLiteral(summary)))).isDisplayed();
        } catch (org.openqa.selenium.TimeoutException ignored) {
            return false;
        }
    }
}
