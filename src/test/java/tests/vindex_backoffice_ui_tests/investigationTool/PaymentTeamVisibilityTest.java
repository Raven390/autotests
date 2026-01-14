package tests.vindex_backoffice_ui_tests.investigationTool;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountDataForUi;
import static business_objects.db.clickhouse.crm_tb_account_for_mt.crm_tb_account.CrmTbAccountForMtObjectFactory.generateAccountForMtByAccount;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.data_science_test.connection_table.ConnectionTableEntryFactory.getConnectionTableEntryForUiFiltration1;
import static business_objects.db.clickhouse.data_science_test.connection_table.ConnectionTableEntryFactory.getConnectionTableEntryForUiFiltration2;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObjectFactory.generateMt4TradesCoercedAccountProfitComment;
import static business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObjectFactory.generateMt4TradesCoercedAccountProfitCommentBuy;
import static business_objects.db.clickhouse.mt_mt5_positions.MtMt5PositionsObjectFactory.generateMtMt5PositionsObject;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.Currency.EUR;
import static helpers.data.enums.Currency.USD;
import static helpers.database.AuHelper.cleanClientAudit;
import static helpers.database.BoHelper.deleteUserBO;
import static helpers.database.DbHelper.*;
import static helpers.database.DbHelper.insertObjectToDb;
import static helpers.kafka.alerts.CreateSimpleAlert.sendSimplePaymentAlert;
import static utils.Constants.*;
import static utils.Utils.getRandomIntPositive;
import static utils.Utils.waitForConnectionSearchToUpdate;

import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_account_for_mt.crm_tb_account.CrmTbAccountForMtObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.data_science_test.connection_table.ConnectionTableEntry;
import business_objects.db.clickhouse.mt_account.MtAccountObject;
import business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObject;
import business_objects.db.clickhouse.mt_mt5_positions.MtMt5PositionsObject;
import helpers.data.ClientHelper;
import helpers.database.ArHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import java.util.List;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

@Tag(TEAM_BACKOFFICE)
@Tag(LAYER_WEB)
@Feature("BMS-1980 Investigation tool visibility + assignment payment alerts")
class PaymentTeamVisibilityTest extends TestBaseWeb {
    private static final ClientHelper client = getRandomVantageClientAllFields();
    private static final ClientHelper connectedClient1 = getRandomVantageClientAllFields();
    private static final ClientHelper connectedClient2 = getRandomVantageClientAllFields();
    private static final CrmTbAccountObject account1 = generateCrmTbAccountDataForUi(connectedClient1);
    private static final CrmTbAccountObject account2 = generateCrmTbAccountDataForUi(connectedClient2);
    private static MtAccountObject mtAccount1;
    private static MtAccountObject mtAccount2;
    private static MtAccountObject mtAccount3;
    private static MtAccountObject mtAccount4;
    private static MtAccountObject mtAccount5;
    private static MtMt4TradesCoercedObject trade1;
    private static MtMt4TradesCoercedObject trade2;
    private static MtMt4TradesCoercedObject trade3;
    private static MtMt4TradesCoercedObject trade4;
    private static MtMt4TradesCoercedObject trade5;
    private static MtMt4TradesCoercedObject trade6;
    private static MtMt4TradesCoercedObject trade7;
    private static MtMt4TradesCoercedObject trade8;
    private static MtMt4TradesCoercedObject trade9;
    private static MtMt4TradesCoercedObject trade10;
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

        insertObjectsToDb(CRM_TB_ACCOUNT_TABLE_NAME, List.of(account1, account2));
        waitForConnectionSearchToUpdate(client);
        CrmTbAccountObject account1 = generateCrmTbAccountDataForUi(client);
        account1.currency = USD.getCode();
        CrmTbAccountObject account2 = generateCrmTbAccountDataForUi(client);
        account2.account = getRandomIntPositive();
        account2.currency = USD.getCode();
        CrmTbAccountObject account3 = generateCrmTbAccountDataForUi(client);
        account3.account = getRandomIntPositive();
        account3.currency = EUR.getCode();
        CrmTbAccountObject account4 = generateCrmTbAccountDataForUi(client);
        account4.account = getRandomIntPositive();
        account4.currency = EUR.getCode();
        CrmTbAccountObject account5 = generateCrmTbAccountDataForUi(client);
        account5.account = getRandomIntPositive();
        account5.currency = USD.getCode();
        mtAccount1 = generateMtAccountByCrmTbAccount(account1);
        mtAccount2 = generateMtAccountByCrmTbAccount(account2);
        mtAccount3 = generateMtAccountByCrmTbAccount(account3);
        mtAccount4 = generateMtAccountByCrmTbAccount(account4);
        mtAccount5 = generateMtAccountByCrmTbAccount(account5);
        CrmTbAccountForMtObject crmTbAccFormtAccount1 = generateAccountForMtByAccount(account1);
        CrmTbAccountForMtObject crmTbAccFormtAccount2 = generateAccountForMtByAccount(account2);
        CrmTbAccountForMtObject crmTbAccFormtAccount3 = generateAccountForMtByAccount(account3);
        CrmTbAccountForMtObject crmTbAccFormtAccount4 = generateAccountForMtByAccount(account4);
        CrmTbAccountForMtObject crmTbAccFormtAccount5 = generateAccountForMtByAccount(account5);

        String comment = "comment";
        trade1 = generateMt4TradesCoercedAccountProfitCommentBuy(account1, 200.12 + 10_000d, comment);
        trade2 = generateMt4TradesCoercedAccountProfitCommentBuy(account1, 300d, comment);
        tradeWithdrawal = generateMt4TradesCoercedAccountProfitComment(account1, -10_000d, "withdraw");
        trade3 = generateMt4TradesCoercedAccountProfitCommentBuy(account2, 500d, comment);
        trade4 = generateMt4TradesCoercedAccountProfitCommentBuy(account2, 500.23, comment);
        trade5 = generateMt4TradesCoercedAccountProfitCommentBuy(account3, 800d, comment);
        trade6 = generateMt4TradesCoercedAccountProfitCommentBuy(account3, 1000.45, comment);
        trade7 = generateMt4TradesCoercedAccountProfitCommentBuy(account4, 1000d, comment);
        trade8 = generateMt4TradesCoercedAccountProfitCommentBuy(account4, 1800.67, comment);
        trade9 = generateMt4TradesCoercedAccountProfitCommentBuy(account5, 400d, comment);
        trade10 = generateMt4TradesCoercedAccountProfitCommentBuy(account5, 1100.89, comment);

        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        insertObjectsToDb(CRM_TB_ACCOUNT_TABLE_NAME, List.of(account1, account2, account3, account4, account5));
        insertObjectsToDb(MT_ACCOUNT_TABLE_NAME, List.of(mtAccount1, mtAccount2, mtAccount3, mtAccount4, mtAccount5));
        insertObjectsToDb(
                CRM_TB_ACCOUNT_FOR_MT_TABLE_NAME,
                List.of(
                        crmTbAccFormtAccount1,
                        crmTbAccFormtAccount2,
                        crmTbAccFormtAccount3,
                        crmTbAccFormtAccount4,
                        crmTbAccFormtAccount5));
        insertObjectsToDb(
                MT4_TRADES_COERCED_TABLE_NAME,
                List.of(
                        trade1,
                        trade2,
                        tradeWithdrawal,
                        trade3,
                        trade4,
                        trade5,
                        trade6,
                        trade7,
                        trade8,
                        trade9,
                        trade10));
        MtMt5PositionsObject position = generateMtMt5PositionsObject(client);
        position.setAccount(mtAccount2.account);
        position.setServerId(mtAccount2.sourceIdSt);
        insertObjectToDb(MT5_POSITIONS_TABLE_NAME, position);
    }

    @Test
    @AllureId("1567")
    @DisplayName("BO user with Payment Team role can see tabs")
    void assignClientCardTest() throws Exception {
        sendSimplePaymentAlert(client.getUcid());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsPaymentTeamUser();
        investigationPage.navigateToClient(client.getUcid());
    }

    @Test
    @AllureId("1561")
    @DisplayName("Payment Team user have reduced set of tabs")
    void paymentTeamNotSeeAllTabs() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsPaymentTeamUser();
        investigationPage.navigateToClient(client.getUcid());
        alertsPage.isAlertTabHidden();
        generalTab.isGeneralTabVisible();
        sessionsTab.isSessionTabVisible();
        paymentsPage.isPaymentsTabVisible();
        tradingPage.isTradingTabVisible();
        connectionPage.isConnectionsTabVisible();
        auditTrailPage.isAuditTabVisible();
        restrictionPage.isRestrictionTabVisible();
    }

    @Test
    @AllureId("1562")
    @DisplayName("Payment Team user can see only summary in payment tab")
    void paymentTeamNotSeePaymentWithdrawals() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsPaymentTeamUser();
        paymentsPage.navigate(client.getUcid());
        paymentsPage.isWithdrawalsSubtabHidden();
        paymentsPage.isSummarySubtabVisible();
    }

    @Test
    @AllureId("1563")
    @DisplayName("Payment Team user not have illegal profit button in trading/operations")
    void paymentTeamNotSeeIllegalProfitButtonInTradingOperations() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsPaymentTeamUser();
        tradingPage.navigateOperations(client.getUcid());
        tradingPage.isIllegalProfitButtonHidden();
    }

    @Test
    @AllureId("1564")
    @DisplayName("Payment Team user not have multiselect on table CS")
    void paymentTeamNotHaveMultiselectInConnectionSearch() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsPaymentTeamUser();
        connectionPage.navigate(client.getUcid());
        connectionPage.openConnectionGraph();
        connectionPage.isMultiselectButtonHidden();
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
        deleteObjectFromDb(MT5_POSITIONS_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
    }
}
