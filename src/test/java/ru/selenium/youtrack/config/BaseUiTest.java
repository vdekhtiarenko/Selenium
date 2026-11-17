package ru.selenium.youtrack.config;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;

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
        return TestEnvironment.get("youtrack.project", "YOUTRACK_PROJECT", "TEST");
    }
}
