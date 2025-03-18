package tests.vindexBackofficeUiTests;

import businessObjects.db.clickhouse.accountIbRelation.AccountIbRelationObject;
import businessObjects.db.clickhouse.clientFraudTypes.ClientFraudTypes;
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
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static utils.Constants.*;
import static utils.Utils.*;

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
    private static AccountIbRelationObject relation;
    private static S3FactIbSalesCommissionsObject commission;
    private static S3FactLoginMetricsObject factLoginMetrics;
    private static final DecimalFormat formatter = new DecimalFormat("#,###.#");

    @BeforeAll
    public static void setup() throws ReflectiveOperationException, SQLException, JsonProcessingException {
        insertObjectsToDb(CRM_USER_TABLE_NAME, List.of(crmTbUser, ibCrmTbUser));
        insertObjectsToDb(CRM_ACCOUNT_TABLE_NAME, List.of(account, ibAccount));
        insertObjectsToDb(MT_ACCOUNT_TABLE_NAME, List.of(mtAccount, ibMtAccount));
        // Relations
        relation = generateAccountIbRelationObjectByClient(client);
        relation.setDirectIbRebateAccount(ibAccount.account);
        relation.setRecordDeletedFlag("N");
        relation.setDirectIbLevel(3);
        relation.setDirectIb(ibClient.getUserId());
        insertObjectToDb(ACCOUNT_IB_RELATION_TABLE_NAME, relation);
        // Commissions
        commission = generateS3FactIbSalesCommissionsClient(client);
        commission.setIbRebateAccount(ibAccount.account);
        commission.setIbCommission(561.78);
        commission.setDate(getCurrentDate());
        insertObjectToDb(S3_FACT_IB_SALES_COMMISSIONS, commission);
        // Metrics
        factLoginMetrics = generateS3FactLoginMetricsClient(client);
        factLoginMetrics.setDate(getCurrentDate());
        factLoginMetrics.setDailyNetClosedPnl(235_125.1);
        factLoginMetrics.setDailyNetDeposit(4344.98);
        factLoginMetrics.setDailyTradingVolIn(473_277_453.4);
        factLoginMetrics.setDailyTradingVolOut(92_538_898.01);
        factLoginMetrics.setDailyGrossClientPnl(-3525.7);
        factLoginMetrics.setEquity(698_467.0);
        factLoginMetrics.setDailyDeposit(432.1);
        factLoginMetrics.setDailyWithdraw(5646.999);
        insertObjectToDb(S3_FACT_LOGIN_METRICS_TABLE_NAME, factLoginMetrics);
        // Frauds
        ClientFraudTypes fraud = new ClientFraudTypes(client.getUcid(), "HEDGING", "VINDEX", 0, getCurrentTimestampDbFormat());
        insertObjectToDb(CLIENT_FRAUD_TYPES_TABLE_NAME, fraud);

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
        ibCpaOverviewPage.waitForPageToLoad();
        assertThat("Verify IB overview summary title", ibCpaOverviewPage.getOverviewTitle(), is("IB overview"));
        assertThat("Verify IB overview summary subheader", ibCpaOverviewPage.getOverviewSubheaderText(), is(String.format("IB %s, %s, %s level", ibAccount.account, ibAccount.brand, relation.getDirectIbLevel())));
        assertThat("Verify IB overview summary under this ib title", ibCpaOverviewPage.getUnderThisTitle(), is("Under this IB"));
        assertThat("Verify IB overview summary under this ib items", ibCpaOverviewPage.getUnderThisItems(), contains(String.format("%sclient", 1), String.format("%sfraudster", 1)));
        assertThat("Verify IB overview summary clients performance title", ibCpaOverviewPage.getClientsPerformanceTitle(), is("Clients performance USD"));
        assertThat("Verify IB overview summary clients performance items", ibCpaOverviewPage.getClientsPerformanceItems(), contains(String.format("%sIB rebates", formatter.format(commission.getIbCommission())), String.format("%sNet PNL", formatter.format(factLoginMetrics.getDailyNetClosedPnl())), String.format("%sNet deposit", formatter.format(factLoginMetrics.getDailyNetDeposit()))));
        assertThat("Verify IB overview summary clients totals title", ibCpaOverviewPage.getClientsTotalsTitle(), is("Clients totals USD"));
        assertThat("Verify IB overview summary clients totals items", ibCpaOverviewPage.getClientsTotalsItems(), contains(String.format("%sVolume", formatter.format((factLoginMetrics.getDailyTradingVolIn() + factLoginMetrics.getDailyTradingVolOut()) / 1_000_000)), String.format("%sProfit", formatter.format(factLoginMetrics.getDailyGrossClientPnl())), String.format("%sEquity", formatter.format(factLoginMetrics.getEquity())), String.format("%sDeposit", formatter.format(factLoginMetrics.getDailyDeposit())), String.format("%sWithdrawal", formatter.format(factLoginMetrics.getDailyWithdraw()))));
        ibCpaOverviewPage.clickOverviewSubheaderIcon();
        page.waitForTimeout(5000);
        PlaywrightAssertions.assertThat(page.context().pages().getLast()).hasURL(String.format("https://risktool.risk-vantagefx.com//rebate?server=%s&login=%s", ibAccount.serverName, ibAccount.account));
    }


    @AfterAll
    public static void teardown() throws Exception {
        cleanCrmUserTableByClient(crmTbUser.ucid, ibCrmTbUser.ucid);
        deleteEntryFromDb(ACCOUNT_IB_RELATION_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        deleteEntryFromDb(S3_FACT_IB_SALES_COMMISSIONS, String.format("ucid = '%s'", client.getUcid()));
        deleteEntryFromDb(S3_FACT_LOGIN_METRICS_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        deleteEntryFromDb(CLIENT_FRAUD_TYPES_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        closeAlert(crmTbUser.ucid);
    }
}
