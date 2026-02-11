package tests.vindex_backoffice_ui_tests.investigationTool;

import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.kafka.alerts.RuleAlertFactory.generateRuleAlertByUcid;
import static helpers.api.AbuseRegistryHelper.addFraudForClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.FraudType.*;
import static helpers.data.enums.FraudTypeStatus.*;
import static helpers.database.ArHelper.deleteUserFromAbuseRegistry;
import static helpers.database.BoHelper.deleteUserBO;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static page_objects.backoffice_pages.investigationTool.RestrictionPage.cleanUserRestriction;
import static utils.Constants.*;
import static utils.Constants.CRM_USER_TABLE_NAME;

import business_objects.db.abuse_registry_db.AbuserFraudType;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.kafka.alerts.RuleAlert;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.data.ClientHelper;
import helpers.database.DbName;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

@Tag(TEAM_BACKOFFICE)
@Tag(LAYER_WEB)
@Feature("BMS-3020 Add ability to remove confirmed fraud types (Trading)")
class RemoveConfirmedFraudTypeTradingTest extends TestBaseWeb {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final ClientHelper client = getRandomVantageClientAllFields();
    private static final CrmTbUserObject crmTbUser = generateUserByClient(client);
    private static final RuleAlert alert = generateRuleAlertByUcid(client);

    @BeforeAll
    static void setup() {
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
    }

    @BeforeEach
    void addFraudsForClient() throws IOException {
        addFraudForClient(client, BONUS_ABUSE, CONFIRMED, List.of());
        addFraudForClient(client, CPA_ABUSE, CONFIRMED, List.of());
        addFraudForClient(client, CHARGEBACK, CONFIRMED, List.of());
    }

    @AfterAll
    static void teardown() throws Exception {
        deleteObjectFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        deleteUserBO(client.getUcid());
        cleanUserRestriction(client.getUcid());
    }

    @AfterEach
    void deleteFraudsForClient() throws Exception {
        deleteUserFromAbuseRegistry(client.getUcid());
    }

    @Test
    @AllureId("2147")
    @DisplayName("trading_ops_senior user should have ability to remove confirmed trading fraud type")
    void seniorOpsUserShouldRemoveConfirmedTradingFraudType() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsTradingOpsSeniorUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        resolvePage.openReportFraudForm();

        assertThat(
                "Verify confirmed fraud has info icon that it can't be deleted",
                resolvePage.isInfoIconVisibleForFraudType(CHARGEBACK.getName()),
                is(true));

        resolvePage.deleteFraudByNameNoPopup(BONUS_ABUSE.getName());
        resolvePage.deleteFraudByNameNoPopup(CPA_ABUSE.getName());
        resolvePage.applyFraudManagement("test comment");
        List<AbuserFraudType> abuserFraudTypes = getObjectsFromDB(
                DbName.POSTGRES,
                AR_ABUSER_FRAUD_TYPE_TABLE_NAME,
                String.format(
                        "ucid = '%s' and fraud_type_code IN ('%s', '%s')",
                        client.getUcid(), BONUS_ABUSE.getCode(), CPA_ABUSE.getCode()),
                AbuserFraudType.class);

        assertThat(
                "Verify all fraud types have status CLEANED",
                abuserFraudTypes,
                everyItem(hasProperty("status", equalTo(CLEANED.getStatus()))));
    }

    @Test
    @AllureId("2148")
    @DisplayName("trading_ops_senior user should not have ability to remove confirmed payment fraud type")
    void seniorOpsUserShouldNotRemoveConfirmedPaymentFraudType() throws Exception {
        kafka.produceMessages(alert.alertId, KAFKA_TOPIC_ALERTS, objectMapper.writeValueAsString(alert));
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsTradingOpsSeniorUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        resolvePage.openResolveSuspicious();

        assertThat(
                "Verify confirmed fraud has info icon that it can't be deleted",
                resolvePage.isInfoIconVisibleForFraudType(CHARGEBACK.getName()),
                is(true));

        resolvePage.deleteFraudByNameNoPopup(BONUS_ABUSE.getName());
        resolvePage.deleteFraudByNameNoPopup(CPA_ABUSE.getName());
        resolvePage.resolveNoActions("test comment");
        List<AbuserFraudType> abuserFraudTypes = getObjectsFromDB(
                DbName.POSTGRES,
                AR_ABUSER_FRAUD_TYPE_TABLE_NAME,
                String.format(
                        "ucid = '%s' and fraud_type_code IN ('%s', '%s')",
                        client.getUcid(), BONUS_ABUSE.getCode(), CPA_ABUSE.getCode()),
                AbuserFraudType.class);

        assertThat(
                "Verify all fraud types have status CLEANED",
                abuserFraudTypes,
                everyItem(hasProperty("status", equalTo(CLEANED.getStatus()))));
    }

    @Test
    @AllureId("2149")
    @DisplayName("trading_ops user should not have ability to remove confirmed fraud type")
    void notSeniorOpsUserShouldNotRemoveConfirmedTradingFraudType() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsTradingOpsUser();
        investigationPage.navigateToClient(client.getUcid());
        resolvePage.openReportFraudForm();
        assertThat(
                "Verify confirmed fraud has info icon that it can't be deleted",
                resolvePage.isInfoIconVisibleForFraudType(BONUS_ABUSE.getName()),
                is(true));
        assertThat(
                "Verify confirmed fraud has info icon that it can't be deleted",
                resolvePage.isInfoIconVisibleForFraudType(CPA_ABUSE.getName()),
                is(true));
        assertThat(
                "Verify confirmed fraud has info icon that it can't be deleted",
                resolvePage.isInfoIconVisibleForFraudType(CHARGEBACK.getName()),
                is(true));
    }
}
