package tests.vindex_backoffice_ui_tests.investigationTool;

import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.kafka.alerts.RuleAlertFactory.generateRuleAlertByUcid;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.AlertType.TRADING;
import static helpers.database.AiDecisionHelper.ageAlertsToBypassCoolingPeriod;
import static helpers.database.AiDecisionHelper.createAiDecision;
import static helpers.database.AuHelper.cleanClientAudit;
import static helpers.database.BoHelper.deleteUserBO;
import static helpers.database.BoHelper.getClientsInvestigationsDb;
import static helpers.database.DbHelper.deleteObjectsFromDb;
import static helpers.database.DbHelper.insertObjectToDb;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static utils.Constants.*;

import business_objects.db.backoffice_db.Investigation;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.kafka.alerts.RuleAlert;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.data.ClientHelper;
import helpers.database.DbName;
import io.qameta.allure.AllureId;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

public class AutoCloseFalsePositiveInvestigationTest extends TestBaseWeb {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private ClientHelper client;
    private String ucid;
    private CrmTbUserObject clientUser;

    @BeforeEach
    void setupEach() {
        client = getRandomVantageClientAllFields();
        ucid = client.getUcid();
        clientUser = generateUserByClient(client);

        insertObjectToDb(CRM_USER_TABLE_NAME, clientUser);
    }

    @AfterEach
    void cleanUpEach() throws Exception {
        deleteUserBO(ucid);
        cleanClientAudit(ucid);
        deleteObjectsFromDb(DbName.CLICKHOUSE, CRM_USER_TABLE_NAME, "ucid", List.of(ucid));
    }

    @Test
    @AllureId("2473")
    @DisplayName("Auto-close investigation: successful when all alerts are Normal")
    void autoCloseInvestigationAllNormalTest() throws Exception {
        RuleAlert alert1 = generateRuleAlertByUcid(client);
        RuleAlert alert2 = generateRuleAlertByUcid(client);

        kafka.produceMessages(alert1.alertId, KAFKA_TOPIC_ALERTS, objectMapper.writeValueAsString(alert1));
        kafka.produceMessages(alert2.alertId, KAFKA_TOPIC_ALERTS, objectMapper.writeValueAsString(alert2));

        Awaitility.await().atMost(5, TimeUnit.SECONDS).until(investigationExists(ucid));

        ageAlertsToBypassCoolingPeriod(List.of(alert1.alertId, alert2.alertId));

        createAiDecision(client, alert1, "Normal", true);
        createAiDecision(client, alert2, "Normal", true);

        Awaitility.await()
                .atMost(15, TimeUnit.SECONDS)
                .pollInterval(1, TimeUnit.SECONDS)
                .until(investigationIsClosed(ucid));
    }

    @Test
    @AllureId("2474")
    @DisplayName("Auto-close investigation: do not close when at least one alert is not Normal")
    void doNotCloseInvestigationWithTruePositiveAlertTest() throws Exception {
        RuleAlert alert1 = generateRuleAlertByUcid(client);
        RuleAlert alert2 = generateRuleAlertByUcid(client);

        kafka.produceMessages(alert1.alertId, KAFKA_TOPIC_ALERTS, objectMapper.writeValueAsString(alert1));
        kafka.produceMessages(alert2.alertId, KAFKA_TOPIC_ALERTS, objectMapper.writeValueAsString(alert2));

        Awaitility.await().atMost(15, TimeUnit.SECONDS).until(investigationExists(ucid));

        ageAlertsToBypassCoolingPeriod(List.of(alert1.alertId, alert2.alertId));

        createAiDecision(client, alert1, "Normal", true);
        createAiDecision(client, alert2, "Fraud", true);

        // Scheduler runs every 5 seconds. Wait 7 seconds to ensure a full cycle has passed.
        Thread.sleep(7000);

        Investigation latestInvestigation = getLatestInvestigation(ucid);
        assertFalse(latestInvestigation == null, "Investigation should exist");
        assertEquals("NEW", latestInvestigation.getStatus(), "Investigation should remain NEW");
    }

    @Test
    @AllureId("2475")
    @DisplayName("Auto-close investigation: do not close when not all alerts have AI decisions")
    void doNotCloseInvestigationWhenNotAllAlertsHaveDecisionsTest() throws Exception {
        RuleAlert alert1 = generateRuleAlertByUcid(client);
        RuleAlert alert2 = generateRuleAlertByUcid(client);

        kafka.produceMessages(alert1.alertId, KAFKA_TOPIC_ALERTS, objectMapper.writeValueAsString(alert1));
        kafka.produceMessages(alert2.alertId, KAFKA_TOPIC_ALERTS, objectMapper.writeValueAsString(alert2));

        Awaitility.await().atMost(15, TimeUnit.SECONDS).until(investigationExists(ucid));

        ageAlertsToBypassCoolingPeriod(List.of(alert1.alertId, alert2.alertId));

        // Create an AI decision for only one alert
        createAiDecision(client, alert1, "Normal", true);

        // Wait 7 seconds to let the scheduler run
        Thread.sleep(7000);

        Investigation latestInvestigation = getLatestInvestigation(ucid);
        assertFalse(latestInvestigation == null, "Investigation should exist");
        assertEquals(
                "NEW",
                latestInvestigation.getStatus(),
                "Investigation should remain NEW because not all alerts have decisions");
    }

    @Test
    @AllureId("2476")
    @DisplayName("Auto-close investigation: do not close if investigation is already taken to work (status is not NEW)")
    void doNotCloseInvestigationIfAlreadyAssignedTest() throws Exception {
        RuleAlert alert1 = generateRuleAlertByUcid(client);
        RuleAlert alert2 = generateRuleAlertByUcid(client);

        kafka.produceMessages(alert1.alertId, KAFKA_TOPIC_ALERTS, objectMapper.writeValueAsString(alert1));
        kafka.produceMessages(alert2.alertId, KAFKA_TOPIC_ALERTS, objectMapper.writeValueAsString(alert2));

        Awaitility.await().atMost(15, TimeUnit.SECONDS).until(investigationExists(ucid));

        // Take client into investigation via UI
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(client.getUcid());
        investigationPage.waitForPageToLoad();
        investigationPage.investigateClientCard();

        // Verify status has changed to INVESTIGATING
        Investigation assignedInvestigation = getLatestInvestigation(ucid);
        assertEquals(
                "INVESTIGATING",
                assignedInvestigation.getStatus(),
                "Investigation status should be INVESTIGATING after taking to work");

        // Age alerts and create decisions
        ageAlertsToBypassCoolingPeriod(List.of(alert1.alertId, alert2.alertId));
        createAiDecision(client, alert1, "Normal", true);
        createAiDecision(client, alert2, "Normal", true);

        // Wait 7 seconds to let the scheduler run
        Thread.sleep(7000);

        // Verify that the investigation was not auto-closed
        Investigation latestInvestigation = getLatestInvestigation(ucid);
        assertFalse(latestInvestigation == null, "Investigation should exist");
        assertEquals(
                "INVESTIGATING",
                latestInvestigation.getStatus(),
                "Investigation should not be automatically closed because it was taken to work");
    }

    private Investigation getLatestInvestigation(String ucid) throws Exception {
        List<Investigation> investigations = getClientsInvestigationsDb(ucid, TRADING);
        if (investigations.isEmpty()) {
            return null;
        }
        investigations.sort(Comparator.comparing(Investigation::getId).reversed());
        return investigations.getFirst();
    }

    private Callable<Boolean> investigationExists(String ucid) {
        return () -> getLatestInvestigation(ucid) != null;
    }

    private Callable<Boolean> investigationIsClosed(String ucid) {
        return () -> {
            Investigation inv = getLatestInvestigation(ucid);
            return inv != null && "COMPLETED".equals(inv.getStatus());
        };
    }
}
