package ru.selenium.youtrack.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.selenium.youtrack.config.BaseUiTest;
import ru.selenium.youtrack.data.CsvData;
import ru.selenium.youtrack.pages.HomePage;
import ru.selenium.youtrack.pages.LoginPage;

import java.util.stream.Stream;

class LoginUiTest extends BaseUiTest {
    static Stream<Arguments> loginCases() {
        return CsvData.rows("testdata/login.csv").stream()
                .map(row -> Arguments.of(row[0], row[1], row[2], Boolean.parseBoolean(row[3])));
    }

    @ParameterizedTest(name = "Login scenario: {0}")
    @MethodSource("loginCases")
    void loginHandlesValidAndInvalidCredentials(String scenario, String login, String secret, boolean expectedSuccess) {
        Assertions.assertFalse(login.isBlank(), "Set YOUTRACK_USERNAME before running login tests");
        LoginPage loginPage = new LoginPage(driver).open(baseUrl());
        loginPage.login(login, secret);

        if (expectedSuccess) {
            Assertions.assertTrue(new HomePage(driver).isLoaded(), "Valid credentials should open YouTrack");
        } else {
            Assertions.assertTrue(loginPage.hasLoginError(), "Invalid credentials should display a login error");
        }
    }
}
