package tests.vindex_backoffice_ui_tests.investigationTool.trading;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateAdditionalCrmTbAccountDataForUi;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountDataForUi;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObjectFactory.generateMt4TradesCoerced;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.BoHelper.closeAlert;
import static helpers.database.DbHelper.deleteObjectFromDb;
import static helpers.database.DbHelper.insertObjectToDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static utils.Constants.*;
import static utils.Utils.*;

import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import helpers.data.ClientHelper;
import io.qameta.allure.AllureId;
import java.sql.SQLException;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

public class TradingInfoOperationsSortingFiltrationTest extends TestBaseWeb {

    private static final ClientHelper client = getRandomVantageClientAllFields();
    private static final CrmTbUserObject crmTbUser = generateUserByClient(client);
    private static CrmTbAccountObject account1;
    private static CrmTbAccountObject account2;
    private static MtMt4TradesCoercedObject trade1;
    private static MtMt4TradesCoercedObject trade2;

    @BeforeAll
    public static void setup() throws ReflectiveOperationException, SQLException, JsonProcessingException {
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        account1 = generateCrmTbAccountDataForUi(client);
        insertObjectToDb(MT_ACCOUNT_TABLE_NAME, generateMtAccountByCrmTbAccount(account1));
        account2 = generateAdditionalCrmTbAccountDataForUi(client);
        account2.platform = "MT5";
        insertCrmAccountsToDb(account1, account2);
        insertObjectToDb(MT_ACCOUNT_TABLE_NAME, generateMtAccountByCrmTbAccount(account2));
        trade1 = generateMt4TradesCoerced(client);
        trade1.setAccount(account1.account.longValue());
        trade1.setServerId(account1.serverIdSt.longValue());
        insertObjectToDb(MT4_TRADES_COERCED_TABLE_NAME, trade1);
        trade2 = generateMt4TradesCoerced(client);
        trade2.setAccount(account2.account.longValue());
        trade2.setServerId(account2.serverIdSt.longValue());
        trade2.setPlatform("MT5");
        trade2.setTicketType("Sell");
        trade2.setOpenTime(getPreviousWeekTimestampDbFormat());
        trade2.setCloseTime(getYesterdayTimestampDbFormat());
        trade2.setSymbol("GBPJPY");
        trade2.setProfitUsd(101.22);
        trade2.setNotionalValueUsd(123.44);
        trade2.setReasonName("API");
        insertObjectToDb(MT4_TRADES_COERCED_TABLE_NAME, trade2);
    }

    @BeforeEach
    public void loginAndGoToOperations() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        tradingPage.openTradingTab();
        tradingPage.openOperationsTab();
        assertThat("Verify there are 2 operations with no filtration", tradingPage.getOperationsCount(), equalTo(2));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("575")
    @DisplayName("Verify sorting by open column in trading - operations tab")
    public void verifyTradingInfoDealsOpenSortingTest() {
        assertThat(
                "Assert value in account column for the 1st operation is as expected",
                tradingPage.getOperationAccountByIndex(0),
                equalTo(String.format("%s%s", trade2.getAccount(), trade2.getPlatform())));
        assertThat(
                "Assert value in account column for the 2nd operation is as expected",
                tradingPage.getOperationAccountByIndex(1),
                equalTo(String.format("%s%s", trade1.getAccount(), trade1.getPlatform())));
        assertThat(
                "Assert sort by open popup text",
                tradingPage.getSortByOpenPopupText(),
                equalTo("Sorted:Oldest → Newest"));
        tradingPage.sortByOpen();
        assertThat(
                "Assert value in account column for the 1st operation is as expected",
                tradingPage.getOperationAccountByIndex(0),
                equalTo(String.format("%s%s", trade1.getAccount(), trade1.getPlatform())));
        assertThat(
                "Assert value in account column for the 2nd operation is as expected",
                tradingPage.getOperationAccountByIndex(1),
                equalTo(String.format("%s%s", trade2.getAccount(), trade2.getPlatform())));
        assertThat(
                "Assert sort by open popup text",
                tradingPage.getSortByOpenPopupText(),
                equalTo("Sorted:Newest → Oldest"));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1058")
    @DisplayName("Verify sorting by profit column in trading - operations tab")
    public void verifyTradingInfoDealsProfitSortingTest() {
        assertThat(
                "Assert sort by profit popup text",
                tradingPage.getSortByProfitPopupText(),
                equalTo("Sort by profit:Descending"));
        tradingPage.sortByProfit();
        assertThat(
                "Assert value in account column for the 1st operation is as expected",
                tradingPage.getOperationAccountByIndex(0),
                equalTo(String.format("%s%s", trade2.getAccount(), trade2.getPlatform())));
        assertThat(
                "Assert value in account column for the 2nd operation is as expected",
                tradingPage.getOperationAccountByIndex(1),
                equalTo(String.format("%s%s", trade1.getAccount(), trade1.getPlatform())));
        assertThat(
                "Assert sort by profit popup text",
                tradingPage.getSortByProfitPopupText(),
                equalTo("Sorted:Descending"));
        tradingPage.sortByProfit();
        assertThat(
                "Assert value in account column for the 1st operation is as expected",
                tradingPage.getOperationAccountByIndex(0),
                equalTo(String.format("%s%s", trade1.getAccount(), trade1.getPlatform())));
        assertThat(
                "Assert value in account column for the 2nd operation is as expected",
                tradingPage.getOperationAccountByIndex(1),
                equalTo(String.format("%s%s", trade2.getAccount(), trade2.getPlatform())));
        assertThat(
                "Assert sort by profit popup text",
                tradingPage.getSortByProfitPopupText(),
                equalTo("Sorted:Ascending"));
        tradingPage.sortByProfit();
        assertThat(
                "Assert value in account column for the 1st operation is as expected",
                tradingPage.getOperationAccountByIndex(0),
                equalTo(String.format("%s%s", trade2.getAccount(), trade2.getPlatform())));
        assertThat(
                "Assert value in account column for the 2nd operation is as expected",
                tradingPage.getOperationAccountByIndex(1),
                equalTo(String.format("%s%s", trade1.getAccount(), trade1.getPlatform())));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1059")
    @DisplayName("Verify sorting by close column in trading - operations tab")
    public void verifyTradingInfoDealsCloseSortingTest() {
        assertThat(
                "Assert sort by close popup text",
                tradingPage.getSortByClosePopupText(),
                equalTo("Sort by close time:Newest → Oldest"));
        tradingPage.sortByClose();
        assertThat(
                "Assert value in account column for the 1st operation is as expected",
                tradingPage.getOperationAccountByIndex(0),
                equalTo(String.format("%s%s", trade1.getAccount(), trade1.getPlatform())));
        assertThat(
                "Assert value in account column for the 2nd operation is as expected",
                tradingPage.getOperationAccountByIndex(1),
                equalTo(String.format("%s%s", trade2.getAccount(), trade2.getPlatform())));
        assertThat(
                "Assert sort by close popup text",
                tradingPage.getSortByClosePopupText(),
                equalTo("Sorted:Newest → Oldest"));
        tradingPage.sortByClose();
        assertThat(
                "Assert value in account column for the 1st operation is as expected",
                tradingPage.getOperationAccountByIndex(0),
                equalTo(String.format("%s%s", trade2.getAccount(), trade2.getPlatform())));
        assertThat(
                "Assert value in account column for the 2nd operation is as expected",
                tradingPage.getOperationAccountByIndex(1),
                equalTo(String.format("%s%s", trade1.getAccount(), trade1.getPlatform())));
        assertThat(
                "Assert sort by close popup text",
                tradingPage.getSortByClosePopupText(),
                equalTo("Sorted:Oldest → Newest"));
        tradingPage.sortByClose();
        assertThat(
                "Assert value in account column for the 1st operation is as expected",
                tradingPage.getOperationAccountByIndex(0),
                equalTo(String.format("%s%s", trade2.getAccount(), trade2.getPlatform())));
        assertThat(
                "Assert value in account column for the 2nd operation is as expected",
                tradingPage.getOperationAccountByIndex(1),
                equalTo(String.format("%s%s", trade1.getAccount(), trade1.getPlatform())));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("576")
    @DisplayName("Verify filtration by type in trading - operations tab")
    public void verifyTradingInfoDealsTypeFiltrationTest() {
        tradingPage.openFilter();
        tradingPage.clickFilterCheckbox(trade2.getTicketType());
        tradingPage.clickApplyButton();
        assertThat("Verify there is 1 operations with filtration", tradingPage.getOperationsCount(), equalTo(1));
        assertThat(
                "Assert only the expected operation is present in the table",
                tradingPage.getOperationAccountByIndex(0),
                equalTo(String.format("%s%s", trade2.getAccount(), trade2.getPlatform())));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("577")
    @DisplayName("Verify filtration by accounts in trading - operations tab")
    public void verifyTradingInfoDealsAccountsFiltrationTest() {
        tradingPage.openFilter();
        tradingPage.clickFilterCheckbox(trade2.getAccount().toString());
        tradingPage.clickApplyButton();
        assertThat("Verify there is 1 operations with filtration", tradingPage.getOperationsCount(), equalTo(1));
        assertThat(
                "Assert only the expected operation is present in the table",
                tradingPage.getOperationAccountByIndex(0),
                equalTo(String.format("%s%s", trade2.getAccount(), trade2.getPlatform())));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("578")
    @DisplayName("Verify filtration by open date in trading - operations tab")
    public void verifyTradingInfoDealsOpenDateFiltrationTest() {
        tradingPage.openFilter();
        tradingPage.selectOpenDate(convertDateTimeDbToDate(trade2.getOpenTime()));
        tradingPage.clickApplyButton();
        assertThat("Verify there is 1 operations with filtration", tradingPage.getOperationsCount(), equalTo(1));
        assertThat(
                "Assert only the expected operation is present in the table",
                tradingPage.getOperationAccountByIndex(0),
                equalTo(String.format("%s%s", trade2.getAccount(), trade2.getPlatform())));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("579")
    @DisplayName("Verify filtration by close date in trading - operations tab")
    public void verifyTradingInfoDealsCloseDateFiltrationTest() {
        tradingPage.openFilter();
        tradingPage.selectCloseDate(convertDateTimeDbToDate(trade2.getCloseTime()));
        tradingPage.clickApplyButton();
        assertThat("Verify there is 1 operations with filtration", tradingPage.getOperationsCount(), equalTo(1));
        assertThat(
                "Assert only the expected operation is present in the table",
                tradingPage.getOperationAccountByIndex(0),
                equalTo(String.format("%s%s", trade2.getAccount(), trade2.getPlatform())));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("580")
    @DisplayName("Verify filtration by duration in trading - operations tab")
    public void verifyTradingInfoDealsDurationFiltrationTest() {
        tradingPage.openFilter();
        tradingPage.fillDurationValues("1", "");
        tradingPage.clickApplyButton();
        assertThat("Verify there is 1 operations with filtration", tradingPage.getOperationsCount(), equalTo(1));
        assertThat(
                "Assert only the expected operation is present in the table",
                tradingPage.getOperationAccountByIndex(0),
                equalTo(String.format("%s%s", trade2.getAccount(), trade2.getPlatform())));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("581")
    @DisplayName("Verify filtration by symbol in trading - operations tab")
    public void verifyTradingInfoDealsSymbolFiltrationTest() {
        tradingPage.openFilter();
        tradingPage.clickFilterCheckbox(trade2.getSymbol());
        tradingPage.clickApplyButton();
        assertThat("Verify there is 1 operations with filtration", tradingPage.getOperationsCount(), equalTo(1));
        assertThat(
                "Assert only the expected operation is present in the table",
                tradingPage.getOperationAccountByIndex(0),
                equalTo(String.format("%s%s", trade2.getAccount(), trade2.getPlatform())));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("582")
    @DisplayName("Verify filtration by profit in trading - operations tab")
    public void verifyTradingInfoDealsProfitFiltrationTest() {
        tradingPage.openFilter();
        tradingPage.fillProfitValues(trade2.getProfitUsd().toString(), "200");
        tradingPage.clickApplyButton();
        assertThat("Verify there is 1 operations with filtration", tradingPage.getOperationsCount(), equalTo(1));
        assertThat(
                "Assert only the expected operation is present in the table",
                tradingPage.getOperationAccountByIndex(0),
                equalTo(String.format("%s%s", trade2.getAccount(), trade2.getPlatform())));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("583")
    @DisplayName("Verify filtration by volume in trading - operations tab")
    public void verifyTradingInfoDealsVolumeFiltrationTest() {
        tradingPage.openFilter();
        tradingPage.fillVolumeAmountValues(trade2.getNotionalValueUsd().toString(), "200");
        tradingPage.clickApplyButton();
        assertThat("Verify there is 1 operations with filtration", tradingPage.getOperationsCount(), equalTo(1));
        assertThat(
                "Assert only the expected operation is present in the table",
                tradingPage.getOperationAccountByIndex(0),
                equalTo(String.format("%s%s", trade2.getAccount(), trade2.getPlatform())));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("584")
    @DisplayName("Verify filtration by method in trading - operations tab")
    public void verifyTradingInfoDealsMethodFiltrationTest() {
        tradingPage.openFilter();
        tradingPage.clickFilterCheckbox(trade2.getReasonName());
        tradingPage.clickApplyButton();
        assertThat("Verify there is 1 operations with filtration", tradingPage.getOperationsCount(), equalTo(1));
        assertThat(
                "Assert only the expected operation is present in the table",
                tradingPage.getOperationAccountByIndex(0),
                equalTo(String.format("%s%s", trade2.getAccount(), trade2.getPlatform())));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("586")
    @DisplayName("Verify reset functionality for filtration in trading - operations tab")
    public void verifyTradingInfoDealsResetFiltrationTest() {
        tradingPage.openFilter();
        // Type
        tradingPage.clickFilterCheckbox(trade2.getTicketType());
        tradingPage.resetTypeFilterAndVerify();
        // Accounts
        tradingPage.clickFilterCheckbox(trade2.getAccount().toString());
        tradingPage.resetAccountsFilterAndVerify();
        // Symbol
        tradingPage.clickFilterCheckbox(trade2.getSymbol());
        tradingPage.resetSymbolFilterAndVerify();
        // Method
        tradingPage.clickFilterCheckbox(trade2.getReasonName());
        tradingPage.resetMethodFilterAndVerify();
        // Open date
        tradingPage.selectOpenDate(convertDateTimeDbToDate(trade2.getOpenTimeUtc()));
        tradingPage.resetOpenDateFilterAndVerify();
        // Close date
        tradingPage.selectCloseDate(convertDateTimeDbToDate(trade2.getCloseTimeUtc()));
        tradingPage.resetCloseDateFilterAndVerify();
        // Duration
        tradingPage.fillDurationValues("1", "");
        tradingPage.resetDurationFilterAndVerify();
        // Profit
        tradingPage.fillProfitValues(trade2.getProfitUsd().toString(), "200");
        tradingPage.resetProfitFilterAndVerify();
        // Volume
        tradingPage.fillVolumeAmountValues(trade2.getNotionalValueUsd().toString(), "200");
        tradingPage.resetVolumeFilterAndVerify();
        // Reset All
        tradingPage.clickFilterCheckbox(trade2.getTicketType());
        tradingPage.clickFilterCheckbox(trade2.getAccount().toString());
        tradingPage.clickFilterCheckbox(trade2.getSymbol());
        tradingPage.clickFilterCheckbox(trade2.getReasonName());
        tradingPage.selectOpenDate(convertDateTimeDbToDate(trade2.getOpenTimeUtc()));
        tradingPage.selectCloseDate(convertDateTimeDbToDate(trade2.getCloseTimeUtc()));
        tradingPage.fillDurationValues("1", "");
        tradingPage.fillProfitValues(trade2.getProfitUsd().toString(), "200");
        tradingPage.fillVolumeAmountValues(trade2.getNotionalValueUsd().toString(), "200");
        tradingPage.resetAllFiltersAndVerify();
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("587")
    @DisplayName("Verify tooltips for filtration in trading - operations tab")
    public void verifyTradingInfoDealsTooltipsFiltrationTest() {
        tradingPage.openFilter();
        tradingPage.verifyOpenDateTooltip();
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("818")
    @DisplayName("Verify Type and Method filter options in trading - operations tab")
    public void verifyTradingInfoDealsFilterOptionsTest() {
        tradingPage.openFilter();
        tradingPage.checkTypeFilterList();
        tradingPage.checkMethodFilterList();
    }

    @AfterAll
    public static void teardown() throws SQLException {
        deleteObjectFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", crmTbUser.ucid));
        deleteObjectFromDb(
                MT4_TRADES_COERCED_TABLE_NAME,
                String.format("account = %s OR account = %s", account1.account, account2.account));
        closeAlert(crmTbUser.ucid);
    }
}
