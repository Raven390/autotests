package tests.vindex_backoffice_ui_tests.investigationTool;

import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.kafka.alerts.RuleAlertFactory.generatePaymentAlertByUcid;
import static business_objects.kafka.alerts.RuleAlertFactory.generatePaymentAlertByUcidByTrigger;
import static business_objects.ui.user.UserFactory.autotestUserOne;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.BoHelper.closeAlert;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Utils.closeAllAlertsBo;

import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.kafka.alerts.PaymentAlertMessage;
import business_objects.kafka.alerts.PaymentAlertMessageV2;
import business_objects.ui.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import helpers.data.enums.Brand;
import helpers.kafka.KafkaHelper;
import io.qameta.allure.AllureId;
import java.util.List;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

@Tag(TEAM_BACKOFFICE)
@Tag(LAYER_WEB)
class SuspiciousClientsPaymentFiltersTest extends TestBaseWeb {

    private static final KafkaHelper kafka = new KafkaHelper();
    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private static final CrmTbUserObject crmTbUser1 = generateUserByClient(getRandomVantageClientAllFields());
    private static final CrmTbUserObject crmTbUser2 = generateUserByClient(getRandomVantageClientAllFields());

    @BeforeAll
    static void setup() throws Exception {
        closeAllAlertsBo();
        crmTbUser2.brand = Brand.INFINOX.getDisplayName();
        crmTbUser2.country = "Malaysia";
        crmTbUser2.countryCode = "MY";
        crmTbUser2.isoCountryCode = "MY";
        insertObjectsToDb(CRM_USER_TABLE_NAME, List.of(crmTbUser1, crmTbUser2));
        PaymentAlertMessage alert1 = generatePaymentAlertByUcid(crmTbUser1.ucid);
        alert1.setAmount("3000");
        PaymentAlertMessageV2 alert2 = generatePaymentAlertByUcidByTrigger(crmTbUser2.ucid, "transferToWA");
        alert2.amount = "9000";
        alert2.amountUSD = "9000";
        alert2.paymentMethod = "Credit card";
        kafka.produceMessage(alert1.getId().toString(), objectMapper.writeValueAsString(alert1), KAFKA_TOPIC_ALERTS);
        kafka.produceMessage(alert2.id.toString(), objectMapper.writeValueAsString(alert2), KAFKA_TOPIC_ALERTS);
    }

    @Test
    @AllureId("1648")
    @DisplayName("Verify payment filtration by brand for suspicious clients")
    void verifyBrandFiltrationTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToMain();
        investigationPage.waitForPageToLoad();
        investigationPage.clickSelectPaymentInvestigationType();
        investigationPage.clickSuspiciousClientsFiltration();
        investigationPage.selectBrandFilterByText("Vantage");
        investigationPage.clickApplyFiltrationButton();
        investigationPage.verifyBrandImagesAreVantageOnly();
        investigationPage.filterUnassigned();
        investigationPage.waitForPageToLoad();
        investigationPage.verifyBrandImagesAreVantageOnly();
        investigationPage.filterAll();
        investigationPage.waitForPageToLoad();
        investigationPage.verifyBrandImagesAreVantageOnly();
    }

    @Test
    @AllureId("1649")
    @DisplayName("Verify payment filtration by rule for suspicious clients")
    void verifyRuleFiltrationTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToMain();
        investigationPage.waitForPageToLoad();
        investigationPage.clickSelectPaymentInvestigationType();
        investigationPage.clickSuspiciousClientsFiltration();
        String ruleName = "Payment Fraud Detection";
        investigationPage.selectRuleWithNameWithSearch(ruleName);
        investigationPage.clickApplyFiltrationButton();
        investigationPage.verifyAllCardsFilteredByRuleName(ruleName);
        investigationPage.filterAll();
        investigationPage.waitForPageToLoad();
        investigationPage.verifyAllCardsFilteredByRuleName(ruleName);
    }

    @Test
    @AllureId("1792")
    @DisplayName("Verify payment filtration by priority for suspicious clients")
    void verifyPriorityFiltrationTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToMain();
        investigationPage.waitForPageToLoad();
        investigationPage.clickSelectPaymentInvestigationType();
        investigationPage.clickSuspiciousClientsFiltration();
        String filterName = "HIGH";
        investigationPage.fillPriorityFilter(filterName);
        investigationPage.clickApplyFiltrationButton();
        investigationPage.verifyAllCardsFilteredByHighPriority();
        investigationPage.filterAll();
        investigationPage.waitForPageToLoad();
        investigationPage.verifyAllCardsFilteredByHighPriority();
    }

    @Test
    @AllureId("1650")
    @DisplayName("Verify payment filtration by country for suspicious clients")
    void verifyCountryFiltrationTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToMain();
        investigationPage.waitForPageToLoad();
        investigationPage.clickSelectPaymentInvestigationType();
        investigationPage.clickSuspiciousClientsFiltration();
        investigationPage.selectCountryFilter("CYPRUS");
        investigationPage.clickApplyFiltrationButton();
        investigationPage.verifyAllCardsFilteredByCountry("CY");
        investigationPage.filterUnassigned();
        investigationPage.waitForPageToLoad();
        investigationPage.verifyAllCardsFilteredByCountry("CY");
        investigationPage.filterAll();
        investigationPage.waitForPageToLoad();
        investigationPage.verifyAllCardsFilteredByCountry("CY");
    }

    @Test
    @AllureId("1651")
    @DisplayName("Verify payment filtration by assignee for suspicious clients")
    void verifyAssigneeFiltrationTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser1.ucid);
        investigationPage.clickSelectPaymentInvestigationType();
        investigationPage.waitForPageToLoad();
        investigationPage.investigateClientCard();
        page.waitForTimeout(1000);
        investigationPage.clickSuspiciousClientsFiltration();
        User user = autotestUserOne();
        investigationPage.selectAssigneeFilter(user);
        investigationPage.clickApplyFiltrationButton();
        investigationPage.verifyAllCardsFilteredByAssignee(user);
        investigationPage.filterAll();
        investigationPage.waitForPageToLoad();
        investigationPage.verifyAllCardsFilteredByAssignee(user);

        investigationPage.openCommentFormResolve();
    }

    @Test
    @AllureId("1652")
    @DisplayName("Verify reset payment filtration functionality for suspicious clients")
    void verifyResetFiltrationTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToMain();
        investigationPage.waitForPageToLoad();
        investigationPage.clickSelectPaymentInvestigationType();
        investigationPage.waitForPageToLoad();
        investigationPage.clickSuspiciousClientsFiltration();
        // Brand
        investigationPage.selectBrandFilterByText("Vantage");
        investigationPage.resetBrandFilterAndVerify();
        // Rules
        investigationPage.selectRuleWithName("Payment Fraud Detection");
        investigationPage.resetRulesFilterAndVerify();
        // Countries
        investigationPage.selectCountryFilter("CYPRUS");
        investigationPage.resetCountriesFilterAndVerify();

        // Reset all
        investigationPage.selectBrandFilterByText("Vantage");
        investigationPage.selectRuleWithName("Payment Fraud Detection");
        investigationPage.selectCountryFilter("CYPRUS");
        investigationPage.resetAllFiltersAndVerify();
    }

    @Test
    @AllureId("1653")
    @DisplayName("Verify payment filtration functionality for amount filter")
    void verifyAmountFiltrationTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToMain();
        investigationPage.waitForPageToLoad();
        investigationPage.clickSelectPaymentInvestigationType();

        // only alert1
        investigationPage.clickSuspiciousClientsFiltration();
        investigationPage.fillAmountFilter("1000", "3000");
        investigationPage.clickApplyFiltrationButton();

        List<String> clientIds = investigationPage.getClientIdsFromClientCards();
        assertThat("Verify that only 1 client is filtered", clientIds.size(), is(1));
        assertThat(
                "Verify that the correct client is filtered", clientIds, contains(String.valueOf(crmTbUser1.userId)));

        // both alerts
        investigationPage.clickSuspiciousClientsFiltration();
        investigationPage.fillAmountFilter("1000", "10000");
        investigationPage.clickApplyFiltrationButton();

        clientIds = investigationPage.getClientIdsFromClientCards();
        assertThat("Verify that 2 clients are filtered", clientIds.size(), is(2));
        assertThat(
                "Verify that both clients are filtered",
                clientIds,
                containsInAnyOrder(String.valueOf(crmTbUser1.userId), String.valueOf(crmTbUser2.userId)));

        // nothing found
        investigationPage.clickSuspiciousClientsFiltration();
        investigationPage.fillAmountFilter("1000", "2000");
        investigationPage.verifyApplyFilterButtonIsDisabled();
    }

    @Test
    @AllureId("1654")
    @DisplayName("Verify payment filtration functionality for payment method filter")
    void verifyPaymentMethodFiltrationTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToMain();
        investigationPage.waitForPageToLoad();
        investigationPage.clickSelectPaymentInvestigationType();
        investigationPage.filterAll();
        investigationPage.clickSuspiciousClientsFiltration();
        String paymentMethod = "Credit card";
        investigationPage.fillPaymentMethodFilter(paymentMethod);
        investigationPage.clickApplyFiltrationButton();

        List<String> clientIds = investigationPage.getClientIdsFromClientCards();
        assertThat("Verify that only 1 client is filtered by payment method", clientIds.size(), is(1));
        assertThat(
                "Verify that the correct client is filtered", clientIds, contains(String.valueOf(crmTbUser2.userId)));
    }

    @AfterAll
    static void teardown() {
        deleteObjectFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", crmTbUser1.ucid));
        deleteObjectFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", crmTbUser2.ucid));
        closeAlert(crmTbUser1.ucid);
        closeAlert(crmTbUser2.ucid);
    }
}
