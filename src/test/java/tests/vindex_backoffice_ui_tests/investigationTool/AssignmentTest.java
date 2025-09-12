package tests.vindex_backoffice_ui_tests.investigationTool;

import business_objects.db.backoffice_db.client.Client;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.kafka.alerts.RuleAlert;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.database.DbName;
import helpers.kafka.KafkaHelper;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

import java.util.List;

import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.kafka.alerts.RuleAlertFactory.generateRuleAlertByUcid;
import static business_objects.ui.user.UserFactory.autotestUserOne;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.BoHelper.closeAlert;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static utils.Constants.*;

public class AssignmentTest extends TestBaseWeb {

    private static final CrmTbUserObject crmTbUser = generateUserByClient(getRandomVantageClientAllFields());

    @BeforeAll
    public static void setup() throws Exception {
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        KafkaHelper kafka = new KafkaHelper();
        ObjectMapper objectMapper = new ObjectMapper();
        RuleAlert alert = generateRuleAlertByUcid(crmTbUser.ucid);
        kafka.produceMessage(alert.alertId, objectMapper.writeValueAsString(alert), KAFKA_TOPIC_ALERTS);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("548")
    @DisplayName("Assign a client to the current user and verify")
    public void assignClientAndVerifyTest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        investigationPage.investigateClientCard();
        investigationPage.filterAssignedMe();
        investigationPage.waitForPageToLoad();
        investigationPage.verifyClientCardWithClientIdVisible(String.valueOf(crmTbUser.userId));
        List<Client> clientList = getObjectsFromDB(DbName.BACKOFFICE, BO_CLIENT_TABLE_NAME, String.format("ucid = '%s'", crmTbUser.ucid), Client.class);
        assertThat("Assert that client is assigned to current user in db table", clientList.getFirst().assignedUserId, equalTo(autotestUserOne().getId()));
    }

    @AfterAll
    public static void teardown() throws Exception {
        deleteEntryFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", crmTbUser.ucid));
        closeAlert(crmTbUser.ucid);
    }
}
