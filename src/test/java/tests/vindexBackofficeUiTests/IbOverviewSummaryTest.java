package tests.vindexBackofficeUiTests;

import businessObjects.db.clickhouse.accountIbRelation.AccountIbRelationObject;
import businessObjects.db.clickhouse.crmTbAccount.CrmTbAccountObject;
import businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObject;
import businessObjects.db.clickhouse.ibSummaryLifetime.IbSummaryLifetimeObject;
import businessObjects.db.clickhouse.mtAccount.MtAccountObject;
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
    private static IbSummaryLifetimeObject ibSummaryLifetime;
    private static final DecimalFormat formatter = new DecimalFormat("#,###.#");

    @BeforeAll
    public static void setup() throws ReflectiveOperationException, SQLException, JsonProcessingException {
        insertObjectsToDb(CRM_USER_TABLE_NAME, List.of(crmTbUser, ibCrmTbUser));
        insertObjectsToDb(CRM_ACCOUNT_TABLE_NAME, List.of(account, ibAccount));
        insertObjectsToDb(MT_ACCOUNT_TABLE_NAME, List.of(mtAccount, ibMtAccount));
        ibSummaryLifetime = new IbSummaryLifetimeObject(
                1, ibClient.getUserId(), ibClient.getBrand(), ibClient.getRegulator(), ibClient.getUcid(), "serverName", 2, 123L, ibClient.getTradingAccount().longValue(), 111, 56, 13, -97_831.978, 1123.33, -78_878.32, 5000.12, 2003.0, 2997.12, 42_124.93, 5443.3, getCurrentTimestampDbFormat()
        );
        insertObjectToDb(IB_SUMMARY_LIFETIME_TABLE_NAME, ibSummaryLifetime);
        AccountIbRelationObject relation1 = generateAccountIbRelationObjectByClient(client);
        relation1.setDirectIbRebateAccount(ibAccount.account);
        insertObjectToDb(ACCOUNT_IB_RELATION_TABLE_NAME, relation1);
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
        assertThat("Verify IB overview summary title", ibOverviewPage.getIbOverviewTitle(), is("IB overview"));
        assertThat("Verify IB overview summary subheader", ibOverviewPage.getIbOverviewSubheaderText(), is(String.format("IB %s, %s, %s level", ibAccount.account, ibAccount.brand, ibSummaryLifetime.ibLevel)));
        assertThat("Verify IB overview summary under this ib title", ibOverviewPage.getUnderThisIbTitle(), is("Under this IB"));
        assertThat("Verify IB overview summary under this ib items", ibOverviewPage.getUnderThisIbItems(), contains(String.format("%sclients", ibSummaryLifetime.directUsersCount), String.format("%sfraudsters", ibSummaryLifetime.fraudstersCount), String.format("%slower-level IB", ibSummaryLifetime.subIbsCount)));
        assertThat("Verify IB overview summary clients performance title", ibOverviewPage.getClientsPerformanceTitle(), is("Clients performance USD"));
        assertThat("Verify IB overview summary clients performance items", ibOverviewPage.getClientsPerformanceItems(), contains(String.format("%sIB rebates", formatter.format(ibSummaryLifetime.rebate)), String.format("%sNet PNL", formatter.format(ibSummaryLifetime.netPnl)), String.format("%sNet deposit", formatter.format(ibSummaryLifetime.netDeposit))));
        assertThat("Verify IB overview summary clients totals title", ibOverviewPage.getClientsTotalsTitle(), is("Clients totals USD"));
        assertThat("Verify IB overview summary clients totals items", ibOverviewPage.getClientsTotalsItems(), contains(String.format("%sVolume", formatter.format(ibSummaryLifetime.notionalValue)), String.format("%sProfit", formatter.format(ibSummaryLifetime.pnl)), String.format("%sEquity", formatter.format(ibSummaryLifetime.equity)), String.format("%sDeposit", formatter.format(ibSummaryLifetime.deposit)), String.format("%sWithdrawal", formatter.format(ibSummaryLifetime.withdrawal))));
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
