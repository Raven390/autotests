package tests;

import static helpers.database.DbHelper.startSshTunnel;
import static helpers.database.DbHelper.stopSshTunnel;
import static utils.ConfigFactory.*;

import com.microsoft.playwright.*;
import java.io.IOException;
import java.nio.file.Paths;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import pageObjects.backofficePages.*;
import pageObjects.vantageUserAccountPages.StageRegistrationHelperPage;
import pageObjects.vantageUserAccountPages.VantageUserAccountPage;
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

    public StageRegistrationHelperPage stageRegistrationHelperPage;
    public VantageUserAccountPage vantageUserAccountPage;
    public InvestigationPage investigationPage;
    public KeycloackPage keycloackPage;
    public ProfilePage profilePage;
    public RestrictionPage restrictionPage;
    public ConnectionPage connectionPage;
    public GeneralPage generalPage;
    public ResolveScreen resolveScreen;
    public TradingPage tradingPage;

    @BeforeAll
    static void setupBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(getHeadless()).setTimeout(
                TIMEOUT));
        startSshTunnel();
    }

    @AfterAll
    static void closeBrowser() {
        if (playwright != null) {
            playwright.close();
        }
        stopSshTunnel();
    }

    @BeforeEach
    void setupContextAndPage() {
        context = browser.newContext(new Browser.NewContextOptions().setRecordVideoDir(Paths.get(PATH_TRACE_VIDEO)));
        context.tracing().start(new Tracing.StartOptions().setScreenshots(true).setSnapshots(true).setSources(true));
        page = context.newPage();

        // Core team pages
        stageRegistrationHelperPage = new StageRegistrationHelperPage(page);
        vantageUserAccountPage = new VantageUserAccountPage(page);
        // Back office team pages
        investigationPage = new InvestigationPage(page);
        keycloackPage = new KeycloackPage(page);
        profilePage = new ProfilePage(page);
        restrictionPage = new RestrictionPage(page);
        connectionPage = new ConnectionPage(page);
        generalPage = new GeneralPage(page);
        resolveScreen = new ResolveScreen(page);
        tradingPage = new TradingPage(page);
    }

    @AfterEach
    void closeContext() throws IOException {
        String traceName = timestamp + n;
        if (context != null) {
            context.tracing().stop(new Tracing.StopOptions().setPath(Paths.get(PATH_TRACE + traceName + ".zip")));
            n += 1;
            // Start attach
            TestUtils.attachPlaywrightTrace(traceName);
            TestUtils.attachScreenshot(page);
            // End attach
            context.close();
        }
    }
}
