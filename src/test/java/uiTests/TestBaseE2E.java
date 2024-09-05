package uiTests;

import static utils.ConfigFactory.HEADLESS;

import com.microsoft.playwright.*;
import java.nio.file.Paths;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public class TestBaseE2E {
    // Shared between all tests in this class.
    static Playwright playwright;
    static Browser browser;

    // New instance for each test method.
    static BrowserContext context;
    public Page page;

    @BeforeAll
    static void setupBrowser() {
        playwright = Playwright.create();
        browser = playwright
                .chromium()
                .launch(new BrowserType.LaunchOptions().setHeadless(HEADLESS).setTimeout(30_000));
    }

    @AfterAll
    static void closeBrowser() {
        if (playwright != null) {
            playwright.close();
        }
    }

    @BeforeEach
    void setupContextAndPage() {
        context = browser.newContext(new Browser.NewContextOptions().setRecordVideoDir(Paths.get("videos/")));
        context.tracing()
                .start(new Tracing.StartOptions()
                        .setScreenshots(true)
                        .setSnapshots(true)
                        .setSources(true));
        page = context.newPage();
    }

    @AfterEach
    void closeContext() {
        if (context != null) {
            context.tracing().stop(new Tracing.StopOptions().setPath(Paths.get("playwright-report/trace.zip")));
            context.close();
        }
    }
}
