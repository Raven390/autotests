package tests.vindexBackofficeUiTests;

import businessObjects.db.clickhouse.crmTbAccount.CrmTbAccountObject;
import businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObject;
import businessObjects.db.clickhouse.mtAccount.MtAccountObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import helpers.data.ClientHelper;
import helpers.data.enums.Brand;
import helpers.data.enums.Regulator;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.TestBaseWeb;

import java.sql.SQLException;
import java.util.List;

import static businessObjects.db.clickhouse.crmTbAccount.CrmTbAccountObjectFactory.generateAdditionalStaticCrmTbAccountActive;
import static businessObjects.db.clickhouse.crmTbAccount.CrmTbAccountObjectFactory.generateStaticCrmTbAccountActive;
import static businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObjectFactory.generateStaticUserByClient;
import static businessObjects.db.clickhouse.mtAccount.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static helpers.database.DbHelper.insertObjectToDb;
import static helpers.database.DbHelper.insertObjectsToDb;
import static utils.Constants.*;

public class TradingTest extends TestBaseWeb {

    private static ClientHelper client = new ClientHelper(151_501, "e5880ca5-8578-4a1e-969d-7a64716ca41f", Brand.INFINOX, Regulator.FCA, 151_501_001, 151_501_002, 42);
    private static CrmTbUserObject crmTbUser = generateStaticUserByClient(client);
    private static CrmTbAccountObject account1 = generateStaticCrmTbAccountActive(client);
    private static CrmTbAccountObject account2 = generateAdditionalStaticCrmTbAccountActive(client);
    private static MtAccountObject mtAccount1 = generateMtAccountByCrmTbAccount(account1);
    private static MtAccountObject mtAccount2 = generateMtAccountByCrmTbAccount(account2);

    @BeforeAll
    public static void setup() throws ReflectiveOperationException, SQLException, JsonProcessingException {
        crmTbUser.firstName = "Trading";
        crmTbUser.lastName = "Trademan";
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        insertObjectsToDb(CRM_ACCOUNT_TABLE_NAME, List.of(account1, account2));
        insertObjectsToDb(MT_ACCOUNT_TABLE_NAME, List.of(mtAccount1, mtAccount2));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("241")
    @DisplayName("Test that operations subtab renders all basic elements")
    public void rendersAllBasicElementsTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigateOperations(client.getUcid());
        tradingPage.operationsRendersTest();
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("505")
    @DisplayName("Test that type filter list contains all necessary types")
    public void testTypeFilterList() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigateOperations(client.getUcid());
        tradingPage.openFilter();
        tradingPage.checkTypeFilterList();
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("455")
    @DisplayName("Test that type filter works Sell")
    public void testTypeFilterSell() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigateOperations(client.getUcid());
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
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigateOperations(client.getUcid());
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
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigateOperations(client.getUcid());
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
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigateOperations(client.getUcid());
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
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigateOperations(client.getUcid());
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
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigateOperations(client.getUcid());
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
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigateOperations(client.getUcid());
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
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigateOperations(client.getUcid());
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
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigateOperations(client.getUcid());
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
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigateOperations(client.getUcid());
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
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigateOperations(client.getUcid());
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
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigateOperations(client.getUcid());
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
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigateOperations(client.getUcid());
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
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigateOperations(client.getUcid());
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
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigateOperations(client.getUcid());
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
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigateOperations(client.getUcid());
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
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigateOperations(client.getUcid());
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
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigateOperations(client.getUcid());
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
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigateOperations(client.getUcid());
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
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigateOperations(client.getUcid());
        tradingPage.openFilter();
        tradingPage.fillProfitValues(String.valueOf(from), String.valueOf(to));
        tradingPage.clickApplyButton();
        tradingPage.checkProfitCellsContentFirst(-37.99);
    }
}
