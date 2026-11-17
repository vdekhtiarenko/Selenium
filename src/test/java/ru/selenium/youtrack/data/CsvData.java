package ru.selenium.youtrack.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public final class CsvData {
    private CsvData() {
    }

    public static List<String[]> rows(String resource) {
        InputStream stream = CsvData.class.getClassLoader().getResourceAsStream(resource);
        if (stream == null) throw new IllegalArgumentException("CSV resource not found: " + resource);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            return reader.lines()
                    .filter(line -> !line.isBlank() && !line.startsWith("case,") && !line.startsWith("summary,"))
                    .map(CsvData::parseRow)
                    .toList();
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read CSV resource: " + resource, e);
        }
    }

    private static String[] parseRow(String line) {
        return java.util.Arrays.stream(line.split(",", -1))
                .map(CsvData::expandEnvironmentVariable)
                .toArray(String[]::new);
    }

    private static String expandEnvironmentVariable(String value) {
        if (value.startsWith("${") && value.endsWith("}")) {
            String variable = value.substring(2, value.length() - 1);
            String resolved = System.getProperty(variable.toLowerCase().replace('_', '.'));
            if (resolved == null || resolved.isBlank()) resolved = System.getenv(variable);
            return resolved == null ? "" : resolved;
        }
        return value;
    }
}
