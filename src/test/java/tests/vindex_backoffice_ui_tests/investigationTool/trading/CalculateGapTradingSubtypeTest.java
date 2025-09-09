package tests.vindex_backoffice_ui_tests.investigationTool.trading;

import business_objects.db.abuse_registry_db.AbuserHistory;
import business_objects.db.abuse_registry_db.PendingProcessing;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.kafka.alerts.RuleAlert;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.data.ClientHelper;
import helpers.database.DbName;
import helpers.kafka.KafkaHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

import java.io.IOException;
import java.util.List;

import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.kafka.alerts.RuleAlertFactory.generateRuleAlertByUcid;
import static helpers.api.AbuseRegistryHelper.addFraudForClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.FraudSubtype.*;
import static helpers.data.enums.FraudType.GAP_TRADING;
import static helpers.data.enums.FraudTypeStatus.CONFIRMED;
import static helpers.database.ArHelper.deleteUserFromAbuseRegistry;
import static helpers.database.BoHelper.closeAlert;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

@Feature("BMS-1830. Calculate subtype for 'Gap Trading'")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag(TEAM_BACKOFFICE)
@Tag(LAYER_WEB)
class CalculateGapTradingSubtypeTest extends TestBaseWeb {

    private static final KafkaHelper kafka = new KafkaHelper();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final ClientHelper client = getRandomVantageClientAllFields();
    private static CrmTbUserObject crmTbUser;

    @BeforeAll
    static void setup() {
        crmTbUser = generateUserByClient(client);
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
    }

    @AfterAll
    static void teardown() throws Exception {
        deleteEntryFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", crmTbUser.ucid));
        closeAlert(crmTbUser.ucid);
        deleteUserFromAbuseRegistry(client.getUcid());
    }

    @Test
    @Order(1)
    @AllureId("1538")
    @DisplayName("Gap trading subtype is 'first time' when no previous frauds")
    void calculateGapTradingSubtypeTest1() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        resolvePage.openReportFraudForm();
        resolvePage.addFraud(GAP_TRADING, CONFIRMED);
        assertThat(resolvePage.getSelectedFraud(), is(String.format("%s (%s)", GAP_TRADING.getName(), FIRST_TIME.getName().toLowerCase())));
    }

    @Test
    @Order(2)
    @AllureId("1539")
    @DisplayName("Gap trading subtype is 'second time' when 1 fraud present previously")
    void calculateGapTradingSubtypeTest2() throws IOException {
        addFraudForClient(client, GAP_TRADING, FIRST_TIME, CONFIRMED, List.of());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        resolvePage.openReportFraudForm();
        resolvePage.addFraud(GAP_TRADING, CONFIRMED);
        assertThat(resolvePage.getSelectedFraud(), is(String.format("%s (%s)", GAP_TRADING.getName(), SECOND_TIME.getName().toLowerCase())));
    }

    @Test
    @Order(3)
    @AllureId("1540")
    @DisplayName("Gap trading subtype is 'Three times and more' when 2 frauds present previously")
    void calculateGapTradingSubtypeTest3() throws IOException {
        addFraudForClient(client, GAP_TRADING, SECOND_TIME, CONFIRMED, List.of());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        resolvePage.openReportFraudForm();
        resolvePage.addFraud(GAP_TRADING, CONFIRMED);
        assertThat(resolvePage.getSelectedFraud(), is(String.format("%s (%s)", GAP_TRADING.getName(), MULTIPLE_TIMES.getName().toLowerCase())));
    }

    @Test
    @Order(4)
    @AllureId("1541")
    @DisplayName("Gap trading subtype does not take into account pending_processing frauds")
    void calculateGapTradingSubtypeTest4() throws Exception {
        List<AbuserHistory> abuserHistory = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_HISTORY_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()), AbuserHistory.class);
        PendingProcessing pendingProcessing = new PendingProcessing(client.getUcid(), GAP_TRADING.getCode(), SECOND_TIME.getCode(), abuserHistory.getLast().getId());
        insertObjectToDb(DbName.POSTGRES, AR_PENDING_PROCESSING_TABLE_NAME, pendingProcessing);
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        resolvePage.openReportFraudForm();
        resolvePage.addFraud(GAP_TRADING, CONFIRMED);
        assertThat(resolvePage.getSelectedFraud(), is(String.format("%s (%s)", GAP_TRADING.getName(), SECOND_TIME.getName().toLowerCase())));
    }

    @Test
    @Order(5)
    @AllureId("1542")
    @DisplayName("Gap trading subtype shown correctly in resolve")
    void calculateGapTradingSubtypeTest5() throws Exception {
        RuleAlert alert = generateRuleAlertByUcid(client);
        kafka.produceMessage(alert.alertId, objectMapper.writeValueAsString(alert), KAFKA_TOPIC_ALERTS);
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        resolvePage.openResolveSuspicious();
        resolvePage.addFraud(GAP_TRADING, CONFIRMED);
        assertThat(resolvePage.getSelectedFraud(), is(String.format("%s (%s)", GAP_TRADING.getName(), SECOND_TIME.getName().toLowerCase())));
    }
}
