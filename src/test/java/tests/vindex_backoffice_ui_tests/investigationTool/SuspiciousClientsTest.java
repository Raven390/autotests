package tests.vindex_backoffice_ui_tests.investigationTool;

import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.kafka.alerts.PaymentAlertMessage;
import business_objects.kafka.alerts.RuleAlert;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.kafka.KafkaHelper;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.kafka.alerts.RuleAlertFactory.generatePaymentAlertByUcid;
import static business_objects.kafka.alerts.RuleAlertFactory.generateRuleAlertByUcid;
import static business_objects.ui.user.UserFactory.autotestUserOne;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.BoHelper.closeAlert;
import static helpers.database.DbHelper.deleteEntryFromDb;
import static helpers.database.DbHelper.insertObjectToDb;
import static utils.Constants.*;
import static utils.Utils.closeAllAlertsBo;

@Tag(TEAM_BACKOFFICE)
@Tag(LAYER_WEB)
class SuspiciousClientsTest extends TestBaseWeb {

    private static final KafkaHelper kafka = new KafkaHelper();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final CrmTbUserObject crmTbUser1 = generateUserByClient(getRandomVantageClientAllFields());
    private static final CrmTbUserObject crmTbUser2 = generateUserByClient(getRandomVantageClientAllFields());
    private static final CrmTbUserObject crmTbUser3 = generateUserByClient(getRandomVantageClientAllFields());
    private static final CrmTbUserObject crmTbUser4 = generateUserByClient(getRandomVantageClientAllFields());

    @BeforeAll
    static void setup() throws Exception {
        objectMapper.findAndRegisterModules();
        closeAllAlertsBo();
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser1);
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser2);
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser3);
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser4);
        RuleAlert alert1 = generateRuleAlertByUcid(crmTbUser1.ucid);
        RuleAlert alert2 = generateRuleAlertByUcid(crmTbUser2.ucid);
        PaymentAlertMessage alertPayment1 = generatePaymentAlertByUcid(crmTbUser3.ucid);
        PaymentAlertMessage alertPayment2 = generatePaymentAlertByUcid(crmTbUser4.ucid);
        kafka.produceMessage(alert1.alertId, objectMapper.writeValueAsString(alert1), KAFKA_TOPIC_ALERTS);
        kafka.produceMessage(alert2.alertId, objectMapper.writeValueAsString(alert2), KAFKA_TOPIC_ALERTS);
        kafka.produceMessage(alertPayment1.getId().toString(), objectMapper.writeValueAsString(alertPayment1), KAFKA_TOPIC_ALERTS);
        kafka.produceMessage(alertPayment2.getId().toString(), objectMapper.writeValueAsString(alertPayment2), KAFKA_TOPIC_ALERTS);
    }

    @Test
    @AllureId("525")
    @DisplayName("Verify that all elements are present for all suspicious clients")
    void verifyAllElementsArePresentForSuspiciousClientsTest() throws InterruptedException {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToMain();
        investigationPage.waitForPageToLoad();
        investigationPage.clickSuspiciousClientsFiltration();
        investigationPage.selectBrandFilterByText(crmTbUser1.brand);
        investigationPage.clickApplyFiltrationButton();
        investigationPage.filterUnassigned();
        investigationPage.waitForPageToLoad();
        investigationPage.navigateToClient(crmTbUser1.ucid);
        investigationPage.investigateClientCard();
        investigationPage.filterAssignedMe();
        investigationPage.waitForPageToLoad();
        // 'My clients' tab
        investigationPage.verifyEachClientHasBrandImg();
        investigationPage.verifyEachClientHasCountryCode();
        investigationPage.verifyEachClientHasClientId();
        investigationPage.verifyEachClientHasInvestigationStatusInvestigating();
        investigationPage.verifyEachClientAssignedToUser(autotestUserOne());
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
        // 'All' tab
        investigationPage.filterAll();
        investigationPage.waitForPageToLoad();
        investigationPage.verifyEachClientHasBrandImg();
        investigationPage.verifyEachClientHasCountryCode();
        investigationPage.verifyEachClientHasClientId();
        investigationPage.verifyEachClientHasAnyInvestigationStatus();
        investigationPage.verifyEachClientHasCardTimer();
        investigationPage.verifyEachClientHasAlertCount();
    }

    @Test
    @AllureId("1550")
    @DisplayName("Verify that all elements are present for PAYMENT suspicious clients")
    void verifyAllElementsArePresentForPaymentSuspiciousClientsTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToMain();
        investigationPage.waitForPageToLoad();
        investigationPage.clickSelectPaymentInvestigationType();
        investigationPage.waitForPageToLoad();
        investigationPage.navigateToClient(crmTbUser3.ucid);
        // 'Unassigned' tab
        investigationPage.filterUnassigned();
        investigationPage.waitForPageToLoad();
        investigationPage.verifyEachClientHasBrandImg();
        investigationPage.verifyEachClientHasCountryCode();
        investigationPage.verifyEachClientHasClientId();
        investigationPage.verifyEachClientHasAnyInvestigationStatus();
        investigationPage.verifyEachClientHasCardTimer();
        investigationPage.verifyEachClientHasAlertCount();
        // 'All' tab
        investigationPage.filterAll();
        investigationPage.waitForPageToLoad();
        investigationPage.verifyEachClientHasBrandImg();
        investigationPage.verifyEachClientHasCountryCode();
        investigationPage.verifyEachClientHasClientId();
        investigationPage.verifyEachClientHasAnyInvestigationStatus();
        investigationPage.verifyEachClientHasCardTimer();
        investigationPage.verifyEachClientHasAlertCount();
    }

    @AfterAll
    static void teardown() {
        deleteEntryFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", crmTbUser1.ucid));
        deleteEntryFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", crmTbUser2.ucid));
        deleteEntryFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", crmTbUser3.ucid));
        closeAlert(crmTbUser1.ucid);
        closeAlert(crmTbUser2.ucid);
        closeAlert(crmTbUser3.ucid);
    }
}
