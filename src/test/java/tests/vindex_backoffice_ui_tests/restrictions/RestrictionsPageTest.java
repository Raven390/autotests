package tests.vindex_backoffice_ui_tests.restrictions;

import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.mtAccount.MtAccountObject;
import helpers.data.ClientHelper;
import helpers.data.enums.Brand;
import helpers.data.enums.Regulator;
import helpers.data.enums.Restriction;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Muted;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import page_objects.backoffice_pages.RestrictionPage;
import tests.TestBaseWeb;


import java.util.List;

import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateStaticCrmTbAccountActive;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateStaticCrmTbAccountInactive;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateStaticUserByClient;
import static business_objects.db.clickhouse.mtAccount.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static helpers.data.enums.Restriction.*;
import static helpers.database.DbHelper.insertObjectsToDb;
import static helpers.database.CleanTableHelper.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static page_objects.backoffice_pages.RestrictionPage.checkKafkaRequestApplyUserId;
import static page_objects.backoffice_pages.RestrictionPage.checkRestrictionApplymentAuditGeneral;
import static utils.Constants.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RestrictionsPageTest extends TestBaseWeb {

    static ClientHelper restrictionClient = new ClientHelper(141_401, "063cde3b-ea9d-48b5-8e2c-99f3d5f67999", Brand.VANTAGE, Regulator.VFSC2, 14_140_101, 42);
    static ClientHelper labelClient = new ClientHelper(141_403, "063cde3b-ea9d-48b5-8e2c-99f3d5f67999", Brand.VANTAGE, Regulator.VFSC2, 14_140_103, 42);

    @BeforeAll
    static void setup() throws Exception {
        Response response = enableCRMEmulator();
        assertNotNull(response);

        CrmTbUserObject restrictionClientDB = generateStaticUserByClient(restrictionClient);
        CrmTbUserObject labelClientDB = generateStaticUserByClient(labelClient);

        CrmTbAccountObject active = generateStaticCrmTbAccountActive(restrictionClient);
        MtAccountObject mtAccountActive1 = generateMtAccountByCrmTbAccount(active);
        CrmTbAccountObject inactive = generateStaticCrmTbAccountInactive(labelClient);
        MtAccountObject mtAccountInactive = generateMtAccountByCrmTbAccount(inactive);

        insertObjectsToDb(MT_ACCOUNT_TABLE_NAME, List.of(mtAccountActive1, mtAccountInactive));

        insertObjectsToDb(CRM_USER_TABLE_NAME, List.of(restrictionClientDB, labelClientDB));

        insertObjectsToDb(CRM_ACCOUNT_TABLE_NAME, List.of(active, inactive));
    }


    @BeforeEach
    public void before() throws Exception {
        cleanUserRestrictionGeneral(restrictionClient.getUcid());
        restrictionPage.cleanUserAudit(restrictionClient.getUcid());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("327")
    @DisplayName("Restriction tab set account restriction UI")
    void setAccountRestrictionUITest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        restrictionPage.navigate(restrictionClient.getUcid());
        restrictionPage.clickAccountSwitch();
        restrictionPage.fillApplyReason("test reason");
        restrictionPage.checkThatAccountIsChecked();
        checkKafkaRequestApplyUserId(restrictionClient.getUserId());
        checkRestrictionApplymentAuditGeneral(restrictionClient.getUcid(), ACCOUNT_CREATION.getName());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("328")
    @DisplayName("Restriction tab remove account restriction UI")
    void cancelAccountRestrictionUITest() throws Exception {
        RestrictionPage.setRestrictionAPIGeneral(restrictionClient.getUcid(), ACCOUNT_CREATION.getCode());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        restrictionPage.navigate(restrictionClient.getUcid());
        restrictionPage.checkThatAccountIsChecked();
        restrictionPage.clickCheckedAccount();
        restrictionPage.fillCancelReason("test reason");
        restrictionPage.checkKafkaRequestCancelUcid(restrictionClient.getUserId());
        restrictionPage.checkRestrictionCancellationAuditBO(restrictionClient.getUcid(), ACCOUNT_CREATION.getName());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("329")
    @DisplayName("Restriction tab set transfer restriction UI")
    void setTransferRestrictionUITest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        restrictionPage.navigate(restrictionClient.getUcid());
        restrictionPage.clickTransferSwitch();
        restrictionPage.fillApplyReason("test reason");
        restrictionPage.checkThatTransferIsChecked();
        checkKafkaRequestApplyUserId(restrictionClient.getUserId());
        checkRestrictionApplymentAuditGeneral(restrictionClient.getUcid(), INTERNAL_TRANSFER.getName());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("330")
    @DisplayName("Restriction tab remove Transfer Restriction UI")
    void cancelTransferRestrictionUITest() throws Exception {
        restrictionPage.setRestrictionAPIGeneral(restrictionClient.getUcid(), INTERNAL_TRANSFER.getCode());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        restrictionPage.navigate(restrictionClient.getUcid());
        restrictionPage.checkThatTransferIsChecked();
        restrictionPage.clickCheckedTransfer();
        restrictionPage.fillCancelReason("test reason");
        restrictionPage.checkKafkaRequestCancelUcid(restrictionClient.getUserId());
        restrictionPage.checkRestrictionCancellationAuditBO(restrictionClient.getUcid(), INTERNAL_TRANSFER.getName());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("331")
    @DisplayName("Restriction tab set Deposits restriction UI")
    void setDepositsRestrictionUITest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        restrictionPage.navigate(restrictionClient.getUcid());
        restrictionPage.clickDepositsSwitch();
        restrictionPage.fillApplyReason("test reason");
        restrictionPage.checkThatDepositsIsChecked();
        checkKafkaRequestApplyUserId(restrictionClient.getUserId());
        checkRestrictionApplymentAuditGeneral(restrictionClient.getUcid(), DEPOSITS.getName());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("332")
    @DisplayName("Restriction tab remove Deposits restriction UI")
    void cancelDepositsRestrictionUITest() throws Exception {
        restrictionPage.setRestrictionAPIGeneral(restrictionClient.getUcid(), DEPOSITS.getCode());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        restrictionPage.navigate(restrictionClient.getUcid());
        restrictionPage.checkThatDepositsIsChecked();
        restrictionPage.clickCheckedDeposits();
        restrictionPage.fillCancelReason("test reason");
        restrictionPage.checkKafkaRequestCancelUcid(restrictionClient.getUserId());
        restrictionPage.checkRestrictionCancellationAuditBO(restrictionClient.getUcid(), DEPOSITS.getName());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("333")
    @DisplayName("Restriction tab set Withdrawals restriction UI")
    void setWithdrawalsRestrictionUITest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        restrictionPage.navigate(restrictionClient.getUcid());
        restrictionPage.clickWithdrawalsSwitch();
        restrictionPage.fillApplyReason("test reason");
        restrictionPage.checkThatWithdrawalsIsChecked();
        checkKafkaRequestApplyUserId(restrictionClient.getUserId());
        checkRestrictionApplymentAuditGeneral(restrictionClient.getUcid(), WITHDRAWALS.getName());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("334")
    @DisplayName("Restriction tab remove Withdrawals restriction UI")
    void cancelWithdrawalsRestrictionUITest() throws Exception {
        restrictionPage.setRestrictionAPIGeneral(restrictionClient.getUcid(), WITHDRAWALS.getCode());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        restrictionPage.navigate(restrictionClient.getUcid());
        restrictionPage.checkThatWithdrawalsIsChecked();
        restrictionPage.clickCheckedWithdrawals();
        restrictionPage.fillCancelReason("test reason");
        restrictionPage.checkKafkaRequestCancelUcid(restrictionClient.getUserId());
        restrictionPage.checkRestrictionCancellationAuditBO(restrictionClient.getUcid(), WITHDRAWALS.getName());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("335")
    @DisplayName("Restriction tab set Login CRM restriction UI")
    void setLoginCRMRestrictionUITest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        restrictionPage.navigate(restrictionClient.getUcid());
        restrictionPage.clickLoginSwitch();
        restrictionPage.fillApplyReason("test reason");
        restrictionPage.checkThatLoginIsChecked();
        checkKafkaRequestApplyUserId(restrictionClient.getUserId());
        checkRestrictionApplymentAuditGeneral(restrictionClient.getUcid(), LOGIN_CRM.getName());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("336")
    @DisplayName("Restriction tab remove Login CRM restriction UI")
    void cancelLoginCRMRestrictionUITest() throws Exception {
        restrictionPage.setRestrictionAPIGeneral(restrictionClient.getUcid(), "05");
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        restrictionPage.navigate(restrictionClient.getUcid());
        restrictionPage.checkThatLoginIsChecked();
        restrictionPage.clickCheckedLogin();
        restrictionPage.fillCancelReason("test reason");
        restrictionPage.checkKafkaRequestCancelUcid(restrictionClient.getUserId());
        restrictionPage.checkRestrictionCancellationAuditBO(restrictionClient.getUcid(), Restriction.LOGIN_CRM.getName());
    }


    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("339")
    @DisplayName("Restriction tab set Close only mode Review restriction UI")
    void setCloseOnlyModeRestrictionUITest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        restrictionPage.navigate(restrictionClient.getUcid());
        restrictionPage.clickCloseOnlyModeSwitch();
        restrictionPage.fillApplyReasonTradingAllAccs("test reason");
        restrictionPage.checkThatCloseOnlyIsChecked();
        restrictionPage.checkKafkaRequestApplyAccount(14_140_101);
        checkRestrictionApplymentAuditGeneral(restrictionClient.getUcid(), "Close only mode; account: 14140101");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("340")
    @DisplayName("Restriction tab remove Close only mode restriction UI")
    void cancelCloseOnlyModeRestrictionUITest() throws Exception {
        restrictionPage.setRestrictionAPITrade(restrictionClient.getUcid(), 14_140_101, 3, CLOSE_ONLY_MODE.getCode());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        restrictionPage.navigate(restrictionClient.getUcid());
        restrictionPage.checkThatCloseOnlyIsChecked();
        restrictionPage.clickCheckedCloseOnly();
        restrictionPage.fillCancelReasonTrade("test reason");
        restrictionPage.checkKafkaRequestCancelAccount(14_140_101);
        restrictionPage.checkRestrictionCancellationAuditBO(restrictionClient.getUcid(), "Close only mode; account: 14140101");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("739")
    @DisplayName("Restriction tab. indicator 'inactive' must be present on row with account with 'inactive' status in trade restriction applyment/removal menu")
    void inactiveAccountIndicatorTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
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
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        restrictionPage.navigate(labelClient.getUcid());
        restrictionPage.clickCloseOnlyModeSwitch();
        restrictionPage.hoverOverActivitySection();
        restrictionPage.checkTooltipText("Last activity");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("918")
    @Feature("BMS-755 Add new restrictions")
    @DisplayName("Restriction tab. check descriptions for restrictions displayed on UI")
    void descriptionsUiTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        restrictionPage.navigate(labelClient.getUcid());
        restrictionPage.checkDisplayedRestrictionsDetails();
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("919")
    @Feature("BMS-755 Add new restrictions")
    @DisplayName("Restriction tab. check that only restrictions with bo_visibility == true is displayed")
    void displayedRestrictionsUiTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        restrictionPage.navigate(labelClient.getUcid());
        restrictionPage.checkDisplayedRestrictions();
    }


    @Disabled
    @Muted
    @Tag(TAG_MANUAL)
    //restriction hidden from UI
    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("341")
    @DisplayName("Restriction tab set Off quotes Review restriction UI")
    void setOffQuotesRestrictionUITest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        restrictionPage.navigate(restrictionClient.getUcid());
        restrictionPage.clickOffQuotesModeSwitch();
        restrictionPage.fillApplyReasonTradingAllAccs("test reason");
        restrictionPage.checkThatAOffQuotesIsChecked();
        checkRestrictionApplymentAuditGeneral(restrictionClient.getUcid(), "Off quotes; account: 14140101");
    }

    @Disabled
    @Muted
    @Tag(TAG_MANUAL)
    //restriction hidden from UI
    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("342")
    @DisplayName("Restriction tab remove Off quotes restriction UI")
    void cancelOffQuotesRestrictionUITest() throws Exception {
        restrictionPage.setRestrictionAPITrade(restrictionClient.getUcid(), 14_140_101, 3, "08");
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        restrictionPage.navigate(restrictionClient.getUcid());
        restrictionPage.checkThatAOffQuotesIsChecked();
        restrictionPage.clickCheckedOffQuotes();
        restrictionPage.fillCancelReasonTrade("test reason");
        restrictionPage.checkRestrictionCancellationAuditBO(restrictionClient.getUcid(), "Off quotes; account: 14140101");
    }


    @Disabled
    @Muted
    @Tag(TAG_MANUAL)
    //restriction hidden from UI
    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("343")
    @DisplayName("Restriction tab set AB book Review restriction UI")
    void setAbBookRestrictionUITest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        restrictionPage.navigate(restrictionClient.getUcid());
        restrictionPage.clickAbBookSwitch();
        restrictionPage.fillApplyReasonTradingAllAccs("test reason");
        restrictionPage.checkThatAbBookIsChecked();
        restrictionPage.checkKafkaRequestApplyAccount(14_140_101);
        checkRestrictionApplymentAuditGeneral(restrictionClient.getUcid(), "B-Book -> A-Book; account: 14140101");
    }

    @Disabled
    @Muted
    @Tag(TAG_MANUAL)
    //restriction hidden from UI
    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("344")
    @DisplayName("Restriction tab remove AB book restriction UI")
    void cancelAbBookRestrictionUITest() throws Exception {
        restrictionPage.setRestrictionAPITrade(restrictionClient.getUcid(), 14_140_101, 3, "09");
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        restrictionPage.navigate(restrictionClient.getUcid());
        restrictionPage.checkThatAbBookIsChecked();
        restrictionPage.clickCheckedAbBook();
        restrictionPage.fillCancelReasonTrade("test reason");
        restrictionPage.checkKafkaRequestCancelAccount(14_140_101);
        restrictionPage.checkRestrictionCancellationAuditBO(restrictionClient.getUcid(), "B-Book -> A-Book; account: 14140101");
    }

    @Disabled
    @Muted
    @Tag(TAG_MANUAL)
    //restriction hidden from UI
    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("742")
    @DisplayName("Restriction tab set Credit and Bonus Review restriction UI")
    void setCreditAndBonusRestrictionUITest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        restrictionPage.navigate(restrictionClient.getUcid());
        restrictionPage.clickCreditAndBonusSwitch();
        restrictionPage.fillApplyReason("test reason");
        restrictionPage.checkThatCreditAndBonusIsChecked();
        checkKafkaRequestApplyUserId(restrictionClient.getUserId());
        checkRestrictionApplymentAuditGeneral(restrictionClient.getUcid(), Restriction.CREDIT_AND_BONUS.getName());
    }

    @Disabled
    @Muted
    @Tag(TAG_MANUAL)
    //restriction hidden from UI
    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("743")
    @DisplayName("Restriction tab remove Credit and Bonus restriction UI client without transactions")
    void cancelCreditAndBonusRestrictionUITest() throws Exception {
        restrictionPage.setRestrictionAPIGeneral(restrictionClient.getUcid(), "14");
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        restrictionPage.navigate(restrictionClient.getUcid());
        restrictionPage.checkThatCreditAndBonusIsChecked();
        restrictionPage.clickCheckedCreditAndBonus();
        restrictionPage.fillCancelReason("test reason");
        restrictionPage.checkKafkaRequestCancelUcid(restrictionClient.getUserId());
        restrictionPage.checkRestrictionCancellationAuditBO(restrictionClient.getUcid(), Restriction.CREDIT_AND_BONUS.getName());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("337")
    @DisplayName("Restriction tab set Manual Withdrawal Review restriction UI")
    void setManualWithdrawalRestrictionUITest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        restrictionPage.navigate(restrictionClient.getUcid());
        restrictionPage.clickManualWithdrawalSwitch();
        restrictionPage.fillApplyReason("test reason");
        restrictionPage.checkThatManualWithdrawalIsChecked();
        checkKafkaRequestApplyUserId(restrictionClient.getUserId());
        checkRestrictionApplymentAuditGeneral(restrictionClient.getUcid(), MANUAL_WITHDRAWAL_REVIEW.getName());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("338")
    @DisplayName("Restriction tab remove Manual Withdrawal Review restriction UI client without transactions")
    void cancelManualWithdrawalRestrictionUITest() throws Exception {
        restrictionPage.setRestrictionAPIGeneral(restrictionClient.getUcid(), MANUAL_WITHDRAWAL_REVIEW.getCode());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        restrictionPage.navigate(restrictionClient.getUcid());
        restrictionPage.checkThatManualWithdrawalIsChecked();
        restrictionPage.clickCheckedManual();
        restrictionPage.fillCancelReason("test reason");
        restrictionPage.checkKafkaRequestCancelUcid(restrictionClient.getUserId());
        restrictionPage.checkRestrictionCancellationAuditBO(restrictionClient.getUcid(), MANUAL_WITHDRAWAL_REVIEW.getName());
    }

}
