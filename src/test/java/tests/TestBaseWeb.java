package tests;

import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static helpers.database.DbHelper.startSshTunnel;
import static helpers.database.DbHelper.stopSshTunnel;
import static utils.ConfigFactory.*;
import static utils.Utils.writeLog;

import com.microsoft.playwright.*;
import helpers.kafka.KafkaHelper;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import net.datafaker.Faker;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import page_objects.StageRegistrationHelperPage;
import page_objects.backoffice_pages.DutyTeamPortal.DutyTeamPage;
import page_objects.backoffice_pages.abuseRegistry.DeductionPage;
import page_objects.backoffice_pages.abuseRegistry.FraudstersPage;
import page_objects.backoffice_pages.alertHistory.AlertHistoryPage;
import page_objects.backoffice_pages.investigationTool.*;
import page_objects.backoffice_pages.search.GeneralSearchElements;
import page_objects.backoffice_pages.search.SearchPage;
import utils.ConfigFactory;
import utils.TestResultWatcher;
import utils.TestUtils;
import utils.Utils;

@ExtendWith(TestResultWatcher.class)
public class TestBaseWeb {
    // Shared between all tests in this class.
    public String timestamp = String.valueOf(Utils.getCurrentTimestampSeconds());
    static int n = 1;
    static Playwright playwright;
    static Browser browser;
    public KafkaHelper kafka = new KafkaHelper();

    // New instance for each test method.
    protected static BrowserContext context;
    public Page page;

    public StageRegistrationHelperPage stageRegistrationHelperPage;
    public InvestigationPage investigationPage;
    public KeycloackPage keycloackPage;
    public ProfilePage profilePage;
    public RestrictionPage restrictionPage;
    public ConnectionPage connectionPage;
    public GeneralTab generalTab;
    public ResolvePage resolvePage;
    public TradingPage tradingPage;
    public AuditTrailPage auditTrailPage;
    public AlertsPage alertsPage;
    public PaymentsPage paymentsPage;
    public SessionsTab sessionsTab;
    public IbCpaOverviewPage ibCpaOverviewPage;
    public GeneralSearchElements generalSearch;
    public AlertHistoryPage alertHistoryPage;
    public static OpenPositions openPositions;
    public FraudstersPage fraudstersPage;
    public DeductionPage deductionPage;
    public SearchPage searchPage;
    public DutyTeamPage dutyTeamPage;
    public AssignDrawer assignDrawer;

    public static DecimalFormat df = new DecimalFormat("#,###");
    public static DecimalFormat dfd = new DecimalFormat("#,###.##");
    public static DecimalFormat dfWholed = new DecimalFormat("###,###,###");
    public static DecimalFormat decimalFormat = new DecimalFormat("###,###,##0.##");

    public static Faker faker = new Faker();
    boolean debug = ConfigFactory.isDebugMode();

    @BeforeAll
    static void setupBrowser() throws IOException {
        playwright = Playwright.create();
        browser = playwright
                .chromium()
                .launch(new BrowserType.LaunchOptions()
                        .setHeadless(getHeadless())
                        .setTimeout(TIMEOUT));
        startSshTunnel();
        enableCRMEmulator();

        df.setMinimumFractionDigits(1);
        df.setMaximumFractionDigits(2);

        dfd.setMinimumFractionDigits(1);
        dfd.setMaximumFractionDigits(2);

        decimalFormat.setMinimumFractionDigits(1);
        decimalFormat.setMaximumFractionDigits(2);
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
        Browser.NewContextOptions options = new Browser.NewContextOptions().setViewportSize(1920, 1080);

        if (debug) {
            options.setRecordVideoDir(Paths.get(PATH_TRACE_VIDEO));
        }

        context = browser.newContext(options);
        context.tracing()
                .start(new Tracing.StartOptions()
                        .setScreenshots(true)
                        .setSnapshots(true)
                        .setSources(true));
        page = context.newPage();

        // Core team pages
        stageRegistrationHelperPage = new StageRegistrationHelperPage(page);
        // Back office team pages
        investigationPage = new InvestigationPage(page);
        keycloackPage = new KeycloackPage(page);
        profilePage = new ProfilePage(page);
        restrictionPage = new RestrictionPage(page);
        connectionPage = new ConnectionPage(page);
        generalTab = new GeneralTab(page);
        resolvePage = new ResolvePage(page);
        tradingPage = new TradingPage(page);
        auditTrailPage = new AuditTrailPage(page);
        alertsPage = new AlertsPage(page);
        paymentsPage = new PaymentsPage(page);
        sessionsTab = new SessionsTab(page);
        ibCpaOverviewPage = new IbCpaOverviewPage(page);
        generalSearch = new GeneralSearchElements(page);
        openPositions = new OpenPositions(page);
        alertHistoryPage = new AlertHistoryPage(page);
        fraudstersPage = new FraudstersPage(page);
        deductionPage = new DeductionPage(page);
        searchPage = new SearchPage(page);
        dutyTeamPage = new DutyTeamPage(page);
        assignDrawer = new AssignDrawer(page);
    }

    @AfterEach
    void closeContext() throws IOException {
        if (context == null) return;

        String traceName = timestamp + "-" + n;
        boolean failed = TestResultWatcher.isFailed();
        Video video;

        if (debug && page != null) {
            video = page.video();
        } else {
            video = null;
        }
        Path traceZip = Paths.get(PATH_TRACE + traceName + ".zip");

        try {
            // Tracing: run only in debug, save zip only on failure
            if (debug) {
                try {
                    if (failed) {
                        context.tracing().stop(new Tracing.StopOptions().setPath(traceZip));
                    } else {
                        context.tracing().stop(); // do not create zip
                    }
                } catch (Throwable t) {
                    // Don't fail teardown because of tracing
                    writeLog("Tracing stop failed: " + t.getMessage());
                }
            }

            // Allure: screenshot only on failure
            if (failed) {
                try {
                    TestUtils.attachScreenshot(page);
                } catch (Throwable t) {
                    writeLog("Failed to attach screenshot: " + t.getMessage());
                }
            }
        } finally {
            n += 1;

            // Close context first to finalize video on disk (if recorded)
            try {
                context.close();
            } catch (Throwable t) {
                writeLog("Failed to close context: " + t.getMessage());
            }

            // Keep video only for debug + failed; otherwise delete it
            if (debug && video != null && !failed) {
                deleteVideoQuietly(video);
            }
        }
    }

    private void deleteVideoQuietly(Video video) {
        try {
            Path videoPath = video.path();
            if (videoPath != null) {
                java.nio.file.Files.deleteIfExists(videoPath);
            }
        } catch (Throwable t) {
            writeLog("Failed to delete video: " + t.getMessage());
        }
    }
}
