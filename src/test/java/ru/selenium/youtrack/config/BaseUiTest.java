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
        return setting("youtrack.url", "YOUTRACK_URL", "http://localhost:8080");
    }

    protected String username() {
        return setting("youtrack.username", "YOUTRACK_USERNAME", "");
    }

    protected String password() {
        return setting("youtrack.password", "YOUTRACK_PASSWORD", "");
    }

    protected String projectKey() {
        return setting("youtrack.project", "YOUTRACK_PROJECT", "TEST");
    }

    private String setting(String property, String environmentVariable, String defaultValue) {
        String value = System.getProperty(property);
        if (value == null || value.isBlank()) {
            value = System.getenv(environmentVariable);
        }
        return value == null || value.isBlank() ? defaultValue : value;
    }
}
