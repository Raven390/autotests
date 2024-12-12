package tests.vindexBackofficeUiTests;

import businessObjects.ui.user.User;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

import static businessObjects.ui.user.UserFactory.coreUser;
import static utils.Constants.*;

public class SuspiciousClientsFiltersTest extends TestBaseWeb {

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
        String ruleName = "CPA";
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
        investigationPage.selectRuleWithName("CPA");
        investigationPage.resetRulesFilterAndVerify();
        // Countries
        investigationPage.selectCountryFilter("Cyprus");
        investigationPage.resetCountriesFilterAndVerify();
        // Assignees
        investigationPage.selectAssigneeFilter(coreUser());
        investigationPage.resetAssigneeFilterAndVerify();
        // Reset all
        investigationPage.selectBrandFilterByText("Vantage");
        investigationPage.selectRuleWithName("CPA");
        investigationPage.selectCountryFilter("Cyprus");
        investigationPage.selectAssigneeFilter(coreUser());
        investigationPage.resetAllFiltersAndVerify();
    }
}
