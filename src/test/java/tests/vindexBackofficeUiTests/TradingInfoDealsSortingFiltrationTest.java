package tests.vindexBackofficeUiTests;

import businessObjects.db.clickhouse.crmTbAccount.CrmTbAccountObject;
import businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObject;
import businessObjects.db.clickhouse.mtTbTradeTable.MtTbTradeObject;
import businessObjects.kafka.alerts.RuleAlert;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.data.ClientHelper;
import helpers.kafka.KafkaHelper;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

import java.sql.SQLException;

import static businessObjects.db.clickhouse.crmTbAccount.CrmTbAccountObjectFactory.generateAdditionalCrmTbAccountDataForUi;
import static businessObjects.db.clickhouse.crmTbAccount.CrmTbAccountObjectFactory.generateCrmTbAccountDataForUi;
import static businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObjectFactory.generateUserByClient;
import static businessObjects.db.clickhouse.mtTbTradeTable.MtTbTradeFactory.generateMtTbTrade;
import static businessObjects.kafka.alerts.RuleAlertFactory.generateRuleAlertByUcid;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.BoHelper.closeAlert;
import static helpers.database.DbHelper.deleteEntryFromDb;
import static helpers.database.DbHelper.insertObjectToDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static utils.Constants.*;
import static utils.Utils.*;

public class TradingInfoDealsSortingFiltrationTest extends TestBaseWeb {

    private static final KafkaHelper kafka = new KafkaHelper();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final ClientHelper client = getRandomVantageClientAllFields();
    private static final CrmTbUserObject crmTbUser = generateUserByClient(client);
    private static CrmTbAccountObject account1;
    private static CrmTbAccountObject account2;
    private static MtTbTradeObject trade1;
    private static MtTbTradeObject trade2;

    @BeforeAll
    public static void setup() throws ReflectiveOperationException, SQLException, JsonProcessingException {
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        account1 = generateCrmTbAccountDataForUi(client);
        insertObjectToDb(CRM_ACCOUNT_TABLE_NAME, account1);
        account2 = generateAdditionalCrmTbAccountDataForUi(client);
        account2.platform = "MT5";
        insertObjectToDb(CRM_ACCOUNT_TABLE_NAME, account2);
        trade1 = generateMtTbTrade(account1.account, account1.serverIdSt);
        insertObjectToDb(MT_TRADES_TABLE_NAME, trade1);
        trade2 = generateMtTbTrade(account2.account, account2.serverIdSt);
        trade2.platform = "MT5";
        trade2.type = "Sell";
        trade2.openTime = getPreviousWeekTimestampDbFormat();
        trade2.closeTime = getYesterdayTimestampDbFormat();
        trade2.symbol = "GBPJPY";
        trade2.profitUsd = 101.22;
        trade2.volumeUsd = 123.44;
        trade2.reason = "API";
        insertObjectToDb(MT_TRADES_TABLE_NAME, trade2);
        RuleAlert alert = generateRuleAlertByUcid(crmTbUser.ucid);
        kafka.produceMessage(alert.alertId, objectMapper.writeValueAsString(alert), KAFKA_TOPIC_ALERTS);
    }

    @BeforeEach
    public void loginAndGoToOperations() throws InterruptedException {
        investigationPage.navigate();
        keycloackPage.loginAsCoreUser();
        investigationPage.waitForPageToLoad();
        investigationPage.clickSuspiciousClientsFiltration();
        investigationPage.selectBrandFilterByText(crmTbUser.brand);
        investigationPage.clickApplyFiltrationButton();
        investigationPage.filterUnassigned();
        investigationPage.waitForPageToLoad();
        investigationPage.scrollClientCardsToBottom();
        investigationPage.clickClientCardByClientId(String.valueOf(crmTbUser.userId));
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
        assertThat("Assert value in account column for the 1st operation is as expected", tradingPage.getOperationAccountByIndex(0), equalTo(String.format("%s%s", trade2.account, trade2.platform)));
        assertThat("Assert value in account column for the 2nd operation is as expected", tradingPage.getOperationAccountByIndex(1), equalTo(String.format("%s%s", trade1.account, trade1.platform)));
        assertThat("Assert sort by open popup text", tradingPage.getSortByOpenPopupText(), equalTo("Change sorting to:Oldest opened → Newest opened"));
        tradingPage.sortByOpen();
        assertThat("Assert value in account column for the 1st operation is as expected", tradingPage.getOperationAccountByIndex(0), equalTo(String.format("%s%s", trade1.account, trade1.platform)));
        assertThat("Assert value in account column for the 2nd operation is as expected", tradingPage.getOperationAccountByIndex(1), equalTo(String.format("%s%s", trade2.account, trade2.platform)));
        assertThat("Assert sort by open popup text", tradingPage.getSortByOpenPopupText(), equalTo("Change sorting to:Newest opened → Oldest opened"));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("576")
    @DisplayName("Verify filtration by type in trading - operations tab")
    public void verifyTradingInfoDealsTypeFiltrationTest() {
        tradingPage.openFilter();
        tradingPage.clickFilterCheckbox(trade2.type);
        tradingPage.clickApplyButton();
        assertThat("Verify there is 1 operations with filtration", tradingPage.getOperationsCount(), equalTo(1));
        assertThat("Assert only the expected operation is present in the table", tradingPage.getOperationAccountByIndex(0), equalTo(String.format("%s%s", trade2.account, trade2.platform)));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("577")
    @DisplayName("Verify filtration by accounts in trading - operations tab")
    public void verifyTradingInfoDealsAccountsFiltrationTest() {
        tradingPage.openFilter();
        tradingPage.clickFilterCheckbox(trade2.account.toString());
        tradingPage.clickApplyButton();
        assertThat("Verify there is 1 operations with filtration", tradingPage.getOperationsCount(), equalTo(1));
        assertThat("Assert only the expected operation is present in the table", tradingPage.getOperationAccountByIndex(0), equalTo(String.format("%s%s", trade2.account, trade2.platform)));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("578")
    @DisplayName("Verify filtration by open date in trading - operations tab")
    public void verifyTradingInfoDealsOpenDateFiltrationTest() {
        tradingPage.openFilter();
        tradingPage.selectOpenDate(convertDateTimeDbToDate(trade2.openTime));
        tradingPage.clickApplyButton();
        assertThat("Verify there is 1 operations with filtration", tradingPage.getOperationsCount(), equalTo(1));
        assertThat("Assert only the expected operation is present in the table", tradingPage.getOperationAccountByIndex(0), equalTo(String.format("%s%s", trade2.account, trade2.platform)));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("579")
    @DisplayName("Verify filtration by close date in trading - operations tab")
    public void verifyTradingInfoDealsCloseDateFiltrationTest() {
        tradingPage.openFilter();
        tradingPage.selectCloseDate(convertDateTimeDbToDate(trade2.closeTime));
        tradingPage.clickApplyButton();
        assertThat("Verify there is 1 operations with filtration", tradingPage.getOperationsCount(), equalTo(1));
        assertThat("Assert only the expected operation is present in the table", tradingPage.getOperationAccountByIndex(0), equalTo(String.format("%s%s", trade2.account, trade2.platform)));
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
        assertThat("Assert only the expected operation is present in the table", tradingPage.getOperationAccountByIndex(0), equalTo(String.format("%s%s", trade2.account, trade2.platform)));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("581")
    @DisplayName("Verify filtration by symbol in trading - operations tab")
    public void verifyTradingInfoDealsSymbolFiltrationTest() {
        tradingPage.openFilter();
        tradingPage.clickFilterCheckbox(trade2.symbol);
        tradingPage.clickApplyButton();
        assertThat("Verify there is 1 operations with filtration", tradingPage.getOperationsCount(), equalTo(1));
        assertThat("Assert only the expected operation is present in the table", tradingPage.getOperationAccountByIndex(0), equalTo(String.format("%s%s", trade2.account, trade2.platform)));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("582")
    @DisplayName("Verify filtration by profit in trading - operations tab")
    public void verifyTradingInfoDealsProfitFiltrationTest() {
        tradingPage.openFilter();
        tradingPage.fillProfitValues(trade2.profitUsd.toString(), "200");
        tradingPage.clickApplyButton();
        assertThat("Verify there is 1 operations with filtration", tradingPage.getOperationsCount(), equalTo(1));
        assertThat("Assert only the expected operation is present in the table", tradingPage.getOperationAccountByIndex(0), equalTo(String.format("%s%s", trade2.account, trade2.platform)));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("583")
    @DisplayName("Verify filtration by volume in trading - operations tab")
    public void verifyTradingInfoDealsVolumeFiltrationTest() {
        tradingPage.openFilter();
        tradingPage.fillVolumeValues(trade2.volumeUsd.toString(), "200");
        tradingPage.clickApplyButton();
        assertThat("Verify there is 1 operations with filtration", tradingPage.getOperationsCount(), equalTo(1));
        assertThat("Assert only the expected operation is present in the table", tradingPage.getOperationAccountByIndex(0), equalTo(String.format("%s%s", trade2.account, trade2.platform)));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("584")
    @DisplayName("Verify filtration by method in trading - operations tab")
    public void verifyTradingInfoDealsMethodFiltrationTest() {
        tradingPage.openFilter();
        tradingPage.clickFilterCheckbox(trade2.reason);
        tradingPage.clickApplyButton();
        assertThat("Verify there is 1 operations with filtration", tradingPage.getOperationsCount(), equalTo(1));
        assertThat("Assert only the expected operation is present in the table", tradingPage.getOperationAccountByIndex(0), equalTo(String.format("%s%s", trade2.account, trade2.platform)));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("585")
    @DisplayName("Verify preset options for filtration in trading - operations tab")
    public void verifyTradingInfoDealsPresetOptionsFiltrationTest() {
        tradingPage.openFilter();
        tradingPage.verifyPresetOptionsForFilters();
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("586")
    @DisplayName("Verify reset functionality for filtration in trading - operations tab")
    public void verifyTradingInfoDealsResetFiltrationTest() {
        tradingPage.openFilter();
        // Type
        tradingPage.clickFilterCheckbox(trade2.type);
        tradingPage.resetTypeFilterAndVerify();
        // Accounts
        tradingPage.clickFilterCheckbox(trade2.account.toString());
        tradingPage.resetAccountsFilterAndVerify();
        // Symbol
        tradingPage.clickFilterCheckbox(trade2.symbol);
        tradingPage.resetSymbolFilterAndVerify();
        // Method
        tradingPage.clickFilterCheckbox(trade2.reason);
        tradingPage.resetMethodFilterAndVerify();
        // Open date
        tradingPage.selectOpenDate(convertDateTimeDbToDate(trade2.openTime));
        tradingPage.resetOpenDateFilterAndVerify();
        // Close date
        tradingPage.selectCloseDate(convertDateTimeDbToDate(trade2.closeTime));
        tradingPage.resetCloseDateFilterAndVerify();
        // Duration
        tradingPage.fillDurationValues("1", "");
        tradingPage.resetDurationFilterAndVerify();
        // Profit
        tradingPage.fillProfitValues(trade2.profitUsd.toString(), "200");
        tradingPage.resetProfitFilterAndVerify();
        // Volume
        tradingPage.fillVolumeValues(trade2.volumeUsd.toString(), "200");
        tradingPage.resetVolumeFilterAndVerify();
        // Reset All
        tradingPage.clickFilterCheckbox(trade2.type);
        tradingPage.clickFilterCheckbox(trade2.account.toString());
        tradingPage.clickFilterCheckbox(trade2.symbol);
        tradingPage.clickFilterCheckbox(trade2.reason);
        tradingPage.selectOpenDate(convertDateTimeDbToDate(trade2.openTime));
        tradingPage.selectCloseDate(convertDateTimeDbToDate(trade2.closeTime));
        tradingPage.fillDurationValues("1", "");
        tradingPage.fillProfitValues(trade2.profitUsd.toString(), "200");
        tradingPage.fillVolumeValues(trade2.volumeUsd.toString(), "200");
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
        tradingPage.verifyProfitTooltip();
    }

    @AfterAll
    public static void teardown() throws SQLException {
        deleteEntryFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", crmTbUser.ucid));
        deleteEntryFromDb(MT_TRADES_TABLE_NAME, String.format("account = %s OR account = %s", account1.account, account2.account));
        closeAlert(crmTbUser.ucid);
    }
}
