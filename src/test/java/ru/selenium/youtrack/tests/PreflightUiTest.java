package ru.selenium.youtrack.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.selenium.youtrack.config.TestEnvironment;
import ru.selenium.youtrack.pages.HomePage;
import ru.selenium.youtrack.pages.LoginPage;

class PreflightUiTest extends BaseUiTest {
    @Test
    void browserOpensYouTrack() {
        Assertions.assertFalse(driver.getTitle().isBlank(), "The YouTrack page should have a title after loading");
    }

    @Test
    void configuredUserCanSignIn() {
        TestEnvironment.requireYouTrackCredentials();
        new LoginPage(driver).login(username(), password());
        Assertions.assertTrue(new HomePage(driver).isLoaded(), "The configured user should sign in to YouTrack");
    }
}
