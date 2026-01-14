package tests.vindex_backoffice_ui_tests.investigationTool;

import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.kafka.alerts.RuleAlertFactory.generatePgsWithdrawalNotificationAlert;
import static business_objects.kafka.alerts.RuleAlertFactory.generateRuleAlertByUcid;
import static business_objects.ui.user.UserFactory.autotestUserOne;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.BoHelper.closeAlert;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Utils.closeAllAlertsBo;
import static utils.Utils.getRandomUuid;

import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.kafka.alerts.RuleAlert;
import business_objects.ui.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.data.ClientHelper;
import helpers.data.enums.Brand;
import helpers.kafka.KafkaHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

@Tag(TEAM_BACKOFFICE)
@Tag(LAYER_WEB)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SuspiciousClientsFiltersTest extends TestBaseWeb {

    private static final KafkaHelper kafka = new KafkaHelper();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final ClientHelper client1 = getRandomVantageClientAllFields();
    private static final ClientHelper client2 = getRandomVantageClientAllFields();
    private static final CrmTbUserObject crmTbUser1 = generateUserByClient(client1);
    private static final CrmTbUserObject crmTbUser2 = generateUserByClient(client2);

    @BeforeAll
    static void setup() throws Exception {
        closeAllAlertsBo();
        crmTbUser2.brand = Brand.INFINOX.getDisplayName();
        crmTbUser2.country = "Malaysia";
        crmTbUser2.countryCode = "MY";
        crmTbUser2.isoCountryCode = "MY";
        insertObjectsToDb(CRM_USER_TABLE_NAME, List.of(crmTbUser1, crmTbUser2));
        RuleAlert alert1 = generateRuleAlertByUcid(crmTbUser1.ucid);
        RuleAlert alert2 = generateRuleAlertByUcid(crmTbUser2.ucid);
        alert2.rule.name = "Mirror Trading";
        alert2.rule.trigger = "closeTrade";
        kafka.produceMessage(alert1.alertId, objectMapper.writeValueAsString(alert1), KAFKA_TOPIC_ALERTS);
        kafka.produceMessage(alert2.alertId, objectMapper.writeValueAsString(alert2), KAFKA_TOPIC_ALERTS);
    }

    @Order(1)
    @Test
    @AllureId("549")
    @DisplayName("Verify filtration by brand for suspicious clients")
    void verifyBrandFiltrationTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToMain();
        investigationPage.waitForPageToLoad();
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

    @Order(2)
    @Test
    @AllureId("550")
    @DisplayName("Verify filtration by rule for suspicious clients")
    void verifyRuleFiltrationTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToMain();
        investigationPage.waitForPageToLoad();
        investigationPage.clickSuspiciousClientsFiltration();
        String ruleName = "Registration";
        investigationPage.selectRuleWithNameWithSearch(ruleName);
        investigationPage.clickApplyFiltrationButton();
        investigationPage.verifyAllCardsFilteredByRuleName(ruleName);
        investigationPage.filterAll();
        investigationPage.waitForPageToLoad();
        investigationPage.verifyAllCardsFilteredByRuleName(ruleName);
    }

    @Order(3)
    @Test
    @AllureId("551")
    @DisplayName("Verify filtration by country for suspicious clients")
    void verifyCountryFiltrationTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToMain();
        investigationPage.waitForPageToLoad();
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

    @Order(4)
    @Test
    @AllureId("552")
    @DisplayName("Verify filtration by assignee for suspicious clients")
    void verifyAssigneeFiltrationTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToMain();
        investigationPage.waitForPageToLoad();
        investigationPage.navigateToClient(crmTbUser1.ucid);
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
    }

    @Order(5)
    @Test
    @AllureId("553")
    @DisplayName("Verify reset filtration functionality for suspicious clients")
    void verifyResetFiltrationTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser1.ucid);
        investigationPage.waitForPageToLoad();
        investigationPage.investigateClientCard();
        investigationPage.clickSuspiciousClientsFiltration();
        // Brand
        investigationPage.selectBrandFilterByText("Vantage");
        investigationPage.resetBrandFilterAndVerify();
        // Rules
        investigationPage.selectRuleWithName("Mirror Trading");
        investigationPage.resetRulesFilterAndVerify();
        // Countries
        investigationPage.selectCountryFilter("CYPRUS");
        investigationPage.resetCountriesFilterAndVerify();
        // Assignees
        investigationPage.selectAssigneeFilter(autotestUserOne());
        investigationPage.resetAssigneeFilterAndVerify();
        // Reset all
        investigationPage.selectBrandFilterByText("Vantage");
        investigationPage.selectRuleWithName("Mirror Trading");
        investigationPage.selectCountryFilter("CYPRUS");
        investigationPage.selectAssigneeFilter(autotestUserOne());
        investigationPage.resetAllFiltersAndVerify();
    }

    @Feature("BMS-3042 Add filter for suspicious clients (trading) by amount to filter alerts for withdrawals")
    @Order(6)
    @Test
    @AllureId("1971")
    @DisplayName("Verify filtration by withdrawal amount for suspicious clients")
    void verifyWithdrawalAmountFiltrationTest() throws Exception {
        RuleAlert withdrawalAlert1 = generatePgsWithdrawalNotificationAlert(client1, getRandomUuid());
        RuleAlert withdrawalAlert2 = generatePgsWithdrawalNotificationAlert(client2, getRandomUuid());
        withdrawalAlert1.rule.attributes.amount = "9000";
        withdrawalAlert1.rule.attributes.currency = "USD";
        withdrawalAlert2.rule.attributes.amount = "11000";
        withdrawalAlert2.rule.attributes.currency = "USD";
        kafka.produceMessage(
                withdrawalAlert1.alertId, objectMapper.writeValueAsString(withdrawalAlert1), KAFKA_TOPIC_ALERTS);
        kafka.produceMessage(
                withdrawalAlert2.alertId, objectMapper.writeValueAsString(withdrawalAlert2), KAFKA_TOPIC_ALERTS);
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToMain();
        investigationPage.waitForPageToLoad();
        investigationPage.clickSuspiciousClientsFiltration();
        investigationPage.fillAmountFilter("0", "10000");
        investigationPage.clickApplyFiltrationButton();
        assertThat(
                "Verify client card that fits the selection is visible",
                investigationPage.getClientIdsFromClientCards(),
                hasItem(client1.getUserId().toString()));
        assertThat(
                "Verify client card that doesn't fit the selection is not visible",
                investigationPage.getClientIdsFromClientCards(),
                not(hasItem(client2.getUserId().toString())));

        investigationPage.clickSuspiciousClientsFiltration();
        Map<String, String> amountFilterValues;

        investigationPage.clickAmountPresetByName("≤20k");
        amountFilterValues = investigationPage.getAmountFromToValues();
        assertThat(amountFilterValues.get("from"), is(""));
        assertThat(amountFilterValues.get("to"), is("20,000 USD"));

        investigationPage.clickAmountPresetByName("20k-50k");
        amountFilterValues = investigationPage.getAmountFromToValues();
        assertThat(amountFilterValues.get("from"), is("20,000 USD"));
        assertThat(amountFilterValues.get("to"), is("50,000 USD"));

        investigationPage.clickAmountPresetByName("50k-200k");
        amountFilterValues = investigationPage.getAmountFromToValues();
        assertThat(amountFilterValues.get("from"), is("50,000 USD"));
        assertThat(amountFilterValues.get("to"), is("200,000 USD"));

        investigationPage.clickAmountPresetByName("≥200k");
        amountFilterValues = investigationPage.getAmountFromToValues();
        assertThat(amountFilterValues.get("from"), is("200,000 USD"));
        assertThat(amountFilterValues.get("to"), is(""));

        // reset amount filter and verify cleared
        investigationPage.resetAmountFilter();
        amountFilterValues = investigationPage.getAmountFromToValues();
        assertThat(amountFilterValues.get("from"), is(""));
        assertThat(amountFilterValues.get("to"), is(""));
    }

    @AfterAll
    static void teardown() {
        deleteObjectFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", crmTbUser1.ucid));
        deleteObjectFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", crmTbUser2.ucid));
        closeAlert(crmTbUser1.ucid);
        closeAlert(crmTbUser2.ucid);
    }
}
