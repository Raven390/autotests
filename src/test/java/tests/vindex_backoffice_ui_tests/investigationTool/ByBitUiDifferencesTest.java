package tests.vindex_backoffice_ui_tests.investigationTool;

import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.mt_balance_orders_table.MtBalanceOrdersObject;
import helpers.data.ClientHelper;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;
import tests.vindex_backoffice_ui_tests.abuseRegistry.ManageSingleDeductionTest;

import java.util.List;
import java.util.logging.Logger;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountDataBybit;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.mt_balance_orders_table.MtBalanceOrdersObjectFactory.generateMtBalanceOrders;
import static helpers.data.ClientFactory.getRandomBybitClient;
import static helpers.database.ArHelper.deleteUserAR;
import static helpers.database.DbHelper.*;
import static helpers.database.DbHelper.deleteEntryFromDb;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.Constants.*;
import static utils.Utils.getCurrentTimestampDbFormat;
import static utils.Utils.getRandomRoundedDouble;

class ByBitUiDifferencesTest extends TestBaseWeb {
    static ClientHelper client = getRandomBybitClient();
    static CrmTbUserObject user = generateUserByClient(client);
    static CrmTbAccountObject account = generateCrmTbAccountDataBybit(client);
    static MtBalanceOrdersObject deposit1 = generateMtBalanceOrders(client, getRandomRoundedDouble(0.01, 99_999.99), getRandomRoundedDouble(0.01, 99_999.99), getCurrentTimestampDbFormat());
    static MtBalanceOrdersObject withdrawal1 = generateMtBalanceOrders(client, getRandomRoundedDouble(-99_999.99, -0.01), getRandomRoundedDouble(-99_999.99, -0.01), getCurrentTimestampDbFormat());
    static MtBalanceOrdersObject deposit2 = generateMtBalanceOrders(client, getRandomRoundedDouble(0.01, 99_999.99), getRandomRoundedDouble(0.01, 99_999.99), getCurrentTimestampDbFormat());
    static MtBalanceOrdersObject withdrawal2 = generateMtBalanceOrders(client, getRandomRoundedDouble(-99_999.99, -0.01), getRandomRoundedDouble(-99_999.99, -0.01), getCurrentTimestampDbFormat());

    @BeforeAll
    static void setup() {
        client.setFirstName("Bybittino");
        client.setFirstName("Bytman");
        user.firstName = client.getFirstName();
        user.lastName = client.getLastName();
        insertObjectToDb(CRM_USER_TABLE_NAME, user);
        insertObjectToDb(CRM_TB_ACCOUNT_TABLE_NAME, account);
        insertObjectsToDb(MT_BALANCE_ORDERS_TABLE_NAME, List.of(deposit1, withdrawal1, deposit2, withdrawal2));
    }

    @AfterAll
    static void teardown() throws Exception {
        deleteUserAR(client.getUcid());
        deleteEntryFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        deleteEntryFromDb(MT_BALANCE_ORDERS_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        try {
            deleteEntryFromDb(CRM_TB_ACCOUNT_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        } catch (Exception e) {
            Logger.getLogger(ManageSingleDeductionTest.class.getName()).info("Account deletion failed");
        }
    }

    @Test
    @AllureId("1475")
    @DisplayName("Bybit client have reduced set of tabs")
    void bybitNotSeeAllTabs() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(client.getUcid());
        alertsPage.isAlertTabVisible();
        generalTab.isGeneralTabHidden();
        sessionsTab.isSessionTabHidden();
        paymentsPage.isPaymentsTabHidden();
        tradingPage.isTradingTabVisible();
        connectionPage.isConnectionsTabHidden();
        auditTrailPage.isAuditTabVisible();
        restrictionPage.isRestrictionTabVisible();
    }

    @Test
    @AllureId("1476")
    @DisplayName("user cant open tabs that bibyt client dont have")
    void bybitNotNavigateAllTabs() {
        String baseTab = "/trading/summary";
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(client.getUcid());
        alertsPage.isAlertTabVisible();
        alertsPage.navigate(client.getUcid());
        investigationPage.checkPageUrl("alerts");
        generalTab.navigate(client.getUcid());
        investigationPage.checkPageUrl(baseTab);
        sessionsTab.navigate(client.getUcid());
        investigationPage.checkPageUrl(baseTab);
        paymentsPage.navigate(client.getUcid());
        investigationPage.checkPageUrl(baseTab);
        tradingPage.navigate(client.getUcid());
        investigationPage.checkPageUrl("trading");
        connectionPage.navigate(client.getUcid());
        investigationPage.checkPageUrl(baseTab);
        auditTrailPage.navigate(client.getUcid());
        investigationPage.checkPageUrl("audit");
        restrictionPage.navigate(client.getUcid());
        investigationPage.checkPageUrl("restrictions");
    }

    @Test
    @AllureId("1482")
    @DisplayName("data in summary tab for withdrawals and deposits for Bybit comes from mt___balance_orders")
    void bybitDepositsWithdrawalsSummary() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(client.getUcid());
        generalTab.checkSummaryPanelValue("Deposits", deposit1.amountUsd + deposit2.amountUsd);
        generalTab.checkSummaryPanelValue("Withdrawals", withdrawal1.amountUsd + withdrawal2.amountUsd);
    }

    @Test
    @AllureId("1483")
    @DisplayName("bybit client have only withdrawal restriction")
    void bybitNoRebatesInTrading() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        restrictionPage.navigate(client.getUcid());
        List<String> restrictions = restrictionPage.getDisplayedRestrictionsList();
        Allure.step("check that only withdrawal restriction is displayed");
        assertEquals(1, restrictions.size());
        assertEquals("Withdrawal", restrictions.getFirst());
    }
}
