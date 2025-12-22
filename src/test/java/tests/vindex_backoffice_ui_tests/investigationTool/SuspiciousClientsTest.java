package tests.vindex_backoffice_ui_tests.investigationTool;

import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.kafka.alerts.RuleAlertFactory.*;
import static business_objects.ui.user.UserFactory.autotestUserOne;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.BoHelper.closeAlert;
import static helpers.database.DbHelper.deleteEntryFromDb;
import static helpers.database.DbHelper.insertObjectToDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInRelativeOrder;
import static org.hamcrest.Matchers.is;
import static utils.Constants.*;
import static utils.Utils.closeAllAlertsBo;

import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.kafka.alerts.PaymentAlertMessageV2;
import business_objects.kafka.alerts.RuleAlert;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.kafka.KafkaHelper;
import io.qameta.allure.AllureId;
import java.util.List;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

@Tag(TEAM_BACKOFFICE)
@Tag(LAYER_WEB)
class SuspiciousClientsTest extends TestBaseWeb {

    private static final KafkaHelper kafka = new KafkaHelper();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final CrmTbUserObject crmTbUser1 = generateUserByClient(getRandomVantageClientAllFields());
    private static final CrmTbUserObject crmTbUser2 = generateUserByClient(getRandomVantageClientAllFields());
    private static final CrmTbUserObject crmTbUser3 = generateUserByClient(getRandomVantageClientAllFields());
    private static final CrmTbUserObject crmTbUser4 = generateUserByClient(getRandomVantageClientAllFields());
    private static final CrmTbUserObject crmTbUser5 = generateUserByClient(getRandomVantageClientAllFields());

    @BeforeAll
    static void setup() throws Exception {
        objectMapper.findAndRegisterModules();
        closeAllAlertsBo();
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser1);
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser2);
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser3);
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser4);
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser5);
        RuleAlert alert1 = generateRuleAlertByUcid(crmTbUser1.ucid, "transferToWA");
        RuleAlert alert2 = generateRuleAlertByUcid(crmTbUser2.ucid, "withdrawalFromWA");
        PaymentAlertMessageV2 alertPayment1 = generatePaymentAlertByUcidByTrigger(crmTbUser3.ucid, "Registration");
        PaymentAlertMessageV2 alertPayment2 = generatePaymentAlertByUcidByTrigger(crmTbUser4.ucid, "transferToWA");
        PaymentAlertMessageV2 alertPayment3 = generatePaymentAlertByUcidByTrigger(crmTbUser5.ucid, "withdrawalFromWA");
        kafka.produceMessage(alert1.alertId, objectMapper.writeValueAsString(alert1), KAFKA_TOPIC_ALERTS);
        kafka.produceMessage(alert2.alertId, objectMapper.writeValueAsString(alert2), KAFKA_TOPIC_ALERTS);
        kafka.produceMessage(
                alertPayment1.getId().toString(), objectMapper.writeValueAsString(alertPayment1), KAFKA_TOPIC_ALERTS);
        kafka.produceMessage(
                alertPayment2.id.toString(), objectMapper.writeValueAsString(alertPayment2), KAFKA_TOPIC_ALERTS);
        kafka.produceMessage(
                alertPayment3.id.toString(), objectMapper.writeValueAsString(alertPayment3), KAFKA_TOPIC_ALERTS);
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

    @Test
    @AllureId("1793")
    @DisplayName("Verify that high priority elements are present and in order for PAYMENT suspicious clients")
    void verifyPriorityElementsArePresentForPaymentSuspiciousClientsTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToMain();
        investigationPage.waitForPageToLoad();
        investigationPage.clickSelectPaymentInvestigationType();
        investigationPage.waitForPageToLoad();
        investigationPage.filterAll();
        investigationPage.waitForPageToLoad();
        List<String> clientIdsFromClientCards = investigationPage.getClientIdsFromClientCards();
        List<Boolean> priorityFromClientCards = investigationPage.getPriorityFromClientCards();
        assertThat("Assert that there is 3 payment suspicious clients", clientIdsFromClientCards.size(), is(3));
        assertThat(
                "Assert that payment suspicious clients in order",
                clientIdsFromClientCards.stream().map(Integer::parseInt).toList(),
                containsInRelativeOrder(crmTbUser4.userId, crmTbUser5.userId, crmTbUser3.userId));
        assertThat(
                "Assert that payment suspicious clients in order",
                priorityFromClientCards,
                containsInRelativeOrder(true, true, false));
    }

    @Test
    @AllureId("1941")
    @DisplayName("Verify that high priority elements are present and in order for TRADING suspicious clients")
    void verifyPriorityElementsArePresentForTradingSuspiciousClientsTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToMain();
        investigationPage.waitForPageToLoad();
        investigationPage.clickSelectTradingInvestigationType();
        investigationPage.waitForPageToLoad();
        investigationPage.filterAll();
        investigationPage.waitForPageToLoad();
        List<String> clientIdsFromClientCards = investigationPage.getClientIdsFromClientCards();
        List<Boolean> priorityFromClientCards = investigationPage.getPriorityFromClientCards();
        assertThat("Assert that there is 2 trading suspicious clients", clientIdsFromClientCards.size(), is(2));
        assertThat(
                "Assert that payment suspicious clients in order",
                clientIdsFromClientCards.stream().map(Integer::parseInt).toList(),
                containsInRelativeOrder(crmTbUser1.userId, crmTbUser2.userId));
        assertThat(
                "Assert that payment suspicious clients in order",
                priorityFromClientCards,
                containsInRelativeOrder(true, true));
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
