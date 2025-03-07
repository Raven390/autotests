package tests.vindexBackofficeUiTests;

import businessObjects.db.clickhouse.accountIbRelation.AccountIbRelationObject;
import businessObjects.db.clickhouse.crmTbAccount.CrmTbAccountObject;
import businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObject;
import businessObjects.db.clickhouse.mtAccount.MtAccountObject;
import businessObjects.db.clickhouse.s3FactIbSalesCommissions.S3FactIbSalesCommissionsObject;
import businessObjects.db.clickhouse.s3FactLoginMetrics.S3FactLoginMetricsObject;
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
import static businessObjects.db.clickhouse.s3FactIbSalesCommissions.S3FactIbSalesCommissionsFactory.generateS3FactIbSalesCommissionsClient;
import static businessObjects.db.clickhouse.s3FactLoginMetrics.S3FactLoginMetricsFactory.generateS3FactLoginMetricsClient;
import static businessObjects.kafka.alerts.RuleAlertFactory.generateRuleAlertByUcid;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.BoHelper.closeAlert;
import static helpers.database.CleanTableHelper.cleanCrmUserTableByClient;
import static helpers.database.DbHelper.*;
import static utils.Constants.*;
import static utils.Utils.*;

@Disabled
@Muted
@Tag(TAG_MANUAL)
public class IbOverviewSummaryTest extends TestBaseWeb {

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
    private static final DecimalFormat formatter = new DecimalFormat("#,###.#");

    @BeforeAll
    public static void setup() throws ReflectiveOperationException, SQLException, JsonProcessingException {
        insertObjectsToDb(CRM_USER_TABLE_NAME, List.of(crmTbUser, ibCrmTbUser));
        insertObjectsToDb(CRM_ACCOUNT_TABLE_NAME, List.of(account, ibAccount));
        insertObjectsToDb(MT_ACCOUNT_TABLE_NAME, List.of(mtAccount, ibMtAccount));
        // Relations
        AccountIbRelationObject relation1 = generateAccountIbRelationObjectByClient(client);
        relation1.setDirectIbRebateAccount(ibAccount.account);
        insertObjectToDb(ACCOUNT_IB_RELATION_TABLE_NAME, relation1);
        // Commissions
        S3FactIbSalesCommissionsObject commission1 = generateS3FactIbSalesCommissionsClient(client);
        commission1.setIbRebateAccount(ibAccount.account);
        commission1.setSalesCommission(12.34);
        commission1.setIbCommission(56.789);
        insertObjectsToDb(S3_FACT_IB_SALES_COMMISSIONS, List.of(commission1));
        // Metrics
        S3FactLoginMetricsObject historyMetrics1 = generateS3FactLoginMetricsClient(ibClient);
        historyMetrics1.setDate(getCurrentDate());
        historyMetrics1.setDailyNetClosedPnl(getRandomRoundedDouble(0, 555_555));
        insertObjectsToDb(S3_FACT_LOGIN_METRICS_TABLE_NAME, List.of(historyMetrics1));

        RuleAlert alert = generateRuleAlertByUcid(crmTbUser.ucid);
        kafka.produceMessage(alert.alertId, objectMapper.writeValueAsString(alert), KAFKA_TOPIC_ALERTS);
        formatter.setMinimumFractionDigits(0);
        formatter.setMaximumFractionDigits(2);
        formatter.setRoundingMode(RoundingMode.DOWN);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1034")
    @DisplayName("Verify IB overview Summary")
    public void verifyIbOverviewSummaryTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        generalTab.clickGeneralTabButton();
        generalTab.clickIbOverviewButton();
        ibOverviewPage.waitForPageToLoad();
//        assertThat("Verify IB overview summary title", ibOverviewPage.getIbOverviewTitle(), is("IB overview"));
//        assertThat("Verify IB overview summary subheader", ibOverviewPage.getIbOverviewSubheaderText(), is(String.format("IB %s, %s, %s level", ibAccount.account, ibAccount.brand, ibSummaryLifetime.ibLevel)));
//        assertThat("Verify IB overview summary under this ib title", ibOverviewPage.getUnderThisIbTitle(), is("Under this IB"));
//        assertThat("Verify IB overview summary under this ib items", ibOverviewPage.getUnderThisIbItems(), contains(String.format("%sclients", ibSummaryLifetime.directUsersCount), String.format("%sfraudsters", ibSummaryLifetime.fraudstersCount), String.format("%slower-level IB", ibSummaryLifetime.subIbsCount)));
//        assertThat("Verify IB overview summary clients performance title", ibOverviewPage.getClientsPerformanceTitle(), is("Clients performance USD"));
//        assertThat("Verify IB overview summary clients performance items", ibOverviewPage.getClientsPerformanceItems(), contains(String.format("%sIB rebates", formatter.format(ibSummaryLifetime.rebate)), String.format("%sNet PNL", formatter.format(ibSummaryLifetime.netPnl)), String.format("%sNet deposit", formatter.format(ibSummaryLifetime.netDeposit))));
//        assertThat("Verify IB overview summary clients totals title", ibOverviewPage.getClientsTotalsTitle(), is("Clients totals USD"));
//        assertThat("Verify IB overview summary clients totals items", ibOverviewPage.getClientsTotalsItems(), contains(String.format("%sVolume", formatter.format(ibSummaryLifetime.notionalValue)), String.format("%sProfit", formatter.format(ibSummaryLifetime.pnl)), String.format("%sEquity", formatter.format(ibSummaryLifetime.equity)), String.format("%sDeposit", formatter.format(ibSummaryLifetime.deposit)), String.format("%sWithdrawal", formatter.format(ibSummaryLifetime.withdrawal))));
        ibOverviewPage.clickIbOverviewSubheaderIcon();
        page.waitForTimeout(2000);
        PlaywrightAssertions.assertThat(page.context().pages().getLast()).hasURL(String.format("https://risktool.risk-vantagefx.com//rebate?server=serverName&login=%s", ibAccount.account));
    }


    @AfterAll
    public static void teardown() throws Exception {
        cleanCrmUserTableByClient(crmTbUser.ucid, ibCrmTbUser.ucid);
        deleteEntryFromDb(ACCOUNT_IB_RELATION_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        deleteEntryFromDb(IB_SUMMARY_LIFETIME_TABLE_NAME, String.format("ucid = '%s'", ibClient.getUcid()));
        closeAlert(crmTbUser.ucid);
    }
}
