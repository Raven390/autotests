package uiTests;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.*;
import uiTests.pages.BasePage;

import static utils.ConfigFactory.*;

public class TestBaseE2E {

    // Shared between all tests in class.
    static Playwright playwright;
    static Browser browser;

    // New instance for each test method.
    BrowserContext context;

    @BeforeAll
    @DisplayName("Create browser for all tests in one class")
    static void launchBrowser() {
        playwright = Playwright.create();

        String browserName = BROWSER;
        if (browserName.equals("chromium")) {
            browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(HEADLESS));
        } else if (browserName.equals("firefox")) {
            browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(HEADLESS));
        } else if (browserName.equals("webkit")) {
            browser = playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(HEADLESS));
        } else {
            browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(HEADLESS));
        }
    }

    @AfterAll
    @DisplayName("Close browser after completing all tests in one class")
    static void closeBrowser() {
        playwright.close();
    }

    @BeforeEach
    @DisplayName("Create new context for each test inside one browser")
    void createContextAndPage() {
        context = browser.newContext(new Browser.NewContextOptions().setBaseURL(BASEURL));
        BasePage.setContext(context);
    }

    @AfterEach
    @DisplayName("Close (clear) context for each test inside one browser")
    void closeContext() {
        context.close();
    }
}