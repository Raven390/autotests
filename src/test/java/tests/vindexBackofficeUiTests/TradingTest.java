package tests.vindexBackofficeUiTests;

import io.qameta.allure.AllureId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.TestBaseWeb;

import static utils.Constants.LAYER_WEB;
import static utils.Constants.TEAM_BACKOFFICE;

public class TradingTest extends TestBaseWeb {

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("241")
    @DisplayName("Test that operations subtab renders all basic elements")
    public void resolveWithWithdrawalsApproveAll() {
        investigationPage.navigate();
        keycloackPage.loginAsDevUser();
        tradingPage.navigateOperations("infinox-151501");
        tradingPage.operationsRendersTest();
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("505")
    @DisplayName("Test that type filter list contains all necessary types")
    public void testTypeFilterList() {
        investigationPage.navigate();
        keycloackPage.loginAsDevUser();
        tradingPage.navigateOperations("infinox-151501");
        tradingPage.openFilter();
        tradingPage.checkTypeFilterList();
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("455")
    @DisplayName("Test that type filter works Sell")
    public void testTypeFilterSell() {
        investigationPage.navigate();
        keycloackPage.loginAsDevUser();
        tradingPage.navigateOperations("infinox-151501");
        tradingPage.openFilter();
        tradingPage.clickFilterCheckbox("Sell");
        tradingPage.clickApplyButton();
        tradingPage.checkTypeCellsContent("Sell");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("435")
    @DisplayName("Test that type filter works Balance")
    public void testTypeFilterBalance() {
        investigationPage.navigate();
        keycloackPage.loginAsDevUser();
        tradingPage.navigateOperations("infinox-151501");
        tradingPage.openFilter();
        tradingPage.clickFilterCheckbox("Balance");
        tradingPage.clickApplyButton();
        tradingPage.checkTypeCellsContent("Balance");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("451")
    @DisplayName("Test that type filter works Buy")
    public void testTypeFilterBuy() {
        investigationPage.navigate();
        keycloackPage.loginAsDevUser();
        tradingPage.navigateOperations("infinox-151501");
        tradingPage.openFilter();
        tradingPage.clickFilterCheckbox("Buy");
        tradingPage.clickApplyButton();
        tradingPage.checkTypeCellsContent("Buy");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("452")
    @DisplayName("Test that type filter works Buy Limit")
    public void testTypeFilterBuyLimit() {
        investigationPage.navigate();
        keycloackPage.loginAsDevUser();
        tradingPage.navigateOperations("infinox-151501");
        tradingPage.openFilter();
        tradingPage.clickFilterCheckbox("Buy Limit");
        tradingPage.clickApplyButton();
        tradingPage.checkTypeCellsContent("Buy Limit");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("453")
    @DisplayName("Test that type filter works Buy Stop")
    public void testTypeFilterBuyStop() {
        investigationPage.navigate();
        keycloackPage.loginAsDevUser();
        tradingPage.navigateOperations("infinox-151501");
        tradingPage.openFilter();
        tradingPage.clickFilterCheckbox("Buy Stop");
        tradingPage.clickApplyButton();
        tradingPage.checkTypeCellsContent("Buy Stop");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("454")
    @DisplayName("Test that type filter works Credit")
    public void testTypeFilterCredit() {
        investigationPage.navigate();
        keycloackPage.loginAsDevUser();
        tradingPage.navigateOperations("infinox-151501");
        tradingPage.openFilter();
        tradingPage.clickFilterCheckbox("Credit");
        tradingPage.clickApplyButton();
        tradingPage.checkTypeCellsContent("Credit");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("456")
    @DisplayName("Test that type filter works Sell Limit")
    public void testTypeFilterSellLimit() {
        investigationPage.navigate();
        keycloackPage.loginAsDevUser();
        tradingPage.navigateOperations("infinox-151501");
        tradingPage.openFilter();
        tradingPage.clickFilterCheckbox("Sell Limit");
        tradingPage.clickApplyButton();
        tradingPage.checkTypeCellsContent("Sell Limit");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("457")
    @DisplayName("Test that type filter works Sell Stop")
    public void testTypeFilterSellStop() {
        investigationPage.navigate();
        keycloackPage.loginAsDevUser();
        tradingPage.navigateOperations("infinox-151501");
        tradingPage.openFilter();
        tradingPage.clickFilterCheckbox("Sell Stop");
        tradingPage.clickApplyButton();
        tradingPage.checkTypeCellsContent("Sell Stop");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("475")
    @DisplayName("Test that Volume filter ")
    public void testVolumeFilter() {
        int from = 4;
        int to = 8;
        investigationPage.navigate();
        keycloackPage.loginAsDevUser();
        tradingPage.navigateOperations("infinox-151501");
        tradingPage.openFilter();
        tradingPage.fillVolumeValues(String.valueOf(from), String.valueOf(to));
        tradingPage.clickApplyButton();
        tradingPage.checkVolumeCellsContentUSD(from, to);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("474")
    @DisplayName("Test that Duration filter works")
    public void testDurationFilter() {
        int from = 4;
        int to = 8;
        investigationPage.navigate();
        keycloackPage.loginAsDevUser();
        tradingPage.navigateOperations("infinox-151501");
        tradingPage.openFilter();
        tradingPage.fillDurationValues(String.valueOf(from), String.valueOf(to));
        tradingPage.clickApplyButton();
        tradingPage.checkDatesMinutes(from, to);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("476")
    @DisplayName("Test that method filter works API")
    public void testMethodFilterAPI() {
        investigationPage.navigate();
        keycloackPage.loginAsDevUser();
        tradingPage.navigateOperations("infinox-151501");
        tradingPage.openFilter();
        tradingPage.clickFilterCheckbox("API");
        tradingPage.clickApplyButton();
        tradingPage.checkMethodCellsContent("API");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("477")
    @DisplayName("Test that method filter works Client")
    public void testMethodFilterClient() {
        investigationPage.navigate();
        keycloackPage.loginAsDevUser();
        tradingPage.navigateOperations("infinox-151501");
        tradingPage.openFilter();
        tradingPage.clickFilterCheckbox("Client");
        tradingPage.clickApplyButton();
        tradingPage.checkMethodCellsContent("Client");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("478")
    @DisplayName("Test that method filter works Dealer")
    public void testMethodFilterDealer() {
        investigationPage.navigate();
        keycloackPage.loginAsDevUser();
        tradingPage.navigateOperations("infinox-151501");
        tradingPage.openFilter();
        tradingPage.clickFilterCheckbox("Dealer");
        tradingPage.clickApplyButton();
        tradingPage.checkMethodCellsContent("Dealer");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("479")
    @DisplayName("Test that method filter works Expert")
    public void testMethodFilterExpert() {
        investigationPage.navigate();
        keycloackPage.loginAsDevUser();
        tradingPage.navigateOperations("infinox-151501");
        tradingPage.openFilter();
        tradingPage.clickFilterCheckbox("Expert");
        tradingPage.clickApplyButton();
        tradingPage.checkMethodCellsContent("Expert");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("480")
    @DisplayName("Test that method filter works Gateway")
    public void testMethodFilterGateway() {
        investigationPage.navigate();
        keycloackPage.loginAsDevUser();
        tradingPage.navigateOperations("infinox-151501");
        tradingPage.openFilter();
        tradingPage.clickFilterCheckbox("Gateway");
        tradingPage.clickApplyButton();
        tradingPage.checkMethodCellsContent("Gateway");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("481")
    @DisplayName("Test that method filter works Mobile")
    public void testMethodFilterMobile() {
        investigationPage.navigate();
        keycloackPage.loginAsDevUser();
        tradingPage.navigateOperations("infinox-151501");
        tradingPage.openFilter();
        tradingPage.clickFilterCheckbox("Mobile");
        tradingPage.clickApplyButton();
        tradingPage.checkMethodCellsContent("Mobile");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("482")
    @DisplayName("Test that method filter works Signal")
    public void testMethodFilterSignal() {
        investigationPage.navigate();
        keycloackPage.loginAsDevUser();
        tradingPage.navigateOperations("infinox-151501");
        tradingPage.openFilter();
        tradingPage.clickFilterCheckbox("Signal");
        tradingPage.clickApplyButton();
        tradingPage.checkMethodCellsContent("Signal");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("483")
    @DisplayName("Test that method filter works Web")
    public void testMethodFilterWeb() {
        investigationPage.navigate();
        keycloackPage.loginAsDevUser();
        tradingPage.navigateOperations("infinox-151501");
        tradingPage.openFilter();
        tradingPage.clickFilterCheckbox("Web");
        tradingPage.clickApplyButton();
        tradingPage.checkMethodCellsContent("Web");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("472")
    @DisplayName("Test that Profit filter works")
    public void testProfitFilter() {
        int from = 4;
        int to = 80;
        investigationPage.navigate();
        keycloackPage.loginAsDevUser();
        tradingPage.navigateOperations("infinox-151501");
        tradingPage.openFilter();
        tradingPage.fillProfitValues(String.valueOf(from), String.valueOf(to));
        tradingPage.clickApplyButton();
        tradingPage.checkProfitCellsContent(from, to);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("472")
    @DisplayName("Test that profit filter works with negative")
    public void testProfitFilterNegativeValuesTest() {
        int from = 36;
        int to = 38;
        investigationPage.navigate();
        keycloackPage.loginAsDevUser();
        tradingPage.navigateOperations("infinox-151501");
        tradingPage.openFilter();
        tradingPage.fillProfitValues(String.valueOf(from), String.valueOf(to));
        tradingPage.clickApplyButton();
        tradingPage.checkProfitCellsContentFirst(-37.99);
    }
}
