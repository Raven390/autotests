package tests.vindex_backoffice_ui_tests.investigationTool;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountDataForUi;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static business_objects.kafka.alerts.RuleAlertFactory.generatePaymentAlertByUcid;
import static business_objects.kafka.alerts.RuleAlertFactory.generateRuleAlertByUcid;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.Currency.USD;
import static helpers.database.BoHelper.*;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Utils.writeLog;

import business_objects.db.abuse_registry_db.FraudTypeCategory;
import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.mt_account.MtAccountObject;
import business_objects.kafka.alerts.PaymentAlertMessage;
import business_objects.kafka.alerts.RuleAlert;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.data.ClientHelper;
import helpers.data.enums.FraudType;
import helpers.database.DbName;
import helpers.kafka.KafkaHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

@Slf4j
@Feature("Display fraud types for managing based on role")
@Story("Add category to fraud type")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag(TEAM_BACKOFFICE)
@Tag(LAYER_WEB)
class FraudTypeCategoryTest extends TestBaseWeb {
    private static final KafkaHelper kafka = new KafkaHelper();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Trading client data
    private static final ClientHelper tradingClient = getRandomVantageClientAllFields();
    private static final CrmTbUserObject tradingCrmUser = generateUserByClient(tradingClient);
    private static final String ALERT_TYPE_TRADING = "TRADING";
    private static CrmTbAccountObject tradingAccount;
    private static MtAccountObject tradingMtAccount;

    // Payment client data
    private static final ClientHelper paymentClient = getRandomVantageClientAllFields();
    private static final CrmTbUserObject paymentCrmUser = generateUserByClient(paymentClient);
    private static CrmTbAccountObject paymentAccount;
    private static MtAccountObject paymentMtAccount;

    // Dual role client data
    private static final ClientHelper dualClient = getRandomVantageClientAllFields();
    private static final CrmTbUserObject dualCrmUser = generateUserByClient(dualClient);
    private static CrmTbAccountObject dualAccount;
    private static MtAccountObject dualMtAccount;

    // Trading fraud types (category_id = 1)
    private static final List<String> tradingFraudTypes = new ArrayList<>();

    // Payment fraud types (category_id = 2)
    private static final List<String> paymentFraudTypes = new ArrayList<>();

    // Dual fraud types (category_id = 1 & 2)
    private static final List<String> dualCategoryFraudTypes = new ArrayList<>();

    @BeforeAll
    static void setupData() throws Exception {
        objectMapper.findAndRegisterModules();

        List<FraudTypeCategory> fraudTypeCategories =
                getObjectsFromDB(DbName.POSTGRES, AR_FRAUD_TYPE_CATEGORY_TABLE_NAME, null, FraudTypeCategory.class);

        for (FraudTypeCategory category : fraudTypeCategories) {
            FraudType fraudType = FraudType.valueOfCode(category.getFraudTypeCode());
            if (!fraudType.isVisible()) continue;

            switch (category.getCategoryId()) {
                case 1 -> tradingFraudTypes.add(fraudType.getName());
                case 2 -> paymentFraudTypes.add(fraudType.getName());
                default -> throw new IllegalStateException("Unexpected value: %s".formatted(category.getCategoryId()));
            }
        }

        Set<String> paymentSet = new HashSet<>(paymentFraudTypes);
        tradingFraudTypes.stream().filter(paymentSet::contains).forEach(dualCategoryFraudTypes::add);

        // Setup Trading client
        tradingAccount = generateCrmTbAccountDataForUi(tradingClient);
        tradingAccount.currency = USD.getCode();
        tradingMtAccount = generateMtAccountByCrmTbAccount(tradingAccount);
        insertObjectToDb(CRM_USER_TABLE_NAME, tradingCrmUser);
        insertObjectToDb(CRM_TB_ACCOUNT_TABLE_NAME, tradingAccount);
        insertObjectToDb(MT_ACCOUNT_TABLE_NAME, tradingMtAccount);

        // Setup Payment client
        paymentAccount = generateCrmTbAccountDataForUi(paymentClient);
        paymentAccount.currency = USD.getCode();
        paymentMtAccount = generateMtAccountByCrmTbAccount(paymentAccount);
        insertObjectToDb(CRM_USER_TABLE_NAME, paymentCrmUser);
        insertObjectToDb(CRM_TB_ACCOUNT_TABLE_NAME, paymentAccount);
        insertObjectToDb(MT_ACCOUNT_TABLE_NAME, paymentMtAccount);

        // Setup Dual role client
        dualAccount = generateCrmTbAccountDataForUi(dualClient);
        dualAccount.currency = USD.getCode();
        dualMtAccount = generateMtAccountByCrmTbAccount(dualAccount);
        insertObjectToDb(CRM_USER_TABLE_NAME, dualCrmUser);
        insertObjectToDb(CRM_TB_ACCOUNT_TABLE_NAME, dualAccount);
        insertObjectToDb(MT_ACCOUNT_TABLE_NAME, dualMtAccount);
    }

    @Test
    @AllureId("1775")
    @DisplayName("Trading team should see only TRADING fraud types")
    void testTradingTeamSeesOnlyTradingFraudTypes() throws Exception {
        // Send trading alert
        RuleAlert tradingAlert = generateRuleAlertByUcid(tradingCrmUser.ucid);
        tradingAlert.type = ALERT_TYPE_TRADING;
        tradingAlert.rule.attributes.account = tradingMtAccount.account.toString();
        kafka.produceMessage(tradingAlert.alertId, objectMapper.writeValueAsString(tradingAlert), KAFKA_TOPIC_ALERTS);
        Thread.sleep(2000);

        // Login and navigate
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsOps24User();
        investigationPage.navigateToMain();
        investigationPage.waitForPageToLoad();
        investigationPage.navigateToClient(tradingCrmUser.ucid);
        alertsPage.waitForPageToLoad();
        resolvePage.openResolveSuspicious();

        // Get fraud types list
        List<String> availableFraudTypes = resolvePage.getFraudTypesList();

        // Verify only TRADING fraud types are visible
        assertThat("Fraud types list should not be empty", availableFraudTypes, not(empty()));

        // Check that all visible fraud types are trading fraud types
        for (String fraudType : availableFraudTypes) {
            assertThat(
                    "Fraud type %s should be a TRADING fraud type".formatted(fraudType),
                    tradingFraudTypes,
                    hasItem(fraudType));
        }

        // Check that payment-only fraud types are NOT present
        for (String paymentFraud : paymentFraudTypes) {
            if (dualCategoryFraudTypes.contains(paymentFraud)) continue;
            assertThat(
                    "Payment-only fraud type %s should NOT be visible".formatted(paymentFraud),
                    availableFraudTypes,
                    not(hasItem(paymentFraud)));
        }
    }

    @Test
    @AllureId("1776")
    @DisplayName("Payment team should see only PAYMENT fraud types")
    void testPaymentTeamSeesOnlyPaymentFraudTypes() throws Exception {
        // Send payment alert
        PaymentAlertMessage paymentAlert = generatePaymentAlertByUcid(paymentCrmUser.ucid);
        kafka.produceMessage(
                paymentAlert.getId().toString(), objectMapper.writeValueAsString(paymentAlert), KAFKA_TOPIC_ALERTS);
        Thread.sleep(2000);

        // Login and navigate
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsPaymentTeamUser();
        investigationPage.navigateToMain();
        investigationPage.waitForPageToLoad();
        investigationPage.navigateToClient(paymentCrmUser.ucid);
        alertsPage.waitForPageToLoad();
        resolvePage.openResolveSuspicious();

        // Get fraud types list
        List<String> availableFraudTypes = resolvePage.getFraudTypesList();

        // Verify only PAYMENT fraud types are visible
        assertThat("Fraud types list should not be empty", availableFraudTypes, not(empty()));

        // Check that all visible fraud types are payment fraud types
        for (String fraudType : availableFraudTypes) {
            assertThat(
                    "Fraud type %s should be a PAYMENT fraud type".formatted(fraudType),
                    paymentFraudTypes,
                    hasItem(fraudType));
        }

        // Check that trading-only fraud types are NOT present
        for (String tradingFraud : tradingFraudTypes) {
            if (dualCategoryFraudTypes.contains(tradingFraud)) continue;
            assertThat(
                    "Trading-only fraud type %s should NOT be visible".formatted(tradingFraud),
                    availableFraudTypes,
                    not(hasItem(tradingFraud)));
        }
    }

    @Test
    @AllureId("1777")
    @DisplayName("User with both roles should see all fraud types")
    void testDualRoleUserSeesCorrectFraudTypes() throws Exception {
        // Send both types of alerts for dual client
        RuleAlert tradingAlert = generateRuleAlertByUcid(dualCrmUser.ucid);
        tradingAlert.type = ALERT_TYPE_TRADING;
        kafka.produceMessage(tradingAlert.alertId, objectMapper.writeValueAsString(tradingAlert), KAFKA_TOPIC_ALERTS);

        PaymentAlertMessage paymentAlert = generatePaymentAlertByUcid(dualCrmUser.ucid);
        kafka.produceMessage(
                paymentAlert.getId().toString(), objectMapper.writeValueAsString(paymentAlert), KAFKA_TOPIC_ALERTS);
        Thread.sleep(2000);

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToMain();
        investigationPage.waitForPageToLoad();
        investigationPage.navigateToClient(dualCrmUser.ucid);
        alertsPage.waitForPageToLoad();
        resolvePage.openResolveSuspicious();

        List<String> tradingFraudTypes = resolvePage.getFraudTypesList();
        List<String> allFraudNames = Stream.concat(tradingFraudTypes.stream(), paymentFraudTypes.stream())
                .toList();

        for (String fraudType : tradingFraudTypes) {
            assertThat("All fraud types should be visible for dual client", allFraudNames, hasItem(fraudType));
        }
    }

    @Test
    @AllureId("1778")
    @DisplayName("Dual-category fraud types should appear in Trading investigation")
    void testDualCategoryFraudTypesInBothModes() throws Exception {
        // Send trading alert
        RuleAlert tradingAlert = generateRuleAlertByUcid(tradingCrmUser.ucid);
        tradingAlert.type = ALERT_TYPE_TRADING;
        tradingAlert.rule.attributes.account = tradingMtAccount.account.toString();
        kafka.produceMessage(tradingAlert.alertId, objectMapper.writeValueAsString(tradingAlert), KAFKA_TOPIC_ALERTS);

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsOps24User();

        // Check Trading mode
        investigationPage.navigateToMain();
        investigationPage.waitForPageToLoad();
        investigationPage.navigateToClient(tradingCrmUser.ucid);
        alertsPage.waitForPageToLoad();
        resolvePage.openResolveSuspicious();

        List<String> tradingFraudTypes = resolvePage.getFraudTypesList();
        for (String dualFraudType : dualCategoryFraudTypes) {
            assertThat(
                    "Dual category fraud types should be available in PAYMENT investigation",
                    tradingFraudTypes,
                    hasItem(dualFraudType));
        }
    }

    @Test
    @AllureId("1779")
    @DisplayName("Dual-category fraud types should appear in Payment investigation")
    void testDualCategoryFraudTypesForPaymentRole() throws Exception {
        // Send trading alert
        PaymentAlertMessage paymentAlert = generatePaymentAlertByUcid(paymentCrmUser.ucid);
        kafka.produceMessage(
                paymentAlert.getId().toString(), objectMapper.writeValueAsString(paymentAlert), KAFKA_TOPIC_ALERTS);

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsPaymentTeamUser();

        // Check Trading mode
        investigationPage.navigateToMain();
        investigationPage.waitForPageToLoad();
        investigationPage.navigateToClient(paymentCrmUser.ucid);
        alertsPage.waitForPageToLoad();
        resolvePage.openResolveSuspicious();

        List<String> paymentFraudTypes = resolvePage.getFraudTypesList();
        for (String dualFraudType : dualCategoryFraudTypes) {
            assertThat(
                    "Dual category fraud types should be available in PAYMENT investigation",
                    paymentFraudTypes,
                    hasItem(dualFraudType));
        }
    }

    @AfterEach
    void cleanUp() {
        closeAlert(dualCrmUser.ucid);
    }

    @AfterAll
    static void teardown() {
        // Cleanup Trading client
        deleteObjectFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", tradingCrmUser.ucid));
        try {
            deleteObjectFromDb(CRM_TB_ACCOUNT_TABLE_NAME, String.format("ucid = '%s'", tradingCrmUser.ucid));
        } catch (Exception e) {
            writeLog("Account deletion failed");
        }
        try {
            deleteObjectFromDb(MT_ACCOUNT_TABLE_NAME, String.format("ucid = '%s'", tradingCrmUser.ucid));
        } catch (Exception e) {
            writeLog("Account deletion failed");
        }
        closeAlert(tradingCrmUser.ucid);
        deleteUserBO(tradingCrmUser.ucid);

        // Cleanup Payment client
        deleteObjectFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", paymentCrmUser.ucid));
        try {
            deleteObjectFromDb(CRM_TB_ACCOUNT_TABLE_NAME, String.format("ucid = '%s'", paymentCrmUser.ucid));
        } catch (Exception e) {
            writeLog("Account deletion failed");
        }
        try {
            deleteObjectFromDb(MT_ACCOUNT_TABLE_NAME, String.format("ucid = '%s'", paymentCrmUser.ucid));
        } catch (Exception e) {
            writeLog("Account deletion failed");
        }
        closeAlert(paymentCrmUser.ucid);
        deleteUserBO(paymentCrmUser.ucid);

        // Cleanup Dual client
        deleteObjectFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", dualCrmUser.ucid));
        try {
            deleteObjectFromDb(CRM_TB_ACCOUNT_TABLE_NAME, String.format("ucid = '%s'", dualCrmUser.ucid));
        } catch (Exception e) {
            writeLog("Account deletion failed");
        }
        try {
            deleteObjectFromDb(MT_ACCOUNT_TABLE_NAME, String.format("ucid = '%s'", dualCrmUser.ucid));
        } catch (Exception e) {
            writeLog("Account deletion failed");
        }
        closeAlert(dualCrmUser.ucid);
        deleteUserBO(dualCrmUser.ucid);
    }
}
