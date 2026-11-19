package ru.selenium.youtrack.config;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import org.junit.jupiter.api.Assertions;
import ru.selenium.youtrack.pages.HomePage;
import ru.selenium.youtrack.pages.LoginPage;

import java.util.UUID;

@ExtendWith(ScreenshotExtension.class)
public abstract class BaseUiTest {
    protected WebDriver driver;

    @BeforeEach
    void startBrowser() {
        driver = DriverFactory.create();
    }

    @AfterEach
    void stopBrowser() {
        DriverFactory.quit();
    }

    protected String baseUrl() {
        return TestEnvironment.get("youtrack.url", "YOUTRACK_URL", "http://localhost:8080");
    }

    protected String username() {
        return TestEnvironment.get("youtrack.username", "YOUTRACK_USERNAME", "");
    }

    protected String password() {
        return TestEnvironment.get("youtrack.password", "YOUTRACK_PASSWORD", "");
    }

    protected String projectKey() {
        return TestEnvironment.get("youtrack.project", "YOUTRACK_PROJECT", "DEMO");
    }

    protected void signIn() {
        Assertions.assertFalse(username().isBlank(), "Set YOUTRACK_USERNAME before running authenticated UI tests");
        Assertions.assertFalse(password().isBlank(), "Set YOUTRACK_PASSWORD before running authenticated UI tests");
        new LoginPage(driver).open(baseUrl()).login(username(), password());
        Assertions.assertTrue(new HomePage(driver).isLoaded(), "The configured user should sign in to YouTrack");
    }

    protected String uniqueSummary(String template) {
        return template + " " + UUID.randomUUID().toString().substring(0, 8);
    }
}
