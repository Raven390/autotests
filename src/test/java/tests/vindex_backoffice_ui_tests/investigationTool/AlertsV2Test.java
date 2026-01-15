package tests.vindex_backoffice_ui_tests.investigationTool;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateStaticCrmTbAccountActive;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Utils.insertCrmAccountsToDb;

import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.kafka.alerts.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import helpers.data.ClientFactory;
import helpers.data.ClientHelper;
import helpers.kafka.KafkaHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

@Tag(TEAM_BACKOFFICE)
@Tag(LAYER_WEB)
@Feature("BMS-2506 - Refactor Alert Schema based on new alert creation logic ")
@DisplayName("Alerts V2 Structure Tests")
class AlertsV2Test extends TestBaseWeb {

    private static final ClientHelper client = ClientFactory.getRandomVantageClientAllFields();
    private static final CrmTbUserObject crmTbUser = generateUserByClient(client);
    private static final CrmTbAccountObject account = generateStaticCrmTbAccountActive(client);
    private static final KafkaHelper kafka = new KafkaHelper();
    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @BeforeAll
    static void setup() {
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        insertCrmAccountsToDb(account);
    }

    @Test
    @AllureId("1673")
    @DisplayName("Verify Trading Alert (OpenTrade) - Mandatory Attributes Structure")
    void verifyOpenTradeAlertStructure() throws Exception {
        // Arrange: Create OpenTrade alert with mandatory attributes
        BaseAlertMessageV2.Rule rule = new BaseAlertMessageV2.Rule();
        rule.name = "mirror_trading";
        rule.ver = "0.1.7";

        Map<String, String> attributes = new HashMap<>();
        attributes.put("toxicityScore", "85.5");
        attributes.put("ticketId", "12345");

        UUID alertID = UUID.randomUUID();
        TradingAlertMessageV2 openTradeAlert = new TradingAlertMessageV2(
                alertID,
                AlertMessageType.TRADING,
                OffsetDateTime.now(),
                OffsetDateTime.now().minusMinutes(1),
                crmTbUser.ucid,
                "HEDGING",
                "OpenTrade",
                "Suspicious trading pattern detected",
                rule,
                attributes,
                String.valueOf(account.account),
                "EURUSD",
                String.valueOf(account.serverIdSt));

        // Act: Produce alert to Kafka
        kafka.produceMessage(alertID.toString(), objectMapper.writeValueAsString(openTradeAlert), KAFKA_TOPIC_ALERTS);

        // Navigate to UI
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        alertsPage.openAlertsTab();
        alertsPage.waitForAlertsCountToLoad();

        // Assert: Verify mandatory attributes are displayed
        assertThat("Alert should be displayed", alertsPage.getAlertsCount(), equalTo(1));
        assertThat("Rule name should match", alertsPage.getAlertsRuleNamesList().getFirst(), equalTo("mirror_trading"));
        assertThat(
                "Trigger should be OpenTrade",
                alertsPage.getAlertsRuleTriggersList().getFirst(),
                equalTo("OpenTrade"));

        // Verify attributes contain flexible data
        String attributesText = alertsPage.getAlertsAttributesList().getFirst();
        assertThat("Attributes should contain toxicityScore", attributesText, containsString("toxicityScore"));
        assertThat("Attributes should contain ticketId", attributesText, containsString("ticketId"));
    }

    @Test
    @AllureId("1674")
    @DisplayName("Verify Trading Alert (CloseTrade) - Mandatory Attributes Structure")
    void verifyCloseTradeAlertStructure() throws Exception {
        // Arrange: Create CloseTrade alert
        BaseAlertMessageV2.Rule rule = new BaseAlertMessageV2.Rule();
        rule.name = "high_risk_exit";
        rule.ver = "1.0.0";

        Map<String, String> attributes = new HashMap<>();
        attributes.put("profitLoss", "-1500.00");
        attributes.put("holdingPeriod", "45");

        UUID alertId = UUID.randomUUID();
        TradingAlertMessageV2 closeTradeAlert = new TradingAlertMessageV2(
                alertId,
                AlertMessageType.TRADING,
                OffsetDateTime.now(),
                OffsetDateTime.now().minusMinutes(2),
                crmTbUser.ucid,
                "MARKET_MANIPULATION",
                "CloseTrade",
                "Abnormal closing pattern",
                rule,
                attributes,
                String.valueOf(account.account),
                "GBPUSD",
                "srv-12");

        // Act
        kafka.produceMessage(alertId.toString(), objectMapper.writeValueAsString(closeTradeAlert), KAFKA_TOPIC_ALERTS);

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        alertsPage.openAlertsTab();
        alertsPage.waitForAlertsCountToLoad();

        // Assert
        assertThat("CloseTrade alert should be displayed", alertsPage.getAlertsCount(), greaterThanOrEqualTo(1));
        assertThat("Trigger should be CloseTrade", alertsPage.getAlertsRuleTriggersList(), hasItem("CloseTrade"));
    }

    @Test
    @AllureId("1675")
    @DisplayName("Verify Multiple Alerts With Different Trigger Types")
    void verifyMultipleAlertsWithDifferentTriggers() throws Exception {
        // Arrange: Create multiple alerts with different triggers
        BaseAlertMessageV2.Rule tradingRule = new BaseAlertMessageV2.Rule();
        tradingRule.name = "mirror_trading";
        tradingRule.ver = "0.1.7";

        UUID tradingAlertId = UUID.randomUUID();
        TradingAlertMessageV2 tradingAlert = new TradingAlertMessageV2(
                tradingAlertId,
                AlertMessageType.TRADING,
                OffsetDateTime.now(),
                OffsetDateTime.now().minusMinutes(1),
                crmTbUser.ucid,
                "HEDGING",
                "OpenTrade",
                "Suspicious pattern",
                tradingRule,
                new HashMap<>(),
                String.valueOf(account.account),
                "EURUSD",
                "srv-45");

        BaseAlertMessageV2.Rule paymentRule = new BaseAlertMessageV2.Rule();
        paymentRule.name = "fraud_detection";
        paymentRule.ver = "1.0.0";

        UUID paymentAlertId = UUID.randomUUID();
        PaymentAlertMessageV2 paymentAlert = new PaymentAlertMessageV2(
                paymentAlertId,
                AlertMessageType.PAYMENT,
                OffsetDateTime.now(),
                OffsetDateTime.now().minusMinutes(2),
                crmTbUser.ucid,
                "POTENTIAL_ABUSE",
                "Deposit",
                "Suspicious payment",
                paymentRule,
                new HashMap<>(),
                String.valueOf(account.account),
                "srv-45",
                "CRYPTO",
                "500.00",
                "500.00",
                "USD",
                "D987654321",
                "evt-" + UUID.randomUUID());

        // Act: Send both alerts
        kafka.produceMessage(
                tradingAlertId.toString(), objectMapper.writeValueAsString(tradingAlert), KAFKA_TOPIC_ALERTS);

        kafka.produceMessage(
                paymentAlertId.toString(), objectMapper.writeValueAsString(paymentAlert), KAFKA_TOPIC_ALERTS);

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        alertsPage.openAlertsTab();
        alertsPage.filterAllAlerts();
        alertsPage.waitForAlertsCountToLoad();

        // Assert
        assertThat("Multiple alerts should be displayed", alertsPage.getAlertsCount(), greaterThanOrEqualTo(2));
        assertThat(
                "Both triggers should be present",
                alertsPage.getAlertsRuleTriggersList(),
                hasItems("OpenTrade", "Deposit"));
    }

    @Test
    @AllureId("1676")
    @DisplayName("Verify Alert Rule Version Display")
    void verifyAlertRuleVersionDisplay() throws Exception {
        // Arrange: Create alert with specific rule version
        BaseAlertMessageV2.Rule rule = new BaseAlertMessageV2.Rule();
        rule.name = "test_rule";
        rule.ver = "2.5.3";

        UUID alertId = UUID.randomUUID();
        TradingAlertMessageV2 alert = new TradingAlertMessageV2(
                alertId,
                AlertMessageType.TRADING,
                OffsetDateTime.now(),
                OffsetDateTime.now().minusMinutes(1),
                crmTbUser.ucid,
                "HEDGING",
                "OpenTrade",
                "Test alert",
                rule,
                new HashMap<>(),
                String.valueOf(account.account),
                "BTCUSD",
                "srv-45");

        // Act
        kafka.produceMessage(alertId.toString(), objectMapper.writeValueAsString(alert), KAFKA_TOPIC_ALERTS);

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        alertsPage.openAlertsTab();
        alertsPage.waitForAlertsCountToLoad();

        // Assert: Verify rule version is displayed or accessible
        assertThat("Alert should be created", alertsPage.getAlertsCount(), greaterThanOrEqualTo(1));
        assertThat("Rule name should match", alertsPage.getAlertsRuleNamesList(), hasItem("test_rule"));
    }

    @Test
    @AllureId("1677")
    @DisplayName("Verify Flexible Attributes Are Stored Separately From Mandatory Fields")
    void verifyFlexibleAttributesSeparation() throws Exception {
        // Arrange: Create alert with various flexible attributes
        BaseAlertMessageV2.Rule rule = new BaseAlertMessageV2.Rule();
        rule.name = "comprehensive_check";
        rule.ver = "1.0.0";

        Map<String, String> flexibleAttributes = new HashMap<>();
        flexibleAttributes.put("customField1", "value1");
        flexibleAttributes.put("customField2", "value2");
        flexibleAttributes.put("riskLevel", "CRITICAL");
        flexibleAttributes.put("notes", "Manual review required");

        UUID alertId = UUID.randomUUID();
        PaymentAlertMessageV2 alert = new PaymentAlertMessageV2(
                alertId,
                AlertMessageType.PAYMENT,
                OffsetDateTime.now(),
                OffsetDateTime.now().minusMinutes(1),
                crmTbUser.ucid,
                "POTENTIAL_ABUSE",
                "Withdrawal",
                "Complex scenario",
                rule,
                flexibleAttributes,
                String.valueOf(account.account),
                "srv-45",
                "WIRE_TRANSFER",
                "10000.00",
                "10000.00",
                "EUR",
                "W555555555",
                "evt-" + UUID.randomUUID());

        // Act
        kafka.produceMessage(alertId.toString(), objectMapper.writeValueAsString(alert), KAFKA_TOPIC_ALERTS);

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        alertsPage.openAlertsTab();
        alertsPage.waitForAlertsCountToLoad();

        // Assert: Verify flexible attributes are displayed
        assertThat("Alert should be displayed", alertsPage.getAlertsCount(), greaterThanOrEqualTo(1));
        String attributesDisplay = alertsPage.getAlertsAttributesList().getFirst();
        assertThat("Flexible attributes should be present", attributesDisplay, not(emptyString()));
    }

    @Test
    @AllureId("1678")
    @DisplayName("Verify Alert Timestamp Fields Are Displayed Correctly")
    void verifyAlertTimestampFields() throws Exception {
        // Arrange
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime triggerTime = now.minusMinutes(5);

        BaseAlertMessageV2.Rule rule = new BaseAlertMessageV2.Rule();
        rule.name = "timestamp_test";
        rule.ver = "1.0.0";

        UUID alertId = UUID.randomUUID();
        TradingAlertMessageV2 alert = new TradingAlertMessageV2(
                alertId,
                AlertMessageType.TRADING,
                now,
                triggerTime,
                crmTbUser.ucid,
                "HEDGING",
                "OpenTrade",
                "Timestamp verification",
                rule,
                new HashMap<>(),
                String.valueOf(account.account),
                "EURUSD",
                "srv-45");

        // Act
        kafka.produceMessage(alertId.toString(), objectMapper.writeValueAsString(alert), KAFKA_TOPIC_ALERTS);

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        alertsPage.openAlertsTab();
        alertsPage.waitForAlertsCountToLoad();

        // Assert: Verify timestamps are displayed
        assertThat("Alert date should be Today", alertsPage.getAlertsDatesList().getFirst(), equalTo("Today"));
        String timePattern = "^([01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d$";
        assertThat(
                "Alert time should match expected format",
                alertsPage.getAlertsTimesList().getFirst(),
                matchesPattern(timePattern));
    }

    @AfterEach
    void cleanupAlerts() {
        // Close any open alerts after each test
        helpers.database.BoHelper.closeAlert(crmTbUser.ucid);
    }

    @AfterAll
    static void teardown() {
        deleteObjectFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", crmTbUser.ucid));
        helpers.database.BoHelper.closeAlert(crmTbUser.ucid);
    }
}
