package tests.vindex_backoffice_ui_tests.investigationTool.auditTrail;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountDataForUi;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.FraudType.SLIPPAGE_FREE_ABUSE;
import static helpers.database.BoHelper.closeAlert;
import static helpers.database.DbHelper.deleteEntryFromDb;
import static helpers.database.DbHelper.insertObjectToDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Utils.getRandomUuid;
import static utils.Utils.insertCrmAccountsToDb;

import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.kafka.alerts.AlertMessageType;
import business_objects.kafka.alerts.BaseAlertMessageV2;
import business_objects.kafka.alerts.TradingAlertMessageV2;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import helpers.data.ClientHelper;
import helpers.kafka.KafkaHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

@Tag(TEAM_BACKOFFICE)
@Tag(LAYER_WEB)
@Feature("BMS-2439 - Display alert attributes in side panel")
class AuditSidePanelAlertAttributesTest extends TestBaseWeb {

    private static final ClientHelper client = getRandomVantageClientAllFields();
    private static final CrmTbUserObject crmTbUser = generateUserByClient(client);
    private static final CrmTbAccountObject account = generateCrmTbAccountDataForUi(client);
    private static final KafkaHelper kafka = new KafkaHelper();
    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @BeforeAll
    static void setup() {
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        insertCrmAccountsToDb(account);
    }

    @Test
    @AllureId("1791")
    @DisplayName("Verify alert attributes in side panel are displayed correctly")
    void auditSidePanelAlertAttributesTest() throws Exception {
        BaseAlertMessageV2.Rule rule = new BaseAlertMessageV2.Rule();
        rule.name = "No Slippage";
        rule.ver = "0.2.0";

        TradingAlertMessageV2 openTradeAlert1 = new TradingAlertMessageV2(
                getRandomUuid(),
                AlertMessageType.TRADING,
                OffsetDateTime.now(),
                OffsetDateTime.now(),
                crmTbUser.ucid,
                SLIPPAGE_FREE_ABUSE.getCode(),
                "Close Trade",
                "Slippage abuse: Account Slippage: 1669.4 Account Profit: -17539.5045 Client Slippage: 1669.4",
                rule,
                new HashMap<>(Map.of("ticketId", "11111")),
                String.valueOf(account.account),
                "XAUUSD",
                String.valueOf(account.serverIdSt));
        TradingAlertMessageV2 openTradeAlert2 = new TradingAlertMessageV2(
                getRandomUuid(),
                AlertMessageType.TRADING,
                OffsetDateTime.now(),
                OffsetDateTime.now(),
                crmTbUser.ucid,
                SLIPPAGE_FREE_ABUSE.getCode(),
                "Close Trade",
                "Slippage abuse: Account Slippage: 1669.4 Account Profit: -17539.5045 Client Slippage: 1669.4",
                rule,
                new HashMap<>(Map.of("ticketId", "11112")),
                String.valueOf(account.account),
                "XAUUSD",
                String.valueOf(account.serverIdSt));

        kafka.produceMessages(
                getRandomUuid().toString(),
                KAFKA_TOPIC_ALERTS,
                objectMapper.writeValueAsString(openTradeAlert1),
                objectMapper.writeValueAsString(openTradeAlert2));

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        auditTrailPage.openAuditTrailTab();
        auditTrailPage.clickFirstAuditCard();

        assertThat("Verify rule name", auditTrailPage.getAuditTrailDetailsTitle(), is(rule.name));
        assertThat(
                "Verify alert type and status",
                auditTrailPage.getAuditTrailDetailsLabels(),
                contains("Trading", "Active"));
        assertThat(
                "Verify alert attribute names",
                auditTrailPage.getAuditTrailDetailsAttributeNames(),
                contains(
                        "Alerts",
                        "First alert",
                        "Last alert",
                        "Reason",
                        "Fraud type",
                        "Symbol",
                        "Account",
                        "Server ID",
                        "ticketId",
                        "ticketId"));
        assertThat(
                "Verify alert attribute values",
                auditTrailPage.getAuditTrailDetailsAttributeValues(),
                hasItems(
                        "2 close trade alerts",
                        openTradeAlert1.reason,
                        openTradeAlert1.fraudType,
                        openTradeAlert1.symbol,
                        openTradeAlert1.account,
                        openTradeAlert1.serverId,
                        "11111",
                        "11112"));
    }

    @AfterAll
    static void teardown() {
        deleteEntryFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", crmTbUser.ucid));
        closeAlert(crmTbUser.ucid);
    }
}
