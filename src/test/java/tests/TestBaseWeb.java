package tests;

import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static helpers.database.DbHelper.startSshTunnel;
import static helpers.database.DbHelper.stopSshTunnel;
import static utils.ConfigFactory.*;

import com.microsoft.playwright.*;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.DecimalFormat;

import net.datafaker.Faker;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import page_objects.backoffice_pages.abuseRegistry.DeductionPage;
import page_objects.backoffice_pages.abuseRegistry.FraudstersPage;
import page_objects.backoffice_pages.alertHistory.AlertHistoryPage;
import page_objects.backoffice_pages.investigationTool.*;
import page_objects.vantage_user_account_pages.StageRegistrationHelperPage;
import page_objects.vantage_user_account_pages.VantageUserAccountPage;
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
    protected static BrowserContext context;
    public Page page;

    public StageRegistrationHelperPage stageRegistrationHelperPage;
    public VantageUserAccountPage vantageUserAccountPage;
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
    public ActivityTab activityTab;
    public IbCpaOverviewPage ibCpaOverviewPage;
    public GeneralSearchElements generalSearch;
    public AlertHistoryPage alertHistoryPage;
    public static OpenPositions openPositions;
    public FraudstersPage fraudstersPage;
    public DeductionPage deductionPage;

    public static DecimalFormat df = new DecimalFormat("#,###");
    public static DecimalFormat dfd = new DecimalFormat("#,###.##");
    public static DecimalFormat dfwholed = new DecimalFormat("###,###,###");
    public static DecimalFormat decimalFormat = new DecimalFormat("###,###,###.##");

    public Faker faker = new Faker();

    @BeforeAll
    static void setupBrowser() throws IOException {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(getHeadless()).setTimeout(
                TIMEOUT));
        startSshTunnel();
        enableCRMEmulator();
    }

    @AfterAll
    static void closeBrowser() throws IOException {
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
        generalTab = new GeneralTab(page);
        resolvePage = new ResolvePage(page);
        tradingPage = new TradingPage(page);
        auditTrailPage = new AuditTrailPage(page);
        alertsPage = new AlertsPage(page);
        paymentsPage = new PaymentsPage(page);
        activityTab = new ActivityTab(page);
        ibCpaOverviewPage = new IbCpaOverviewPage(page);
        generalSearch = new GeneralSearchElements(page);
        openPositions = new OpenPositions(page);
        alertHistoryPage = new AlertHistoryPage(page);
        fraudstersPage = new FraudstersPage(page);
        deductionPage = new DeductionPage(page);
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
