package tests.vindex_backoffice_ui_tests.investigationTool.trading;

import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.kafka.alerts.RuleAlertFactory.generateRuleAlertByUcid;
import static business_objects.ui.user.UserFactory.autotestUserOPS24;
import static business_objects.ui.user.UserFactory.autotestUserOne;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.HistoryAction.*;
import static helpers.database.ArHelper.deleteUserFromAbuseRegistry;
import static helpers.database.BoHelper.*;
import static helpers.database.CleanTableHelper.*;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNull;
import static utils.Constants.*;

import business_objects.db.backoffice_db.Investigation;
import business_objects.db.backoffice_db.InvestigationHistory.InvestigationHistoryObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.kafka.alerts.RuleAlert;
import business_objects.ui.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.data.ClientHelper;
import helpers.kafka.KafkaHelper;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import java.util.List;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

@Feature("BMS-2849 Complete assignment")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag(TEAM_BACKOFFICE)
@Tag(LAYER_WEB)
class AssignSuspiciousClientTest extends TestBaseWeb {

    private static final KafkaHelper kafka = new KafkaHelper();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final ClientHelper client = getRandomVantageClientAllFields();
    private static final CrmTbUserObject crmUser = generateUserByClient(client);
    private static final RuleAlert alert = generateRuleAlertByUcid(client);

    @BeforeAll
    static void setup() throws InterruptedException {
        insertObjectToDb(CRM_USER_TABLE_NAME, crmUser);
    }

    @AfterAll
    static void teardown() throws Exception {
        cleanCrmUserTableByClient(client.getUcid());
        deleteUserBO(client.getUcid());
        cleanUserAudit(client.getUcid());
        deleteUserFromAbuseRegistry(client.getUcid());
    }

    @BeforeEach
    void setupEach() throws Exception {
        deleteUserBO(client.getUcid());
        cleanUserAudit(client.getUcid());
        deleteUserFromAbuseRegistry(client.getUcid());
        cleanUserRestrictionGeneral(client.getUcid());
    }

    @Test
    @AllureId("2103")
    @DisplayName("Initial assign test")
    void assignTest1() throws Exception {
        User currentUser = autotestUserOne();
        kafka.produceMessages(alert.alertId, KAFKA_TOPIC_ALERTS, objectMapper.writeValueAsString(alert));
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(client.getUcid());
        assignDrawer.openAssignDrawer();
        assignDrawer.assignClientToMyself();
        Allure.step("Check clients investigation record In DB");
        List<Investigation> investigations = getClientsInvestigationsDb(client.getUcid());
        assertThat("there is only one investigation in DB", investigations.size(), is(1));
        Investigation investigation = investigations.getFirst();
        assertThat("check id of assigned user", investigation.getAssignedUserId(), is(currentUser.getId()));
        assertThat("check id of user modifier", investigation.getModifiedByUserId(), is(currentUser.getId()));
        Allure.step("Check clients investigation history record In DB");
        InvestigationHistoryObject historyRecord =
                getClientsInvestigationsHistoryLastDb((Integer) investigation.getId());
        assertThat("check assigned_to_user_id", historyRecord.getAssignedToUserId(), is(currentUser.getId()));
        assertThat("check action", historyRecord.getAssignedToUserId(), is(currentUser.getId()));
        assertNull(historyRecord.getReassignedFromUserId());
        assertThat("check reassigned_from_user_id", historyRecord.getReassignedFromUserId(), nullValue());
        assertThat("check actor_user_id", historyRecord.getActorUserId(), is(currentUser.getId()));
        assertThat("check action", historyRecord.getAction(), is(ASSIGNED.getDisplayName()));
    }

    @Test
    @AllureId("2105")
    @DisplayName("Reassign assign test")
    void assignTest2() throws Exception {
        User currentUser = autotestUserOne();
        User anotherUser = autotestUserOPS24();
        kafka.produceMessages(alert.alertId, KAFKA_TOPIC_ALERTS, objectMapper.writeValueAsString(alert));
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(client.getUcid());
        assignDrawer.openAssignDrawer();
        assignDrawer.assignClientToMyself();
        assignDrawer.openAssignDrawer();
        assignDrawer.assignClientToUser(anotherUser.getFullName());
        Allure.step("Check clients investigation record In DB");
        List<Investigation> investigations = getClientsInvestigationsDb(client.getUcid());
        assertThat("there is only one investigation in DB", investigations.size(), is(1));
        Investigation investigation = investigations.getFirst();
        assertThat("check id of assigned user", investigation.getAssignedUserId(), is(anotherUser.getId()));
        assertThat("check id of user modifier", investigation.getModifiedByUserId(), is(currentUser.getId()));
        Allure.step("Check clients investigation history record In DB");
        InvestigationHistoryObject historyRecord =
                getClientsInvestigationsHistoryLastDb((Integer) investigation.getId());
        assertThat("check assigned_to_user_id", historyRecord.getAssignedToUserId(), is(anotherUser.getId()));
        assertThat("check reassigned_from_user_id", historyRecord.getReassignedFromUserId(), is(currentUser.getId()));
        assertThat("check actor_user_id", historyRecord.getActorUserId(), is(currentUser.getId()));
        assertThat("check action", historyRecord.getAction(), is(REASSIGNED.getDisplayName()));
    }

    @Test
    @AllureId("2104")
    @DisplayName("Unassign assign test")
    void assignTest3() throws Exception {
        User currentUser = autotestUserOne();
        kafka.produceMessages(alert.alertId, KAFKA_TOPIC_ALERTS, objectMapper.writeValueAsString(alert));
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(client.getUcid());
        assignDrawer.openAssignDrawer();
        assignDrawer.assignClientToMyself();
        assignDrawer.openAssignDrawer();
        assignDrawer.unassignClient();
        Allure.step("Check clients investigation record In DB");
        List<Investigation> investigations = getClientsInvestigationsDb(client.getUcid());
        assertThat("there is only one investigation in DB", investigations.size(), is(1));
        Investigation investigation = investigations.getFirst();
        assertThat("check id of assigned user", investigation.getAssignedUserId(), nullValue());
        assertThat("check id of user modifier", investigation.getModifiedByUserId(), is(currentUser.getId()));
        Allure.step("Check clients investigation history record In DB");
        InvestigationHistoryObject historyRecord =
                getClientsInvestigationsHistoryLastDb((Integer) investigation.getId());
        assertThat("check assigned_to_user_id", historyRecord.getAssignedToUserId(), nullValue());
        assertThat("check reassigned_from_user_id", historyRecord.getReassignedFromUserId(), is(currentUser.getId()));
        assertThat("check actor_user_id", historyRecord.getActorUserId(), is(currentUser.getId()));
        assertThat("check action", historyRecord.getAction(), is(UNASSIGNED.getDisplayName()));
    }
}
