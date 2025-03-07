package tests.vindexBackofficeUiTests;

import businessObjects.db.clickhouse.accountIbRelation.AccountIbRelationObject;
import businessObjects.db.clickhouse.crmTbAccount.CrmTbAccountObject;
import businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObject;
import businessObjects.db.clickhouse.ibSummaryLifetime.IbSummaryLifetimeObject;
import businessObjects.db.clickhouse.mtAccount.MtAccountObject;
import businessObjects.db.ibSummaryByDate.IbSummaryByDateObject;
import businessObjects.kafka.alerts.RuleAlert;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.data.ClientHelper;
import helpers.kafka.KafkaHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Muted;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

import java.math.RoundingMode;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;

import static businessObjects.db.clickhouse.accountIbRelation.AccountIbRelationFactory.generateAccountIbRelationObjectByClient;
import static businessObjects.db.clickhouse.crmTbAccount.CrmTbAccountObjectFactory.generateCrmTbAccountDataForUi;
import static businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObjectFactory.generateUserByClient;
import static businessObjects.db.clickhouse.mtAccount.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static businessObjects.db.ibSummaryByDate.IbSummaryByDateFactory.generateIbSummaryByDateByClient;
import static businessObjects.kafka.alerts.RuleAlertFactory.generateRuleAlertByUcid;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.DateTimeFormat.DATE;
import static helpers.database.BoHelper.closeAlert;
import static helpers.database.CleanTableHelper.cleanCrmUserTableByClient;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.matchesPattern;
import static utils.Constants.*;
import static utils.Utils.getCurrentTimestampDbFormat;
import static utils.Utils.getCurrentTimestampMinusOffsetFormatted;

@Disabled
@Muted
@Tag(TAG_MANUAL)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class IbOverviewChartTest extends TestBaseWeb {

    private static final KafkaHelper kafka = new KafkaHelper();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final ClientHelper client = getRandomVantageClientAllFields();
    private static final ClientHelper ibClient = getRandomVantageClientAllFields();
    private static final CrmTbUserObject crmTbUser = generateUserByClient(client);
    private static final CrmTbUserObject ibCrmTbUser = generateUserByClient(ibClient);
    private static final CrmTbAccountObject account = generateCrmTbAccountDataForUi(client);
    private static final CrmTbAccountObject ibAccount = generateCrmTbAccountDataForUi(ibClient);
    private static final MtAccountObject mtAccount = generateMtAccountByCrmTbAccount(account);
    private static final MtAccountObject ibMtAccount = generateMtAccountByCrmTbAccount(ibAccount);
    private static final IbSummaryByDateObject ibSummaryByDate1 = generateIbSummaryByDateByClient(ibClient);
    private static final IbSummaryByDateObject ibSummaryByDate2 = generateIbSummaryByDateByClient(ibClient);
    private static final IbSummaryByDateObject ibSummaryByDate3 = generateIbSummaryByDateByClient(ibClient);
    private static final IbSummaryByDateObject ibSummaryByDate4 = generateIbSummaryByDateByClient(ibClient);
    private static final IbSummaryByDateObject ibSummaryByDate5 = generateIbSummaryByDateByClient(ibClient);
    private static final IbSummaryByDateObject ibSummaryByDate6 = generateIbSummaryByDateByClient(ibClient);
    private static final IbSummaryByDateObject ibSummaryByDate7 = generateIbSummaryByDateByClient(ibClient);
    private static final DecimalFormat formatter = new DecimalFormat("#,###.#");
    private static final String MONTH_DAY_LABEL_PATTERN = "^(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec) \\d{2}$";
    private static final String MONTH_YEAR_LABEL_PATTERN = "^(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec) \\d{4}$";

    @BeforeAll
    public static void setup() throws ReflectiveOperationException, SQLException, JsonProcessingException {
        formatter.setMinimumFractionDigits(0);
        formatter.setMaximumFractionDigits(2);
        formatter.setRoundingMode(RoundingMode.DOWN);
        insertObjectsToDb(CRM_USER_TABLE_NAME, List.of(crmTbUser, ibCrmTbUser));
        insertObjectsToDb(CRM_ACCOUNT_TABLE_NAME, List.of(account, ibAccount));
        insertObjectsToDb(MT_ACCOUNT_TABLE_NAME, List.of(mtAccount, ibMtAccount));
        AccountIbRelationObject relation1 = generateAccountIbRelationObjectByClient(client);
        relation1.setDirectIbRebateAccount(ibAccount.account);
        insertObjectToDb(ACCOUNT_IB_RELATION_TABLE_NAME, relation1);
        IbSummaryLifetimeObject ibSummaryLifetime = new IbSummaryLifetimeObject(
                1, ibClient.getUserId(), ibClient.getBrand(), ibClient.getRegulator(), ibClient.getUcid(), "serverName", 2, 123L, ibClient.getTradingAccount().longValue(), 111, 56, 13, -97_831.978, 1123.33, -78_878.32, 5000.12, 2003.0, 2997.12, 42_124.93, 5443.3, getCurrentTimestampDbFormat()
        );
        insertObjectToDb(IB_SUMMARY_LIFETIME_TABLE_NAME, ibSummaryLifetime);
        ibSummaryByDate1.netDeposit = 142.342;
        ibSummaryByDate1.netPnl = 878.23;
        ibSummaryByDate1.rebate = 235.0;
        ibSummaryByDate2.date = getCurrentTimestampMinusOffsetFormatted(DATE, 0, 0, 1, 0, 0);
        ibSummaryByDate2.netDeposit = 253.1;
        ibSummaryByDate2.netPnl = -985.45;
        ibSummaryByDate2.rebate = 854.37;
        ibSummaryByDate3.date = getCurrentTimestampMinusOffsetFormatted(DATE, 0, 0, 2, 0, 0);
        ibSummaryByDate3.netDeposit = 495.53;
        ibSummaryByDate3.netPnl = -649.243;
        ibSummaryByDate3.rebate = 352.08;
        ibSummaryByDate4.date = getCurrentTimestampMinusOffsetFormatted(DATE, 0, 0, 3, 0, 0);
        ibSummaryByDate4.netDeposit = 976.745;
        ibSummaryByDate4.netPnl = 754.68;
        ibSummaryByDate4.rebate = 98.68;
        ibSummaryByDate5.date = getCurrentTimestampMinusOffsetFormatted(DATE, 0, 0, 4, 0, 0);
        ibSummaryByDate5.netDeposit = 35.45;
        ibSummaryByDate5.netPnl = 453.2;
        ibSummaryByDate5.rebate = 344.22;
        insertObjectsToDb(IB_SUMMARY_BY_DATE_TABLE_NAME, List.of(ibSummaryByDate1, ibSummaryByDate2, ibSummaryByDate3, ibSummaryByDate4, ibSummaryByDate5));
        RuleAlert alert = generateRuleAlertByUcid(crmTbUser.ucid);
        kafka.produceMessage(alert.alertId, objectMapper.writeValueAsString(alert), KAFKA_TOPIC_ALERTS);
    }

    @Order(1)
    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1041")
    @DisplayName("Verify IB overview Chart by days")
    public void verifyIbOverviewChart1Test() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        generalTab.clickGeneralTabButton();
        generalTab.clickIbOverviewButton();
        ibOverviewPage.waitForPageToLoad();
        assertThat("Verify Y axis label", ibOverviewPage.getChartYAxisLabel(), is("1k"));
        assertThat("Verify X axis labels", ibOverviewPage.getChartXAxisLabels(), everyItem(matchesPattern(MONTH_DAY_LABEL_PATTERN)));
    }

    @Order(2)
    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1042")
    @DisplayName("Verify IB overview Chart by days with empty data")
    public void verifyIbOverviewChart2Test() {
        ibSummaryByDate6.date = getCurrentTimestampMinusOffsetFormatted(DATE, 0, 0, 119, 0, 0);
        ibSummaryByDate6.netDeposit = 4124.498;
        ibSummaryByDate6.netPnl = -12.123;
        ibSummaryByDate6.rebate = 2444.87;
        insertObjectToDb(IB_SUMMARY_BY_DATE_TABLE_NAME, ibSummaryByDate6);
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        generalTab.clickGeneralTabButton();
        generalTab.clickIbOverviewButton();
        ibOverviewPage.waitForPageToLoad();
        assertThat("Verify Y axis label", ibOverviewPage.getChartYAxisLabel(), is("5k"));
        assertThat("Verify X axis labels", ibOverviewPage.getChartXAxisLabels(), everyItem(matchesPattern(MONTH_DAY_LABEL_PATTERN)));
    }

    @Order(3)
    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1043")
    @DisplayName("Verify IB overview Chart by months")
    public void verifyIbOverviewChart3Test() {
        ibSummaryByDate7.date = getCurrentTimestampMinusOffsetFormatted(DATE, 0, 0, 121, 0, 0);
        ibSummaryByDate7.netDeposit = 32_456.654;
        ibSummaryByDate7.netPnl = -9877.12;
        ibSummaryByDate7.rebate = 5448.56;
        insertObjectToDb(IB_SUMMARY_BY_DATE_TABLE_NAME, ibSummaryByDate7);
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        generalTab.clickGeneralTabButton();
        generalTab.clickIbOverviewButton();
        ibOverviewPage.waitForPageToLoad();
        assertThat("Verify Y axis label", ibOverviewPage.getChartYAxisLabel(), is("40k"));
        assertThat("Verify X axis labels", ibOverviewPage.getChartXAxisLabels(), everyItem(matchesPattern(MONTH_YEAR_LABEL_PATTERN)));
    }

    @AfterAll
    public static void teardown() throws Exception {
        cleanCrmUserTableByClient(crmTbUser.ucid, ibCrmTbUser.ucid);
        deleteEntryFromDb(ACCOUNT_IB_RELATION_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        deleteEntryFromDb(IB_SUMMARY_BY_DATE_TABLE_NAME, String.format("ucid = '%s'", ibClient.getUcid()));
        deleteEntryFromDb(IB_SUMMARY_LIFETIME_TABLE_NAME, String.format("ucid = '%s'", ibClient.getUcid()));
        closeAlert(crmTbUser.ucid);
    }
}
