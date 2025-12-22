package tests.vindex_backoffice_ui_tests.investigationTool.trading;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateAdditionalCrmTbAccountDataForUi;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountDataForUi;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObjectFactory.generateMt4TradesCoerced;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.DbHelper.*;
import static helpers.database.DbHelper.deleteEntryFromDb;
import static java.util.function.Function.identity;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static utils.Constants.*;
import static utils.Utils.insertCrmAccountsToDb;

import business_objects.db.backoffice_db.IllegalTrades;
import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import helpers.data.ClientHelper;
import helpers.database.DbName;
import io.qameta.allure.AllureId;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

@Tag(TEAM_BACKOFFICE)
@Tag(LAYER_WEB)
class IllegalProfitCalculationTest extends TestBaseWeb {
    private static final Pattern MONEY = Pattern.compile("([-\\d.,]+)");
    private static final ClientHelper client = getRandomVantageClientAllFields();
    private static final CrmTbUserObject crmTbUser = generateUserByClient(client);

    private static final CrmTbAccountObject account1 = generateCrmTbAccountDataForUi(client);
    ;
    private static final CrmTbAccountObject account2 = generateAdditionalCrmTbAccountDataForUi(client);
    ;

    private static MtMt4TradesCoercedObject balance1;
    private static MtMt4TradesCoercedObject balance2;
    private static MtMt4TradesCoercedObject buy1;
    private static MtMt4TradesCoercedObject buy2;
    private static MtMt4TradesCoercedObject sell1;
    private static MtMt4TradesCoercedObject sell2;

    static BigDecimal parseMoney(String ui) {
        var m = MONEY.matcher(ui);
        if (!m.find()) throw new IllegalArgumentException("No number in: " + ui);

        String raw = m.group(1).replace(",", "");
        return new BigDecimal(raw);
    }

    @BeforeAll
    static void setup() throws ReflectiveOperationException, SQLException, JsonProcessingException {
        account2.setServerIdSt(client.getServerId());
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        insertCrmAccountsToDb(account1);
        insertCrmAccountsToDb(account2);
        insertObjectToDb(MT_ACCOUNT_TABLE_NAME, generateMtAccountByCrmTbAccount(account1));
        insertObjectToDb(MT_ACCOUNT_TABLE_NAME, generateMtAccountByCrmTbAccount(account2));

        var now = OffsetDateTime.now();
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // balance
        balance1 = generateMt4TradesCoerced(client);
        balance1.setTicketType("Balance");
        balance1.setOpenTime(now.minusDays(2).format(formatter));
        balance1.setOpenTimeUtc(now.minusDays(2).format(formatter));
        balance1.setAccount((long) account1.account);
        balance1.setProfit(1000d);
        balance1.setProfitUsd(1000d);
        balance1.setCommission(0d);
        balance1.setCommissionUsd(0d);
        balance1.setStorage(0d);
        balance1.setStorageUsd(0d);
        balance2 = generateMt4TradesCoerced(client);
        balance2.setOpenTime(now.minusDays(2).format(formatter));
        balance2.setOpenTimeUtc(now.minusDays(2).format(formatter));
        balance2.setTicketType("Balance");
        balance2.setAccount((long) client.getTradingAccount2());
        balance2.setProfit(2000d);
        balance2.setProfitUsd(2000d);
        balance2.setCommission(0d);
        balance2.setCommissionUsd(0d);
        balance2.setStorage(0d);
        balance2.setStorageUsd(0d);

        // Buy
        buy1 = generateMt4TradesCoerced(client);
        buy1.setOpenTime(now.minusDays(1).format(formatter));
        buy1.setOpenTimeUtc(now.minusDays(1).format(formatter));
        buy1.setTicketType("Buy");
        buy1.setAccount((long) account1.account);
        buy1.setProfit(1000d);
        buy1.setProfitUsd(1000d);
        buy1.setCommission(100d);
        buy1.setCommissionUsd(100d);
        buy1.setStorage(10d);
        buy1.setStorageUsd(10d);
        buy2 = generateMt4TradesCoerced(client);
        buy2.setOpenTime(now.minusDays(1).format(formatter));
        buy2.setOpenTimeUtc(now.minusDays(1).format(formatter));
        buy2.setTicketType("Buy");
        buy2.setAccount((long) client.getTradingAccount2());
        buy2.setProfit(2000d);
        buy2.setProfitUsd(2000d);
        buy2.setCommission(200d);
        buy2.setCommissionUsd(200d);
        buy2.setStorage(20d);
        buy2.setStorageUsd(20d);

        // Sell
        sell1 = generateMt4TradesCoerced(client);
        sell1.setOpenTime(now.format(formatter));
        sell1.setOpenTimeUtc(now.format(formatter));
        sell1.setTicketType("Sell");
        sell1.setAccount((long) account1.account);
        sell1.setProfit(3000d);
        sell1.setProfitUsd(3000d);
        sell1.setCommission(300d);
        sell1.setCommissionUsd(300d);
        sell1.setStorage(30d);
        sell1.setStorageUsd(30d);
        sell2 = generateMt4TradesCoerced(client);
        sell2.setOpenTime(now.format(formatter));
        sell2.setOpenTimeUtc(now.format(formatter));
        sell2.setTicketType("Sell");
        sell2.setAccount((long) client.getTradingAccount2());
        sell2.setProfit(4000d);
        sell2.setProfitUsd(4000d);
        sell2.setCommission(40d);
        sell2.setCommissionUsd(40d);
        sell2.setStorage(40d);
        sell2.setStorageUsd(40d);

        // Inserts
        insertObjectsToDb(
                DbName.CLICKHOUSE,
                MT4_TRADES_COERCED_TABLE_NAME,
                List.of(balance1, balance2, buy1, buy2, sell1, sell2));
    }

    @AfterAll
    static void teardown() throws SQLException {
        deleteEntryFromDb(
                MT4_TRADES_COERCED_TABLE_NAME,
                String.format("account = '%s' OR account = '%s'", account1.account, account2.account));
        deleteEntryFromDb(
                MT_ACCOUNT_TABLE_NAME,
                String.format("account = '%s' OR account = '%s'", account1.account, account2.account));
        deleteEntryFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", crmTbUser.ucid));
    }

    @AfterEach
    void afterEach() throws Exception {
        deleteEntryFromDb(
                DbName.POSTGRES,
                BO_ILLEGAL_TRADES_TABLE_NAME,
                String.format(
                        "(account = '%s' OR account = '%s') AND server_id = %s",
                        account1.account, account2.account, client.getServerId()));
    }

    @Test
    @AllureId("1975")
    @DisplayName("All trades illegal test")
    void verifyIllegalProfitAllTradesTest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        tradingPage.openTradingTab();
        tradingPage.openOperationsTab();
        tradingPage.waitForPageToLoad();
        tradingPage.clickIllegalProfitButton();
        tradingPage.clickSelectAllTradesAsIllegal();
        tradingPage.waitForPageToLoad();

        var acc1IllegalProfit = buy1.getProfitUsd()
                + buy1.getCommissionUsd()
                + buy1.getStorageUsd()
                + sell1.getProfitUsd()
                + sell1.getCommissionUsd()
                + sell1.getStorageUsd();
        var acc2IllegalProfit = buy2.getProfitUsd()
                + buy2.getCommissionUsd()
                + buy2.getStorageUsd()
                + sell2.getProfitUsd()
                + sell2.getCommissionUsd()
                + sell2.getStorageUsd();
        var expectedIllegalAmount = acc1IllegalProfit + acc2IllegalProfit;
        var selected = tradingPage.getSelectedIllegalTradesCounter();
        var illegalProfit = tradingPage.getSelectedIllegalProfitAmount();
        var accountsQuantityString = tradingPage.getSelectedIllegalProfitAccountsQuantity();
        var bigDecimalIllegalProfit = parseMoney(illegalProfit);
        var bigDecimalExpectedIllegalProfit = BigDecimal.valueOf(expectedIllegalAmount);
        var diff = bigDecimalIllegalProfit
                .subtract(bigDecimalExpectedIllegalProfit)
                .abs();
        assertThat("4 deals selected check", selected, equalTo("4 selected"));
        assertThat("IllegalProfit amount check", diff, lessThanOrEqualTo(new BigDecimal("0.0000001")));
        assertThat("account count check", accountsQuantityString, equalTo("profit on 2 accounts"));

        tradingPage.clickSaveAsIllegalProfit();
        var text = tradingPage.getToastMessageText();
        assertThat(
                "Toaster text",
                text,
                equalTo(
                        "Illegal profit savedSuggested deduction will be calculated automatically after fraud confirmation"));

        var illegalTrades = getObjectsFromDB(
                DbName.POSTGRES,
                BO_ILLEGAL_TRADES_TABLE_NAME,
                String.format("account = '%s' OR account = '%s'", account1.account, account2.account),
                IllegalTrades.class);
        var illegalTradesMap = illegalTrades.stream().collect(Collectors.toMap(IllegalTrades::getAccount, identity()));
        var illegalProfit1 =
                illegalTradesMap.get(String.valueOf(account1.getAccount())).getIllegalProfit();
        var ticketCount1 =
                illegalTradesMap.get(String.valueOf(account1.getAccount())).getTicketCount();
        var illegalProfit2 =
                illegalTradesMap.get(String.valueOf(account2.getAccount())).getIllegalProfit();
        var ticketCount2 =
                illegalTradesMap.get(String.valueOf(account2.getAccount())).getTicketCount();

        var diffWithSaved1 = illegalProfit1 - acc1IllegalProfit;
        assertThat("acc1 saved illegal profit check", diffWithSaved1, lessThanOrEqualTo(0.000_000_1d));
        assertThat("acc1 saved ticket count check", ticketCount1, equalTo(2));

        var diffWithSaved2 = illegalProfit2 - acc2IllegalProfit;
        assertThat("acc2 saved illegal profit check", diffWithSaved2, lessThanOrEqualTo(0.000_000_1d));
        assertThat("acc2 saved ticket count check", ticketCount2, equalTo(2));
    }

    @Test
    @AllureId("1976")
    @DisplayName("Account filter trades illegal test")
    void verifyIllegalProfitAccFilterTradesTest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        tradingPage.openTradingTab();
        tradingPage.openOperationsTab();
        tradingPage.waitForPageToLoad();
        tradingPage.clickIllegalProfitButton();

        tradingPage.openFilter();
        // Accounts
        tradingPage.clickFilterCheckbox(account1.getAccount().toString());
        tradingPage.clickApplyButton();
        tradingPage.waitForPageToLoad();

        tradingPage.clickSelectAllTradesAsIllegal();

        var acc1IllegalProfit = buy1.getProfitUsd()
                + buy1.getCommissionUsd()
                + buy1.getStorageUsd()
                + sell1.getProfitUsd()
                + sell1.getCommissionUsd()
                + sell1.getStorageUsd();
        var selected = tradingPage.getSelectedIllegalTradesCounter();
        var illegalProfit = tradingPage.getSelectedIllegalProfitAmount();
        var accountsQuantityString = tradingPage.getSelectedIllegalProfitAccountsQuantity();
        var bigDecimalIllegalProfit = parseMoney(illegalProfit);
        var bigDecimalExpectedIllegalProfit = BigDecimal.valueOf(acc1IllegalProfit);
        var diff = bigDecimalIllegalProfit
                .subtract(bigDecimalExpectedIllegalProfit)
                .abs();
        assertThat("2 deals selected check", selected, equalTo("2 selected"));
        assertThat("IllegalProfit acc1 filter amount check", diff, lessThanOrEqualTo(new BigDecimal("0.0000001")));
        assertThat("account count check", accountsQuantityString, equalTo("profit on 1 account"));

        tradingPage.clickSaveAsIllegalProfit();
        var text = tradingPage.getToastMessageText();
        assertThat(
                "Toaster text",
                text,
                equalTo(
                        "Illegal profit savedSuggested deduction will be calculated automatically after fraud confirmation"));

        var illegalTrades = getObjectsFromDB(
                DbName.POSTGRES,
                BO_ILLEGAL_TRADES_TABLE_NAME,
                String.format("account = '%s' OR account = '%s'", account1.account, account2.account),
                IllegalTrades.class);
        assertThat(illegalTrades.size(), equalTo(1));
        var illegalTradesMap = illegalTrades.stream().collect(Collectors.toMap(IllegalTrades::getAccount, identity()));
        var illegalProfit1 =
                illegalTradesMap.get(String.valueOf(account1.getAccount())).getIllegalProfit();
        var ticketCount1 =
                illegalTradesMap.get(String.valueOf(account1.getAccount())).getTicketCount();

        var diffWithSaved1 = illegalProfit1 - acc1IllegalProfit;
        assertThat("acc1 saved illegal profit check", diffWithSaved1, lessThanOrEqualTo(0.000_000_1d));
        assertThat("acc1 saved ticket count check", ticketCount1, equalTo(2));
    }

    @Test
    @AllureId("1977")
    @DisplayName("Exclude trades illegal test")
    void verifyIllegalProfitExcludeTradesTest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        tradingPage.openTradingTab();
        tradingPage.openOperationsTab();
        tradingPage.waitForPageToLoad();
        tradingPage.clickIllegalProfitButton();

        tradingPage.clickSelectAllTradesAsIllegal();

        tradingPage.selectIllegalTradeByTicket(buy2.getTicket());
        tradingPage.selectIllegalTradeByTicket(sell2.getTicket());

        var acc1IllegalProfit = buy1.getProfitUsd()
                + buy1.getCommissionUsd()
                + buy1.getStorageUsd()
                + sell1.getProfitUsd()
                + sell1.getCommissionUsd()
                + sell1.getStorageUsd();
        var selected = tradingPage.getSelectedIllegalTradesCounter();
        var illegalProfit = tradingPage.getSelectedIllegalProfitAmount();
        var accountsQuantityString = tradingPage.getSelectedIllegalProfitAccountsQuantity();
        var bigDecimalIllegalProfit = parseMoney(illegalProfit);
        var bigDecimalExpectedIllegalProfit = BigDecimal.valueOf(acc1IllegalProfit);
        var diff = bigDecimalIllegalProfit
                .subtract(bigDecimalExpectedIllegalProfit)
                .abs();
        assertThat("2 deals selected check", selected, equalTo("2 selected"));
        assertThat("IllegalProfit acc1 filter amount check", diff, lessThanOrEqualTo(new BigDecimal("0.0000001")));
        assertThat("account count check", accountsQuantityString, equalTo("profit on 1 account"));

        tradingPage.clickSaveAsIllegalProfit();
        var text = tradingPage.getToastMessageText();
        assertThat(
                "Toaster text",
                text,
                equalTo(
                        "Illegal profit savedSuggested deduction will be calculated automatically after fraud confirmation"));

        var illegalTrades = getObjectsFromDB(
                DbName.POSTGRES,
                BO_ILLEGAL_TRADES_TABLE_NAME,
                String.format("account = '%s' OR account = '%s'", account1.account, account2.account),
                IllegalTrades.class);
        assertThat(illegalTrades.size(), equalTo(1));
        var illegalTradesMap = illegalTrades.stream().collect(Collectors.toMap(IllegalTrades::getAccount, identity()));
        var illegalProfit1 =
                illegalTradesMap.get(String.valueOf(account1.getAccount())).getIllegalProfit();
        var ticketCount1 =
                illegalTradesMap.get(String.valueOf(account1.getAccount())).getTicketCount();

        var diffWithSaved1 = illegalProfit1 - acc1IllegalProfit;
        assertThat("acc1 saved illegal profit check", diffWithSaved1, lessThanOrEqualTo(0.000_000_1d));
        assertThat("acc1 saved ticket count check", ticketCount1, equalTo(2));
    }

    @Test
    @AllureId("1978")
    @DisplayName("Include trades illegal test")
    void verifyIllegalProfitIncludeTradesTest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        tradingPage.openTradingTab();
        tradingPage.openOperationsTab();
        tradingPage.waitForPageToLoad();
        tradingPage.clickIllegalProfitButton();

        tradingPage.openFilter();
        // Accounts
        tradingPage.clickFilterCheckbox(account1.getAccount().toString());
        tradingPage.clickApplyButton();
        tradingPage.waitForPageToLoad();

        tradingPage.selectIllegalTradeByTicket(buy1.getTicket());

        var buy1IllegalProfit = buy1.getProfitUsd() + buy1.getCommissionUsd() + buy1.getStorageUsd();
        var selected = tradingPage.getSelectedIllegalTradesCounter();
        var illegalProfit = tradingPage.getSelectedIllegalProfitAmount();
        var accountsQuantityString = tradingPage.getSelectedIllegalProfitAccountsQuantity();
        var bigDecimalIllegalProfit = parseMoney(illegalProfit);
        var bigDecimalExpectedIllegalProfit = BigDecimal.valueOf(buy1IllegalProfit);
        var diff = bigDecimalIllegalProfit
                .subtract(bigDecimalExpectedIllegalProfit)
                .abs();
        assertThat("1 deals selected check", selected, equalTo("1 selected"));
        assertThat("IllegalProfit buy1 filter amount check", diff, lessThanOrEqualTo(new BigDecimal("0.0000001")));
        assertThat("account count check", accountsQuantityString, equalTo("profit on 1 account"));

        tradingPage.clickSaveAsIllegalProfit();
        var text = tradingPage.getToastMessageText();
        assertThat(
                "Toaster text",
                text,
                equalTo(
                        "Illegal profit savedSuggested deduction will be calculated automatically after fraud confirmation"));

        var illegalTrades = getObjectsFromDB(
                DbName.POSTGRES,
                BO_ILLEGAL_TRADES_TABLE_NAME,
                String.format("account = '%s' OR account = '%s'", account1.account, account2.account),
                IllegalTrades.class);
        assertThat(illegalTrades.size(), equalTo(1));
        var illegalTradesMap = illegalTrades.stream().collect(Collectors.toMap(IllegalTrades::getAccount, identity()));
        var illegalProfit1 =
                illegalTradesMap.get(String.valueOf(account1.getAccount())).getIllegalProfit();
        var ticketCount1 =
                illegalTradesMap.get(String.valueOf(account1.getAccount())).getTicketCount();

        var diffWithSaved1 = illegalProfit1 - buy1IllegalProfit;
        assertThat("acc1 saved illegal profit check", diffWithSaved1, lessThanOrEqualTo(0.000_000_1d));
        assertThat("acc1 saved ticket count check", ticketCount1, equalTo(1));
    }
}
