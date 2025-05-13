package tests.mitigation_service_api_tests;

import business_objects.api.mitigation_service.*;
import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.kafka.restriction_events.ClientRestrictionApply;
import helpers.data.ClientHelper;
import helpers.data.enums.Brand;
import helpers.data.enums.Regulator;
import helpers.data.enums.Restriction;
import helpers.database.CleanTableHelper;
import helpers.kafka.KafkaHelper;
import io.qameta.allure.*;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import page_objects.backoffice_pages.RestrictionPage;
import tests.TestBaseApi;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static business_objects.api.mitigation_service.MitigationServiceRequest.*;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateStaticCrmTbAccountActive;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateStaticUserByClient;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;
import static utils.Constants.*;
import static utils.Utils.getCurrentTimestamp;

@Tag(TEAM_BACKOFFICE)
@Tag(LAYER_API)
@Tag(SUITE_MITIGATION_SERVICE)
class MitigationServiceApiTest extends TestBaseApi {

    static ClientHelper restrictionClient = new ClientHelper(141_401, "063cde3b-ea9d-48b5-8e2c-99f3d5f67999", Brand.VANTAGE, Regulator.VFSC2, 14_140_101, 42);

    @BeforeAll
    static void initialSetup() throws IOException {
        Response response = enableCRMEmulator();
        assertNotNull(response);
        CrmTbUserObject restrictionClientDB = generateStaticUserByClient(restrictionClient);
        CrmTbAccountObject active = generateStaticCrmTbAccountActive(restrictionClient);
        insertObjectToDb(CRM_USER_TABLE_NAME, restrictionClientDB);
        insertObjectToDb(CRM_ACCOUNT_TABLE_NAME, active);
    }

    @BeforeEach
    void before() throws Exception {
        CleanTableHelper.cleanUserRestrictionGeneral(restrictionClient.getUcid());
        CleanTableHelper.cleanUserRestrictionTrading(restrictionClient.getUcid());
        CleanTableHelper.cleanUserAudit(restrictionClient.getUcid());
    }

    @Test
    @DisplayName("Restriction Set PUT request returns 200 if restriction already applied")
    @AllureId("920")
    @Owner("DMITRI KALACHEV")
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_API)
    void successIfRestrictionAlreadyApplied() throws Exception {

        PostRestrictionRequestBody postRestrictionRequestBody = new PostRestrictionRequestBody(
                restrictionClient.getUcid(), "05", "GENERAL", null, null, "Integration test", new PostRestrictionRequestBody.UpdatedBy("API", "QA")
        );

        Allure.step("Send and check first request");
        Response response = postRestriction(postRestrictionRequestBody);
        assertEquals(200, response.code());

        Allure.step("Send and check second request");
        Response response2 = putRestriction(postRestrictionRequestBody);

        assertEquals(response2.code(), 200);

    }

    @Test
    @DisplayName("Set restriction 'Open new account' API")
    @Feature("BMS-755 Add new restrictions")
    @Owner("DMITRI KALACHEV")
    @AllureId("922")
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_API)
    void setRestrictionApiOpenNewAccountTest() throws Exception {
        Restriction restriction = Restriction.ACCOUNT_CREATION_REVIEW;
        String applyReason = "reason" + getCurrentTimestamp();
        String updatedBySystem = "system" + getCurrentTimestamp();
        String updatedByUser = "user" + getCurrentTimestamp();
        String restrictionIdRaw = RestrictionPage.setRestrictionAPIGeneralResponse(restrictionClient.getUcid(), restriction.getCode(), applyReason, updatedBySystem, updatedByUser);
        int restrictionID = Integer.parseInt(restrictionIdRaw.split(":")[1].replace("}", ""));
        RestrictionPage.checkRestrictionApplymentAuditGeneral(restrictionClient.getUcid(), updatedBySystem, updatedByUser, applyReason, restriction.getName());
        RestrictionPage.checkKafkaRequestApplyUserId(restrictionClient.getUserId());
        RestrictionPage.checkUserHaveRestrictionGeneral(restrictionClient.getUcid(), restrictionID, "APPLIED");
    }

    @Test
    @DisplayName("Set restriction 'Internal transfer' API")
    @Feature("BMS-755 Add new restrictions")
    @Owner("DMITRI KALACHEV")
    @AllureId("923")
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_API)
    void setRestrictionApiInternalTransferTest() throws Exception {
        Restriction restriction = Restriction.INTERNAL_TRANSFER;
        String applyReason = "reason" + getCurrentTimestamp();
        String updatedBySystem = "system" + getCurrentTimestamp();
        String updatedByUser = "user" + getCurrentTimestamp();
        String restrictionIdRaw = RestrictionPage.setRestrictionAPIGeneralResponse(restrictionClient.getUcid(), restriction.getCode(), applyReason, updatedBySystem, updatedByUser);
        int restrictionID = Integer.parseInt(restrictionIdRaw.split(":")[1].replace("}", ""));
        RestrictionPage.checkRestrictionApplymentAuditGeneral(restrictionClient.getUcid(), updatedBySystem, updatedByUser, applyReason, restriction.getName());
        RestrictionPage.checkKafkaRequestApplyUserId(restrictionClient.getUserId());
        RestrictionPage.checkUserHaveRestrictionGeneral(restrictionClient.getUcid(), restrictionID, "APPLIED");
    }

    @Test
    @DisplayName("Set restriction 'Deposits' API")
    @Feature("BMS-755 Add new restrictions")
    @Owner("DMITRI KALACHEV")
    @AllureId("924")
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_API)
    void setRestrictionApiDepositsTest() throws Exception {
        Restriction restriction = Restriction.DEPOSITS;
        String applyReason = "reason" + getCurrentTimestamp();
        String updatedBySystem = "system" + getCurrentTimestamp();
        String updatedByUser = "user" + getCurrentTimestamp();
        String restrictionIdRaw = RestrictionPage.setRestrictionAPIGeneralResponse(restrictionClient.getUcid(), restriction.getCode(), applyReason, updatedBySystem, updatedByUser);
        int restrictionID = Integer.parseInt(restrictionIdRaw.split(":")[1].replace("}", ""));
        RestrictionPage.checkRestrictionApplymentAuditGeneral(restrictionClient.getUcid(), updatedBySystem, updatedByUser, applyReason, restriction.getName());
        RestrictionPage.checkKafkaRequestApplyUserId(restrictionClient.getUserId());
        RestrictionPage.checkUserHaveRestrictionGeneral(restrictionClient.getUcid(), restrictionID, "APPLIED");
    }

    @Test
    @DisplayName("Set restriction 'Withdrawals' API")
    @Feature("BMS-755 Add new restrictions")
    @Owner("DMITRI KALACHEV")
    @AllureId("925")
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_API)
    void setRestrictionApiWithdrawalsTest() throws Exception {
        Restriction restriction = Restriction.WITHDRAWALS;
        String applyReason = "reason" + getCurrentTimestamp();
        String updatedBySystem = "system" + getCurrentTimestamp();
        String updatedByUser = "user" + getCurrentTimestamp();
        String restrictionIdRaw = RestrictionPage.setRestrictionAPIGeneralResponse(restrictionClient.getUcid(), restriction.getCode(), applyReason, updatedBySystem, updatedByUser);
        int restrictionID = Integer.parseInt(restrictionIdRaw.split(":")[1].replace("}", ""));
        RestrictionPage.checkRestrictionApplymentAuditGeneral(restrictionClient.getUcid(), updatedBySystem, updatedByUser, applyReason, restriction.getName());
        RestrictionPage.checkKafkaRequestApplyUserId(restrictionClient.getUserId());
        RestrictionPage.checkUserHaveRestrictionGeneral(restrictionClient.getUcid(), restrictionID, "APPLIED");
    }

    @Test
    @DisplayName("Set restriction 'Login CRM' API")
    @Feature("BMS-755 Add new restrictions")
    @Owner("DMITRI KALACHEV")
    @AllureId("925")
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_API)
    void setRestrictionApiLoginCrmTest() throws Exception {
        Restriction restriction = Restriction.LOGIN_CRM;
        String applyReason = "reason" + getCurrentTimestamp();
        String updatedBySystem = "system" + getCurrentTimestamp();
        String updatedByUser = "user" + getCurrentTimestamp();
        String restrictionIdRaw = RestrictionPage.setRestrictionAPIGeneralResponse(restrictionClient.getUcid(), restriction.getCode(), applyReason, updatedBySystem, updatedByUser);
        int restrictionID = Integer.parseInt(restrictionIdRaw.split(":")[1].replace("}", ""));
        RestrictionPage.checkRestrictionApplymentAuditGeneral(restrictionClient.getUcid(), updatedBySystem, updatedByUser, applyReason, restriction.getName());
        RestrictionPage.checkKafkaRequestApplyUserId(restrictionClient.getUserId());
        RestrictionPage.checkUserHaveRestrictionGeneral(restrictionClient.getUcid(), restrictionID, "APPLIED");
    }

    @Test
    @DisplayName("Set restriction 'Close only mode' API")
    @Feature("BMS-755 Add new restrictions")
    @Owner("DMITRI KALACHEV")
    @AllureId("926")
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_API)
    void setRestrictionApiCloseOnlyModeTest() throws Exception {
        Restriction restriction = Restriction.CLOSE_ONLY_MODE;
        String applyReason = "reason" + getCurrentTimestamp();
        String updatedBySystem = "system" + getCurrentTimestamp();
        String updatedByUser = "user" + getCurrentTimestamp();
        String restrictionIdRaw = RestrictionPage.setRestrictionAPITradeResponse(restrictionClient.getUcid(), restriction.getCode(), restrictionClient.getTradingAccount(), restrictionClient.getServerId(), applyReason, updatedBySystem, updatedByUser);
        int restrictionID = Integer.parseInt(restrictionIdRaw.split(":")[1].replace("}", ""));
        RestrictionPage.checkRestrictionApplymentAuditTrading(restrictionClient.getUcid(), updatedBySystem, updatedByUser, applyReason, restriction.getName(), restrictionClient.getTradingAccount());
        RestrictionPage.checkKafkaRequestApplyAccount(restrictionClient.getTradingAccount(), restrictionClient.getServerId(), 525_600, restrictionID, applyReason, restriction.getCode());
        RestrictionPage.checkUserHaveRestrictionTrading(restrictionClient.getUcid(), restrictionID);
    }

    @Test
    @DisplayName("Set restriction 'B-Book -> A-Book' API")
    @Feature("BMS-755 Add new restrictions")
    @Owner("DMITRI KALACHEV")
    @AllureId("926")
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_API)
    void setRestrictionApiBaBookTest() throws Exception {
        Restriction restriction = Restriction.B_BOOK_TO_A_BOOK;
        String applyReason = "reason" + getCurrentTimestamp();
        String updatedBySystem = "system" + getCurrentTimestamp();
        String updatedByUser = "user" + getCurrentTimestamp();
        String restrictionIdRaw = RestrictionPage.setRestrictionAPITradeResponse(restrictionClient.getUcid(), restriction.getCode(), restrictionClient.getTradingAccount(), restrictionClient.getServerId(), applyReason, updatedBySystem, updatedByUser);
        int restrictionID = Integer.parseInt(restrictionIdRaw.split(":")[1].replace("}", ""));
        RestrictionPage.checkRestrictionApplymentAuditTrading(restrictionClient.getUcid(), updatedBySystem, updatedByUser, applyReason, restriction.getName(), restrictionClient.getTradingAccount());
        RestrictionPage.checkKafkaRequestApplyAccount(restrictionClient.getTradingAccount(), restrictionClient.getServerId(), 525_600, restrictionID, applyReason, restriction.getCode());
        RestrictionPage.checkUserHaveRestrictionTrading(restrictionClient.getUcid(), restrictionID);
    }

    @Test
    @DisplayName("Set restriction 'Manual Withdrawal Review' API")
    @Feature("BMS-755 Add new restrictions")
    @Owner("DMITRI KALACHEV")
    @AllureId("927")
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_API)
    void setRestrictionApiManualWithdrawalReviewTest() throws Exception {
        Restriction restriction = Restriction.MANUAL_WITHDRAWAL_REVIEW;
        String applyReason = "reason" + getCurrentTimestamp();
        String updatedBySystem = "system" + getCurrentTimestamp();
        String updatedByUser = "user" + getCurrentTimestamp();
        String restrictionIdRaw = RestrictionPage.setRestrictionAPIGeneralResponse(restrictionClient.getUcid(), restriction.getCode(), applyReason, updatedBySystem, updatedByUser);
        int restrictionID = Integer.parseInt(restrictionIdRaw.split(":")[1].replace("}", ""));
        RestrictionPage.checkRestrictionApplymentAuditGeneral(restrictionClient.getUcid(), updatedBySystem, updatedByUser, applyReason, restriction.getName());
        RestrictionPage.checkKafkaRequestApplyUserId(restrictionClient.getUserId());
        RestrictionPage.checkUserHaveRestrictionGeneral(restrictionClient.getUcid(), restrictionID, "APPLIED");
    }

    @Test
    @DisplayName("Set restriction 'Credit and Bonus' API")
    @Feature("BMS-755 Add new restrictions")
    @Owner("DMITRI KALACHEV")
    @AllureId("928")
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_API)
    void setRestrictionApiCreditAndBonusTest() throws Exception {
        Restriction restriction = Restriction.CREDIT_AND_BONUS;
        String applyReason = "reason" + getCurrentTimestamp();
        String updatedBySystem = "system" + getCurrentTimestamp();
        String updatedByUser = "user" + getCurrentTimestamp();
        String restrictionIdRaw = RestrictionPage.setRestrictionAPIGeneralResponse(restrictionClient.getUcid(), restriction.getCode(), applyReason, updatedBySystem, updatedByUser);
        int restrictionID = Integer.parseInt(restrictionIdRaw.split(":")[1].replace("}", ""));
        RestrictionPage.checkRestrictionApplymentAuditGeneral(restrictionClient.getUcid(), updatedBySystem, updatedByUser, applyReason, restriction.getName());
        RestrictionPage.checkKafkaRequestApplyUserId(restrictionClient.getUserId());
        RestrictionPage.checkUserHaveRestrictionGeneral(restrictionClient.getUcid(), restrictionID, "APPLIED");
    }

    @Test
    @DisplayName("Set restriction 'Read-only mode' API")
    @Feature("BMS-755 Add new restrictions")
    @Owner("DMITRI KALACHEV")
    @AllureId("930")
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_API)
    void setRestrictionApiReadOnlyModeTest() throws Exception {
        Restriction restriction = Restriction.READ_ONLY_MODE;
        String applyReason = "reason" + getCurrentTimestamp();
        String updatedBySystem = "system" + getCurrentTimestamp();
        String updatedByUser = "user" + getCurrentTimestamp();
        String restrictionIdRaw = RestrictionPage.setRestrictionAPITradeResponse(restrictionClient.getUcid(), restriction.getCode(), restrictionClient.getTradingAccount(), restrictionClient.getServerId(), applyReason, updatedBySystem, updatedByUser);
        int restrictionID = Integer.parseInt(restrictionIdRaw.split(":")[1].replace("}", ""));
        RestrictionPage.checkRestrictionApplymentAuditTrading(restrictionClient.getUcid(), updatedBySystem, updatedByUser, applyReason, restriction.getName(), restrictionClient.getTradingAccount());
        RestrictionPage.checkKafkaRequestApplyAccount(restrictionClient.getTradingAccount(), restrictionClient.getServerId(), 525_600, restrictionID, applyReason, restriction.getCode());
        RestrictionPage.checkUserHaveRestrictionTrading(restrictionClient.getUcid(), restrictionID);
    }

    @Test
    @DisplayName("Set restriction 'Off quotes' API")
    @Feature("BMS-755 Add new restrictions")
    @Owner("DMITRI KALACHEV")
    @AllureId("931")
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_API)
    void setRestrictionApiOffQuotesTest() throws Exception {
        Restriction restriction = Restriction.OFF_QUOTES;
        String applyReason = "reason" + getCurrentTimestamp();
        String updatedBySystem = "system" + getCurrentTimestamp();
        String updatedByUser = "user" + getCurrentTimestamp();
        String restrictionIdRaw = RestrictionPage.setRestrictionAPITradeResponse(restrictionClient.getUcid(), restriction.getCode(), restrictionClient.getTradingAccount(), restrictionClient.getServerId(), applyReason, updatedBySystem, updatedByUser);
        int restrictionID = Integer.parseInt(restrictionIdRaw.split(":")[1].replace("}", ""));
        RestrictionPage.checkRestrictionApplymentAuditTrading(restrictionClient.getUcid(), updatedBySystem, updatedByUser, applyReason, restriction.getName(), restrictionClient.getTradingAccount());
        RestrictionPage.checkKafkaRequestApplyAccount(restrictionClient.getTradingAccount(), restrictionClient.getServerId(), 525_600, restrictionID, applyReason, restriction.getCode());
        RestrictionPage.checkUserHaveRestrictionTrading(restrictionClient.getUcid(), restrictionID);
    }

    @Test
    @DisplayName("Set restriction 'Trading hours' API")
    @Feature("BMS-755 Add new restrictions")
    @Owner("DMITRI KALACHEV")
    @AllureId("1148")
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_API)
    void setRestrictionApiTradingHoursTest() throws Exception {
        Restriction restriction = Restriction.TRADING_HOURS;
        String applyReason = "reason" + getCurrentTimestamp();
        String updatedBySystem = "system" + getCurrentTimestamp();
        String updatedByUser = "user" + getCurrentTimestamp();
        String restrictionIdRaw = RestrictionPage.setRestrictionAPITradeResponse(restrictionClient.getUcid(), restriction.getCode(), restrictionClient.getTradingAccount(), restrictionClient.getServerId(), applyReason, updatedBySystem, updatedByUser);
        int restrictionID = Integer.parseInt(restrictionIdRaw.split(":")[1].replace("}", ""));
        RestrictionPage.checkRestrictionApplymentAuditTrading(restrictionClient.getUcid(), updatedBySystem, updatedByUser, applyReason, restriction.getName(), restrictionClient.getTradingAccount());
        RestrictionPage.checkKafkaRequestApplyAccount(restrictionClient.getTradingAccount(), restrictionClient.getServerId(), 525_600, restrictionID, applyReason, restriction.getCode());
        RestrictionPage.checkUserHaveRestrictionTrading(restrictionClient.getUcid(), restrictionID);
    }

    @Test
    @DisplayName("Set restriction 'Login MT' API")
    @Feature("BMS-755 Add new restrictions")
    @Owner("DMITRI KALACHEV")
    @AllureId("932")
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_API)
    void setRestrictionApiLoginMTTest() throws Exception {
        Restriction restriction = Restriction.LOGIN_MT;
        String applyReason = "reason" + getCurrentTimestamp();
        String updatedBySystem = "system" + getCurrentTimestamp();
        String updatedByUser = "user" + getCurrentTimestamp();
        String restrictionIdRaw = RestrictionPage.setRestrictionAPITradeResponse(restrictionClient.getUcid(), restriction.getCode(), restrictionClient.getTradingAccount(), restrictionClient.getServerId(), applyReason, updatedBySystem, updatedByUser);
        int restrictionID = Integer.parseInt(restrictionIdRaw.split(":")[1].replace("}", ""));
        RestrictionPage.checkRestrictionApplymentAuditTrading(restrictionClient.getUcid(), updatedBySystem, updatedByUser, applyReason, restriction.getName(), restrictionClient.getTradingAccount());
        RestrictionPage.checkKafkaRequestApplyAccount(restrictionClient.getTradingAccount(), restrictionClient.getServerId(), 525_600, restrictionID, applyReason, restriction.getCode());
        RestrictionPage.checkUserHaveRestrictionTrading(restrictionClient.getUcid(), restrictionID);
    }

    @Test
    @DisplayName("Set restriction 'Note for withdrawals' API")
    @Feature("BMS-755 Add new restrictions")
    @Owner("DMITRI KALACHEV")
    @AllureId("933")
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_API)
    void setRestrictionApiNoteForWithdrawalsTest() throws Exception {
        Restriction restriction = Restriction.NOTE_FOR_WITHDRAWALS;
        String applyReason = "reason" + getCurrentTimestamp();
        String updatedBySystem = "system" + getCurrentTimestamp();
        String updatedByUser = "user" + getCurrentTimestamp();
        String restrictionIdRaw = RestrictionPage.setRestrictionAPIGeneralResponse(restrictionClient.getUcid(), restriction.getCode(), applyReason, updatedBySystem, updatedByUser);
        int restrictionID = Integer.parseInt(restrictionIdRaw.split(":")[1].replace("}", ""));
        RestrictionPage.checkRestrictionApplymentAuditGeneral(restrictionClient.getUcid(), updatedBySystem, updatedByUser, applyReason, restriction.getName());
        RestrictionPage.checkKafkaRequestApplyUserId(restrictionClient.getUserId());
        RestrictionPage.checkUserHaveRestrictionGeneral(restrictionClient.getUcid(), restrictionID, "APPLIED");
    }

    @Test
    @DisplayName("Set restriction 'Group Change' API")
    @Feature("BMS-755 Add new restrictions")
    @Owner("DMITRI KALACHEV")
    @AllureId("934")
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_API)
    void setRestrictionApiGroupChangeTest() throws Exception {
        Restriction restriction = Restriction.GROUP_CHANGE;
        String applyReason = "reason" + getCurrentTimestamp();
        String updatedBySystem = "system" + getCurrentTimestamp();
        String updatedByUser = "user" + getCurrentTimestamp();
        String restrictionIdRaw = RestrictionPage.setRestrictionAPITradeResponse(restrictionClient.getUcid(), restriction.getCode(), restrictionClient.getTradingAccount(), restrictionClient.getServerId(), applyReason, updatedBySystem, updatedByUser);
        int restrictionID = Integer.parseInt(restrictionIdRaw.split(":")[1].replace("}", ""));
        RestrictionPage.checkRestrictionApplymentAuditTrading(restrictionClient.getUcid(), updatedBySystem, updatedByUser, applyReason, restriction.getName(), restrictionClient.getTradingAccount());
        RestrictionPage.checkKafkaRequestApplyAccount(restrictionClient.getTradingAccount(), restrictionClient.getServerId(), 525_600, restrictionID, applyReason, restriction.getCode());
        RestrictionPage.checkUserHaveRestrictionTrading(restrictionClient.getUcid(), restrictionID);
    }

    @Test
    @DisplayName("Set restriction 'KYC' API")
    @Feature("BMS-755 Add new restrictions")
    @Owner("DMITRI KALACHEV")
    @AllureId("935")
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_API)
    void setRestrictionApiKYCTest() throws Exception {
        Restriction restriction = Restriction.KYC;
        String applyReason = "reason" + getCurrentTimestamp();
        String updatedBySystem = "system" + getCurrentTimestamp();
        String updatedByUser = "user" + getCurrentTimestamp();
        String restrictionIdRaw = RestrictionPage.setRestrictionAPIGeneralResponse(restrictionClient.getUcid(), restriction.getCode(), applyReason, updatedBySystem, updatedByUser);
        int restrictionID = Integer.parseInt(restrictionIdRaw.split(":")[1].replace("}", ""));
        RestrictionPage.checkRestrictionApplymentAuditGeneral(restrictionClient.getUcid(), updatedBySystem, updatedByUser, applyReason, restriction.getName());
        RestrictionPage.checkKafkaRequestApplyUserId(restrictionClient.getUserId());
        RestrictionPage.checkUserHaveRestrictionGeneral(restrictionClient.getUcid(), restrictionID, "APPLIED");
    }

    @Test
    @DisplayName("Set restriction 'Leverage' API")
    @Feature("BMS-755 Add new restrictions")
    @Owner("DMITRI KALACHEV")
    @AllureId("936")
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_API)
    void setRestrictionApiLeverageTest() throws Exception {
        Restriction restriction = Restriction.LEVERAGE;
        String applyReason = "reason" + getCurrentTimestamp();
        String updatedBySystem = "system" + getCurrentTimestamp();
        String updatedByUser = "user" + getCurrentTimestamp();
        String restrictionIdRaw = RestrictionPage.setRestrictionAPIGeneralResponse(restrictionClient.getUcid(), restriction.getCode(), applyReason, updatedBySystem, updatedByUser);
        int restrictionID = Integer.parseInt(restrictionIdRaw.split(":")[1].replace("}", ""));
        RestrictionPage.checkRestrictionApplymentAuditGeneral(restrictionClient.getUcid(), updatedBySystem, updatedByUser, applyReason, restriction.getName());
        RestrictionPage.checkKafkaRequestApplyUserId(restrictionClient.getUserId());
        RestrictionPage.checkUserHaveRestrictionGeneral(restrictionClient.getUcid(), restrictionID, "APPLIED");
    }

    @Test
    @DisplayName("Set restriction 'Warning Letter' API")
    @Feature("BMS-755 Add new restrictions")
    @Owner("DMITRI KALACHEV")
    @AllureId("937")
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_API)
    void setRestrictionApiWarningLetterTest() throws Exception {
        Restriction restriction = Restriction.WARNING_LETTER;
        String applyReason = "reason" + getCurrentTimestamp();
        String updatedBySystem = "system" + getCurrentTimestamp();
        String updatedByUser = "user" + getCurrentTimestamp();
        String restrictionIdRaw = RestrictionPage.setRestrictionAPIGeneralResponse(restrictionClient.getUcid(), restriction.getCode(), applyReason, updatedBySystem, updatedByUser);
        int restrictionID = Integer.parseInt(restrictionIdRaw.split(":")[1].replace("}", ""));
        RestrictionPage.checkRestrictionApplymentAuditGeneral(restrictionClient.getUcid(), updatedBySystem, updatedByUser, applyReason, restriction.getName());
        RestrictionPage.checkKafkaRequestApplyUserId(restrictionClient.getUserId());
        RestrictionPage.checkUserHaveRestrictionGeneral(restrictionClient.getUcid(), restrictionID, "APPLIED");
    }

    @Test
    @DisplayName("Set restriction 'Remove swap free option' API")
    @Feature("BMS-755 Add new restrictions")
    @Owner("DMITRI KALACHEV")
    @AllureId("938")
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_API)
    void setRestrictionApiRemoveSwapFreeOptionTest() throws Exception {
        Restriction restriction = Restriction.GROUP_CHANGE;
        String applyReason = "reason" + getCurrentTimestamp();
        String updatedBySystem = "system" + getCurrentTimestamp();
        String updatedByUser = "user" + getCurrentTimestamp();
        String restrictionIdRaw = RestrictionPage.setRestrictionAPITradeResponse(restrictionClient.getUcid(), restriction.getCode(), restrictionClient.getTradingAccount(), restrictionClient.getServerId(), applyReason, updatedBySystem, updatedByUser);
        int restrictionID = Integer.parseInt(restrictionIdRaw.split(":")[1].replace("}", ""));
        RestrictionPage.checkRestrictionApplymentAuditTrading(restrictionClient.getUcid(), updatedBySystem, updatedByUser, applyReason, restriction.getName(), restrictionClient.getTradingAccount());
        RestrictionPage.checkKafkaRequestApplyAccount(restrictionClient.getTradingAccount(), restrictionClient.getServerId(), 525_600, restrictionID, applyReason, restriction.getCode());
        RestrictionPage.checkUserHaveRestrictionTrading(restrictionClient.getUcid(), restrictionID);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1075")
    @DisplayName("Verify logic for internalReason field")
    void internalReasonTest() throws IOException, InterruptedException {
        PostRestrictionRequestBody postRestriction = new PostRestrictionRequestBody(restrictionClient.getUcid(), "05", "GENERAL", null, null, null, new PostRestrictionRequestBody.UpdatedBy("autotest", "autotest"), new PostRestrictionRequestBody.AdditionalParam[]{new PostRestrictionRequestBody.AdditionalParam("connectionScore", "string", "0.75"), new PostRestrictionRequestBody.AdditionalParam("potentialFraudTypes", "array", new String[]{"HEDGING"}), new PostRestrictionRequestBody.AdditionalParam("confirmedFraudTypes", "array", new String[]{"PRICING_ERROR"})});
        Response response = postRestriction(postRestriction);
        assertThat("Verify 200 response code", response.code(), is(200));
        assertThat(response.body(), notNullValue());
        KafkaHelper kafka = new KafkaHelper();
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_CLIENT_RESTRICTIONS_APPLY, restrictionClient.getUserId().toString());
        boolean internalReasonFound = false;
        String expectedInternalReason = """
                Potential Fraud Type: Hedging (mirror trading) - Description: Client are engaging in Hedging (Mirror trading) fraud in order to abuse our deposit bonus scheme and gain guaranteed profit through their trades.
                Confirmed Fraud Type: Pricing errors - Description: Client is taking advantage of errors in our quotes/pricing in order to make guaranteed profits.
                Restriction: Login CRM - Description: Considering the severity of certain clients' actions, their accounts need to be blocked completely. This can be relevant for more serious cases of Market manipulation, thin liquidity scalping, gap trading and more.
                Connection Score is 0.75""";
        for (String message : consumedMessages) {
            ClientRestrictionApply kafkaMessage = objectMapper.readValue(message, ClientRestrictionApply.class);
            System.out.println((kafkaMessage.restrictions[0].internalReason));
            if ((kafkaMessage.restrictions.length == 1) && Objects.equals(kafkaMessage.restrictions[0].internalReason, expectedInternalReason)) {
                internalReasonFound = true;
            }
        }
        assertThat("Verify internalReason was found in one of the kafka messages ", internalReasonFound, is(true));
    }

}
