package tests.vindexBackofficeUiTests;

import businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObject;
import businessObjects.kafka.alerts.RuleAlert;
import businessObjects.ui.user.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.data.enums.Brand;
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

public class SuspiciousClientsFiltersTest extends TestBaseWeb {

    private static final KafkaHelper kafka = new KafkaHelper();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final CrmTbUserObject crmTbUser1 = generateUserByClient(getRandomVantageClientAllFields());
    private static final CrmTbUserObject crmTbUser2 = generateUserByClient(getRandomVantageClientAllFields());

    @BeforeAll
    public static void setup() throws ReflectiveOperationException, SQLException, JsonProcessingException {
        crmTbUser2.brand = Brand.INFINOX.getDisplayName();
        crmTbUser2.country = "Malaysia";
        crmTbUser2.countryCode = "MY";
        crmTbUser2.isoCountryCode = "MY";
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser1);
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser2);
        RuleAlert alert1 = generateRuleAlertByUcid(crmTbUser1.ucid);
        RuleAlert alert2 = generateRuleAlertByUcid(crmTbUser2.ucid);
        alert2.rule.name = "Mirror Trading";
        alert2.rule.trigger = "closeTrade";
        kafka.produceMessage(alert1.alertId, objectMapper.writeValueAsString(alert1), KAFKA_TOPIC_ALERTS);
        kafka.produceMessage(alert2.alertId, objectMapper.writeValueAsString(alert2), KAFKA_TOPIC_ALERTS);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("549")
    @DisplayName("Verify filtration by brand for suspicious clients")
    public void verifyBrandFiltrationTest() {
        investigationPage.navigate();
        keycloackPage.loginAsCoreUser();
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

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("550")
    @DisplayName("Verify filtration by rule for suspicious clients")
    public void verifyRuleFiltrationTest() {
        investigationPage.navigate();
        keycloackPage.loginAsCoreUser();
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

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("551")
    @DisplayName("Verify filtration by country for suspicious clients")
    public void verifyCountryFiltrationTest() {
        investigationPage.navigate();
        keycloackPage.loginAsCoreUser();
        investigationPage.waitForPageToLoad();
        investigationPage.clickSuspiciousClientsFiltration();
        investigationPage.selectCountryFilter("Cyprus");
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
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("552")
    @DisplayName("Verify filtration by assignee for suspicious clients")
    public void verifyAssigneeFiltrationTest() {
        investigationPage.navigate();
        keycloackPage.loginAsCoreUser();
        investigationPage.waitForPageToLoad();
        investigationPage.clickSuspiciousClientsFiltration();
        investigationPage.selectBrandFilterByText(crmTbUser1.brand);
        investigationPage.clickApplyFiltrationButton();
        investigationPage.filterUnassigned();
        investigationPage.waitForPageToLoad();
        investigationPage.assignClientByClientId(String.valueOf(crmTbUser1.userId));
        investigationPage.clickSuspiciousClientsFiltration();
        User user = coreUser();
        investigationPage.selectAssigneeFilter(user);
        investigationPage.clickApplyFiltrationButton();
        investigationPage.verifyAllCardsFilteredByAssignee(user);
        investigationPage.filterAll();
        investigationPage.waitForPageToLoad();
        investigationPage.verifyAllCardsFilteredByAssignee(user);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("553")
    @DisplayName("Verify reset filtration functionality for suspicious clients")
    public void verifyResetFiltrationTest() {
        investigationPage.navigate();
        keycloackPage.loginAsCoreUser();
        investigationPage.waitForPageToLoad();
        investigationPage.clickSuspiciousClientsFiltration();
        // Brand
        investigationPage.selectBrandFilterByText("Vantage");
        investigationPage.resetBrandFilterAndVerify();
        // Rules
        investigationPage.selectRuleWithName("Mirror Trading");
        investigationPage.resetRulesFilterAndVerify();
        // Countries
        investigationPage.selectCountryFilter("Cyprus");
        investigationPage.resetCountriesFilterAndVerify();
        // Assignees
        investigationPage.selectAssigneeFilter(coreUser());
        investigationPage.resetAssigneeFilterAndVerify();
        // Reset all
        investigationPage.selectBrandFilterByText("Vantage");
        investigationPage.selectRuleWithName("Mirror Trading");
        investigationPage.selectCountryFilter("Cyprus");
        investigationPage.selectAssigneeFilter(coreUser());
        investigationPage.resetAllFiltersAndVerify();
    }

    @AfterAll
    public static void teardown() throws SQLException {
        deleteEntryFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", crmTbUser1.ucid));
        deleteEntryFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", crmTbUser2.ucid));
        closeAlert(crmTbUser1.ucid);
        closeAlert(crmTbUser2.ucid);
    }
}
