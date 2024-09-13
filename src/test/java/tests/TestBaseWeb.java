package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.ConfigFactory.TIMEOUT;
import static utils.ConfigFactory.getHeadless;

import com.github.romankh3.image.comparison.ImageComparison;
import com.github.romankh3.image.comparison.ImageComparisonUtil;
import com.github.romankh3.image.comparison.model.ImageComparisonResult;
import com.github.romankh3.image.comparison.model.ImageComparisonState;
import com.microsoft.playwright.*;
import io.qameta.allure.Step;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import utils.TestResultWatcher;
import utils.TestUtils;
import utils.Utils;

@ExtendWith(TestResultWatcher.class)
public class TestBaseWeb {
    // Shared between all tests in this class.
    public String timestamp = String.valueOf(Utils.getCurrentTimestamp());
    static int n = 1;
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
                .launch(new BrowserType.LaunchOptions()
                        .setHeadless(getHeadless())
                        .setTimeout(TIMEOUT));
    }

    @AfterAll
    static void closeBrowser() {
        if (playwright != null) {
            playwright.close();
        }
    }

    @BeforeEach
    void setupContextAndPage() {
        context =
                browser.newContext(new Browser.NewContextOptions().setRecordVideoDir(Paths.get("test-output/videos/")));
        context.tracing()
                .start(new Tracing.StartOptions()
                        .setScreenshots(true)
                        .setSnapshots(true)
                        .setSources(true));
        page = context.newPage();
    }

    @AfterEach
    void closeContext() throws IOException {
        String traceName = timestamp + n;
        if (context != null) {
            context.tracing()
                    .stop(new Tracing.StopOptions().setPath(Paths.get("playwright-report/trace" + traceName + ".zip")));
            n += 1;

            // Start attach
            TestUtils.attachPlaywrightTrace(traceName);
            TestUtils.attachScreenshot(page);
            // End attach

            context.close();
        }
    }

    @Step("compare page with etalon screenshot")
    public void comparePageScreenshot(String pathToEtalon) {
        isLoaded();
        page.waitForTimeout(2000);
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("screenshot.png")));
        BufferedImage expectedImage = ImageComparisonUtil.readImageFromResources(pathToEtalon);
        BufferedImage actualImage = ImageComparisonUtil.readImageFromResources("screenshot.png");
        File resultDestination = new File("result" + String.valueOf(Utils.getCurrentTimestamp()) + ".png");
        ImageComparisonResult imageComparisonResult =
                new ImageComparison(expectedImage, actualImage, resultDestination).compareImages();
        assertEquals(ImageComparisonState.MATCH, imageComparisonResult.getImageComparisonState());
    }

    @Step("check if the page loaded")
    public void isLoaded() {
        int n = 0;
        page.waitForTimeout(500);
        while (page.locator(".v-loader").isVisible() && n < 8)
            ;
        {
            page.waitForTimeout(2000);
            n += 1;
        }
    }
}
