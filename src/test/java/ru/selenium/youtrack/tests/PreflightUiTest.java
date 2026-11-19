package ru.selenium.youtrack.tests;

import org.junit.jupiter.api.Test;
import ru.selenium.youtrack.config.BaseUiTest;

class PreflightUiTest extends BaseUiTest {
    @Test
    void configuredUserCanSignIn() {
        signIn();
    }
}
