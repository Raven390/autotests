package tests.vindexBackofficeUiTests;

import businessObjects.db.backofficeDb.client.Client;
import businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObject;
import businessObjects.kafka.alerts.RuleAlert;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.database.DbName;
import helpers.kafka.KafkaHelper;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

import java.util.List;

import static businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObjectFactory.generateUserByClient;
import static businessObjects.kafka.alerts.RuleAlertFactory.generateRuleAlertByUcid;
import static businessObjects.ui.user.UserFactory.coreUser;
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
        investigationPage.navigate();
        keycloackPage.loginAsCoreUser();
        investigationPage.waitForPageToLoad();
        investigationPage.filterUnassigned();
        investigationPage.waitForPageToLoad();
        investigationPage.scrollClientCardsToBottom();
        investigationPage.assignClientByClientId(String.valueOf(crmTbUser.userId));
        investigationPage.filterAssignedMe();
        investigationPage.waitForPageToLoad();
        investigationPage.verifyClientCardWithClientIdVisible(String.valueOf(crmTbUser.userId));
        List<Client> clientList = getObjectsFromDB(DbName.BO, BO_CLIENT_TABLE_NAME, String.format("ucid = '%s'", crmTbUser.ucid), Client.class);
        assertThat("Assert that client is assigned to current user in db table", clientList.getFirst().assignedUserId, equalTo(coreUser().getId()));
    }

    @AfterAll
    public static void teardown() throws Exception {
        deleteEntryFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", crmTbUser.ucid));
        closeAlert(crmTbUser.ucid);
    }
}
