package tests.vindex_backoffice_ui_tests.search;

import static business_objects.api.mitigation_service.MitigationServiceRequest.postRestriction;
import static business_objects.db.clickhouse.account_ib_relation.AccountIbRelationFactory.generateAccountIbRelationObjectByClient;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateAdditionalStaticCrmTbAccountActive;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateStaticCrmTbAccountActive;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateStaticUserByClient;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static business_objects.db.clickhouse.mt_balance_orders_table.MtBalanceOrdersObjectFactory.generateMtBalanceOrder;
import static business_objects.db.clickhouse.mt_mt4_trades.MtMt4TradesObjectFactory.generateMt4TradesObject;
import static business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedFactory.generateTradeByClient;
import static business_objects.db.clickhouse.mt_mt5_positions.MtMt5PositionsObjectFactory.generateMtMt5PositionsObject;
import static business_objects.db.clickhouse.s3___dim_client.S3DimClientFactory.generateS3DimClientObjectRandom;
import static business_objects.db.clickhouse.s3_fact_login_metrics.S3FactLoginMetricsFactory.generateS3FactLoginMetricsClient;
import static business_objects.kafka.alerts.RuleAlertFactory.generateRuleAlertByUcid;
import static helpers.api.AbuseRegistryHelper.addFraudsForClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.rules.MirrorFlagDataInserter.insertMirrorFlagData;
import static helpers.database.BoHelper.closeAlert;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.fail;
import static utils.Constants.*;
import static utils.Utils.*;

import business_objects.api.mitigation_service.PostRestrictionRequestBody;
import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalEntity;
import business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalEntityFactory;
import business_objects.db.clickhouse.mt_account.MtAccountObject;
import business_objects.db.clickhouse.mt_mt4_trades.MtMt4TradesObject;
import business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedObject;
import business_objects.db.clickhouse.mt_mt5_positions.MtMt5PositionsObject;
import business_objects.db.clickhouse.s3___dim_client.S3DimClientObject;
import business_objects.db.clickhouse.s3_fact_login_metrics.S3FactLoginMetricsObject;
import business_objects.kafka.alerts.RuleAlert;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.Page;
import helpers.data.ClientHelper;
import helpers.data.enums.*;
import helpers.database.ArHelper;
import io.javalin.http.HttpStatus;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import org.junit.jupiter.api.*;
import page_objects.backoffice_pages.search.SearchPage;
import tests.TestBaseWeb;

@Feature("Client Bulk Search by server and account")
class ClientBulkSearchTest extends TestBaseWeb {
    private static final int SERVER_ID = 129;
    private static final String SERVER_NAME = "SERVER129";

    private static final ClientHelper client1 = getRandomVantageClientAllFields();
    private static final ClientHelper client2 = getRandomVantageClientAllFields();
    private static final String CURRENCY_EUR = "EUR";
    private static final String DEFAULT_DEPOSITS = "1,027.12";

    private static CrmTbAccountObject account1;
    private static CrmTbAccountObject account2;
    private static CrmTbAccountObject account1Additional;

    private static MtAccountObject mtAccount1;
    private static MtAccountObject mtAccount2;
    private static MtAccountObject mtAccount1Additional;

    private static CrmTbUserObject crmTbUser1;
    private static CrmTbUserObject crmTbUser2;

    private static String searchQueryAccount1;

    @BeforeAll
    static void setup() {
        // Clean up
        deleteObjectFromDb(ACCOUNT_IB_RELATION_TABLE_NAME, "ucid ='" + client1.getUcid() + "'");
        deleteObjectFromDb(S3_FACT_IB_SALES_COMMISSIONS, "ucid ='" + client1.getUcid() + "'");
        deleteObjectFromDb(ACCOUNT_IB_RELATION_TABLE_NAME, "ucid ='" + client2.getUcid() + "'");
        deleteObjectFromDb(S3_FACT_IB_SALES_COMMISSIONS, "ucid ='" + client2.getUcid() + "'");

        client1.setServerId(SERVER_ID);
        client2.setServerId(SERVER_ID);

        crmTbUser1 = generateStaticUserByClient(client1);
        crmTbUser2 = generateStaticUserByClient(client2);

        // Insert users
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser1);
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser2);

        // Insert accounts for client1 - 2 accounts
        account1 = generateStaticCrmTbAccountActive(client1);
        account1.currency = CURRENCY_EUR;
        account1.serverName = SERVER_NAME;
        account1.serverIdSt = SERVER_ID;

        account1Additional = generateAdditionalStaticCrmTbAccountActive(client1);
        account1Additional.currency = CURRENCY_EUR;
        account1Additional.serverName = SERVER_NAME;
        account1Additional.serverIdSt = SERVER_ID;

        // Insert account for client2 - 1 account
        account2 = generateStaticCrmTbAccountActive(client2);
        account2.currency = CURRENCY_EUR;
        account2.serverName = SERVER_NAME;
        account2.serverIdSt = SERVER_ID;

        // Insert MT accounts
        mtAccount1 = generateMtAccountByCrmTbAccount(account1);
        mtAccount1Additional = generateMtAccountByCrmTbAccount(account1Additional);
        mtAccount2 = generateMtAccountByCrmTbAccount(account2);

        insertObjectsToDb(MT_ACCOUNT_TABLE_NAME, List.of(mtAccount1, mtAccount1Additional, mtAccount2));
        insertCrmAccountsToDb(account1, account1Additional, account2);

        searchQueryAccount1 = String.join(" ", account1.serverName, String.valueOf(account1.account));
    }

    @AfterAll
    static void teardown() throws Exception {
        deleteObjectFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", client1.getUcid()));
        deleteObjectFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", client2.getUcid()));
        deleteObjectFromDb(MT_ACCOUNT_TABLE_NAME, String.format("account = %d", mtAccount1.account));
        deleteObjectFromDb(MT_ACCOUNT_TABLE_NAME, String.format("account = %d", mtAccount1Additional.account));
        deleteObjectFromDb(MT_ACCOUNT_TABLE_NAME, String.format("account = %d", mtAccount2.account));
        ArHelper.deleteUserFromAbuseRegistry(client1.getUcid());
        closeAlert(client1.getUcid());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1659")
    @DisplayName("Search by single server + account pair")
    void searchBySingleAccountTest() {
        searchPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        searchPage.navigateToSearchPage();
        searchPage.waitForPageToLoad();

        // Search by server and account
        searchPage.searchByBulk(searchQueryAccount1);
        searchPage.waitForPageToLoad();

        // Verify results
        List<String> searchResults = searchPage.getSearchResults();
        assertThat("Verify search results contain the client", searchResults, hasSize(1));
        assertThat(
                "Verify client ID is present",
                searchResults.getFirst(),
                containsString(String.valueOf(crmTbUser1.userId)));
        assertThat("Verify account is present", searchResults.getFirst(), containsString(account1.account.toString()));
        assertThat("Verify server name is present", searchResults.getFirst(), containsString(account1.serverName));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1660")
    @DisplayName("Search by multiple server + account pairs (same client)")
    void searchByMultipleAccountsSameClientTest() {
        searchPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        searchPage.navigateToSearchPage();
        searchPage.waitForPageToLoad();

        // Search by multiple accounts of the same client
        String searchQuery = String.join(
                " ",
                account1.serverName,
                String.valueOf(account1.account),
                account1Additional.serverName,
                String.valueOf(account1Additional.account));

        searchPage.searchByBulk(searchQuery);
        searchPage.waitForPageToLoad();

        // Verify results
        List<String> searchResults = searchPage.getSearchResults();
        assertThat("Verify search results contain both accounts", searchResults, hasSize(2));

        // Verify first account
        assertThat(
                "Verify first account is present",
                searchResults.stream().anyMatch(r -> r.contains(account1.account.toString())),
                is(true));

        // Verify second account
        assertThat(
                "Verify second account is present",
                searchResults.stream().anyMatch(r -> r.contains(account1Additional.account.toString())),
                is(true));

        // Both should belong to the same user
        searchResults.forEach(result -> assertThat(
                "Verify all results belong to same client", result, containsString(String.valueOf(crmTbUser1.userId))));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1661")
    @DisplayName("Search by multiple server + account pairs (different clients)")
    void searchByMultipleAccountsDifferentClientsTest() {
        searchPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        searchPage.navigateToSearchPage();
        searchPage.waitForPageToLoad();

        // Search by accounts of different clients
        String searchQuery = String.join(
                " ",
                account1.serverName,
                String.valueOf(account1.account),
                account2.serverName,
                String.valueOf(account2.account));

        searchPage.searchByBulk(searchQuery);
        searchPage.waitForPageToLoad();

        // Verify results
        List<String> searchResults = searchPage.getSearchResults();
        assertThat("Verify search results contain both accounts", searchResults, hasSize(2));

        // Verify client1 account is present
        assertThat(
                "Verify client1 account is present",
                searchResults.stream()
                        .anyMatch(r -> r.contains(account1.account.toString())
                                && r.contains(String.valueOf(crmTbUser1.userId))),
                is(true));

        // Verify client2 account is present
        assertThat(
                "Verify client2 account is present",
                searchResults.stream()
                        .anyMatch(r -> r.contains(account2.account.toString())
                                && r.contains(String.valueOf(crmTbUser2.userId))),
                is(true));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1662")
    @DisplayName("Search by non-existent server + account pair")
    void searchByNonExistentAccountTest() {
        searchPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        searchPage.navigateToSearchPage();
        searchPage.waitForPageToLoad();

        // Search by non-existent account
        var searchQuery = "NonExistentServer 999999999";
        searchPage.searchByBulk(searchQuery);
        searchPage.waitForPageToLoad();

        // Verify no results
        List<String> searchResults = searchPage.getSearchResults();
        assertThat("Verify no search results returned", searchResults, empty());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1663")
    @DisplayName("Search with mix of valid and invalid server + account pairs")
    void searchByMixedValidInvalidAccountsTest() {
        searchPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        searchPage.navigateToSearchPage();
        searchPage.waitForPageToLoad();

        // Search with valid and invalid accounts mixed
        String searchQuery = String.join(
                " ",
                account1.serverName,
                String.valueOf(account1.account),
                "InvalidServer 999999999",
                account2.serverName,
                String.valueOf(account2.account));

        searchPage.searchByBulk(searchQuery);
        searchPage.waitForPageToLoad();

        // Verify results - only valid accounts should be returned
        List<String> searchResults = searchPage.getSearchResults();
        assertThat("Verify only valid accounts are returned", searchResults, hasSize(2));

        // Verify valid accounts are present
        assertThat(
                "Verify client1 account is present",
                searchResults.stream().anyMatch(r -> r.contains(account1.account.toString())),
                is(true));
        assertThat(
                "Verify client2 account is present",
                searchResults.stream().anyMatch(r -> r.contains(account2.account.toString())),
                is(true));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1664")
    @DisplayName("Verify search results display basic columns")
    void verifySearchResultsColumnsTest() {
        searchPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        searchPage.navigateToSearchPage();
        searchPage.waitForPageToLoad();

        searchPage.searchByBulk(searchQueryAccount1);
        searchPage.waitForPageToLoad();

        // Verify data in first result row
        String firstResult = searchPage.getFirstSearchResult();
        assertThat(
                "Verify user full name is displayed",
                firstResult,
                containsString(String.join(" ", crmTbUser1.firstName, crmTbUser1.lastName)));
        assertThat("Verify account number is displayed", firstResult, containsString(account1.account.toString()));

        assertThat("Verify server name is displayed", firstResult, containsString(account1.serverName));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1665")
    @DisplayName("Click on search result to navigate to client investigation")
    void clickSearchResultNavigateToInvestigationTest() {
        searchPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        searchPage.navigateToSearchPage();
        searchPage.waitForPageToLoad();

        searchPage.searchByBulk(searchQueryAccount1);
        searchPage.waitForPageToLoad();

        // Click on the first result
        try (Page clientPage = searchPage.clickFirstSearchResultAndSwitchTab()) {
            var url = clientPage.url();
            assertThat("Verify URL contains client UCID", url, containsString(crmTbUser1.ucid));
        }
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1666")
    @DisplayName("Search with different separators between server and account")
    void searchWithDifferentSeparatorsTest() {
        searchPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        searchPage.navigateToSearchPage();
        searchPage.waitForPageToLoad();

        // Test with different separators: space, comma, tab
        String[] searchQueries = {
            searchQueryAccount1, // space
            String.join(",", account1.serverName, String.valueOf(account1.account)), // comma
            String.join("\t", account1.serverName, String.valueOf(account1.account))
        };

        for (String query : searchQueries) {
            searchPage.clearSearch();
            searchPage.searchByBulk(query);
            searchPage.waitForPageToLoad();

            List<String> searchResults = searchPage.getSearchResults();
            assertThat(
                    "Verify search works with different separator: " + query, searchResults, hasSize(greaterThan(0)));
            assertThat(
                    "Verify correct account found",
                    searchResults.getFirst(),
                    containsString(account1.account.toString()));
        }
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1667")
    @DisplayName("Verify all columns in search results table")
    void verifyAllTableColumnsTest() {
        fail("need to fix data because querry \n" + "WITH [({serverId}, {account})] AS server_acc_list\n" + "SELECT *\n"
                + "FROM consolidated.bo___account_details_bulk_pv (server_acc_list = server_acc_list);"
                + "\n returns NaN and Infinite");
        // Clean related data before test
        deleteObjectFromDb(S3_FACT_LOGIN_METRICS_TABLE_NAME, "ucid ='" + client1.getUcid() + "'");
        deleteObjectFromDb(MT5_DEALS_COERCED_TABLE_NAME, "ucid ='" + client1.getUcid() + "'");
        deleteObjectFromDb(MT4_TRADES_COERCED_TABLE_NAME, "ucid ='" + client1.getUcid() + "'");
        deleteObjectFromDb(MT5_POSITIONS_TABLE_NAME, "ucid ='" + client1.getUcid() + "'");
        deleteObjectFromDb(MT4_TRADES_TABLE_NAME, "ucid ='" + client1.getUcid() + "'");
        deleteObjectFromDb(CLICKHOUSE_CRM_TB_WITHDRAWAL, "ucid ='" + client1.getUcid() + "'");

        insertMirrorFlagData(client1);

        var data1 = generateMtBalanceOrder(client1, 1d, 2d, "2024-12-10 17:59:14");
        var data2 = generateMtBalanceOrder(client1, 3d, 4d, "2024-12-10 17:59:15");
        insertObjectsToDb(MT_BALANCE_ORDERS_TABLE_NAME, List.of(data1, data2));

        // set IB relation
        var ibRelation = generateAccountIbRelationObjectByClient(client1);
        insertObjectToDb(ACCOUNT_IB_RELATION_TABLE_NAME, ibRelation);

        S3DimClientObject manager1 = generateS3DimClientObjectRandom();
        manager1.setBrand(client1.getBrand());
        manager1.setUserId(ibRelation.getSalesId().longValue());
        insertObjectToDb(S3_DIM_CLIENT, manager1);

        // Format: US format with comma as thousands separator, dot as decimal, up to 2 decimals (not always 2)
        var decimalFormat = new DecimalFormat("#,##0.##", new DecimalFormatSymbols(Locale.US));
        decimalFormat.setMaximumFractionDigits(2);

        // Prepare Trading PNL data (same logic as SummaryPanelTest)
        Allure.step("Generate historical data not including current date");
        S3FactLoginMetricsObject historyMetrics1 = generateS3FactLoginMetricsClient(client1);
        historyMetrics1.setCurrency("EUR");
        historyMetrics1.setDate(getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE, 0, 0, 1, 0, 0));
        historyMetrics1.setDailyNetClosedPnl(getRandomRoundedDouble(100, 1000));

        S3FactLoginMetricsObject historyMetrics2 = generateS3FactLoginMetricsClient(client1);
        historyMetrics2.setCurrency("EUR");
        historyMetrics2.setDate(getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE, 0, 0, 2, 0, 0));
        historyMetrics2.setDailyNetClosedPnl(getRandomRoundedDouble(100, 1000));

        insertObjectsToDb(S3_FACT_LOGIN_METRICS_TABLE_NAME, List.of(historyMetrics1, historyMetrics2));

        // Calculate daily_net_closed_pnl_d1_usd (historical PNL)
        double dailyNetClosedPnlD1 = historyMetrics1.getDailyNetClosedPnl() + historyMetrics2.getDailyNetClosedPnl();

        Allure.step("Generate MT5 closed deals (today_pnl_usd)");
        Mt5DealsCoercedObject deal1 = generateTradeByClient(client1);
        deal1.setTime(getCurrentTimestampDbFormat());
        deal1.setProfitUsd(getRandomRoundedDouble(100, 500));
        deal1.setCommissionUsd(getRandomRoundedDouble(10, 50));
        deal1.setStorageUsd(getRandomRoundedDouble(5, 20));

        Mt5DealsCoercedObject deal2 = generateTradeByClient(client1);
        deal2.setTime(getCurrentTimestampDbFormat());
        deal2.setProfitUsd(getRandomRoundedDouble(100, 500));
        deal2.setCommissionUsd(getRandomRoundedDouble(10, 50));
        deal2.setStorageUsd(getRandomRoundedDouble(5, 20));

        var expectedLots = deal1.getVolumeLots() + deal2.getVolumeLots();

        insertObjectsToDb(MT5_DEALS_COERCED_TABLE_NAME, List.of(deal1, deal2));

        // Calculate today_pnl_usd
        double todayPnlUsd = (deal1.getProfitUsd() + deal1.getCommissionUsd() + deal1.getStorageUsd())
                + (deal2.getProfitUsd() + deal2.getCommissionUsd() + deal2.getStorageUsd());

        // realized_pnl_usd = daily_net_closed_pnl_d1_usd + today_pnl_usd
        var realizedPnlUsd = dailyNetClosedPnlD1 + todayPnlUsd;

        Allure.step("Generate MT4 open trades (floating_pnl_mt4)");
        MtMt4TradesObject mt4Trade1 = generateMt4TradesObject(client1);
        mt4Trade1.setCloseTime("1970-01-01 00:00:00");
        mt4Trade1.setCmd(0);
        mt4Trade1.setProfitUsd(getRandomRoundedDouble(50, 200));
        mt4Trade1.setCommissionUsd(getRandomRoundedDouble(5, 20));
        mt4Trade1.setStorageUsd(getRandomRoundedDouble(2, 10));

        MtMt4TradesObject mt4Trade2 = generateMt4TradesObject(client1);
        mt4Trade2.setCloseTime("1970-01-01 00:00:00");
        mt4Trade2.setCmd(1);
        mt4Trade2.setProfitUsd(getRandomRoundedDouble(50, 200));
        mt4Trade2.setCommissionUsd(getRandomRoundedDouble(5, 20));
        mt4Trade2.setStorageUsd(getRandomRoundedDouble(2, 10));

        insertObjectsToDb(MT4_TRADES_TABLE_NAME, List.of(mt4Trade1, mt4Trade2));

        // Calculate floating_pnl_mt4_usd
        double floatingPnlMt4 = (mt4Trade1.getProfitUsd() + mt4Trade1.getCommissionUsd() + mt4Trade1.getStorageUsd())
                + (mt4Trade2.getProfitUsd() + mt4Trade2.getCommissionUsd() + mt4Trade2.getStorageUsd());

        Allure.step("Generate MT5 open positions (floating_pnl_mt5)");
        MtMt5PositionsObject position1 = generateMtMt5PositionsObject(client1);
        position1.setAccount(client1.getTradingAccount());
        position1.setServerId(client1.getServerId());
        position1.setIsDeleted(0);
        position1.setAction(0);
        position1.setProfitUsd(getRandomRoundedDouble(50, 200));
        position1.setStorageUsd(getRandomRoundedDouble(2, 10));

        MtMt5PositionsObject position2 = generateMtMt5PositionsObject(client1);
        position2.setAccount(client1.getTradingAccount());
        position2.setServerId(client1.getServerId());
        position2.setIsDeleted(0);
        position2.setAction(1);
        position2.setProfitUsd(getRandomRoundedDouble(50, 200));
        position2.setStorageUsd(getRandomRoundedDouble(2, 10));

        insertObjectsToDb(MT5_POSITIONS_TABLE_NAME, List.of(position1, position2));

        // Calculate floating_pnl_mt5_usd
        double floatingPnlMt5 = (position1.getProfitUsd() + position1.getStorageUsd())
                + (position2.getProfitUsd() + position2.getStorageUsd());

        // trading_client_pnl_usd = realized_pnl_usd + floating_pnl_mt4_usd + floating_pnl_mt5_usd
        var expectedTradingPnl = realizedPnlUsd + floatingPnlMt4 + floatingPnlMt5;

        // Prepare Withdrawals data (same logic as SummaryPanelTest)
        CrmTbWithdrawalEntity withdrawal1 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client1);
        withdrawal1.setAmountUsd(BigDecimal.valueOf(getRandomRoundedDouble(100.00, 1000.00)));
        withdrawal1.setReversedAmountUsd(
                withdrawal1.getAmountUsd().divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP));
        withdrawal1.setStatusId(3);

        CrmTbWithdrawalEntity withdrawal2 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client1);
        withdrawal2.setAmountUsd(BigDecimal.valueOf(getRandomRoundedDouble(100.00, 1000.00)));
        withdrawal1.setReversedAmountUsd(
                withdrawal1.getAmountUsd().divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP));
        withdrawal2.setStatusId(5);

        CrmTbWithdrawalEntity withdrawal3 = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client1);
        withdrawal3.setAmountUsd(BigDecimal.valueOf(getRandomRoundedDouble(100.00, 1000.00)));
        withdrawal1.setReversedAmountUsd(
                withdrawal1.getAmountUsd().divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP));
        withdrawal3.setStatusId(7);
        insertObjectsToDb(CLICKHOUSE_CRM_TB_WITHDRAWAL, List.of(withdrawal1, withdrawal2, withdrawal3));

        searchPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        searchPage.navigateToSearchPage();
        searchPage.waitForPageToLoad();

        // Search by account
        searchPage.searchByBulk(searchQueryAccount1);
        searchPage.waitForPageToLoad();

        // Get structured data for the row
        SearchPage.SearchResultRow row = searchPage.getSearchResultRow(account1.serverName, account1.account);

        assertThat("Verify row data is not null", row, notNullValue());

        // Verify CLIENT column
        assertThat("Verify client ID contains userId", row.client, containsString(String.valueOf(crmTbUser1.userId)));
        String clientId = searchPage.getClientIdFromRow(row.rowDataQa);
        assertThat("Verify client ID", clientId, is(String.valueOf(crmTbUser1.userId)));

        // Verify client link
        String clientLink = searchPage.getClientLinkHref(row.rowDataQa);
        assertThat("Verify client link contains investigation path", clientLink, containsString("/investigation/"));
        assertThat("Verify client link contains ucid", clientLink, containsString(crmTbUser1.ucid));

        // Verify ACCOUNT column
        assertThat(
                "Verify account info contains account number",
                row.accountInfo,
                containsString(account1.account.toString()));
        assertThat("Verify account info contains server name", row.accountInfo, containsString(account1.serverName));
        assertThat("Verify account status", row.accountInfo, containsString("Active"));

        // Verify BEHAVIOR column
        assertThat("Verify behavior column when client is normal", row.behavior, containsString("Normal"));

        // Verify COUNTRY column
        assertThat(
                "Verify country",
                row.country.toUpperCase(),
                is(client1.getCountry().toUpperCase()));

        // Verify SALES GROUP column
        assertThat("Verify sales group", row.salesGroup, is(crmTbUser1.brandGroup));

        // Verify TRADING PNL column
        var formattedExpectedTradingPnl = decimalFormat.format(expectedTradingPnl);
        assertThat("Verify trading PNL", row.tradingPnl, containsString(formattedExpectedTradingPnl));

        // Verify GROSS PNL column
        assertThat("Verify gross PNL is not null", row.grossPnl, notNullValue());

        // Verify LOTS column
        assertThat("Verify lots is not null", row.lots, containsString(String.valueOf(expectedLots)));

        // Verify BALANCE column
        assertThat(
                "Verify balance contains 101.5", row.balance, containsString(decimalFormat.format(mtAccount1.balance)));

        // Verify EQUITY column
        assertThat("Verify equity", row.equity, containsString(decimalFormat.format(mtAccount1.equity)));

        // Verify CREDITS column
        assertThat("Verify credits", row.credits, containsString(decimalFormat.format(mtAccount1.credit)));

        // Verify DEPOSITS column (just a constant from insertMirrorFlagData)
        assertThat("Verify deposits is not null", row.deposits, containsString(DEFAULT_DEPOSITS));

        // Verify WITHDRAWALS column (calculated same as in Summary Panel)
        // TODO: update bo___account_details_bulk_pv to match new logic of withdrawal calculation
        // double expectedWithdrawals = calculateWithdrawalsValue(withdrawal1, withdrawal2, withdrawal3);
        // String formattedExpectedWithdrawals = decimalFormat.format(expectedWithdrawals);
        // assertThat("Verify withdrawals", row.withdrawals, containsString(formattedExpectedWithdrawals));

        // Verify NET DEPOSITS column (TODO: net deposits depends on withdrawals, update when withdrawals are ready)
        assertThat("Verify net deposits is not null", row.netDeposits, notNullValue());

        // Verify CPA/IB column
        assertThat("Verify IB/CPA column exists", row.ibCpa, notNullValue());
        if (!row.ibCpa.isEmpty()) {
            assertThat("Verify IB/CPA contains CPA ID", row.ibCpa, containsString(String.valueOf(crmTbUser1.cpaId)));
            assertThat(
                    "Verify IB/CPA contains IB ID",
                    row.ibCpa,
                    containsString(ibRelation.getDirectIbRebateAccount().toString()));
        }

        // Verify REGISTERED column
        assertThat("Verify registered date", row.registered, is(crmTbUser1.registrationDate));

        // Verify LAST LOGIN column
        assertThat(
                "Verify last login contains date",
                row.lastLogin,
                // parse db format to expected format
                containsString(account1.lastLogin.substring(0, 16).replace(" ", "")));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1668")
    @DisplayName("Verify restrictions list in behavior tooltip")
    void verifyRestrictionsInBehaviorTooltipTest() throws Exception {
        Allure.step("Add restrictions to client account");
        // Add several visible restrictions to the account
        Restriction restriction = Restriction.WITHDRAWALS;

        String updatedBySystem = "system %s".formatted(getCurrentTimestampSeconds());
        String updatedByUser = "user %s".formatted(getCurrentTimestampSeconds());
        PostRestrictionRequestBody postRestrictionRequestBody = new PostRestrictionRequestBody(
                client1.getUcid(),
                restriction.getCode(),
                restriction.getType(),
                account1.account,
                account1.serverIdSt,
                "Search Test",
                new PostRestrictionRequestBody.UpdatedBy(updatedBySystem, updatedByUser));

        var response = postRestriction(postRestrictionRequestBody);
        assertThat(response.code(), is(HttpStatus.OK.getCode()));

        searchPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        searchPage.navigateToSearchPage();
        searchPage.waitForPageToLoad();

        // Search by account
        searchPage.searchByBulk(searchQueryAccount1);
        searchPage.waitForPageToLoad();

        // Get structured data for the row
        SearchPage.SearchResultRow row = searchPage.getSearchResultRow(account1.serverName, account1.account);
        assertThat("Verify row data is not null", row, notNullValue());

        // Get restrictions from behavior tooltip
        List<String> displayedRestrictions = searchPage.getRestrictionsFromBehaviorTooltip(row.rowDataQa);

        assertThat(
                "Verify restriction is displayed in tooltip: %s".formatted(restriction.getName()),
                displayedRestrictions,
                hasItem(restriction.getName()));

        // Verify the count matches
        assertThat("Verify number of restrictions displayed", displayedRestrictions.size(), is(equalTo(1)));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1669")
    @DisplayName("Verify behavior changes from Normal to Suspicious to Fraud with confirmed fraud types")
    void verifyBehaviorChangeWithAlertAndFraudTest() throws Exception {
        Allure.step("Clean user data before test");
        ArHelper.deleteUserFromAbuseRegistry(client1.getUcid());
        closeAlert(client1.getUcid());

        searchPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        searchPage.navigateToSearchPage();
        searchPage.waitForPageToLoad();

        // Search by account
        searchPage.searchByBulk(searchQueryAccount1);
        searchPage.waitForPageToLoad();

        // Step 1: Verify initial behavior is "Normal"
        Allure.step("Step 1: Verify behavior is 'Normal' without alerts");
        SearchPage.SearchResultRow row = searchPage.getSearchResultRow(account1.serverName, account1.account);
        assertThat("Verify row data is not null", row, notNullValue());
        assertThat("Verify behavior is Normal", row.behavior, containsString("Normal"));

        // Step 2: Create an alert for the client
        Allure.step("Step 2: Create alert and verify behavior changes to 'Suspicious'");
        RuleAlert alert = generateRuleAlertByUcid(client1.getUcid());
        kafka.produceMessage(alert.alertId, new ObjectMapper().writeValueAsString(alert), KAFKA_TOPIC_ALERTS);
        Thread.sleep(3000); // Wait for alert processing

        // Refresh search and check behavior changed to Suspicious
        searchPage.navigateToSearchPage();
        searchPage.waitForPageToLoad();
        searchPage.searchByBulk(searchQueryAccount1);
        searchPage.waitForPageToLoad();
        row = searchPage.getSearchResultRow(account1.serverName, account1.account);
        assertThat("Verify behavior changed to Suspicious", row.behavior, containsString("Suspicious"));

        // Step 3: Add confirmed fraud types
        Allure.step("Step 3: Add confirmed fraud types and verify they are displayed");
        List<FraudType> confirmedFrauds = List.of(FraudType.HEDGING, FraudType.CPA_ABUSE, FraudType.BONUS_ABUSE);

        for (FraudType fraud : confirmedFrauds) {
            addFraudsForClient(client1, List.of(fraud), FraudTypeStatus.CONFIRMED);
        }
        Thread.sleep(2000); // Wait for fraud processing

        // Refresh search and verify confirmed frauds in tooltip
        searchPage.navigateToSearchPage();
        searchPage.waitForPageToLoad();
        searchPage.searchByBulk(searchQueryAccount1);
        searchPage.waitForPageToLoad();
        row = searchPage.getSearchResultRow(account1.serverName, account1.account);

        // Verify all confirmed fraud types are displayed in behavior text
        for (FraudType fraud : confirmedFrauds) {
            assertThat(
                    "Verify fraud type is displayed in behavior: %s".formatted(fraud.getName()),
                    row.behavior,
                    containsString(fraud.getName()));
        }
    }
}
