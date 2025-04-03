package tests.vindex_backoffice_ui_tests.trading;

import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.mtAccount.MtAccountObject;
import business_objects.db.clickhouse.mt_mt5_deals_coerced_toxicity.Mt5DealsCoercedToxicityObject;
import business_objects.kafka.alerts.RuleAlert;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.data.ClientHelper;
import helpers.kafka.KafkaHelper;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountDataForUi;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.mtAccount.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static business_objects.db.clickhouse.mt_mt5_deals_coerced_toxicity.Mt5DealsCoercedToxicityFactory.generateMt5DealsCoercedToxicityByClient;
import static business_objects.kafka.alerts.RuleAlertFactory.generateRuleAlertByUcid;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.DateTimeFormat.DATE_AND_TIME;
import static helpers.database.BoHelper.closeAlert;
import static helpers.database.CleanTableHelper.*;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static utils.Constants.*;
import static utils.Utils.getCurrentTimestampMinusOffsetFormatted;

public class TradingSummaryAbsoluteToxicityTest extends TestBaseWeb {

    private static final KafkaHelper kafka = new KafkaHelper();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final ClientHelper client = getRandomVantageClientAllFields();
    private static final CrmTbUserObject crmTbUser = generateUserByClient(client);
    private static final CrmTbAccountObject account = generateCrmTbAccountDataForUi(client);
    private static final MtAccountObject mtAccount = generateMtAccountByCrmTbAccount(account);
    DecimalFormat formatter = new DecimalFormat("#,##0.00");

    @BeforeAll
    public static void setup() throws ReflectiveOperationException, SQLException, JsonProcessingException {
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        insertObjectToDb(CRM_ACCOUNT_TABLE_NAME, account);
        insertObjectToDb(MT_ACCOUNT_TABLE_NAME, mtAccount);
        RuleAlert alert = generateRuleAlertByUcid(crmTbUser.ucid);
        kafka.produceMessage(alert.alertId, objectMapper.writeValueAsString(alert), KAFKA_TOPIC_ALERTS);
    }

    @AfterEach
    public void teardownEach() throws Exception {
        cleanMt5CoercedToxicityTableByUcid(client.getUcid());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1008")
    @DisplayName("Verify Absolute toxicity widget positive in Trading - Summary")
    public void verifyTradingSummaryAbsoluteToxicity1Test() {
        Mt5DealsCoercedToxicityObject trade1 = generateMt5DealsCoercedToxicityByClient(client);
        trade1.toxicityUsd = 3545.534;
        Mt5DealsCoercedToxicityObject trade2 = generateMt5DealsCoercedToxicityByClient(client);
        trade2.toxicityUsd = 678.0;
        trade2.timeUtc = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 1, 0, 0);
        insertObjectsToDb(MT5_DEALS_COERCED_TOXICITY_TABLE_NAME, List.of(trade1, trade2));
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        tradingPage.openTradingTab();
        tradingPage.openSummaryTab();
        assertThat("Verify title", tradingPage.getAbsoluteToxicityWidgetTitle(), is("Absolute toxicity USD"));
        assertThat("Verify value", tradingPage.getAbsoluteToxicityWidgetValue(), is(formatter.format(trade1.toxicityUsd + trade2.toxicityUsd)));
        assertThat("Verify info", tradingPage.getAbsoluteToxicityWidgetInfo(), is(String.format("on %s deals", "2")));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1009")
    @DisplayName("Verify Absolute toxicity widget negative in Trading - Summary")
    public void verifyTradingSummaryAbsoluteToxicity2Test() {
        Mt5DealsCoercedToxicityObject trade1 = generateMt5DealsCoercedToxicityByClient(client);
        trade1.toxicityUsd = -123_545.534;
        insertObjectToDb(MT5_DEALS_COERCED_TOXICITY_TABLE_NAME, trade1);
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        tradingPage.openTradingTab();
        tradingPage.openSummaryTab();
        assertThat("Verify value", tradingPage.getAbsoluteToxicityWidgetValue(), is(formatter.format(trade1.toxicityUsd)));
        assertThat("Verify info", tradingPage.getAbsoluteToxicityWidgetInfo(), is(String.format("on %s deal", "1")));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1010")
    @DisplayName("Verify Absolute toxicity widget no data in Trading - Summary")
    public void verifyTradingSummaryAbsoluteToxicity3Test() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        tradingPage.openTradingTab();
        tradingPage.openSummaryTab();
        assertThat("Verify value", tradingPage.getAbsoluteToxicityWidgetValue(), is("0"));
        assertThat("Verify info", tradingPage.getAbsoluteToxicityWidgetInfo(), is(String.format("on %s deals", "0")));
    }

    @AfterAll
    public static void teardown() throws Exception {
        cleanCrmUserTableByClient(crmTbUser.ucid);
        cleanMt5CoercedToxicityTableByUcid(client.getUcid());
        closeAlert(crmTbUser.ucid);
    }
}
