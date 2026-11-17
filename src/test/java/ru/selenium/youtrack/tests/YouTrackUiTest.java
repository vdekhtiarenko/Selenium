package ru.selenium.youtrack.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.selenium.youtrack.config.BaseUiTest;
import ru.selenium.youtrack.data.CsvData;
import ru.selenium.youtrack.pages.HomePage;
import ru.selenium.youtrack.pages.IssuesPage;
import ru.selenium.youtrack.pages.LoginPage;

import java.util.UUID;
import java.util.stream.Stream;

class YouTrackUiTest extends BaseUiTest {
    static Stream<Arguments> loginCases() {
        return CsvData.rows("testdata/login.csv").stream()
                .map(row -> Arguments.of(row[0], row[1], row[2], Boolean.parseBoolean(row[3])));
    }

    static Stream<Arguments> issueCases() {
        return CsvData.rows("testdata/issues.csv").stream()
                .map(row -> Arguments.of(row[0], row[1]));
    }

    @ParameterizedTest(name = "Вход: {0}")
    @MethodSource("loginCases")
    void loginHandlesValidAndInvalidCredentials(String scenario, String login, String secret, boolean expectedSuccess) {
        Assertions.assertFalse(login.isBlank(), "Задайте YOUTRACK_USERNAME для входа");
        LoginPage loginPage = new LoginPage(driver).open(baseUrl());
        loginPage.login(login, secret);

        if (expectedSuccess) {
            Assertions.assertTrue(new HomePage(driver).isLoaded(), "После успешного входа должна открыться YouTrack");
        } else {
            Assertions.assertTrue(loginPage.hasLoginError(), "Неверные учетные данные должны быть отклонены");
        }
    }

    @Test
    void projectsPageShowsConfiguredProject() {
        signIn();
        HomePage home = new HomePage(driver);
        home.openProjects(baseUrl());
        Assertions.assertTrue(home.containsProject("Demo project"),
                "В списке проектов должен присутствовать проект " + projectKey());
    }

    @ParameterizedTest(name = "Создание задачи: {0}")
    @MethodSource("issueCases")
    void userCanCreateIssue(String summaryTemplate, String description) {
        signIn();
        String summary = uniqueSummary(summaryTemplate);
        new IssuesPage(driver).open(baseUrl());
        IssuesPage issues = new IssuesPage(driver);
        issues.createIssue(projectKey(), summary, description);
        Assertions.assertTrue(issues.showsIssue(summary), "Созданная задача должна отображаться в интерфейсе");
    }

    @ParameterizedTest(name = "Поиск задачи: {0}")
    @MethodSource("issueCases")
    void userCanFindIssueBySummary(String summaryTemplate, String description) {
        signIn();
        String summary = uniqueSummary(summaryTemplate);
        IssuesPage issues = new IssuesPage(driver);
        issues.open(baseUrl());
        issues.createIssue(projectKey(), summary, description);
        issues.open(baseUrl());
        issues.search(summary);
        Assertions.assertTrue(issues.showsIssue(summary), "Поиск должен вернуть созданную задачу");
    }

    private void signIn() {
        Assertions.assertFalse(username().isBlank(), "Задайте YOUTRACK_USERNAME");
        Assertions.assertFalse(password().isBlank(), "Задайте YOUTRACK_PASSWORD");
        new LoginPage(driver).open(baseUrl()).login(username(), password());
        Assertions.assertTrue(new HomePage(driver).isLoaded(), "Не удалось войти в YouTrack");
    }

    private String uniqueSummary(String template) {
        return template + " " + UUID.randomUUID().toString().substring(0, 8);
    }
}
