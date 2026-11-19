package ru.selenium.youtrack.config;

import org.junit.jupiter.api.Assertions;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public final class TestEnvironment {
    private static final Map<String, String> DOTENV = loadDotenv();

    private TestEnvironment() {
    }

    public static String get(String systemProperty, String environmentVariable, String defaultValue) {
        String value = System.getProperty(systemProperty);
        if (isBlank(value)) value = System.getenv(environmentVariable);
        if (isBlank(value)) value = DOTENV.get(environmentVariable);
        return isBlank(value) ? defaultValue : value;
    }

    public static String getEnvironmentValue(String environmentVariable) {
        String systemProperty = environmentVariable.toLowerCase().replace('_', '.');
        return get(systemProperty, environmentVariable, "");
    }

    public static void requireYouTrackCredentials() {
        Assertions.assertFalse(get("youtrack.username", "YOUTRACK_USERNAME", "").isBlank(),
                "Set YOUTRACK_USERNAME before running authenticated UI tests");
        Assertions.assertFalse(get("youtrack.password", "YOUTRACK_PASSWORD", "").isBlank(),
                "Set YOUTRACK_PASSWORD before running authenticated UI tests");
    }

    private static Map<String, String> loadDotenv() {
        Path dotenvFile = Path.of(System.getProperty("user.dir"), ".env");
        if (!Files.isRegularFile(dotenvFile)) return Map.of();

        Map<String, String> values = new HashMap<>();
        try {
            for (String rawLine : Files.readAllLines(dotenvFile, StandardCharsets.UTF_8)) {
                String line = rawLine.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;

                int separator = line.indexOf('=');
                if (separator < 1) continue;

                String key = line.substring(0, separator).trim();
                String value = line.substring(separator + 1).trim();
                if (value.length() >= 2 && isQuoted(value)) {
                    value = value.substring(1, value.length() - 1);
                }
                values.put(key, value);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read .env file: " + dotenvFile, e);
        }
        return Map.copyOf(values);
    }

    private static boolean isQuoted(String value) {
        char first = value.charAt(0);
        char last = value.charAt(value.length() - 1);
        return (first == '"' && last == '"') || (first == '\'' && last == '\'');
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
