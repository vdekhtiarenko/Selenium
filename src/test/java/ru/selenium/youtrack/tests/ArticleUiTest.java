package ru.selenium.youtrack.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.selenium.youtrack.pages.ArticlesPage;

import java.util.UUID;

class ArticleUiTest extends AuthenticatedUiTestSupport {
    @Test
    void userCanCreateAndOpenProjectArticleFromSidebar() {
        String title = "UI article " + UUID.randomUUID().toString().substring(0, 8);
        ArticlesPage articles = new ArticlesPage(driver).openProject(baseUrl(), "DEMO");
        articles.createArticle(title, "Created by Selenium UI test.");

        Assertions.assertTrue(articles.isListedInSidebar(title), "The new article should appear in the sidebar");
        articles.openFromSidebar(title);
        Assertions.assertTrue(articles.isArticleOpen(title), "The selected article should open");
    }
}
