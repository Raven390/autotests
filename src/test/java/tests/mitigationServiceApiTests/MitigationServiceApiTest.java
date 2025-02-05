package tests.mitigationServiceApiTests;

import businessObjects.api.mitigationService.*;
import businessObjects.db.clickhouse.crmTbAccount.CrmTbAccountObject;
import businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObject;
import helpers.data.ClientHelper;
import helpers.data.enums.Brand;
import helpers.data.enums.Regulator;
import helpers.data.enums.Restriction;
import helpers.database.MitigationHelper;
import io.qameta.allure.*;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import pageObjects.backofficePages.RestrictionPage;
import tests.TestBaseApi;

import java.io.IOException;
import java.sql.SQLException;

import static businessObjects.api.mitigationService.MitigationServiceRequest.*;
import static businessObjects.db.clickhouse.crmTbAccount.CrmTbAccountObjectFactory.generateStaticCrmTbAccountActive;
import static businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObjectFactory.generateStaticUserByClient;
import static helpers.database.DbHelper.*;
import static org.junit.jupiter.api.Assertions.*;
import static utils.Constants.*;
import static utils.Utils.getCurrentTimestamp;


@Tag(TEAM_BACKOFFICE)
@Tag(LAYER_API)
@Tag(SUITE_MITIGATION_SERVICE)
public class MitigationServiceApiTest extends TestBaseApi {

    static ClientHelper restrictionClient = new ClientHelper(141_401, "063cde3b-ea9d-48b5-8e2c-99f3d5f67999", Brand.INFINOX, Regulator.VFSC2, 14_140_101, 42);


    @BeforeAll
    public static void initialSetup() throws IOException, ReflectiveOperationException, SQLException {
        Response response = enableCRMEmulator();
        assertNotNull(response);
        CrmTbUserObject restrictionClientDB = generateStaticUserByClient(restrictionClient);
        CrmTbAccountObject active = generateStaticCrmTbAccountActive(restrictionClient);
        insertObjectToDb(CRM_USER_TABLE_NAME, restrictionClientDB);
        insertObjectToDb(CRM_ACCOUNT_TABLE_NAME, active);
    }

    @BeforeEach
    public void before() throws Exception {
        MitigationHelper.cleanUserRestriction(restrictionClient.getUcid());
//        AuditHelper.cleanUserAudit(restrictionClient.getUcid());
    }

    @Test
    @DisplayName("Restriction Set PUT request returns 200 if restriction already applied")
    @AllureId("920")
    @Owner("DMITRI KALACHEV")
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_API)
    public void successIfRestrictionAlreadyApplied() throws Exception {

        PostRestrictionRequestBody postRestrictionRequestBody = new PostRestrictionRequestBody(
                "vantage-10081449", "05", "GENERAL", null, null, "Integration test", new PostRestrictionRequestBody.UpdatedBy("API", "QA")
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
    public void setRestrictionApiOpenNewAccountTest() throws Exception {
        Restriction restriction = Restriction.OPEN_NEW_ACCOUNT;
        String applyReason = "reason" + getCurrentTimestamp();
        String updatedBySystem = "system" + getCurrentTimestamp();
        String updatedByUser = "user" + getCurrentTimestamp();
        RestrictionPage.setRestrictionAPIGeneral(restrictionClient.getUcid(), restriction.getCode(), applyReason, updatedBySystem, updatedByUser);
        RestrictionPage.checkRestrictionApplymentAuditGeneral(restrictionClient.getUcid(), updatedBySystem, updatedByUser, applyReason, restriction.getName());
        RestrictionPage.checkKafkaRequestApplyUserId(restrictionClient.getUserId());
        RestrictionPage.checkUserHaveRestriction(restrictionClient.getUcid(), restriction.getId(), applyReason, "APPLIED");
    }

    @Test
    @DisplayName("Set restriction 'Internal transfer' API")
    @Feature("BMS-755 Add new restrictions")
    @Owner("DMITRI KALACHEV")
    @AllureId("923")
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_API)
    public void setRestrictionApiInternalTransferTest() throws Exception {
        Restriction restriction = Restriction.INTERNAL_TRANSFER;
        String applyReason = "reason" + getCurrentTimestamp();
        String updatedBySystem = "system" + getCurrentTimestamp();
        String updatedByUser = "user" + getCurrentTimestamp();
        RestrictionPage.setRestrictionAPIGeneral(restrictionClient.getUcid(), restriction.getCode(), applyReason, updatedBySystem, updatedByUser);
        RestrictionPage.checkRestrictionApplymentAuditGeneral(restrictionClient.getUcid(), updatedBySystem, updatedByUser, applyReason, restriction.getName());
        RestrictionPage.checkKafkaRequestApplyUserId(restrictionClient.getUserId());
        RestrictionPage.checkUserHaveRestriction(restrictionClient.getUcid(), restriction.getId(), applyReason, "APPLIED");
    }

    @Test
    @DisplayName("Set restriction 'Deposits' API")
    @Feature("BMS-755 Add new restrictions")
    @Owner("DMITRI KALACHEV")
    @AllureId("924")
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_API)
    public void setRestrictionApiDepositsTest() throws Exception {
        Restriction restriction = Restriction.DEPOSITS;
        String applyReason = "reason" + getCurrentTimestamp();
        String updatedBySystem = "system" + getCurrentTimestamp();
        String updatedByUser = "user" + getCurrentTimestamp();
        RestrictionPage.setRestrictionAPIGeneral(restrictionClient.getUcid(), restriction.getCode(), applyReason, updatedBySystem, updatedByUser);
        RestrictionPage.checkRestrictionApplymentAuditGeneral(restrictionClient.getUcid(), updatedBySystem, updatedByUser, applyReason, restriction.getName());
        RestrictionPage.checkKafkaRequestApplyUserId(restrictionClient.getUserId());
        RestrictionPage.checkUserHaveRestriction(restrictionClient.getUcid(), restriction.getId(), applyReason, "APPLIED");
    }

    @Test
    @DisplayName("Set restriction 'Withdrawals' API")
    @Feature("BMS-755 Add new restrictions")
    @Owner("DMITRI KALACHEV")
    @AllureId("925")
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_API)
    public void setRestrictionApiWithdrawalsTest() throws Exception {
        Restriction restriction = Restriction.WITHDRAWALS;
        String applyReason = "reason" + getCurrentTimestamp();
        String updatedBySystem = "system" + getCurrentTimestamp();
        String updatedByUser = "user" + getCurrentTimestamp();
        RestrictionPage.setRestrictionAPIGeneral(restrictionClient.getUcid(), restriction.getCode(), applyReason, updatedBySystem, updatedByUser);
        RestrictionPage.checkRestrictionApplymentAuditGeneral(restrictionClient.getUcid(), updatedBySystem, updatedByUser, applyReason, restriction.getName());
        RestrictionPage.checkKafkaRequestApplyUserId(restrictionClient.getUserId());
        RestrictionPage.checkUserHaveRestriction(restrictionClient.getUcid(), restriction.getId(), applyReason, "APPLIED");
    }

    @Test
    @DisplayName("Set restriction 'Login CRM' API")
    @Feature("BMS-755 Add new restrictions")
    @Owner("DMITRI KALACHEV")
    @AllureId("925")
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_API)
    public void setRestrictionApiLoginCrmTest() throws Exception {
        Restriction restriction = Restriction.LOGIN_CRM;
        String applyReason = "reason" + getCurrentTimestamp();
        String updatedBySystem = "system" + getCurrentTimestamp();
        String updatedByUser = "user" + getCurrentTimestamp();
        RestrictionPage.setRestrictionAPIGeneral(restrictionClient.getUcid(), restriction.getCode(), applyReason, updatedBySystem, updatedByUser);
        RestrictionPage.checkRestrictionApplymentAuditGeneral(restrictionClient.getUcid(), updatedBySystem, updatedByUser, applyReason, restriction.getName());
        RestrictionPage.checkKafkaRequestApplyUserId(restrictionClient.getUserId());
        RestrictionPage.checkUserHaveRestriction(restrictionClient.getUcid(), restriction.getId(), applyReason, "APPLIED");
    }

    @Test
    @DisplayName("Set restriction 'Close only mode' API")
    @Feature("BMS-755 Add new restrictions")
    @Owner("DMITRI KALACHEV")
    @AllureId("926")
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_API)
    public void setRestrictionApiCloseOnlyModeTest() throws Exception {
        Restriction restriction = Restriction.CLOSE_ONLY_MODE;
        String applyReason = "reason" + getCurrentTimestamp();
        String updatedBySystem = "system" + getCurrentTimestamp();
        String updatedByUser = "user" + getCurrentTimestamp();
        String restrictionIdRaw = RestrictionPage.setRestrictionAPITradeResponse(restrictionClient.getUcid(), restriction.getCode(), restrictionClient.getTradingAccount(), restrictionClient.getServerId(), applyReason, updatedBySystem, updatedByUser);
        int restrictionID = Integer.parseInt(restrictionIdRaw.split(":")[1].replace("}", ""));
        RestrictionPage.checkRestrictionApplymentAuditTrading(restrictionClient.getUcid(), updatedBySystem, updatedByUser, applyReason, restriction.getName(), restrictionClient.getTradingAccount());
        RestrictionPage.checkKafkaRequestApplyAccount(restrictionClient.getTradingAccount(), restrictionClient.getServerId(), 525_600, restrictionID, applyReason, restriction.getCode());
        RestrictionPage.checkUserHaveRestriction(restrictionClient.getUcid(), restriction.getId(), applyReason, "APPLIED");
    }

    @Test
    @DisplayName("Set restriction 'B-Book -> A-Book' API")
    @Feature("BMS-755 Add new restrictions")
    @Owner("DMITRI KALACHEV")
    @AllureId("926")
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_API)
    public void setRestrictionApiBaBookTest() throws Exception {
        Restriction restriction = Restriction.B_BOOK_TO_A_BOOK;
        String applyReason = "reason" + getCurrentTimestamp();
        String updatedBySystem = "system" + getCurrentTimestamp();
        String updatedByUser = "user" + getCurrentTimestamp();
        String restrictionIdRaw = RestrictionPage.setRestrictionAPITradeResponse(restrictionClient.getUcid(), restriction.getCode(), restrictionClient.getTradingAccount(), restrictionClient.getServerId(), applyReason, updatedBySystem, updatedByUser);
        int restrictionID = Integer.parseInt(restrictionIdRaw.split(":")[1].replace("}", ""));
        RestrictionPage.checkRestrictionApplymentAuditTrading(restrictionClient.getUcid(), updatedBySystem, updatedByUser, applyReason, restriction.getName(), restrictionClient.getTradingAccount());
        RestrictionPage.checkKafkaRequestApplyAccount(restrictionClient.getTradingAccount(), restrictionClient.getServerId(), 525_600, restrictionID, applyReason, restriction.getCode());
        RestrictionPage.checkUserHaveRestriction(restrictionClient.getUcid(), restriction.getId(), applyReason, "APPLIED");
    }

    @Test
    @DisplayName("Set restriction 'Manual Withdrawal Review' API")
    @Feature("BMS-755 Add new restrictions")
    @Owner("DMITRI KALACHEV")
    @AllureId("927")
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_API)
    public void setRestrictionApiManualWithdrawalReviewTest() throws Exception {
        Restriction restriction = Restriction.MANUAL_WITHDRAWAL_REVIEW;
        String applyReason = "reason" + getCurrentTimestamp();
        String updatedBySystem = "system" + getCurrentTimestamp();
        String updatedByUser = "user" + getCurrentTimestamp();
        RestrictionPage.setRestrictionAPIGeneral(restrictionClient.getUcid(), restriction.getCode(), applyReason, updatedBySystem, updatedByUser);
        RestrictionPage.checkRestrictionApplymentAuditGeneral(restrictionClient.getUcid(), updatedBySystem, updatedByUser, applyReason, restriction.getName());
        RestrictionPage.checkKafkaRequestApplyUserId(restrictionClient.getUserId());
        RestrictionPage.checkUserHaveRestriction(restrictionClient.getUcid(), restriction.getId(), applyReason, "APPLIED");
    }

    @Test
    @DisplayName("Set restriction 'Credit and Bonus' API")
    @Feature("BMS-755 Add new restrictions")
    @Owner("DMITRI KALACHEV")
    @AllureId("928")
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_API)
    public void setRestrictionApiCreditAndBonusTest() throws Exception {
        Restriction restriction = Restriction.CREDIT_AND_BONUS;
        String applyReason = "reason" + getCurrentTimestamp();
        String updatedBySystem = "system" + getCurrentTimestamp();
        String updatedByUser = "user" + getCurrentTimestamp();
        RestrictionPage.setRestrictionAPIGeneral(restrictionClient.getUcid(), restriction.getCode(), applyReason, updatedBySystem, updatedByUser);
        RestrictionPage.checkRestrictionApplymentAuditGeneral(restrictionClient.getUcid(), updatedBySystem, updatedByUser, applyReason, restriction.getName());
        RestrictionPage.checkKafkaRequestApplyUserId(restrictionClient.getUserId());
        RestrictionPage.checkUserHaveRestriction(restrictionClient.getUcid(), restriction.getId(), applyReason, "APPLIED");
    }

    @Test
    @DisplayName("Set restriction 'Read-only mode' API")
    @Feature("BMS-755 Add new restrictions")
    @Owner("DMITRI KALACHEV")
    @AllureId("930")
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_API)
    public void setRestrictionApiReadOnlyModeTest() throws Exception {
        Restriction restriction = Restriction.READ_ONLY_MODE;
        String applyReason = "reason" + getCurrentTimestamp();
        String updatedBySystem = "system" + getCurrentTimestamp();
        String updatedByUser = "user" + getCurrentTimestamp();
        String restrictionIdRaw = RestrictionPage.setRestrictionAPITradeResponse(restrictionClient.getUcid(), restriction.getCode(), restrictionClient.getTradingAccount(), restrictionClient.getServerId(), applyReason, updatedBySystem, updatedByUser);
        int restrictionID = Integer.parseInt(restrictionIdRaw.split(":")[1].replace("}", ""));
        RestrictionPage.checkRestrictionApplymentAuditTrading(restrictionClient.getUcid(), updatedBySystem, updatedByUser, applyReason, restriction.getName(), restrictionClient.getTradingAccount());
        RestrictionPage.checkKafkaRequestApplyAccount(restrictionClient.getTradingAccount(), restrictionClient.getServerId(), 525_600, restrictionID, applyReason, restriction.getCode());
        RestrictionPage.checkUserHaveRestriction(restrictionClient.getUcid(), restriction.getId(), applyReason, "APPLIED");
    }

    @Test
    @DisplayName("Set restriction 'Off quotes' API")
    @Feature("BMS-755 Add new restrictions")
    @Owner("DMITRI KALACHEV")
    @AllureId("931")
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_API)
    public void setRestrictionApiOffQuotesTest() throws Exception {
        Restriction restriction = Restriction.OFF_QUOTES;
        String applyReason = "reason" + getCurrentTimestamp();
        String updatedBySystem = "system" + getCurrentTimestamp();
        String updatedByUser = "user" + getCurrentTimestamp();
        String restrictionIdRaw = RestrictionPage.setRestrictionAPITradeResponse(restrictionClient.getUcid(), restriction.getCode(), restrictionClient.getTradingAccount(), restrictionClient.getServerId(), applyReason, updatedBySystem, updatedByUser);
        int restrictionID = Integer.parseInt(restrictionIdRaw.split(":")[1].replace("}", ""));
        RestrictionPage.checkRestrictionApplymentAuditTrading(restrictionClient.getUcid(), updatedBySystem, updatedByUser, applyReason, restriction.getName(), restrictionClient.getTradingAccount());
        RestrictionPage.checkKafkaRequestApplyAccount(restrictionClient.getTradingAccount(), restrictionClient.getServerId(), 525_600, restrictionID, applyReason, restriction.getCode());
        RestrictionPage.checkUserHaveRestriction(restrictionClient.getUcid(), restriction.getId(), applyReason, "APPLIED");
    }

    @Test
    @DisplayName("Set restriction 'Trading hours' API")
    @Feature("BMS-755 Add new restrictions")
    @Owner("DMITRI KALACHEV")
    @AllureId("931")
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_API)
    public void setRestrictionApiTradingHoursTest() throws Exception {
        Restriction restriction = Restriction.TRADING_HOURS;
        String applyReason = "reason" + getCurrentTimestamp();
        String updatedBySystem = "system" + getCurrentTimestamp();
        String updatedByUser = "user" + getCurrentTimestamp();
        String restrictionIdRaw = RestrictionPage.setRestrictionAPITradeResponse(restrictionClient.getUcid(), restriction.getCode(), restrictionClient.getTradingAccount(), restrictionClient.getServerId(), applyReason, updatedBySystem, updatedByUser);
        int restrictionID = Integer.parseInt(restrictionIdRaw.split(":")[1].replace("}", ""));
        RestrictionPage.checkRestrictionApplymentAuditTrading(restrictionClient.getUcid(), updatedBySystem, updatedByUser, applyReason, restriction.getName(), restrictionClient.getTradingAccount());
        RestrictionPage.checkKafkaRequestApplyAccount(restrictionClient.getTradingAccount(), restrictionClient.getServerId(), 525_600, restrictionID, applyReason, restriction.getCode());
        RestrictionPage.checkUserHaveRestriction(restrictionClient.getUcid(), restriction.getId(), applyReason, "APPLIED");
    }

    @Test
    @DisplayName("Set restriction 'Login MT' API")
    @Feature("BMS-755 Add new restrictions")
    @Owner("DMITRI KALACHEV")
    @AllureId("932")
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_API)
    public void setRestrictionApiLoginMTTest() throws Exception {
        Restriction restriction = Restriction.LOGIN_MT;
        String applyReason = "reason" + getCurrentTimestamp();
        String updatedBySystem = "system" + getCurrentTimestamp();
        String updatedByUser = "user" + getCurrentTimestamp();
        String restrictionIdRaw = RestrictionPage.setRestrictionAPITradeResponse(restrictionClient.getUcid(), restriction.getCode(), restrictionClient.getTradingAccount(), restrictionClient.getServerId(), applyReason, updatedBySystem, updatedByUser);
        int restrictionID = Integer.parseInt(restrictionIdRaw.split(":")[1].replace("}", ""));
        RestrictionPage.checkRestrictionApplymentAuditTrading(restrictionClient.getUcid(), updatedBySystem, updatedByUser, applyReason, restriction.getName(), restrictionClient.getTradingAccount());
        RestrictionPage.checkKafkaRequestApplyAccount(restrictionClient.getTradingAccount(), restrictionClient.getServerId(), 525_600, restrictionID, applyReason, restriction.getCode());
        RestrictionPage.checkUserHaveRestriction(restrictionClient.getUcid(), restriction.getId(), applyReason, "APPLIED");
    }

    @Test
    @DisplayName("Set restriction 'Note for withdrawals' API")
    @Feature("BMS-755 Add new restrictions")
    @Owner("DMITRI KALACHEV")
    @AllureId("933")
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_API)
    public void setRestrictionApiNoteForWithdrawalsTest() throws Exception {
        Restriction restriction = Restriction.NOTE_FOR_WITHDRAWALS;
        String applyReason = "reason" + getCurrentTimestamp();
        String updatedBySystem = "system" + getCurrentTimestamp();
        String updatedByUser = "user" + getCurrentTimestamp();
        RestrictionPage.setRestrictionAPIGeneral(restrictionClient.getUcid(), restriction.getCode(), applyReason, updatedBySystem, updatedByUser);
        RestrictionPage.checkRestrictionApplymentAuditGeneral(restrictionClient.getUcid(), updatedBySystem, updatedByUser, applyReason, restriction.getName());
        RestrictionPage.checkKafkaRequestApplyUserId(restrictionClient.getUserId());
        RestrictionPage.checkUserHaveRestriction(restrictionClient.getUcid(), restriction.getId(), applyReason, "APPLIED");
    }

    @Test
    @DisplayName("Set restriction 'Group Change' API")
    @Feature("BMS-755 Add new restrictions")
    @Owner("DMITRI KALACHEV")
    @AllureId("934")
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_API)
    public void setRestrictionApiGroupChangeTest() throws Exception {
        Restriction restriction = Restriction.GROUP_CHANGE;
        String applyReason = "reason" + getCurrentTimestamp();
        String updatedBySystem = "system" + getCurrentTimestamp();
        String updatedByUser = "user" + getCurrentTimestamp();
        String restrictionIdRaw = RestrictionPage.setRestrictionAPITradeResponse(restrictionClient.getUcid(), restriction.getCode(), restrictionClient.getTradingAccount(), restrictionClient.getServerId(), applyReason, updatedBySystem, updatedByUser);
        int restrictionID = Integer.parseInt(restrictionIdRaw.split(":")[1].replace("}", ""));
        RestrictionPage.checkRestrictionApplymentAuditTrading(restrictionClient.getUcid(), updatedBySystem, updatedByUser, applyReason, restriction.getName(), restrictionClient.getTradingAccount());
        RestrictionPage.checkKafkaRequestApplyAccount(restrictionClient.getTradingAccount(), restrictionClient.getServerId(), 525_600, restrictionID, applyReason, restriction.getCode());
        RestrictionPage.checkUserHaveRestriction(restrictionClient.getUcid(), restriction.getId(), applyReason, "APPLIED");
    }

    @Test
    @DisplayName("Set restriction 'KYC' API")
    @Feature("BMS-755 Add new restrictions")
    @Owner("DMITRI KALACHEV")
    @AllureId("935")
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_API)
    public void setRestrictionApiKYCTest() throws Exception {
        Restriction restriction = Restriction.KYC;
        String applyReason = "reason" + getCurrentTimestamp();
        String updatedBySystem = "system" + getCurrentTimestamp();
        String updatedByUser = "user" + getCurrentTimestamp();
        RestrictionPage.setRestrictionAPIGeneral(restrictionClient.getUcid(), restriction.getCode(), applyReason, updatedBySystem, updatedByUser);
        RestrictionPage.checkRestrictionApplymentAuditGeneral(restrictionClient.getUcid(), updatedBySystem, updatedByUser, applyReason, restriction.getName());
        RestrictionPage.checkKafkaRequestApplyUserId(restrictionClient.getUserId());
        RestrictionPage.checkUserHaveRestriction(restrictionClient.getUcid(), restriction.getId(), applyReason, "APPLIED");
    }

    @Test
    @DisplayName("Set restriction 'Leverage' API")
    @Feature("BMS-755 Add new restrictions")
    @Owner("DMITRI KALACHEV")
    @AllureId("936")
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_API)
    public void setRestrictionApiLeverageTest() throws Exception {
        Restriction restriction = Restriction.LEVERAGE;
        String applyReason = "reason" + getCurrentTimestamp();
        String updatedBySystem = "system" + getCurrentTimestamp();
        String updatedByUser = "user" + getCurrentTimestamp();
        RestrictionPage.setRestrictionAPIGeneral(restrictionClient.getUcid(), restriction.getCode(), applyReason, updatedBySystem, updatedByUser);
        RestrictionPage.checkRestrictionApplymentAuditGeneral(restrictionClient.getUcid(), updatedBySystem, updatedByUser, applyReason, restriction.getName());
        RestrictionPage.checkKafkaRequestApplyUserId(restrictionClient.getUserId());
        RestrictionPage.checkUserHaveRestriction(restrictionClient.getUcid(), restriction.getId(), applyReason, "APPLIED");
    }

    @Test
    @DisplayName("Set restriction 'Warning Letter' API")
    @Feature("BMS-755 Add new restrictions")
    @Owner("DMITRI KALACHEV")
    @AllureId("937")
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_API)
    public void setRestrictionApiWarningLetterTest() throws Exception {
        Restriction restriction = Restriction.WARNING_LETTER;
        String applyReason = "reason" + getCurrentTimestamp();
        String updatedBySystem = "system" + getCurrentTimestamp();
        String updatedByUser = "user" + getCurrentTimestamp();
        RestrictionPage.setRestrictionAPIGeneral(restrictionClient.getUcid(), restriction.getCode(), applyReason, updatedBySystem, updatedByUser);
        RestrictionPage.checkRestrictionApplymentAuditGeneral(restrictionClient.getUcid(), updatedBySystem, updatedByUser, applyReason, restriction.getName());
        RestrictionPage.checkKafkaRequestApplyUserId(restrictionClient.getUserId());
        RestrictionPage.checkUserHaveRestriction(restrictionClient.getUcid(), restriction.getId(), applyReason, "APPLIED");
    }

    @Test
    @DisplayName("Set restriction 'Remove swap free option' API")
    @Feature("BMS-755 Add new restrictions")
    @Owner("DMITRI KALACHEV")
    @AllureId("938")
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_API)
    public void setRestrictionApiRemoveSwapFreeOptionTest() throws Exception {
        Restriction restriction = Restriction.GROUP_CHANGE;
        String applyReason = "reason" + getCurrentTimestamp();
        String updatedBySystem = "system" + getCurrentTimestamp();
        String updatedByUser = "user" + getCurrentTimestamp();
        String restrictionIdRaw = RestrictionPage.setRestrictionAPITradeResponse(restrictionClient.getUcid(), restriction.getCode(), restrictionClient.getTradingAccount(), restrictionClient.getServerId(), applyReason, updatedBySystem, updatedByUser);
        int restrictionID = Integer.parseInt(restrictionIdRaw.split(":")[1].replace("}", ""));
        RestrictionPage.checkRestrictionApplymentAuditTrading(restrictionClient.getUcid(), updatedBySystem, updatedByUser, applyReason, restriction.getName(), restrictionClient.getTradingAccount());
        RestrictionPage.checkKafkaRequestApplyAccount(restrictionClient.getTradingAccount(), restrictionClient.getServerId(), 525_600, restrictionID, applyReason, restriction.getCode());
        RestrictionPage.checkUserHaveRestriction(restrictionClient.getUcid(), restriction.getId(), applyReason, "APPLIED");
    }


//    @Test
//    @DisplayName("Mitigation service tests")
//    @AllureId("")
//    public void getRestrictionsByUcidTest() throws IOException {
//
//        Response response = getRestrictionsByUcid("vantage-1370903308");
//        GetRestrictionResponseBody[] restrictionBody = objectMapper.readValue(
//                response.body().string(),
//                GetRestrictionResponseBody[].class
//        );
//        System.out.println(Arrays.toString(restrictionBody));
//        System.out.println(response.code());
//    }
//
//    @Test
//    @DisplayName("Mitigation service tests")
//    @AllureId("")
//    public void postRestrictionsTest() throws IOException {
//
//        PostRestrictionRequestBody postRestrictionRequestBody = new PostRestrictionRequestBody(
//                "vantage-10079867",
//                "05",
//                "GENERAL",
//                null,
//                null,
//                "Integration test",
//                new PostRestrictionRequestBody.UpdatedBy("string", "string")
//        );
//
//        Response response = postRestriction(postRestrictionRequestBody);
//        PostRestrictionResponse restrictionBody = objectMapper.readValue(
//                response.body().string(),
//                PostRestrictionResponse.class
//        );
//        System.out.println(restrictionBody);
//        System.out.println(response.code());
//    }
//
//    @Test
//    @DisplayName("Mitigation service tests")
//    @AllureId("")
//    public void cancelRestrictionTest() throws IOException {
//
//        KafkaHelper kafka = new KafkaHelper();
//        ObjectMapper objectMapper = new ObjectMapper();
//
////         // confirm apply
////         ApplyConfirmedKafkaMessage applyConfirmedKafkaMessage = new ApplyConfirmedKafkaMessage(
////                 "2024-09-11T12:00:00Z",
////                 new ApplyConfirmedKafkaMessage.Restriction[]{
////                         new ApplyConfirmedKafkaMessage.Restriction(183, "Applied", "")
////                 });
////
////         kafka.produceMessage("13", objectMapper.writeValueAsString(applyConfirmedKafkaMessage), "client.restrictions.applyConfirmed");
//
//        // cancel
//        CancelRestrictionRequestBody cancelRestrictionRequestBody = new CancelRestrictionRequestBody(
//                "string",
//                new CancelRestrictionRequestBody.UpdatedBy("string", "string")
//        );
//
//        Response response = cancelRestrictionById(184, cancelRestrictionRequestBody);
//        System.out.println(response.body().string());
//        System.out.println(response.code());
//
//        // confirm cancel
//        CancelConfirmedKafkaMessage cancelConfirmedKafkaMessage = new CancelConfirmedKafkaMessage(
//                "2024-09-11T12:00:00Z",
//                new CancelConfirmedKafkaMessage.Restriction[]{
//                        new CancelConfirmedKafkaMessage.Restriction(184, "Canceled")
//                });
//
//        kafka.produceMessage("13", objectMapper.writeValueAsString(cancelConfirmedKafkaMessage), "client.restrictions.cancelConfirmed");
//    }
//
//    @Test
//    @DisplayName("Mitigation service tests")
//    @AllureId("")
//    public void getRestrictionCatalogTest() throws IOException {
//
//        Response response = getRestrictionCatalog();
//        RestrictionCatalogEntry[] restrictionCatalog = objectMapper.readValue(
//                response.body().string(),
//                RestrictionCatalogEntry[].class
//        );
//        System.out.println(Arrays.toString(restrictionCatalog));
//        System.out.println(response.code());
//    }
}
