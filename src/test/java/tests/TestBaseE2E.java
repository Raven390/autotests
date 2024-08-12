package tests;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import pages.BasePage;

import static config.ConfigFactory.BASEURL;


public class TestBaseE2E {

    // Shared between all tests in class.
    static Playwright playwright;
    static Browser browser;

    // New instance for each test method.
    BrowserContext context;

    @BeforeAll
    @DisplayName("create browser for all tests in one class")
    static void launchBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
    }

    @AfterAll
    @DisplayName("close browser after completing all tests in one class")
    static void closeBrowser() {
        playwright.close();
    }

    @BeforeEach
    @DisplayName("create new context for each test inside one browser")
    void createContextAndPage() {
        context = browser.newContext(new Browser.NewContextOptions().setBaseURL(BASEURL));
        BasePage.setContext(context);
    }

    @AfterEach
    @DisplayName("close (clear) context for each test inside one browser")
    void closeContext() {
        context.close();
    }
}


