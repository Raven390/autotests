package tests.vindex_backoffice_ui_tests.alertHistory;

import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.kafka.alerts.RuleAlertFactory.generateRuleAlertByUcid;
import static business_objects.ui.user.UserFactory.autotestUserOne;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.Brand.VANTAGE;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Utils.getCurrentDate;

import business_objects.db.backoffice_db.alert.Alert;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.kafka.alerts.RuleAlert;
import business_objects.ui.user.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.data.ClientHelper;
import helpers.database.DbName;
import helpers.kafka.KafkaHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import java.util.List;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

@Tag(TEAM_BACKOFFICE)
@Tag(LAYER_WEB)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Feature("BMS-1446 Alert history")
class AlertHistoryTest extends TestBaseWeb {

    private static final KafkaHelper kafka = new KafkaHelper();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final ClientHelper client = getRandomVantageClientAllFields();
    private static final CrmTbUserObject crmTbUser = generateUserByClient(client);
    private static final RuleAlert alert = generateRuleAlertByUcid(client.getUcid());
    private static final User user = autotestUserOne();

    @BeforeAll
    static void setup() throws JsonProcessingException {
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        kafka.produceMessage(alert.alertId, objectMapper.writeValueAsString(alert), KAFKA_TOPIC_ALERTS);
    }

    @BeforeEach
    void goToAlertsHistoryPage() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(client.getUcid());
        alertsPage.waitForPageToLoad();
        alertHistoryPage.openAlertHistory();
    }

    @Test
    @Order(1)
    @DisplayName("Setup data before Alert history test execution")
    void setupData() {
        investigationPage.navigateToClient(client.getUcid());
        alertsPage.waitForPageToLoad();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveNoActions("Test alerts history comment resolve");
    }

    @Test
    @Order(2)
    @AllureId("1151")
    @DisplayName("Verify Alert history title, table headers, data")
    void verifyAlertHistory1() {
        String dateTimeRegex = "\\d{4}-\\d{2}-\\d{2}\\d{2}:\\d{2}:\\d{2}";
        assertThat("Verify alert history page title", alertHistoryPage.getTitle(), is("Alert history"));
        assertThat(
                "Verify alert history table headers",
                alertHistoryPage.getTableHeaders(),
                contains(
                        "CLIENT",
                        "ALERTED RULE",
                        "CREATED",
                        "RESOLVED",
                        "DURATION",
                        "INVESTIGATOR",
                        "RESOLUTION",
                        "QC",
                        "REVIEWER",
                        "QC NOTE"));
        assertThat(
                "Verify alert history client value",
                alertHistoryPage.getClientValues().getFirst(),
                is(String.format("%s %s%s", crmTbUser.firstName, crmTbUser.lastName, client.getUserId())));
        assertThat(
                "Verify alert history rule name value",
                alertHistoryPage.getAlertedRuleValues().getFirst(),
                is(alert.rule.name));
        assertThat(
                "Verify alert history created cell value",
                alertHistoryPage.getCreatedValues().getFirst(),
                matchesPattern(dateTimeRegex));
        assertThat(
                "Verify alert history resolved cell value",
                alertHistoryPage.getResolvedValues().getFirst(),
                matchesPattern(dateTimeRegex));
        assertThat(
                "Verify alert history duration value",
                alertHistoryPage.getDurationValues().getFirst(),
                is("0 min"));
        assertThat(
                "Verify alert history investigator value",
                alertHistoryPage.getInvestigatorValues().getFirst(),
                is(String.format("%s %s", user.getFirstName(), user.getLastName())));
    }

    @Test
    @Order(3)
    @AllureId("1152")
    @DisplayName("Verify Alert history brand filter")
    void verifyAlertHistory2() {
        alertHistoryPage.clickFilterButton();
        alertHistoryPage.selectBrandFilter(client.getBrand());
        alertHistoryPage.applyFilter();
        assertThat(
                "Verify only selected brand is displayed",
                alertHistoryPage.getUcidBrandValues(),
                everyItem(is(VANTAGE.getUcidBrand())));
    }

    @Test
    @Order(4)
    @AllureId("1153")
    @DisplayName("Verify Alert history rule filter")
    void verifyAlertHistory3() {
        alertHistoryPage.clickFilterButton();
        alertHistoryPage.selectRulesFilter(alert.rule.name);
        alertHistoryPage.applyFilter();
        assertThat(
                "Verify only selected rule is displayed",
                alertHistoryPage.getAlertedRuleValues(),
                everyItem(is(alert.rule.name)));
    }

    @Test
    @Order(5)
    @AllureId("1154")
    @DisplayName("Verify Alert history created filter")
    void verifyAlertHistory4() {
        alertHistoryPage.clickFilterButton();
        String date = getCurrentDate();
        alertHistoryPage.selectCreationDate(date, date);
        alertHistoryPage.applyFilter();
        assertThat(
                "Verify only selected creation date is displayed",
                alertHistoryPage.getCreatedValues(),
                everyItem(startsWith(date)));
    }

    @Test
    @Order(6)
    @AllureId("1155")
    @DisplayName("Verify Alert history resolved filter")
    void verifyAlertHistory5() {
        alertHistoryPage.clickFilterButton();
        String date = getCurrentDate();
        alertHistoryPage.selectResolutionDate(date, date);
        alertHistoryPage.applyFilter();
        assertThat(
                "Verify only selected resolution date is displayed",
                alertHistoryPage.getResolvedValues(),
                everyItem(startsWith(date)));
    }

    @Test
    @Order(7)
    @AllureId("1156")
    @DisplayName("Verify Alert history investigator filter")
    void verifyAlertHistory6() {
        alertHistoryPage.clickFilterButton();
        String fullName = String.format("%s %s", user.getFirstName(), user.getLastName());
        alertHistoryPage.selectInvestigatorFilter(fullName);
        alertHistoryPage.applyFilter();
        assertThat(
                "Verify only selected resolution date is displayed",
                alertHistoryPage.getInvestigatorValues(),
                everyItem(is(fullName)));
    }

    @Test
    @AllureId("2143")
    @Order(8)
    @Feature("BMS-3200 Total number of alerts under filters in alert history")
    @DisplayName("Verify Alert history brand filter")
    void alertFilterButtonHaveCounterTest() throws Exception {
        alertHistoryPage.clickFilterButton();
        alertHistoryPage.fillFilterById(client.getUserId().toString());
        List<Alert> alerts = getObjectsFromDB(
                DbName.POSTGRES, BO_ALERT_TABLE_NAME, "client_ucid = '" + client.getUcid() + "'", Alert.class);
        int dBAlertCounter = alerts.size();
        alertHistoryPage.checkCountOfAlertsInFilter(dBAlertCounter);
    }

    @AfterAll
    static void teardown() {
        deleteObjectFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
    }
}
