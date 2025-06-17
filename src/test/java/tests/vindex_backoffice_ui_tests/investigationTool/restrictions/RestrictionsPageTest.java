package tests.vindex_backoffice_ui_tests.investigationTool.restrictions;

import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.mtAccount.MtAccountObject;
import business_objects.ui.user.User;
import helpers.data.ClientHelper;
import helpers.data.enums.Brand;
import helpers.data.enums.Regulator;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import page_objects.backoffice_pages.investigationTool.RestrictionPage;
import tests.TestBaseWeb;


import java.util.List;

import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateStaticCrmTbAccountActive;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateStaticCrmTbAccountInactive;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateStaticUserByClient;
import static business_objects.db.clickhouse.mtAccount.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static business_objects.ui.user.UserFactory.autotestUserOne;
import static helpers.data.enums.Restriction.*;
import static helpers.database.DbHelper.insertObjectsToDb;
import static helpers.database.CleanTableHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static page_objects.backoffice_pages.investigationTool.RestrictionPage.checkKafkaRequestApplyUserId;
import static page_objects.backoffice_pages.investigationTool.RestrictionPage.checkRestrictionApplymentAuditGeneral;
import static utils.Constants.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RestrictionsPageTest extends TestBaseWeb {

    static ClientHelper restrictionClient = new ClientHelper(141_401, "063cde3b-ea9d-48b5-8e2c-99f3d5f67999", Brand.VANTAGE, Regulator.VFSC2, 14_140_101, 42);
    static ClientHelper labelClient = new ClientHelper(141_403, "063cde3b-ea9d-48b5-8e2c-99f3d5f67999", Brand.VANTAGE, Regulator.VFSC2, 14_140_103, 42);

    private static final String RESTRICTION_COMMENT = "test reason";
    private static final User user = autotestUserOne();

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
        cleanUserRestrictionTrading(restrictionClient.getUcid());
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
        restrictionPage.addNewRestriction(ACCOUNT_CREATION, RESTRICTION_COMMENT);
        restrictionPage.verifyRestrictionAppliedInUi(ACCOUNT_CREATION, user, RESTRICTION_COMMENT);
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
        restrictionPage.removeRestriction(ACCOUNT_CREATION, RESTRICTION_COMMENT);
        restrictionPage.verifyRestrictionNotAppliedInUi(ACCOUNT_CREATION);
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
        restrictionPage.addNewRestriction(INTERNAL_TRANSFER, RESTRICTION_COMMENT);
        restrictionPage.verifyRestrictionAppliedInUi(INTERNAL_TRANSFER, user, RESTRICTION_COMMENT);
        checkKafkaRequestApplyUserId(restrictionClient.getUserId());
        checkRestrictionApplymentAuditGeneral(restrictionClient.getUcid(), INTERNAL_TRANSFER.getName());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("330")
    @DisplayName("Restriction tab remove Transfer Restriction UI")
    void cancelTransferRestrictionUITest() throws Exception {
        RestrictionPage.setRestrictionAPIGeneral(restrictionClient.getUcid(), INTERNAL_TRANSFER.getCode());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        restrictionPage.navigate(restrictionClient.getUcid());
        restrictionPage.removeRestriction(INTERNAL_TRANSFER, RESTRICTION_COMMENT);
        restrictionPage.verifyRestrictionNotAppliedInUi(INTERNAL_TRANSFER);
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
        restrictionPage.addNewRestriction(DEPOSITS, RESTRICTION_COMMENT);
        restrictionPage.verifyRestrictionAppliedInUi(DEPOSITS, user, RESTRICTION_COMMENT);
        checkKafkaRequestApplyUserId(restrictionClient.getUserId());
        checkRestrictionApplymentAuditGeneral(restrictionClient.getUcid(), DEPOSITS.getName());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("332")
    @DisplayName("Restriction tab remove Deposits restriction UI")
    void cancelDepositsRestrictionUITest() throws Exception {
        RestrictionPage.setRestrictionAPIGeneral(restrictionClient.getUcid(), DEPOSITS.getCode());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        restrictionPage.navigate(restrictionClient.getUcid());
        restrictionPage.removeRestriction(DEPOSITS, RESTRICTION_COMMENT);
        restrictionPage.verifyRestrictionNotAppliedInUi(DEPOSITS);
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
        restrictionPage.addNewRestriction(WITHDRAWALS, RESTRICTION_COMMENT);
        restrictionPage.verifyRestrictionAppliedInUi(WITHDRAWALS, user, RESTRICTION_COMMENT);
        checkKafkaRequestApplyUserId(restrictionClient.getUserId());
        checkRestrictionApplymentAuditGeneral(restrictionClient.getUcid(), WITHDRAWALS.getName());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("334")
    @DisplayName("Restriction tab remove Withdrawals restriction UI")
    void cancelWithdrawalsRestrictionUITest() throws Exception {
        RestrictionPage.setRestrictionAPIGeneral(restrictionClient.getUcid(), WITHDRAWALS.getCode());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        restrictionPage.navigate(restrictionClient.getUcid());
        restrictionPage.removeRestriction(WITHDRAWALS, RESTRICTION_COMMENT);
        restrictionPage.verifyRestrictionNotAppliedInUi(WITHDRAWALS);
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
        restrictionPage.addNewRestriction(LOGIN_CRM, RESTRICTION_COMMENT);
        restrictionPage.verifyRestrictionAppliedInUi(LOGIN_CRM, user, RESTRICTION_COMMENT);
        checkKafkaRequestApplyUserId(restrictionClient.getUserId());
        checkRestrictionApplymentAuditGeneral(restrictionClient.getUcid(), LOGIN_CRM.getName());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("336")
    @DisplayName("Restriction tab remove Login CRM restriction UI")
    void cancelLoginCRMRestrictionUITest() throws Exception {
        RestrictionPage.setRestrictionAPIGeneral(restrictionClient.getUcid(), LOGIN_CRM.getCode());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        restrictionPage.navigate(restrictionClient.getUcid());
        restrictionPage.removeRestriction(LOGIN_CRM, RESTRICTION_COMMENT);
        restrictionPage.verifyRestrictionNotAppliedInUi(LOGIN_CRM);
        restrictionPage.checkKafkaRequestCancelUcid(restrictionClient.getUserId());
        restrictionPage.checkRestrictionCancellationAuditBO(restrictionClient.getUcid(), LOGIN_CRM.getName());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("742")
    @DisplayName("Restriction tab set Credit and Bonus Review restriction UI")
    void setCreditAndBonusRestrictionUITest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        restrictionPage.navigate(restrictionClient.getUcid());
        restrictionPage.addNewRestriction(CREDIT_AND_BONUS, RESTRICTION_COMMENT);
        restrictionPage.verifyRestrictionAppliedInUi(CREDIT_AND_BONUS, user, RESTRICTION_COMMENT);
        checkKafkaRequestApplyUserId(restrictionClient.getUserId());
        checkRestrictionApplymentAuditGeneral(restrictionClient.getUcid(), CREDIT_AND_BONUS.getName());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("743")
    @DisplayName("Restriction tab remove Credit and Bonus restriction UI client without transactions")
    void cancelCreditAndBonusRestrictionUITest() throws Exception {
        RestrictionPage.setRestrictionAPIGeneral(restrictionClient.getUcid(), CREDIT_AND_BONUS.getCode());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        restrictionPage.navigate(restrictionClient.getUcid());
        restrictionPage.removeRestriction(CREDIT_AND_BONUS, RESTRICTION_COMMENT);
        restrictionPage.verifyRestrictionNotAppliedInUi(CREDIT_AND_BONUS);
        restrictionPage.checkKafkaRequestCancelUcid(restrictionClient.getUserId());
        restrictionPage.checkRestrictionCancellationAuditBO(restrictionClient.getUcid(), CREDIT_AND_BONUS.getName());
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
        restrictionPage.addNewRestriction(MANUAL_WITHDRAWAL_REVIEW, RESTRICTION_COMMENT);
        restrictionPage.verifyRestrictionAppliedInUi(MANUAL_WITHDRAWAL_REVIEW, user, RESTRICTION_COMMENT);
        checkKafkaRequestApplyUserId(restrictionClient.getUserId());
        checkRestrictionApplymentAuditGeneral(restrictionClient.getUcid(), MANUAL_WITHDRAWAL_REVIEW.getName());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("338")
    @DisplayName("Restriction tab remove Manual Withdrawal Review restriction UI")
    void cancelManualWithdrawalRestrictionUITest() throws Exception {
        RestrictionPage.setRestrictionAPIGeneral(restrictionClient.getUcid(), MANUAL_WITHDRAWAL_REVIEW.getCode());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        restrictionPage.navigate(restrictionClient.getUcid());
        restrictionPage.removeRestriction(MANUAL_WITHDRAWAL_REVIEW, RESTRICTION_COMMENT);
        restrictionPage.verifyRestrictionNotAppliedInUi(MANUAL_WITHDRAWAL_REVIEW);
        restrictionPage.checkKafkaRequestCancelUcid(restrictionClient.getUserId());
        restrictionPage.checkRestrictionCancellationAuditBO(restrictionClient.getUcid(), MANUAL_WITHDRAWAL_REVIEW.getName());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1158")
    @DisplayName("Restriction tab remove Note for withdrawals restriction UI")
    void cancelNoteForWithdrawalsRestrictionUITest() throws Exception {
        RestrictionPage.setRestrictionAPIGeneral(restrictionClient.getUcid(), NOTE_FOR_WITHDRAWALS.getCode());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        restrictionPage.navigate(restrictionClient.getUcid());
        restrictionPage.removeRestriction(NOTE_FOR_WITHDRAWALS, RESTRICTION_COMMENT);
        restrictionPage.verifyRestrictionNotAppliedInUi(NOTE_FOR_WITHDRAWALS);
        restrictionPage.checkKafkaRequestCancelUcid(restrictionClient.getUserId());
        restrictionPage.checkRestrictionCancellationAuditBO(restrictionClient.getUcid(), NOTE_FOR_WITHDRAWALS.getName());
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
        restrictionPage.addNewRestriction(CLOSE_ONLY_MODE, RESTRICTION_COMMENT);
        restrictionPage.verifyRestrictionAppliedInUi(CLOSE_ONLY_MODE, user, RESTRICTION_COMMENT);
        restrictionPage.checkKafkaRequestApplyAccount(restrictionClient.getTradingAccount());
        checkRestrictionApplymentAuditGeneral(restrictionClient.getUcid(), String.format("%s; account: %s", CLOSE_ONLY_MODE.getName(), restrictionClient.getTradingAccount()));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("340")
    @DisplayName("Restriction tab remove Close only mode restriction UI")
    void cancelCloseOnlyModeRestrictionUITest() throws Exception {
        restrictionPage.setRestrictionAPITrade(restrictionClient.getUcid(), restrictionClient.getTradingAccount(), restrictionClient.getServerId(), CLOSE_ONLY_MODE.getCode());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        restrictionPage.navigate(restrictionClient.getUcid());
        restrictionPage.removeRestriction(CLOSE_ONLY_MODE, RESTRICTION_COMMENT);
        restrictionPage.verifyRestrictionNotAppliedInUi(CLOSE_ONLY_MODE);
        restrictionPage.checkKafkaRequestCancelAccount(restrictionClient.getTradingAccount());
        restrictionPage.checkRestrictionCancellationAuditBO(restrictionClient.getUcid(), String.format("%s; account: %s", CLOSE_ONLY_MODE.getName(), restrictionClient.getTradingAccount()));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("341")
    @DisplayName("Restriction tab set Off quotes Review restriction UI")
    void setOffQuotesRestrictionUITest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        restrictionPage.navigate(restrictionClient.getUcid());
        restrictionPage.addNewRestriction(OFF_QUOTES, RESTRICTION_COMMENT);
        restrictionPage.verifyRestrictionAppliedInUi(OFF_QUOTES, user, RESTRICTION_COMMENT);
        restrictionPage.checkKafkaRequestApplyAccount(restrictionClient.getTradingAccount());
        checkRestrictionApplymentAuditGeneral(restrictionClient.getUcid(), String.format("%s; account: %s", OFF_QUOTES.getName(), restrictionClient.getTradingAccount()));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("342")
    @DisplayName("Restriction tab remove Off quotes restriction UI")
    void cancelOffQuotesRestrictionUITest() throws Exception {
        restrictionPage.setRestrictionAPITrade(restrictionClient.getUcid(), restrictionClient.getTradingAccount(), restrictionClient.getServerId(), OFF_QUOTES.getCode());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        restrictionPage.navigate(restrictionClient.getUcid());
        restrictionPage.removeRestriction(OFF_QUOTES, RESTRICTION_COMMENT);
        restrictionPage.verifyRestrictionNotAppliedInUi(OFF_QUOTES);
        restrictionPage.checkKafkaRequestCancelAccount(restrictionClient.getTradingAccount());
        restrictionPage.checkRestrictionCancellationAuditBO(restrictionClient.getUcid(), String.format("%s; account: %s", OFF_QUOTES.getName(), restrictionClient.getTradingAccount()));
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
        restrictionPage.verifyInactiveLabelIsVisible(CLOSE_ONLY_MODE);
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
        assertThat("Verify popup text for last activity of an account", restrictionPage.getLastActivityTooltip(CLOSE_ONLY_MODE), is("Last activity"));
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
        assertThat("Verify only restrictions with bo_visibility = true are displayed", restrictionPage.getDisplayedRestrictionsList(), containsInAnyOrder(getVisibleRestrictionsList().toArray()));
    }
}
