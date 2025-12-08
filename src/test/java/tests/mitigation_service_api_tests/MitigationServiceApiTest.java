package tests.mitigation_service_api_tests;

import business_objects.api.lark.TenantAccessToken.TenantAccessTokenResponse;
import business_objects.api.lark.chatHistory.ByBitRestrictionCancellationMessage;
import business_objects.api.lark.chatHistory.ChatHistoryResponse;
import business_objects.api.mitigation_service.CancelRestrictionByBitRequest;
import business_objects.api.mitigation_service.PostRestrictionRequestBody;
import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.mitigation_service_db.ClientBybitRestriction;
import business_objects.kafka.restriction_events.ClientRestrictionApply;
import helpers.data.ClientHelper;
import helpers.data.enums.Brand;
import helpers.data.enums.Regulator;
import helpers.data.enums.Restriction;
import helpers.database.CleanTableHelper;
import helpers.database.DbName;
import helpers.kafka.KafkaHelper;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import page_objects.backoffice_pages.investigationTool.RestrictionPage;
import tests.TestBaseApi;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static business_objects.api.lark.LarkRequest.getMessagesChatLast10Minutes;
import static business_objects.api.lark.LarkRequest.getTenantToken;
import static business_objects.api.mitigation_service.MitigationServiceRequest.*;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateStaticCrmTbAccountActive;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateStaticUserByClient;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static helpers.api.RestrictionHelper.setRestrictionAPIGeneralResponse;
import static helpers.api.RestrictionHelper.setRestrictionAPITradeResponse;
import static helpers.data.ClientFactory.getRandomBybitClient;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static utils.Constants.*;
import static utils.Utils.*;

@Tag(TEAM_BACKOFFICE)
@Tag(LAYER_API)
@Tag(SUITE_MITIGATION_SERVICE)
class MitigationServiceApiTest extends TestBaseApi {

    static ClientHelper restrictionClient = new ClientHelper(141_401, "063cde3b-ea9d-48b5-8e2c-99f3d5f67999", Brand.VANTAGE, Regulator.VFSC2, 14_140_101, 42);
    static ClientHelper byBitClient = getRandomBybitClient();
    static CrmTbUserObject byBitUser = generateUserByClient(byBitClient);
    static CrmTbAccountObject activeByBit = generateStaticCrmTbAccountActive(byBitClient);

    @BeforeAll
    static void initialSetup() throws IOException, InterruptedException {
        Response response = enableCRMEmulator();
        assertNotNull(response);
        CrmTbUserObject restrictionClientDB = generateStaticUserByClient(restrictionClient);
        CrmTbAccountObject active = generateStaticCrmTbAccountActive(restrictionClient);
        insertObjectsToDb(CRM_USER_TABLE_NAME, List.of(restrictionClientDB, byBitUser));
        insertCrmAccountsToDb(active, activeByBit);
        Thread.sleep(5000);
    }

    @BeforeEach
    void before() throws Exception {
        CleanTableHelper.cleanUserRestrictionGeneral(restrictionClient.getUcid());
        CleanTableHelper.cleanUserRestrictionTrading(restrictionClient.getUcid());
        CleanTableHelper.cleanUserAudit(restrictionClient.getUcid());
    }

    @AfterAll
    static void breakdown() throws Exception {
        CleanTableHelper.cleanUserRestrictionGeneral(restrictionClient.getUcid());
        CleanTableHelper.cleanUserRestrictionGeneral(byBitClient.getUcid());
        CleanTableHelper.cleanUserRestrictionTrading(restrictionClient.getUcid());
        CleanTableHelper.cleanUserAudit(restrictionClient.getUcid());
        CleanTableHelper.cleanUserAudit(byBitClient.getUcid());
        deleteEntryFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", byBitClient.getUcid()));
    }

    @Test
    @DisplayName("Restriction Set PUT request returns 200 if restriction already applied")
    @AllureId("920")
    @Owner("DMITRI KALACHEV")
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
    void setRestrictionApiOpenNewAccountTest() throws Exception {
        Restriction restriction = Restriction.ACCOUNT_CREATION_REVIEW;
        String applyReason = "reason" + getCurrentTimestampSeconds();
        String updatedBySystem = "system" + getCurrentTimestampSeconds();
        String updatedByUser = "user" + getCurrentTimestampSeconds();
        setRestrictionAPIGeneralResponse(restrictionClient.getUcid(), restriction.getCode(), applyReason, updatedBySystem, updatedByUser);
        RestrictionPage.checkKafkaRequestApplyUserId(restrictionClient.getUserId());
        RestrictionPage.checkUserHaveRestrictionGeneral(restrictionClient.getUcid(), restriction.getId(), "APPLIED");
        RestrictionPage.checkRestrictionApplymentAudit(restrictionClient.getUcid());
    }

    @Test
    @DisplayName("Set restriction 'Internal transfer' API")
    @Feature("BMS-755 Add new restrictions")
    @Owner("DMITRI KALACHEV")
    @AllureId("923")
    void setRestrictionApiInternalTransferTest() throws Exception {
        Restriction restriction = Restriction.INTERNAL_TRANSFER;
        String applyReason = "reason" + getCurrentTimestampSeconds();
        String updatedBySystem = "system" + getCurrentTimestampSeconds();
        String updatedByUser = "user" + getCurrentTimestampSeconds();
        setRestrictionAPIGeneralResponse(restrictionClient.getUcid(), restriction.getCode(), applyReason, updatedBySystem, updatedByUser);
        RestrictionPage.checkRestrictionApplymentAudit(restrictionClient.getUcid());
        RestrictionPage.checkKafkaRequestApplyUserId(restrictionClient.getUserId());
        RestrictionPage.checkUserHaveRestrictionGeneral(restrictionClient.getUcid(), restriction.getId(), "APPLIED");
    }

    @Test
    @DisplayName("Set restriction 'Deposits' API")
    @Feature("BMS-755 Add new restrictions")
    @Owner("DMITRI KALACHEV")
    @AllureId("924")
    void setRestrictionApiDepositsTest() throws Exception {
        Restriction restriction = Restriction.DEPOSITS;
        String applyReason = "reason" + getCurrentTimestampSeconds();
        String updatedBySystem = "system" + getCurrentTimestampSeconds();
        String updatedByUser = "user" + getCurrentTimestampSeconds();
        setRestrictionAPIGeneralResponse(restrictionClient.getUcid(), restriction.getCode(), applyReason, updatedBySystem, updatedByUser);
        RestrictionPage.checkRestrictionApplymentAudit(restrictionClient.getUcid());
        RestrictionPage.checkKafkaRequestApplyUserId(restrictionClient.getUserId());
        RestrictionPage.checkUserHaveRestrictionGeneral(restrictionClient.getUcid(), restriction.getId(), "APPLIED");
    }

    @Test
    @DisplayName("Set restriction 'Withdrawals' API")
    @Feature("BMS-755 Add new restrictions")
    @Owner("DMITRI KALACHEV")
    @AllureId("925")
    void setRestrictionApiWithdrawalsTest() throws Exception {
        Restriction restriction = Restriction.WITHDRAWALS;
        String applyReason = "reason" + getCurrentTimestampSeconds();
        String updatedBySystem = "system" + getCurrentTimestampSeconds();
        String updatedByUser = "user" + getCurrentTimestampSeconds();
        setRestrictionAPIGeneralResponse(restrictionClient.getUcid(), restriction.getCode(), applyReason, updatedBySystem, updatedByUser);
        RestrictionPage.checkRestrictionApplymentAudit(restrictionClient.getUcid());
        RestrictionPage.checkKafkaRequestApplyUserId(restrictionClient.getUserId());
        RestrictionPage.checkUserHaveRestrictionGeneral(restrictionClient.getUcid(), restriction.getId(), "APPLIED");
    }

    @Test
    @DisplayName("Set restriction 'Login CRM' API")
    @Feature("BMS-755 Add new restrictions")
    @Owner("DMITRI KALACHEV")
    @AllureId("925")
    void setRestrictionApiLoginCrmTest() throws Exception {
        Restriction restriction = Restriction.LOGIN_CRM;
        String applyReason = "reason" + getCurrentTimestampSeconds();
        String updatedBySystem = "system" + getCurrentTimestampSeconds();
        String updatedByUser = "user" + getCurrentTimestampSeconds();
        setRestrictionAPIGeneralResponse(restrictionClient.getUcid(), restriction.getCode(), applyReason, updatedBySystem, updatedByUser);
        RestrictionPage.checkRestrictionApplymentAudit(restrictionClient.getUcid());
        RestrictionPage.checkKafkaRequestApplyUserId(restrictionClient.getUserId());
        RestrictionPage.checkUserHaveRestrictionGeneral(restrictionClient.getUcid(), restriction.getId(), "APPLIED");
    }

    @Test
    @DisplayName("Set restriction 'Close only mode' API")
    @Feature("BMS-755 Add new restrictions")
    @Owner("DMITRI KALACHEV")
    @AllureId("926")
    void setRestrictionApiCloseOnlyModeTest() throws Exception {
        Restriction restriction = Restriction.CLOSE_ONLY_MODE;
        String applyReason = "reason" + getCurrentTimestampSeconds();
        String updatedBySystem = "system" + getCurrentTimestampSeconds();
        String updatedByUser = "user" + getCurrentTimestampSeconds();
        String restrictionIdRaw = setRestrictionAPITradeResponse(restrictionClient.getUcid(), restriction.getCode(), restrictionClient.getTradingAccount(), restrictionClient.getServerId(), applyReason, updatedBySystem, updatedByUser);
        int restrictionID = Integer.parseInt(restrictionIdRaw.split(":")[1].replace("}", ""));
        RestrictionPage.checkRestrictionApplymentAudit(restrictionClient.getUcid());
        RestrictionPage.checkKafkaRequestApplyAccount(restrictionClient.getTradingAccount(), restrictionClient.getServerId(), 525_600, restrictionID, applyReason, restriction.getCode());
        RestrictionPage.checkUserHaveRestrictionTrading(restrictionClient.getUcid(), restriction.getId());
    }

    @Test
    @DisplayName("Set restriction 'B-Book -> A-Book' API")
    @Feature("BMS-755 Add new restrictions")
    @Owner("DMITRI KALACHEV")
    @AllureId("926")
    void setRestrictionApiBaBookTest() throws Exception {
        Restriction restriction = Restriction.B_BOOK_TO_A_BOOK;
        String applyReason = "reason" + getCurrentTimestampSeconds();
        String updatedBySystem = "system" + getCurrentTimestampSeconds();
        String updatedByUser = "user" + getCurrentTimestampSeconds();
        String restrictionIdRaw = setRestrictionAPITradeResponse(restrictionClient.getUcid(), restriction.getCode(), restrictionClient.getTradingAccount(), restrictionClient.getServerId(), applyReason, updatedBySystem, updatedByUser);
        int restrictionID = Integer.parseInt(restrictionIdRaw.split(":")[1].replace("}", ""));
        RestrictionPage.checkRestrictionApplymentAudit(restrictionClient.getUcid());
        RestrictionPage.checkKafkaRequestApplyAccount(restrictionClient.getTradingAccount(), restrictionClient.getServerId(), 525_600, restrictionID, applyReason, restriction.getCode());
        RestrictionPage.checkUserHaveRestrictionTrading(restrictionClient.getUcid(), restriction.getId());
    }

    @Test
    @DisplayName("Set restriction 'Manual Withdrawal Review' API")
    @Feature("BMS-755 Add new restrictions")
    @Owner("DMITRI KALACHEV")
    @AllureId("927")
    void setRestrictionApiManualWithdrawalReviewTest() throws Exception {
        Restriction restriction = Restriction.MANUAL_WITHDRAWAL_REVIEW;
        String applyReason = "reason" + getCurrentTimestampSeconds();
        String updatedBySystem = "system" + getCurrentTimestampSeconds();
        String updatedByUser = "user" + getCurrentTimestampSeconds();
        setRestrictionAPIGeneralResponse(restrictionClient.getUcid(), restriction.getCode(), applyReason, updatedBySystem, updatedByUser);
        RestrictionPage.checkRestrictionApplymentAudit(restrictionClient.getUcid());
        RestrictionPage.checkKafkaRequestApplyUserId(restrictionClient.getUserId());
        RestrictionPage.checkUserHaveRestrictionGeneral(restrictionClient.getUcid(), restriction.getId(), "APPLIED");
    }

    @Test
    @DisplayName("Set restriction 'Credit and Bonus' API")
    @Feature("BMS-755 Add new restrictions")
    @Owner("DMITRI KALACHEV")
    @AllureId("928")
    void setRestrictionApiCreditAndBonusTest() throws Exception {
        Restriction restriction = Restriction.CREDIT_AND_BONUS;
        String applyReason = "reason" + getCurrentTimestampSeconds();
        String updatedBySystem = "system" + getCurrentTimestampSeconds();
        String updatedByUser = "user" + getCurrentTimestampSeconds();
        setRestrictionAPIGeneralResponse(restrictionClient.getUcid(), restriction.getCode(), applyReason, updatedBySystem, updatedByUser);
        RestrictionPage.checkRestrictionApplymentAudit(restrictionClient.getUcid());
        RestrictionPage.checkKafkaRequestApplyUserId(restrictionClient.getUserId());
        RestrictionPage.checkUserHaveRestrictionGeneral(restrictionClient.getUcid(), restriction.getId(), "APPLIED");
    }

    @Test
    @DisplayName("Set restriction 'Read-only mode' API")
    @Feature("BMS-755 Add new restrictions")
    @Owner("DMITRI KALACHEV")
    @AllureId("930")
    void setRestrictionApiReadOnlyModeTest() throws Exception {
        Restriction restriction = Restriction.READ_ONLY_MODE;
        String applyReason = "reason" + getCurrentTimestampSeconds();
        String updatedBySystem = "system" + getCurrentTimestampSeconds();
        String updatedByUser = "user" + getCurrentTimestampSeconds();
        String restrictionIdRaw = setRestrictionAPITradeResponse(restrictionClient.getUcid(), restriction.getCode(), restrictionClient.getTradingAccount(), restrictionClient.getServerId(), applyReason, updatedBySystem, updatedByUser);
        int restrictionID = Integer.parseInt(restrictionIdRaw.split(":")[1].replace("}", ""));
        RestrictionPage.checkRestrictionApplymentAudit(restrictionClient.getUcid());
        RestrictionPage.checkKafkaRequestApplyAccount(restrictionClient.getTradingAccount(), restrictionClient.getServerId(), 525_600, restrictionID, applyReason, restriction.getCode());
        RestrictionPage.checkUserHaveRestrictionTrading(restrictionClient.getUcid(), restriction.getId());
    }

    @Test
    @DisplayName("Set restriction 'Off quotes' API")
    @Feature("BMS-755 Add new restrictions")
    @Owner("DMITRI KALACHEV")
    @AllureId("931")
    void setRestrictionApiOffQuotesTest() throws Exception {
        Restriction restriction = Restriction.OFF_QUOTES;
        String applyReason = "reason" + getCurrentTimestampSeconds();
        String updatedBySystem = "system" + getCurrentTimestampSeconds();
        String updatedByUser = "user" + getCurrentTimestampSeconds();
        String restrictionIdRaw = setRestrictionAPITradeResponse(restrictionClient.getUcid(), restriction.getCode(), restrictionClient.getTradingAccount(), restrictionClient.getServerId(), applyReason, updatedBySystem, updatedByUser);
        int restrictionID = Integer.parseInt(restrictionIdRaw.split(":")[1].replace("}", ""));
        RestrictionPage.checkRestrictionApplymentAudit(restrictionClient.getUcid());
        RestrictionPage.checkKafkaRequestApplyAccount(restrictionClient.getTradingAccount(), restrictionClient.getServerId(), 525_600, restrictionID, applyReason, restriction.getCode());
        RestrictionPage.checkUserHaveRestrictionTrading(restrictionClient.getUcid(), restriction.getId());
    }

    @Test
    @DisplayName("Set restriction 'Trading hours' API")
    @Feature("BMS-755 Add new restrictions")
    @Owner("DMITRI KALACHEV")
    @AllureId("1148")
    void setRestrictionApiTradingHoursTest() throws Exception {
        Restriction restriction = Restriction.TRADING_HOURS;
        String applyReason = "reason" + getCurrentTimestampSeconds();
        String updatedBySystem = "system" + getCurrentTimestampSeconds();
        String updatedByUser = "user" + getCurrentTimestampSeconds();
        String restrictionIdRaw = setRestrictionAPITradeResponse(restrictionClient.getUcid(), restriction.getCode(), restrictionClient.getTradingAccount(), restrictionClient.getServerId(), applyReason, updatedBySystem, updatedByUser);
        int restrictionID = Integer.parseInt(restrictionIdRaw.split(":")[1].replace("}", ""));
        RestrictionPage.checkRestrictionApplymentAudit(restrictionClient.getUcid());
        RestrictionPage.checkKafkaRequestApplyAccount(restrictionClient.getTradingAccount(), restrictionClient.getServerId(), 525_600, restrictionID, applyReason, restriction.getCode());
        RestrictionPage.checkUserHaveRestrictionTrading(restrictionClient.getUcid(), restriction.getId());
    }

    @Test
    @DisplayName("Set restriction 'Login MT' API")
    @Feature("BMS-755 Add new restrictions")
    @Owner("DMITRI KALACHEV")
    @AllureId("932")
    void setRestrictionApiLoginMTTest() throws Exception {
        Restriction restriction = Restriction.LOGIN_MT;
        String applyReason = "reason" + getCurrentTimestampSeconds();
        String updatedBySystem = "system" + getCurrentTimestampSeconds();
        String updatedByUser = "user" + getCurrentTimestampSeconds();
        String restrictionIdRaw = setRestrictionAPITradeResponse(restrictionClient.getUcid(), restriction.getCode(), restrictionClient.getTradingAccount(), restrictionClient.getServerId(), applyReason, updatedBySystem, updatedByUser);
        int restrictionID = Integer.parseInt(restrictionIdRaw.split(":")[1].replace("}", ""));
        RestrictionPage.checkRestrictionApplymentAudit(restrictionClient.getUcid());
        RestrictionPage.checkKafkaRequestApplyAccount(restrictionClient.getTradingAccount(), restrictionClient.getServerId(), 525_600, restrictionID, applyReason, restriction.getCode());
        RestrictionPage.checkUserHaveRestrictionTrading(restrictionClient.getUcid(), restriction.getId());
    }

    @Test
    @DisplayName("Set restriction 'Note for withdrawals' API")
    @Feature("BMS-755 Add new restrictions")
    @Owner("DMITRI KALACHEV")
    @AllureId("933")
    void setRestrictionApiNoteForWithdrawalsTest() throws Exception {
        Restriction restriction = Restriction.NOTE_FOR_WITHDRAWALS;
        String applyReason = "reason" + getCurrentTimestampSeconds();
        String updatedBySystem = "system" + getCurrentTimestampSeconds();
        String updatedByUser = "user" + getCurrentTimestampSeconds();
        setRestrictionAPIGeneralResponse(restrictionClient.getUcid(), restriction.getCode(), applyReason, updatedBySystem, updatedByUser);
        RestrictionPage.checkRestrictionApplymentAudit(restrictionClient.getUcid());
        RestrictionPage.checkKafkaRequestApplyUserId(restrictionClient.getUserId());
        RestrictionPage.checkUserHaveRestrictionGeneral(restrictionClient.getUcid(), restriction.getId(), "APPLIED");
    }

    @Test
    @DisplayName("Set restriction 'Group Change' API")
    @Feature("BMS-755 Add new restrictions")
    @Owner("DMITRI KALACHEV")
    @AllureId("934")
    void setRestrictionApiGroupChangeTest() throws Exception {
        Restriction restriction = Restriction.GROUP_CHANGE;
        String applyReason = "reason" + getCurrentTimestampSeconds();
        String updatedBySystem = "system" + getCurrentTimestampSeconds();
        String updatedByUser = "user" + getCurrentTimestampSeconds();
        String restrictionIdRaw = setRestrictionAPITradeResponse(restrictionClient.getUcid(), restriction.getCode(), restrictionClient.getTradingAccount(), restrictionClient.getServerId(), applyReason, updatedBySystem, updatedByUser);
        int restrictionID = Integer.parseInt(restrictionIdRaw.split(":")[1].replace("}", ""));
        RestrictionPage.checkRestrictionApplymentAudit(restrictionClient.getUcid());
        RestrictionPage.checkKafkaRequestApplyAccount(restrictionClient.getTradingAccount(), restrictionClient.getServerId(), 525_600, restrictionID, applyReason, restriction.getCode());
        RestrictionPage.checkUserHaveRestrictionTrading(restrictionClient.getUcid(), restriction.getId());
    }

    @Test
    @DisplayName("Set restriction 'KYC' API")
    @Feature("BMS-755 Add new restrictions")
    @Owner("DMITRI KALACHEV")
    @AllureId("935")
    void setRestrictionApiKYCTest() throws Exception {
        Restriction restriction = Restriction.KYC;
        String applyReason = "reason" + getCurrentTimestampSeconds();
        String updatedBySystem = "system" + getCurrentTimestampSeconds();
        String updatedByUser = "user" + getCurrentTimestampSeconds();
        setRestrictionAPIGeneralResponse(restrictionClient.getUcid(), restriction.getCode(), applyReason, updatedBySystem, updatedByUser);
        RestrictionPage.checkRestrictionApplymentAudit(restrictionClient.getUcid());
        RestrictionPage.checkKafkaRequestApplyUserId(restrictionClient.getUserId());
        RestrictionPage.checkUserHaveRestrictionGeneral(restrictionClient.getUcid(), restriction.getId(), "APPLIED");
    }

    @Test
    @DisplayName("Set restriction 'Leverage' API")
    @Feature("BMS-755 Add new restrictions")
    @Owner("DMITRI KALACHEV")
    @AllureId("936")
    void setRestrictionApiLeverageTest() throws Exception {
        Restriction restriction = Restriction.LEVERAGE;
        String applyReason = "reason" + getCurrentTimestampSeconds();
        String updatedBySystem = "system" + getCurrentTimestampSeconds();
        String updatedByUser = "user" + getCurrentTimestampSeconds();
        setRestrictionAPIGeneralResponse(restrictionClient.getUcid(), restriction.getCode(), applyReason, updatedBySystem, updatedByUser);
        RestrictionPage.checkRestrictionApplymentAudit(restrictionClient.getUcid());
        RestrictionPage.checkKafkaRequestApplyUserId(restrictionClient.getUserId());
        RestrictionPage.checkUserHaveRestrictionGeneral(restrictionClient.getUcid(), restriction.getId(), "APPLIED");
    }

    @Test
    @DisplayName("Set restriction 'Warning Letter' API")
    @Feature("BMS-755 Add new restrictions")
    @Owner("DMITRI KALACHEV")
    @AllureId("937")
    void setRestrictionApiWarningLetterTest() throws Exception {
        Restriction restriction = Restriction.WARNING_LETTER;
        String applyReason = "reason" + getCurrentTimestampSeconds();
        String updatedBySystem = "system" + getCurrentTimestampSeconds();
        String updatedByUser = "user" + getCurrentTimestampSeconds();
        setRestrictionAPIGeneralResponse(restrictionClient.getUcid(), restriction.getCode(), applyReason, updatedBySystem, updatedByUser);
        RestrictionPage.checkRestrictionApplymentAudit(restrictionClient.getUcid());
        RestrictionPage.checkKafkaRequestApplyUserId(restrictionClient.getUserId());
        RestrictionPage.checkUserHaveRestrictionGeneral(restrictionClient.getUcid(), restriction.getId(), "APPLIED");
    }

    @Test
    @DisplayName("Set restriction 'Remove swap free option' API")
    @Feature("BMS-755 Add new restrictions")
    @Owner("DMITRI KALACHEV")
    @AllureId("938")
    void setRestrictionApiRemoveSwapFreeOptionTest() throws Exception {
        Restriction restriction = Restriction.GROUP_CHANGE;
        String applyReason = "reason" + getCurrentTimestampSeconds();
        String updatedBySystem = "system" + getCurrentTimestampSeconds();
        String updatedByUser = "user" + getCurrentTimestampSeconds();
        String restrictionIdRaw = setRestrictionAPITradeResponse(restrictionClient.getUcid(), restriction.getCode(), restrictionClient.getTradingAccount(), restrictionClient.getServerId(), applyReason, updatedBySystem, updatedByUser);
        int restrictionID = Integer.parseInt(restrictionIdRaw.split(":")[1].replace("}", ""));
        RestrictionPage.checkRestrictionApplymentAudit(restrictionClient.getUcid());
        RestrictionPage.checkKafkaRequestApplyAccount(restrictionClient.getTradingAccount(), restrictionClient.getServerId(), 525_600, restrictionID, applyReason, restriction.getCode());
        RestrictionPage.checkUserHaveRestrictionTrading(restrictionClient.getUcid(), restriction.getId());
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
            writeLog((kafkaMessage.restrictions[0].internalReason));
            if ((kafkaMessage.restrictions.length == 1) && Objects.equals(kafkaMessage.restrictions[0].internalReason, expectedInternalReason)) {
                internalReasonFound = true;
            }
        }
        assertThat("Verify internalReason was found in one of the kafka messages ", internalReasonFound, is(true));
    }

    @AllureId("1486")
    @DisplayName("cancellation message for byBit restrictions appears in Lark")
    @Test
    void byBitCancellationLarkTest() throws IOException {
        String applyReason = "reason" + getCurrentTimestampSeconds();
        String updatedBySystem = "system" + getCurrentTimestampSeconds();
        String updatedByUser = "user" + getCurrentTimestampSeconds();
        ClientBybitRestriction restrictionDb = new ClientBybitRestriction();
        restrictionDb.setUcid(byBitClient.getUcid());
        restrictionDb.setRestrictionId(1);
        restrictionDb.setAccount(byBitClient.getTradingAccount().toString());
        restrictionDb.setId(getRandomIntPositive() + 5000);
        restrictionDb.setStatus("APPLIED");
        restrictionDb.setTraceId(getRandomUuidString());
        restrictionDb.setComment(applyReason);
        restrictionDb.setUpdatedAt(LocalDateTime.now());
        restrictionDb.setCreatedAt(LocalDateTime.now());
        insertObjectToDb(DbName.POSTGRES, MITIGATION_CLIENT_BYBIT_RESTRICTION, restrictionDb);
        CancelRestrictionByBitRequest.UpdatedBy cancelBy = new CancelRestrictionByBitRequest.UpdatedBy(updatedByUser, updatedBySystem);
        CancelRestrictionByBitRequest cancelRequest = new CancelRestrictionByBitRequest(restrictionDb.getId(), applyReason, cancelBy);
        Response responseCancel = cancelRestrictionByIdByBit(cancelRequest);
        assertEquals(200, responseCancel.code());
        Response tenant = getTenantToken("cli_a829a3882cb8902f", "x3Tu9aG8DBY8XOQXc0WZneu8lQdauXR2");
        String token = objectMapper.readValue(tenant.body().string(), TenantAccessTokenResponse.class).getTenantAccessToken();
        Response messageHistory = getMessagesChatLast10Minutes(token, "oc_dfeae72f51c406fadbc16c3890678895");
        ChatHistoryResponse response = objectMapper.readValue(messageHistory.body().string(), ChatHistoryResponse.class);
        List<ChatHistoryResponse.LarkApiDataItem> items = response.getData().getItems();
        List<ChatHistoryResponse.LarkApiDataItem> itemsFiltered = items.stream().filter(i -> i.getBody().getContent().contains(applyReason)).toList();
        String clearedContent = itemsFiltered.getFirst().getBody().getContent().replace("\\n", "").replace("\\", "");
        ByBitRestrictionCancellationMessage message = objectMapper.readValue(clearedContent, ByBitRestrictionCancellationMessage.class);
        Allure.step("check that message contains accountId");
        assertEquals(": " + byBitClient.getTradingAccount(), message.getElements().getFirst().get(1).getText());
        Allure.step("check that message contains userId");
        assertEquals(": " + byBitClient.getUserId(), message.getElements().getFirst().get(3).getText());
        Allure.step("check that message contains cancellation reason");
        assertEquals(": " + applyReason, message.getElements().getFirst().get(5).getText());
    }


}
