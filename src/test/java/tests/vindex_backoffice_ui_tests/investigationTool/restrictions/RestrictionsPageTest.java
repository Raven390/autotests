package tests.vindex_backoffice_ui_tests.investigationTool.restrictions;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.*;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static business_objects.ui.user.UserFactory.autotestUserOne;
import static helpers.api.RestrictionHelper.setRestrictionAPIGeneral;
import static helpers.api.RestrictionHelper.setRestrictionAPITrade;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.Restriction.*;
import static helpers.database.AuHelper.cleanClientAudit;
import static helpers.database.CleanTableHelper.*;
import static helpers.database.DbHelper.insertObjectsToDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static page_objects.backoffice_pages.investigationTool.RestrictionPage.*;
import static utils.Constants.*;
import static utils.Utils.getCurrentTimestampMinusOffsetFormatted;
import static utils.Utils.insertCrmAccountsToDb;

import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.mt_account.MtAccountObject;
import business_objects.ui.user.User;
import helpers.data.ClientHelper;
import helpers.data.enums.DateTimeFormat;
import helpers.database.AuHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import java.util.List;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

@Tag(TEAM_BACKOFFICE)
@Tag(LAYER_WEB)
class RestrictionsPageTest extends TestBaseWeb {

    static ClientHelper restrictionClient = getRandomVantageClientAllFields();
    static ClientHelper labelClient = getRandomVantageClientAllFields();

    private static final String RESTRICTION_COMMENT = "test reason";
    private static final User user = autotestUserOne();

    @BeforeAll
    static void setup() {
        CrmTbUserObject restrictionClientDB = generateUserByClient(restrictionClient);
        CrmTbUserObject labelClientDB = generateUserByClient(labelClient);
        CrmTbAccountObject activeAccount = generateCrmTbAccountDataForUi(restrictionClient);
        MtAccountObject mtAccountActive = generateMtAccountByCrmTbAccount(activeAccount);
        CrmTbAccountObject activeAccount2 = generateCrmTbAccountDataForUi(labelClient);
        CrmTbAccountObject inactiveAccount = generateAdditionalCrmTbAccountDataForUi(labelClient);
        inactiveAccount.accountStatus = ACCOUNT_STATUS_INACTIVE;
        MtAccountObject mtAccountActive2 = generateMtAccountByCrmTbAccount(activeAccount2);
        MtAccountObject mtAccountInactive = generateMtAccountByCrmTbAccount(inactiveAccount);
        mtAccountInactive.lastLogin =
                getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE_AND_TIME, 0, 1, 0, 0, 0);

        insertObjectsToDb(MT_ACCOUNT_TABLE_NAME, List.of(mtAccountActive, mtAccountActive2, mtAccountInactive));
        insertObjectsToDb(CRM_USER_TABLE_NAME, List.of(restrictionClientDB, labelClientDB));
        insertCrmAccountsToDb(activeAccount, activeAccount2, inactiveAccount);
    }

    @BeforeEach
    void before() throws Exception {
        cleanUserRestrictionGeneral(restrictionClient.getUcid());
        cleanUserRestrictionTrading(restrictionClient.getUcid());
        cleanClientAudit(restrictionClient.getUcid());
    }

    @AfterAll
    static void teardown() throws Exception {
        cleanUserRestrictionGeneral(restrictionClient.getUcid());
        cleanUserRestrictionTrading(restrictionClient.getUcid());
        cleanClientAudit(restrictionClient.getUcid());
        cleanCrmUserTableByClient(restrictionClient.getUcid());
    }

    @Test
    @AllureId("327")
    @DisplayName("Restriction tab set account restriction UI")
    void setAccountRestrictionUITest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        restrictionPage.navigate(restrictionClient.getUcid());
        restrictionPage.addNewRestriction(ACCOUNT_CREATION, RESTRICTION_COMMENT);
        restrictionPage.verifyRestrictionAppliedInUi(ACCOUNT_CREATION, user, RESTRICTION_COMMENT);
        checkKafkaRequestApplyUserId(restrictionClient.getUserId());
        AuHelper.checkRestrictionApplyAudit(restrictionClient.getUcid());
    }

    @Test
    @AllureId("328")
    @DisplayName("Restriction tab remove account restriction UI")
    void cancelAccountRestrictionUITest() throws Exception {
        setRestrictionAPIGeneral(restrictionClient.getUcid(), ACCOUNT_CREATION.getCode());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        restrictionPage.navigate(restrictionClient.getUcid());
        restrictionPage.removeRestriction(ACCOUNT_CREATION, RESTRICTION_COMMENT);
        restrictionPage.verifyRestrictionNotAppliedInUi(ACCOUNT_CREATION);
        restrictionPage.checkKafkaRequestCancelUcid(restrictionClient.getUserId());
        AuHelper.checkRestrictionCancelAudit(restrictionClient.getUcid());
    }

    @Test
    @AllureId("329")
    @DisplayName("Restriction tab set transfer restriction UI")
    void setTransferRestrictionUITest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        restrictionPage.navigate(restrictionClient.getUcid());
        restrictionPage.addNewRestriction(INTERNAL_TRANSFER, RESTRICTION_COMMENT);
        restrictionPage.verifyRestrictionAppliedInUi(INTERNAL_TRANSFER, user, RESTRICTION_COMMENT);
        checkKafkaRequestApplyUserId(restrictionClient.getUserId());
        AuHelper.checkRestrictionApplyAudit(restrictionClient.getUcid());
    }

    @Test
    @AllureId("330")
    @DisplayName("Restriction tab remove Transfer Restriction UI")
    void cancelTransferRestrictionUITest() throws Exception {
        setRestrictionAPIGeneral(restrictionClient.getUcid(), INTERNAL_TRANSFER.getCode());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        restrictionPage.navigate(restrictionClient.getUcid());
        restrictionPage.removeRestriction(INTERNAL_TRANSFER, RESTRICTION_COMMENT);
        restrictionPage.verifyRestrictionNotAppliedInUi(INTERNAL_TRANSFER);
        restrictionPage.checkKafkaRequestCancelUcid(restrictionClient.getUserId());
        AuHelper.checkRestrictionCancelAudit(restrictionClient.getUcid());
    }

    @Test
    @AllureId("331")
    @DisplayName("Restriction tab set Deposits restriction UI")
    void setDepositsRestrictionUITest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        restrictionPage.navigate(restrictionClient.getUcid());
        restrictionPage.addNewRestriction(DEPOSITS, RESTRICTION_COMMENT);
        restrictionPage.verifyRestrictionAppliedInUi(DEPOSITS, user, RESTRICTION_COMMENT);
        checkKafkaRequestApplyUserId(restrictionClient.getUserId());
        AuHelper.checkRestrictionApplyAudit(restrictionClient.getUcid());
    }

    @Test
    @AllureId("332")
    @DisplayName("Restriction tab remove Deposits restriction UI")
    void cancelDepositsRestrictionUITest() throws Exception {
        setRestrictionAPIGeneral(restrictionClient.getUcid(), DEPOSITS.getCode());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        restrictionPage.navigate(restrictionClient.getUcid());
        restrictionPage.removeRestriction(DEPOSITS, RESTRICTION_COMMENT);
        restrictionPage.verifyRestrictionNotAppliedInUi(DEPOSITS);
        restrictionPage.checkKafkaRequestCancelUcid(restrictionClient.getUserId());
        AuHelper.checkRestrictionCancelAudit(restrictionClient.getUcid());
    }

    @Test
    @AllureId("333")
    @DisplayName("Restriction tab set Withdrawals restriction UI")
    void setWithdrawalsRestrictionUITest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        restrictionPage.navigate(restrictionClient.getUcid());
        restrictionPage.addNewRestriction(WITHDRAWALS, RESTRICTION_COMMENT);
        restrictionPage.verifyRestrictionAppliedInUi(WITHDRAWALS, user, RESTRICTION_COMMENT);
        checkKafkaRequestApplyUserId(restrictionClient.getUserId());
        AuHelper.checkRestrictionApplyAudit(restrictionClient.getUcid());
    }

    @Test
    @AllureId("334")
    @DisplayName("Restriction tab remove Withdrawals restriction UI")
    void cancelWithdrawalsRestrictionUITest() throws Exception {
        setRestrictionAPIGeneral(restrictionClient.getUcid(), WITHDRAWALS.getCode());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        restrictionPage.navigate(restrictionClient.getUcid());
        restrictionPage.removeRestriction(WITHDRAWALS, RESTRICTION_COMMENT);
        restrictionPage.verifyRestrictionNotAppliedInUi(WITHDRAWALS);
        restrictionPage.checkKafkaRequestCancelUcid(restrictionClient.getUserId());
        AuHelper.checkRestrictionCancelAudit(restrictionClient.getUcid());
    }

    @Test
    @AllureId("335")
    @DisplayName("Restriction tab set Login CRM restriction UI")
    void setLoginCRMRestrictionUITest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        restrictionPage.navigate(restrictionClient.getUcid());
        restrictionPage.addNewRestriction(LOGIN_CRM, RESTRICTION_COMMENT);
        restrictionPage.verifyRestrictionAppliedInUi(LOGIN_CRM, user, RESTRICTION_COMMENT);
        checkKafkaRequestApplyUserId(restrictionClient.getUserId());
        AuHelper.checkRestrictionApplyAudit(restrictionClient.getUcid());
    }

    @Test
    @AllureId("336")
    @DisplayName("Restriction tab remove Login CRM restriction UI")
    void cancelLoginCRMRestrictionUITest() throws Exception {
        setRestrictionAPIGeneral(restrictionClient.getUcid(), LOGIN_CRM.getCode());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        restrictionPage.navigate(restrictionClient.getUcid());
        restrictionPage.removeRestriction(LOGIN_CRM, RESTRICTION_COMMENT);
        restrictionPage.verifyRestrictionNotAppliedInUi(LOGIN_CRM);
        restrictionPage.checkKafkaRequestCancelUcid(restrictionClient.getUserId());
        AuHelper.checkRestrictionCancelAudit(restrictionClient.getUcid());
    }

    @Test
    @AllureId("742")
    @DisplayName("Restriction tab set Credit and Bonus Review restriction UI")
    void setCreditAndBonusRestrictionUITest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        restrictionPage.navigate(restrictionClient.getUcid());
        restrictionPage.addNewRestriction(CREDIT_AND_BONUS, RESTRICTION_COMMENT);
        restrictionPage.verifyRestrictionAppliedInUi(CREDIT_AND_BONUS, user, RESTRICTION_COMMENT);
        checkKafkaRequestApplyUserId(restrictionClient.getUserId());
        AuHelper.checkRestrictionApplyAudit(restrictionClient.getUcid());
    }

    @Test
    @AllureId("743")
    @DisplayName("Restriction tab remove Credit and Bonus restriction UI client without transactions")
    void cancelCreditAndBonusRestrictionUITest() throws Exception {
        setRestrictionAPIGeneral(restrictionClient.getUcid(), CREDIT_AND_BONUS.getCode());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        restrictionPage.navigate(restrictionClient.getUcid());
        restrictionPage.removeRestriction(CREDIT_AND_BONUS, RESTRICTION_COMMENT);
        restrictionPage.verifyRestrictionNotAppliedInUi(CREDIT_AND_BONUS);
        restrictionPage.checkKafkaRequestCancelUcid(restrictionClient.getUserId());
        AuHelper.checkRestrictionCancelAudit(restrictionClient.getUcid());
    }

    @Test
    @AllureId("337")
    @DisplayName("Restriction tab set Manual Withdrawal Review restriction UI")
    void setManualWithdrawalRestrictionUITest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        restrictionPage.navigate(restrictionClient.getUcid());
        restrictionPage.addNewRestriction(MANUAL_WITHDRAWAL_REVIEW, RESTRICTION_COMMENT);
        restrictionPage.verifyRestrictionAppliedInUi(MANUAL_WITHDRAWAL_REVIEW, user, RESTRICTION_COMMENT);
        checkKafkaRequestApplyUserId(restrictionClient.getUserId());
        AuHelper.checkRestrictionApplyAudit(restrictionClient.getUcid());
    }

    @Test
    @AllureId("338")
    @DisplayName("Restriction tab remove Manual Withdrawal Review restriction UI")
    void cancelManualWithdrawalRestrictionUITest() throws Exception {
        setRestrictionAPIGeneral(restrictionClient.getUcid(), MANUAL_WITHDRAWAL_REVIEW.getCode());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        restrictionPage.navigate(restrictionClient.getUcid());
        restrictionPage.removeRestriction(MANUAL_WITHDRAWAL_REVIEW, RESTRICTION_COMMENT);
        restrictionPage.verifyRestrictionNotAppliedInUi(MANUAL_WITHDRAWAL_REVIEW);
        restrictionPage.checkKafkaRequestCancelUcid(restrictionClient.getUserId());
        AuHelper.checkRestrictionCancelAudit(restrictionClient.getUcid());
    }

    @Test
    @AllureId("1158")
    @DisplayName("Restriction tab remove Note for withdrawals restriction UI")
    void cancelNoteForWithdrawalsRestrictionUITest() throws Exception {
        setRestrictionAPIGeneral(restrictionClient.getUcid(), NOTE_FOR_WITHDRAWALS.getCode());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        restrictionPage.navigate(restrictionClient.getUcid());
        restrictionPage.removeRestriction(NOTE_FOR_WITHDRAWALS, RESTRICTION_COMMENT);
        restrictionPage.verifyRestrictionNotAppliedInUi(NOTE_FOR_WITHDRAWALS);
        restrictionPage.checkKafkaRequestCancelUcid(restrictionClient.getUserId());
        AuHelper.checkRestrictionCancelAudit(restrictionClient.getUcid());
    }

    @Test
    @AllureId("339")
    @DisplayName("Restriction tab set Close only mode Review restriction UI")
    void setCloseOnlyModeRestrictionUITest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        restrictionPage.navigate(restrictionClient.getUcid());
        restrictionPage.addNewRestriction(CLOSE_ONLY_MODE, RESTRICTION_COMMENT);
        restrictionPage.verifyRestrictionAppliedInUi(CLOSE_ONLY_MODE, user, RESTRICTION_COMMENT);
        restrictionPage.checkKafkaRequestApplyAccount(restrictionClient.getTradingAccount());
        AuHelper.checkRestrictionApplyAudit(restrictionClient.getUcid());
    }

    @Test
    @AllureId("340")
    @DisplayName("Restriction tab remove Close only mode restriction UI")
    void cancelCloseOnlyModeRestrictionUITest() throws Exception {
        setRestrictionAPITrade(
                restrictionClient.getUcid(),
                restrictionClient.getTradingAccount(),
                restrictionClient.getServerId(),
                CLOSE_ONLY_MODE.getCode());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        restrictionPage.navigate(restrictionClient.getUcid());
        restrictionPage.removeRestriction(CLOSE_ONLY_MODE, RESTRICTION_COMMENT);
        restrictionPage.verifyRestrictionNotAppliedInUi(CLOSE_ONLY_MODE);
        restrictionPage.checkKafkaRequestCancelAccount(restrictionClient.getTradingAccount());
        AuHelper.checkRestrictionCancelAudit(restrictionClient.getUcid());
    }

    @Test
    @AllureId("341")
    @DisplayName("Restriction tab set Off quotes Review restriction UI")
    void setOffQuotesRestrictionUITest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        restrictionPage.navigate(restrictionClient.getUcid());
        restrictionPage.addNewRestriction(OFF_QUOTES, RESTRICTION_COMMENT);
        restrictionPage.verifyRestrictionAppliedInUi(OFF_QUOTES, user, RESTRICTION_COMMENT);
        restrictionPage.checkKafkaRequestApplyAccount(restrictionClient.getTradingAccount());
        AuHelper.checkRestrictionApplyAudit(restrictionClient.getUcid());
    }

    @Test
    @AllureId("342")
    @DisplayName("Restriction tab remove Off quotes restriction UI")
    void cancelOffQuotesRestrictionUITest() throws Exception {
        setRestrictionAPITrade(
                restrictionClient.getUcid(),
                restrictionClient.getTradingAccount(),
                restrictionClient.getServerId(),
                OFF_QUOTES.getCode());
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        restrictionPage.navigate(restrictionClient.getUcid());
        restrictionPage.removeRestriction(OFF_QUOTES, RESTRICTION_COMMENT);
        restrictionPage.verifyRestrictionNotAppliedInUi(OFF_QUOTES);
        restrictionPage.checkKafkaRequestCancelAccount(restrictionClient.getTradingAccount());
        AuHelper.checkRestrictionCancelAudit(restrictionClient.getUcid());
    }

    @Test
    @AllureId("739")
    @DisplayName(
            "Restriction tab. indicator 'inactive' must be present on row with account with 'inactive' status in trade restriction applyment/removal menu")
    void inactiveAccountIndicatorTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        restrictionPage.navigate(labelClient.getUcid());
        restrictionPage.verifyInactiveLabelIsVisible(CLOSE_ONLY_MODE);
    }

    @Test
    @AllureId("738")
    @DisplayName(
            "Restriction tab. Popup with tip about last active date must appear on hover to last activity date in trade restriction applyment/removal menu")
    void activityTooltipTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        restrictionPage.navigate(labelClient.getUcid());
        assertThat(
                "Verify popup text for last activity of an account",
                restrictionPage.getLastActivityTooltip(CLOSE_ONLY_MODE),
                is("Last activity"));
    }

    @Test
    @AllureId("919")
    @Feature("BMS-755 Add new restrictions")
    @DisplayName("Restriction tab. check that only restrictions with bo_visibility == true is displayed")
    void displayedRestrictionsUiTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        restrictionPage.navigate(labelClient.getUcid());
        assertThat(
                "Verify only restrictions with bo_visibility = true are displayed",
                restrictionPage.getDisplayedRestrictionsList(),
                containsInAnyOrder(getVisibleRestrictionsList().toArray()));
    }
}
