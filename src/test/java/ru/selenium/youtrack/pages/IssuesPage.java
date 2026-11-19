package ru.selenium.youtrack.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class IssuesPage extends BasePage {
    private static final By CREATE_ISSUE = By.xpath("//a[@data-test='createIssueButton']");
    private static final By SUMMARY = By.xpath("//textarea[@data-test='summary']");
    private static final By DESCRIPTION = By.xpath("//*[@data-test='wysiwyg-editor-content' and @contenteditable='true']");
    private static final By SUBMIT = By.xpath("//button[@data-test='submit-button' or ((normalize-space(.)='Create' or normalize-space(.)='Submit') and not(@data-test='ring-link'))]");
    private static final By ISSUE_DIALOG = By.xpath("//div[@role='dialog' and @data-test='ring-island ring-dialog']");
    private static final By ISSUE_DIALOG_SUMMARY = By.xpath("//div[@role='dialog' and @data-test='ring-island ring-dialog']//h1[@data-test='ticket-summary']");

    public IssuesPage(WebDriver driver) {
        super(driver);
    }

    public void open(String baseUrl) {
        driver.get(baseUrl + "/issues");
        wait.until(ExpectedConditions.presenceOfElementLocated(CREATE_ISSUE));
    }

    public void createIssue(String summary, String description) {
        clickable(CREATE_ISSUE).click();
        visible(SUMMARY).sendKeys(summary);
        for (WebElement editor : driver.findElements(DESCRIPTION)) {
            if (editor.isDisplayed() && editor.isEnabled()) {
                editor.sendKeys(description);
                break;
            }
        }
        clickable(SUBMIT).click();
        visible(ISSUE_DIALOG);
        wait.until(ExpectedConditions.textToBePresentInElementLocated(ISSUE_DIALOG_SUMMARY, summary));
    }

}
