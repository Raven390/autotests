package tests.vindex_backoffice_ui_tests.alertHistory;

import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.kafka.alerts.RuleAlertFactory.generateRuleAlertByUcid;
import static business_objects.ui.user.UserFactory.autotestUserOne;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.DbHelper.deleteEntryFromDb;
import static helpers.database.DbHelper.insertObjectToDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.kafka.alerts.RuleAlert;
import business_objects.ui.user.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.data.ClientHelper;
import helpers.kafka.KafkaHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag(TEAM_BACKOFFICE)
@Tag(LAYER_WEB)
@Feature("BMS-2858 Add QA QC filters for alerts history")
class AlertHistoryQaQcFiltersTest extends TestBaseWeb {

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
    @DisplayName("Setup data before test execution")
    void setupData() {
        investigationPage.navigateToClient(client.getUcid());
        alertsPage.waitForPageToLoad();
        resolvePage.openResolveSuspicious();
        resolvePage.resolveNoActions("Test alerts history comment resolve");
        alertHistoryPage.openAlertHistory();
        alertHistoryPage.openAlertHistoryDrawerByClient(client);
        alertHistoryPage.fillQaQcComment("Test alerts history QA QC filters");
        alertHistoryPage.clickCorrectResolutionButton();
    }

    @Test
    @Order(2)
    @AllureId("1902")
    @DisplayName("Verify Alert history QC result filter")
    void verifyAlertHistoryQaQcFilters1() {
        alertHistoryPage.clickFilterButton();
        alertHistoryPage.selectQcCheckResultFilter("Correct");
        alertHistoryPage.applyFilter();
        assertThat(
                "Verify only selected QC result is displayed",
                alertHistoryPage.getQcResultValues(),
                everyItem(is("Correct")));
    }

    @Test
    @Order(3)
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1903")
    @DisplayName("Verify Alert history reviewer filter")
    void verifyAlertHistoryQaQcFilters2() {
        alertHistoryPage.clickFilterButton();
        String fullName = String.format("%s %s", user.getFirstName(), user.getLastName());
        alertHistoryPage.selectReviewerFilter(fullName);
        alertHistoryPage.applyFilter();
        assertThat(
                "Verify only selected reviewer is displayed",
                alertHistoryPage.getReviewerValues(),
                everyItem(is(fullName)));
    }

    @AfterAll
    static void teardown() {
        deleteEntryFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
    }
}
