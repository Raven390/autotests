package tests.vindex_backoffice_ui_tests;

import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedObject;
import business_objects.db.clickhouse.mtAccount.MtAccountObject;
import business_objects.kafka.alerts.RuleAlert;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.data.ClientHelper;
import helpers.kafka.KafkaHelper;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

import java.sql.SQLException;
import java.util.List;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateAdditionalCrmTbAccountDataForUi;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountDataForUi;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.mtAccount.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedFactory.generateTradeByClient;
import static business_objects.kafka.alerts.RuleAlertFactory.generateRuleAlertByUcid;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.BoHelper.closeAlert;
import static helpers.database.CleanTableHelper.cleanMt5CoercedTableByUcid;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static utils.Constants.*;

public class TradingInfoAccountsTest extends TestBaseWeb {

    private static final KafkaHelper kafka = new KafkaHelper();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final ClientHelper client = getRandomVantageClientAllFields();
    private static final CrmTbUserObject crmTbUser = generateUserByClient(client);
    private static CrmTbAccountObject account1;
    private static CrmTbAccountObject account2;
    private static MtAccountObject mtAccount1;
    private static MtAccountObject mtAccount2;

    @BeforeAll
    public static void setup() throws ReflectiveOperationException, SQLException, JsonProcessingException {
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        account1 = generateCrmTbAccountDataForUi(client);
        account1.currency = "EUR";
        insertObjectToDb(CRM_ACCOUNT_TABLE_NAME, account1);
        insertObjectToDb(MT_ACCOUNT_TABLE_NAME, generateMtAccountByCrmTbAccount(account1));
        account2 = generateAdditionalCrmTbAccountDataForUi(client);
        account2.serverIdSt = 22;
        account2.accountStatus = "Inactive";
        Mt5DealsCoercedObject trade1 = generateTradeByClient(client);
        trade1.setProfitUsd(22.2);
        trade1.setStorageUsd(0);
        trade1.setCommissionUsd(0.0);
        Mt5DealsCoercedObject trade2 = generateTradeByClient(client);
        trade2.setAccount(account2.account);
        trade2.setServerId(account2.serverIdSt);
        trade2.setProfitUsd(22.2);
        trade2.setStorageUsd(0);
        trade2.setCommissionUsd(0.0);
        insertObjectToDb(CRM_ACCOUNT_TABLE_NAME, account2);

        mtAccount1 = generateMtAccountByCrmTbAccount(account1);
        mtAccount2 = generateMtAccountByCrmTbAccount(account2);
        insertObjectsToDb(MT_ACCOUNT_TABLE_NAME, List.of(mtAccount1, mtAccount2));
        insertObjectToDb(MT_ACCOUNT_TABLE_NAME, generateMtAccountByCrmTbAccount(account2));
        insertObjectsToDb(MT5_DEALS_COERCED_TABLE_NAME, List.of(trade1, trade2));
        RuleAlert alert = generateRuleAlertByUcid(crmTbUser.ucid);
        kafka.produceMessage(alert.alertId, objectMapper.writeValueAsString(alert), KAFKA_TOPIC_ALERTS);
    }

    @AfterAll
    public static void teardown() throws Exception {
        deleteEntryFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", crmTbUser.ucid));
        cleanMt5CoercedTableByUcid(MT5_DEALS_COERCED_TABLE_NAME, crmTbUser.ucid);
        closeAlert(crmTbUser.ucid);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("560")
    @DisplayName("Verify all data is present in trading info - accounts. Card view")
    public void verifyAccountsCardViewTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        tradingPage.openTradingTab();
        tradingPage.openAccountsTab();
        // Verify 1st account card
        assertThat("Assert that account balance in card view is as expected", tradingPage.getAccountBalance(account1.account), equalTo(String.format("%s %s", account1.balance, account1.currency)));
        assertThat("Assert that account balance usd in card view is as expected", tradingPage.getAccountBalanceUsd(account1.account), equalTo(String.format("%s %s", account1.balanceUsd, "USD")));
        assertThat("Assert that account status in card view is as expected", tradingPage.getAccountStatus(account1.account), equalTo(account1.accountStatus));
        assertThat("Assert that account platform in card view is as expected", tradingPage.getAccountPlatform(account1.account), equalTo(account1.platform));
        assertThat("Assert that account type in card view is as expected", tradingPage.getAccountType(account1.account), equalTo(account1.accountType));
        assertThat("Assert that account total pnl in card view is as expected", tradingPage.getAccountTotalPnl(account1.account), equalTo(String.format("%s %s", account1.pnl, account1.currency)));
        assertThat("Assert that account equity in card view is as expected", tradingPage.getAccountEquity(account1.account), equalTo(String.format("%s %s", account1.equity, account1.currency)));
        assertThat("Assert that account credit in card view is as expected", tradingPage.getAccountCredit(account1.account), equalTo(String.format("%s %s", account1.credit, account1.currency)));
        assertThat("Assert that account leverage in card view is as expected", tradingPage.getAccountLeverage(account1.account), equalTo(account1.leverage.toString()));
        assertThat("Assert that account margin free in card view is as expected", tradingPage.getAccountMarginFree(account1.account), equalTo(String.format("%s %s", account1.marginFree, account1.currency)));
        assertThat("Assert that account server in card view is as expected", tradingPage.getAccountServer(account1.account), equalTo(account1.serverName));
        assertThat("Assert that account group in card view is as expected", tradingPage.getAccountGroup(account1.account), equalTo(account1.accountGroup));
        assertThat("Assert that account created time in card view is as expected", tradingPage.getAccountCreatedTime(account1.account), equalTo(account1.createTimeUtc));
        assertThat("Assert that account updated time in card view is as expected", tradingPage.getAccountUpdatedTime(account1.account), equalTo(account1.lastUpdated));
        // Verify 2nd account card
        assertThat("Assert that account balance in card view is as expected", tradingPage.getAccountBalance(account2.account), equalTo(String.format("%s %s", account2.balance, account2.currency)));
        assertThat("Assert that account status in card view is as expected", tradingPage.getAccountStatus(account2.account), equalTo(account2.accountStatus));
        assertThat("Assert that account platform in card view is as expected", tradingPage.getAccountPlatform(account2.account), equalTo(account2.platform));
        assertThat("Assert that account type in card view is as expected", tradingPage.getAccountType(account2.account), equalTo(account2.accountType));
        assertThat("Assert that account total pnl in card view is as expected", tradingPage.getAccountTotalPnl(account2.account), equalTo(String.format("%s %s", account2.pnl, account2.currency)));
        assertThat("Assert that account equity in card view is as expected", tradingPage.getAccountEquity(account2.account), equalTo(String.format("%s %s", account2.equity, account2.currency)));
        assertThat("Assert that account credit in card view is as expected", tradingPage.getAccountCredit(account2.account), equalTo(String.format("%s %s", account2.credit, account2.currency)));
        assertThat("Assert that account leverage in card view is as expected", tradingPage.getAccountLeverage(account2.account), equalTo(account2.leverage.toString()));
        assertThat("Assert that account margin free in card view is as expected", tradingPage.getAccountMarginFree(account2.account), equalTo(String.format("%s %s", account2.marginFree, account2.currency)));
        assertThat("Assert that account server in card view is as expected", tradingPage.getAccountServer(account2.account), equalTo(account2.serverName));
        assertThat("Assert that account group in card view is as expected", tradingPage.getAccountGroup(account2.account), equalTo(account2.accountGroup));
        assertThat("Assert that account created time in card view is as expected", tradingPage.getAccountCreatedTime(account2.account), equalTo(account2.createTimeUtc));
        assertThat("Assert that account updated time in card view is as expected", tradingPage.getAccountUpdatedTime(account2.account), equalTo(account2.lastUpdated));
        // Verify tooltips
        tradingPage.verifyAccountIdPopup();
        tradingPage.verifyBalancePopup(account1.account);
        tradingPage.verifyBalanceUsdPopup(account1.account);
        tradingPage.verifyStatusPopup();
        tradingPage.verifyPlatformPopup();
        tradingPage.verifyAccountTypePopup();
        tradingPage.verifyCreatedTimePopup();
        tradingPage.verifyUpdatedTimePopup();
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("561")
    @DisplayName("Verify all data is present in trading info - accounts. Table view")
    public void verifyAccountsTableViewTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        tradingPage.openTradingTab();
        tradingPage.openAccountsTab();
        tradingPage.clickTableViewButton();
        // Verify table headers
        tradingPage.verifyAccountTableHeaders();
        // Verify 1st account row
        assertThat("Assert that account platform in table view is as expected", tradingPage.getAccountTablePlatform(account1.account), equalTo(account1.platform));
        assertThat("Assert that account type in table view is as expected", tradingPage.getAccountTableType(account1.account), equalTo(account1.accountType));
        assertThat("Assert that account status in table view is as expected", tradingPage.getAccountTableStatus(account1.account), equalTo(account1.accountStatus));
        assertThat("Assert that account created time in table view is as expected", tradingPage.getAccountTableCreated(account1.account), equalTo(account1.createTimeUtc.replace(" ", "")));
        assertThat("Assert that account updated time in table view is as expected", tradingPage.getAccountTableUpdated(account1.account), equalTo(account1.lastUpdated.replace(" ", "")));
        assertThat("Assert that account balance in table view is as expected", tradingPage.getAccountTableBalance(account1.account), equalTo(String.format("%s %s%s %s", account1.balance, account1.currency, account1.balanceUsd, "USD")));
        assertThat("Assert that account total pnl in table view is as expected", tradingPage.getAccountTableTotalPnl(account1.account), equalTo(String.format("%s %s", account1.pnl, account1.currency)));
        assertThat("Assert that account equity in table view is as expected", tradingPage.getAccountTableEquity(account1.account), equalTo(String.format("%s %s", account1.equity, account1.currency)));
        assertThat("Assert that account credit in table view is as expected", tradingPage.getAccountTableCredit(account1.account), equalTo(String.format("%s %s", account1.credit, account1.currency)));
        assertThat("Assert that account leverage in table view is as expected", tradingPage.getAccountTableLeverage(account1.account), equalTo(account1.leverage.toString()));
        assertThat("Assert that account margin free in table view is as expected", tradingPage.getAccountTableMarginFree(account1.account), equalTo(String.format("%s %s", account1.marginFree, account1.currency)));
        assertThat("Assert that account server in table view is as expected", tradingPage.getAccountTableServer(account1.account), equalTo(account1.serverName));
        assertThat("Assert that account group in table view is as expected", tradingPage.getAccountTableGroup(account1.account), equalTo(account1.accountGroup));
        // Verify 2nd account row
        assertThat("Assert that account platform in table view is as expected", tradingPage.getAccountTablePlatform(account2.account), equalTo(account2.platform));
        assertThat("Assert that account type in table view is as expected", tradingPage.getAccountTableType(account2.account), equalTo(account2.accountType));
        assertThat("Assert that account status in table view is as expected", tradingPage.getAccountTableStatus(account2.account), equalTo(account2.accountStatus));
        assertThat("Assert that account created time in table view is as expected", tradingPage.getAccountTableCreated(account2.account), equalTo(account2.createTimeUtc.replace(" ", "")));
        assertThat("Assert that account updated time in table view is as expected", tradingPage.getAccountTableUpdated(account2.account), equalTo(account2.lastUpdated.replace(" ", "")));
        assertThat("Assert that account balance in table view is as expected", tradingPage.getAccountTableBalance(account2.account), equalTo(String.format("%s %s", account2.balance, account2.currency)));
        assertThat("Assert that account total pnl in table view is as expected", tradingPage.getAccountTableTotalPnl(account2.account), equalTo(String.format("%s %s", account2.pnl, account2.currency)));
        assertThat("Assert that account equity in table view is as expected", tradingPage.getAccountTableEquity(account2.account), equalTo(String.format("%s %s", account2.equity, account2.currency)));
        assertThat("Assert that account credit in table view is as expected", tradingPage.getAccountTableCredit(account2.account), equalTo(String.format("%s %s", account2.credit, account2.currency)));
        assertThat("Assert that account leverage in table view is as expected", tradingPage.getAccountTableLeverage(account2.account), equalTo(account2.leverage.toString()));
        assertThat("Assert that account margin free in table view is as expected", tradingPage.getAccountTableMarginFree(account2.account), equalTo(String.format("%s %s", account2.marginFree, account2.currency)));
        assertThat("Assert that account server in table view is as expected", tradingPage.getAccountTableServer(account2.account), equalTo(account2.serverName));
        assertThat("Assert that account group in table view is as expected", tradingPage.getAccountTableGroup(account2.account), equalTo(account2.accountGroup));
    }
}
