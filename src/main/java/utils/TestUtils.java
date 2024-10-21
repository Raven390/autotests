package utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.ConfigFactory.PATH_BASELINE_SCREENSHOT;
import static utils.ConfigFactory.PATH_TRACE;

import com.github.romankh3.image.comparison.ImageComparison;
import com.github.romankh3.image.comparison.ImageComparisonUtil;
import com.github.romankh3.image.comparison.model.ImageComparisonResult;
import com.github.romankh3.image.comparison.model.ImageComparisonState;
import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestUtils {
    public static void attachScreenshot(Page page) throws IOException {
        String resultName = Utils.getCurrentDateTime();
        Path screenshotPath = Paths.get("test-output/screenshots/" + resultName + ".png");

        page.screenshot(new Page.ScreenshotOptions().setPath(screenshotPath).setFullPage(true));
        attachScreenshotToAllureReport(resultName, screenshotPath);
    }

    public static void attachPlaywrightTrace(String traceName) throws IOException {
        Path tracePath = Paths.get(PATH_TRACE + traceName + ".zip");
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

    @Step("Compare page with baseline screenshot")
    public static void comparePageScreenshotWithBaseline(Page page, String baselineScreenshotName) {
        page.waitForTimeout(1000);
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("visual-comparsion/current-screenshots/screenshot.png")));
        BufferedImage expectedImage = ImageComparisonUtil.readImageFromResources(PATH_BASELINE_SCREENSHOT + baselineScreenshotName);
        BufferedImage actualImage = ImageComparisonUtil.readImageFromResources("visual-comparsion/current-screenshots/screenshot.png");
        File resultDestination = new File("visual-comparsion/result-screenshots/" + "result" + Utils.getCurrentTimestamp() + ".png");
        ImageComparisonResult imageComparisonResult = new ImageComparison(expectedImage, actualImage, resultDestination).compareImages();
        assertEquals(ImageComparisonState.MATCH, imageComparisonResult.getImageComparisonState());
    }
}
