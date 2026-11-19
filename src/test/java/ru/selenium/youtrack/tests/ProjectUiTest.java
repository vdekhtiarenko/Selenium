package ru.selenium.youtrack.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.selenium.youtrack.pages.HomePage;

class ProjectUiTest extends AuthenticatedUiTestSupport {
    @Test
    void projectsPageShowsConfiguredProject() {
        HomePage home = new HomePage(driver);
        home.openProjects(baseUrl());
        Assertions.assertTrue(home.containsProject("Demo project"),
                "The configured project should appear on the projects page: " + projectKey());
    }
}
