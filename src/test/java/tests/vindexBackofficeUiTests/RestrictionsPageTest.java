package tests.vindexBackofficeUiTests;

import businessObjects.db.clickhouse.crmTbAccount.CrmTbAccountObject;
import businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObject;
import businessObjects.db.clickhouse.crmTbWithdrawal.CrmTbWithdrawalObject;
import helpers.data.ClientHelper;
import helpers.data.enums.Brand;
import helpers.data.enums.Regulator;
import io.qameta.allure.AllureId;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;


import java.util.ArrayList;
import java.util.List;

import static businessObjects.api.mitigationService.MitigationServiceRequest.enableCRMEmulator;
import static businessObjects.db.clickhouse.crmTbAccount.CrmTbAccountObjectFactory.generateStaticCrmTbAccountActive;
import static businessObjects.db.clickhouse.crmTbAccount.CrmTbAccountObjectFactory.generateStaticCrmTbAccountInactive;
import static businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObjectFactory.generateStaticUserByClient;
import static businessObjects.db.clickhouse.crmTbWithdrawal.CrmTbWithdrawalObjectFactory.generateStaticWithdrawalByClient;
import static helpers.database.DbHelper.insertObjectsToDb;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static utils.Constants.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RestrictionsPageTest extends TestBaseWeb {

    static ClientHelper restrictionClient = new ClientHelper(141_401, "063cde3b-ea9d-48b5-8e2c-99f3d5f67999", Brand.INFINOX, Regulator.VFSC2, 14_140_101, 42);
    static ClientHelper withdrawalClient = new ClientHelper(141_402, "063cde3b-ea9d-48b5-8e2c-99f3d5f67999", Brand.INFINOX, Regulator.VFSC2, 14_140_102, 42);
    static ClientHelper labelClient = new ClientHelper(141_403, "063cde3b-ea9d-48b5-8e2c-99f3d5f67999", Brand.INFINOX, Regulator.VFSC2, 14_140_103, 42);

    @BeforeAll
    public static void Setup() throws Exception {
        Response response = enableCRMEmulator();
        assertNotNull(response);

        CrmTbUserObject restrictionClientDB = generateStaticUserByClient(restrictionClient);
        CrmTbUserObject withdrawalClientDB = generateStaticUserByClient(withdrawalClient);
        CrmTbUserObject labelClientDB = generateStaticUserByClient(labelClient);
        CrmTbWithdrawalObject withdrawal1 = generateStaticWithdrawalByClient(withdrawalClient, "first withdrawal", 1);
        CrmTbWithdrawalObject withdrawal2 = generateStaticWithdrawalByClient(withdrawalClient, "second withdrawal", 2);
        CrmTbWithdrawalObject withdrawal3 = generateStaticWithdrawalByClient(withdrawalClient, "third withdrawal", 3);

        CrmTbAccountObject active = generateStaticCrmTbAccountActive(restrictionClient);
        CrmTbAccountObject inactive = generateStaticCrmTbAccountInactive(labelClient);

        List<CrmTbUserObject> testUser = new ArrayList<>();
        testUser.add(restrictionClientDB);
        testUser.add(withdrawalClientDB);
        testUser.add(labelClientDB);
        insertObjectsToDb(CRM_USER_TABLE_NAME, testUser);

        List<CrmTbWithdrawalObject> withdrawals = new ArrayList<>();
        withdrawals.add(withdrawal1);
        withdrawals.add(withdrawal2);
        withdrawals.add(withdrawal3);
        insertObjectsToDb(CRM_WITHDRAWAL_TABLE_NAME, withdrawals);

        List<CrmTbAccountObject> accounts = new ArrayList<>();
        accounts.add(active);
        accounts.add(inactive);
        insertObjectsToDb(CRM_ACCOUNT_TABLE_NAME, accounts);
    }


    @BeforeEach
    public void before() throws Exception {
        restrictionPage.cleanUserRestriction(restrictionClient.getUcid());
        restrictionPage.cleanUserAudit(restrictionClient.getUcid());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("327")
    @DisplayName("Restriction tab set account restriction UI")
    void setAccountRestrictionUITest() throws Exception {
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        restrictionPage.navigate(restrictionClient.getUcid());
        restrictionPage.clickAccountSwitch();
        restrictionPage.fillApplyReason("test reason");
        restrictionPage.checkThatAccountIsChecked();
        restrictionPage.checkKafkaRequestApplyUCID(restrictionClient.getUserId());
        restrictionPage.checkRestrictionApplymentAudit(restrictionClient.getUcid(), "Open new account");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("328")
    @DisplayName("Restriction tab remove account restriction UI")
    void cancelAccountRestrictionUITest() throws Exception {
        restrictionPage.setRestrictionAPIGeneral(restrictionClient.getUcid(), "01");
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        restrictionPage.navigate(restrictionClient.getUcid());
        restrictionPage.checkThatAccountIsChecked();
        restrictionPage.clickCheckedAccount();
        restrictionPage.fillCancelReason("test reason");
        restrictionPage.checkKafkaRequestApplyUCID(restrictionClient.getUserId());
        restrictionPage.checkRestrictionCancellationAudit(restrictionClient.getUcid(), "Open new account");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("329")
    @DisplayName("Restriction tab set transfer restriction UI")
    void setTransferRestrictionUITest() throws Exception {
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        restrictionPage.navigate(restrictionClient.getUcid());
        restrictionPage.clickTransferSwitch();
        restrictionPage.fillApplyReason("test reason");
        restrictionPage.checkThatTransferIsChecked();
        restrictionPage.checkKafkaRequestApplyUCID(restrictionClient.getUserId());
        restrictionPage.checkRestrictionApplymentAudit(restrictionClient.getUcid(), "Internal transfer");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("330")
    @DisplayName("Restriction tab remove Transfer Restriction UI")
    void cancelTransferRestrictionUITest() throws Exception {
        restrictionPage.setRestrictionAPIGeneral(restrictionClient.getUcid(), "02");
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        restrictionPage.navigate(restrictionClient.getUcid());
        restrictionPage.checkThatTransferIsChecked();
        restrictionPage.clickCheckedTransfer();
        restrictionPage.fillCancelReason("test reason");
        restrictionPage.checkRestrictionCancellationAudit(restrictionClient.getUcid(), "Internal transfer");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("331")
    @DisplayName("Restriction tab set Deposits restriction UI")
    void setDepositsRestrictionUITest() throws Exception {
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        restrictionPage.navigate(restrictionClient.getUcid());
        restrictionPage.clickDepositsSwitch();
        restrictionPage.fillApplyReason("test reason");
        restrictionPage.checkThatDepositsIsChecked();
        restrictionPage.checkKafkaRequestApplyUCID(restrictionClient.getUserId());
        restrictionPage.checkRestrictionApplymentAudit(restrictionClient.getUcid(), "Deposits");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("332")
    @DisplayName("Restriction tab remove Deposits restriction UI")
    void cancelDepositsRestrictionUITest() throws Exception {
        restrictionPage.setRestrictionAPIGeneral(restrictionClient.getUcid(), "03");
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        restrictionPage.navigate(restrictionClient.getUcid());
        restrictionPage.checkThatDepositsIsChecked();
        restrictionPage.clickCheckedDeposits();
        restrictionPage.fillCancelReason("test reason");
        restrictionPage.checkRestrictionCancellationAudit(restrictionClient.getUcid(), "Deposits");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("333")
    @DisplayName("Restriction tab set Withdrawals restriction UI")
    void setWithdrawalsRestrictionUITest() throws Exception {
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        restrictionPage.navigate(restrictionClient.getUcid());
        restrictionPage.clickWithdrawalsSwitch();
        restrictionPage.fillApplyReason("test reason");
        restrictionPage.checkThatWithdrawalsIsChecked();
        restrictionPage.checkKafkaRequestApplyUCID(restrictionClient.getUserId());
        restrictionPage.checkRestrictionApplymentAudit(restrictionClient.getUcid(), "Withdrawals");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("334")
    @DisplayName("Restriction tab remove Withdrawals restriction UI")
    void cancelWithdrawalsRestrictionUITest() throws Exception {
        restrictionPage.setRestrictionAPIGeneral(restrictionClient.getUcid(), "04");
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        restrictionPage.navigate(restrictionClient.getUcid());
        restrictionPage.checkThatWithdrawalsIsChecked();
        restrictionPage.clickCheckedWithdrawals();
        restrictionPage.fillCancelReason("test reason");
        restrictionPage.checkRestrictionCancellationAudit(restrictionClient.getUcid(), "Withdrawals");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("335")
    @DisplayName("Restriction tab set Login CRM restriction UI")
    void setLoginCRMRestrictionUITest() throws Exception {
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        restrictionPage.navigate(restrictionClient.getUcid());
        restrictionPage.clickLoginSwitch();
        restrictionPage.fillApplyReason("test reason");
        restrictionPage.checkThatLoginIsChecked();
        restrictionPage.checkKafkaRequestApplyUCID(restrictionClient.getUserId());
        restrictionPage.checkRestrictionApplymentAudit(restrictionClient.getUcid(), "Login CRM");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("336")
    @DisplayName("Restriction tab remove Login CRM restriction UI")
    void cancelLoginCRMRestrictionUITest() throws Exception {
        restrictionPage.setRestrictionAPIGeneral(restrictionClient.getUcid(), "05");
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        restrictionPage.navigate(restrictionClient.getUcid());
        restrictionPage.checkThatLoginIsChecked();
        restrictionPage.clickCheckedLogin();
        restrictionPage.fillCancelReason("test reason");
        restrictionPage.checkRestrictionCancellationAudit(restrictionClient.getUcid(), "Login CRM");
    }


    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("337")
    @DisplayName("Restriction tab set Manual Withdrawal Review restriction UI")
    void setManualWithdrawalRestrictionUITest() throws Exception {
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        restrictionPage.navigate(restrictionClient.getUcid());
        restrictionPage.clickManualWithdrawalSwitch();
        restrictionPage.fillApplyReason("test reason");
        restrictionPage.checkThatManualWithdrawalIsChecked();
        restrictionPage.checkKafkaRequestApplyUCID(restrictionClient.getUserId());
        restrictionPage.checkRestrictionApplymentAudit(restrictionClient.getUcid(), "Manual Withdrawal Review");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("338")
    @DisplayName("Restriction tab remove Manual Withdrawal Review restriction UI client without transactions")
    void cancelManualWithdrawalRestrictionUITest() throws Exception {
        restrictionPage.setRestrictionAPIGeneral(restrictionClient.getUcid(), "13");
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        restrictionPage.navigate(restrictionClient.getUcid());
        restrictionPage.checkThatManualWithdrawalIsChecked();
        restrictionPage.clickCheckedManual();
        restrictionPage.fillCancelReason("test reason");
        restrictionPage.checkRestrictionCancellationAudit(restrictionClient.getUcid(), "Manual Withdrawal Review");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("361")
    @DisplayName("Restriction tab remove Manual Withdrawal Review restriction UI client with transactions all green")
    void cancelManualWithdrawalRestrictionUITestWithTransactionsGreenTest() throws Exception {
        restrictionPage.cleanUserAudit(withdrawalClient.getUcid());
        restrictionPage.setRestrictionAPIGeneral(withdrawalClient.getUcid(), "13");
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        restrictionPage.navigate(withdrawalClient.getUcid());
        restrictionPage.checkThatManualWithdrawalIsChecked();
        restrictionPage.clickCheckedManual();
        restrictionPage.fillCancelReasonManualWithdrawalAllGreen("test reason");
        String details = "Transaction ID 14140201; 71.00 USDT 2024-11-13 10:11 first withdrawal; Accept";
        restrictionPage.checkRestrictionCancellationAudit(withdrawalClient.getUcid(), "WD_REQUEST_DECISION", details);
        restrictionPage.checkKafkaRequestWithdrawal("14140201", "5");
        restrictionPage.checkKafkaRequestApplyUCID(withdrawalClient.getUserId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("365")
    @DisplayName("Restriction tab remove Manual Withdrawal Review restriction UI client with transactions all refuse")
    void cancelManualWithdrawalRestrictionUITestWithTransactionsRefuseTest() throws Exception {
        restrictionPage.cleanUserAudit(withdrawalClient.getUcid());
        restrictionPage.setRestrictionAPIGeneral(withdrawalClient.getUcid(), "13");
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        restrictionPage.navigate(withdrawalClient.getUcid());
        restrictionPage.checkThatManualWithdrawalIsChecked();
        restrictionPage.clickCheckedManual();
        restrictionPage.fillCancelReasonManualWithdrawalAllrefuse("test reason");
        String details = "Transaction ID 14140201; 71.00 USDT 2024-11-13 10:11 first withdrawal; Refuse";
        restrictionPage.checkRestrictionCancellationAudit(withdrawalClient.getUcid(), "WD_REQUEST_DECISION", details);
        restrictionPage.checkKafkaRequestWithdrawal("14140201", "4");
        restrictionPage.checkKafkaRequestApplyUCID(withdrawalClient.getUserId());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("366")
    @DisplayName("Restriction tab remove Manual Withdrawal Review restriction UI client with transactions  approve one")
    void cancelManualWithdrawalRestrictionUITestWithTransactionsApproveOneTest() throws Exception {
        //login
        restrictionPage.cleanUserAudit(withdrawalClient.getUcid());
        restrictionPage.setRestrictionAPIGeneral(withdrawalClient.getUcid(), "13");
        investigationPage.navigate();
        //first run
        keycloackPage.loginWeb("dev", "123");
        restrictionPage.navigate(withdrawalClient.getUcid());
        restrictionPage.checkThatManualWithdrawalIsChecked();
        restrictionPage.clickCheckedManual();
        restrictionPage.fillCancelReasonManualWithdrawalApproveOneByPaymentType("test reason", "first withdrawal");
        String details1 = "Transaction ID 14140201; 71.00 USDT 2024-11-13 10:11 first withdrawal; Accept";
        restrictionPage.checkRestrictionCancellationAudit(withdrawalClient.getUcid(), "WD_REQUEST_DECISION", details1);
        restrictionPage.checkKafkaRequestWithdrawal("14140201", "5");
        restrictionPage.checkKafkaRequestApplyUCID(withdrawalClient.getUserId());
        //second run
        restrictionPage.cleanUserAudit(withdrawalClient.getUcid());
        restrictionPage.setRestrictionAPIGeneral(withdrawalClient.getUcid(), "13");
        restrictionPage.navigate(withdrawalClient.getUcid());
        restrictionPage.checkThatManualWithdrawalIsChecked();
        restrictionPage.clickCheckedManual();
        restrictionPage.fillCancelReasonManualWithdrawalApproveOneByPaymentType("test reason", "first withdrawal");
        String details2 = "Transaction ID 14140203; 71.00 USDT 2024-11-13 10:11 third withdrawal; Refuse";
        restrictionPage.checkRestrictionCancellationAudit(withdrawalClient.getUcid(), "WD_REQUEST_DECISION", details2);
        restrictionPage.checkKafkaRequestWithdrawal("14140203", "4");
        restrictionPage.checkKafkaRequestApplyUCID(withdrawalClient.getUserId());
    }


    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("339")
    @DisplayName("Restriction tab set Close only mode Review restriction UI")
    void setCloseOnlyModeRestrictionUITest() throws Exception {
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        restrictionPage.navigate(restrictionClient.getUcid());
        restrictionPage.clickCloseOnlyModeSwitch();
        restrictionPage.fillApplyReasonTradingAllAccs("test reason");
        restrictionPage.checkThatCloseOnlyIsChecked();
//        restrictionPage.checkKafkaRequestApplyTradeUCID("141401");
        restrictionPage.checkRestrictionApplymentAudit(restrictionClient.getUcid(), "Close only mode; account: 14140101");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("340")
    @DisplayName("Restriction tab remove Close only mode restriction UI")
    void cancelCloseOnlyModeRestrictionUITest() throws Exception {
        restrictionPage.setRestrictionAPITrade(restrictionClient.getUcid(), 14_140_101, 3, "06");
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        restrictionPage.navigate(restrictionClient.getUcid());
        restrictionPage.checkThatCloseOnlyIsChecked();
        restrictionPage.clickCheckedCloseOnly();
        restrictionPage.fillCancelReasonTrade("test reason");
        restrictionPage.checkRestrictionCancellationAudit(restrictionClient.getUcid(), "Close only mode; account: 14140101");
    }


    @Disabled
    //restriction abadoned
    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("341")
    @DisplayName("Restriction tab set Off quotes Review restriction UI")
    void setOffQuotesRestrictionUITest() throws Exception {
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        restrictionPage.navigate(restrictionClient.getUcid());
        restrictionPage.clickOffQuotesModeSwitch();
        restrictionPage.fillApplyReasonTradingAllAccs("test reason");
        restrictionPage.checkThatAOffQuotesIsChecked();
//        restrictionPage.checkKafkaRequestApplyTradeUCID("141401");
        restrictionPage.checkRestrictionApplymentAudit(restrictionClient.getUcid(), "Off quotes; account: 14140101");
    }

    @Disabled
    //restriction abadoned
    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("342")
    @DisplayName("Restriction tab remove Off quotes restriction UI")
    void cancelOffQuotesRestrictionUITest() throws Exception {
        restrictionPage.setRestrictionAPITrade(restrictionClient.getUcid(), 14_140_101, 3, "08");
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        restrictionPage.navigate(restrictionClient.getUcid());
        restrictionPage.checkThatAOffQuotesIsChecked();
        restrictionPage.clickCheckedOffQuotes();
        restrictionPage.fillCancelReasonTrade("test reason");
        restrictionPage.checkRestrictionCancellationAudit(restrictionClient.getUcid(), "Off quotes; account: 14140101");
    }


    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("343")
    @DisplayName("Restriction tab set AB book Review restriction UI")
    void setAbBookRestrictionUITest() throws Exception {
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        restrictionPage.navigate(restrictionClient.getUcid());
        restrictionPage.clickAbBookSwitch();
        restrictionPage.fillApplyReasonTradingAllAccs("test reason");
        restrictionPage.checkThatAbBookIsChecked();
//        restrictionPage.checkKafkaRequestApplyUCID(restrictionClient.getUserId());
        restrictionPage.checkRestrictionApplymentAudit(restrictionClient.getUcid(), "B-Book -> A-Book; account: 14140101");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("344")
    @DisplayName("Restriction tab remove AB book restriction UI")
    void cancelAbBookRestrictionUITest() throws Exception {
        restrictionPage.setRestrictionAPITrade(restrictionClient.getUcid(), 14_140_101, 3, "09");
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        restrictionPage.navigate(restrictionClient.getUcid());
        restrictionPage.checkThatAbBookIsChecked();
        restrictionPage.clickCheckedAbBook();
        restrictionPage.fillCancelReasonTrade("test reason");
        restrictionPage.checkRestrictionCancellationAudit(restrictionClient.getUcid(), "B-Book -> A-Book; account: 14140101");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("739")
    @DisplayName("Restriction tab. indicator 'inactive' must be presenr on row with account with 'inactive' status in trade restriction applyment/removal menu")
    void inactiveAccountIndicatorTest() {
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        restrictionPage.navigate(labelClient.getUcid());
        restrictionPage.clickCloseOnlyModeSwitch();
        restrictionPage.isInactiveAccountLabelPresented();
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("738")
    @DisplayName("Restriction tab. Popup with tip about last active date must appear on hover to last activity date in trade restriction applyment/removal menu")
    void activityTooltipTest() {
        investigationPage.navigate();
        keycloackPage.loginWeb("dev", "123");
        restrictionPage.navigate(labelClient.getUcid());
        restrictionPage.clickCloseOnlyModeSwitch();
        restrictionPage.hoverOverActivitySection();
        restrictionPage.checkTooltipText("Last activity");
    }

}
