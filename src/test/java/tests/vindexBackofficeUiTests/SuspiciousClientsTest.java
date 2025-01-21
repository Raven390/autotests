package tests.vindexBackofficeUiTests;

import businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObject;
import businessObjects.kafka.alerts.RuleAlert;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.kafka.KafkaHelper;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

import java.sql.SQLException;

import static businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObjectFactory.generateUserByClient;
import static businessObjects.kafka.alerts.RuleAlertFactory.generateRuleAlertByUcid;
import static businessObjects.ui.user.UserFactory.coreUser;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.BoHelper.closeAlert;
import static helpers.database.DbHelper.deleteEntryFromDb;
import static helpers.database.DbHelper.insertObjectToDb;
import static utils.Constants.*;

public class SuspiciousClientsTest extends TestBaseWeb {

    private static final KafkaHelper kafka = new KafkaHelper();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final CrmTbUserObject crmTbUser1 = generateUserByClient(getRandomVantageClientAllFields());
    private static final CrmTbUserObject crmTbUser2 = generateUserByClient(getRandomVantageClientAllFields());

    @BeforeAll
    public static void setup() throws ReflectiveOperationException, SQLException, JsonProcessingException {
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser1);
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser2);
        RuleAlert alert1 = generateRuleAlertByUcid(crmTbUser1.ucid);
        RuleAlert alert2 = generateRuleAlertByUcid(crmTbUser2.ucid);
        kafka.produceMessage(alert1.alertId, objectMapper.writeValueAsString(alert1), KAFKA_TOPIC_ALERTS);
        kafka.produceMessage(alert2.alertId, objectMapper.writeValueAsString(alert2), KAFKA_TOPIC_ALERTS);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("525")
    @DisplayName("Verify that all elements are present for all suspicious clients")
    public void verifyAllElementsArePresentForSuspiciousClientsTest() throws InterruptedException {
        investigationPage.navigate();
        keycloackPage.loginAsCoreUser();
        investigationPage.waitForPageToLoad();
        investigationPage.clickSuspiciousClientsFiltration();
        investigationPage.selectBrandFilterByText(crmTbUser1.brand);
        investigationPage.clickApplyFiltrationButton();
        investigationPage.filterUnassigned();
        investigationPage.waitForPageToLoad();
        investigationPage.clickClientCardByClientId(String.valueOf(crmTbUser1.userId));
        investigationPage.assignClientByClientId(String.valueOf(crmTbUser1.userId));
        investigationPage.filterAssignedMe();
        investigationPage.waitForPageToLoad();
        // 'My clients' tab
        investigationPage.verifyEachClientHasBrandImg();
        investigationPage.verifyEachClientHasCountryCode();
        investigationPage.verifyEachClientHasClientId();
        investigationPage.verifyEachClientHasInvestigationStatusInvestigating();
        investigationPage.verifyEachClientAssignedToUser(coreUser());
        investigationPage.verifyEachClientHasCardTimer();
        investigationPage.verifyEachClientHasAlertCount();
        investigationPage.verifyClientCardsCount();
        // 'Unassigned' tab
        investigationPage.filterUnassigned();
        investigationPage.waitForPageToLoad();
        investigationPage.verifyEachClientHasBrandImg();
        investigationPage.verifyEachClientHasCountryCode();
        investigationPage.verifyEachClientHasClientId();
        investigationPage.verifyEachClientHasAnyInvestigationStatus();
        investigationPage.verifyEachClientHasCardTimer();
        investigationPage.verifyEachClientHasAlertCount();
        investigationPage.verifyClientCardsCount();
        // 'All' tab
        investigationPage.filterAll();
        investigationPage.waitForPageToLoad();
        investigationPage.verifyEachClientHasBrandImg();
        investigationPage.verifyEachClientHasCountryCode();
        investigationPage.verifyEachClientHasClientId();
        investigationPage.verifyEachClientHasAnyInvestigationStatus();
        investigationPage.verifyEachClientHasCardTimer();
        investigationPage.verifyEachClientHasAlertCount();
        investigationPage.verifyClientCardsCount();
    }

    @AfterAll
    public static void teardown() throws SQLException {
        deleteEntryFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", crmTbUser1.ucid));
        deleteEntryFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", crmTbUser2.ucid));
        closeAlert(crmTbUser1.ucid);
        closeAlert(crmTbUser2.ucid);
    }
}
