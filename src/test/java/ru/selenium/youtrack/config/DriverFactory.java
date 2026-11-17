package ru.selenium.youtrack.config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public final class DriverFactory {
    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    private DriverFactory() {
    }

    public static WebDriver create() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--window-size=1440,1000", "--disable-notifications");
        if (Boolean.parseBoolean(TestEnvironment.get("browser.headless", "BROWSER_HEADLESS", "true"))) {
            options.addArguments("--headless=new");
        }
        WebDriver driver = new ChromeDriver(options);
        DRIVER.set(driver);
        return driver;
    }

    public static WebDriver current() {
        return DRIVER.get();
    }

    public static void quit() {
        WebDriver driver = DRIVER.get();
        try {
            if (driver != null) {
                driver.quit();
            }
        } finally {
            DRIVER.remove();
        }
    }
}
