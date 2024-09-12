package utils;

import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestUtils {
    public static void attachScreenshot(Page page) throws IOException {
        String resultName = Utils.getCurrentDateTime();
        Path screenshotPath = Paths.get("screenshots/" + resultName + ".png");

        page.screenshot(new Page.ScreenshotOptions().setPath(screenshotPath).setFullPage(true));
        attachScreenshotToAllureReport(resultName, screenshotPath);
    }

    public static void attachPlaywrightTrace(String traceName) throws IOException {
        Path tracePath = Paths.get("playwright-report/trace" + traceName + ".zip");
        attachPlaywrightTraceToAllureReport(traceName, tracePath);
    }

    @Attachment(type = "other/zip")
    private static void attachPlaywrightTraceToAllureReport(String resultName, Path tracePath) throws IOException {
        Allure.addAttachment(resultName, new ByteArrayInputStream(Files.readAllBytes(tracePath)));
    }

    @Attachment(type = "image/png")
    private static void attachScreenshotToAllureReport(String resultName, Path screenshotPath) throws IOException {
        Allure.addAttachment(resultName, new ByteArrayInputStream(Files.readAllBytes(screenshotPath)));
    }
}
