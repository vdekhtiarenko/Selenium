package ru.selenium.youtrack.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ArticlesPage extends BasePage {
    private static final By NEW_ARTICLE = By.cssSelector("[data-test='articlesCreateButtonContainer']");
    private static final By ARTICLE_FORM = By.cssSelector("form[data-test='form']");
    private static final By TITLE = By.cssSelector("[data-test='summary']");
    private static final By CONTENT = By.cssSelector("[data-test='wysiwyg-editor-content']");
    private static final By PUBLISH = By.cssSelector("[data-test='publish-button']");
    private static final By ARTICLE_TITLE = By.cssSelector("main h1");

    public ArticlesPage(WebDriver driver) {
        super(driver);
    }

    public ArticlesPage openProject(String baseUrl, String projectKey) {
        driver.get(baseUrl + "/articles/" + projectKey);
        clickable(NEW_ARTICLE);
        return this;
    }

    public void createArticle(String title, String content) {
        clickable(NEW_ARTICLE).click();
        visible(ARTICLE_FORM);
        visible(TITLE).sendKeys(title);
        visible(CONTENT).sendKeys(content);
        clickable(PUBLISH).click();
        wait.until(ExpectedConditions.not(ExpectedConditions.visibilityOfElementLocated(ARTICLE_FORM)));
    }

    public boolean isListedInSidebar(String title) {
        By articleLink = sidebarArticleLink(title);
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(articleLink)).isDisplayed();
        } catch (org.openqa.selenium.TimeoutException ignored) {
            return false;
        }
    }

    public void openFromSidebar(String title) {
        clickable(sidebarArticleLink(title)).click();
        wait.until(ExpectedConditions.urlContains("/articles/"));
        wait.until(ExpectedConditions.textToBePresentInElementLocated(ARTICLE_TITLE, title));
    }

    public boolean isArticleOpen(String title) {
        try {
            return wait.until(ExpectedConditions.textToBePresentInElementLocated(ARTICLE_TITLE, title));
        } catch (org.openqa.selenium.TimeoutException ignored) {
            return false;
        }
    }

    private By sidebarArticleLink(String title) {
        return By.cssSelector("[data-test='left-sidebar'] [data-test~='article-summary'][data-test-title='"
                + title + "']");
    }
}
