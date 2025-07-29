package tests.vindex_backoffice_ui_tests.investigationTool.trading;

import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObject;
import business_objects.kafka.alerts.RuleAlert;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.data.ClientHelper;
import helpers.kafka.KafkaHelper;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

import java.sql.SQLException;
import java.util.List;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountDataForUi;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObjectFactory.generateMt4TradesCoerced;
import static business_objects.kafka.alerts.RuleAlertFactory.generateRuleAlertByUcid;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.BoHelper.closeAlert;
import static helpers.database.CleanTableHelper.cleanCrmUserTableByClient;
import static helpers.database.CleanTableHelper.cleanMt4CoercedTableByUcid;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static utils.Constants.*;

public class TradingSummaryWinrateTest extends TestBaseWeb {

    private static final KafkaHelper kafka = new KafkaHelper();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final ClientHelper client = getRandomVantageClientAllFields();
    private static final CrmTbUserObject crmTbUser = generateUserByClient(client);
    private static final CrmTbAccountObject account = generateCrmTbAccountDataForUi(client);

    @BeforeAll
    public static void setup() throws ReflectiveOperationException, SQLException, JsonProcessingException {
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        insertObjectToDb(CRM_TB_ACCOUNT_TABLE_NAME, account);
        RuleAlert alert = generateRuleAlertByUcid(crmTbUser.ucid);
        kafka.produceMessage(alert.alertId, objectMapper.writeValueAsString(alert), KAFKA_TOPIC_ALERTS);
    }

    @AfterEach
    public void teardownEach() {
        deleteEntryFromDb(MT4_TRADES_COERCED_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("870")
    @DisplayName("Verify Winrate widget 100% in Trading - Summary")
    public void verifyTradingSummaryWinrate1Test() {
        insertObjectToDb(MT4_TRADES_COERCED_TABLE_NAME, generateMt4TradesCoerced(client));
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        tradingPage.openTradingTab();
        tradingPage.openSummaryTab();
        assertThat("Verify value", tradingPage.getWinrateWidgetValue(), equalTo("100%"));
        assertThat("Verify info", tradingPage.getWinrateWidgetInfo(), equalTo(String.format("on %s ticket", "1")));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("871")
    @DisplayName("Verify Winrate widget 0% in Trading - Summary")
    public void verifyTradingSummaryWinrate2Test() {
        MtMt4TradesCoercedObject trade = generateMt4TradesCoerced(client);
        trade.profitUsd = 0d;
        trade.storageUsd = 0d;
        trade.commissionUsd = 0d;
        insertObjectToDb(MT4_TRADES_COERCED_TABLE_NAME, trade);
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        tradingPage.openTradingTab();
        tradingPage.openSummaryTab();
        assertThat("Verify value", tradingPage.getWinrateWidgetValue(), equalTo("0%"));
        assertThat("Verify info", tradingPage.getWinrateWidgetInfo(), equalTo(String.format("on %s ticket", "1")));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("872")
    @DisplayName("Verify Winrate widget 66.67% in Trading - Summary")
    public void verifyTradingSummaryWinrate3Test() {
        MtMt4TradesCoercedObject trade1 = generateMt4TradesCoerced(client);
        trade1.profitUsd = 0d;
        trade1.storageUsd = 0d;
        trade1.commissionUsd = 0d;
        insertObjectsToDb(MT4_TRADES_COERCED_TABLE_NAME, List.of(trade1, generateMt4TradesCoerced(client), generateMt4TradesCoerced(client)));
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        tradingPage.openTradingTab();
        tradingPage.openSummaryTab();
        assertThat("Verify value", tradingPage.getWinrateWidgetValue(), equalTo("66.67%"));
        assertThat("Verify info", tradingPage.getWinrateWidgetInfo(), equalTo(String.format("on %s tickets", "3")));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("873")
    @DisplayName("Verify Winrate widget no data in Trading - Summary")
    public void verifyTradingSummaryWinrate4Test() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        tradingPage.openTradingTab();
        tradingPage.openSummaryTab();
        assertThat("Verify value", tradingPage.getWinrateWidgetValue(), equalTo("No data"));
        assertThat("Verify info", tradingPage.getWinrateWidgetInfo(), equalTo("on 0 tickets"));
    }

    @AfterAll
    public static void teardown() throws Exception {
        cleanCrmUserTableByClient(crmTbUser.ucid);
        cleanMt4CoercedTableByUcid(client.getUcid());
        closeAlert(crmTbUser.ucid);
    }
}
