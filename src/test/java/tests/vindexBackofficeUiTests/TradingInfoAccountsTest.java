package tests.vindexBackofficeUiTests;

import businessObjects.db.clickhouse.crmTbAccount.CrmTbAccountObject;
import businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObject;
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
import static businessObjects.kafka.alerts.RuleAlertFactory.generateRuleAlertByUcid;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.BoHelper.closeAlert;
import static helpers.database.DbHelper.deleteEntryFromDb;
import static helpers.database.DbHelper.insertObjectToDb;
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

    @BeforeAll
    public static void setup() throws ReflectiveOperationException, SQLException, JsonProcessingException {
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        account1 = generateCrmTbAccountDataForUi(client);
        account1.currency = "EUR";
        insertObjectToDb(CRM_ACCOUNT_TABLE_NAME, account1);
        account2 = generateAdditionalCrmTbAccountDataForUi(client);
        account2.serverIdSt = 22;
        account2.accountStatus = "Inactive";
        insertObjectToDb(CRM_ACCOUNT_TABLE_NAME, account2);
        RuleAlert alert = generateRuleAlertByUcid(crmTbUser.ucid);
        kafka.produceMessage(alert.alertId, objectMapper.writeValueAsString(alert), KAFKA_TOPIC_ALERTS);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("560")
    @DisplayName("Verify all data is present in trading info - accounts. Card view")
    public void verifyAccountsCardViewTest() {
        investigationPage.navigate();
        keycloackPage.loginAsCoreUser();
        investigationPage.waitForPageToLoad();
        investigationPage.clickSuspiciousClientsFiltration();
        investigationPage.selectBrandFilterByText(crmTbUser.brand);
        investigationPage.clickApplyFiltrationButton();
        investigationPage.filterUnassigned();
        investigationPage.waitForPageToLoad();
        investigationPage.clickClientCardByClientId(String.valueOf(crmTbUser.userId));
        alertsPage.waitForPageToLoad();
        tradingPage.openTradingTab();
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
        investigationPage.navigate();
        keycloackPage.loginAsCoreUser();
        investigationPage.waitForPageToLoad();
        investigationPage.clickSuspiciousClientsFiltration();
        investigationPage.selectBrandFilterByText(crmTbUser.brand);
        investigationPage.clickApplyFiltrationButton();
        investigationPage.filterUnassigned();
        investigationPage.waitForPageToLoad();
        investigationPage.clickClientCardByClientId(String.valueOf(crmTbUser.userId));
        alertsPage.waitForPageToLoad();
        tradingPage.openTradingTab();
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

    @AfterAll
    public static void teardown() throws SQLException {
        deleteEntryFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", crmTbUser.ucid));
        closeAlert(crmTbUser.ucid);
    }
}
