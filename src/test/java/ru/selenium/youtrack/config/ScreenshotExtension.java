package ru.selenium.youtrack.config;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenshotExtension implements AfterTestExecutionCallback {
    private static final DateTimeFormatter TIMESTAMP = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss-SSS");

    @Override
    public void afterTestExecution(ExtensionContext context) throws IOException {
        if (context.getExecutionException().isEmpty()) {
            return;
        }
        WebDriver driver = DriverFactory.current();
        if (!(driver instanceof TakesScreenshot screenshotDriver)) {
            return;
        }
        Path directory = Path.of("target", "screenshots");
        Files.createDirectories(directory);
        String testName = context.getRequiredTestMethod().getName().replaceAll("[^a-zA-Z0-9_-]", "_");
        Path target = directory.resolve(testName + "-" + LocalDateTime.now().format(TIMESTAMP) + ".png");
        Files.write(target, screenshotDriver.getScreenshotAs(OutputType.BYTES));
        System.err.println("Screenshot saved to " + target.toAbsolutePath());
    }
}
