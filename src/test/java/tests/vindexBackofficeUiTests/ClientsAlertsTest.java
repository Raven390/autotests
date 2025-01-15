package tests.vindexBackofficeUiTests;

import businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObject;
import businessObjects.kafka.alerts.RuleAlert;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.kafka.KafkaHelper;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

import static businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObjectFactory.generateUserByClient;
import static businessObjects.kafka.alerts.RuleAlertFactory.generateRuleAlertByUcid;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.BoHelper.closeAlert;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.matchesPattern;
import static utils.Constants.*;

public class ClientsAlertsTest extends TestBaseWeb {

    private static final CrmTbUserObject crmTbUser = generateUserByClient(getRandomVantageClientAllFields());
    private static final KafkaHelper kafka = new KafkaHelper();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    public static void setup() throws Exception {
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        RuleAlert alert = generateRuleAlertByUcid(crmTbUser.ucid);
        kafka.produceMessage(alert.alertId, objectMapper.writeValueAsString(alert), KAFKA_TOPIC_ALERTS);
        closeAlert(crmTbUser.ucid);
        RuleAlert alert1 = generateRuleAlertByUcid(crmTbUser.ucid);
        alert1.rule.trigger = "clientRegistrationNew";
        kafka.produceMessage(alert1.alertId, objectMapper.writeValueAsString(alert1), KAFKA_TOPIC_ALERTS);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("558")
    @DisplayName("Verify clients alerts functionality, elements, filtration, refresh, sorting")
    public void clientsAlertsTest() throws Exception {
        investigationPage.navigate();
        keycloackPage.loginAsCoreUser();
        investigationPage.waitForPageToLoad();
        investigationPage.clickSuspiciousClientsFiltration();
        investigationPage.selectBrandFilterByText(crmTbUser.brand);
        investigationPage.clickApplyFiltrationButton();
        investigationPage.filterUnassigned();
        investigationPage.waitForPageToLoad();
        investigationPage.clickClientCardByClientId(String.valueOf(crmTbUser.userId));
        alertsPage.waitForPageToLoad();

        assertThat("Verify text of the alerts counter", alertsPage.getAlertsCountText(), equalTo("1 active"));
        assertThat("Verify there is 1 alert", alertsPage.getAlertsCount(), equalTo(1));
        assertThat("Verify alert date value", alertsPage.getAlertsDatesList().getFirst(), equalTo("Today"));
        String timePattern = "^([01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d$";
        assertThat("Verify alert time value", alertsPage.getAlertsTimesList().getFirst(), matchesPattern(timePattern));
        assertThat("Verify alert rule name value", alertsPage.getAlertsRuleNamesList().getFirst(), equalTo("Registration"));
        assertThat("Verify alert rule trigger value", alertsPage.getAlertsRuleTriggersList().getFirst(), equalTo("clientRegistrationNew"));
        assertThat("Verify alert attributes are present", alertsPage.getAlertsAttributesList().size(), equalTo(1));

        alertsPage.filterAllAlerts();
        assertThat("Verify there are 2 alerts", alertsPage.getAlertsCount(), equalTo(2));
        assertThat("Verify alerts are sorted by time desc", alertsPage.getAlertsRuleTriggersList().getFirst(), equalTo("clientRegistrationNew"));
        assertThat("Verify text of the alerts counter", alertsPage.getAlertsCountText(), equalTo("2 in total"));
        RuleAlert alert2 = generateRuleAlertByUcid(crmTbUser.ucid);
        kafka.produceMessage(alert2.alertId, objectMapper.writeValueAsString(alert2), KAFKA_TOPIC_ALERTS);
        alertsPage.clickRefreshButton();
        assertThat("Verify there are 3 alerts", alertsPage.getAlertsCount(), equalTo(3));

        alertsPage.filterClosedAlerts();
        assertThat("Verify text of the alerts counter", alertsPage.getAlertsCountText(), equalTo("1 closed"));
        String alertStatusTooltipPattern = "^\\d{4}-\\d{2}-\\d{2} at [0-2]\\d:[0-5]\\d$";
        assertThat("Verify text of the alert status tooltip", alertsPage.getFirstAlertStatus(), matchesPattern(alertStatusTooltipPattern));
    }

    @AfterAll
    public static void teardown() throws Exception {
        deleteEntryFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", crmTbUser.ucid));
        closeAlert(crmTbUser.ucid);
    }
}
