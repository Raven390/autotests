package tests.vindex_backoffice_ui_tests.investigationTool;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountDataForUi;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.data_science_test.connection_table.ConnectionTableEntryFactory.getConnectionTableEntryForUiFiltration1;
import static business_objects.db.clickhouse.data_science_test.connection_table.ConnectionTableEntryFactory.getConnectionTableEntryForUiFiltration2;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObjectFactory.generateMt4TradesCoercedAccountProfitComment;
import static business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObjectFactory.generateMt4TradesCoercedAccountProfitCommentBuy;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.Currency.USD;
import static helpers.data.enums.FraudType.CPA_ABUSE;
import static helpers.database.AuHelper.cleanClientAudit;
import static helpers.database.BoHelper.closeAlert;
import static helpers.database.BoHelper.deleteUserBO;
import static helpers.database.DbHelper.*;
import static helpers.kafka.alerts.CreateSimpleAlert.sendSimpleAlert;
import static utils.Constants.*;
import static utils.Utils.*;

import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.data_science_test.connection_table.ConnectionTableEntry;
import business_objects.db.clickhouse.mt_account.MtAccountObject;
import business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObject;
import helpers.data.ClientHelper;
import helpers.database.ArHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import java.util.List;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

@Tag(TEAM_BACKOFFICE)
@Tag(LAYER_WEB)
@Feature("BMS-1872 [Q3] Role based model")
class ViewerVisibilityTest extends TestBaseWeb {
    private static final ClientHelper client = getRandomVantageClientAllFields();
    private static final ClientHelper connectedClient1 = getRandomVantageClientAllFields();
    private static final ClientHelper connectedClient2 = getRandomVantageClientAllFields();
    private static final CrmTbAccountObject account1 = generateCrmTbAccountDataForUi(connectedClient1);
    private static MtAccountObject mtAccount1;
    private static MtMt4TradesCoercedObject trade1;
    private static MtMt4TradesCoercedObject trade2;
    private static MtMt4TradesCoercedObject tradeWithdrawal;

    @BeforeAll
    static void setup() throws Exception {
        CrmTbUserObject crmTbUser = generateUserByClient(client);
        CrmTbUserObject connectedCrmTbUser1 = generateUserByClient(connectedClient1);
        CrmTbUserObject connectedCrmTbUser2 = generateUserByClient(connectedClient2);
        insertObjectsToDb(CRM_USER_TABLE_NAME, List.of(crmTbUser, connectedCrmTbUser1, connectedCrmTbUser2));
        ConnectionTableEntry connectionTableEntry1 = getConnectionTableEntryForUiFiltration1(client, connectedClient1);
        ConnectionTableEntry connectionTableEntry2 = getConnectionTableEntryForUiFiltration2(client, connectedClient2);
        insertObjectsToDb(CONNECTIONS_TABLE_NAME, List.of(connectionTableEntry1, connectionTableEntry2));

        insertObjectsToDb(CRM_TB_ACCOUNT_TABLE_NAME, List.of(account1));
        waitForConnectionSearchToUpdate(client);
        CrmTbAccountObject account1 = generateCrmTbAccountDataForUi(client);
        account1.currency = USD.getIsoCode();
        mtAccount1 = generateMtAccountByCrmTbAccount(account1);

        String comment = "comment";
        trade1 = generateMt4TradesCoercedAccountProfitCommentBuy(account1, 200.12 + 10_000d, comment);
        trade2 = generateMt4TradesCoercedAccountProfitCommentBuy(account1, 300d, comment);
        tradeWithdrawal = generateMt4TradesCoercedAccountProfitComment(account1, -10_000d, "withdraw");

        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        insertCrmAccountsToDb(account1);
        insertObjectToDb(MT_ACCOUNT_TABLE_NAME, mtAccount1);
        insertObjectsToDb(MT4_TRADES_COERCED_TABLE_NAME, List.of(trade1, trade2, tradeWithdrawal));
        sendSimpleAlert(client.getUcid(), CPA_ABUSE.getCode());
    }

    @Test
    @AllureId("1738")
    @DisplayName("Trading user have reduced set of tabs, subtabs and buttons")
    void tradingUser() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsTradingUser();
        investigationPage.navigateToClient(client.getUcid());
        alertsPage.isAlertTabVisible();
        generalTab.isGeneralTabVisible();
        sessionsTab.isSessionTabVisible();
        paymentsPage.isPaymentsTabVisible();
        tradingPage.isTradingTabVisible();
        connectionPage.isConnectionsTabVisible();
        auditTrailPage.isAuditTabVisible();
        restrictionPage.isRestrictionTabVisible();
        paymentsPage.navigate(client.getUcid());
        paymentsPage.isWithdrawalsSubtabVisible();
        paymentsPage.isSummarySubtabVisible();
        tradingPage.navigateOperations(client.getUcid());
        tradingPage.isIllegalProfitButtonHidden();
        connectionPage.clickConnectionTabButton();
        connectionPage.openConnectionGraph();
        connectionPage.isMultiselectButtonHidden();
    }

    @Test
    @AllureId("1739")
    @DisplayName("Viewer user can see summary and withdrawals in payment tab")
    void viewerNotSeePaymentWithdrawals() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsViewerUser();
        paymentsPage.navigate(client.getUcid());
        paymentsPage.isWithdrawalsSubtabVisible();
        paymentsPage.isSummarySubtabVisible();
    }

    @Test
    @AllureId("1740")
    @DisplayName("Viewer user not have illegal profit button in trading/operations")
    void viewerNotSeeIllegalProfitButtonInTradingOperations() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsViewerUser();
        tradingPage.navigateOperations(client.getUcid());
        tradingPage.isIllegalProfitButtonHidden();
    }

    @Test
    @AllureId("1741")
    @DisplayName("Viewer user not have multiselect on table CS")
    void viewerNotHaveMultiselectInConnectionSearch() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsViewerUser();
        connectionPage.navigate(client.getUcid());
        connectionPage.openConnectionGraph();
        connectionPage.isMultiselectButtonHidden();
    }

    @Test
    @AllureId("1324")
    @DisplayName(
            "Viewer can't assign suspicious client with the active alert to himself to perform investigation from the client card")
    void assignClientTest() {
        sendSimpleAlert(client.getUcid(), CPA_ABUSE.getCode());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsViewerUser();
        investigationPage.navigateToClient(client.getUcid());
        investigationPage.cantInvestigateClientCard();
        closeAlert(client);
    }

    @Test
    @AllureId("1325")
    @DisplayName("Viewer can't comment client")
    void commentTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsViewerUser();
        investigationPage.navigateToClient(client.getUcid());
        investigationPage.cantOpenCommentForm();
    }

    @Test
    @AllureId("1325")
    @DisplayName("Viewer can't report fraud")
    void reportFraudTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsViewerUser();
        investigationPage.navigateToClient(client.getUcid());
        resolvePage.cantOpenReportFraudForm();
    }

    @Test
    @AllureId("1327")
    @DisplayName("Restriction tab viewer can't set restriction UI")
    void setRestrictionTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsViewerUser();
        restrictionPage.navigate(client.getUcid());
        restrictionPage.cantAddNewRestriction();
    }

    @AfterAll
    static void teardown() throws Exception {
        deleteObjectFromDb(
                CRM_USER_TABLE_NAME,
                String.format(
                        "ucid IN ('%s', '%s', '%s')",
                        client.getUcid(), connectedClient1.getUcid(), connectedClient2.getUcid()));
        deleteObjectFromDb(
                CONNECTIONS_TABLE_NAME,
                String.format(
                        "user_from IN ('%s', '%s', '%s')",
                        client.getUcid(), connectedClient1.getUcid(), connectedClient2.getUcid()));
        cleanClientAudit(client.getUcid(), connectedClient1.getUcid(), connectedClient2.getUcid());
        deleteUserBO(client.getUcid());
        deleteUserBO(connectedClient1.getUcid());
        deleteUserBO(connectedClient2.getUcid());
        ArHelper.deleteUserFromAbuseRegistry(client.getUcid(), connectedClient1.getUcid(), connectedClient2.getUcid());
        deleteObjectFromDb(MT4_TRADES_COERCED_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
    }
}
