package ru.selenium.youtrack.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.selenium.youtrack.config.BaseUiTest;
import ru.selenium.youtrack.data.CsvData;
import ru.selenium.youtrack.pages.IssuesPage;

import java.util.stream.Stream;

class IssueUiTest extends BaseUiTest {
    static Stream<Arguments> issueCases() {
        return CsvData.rows("testdata/issues.csv").stream()
                .map(row -> Arguments.of(row[0], row[1]));
    }

    @ParameterizedTest(name = "Create issue: {0}")
    @MethodSource("issueCases")
    void userCanCreateAndFindIssueBySummary(String summaryTemplate, String description) {
        signIn();
        String summary = uniqueSummary(summaryTemplate);
        IssuesPage issues = new IssuesPage(driver);
        issues.open(baseUrl());
        issues.createIssue(summary, description);
        issues.refresh();
        issues.search(summary);
        Assertions.assertTrue(issues.showsIssue(summary), "The issue should be found by its summary");
    }
}
