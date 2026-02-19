package tests.mitigation_service_api_tests;

import static business_objects.api.lark.LarkRequest.getMessagesChatLast10Minutes;
import static business_objects.api.lark.LarkRequest.getTenantToken;
import static business_objects.api.mitigation_service.MitigationServiceRequest.*;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateStaticCrmTbAccountActive;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateStaticUserByClient;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static helpers.api.RestrictionHelper.setRestrictionAPIGeneralResponse;
import static helpers.api.RestrictionHelper.setRestrictionAPITradeResponse;
import static helpers.data.ClientFactory.getRandomBybitClient;
import static helpers.data.enums.FraudType.BONUS_ABUSE;
import static helpers.data.enums.FraudType.CPA_ABUSE;
import static helpers.data.enums.FraudType.GAP_TRADING;
import static helpers.data.enums.FraudType.HEDGING;
import static helpers.data.enums.FraudType.LATENCY_ARBITRAGE;
import static helpers.data.enums.FraudType.LOSS_VOUCHER_ABUSE;
import static helpers.data.enums.FraudType.NBP_ABUSE;
import static helpers.data.enums.InternalReason.*;
import static helpers.data.enums.InternalReason.EXCHANGER;
import static helpers.data.enums.InternalReason.UPGRADER;
import static helpers.data.enums.Restriction.ACCOUNT_CREATION;
import static helpers.data.enums.Restriction.DEPOSITS;
import static helpers.data.enums.Restriction.INTERNAL_TRANSFER;
import static helpers.data.enums.TradingEnvironmentLevel.LOW;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;
import static utils.Constants.*;
import static utils.Utils.*;

import business_objects.api.lark.TenantAccessToken.TenantAccessTokenResponse;
import business_objects.api.lark.chatHistory.ByBitRestrictionBotMessage;
import business_objects.api.lark.chatHistory.ChatHistoryResponse;
import business_objects.api.mitigation_service.*;
import business_objects.api.mitigation_service.CancelRestrictionByBitRequest;
import business_objects.api.mitigation_service.PostRestrictionByBitRequest;
import business_objects.api.mitigation_service.PostRestrictionByBitResponse;
import business_objects.api.mitigation_service.PostRestrictionRequestBody;
import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.kafka.restriction_events.ApplyTradingEnvironmentRestrictionMessage;
import business_objects.kafka.restriction_events.ClientRestrictionApply;
import com.fasterxml.jackson.core.type.TypeReference;
import helpers.api.RestrictionHelper;
import helpers.data.ClientHelper;
import helpers.data.enums.*;
import helpers.database.AuHelper;
import helpers.database.CleanTableHelper;
import helpers.kafka.KafkaHelper;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import java.io.IOException;
import java.math.BigInteger;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import okhttp3.Response;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.*;
import page_objects.backoffice_pages.investigationTool.RestrictionPage;
import tests.TestBaseApi;

@Tag(TEAM_BACKOFFICE)
@Tag(LAYER_API)
@Tag(SUITE_MITIGATION_SERVICE)
class MitigationServiceApiTest extends TestBaseApi {

    static ClientHelper restrictionClient;

    static {
        restrictionClient = ClientHelper.builder()
                .userId(141_401)
                .uid("063cde3b-ea9d-48b5-8e2c-99f3d5f67999")
                .brand(Brand.VANTAGE)
                .regulator(Regulator.VFSC2)
                .tradingAccount(14_140_101)
                .serverId(42)
                .build();
    }

    static ClientHelper byBitClient = getRandomBybitClient();
    static CrmTbUserObject byBitUser = generateUserByClient(byBitClient);
    static CrmTbAccountObject activeByBit = generateStaticCrmTbAccountActive(byBitClient);

    @BeforeAll
    static void initialSetup() throws IOException, InterruptedException {
        objectMapper.findAndRegisterModules();
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
        CleanTableHelper.cleanUserRestrictionTradingEnv(restrictionClient.getUcid());
        CleanTableHelper.cleanUserAudit(restrictionClient.getUcid());
    }

    @AfterAll
    static void breakdown() throws Exception {
        CleanTableHelper.cleanUserRestrictionGeneral(restrictionClient.getUcid());
        CleanTableHelper.cleanUserRestrictionGeneral(byBitClient.getUcid());
        CleanTableHelper.cleanUserRestrictionTrading(restrictionClient.getUcid());
        CleanTableHelper.cleanUserRestrictionTradingEnv(restrictionClient.getUcid());
        CleanTableHelper.cleanUserAudit(restrictionClient.getUcid());
        CleanTableHelper.cleanUserAudit(byBitClient.getUcid());
        deleteObjectFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", byBitClient.getUcid()));
    }

    @Test
    @DisplayName("Restriction Set PUT request returns 200 if restriction already applied")
    @AllureId("920")
    void successIfRestrictionAlreadyApplied() throws Exception {

        PostRestrictionRequestBody postRestrictionRequestBody = new PostRestrictionRequestBody(
                restrictionClient.getUcid(),
                "05",
                "GENERAL",
                null,
                null,
                "Integration test",
                new PostRestrictionRequestBody.UpdatedBy("API", "QA"));

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
    @AllureId("922")
    void setRestrictionApiOpenNewAccountTest() throws Exception {
        Restriction restriction = Restriction.ACCOUNT_CREATION_REVIEW;
        String applyReason = "reason" + getCurrentTimestampSeconds();
        String updatedBySystem = "system" + getCurrentTimestampSeconds();
        String updatedByUser = "user" + getCurrentTimestampSeconds();
        setRestrictionAPIGeneralResponse(
                restrictionClient.getUcid(), restriction.getCode(), applyReason, updatedBySystem, updatedByUser);
        RestrictionPage.checkKafkaRequestApplyUserId(restrictionClient.getUserId());
        RestrictionPage.checkUserHaveRestrictionGeneral(restrictionClient.getUcid(), restriction.getId(), "APPLIED");
        AuHelper.checkRestrictionApplyAudit(restrictionClient.getUcid());
    }

    @Test
    @DisplayName("Set restriction 'Internal transfer' API")
    @Feature("BMS-755 Add new restrictions")
    @AllureId("923")
    void setRestrictionApiInternalTransferTest() throws Exception {
        Restriction restriction = INTERNAL_TRANSFER;
        String applyReason = "reason" + getCurrentTimestampSeconds();
        String updatedBySystem = "system" + getCurrentTimestampSeconds();
        String updatedByUser = "user" + getCurrentTimestampSeconds();
        setRestrictionAPIGeneralResponse(
                restrictionClient.getUcid(), restriction.getCode(), applyReason, updatedBySystem, updatedByUser);
        AuHelper.checkRestrictionApplyAudit(restrictionClient.getUcid());
        RestrictionPage.checkKafkaRequestApplyUserId(restrictionClient.getUserId());
        RestrictionPage.checkUserHaveRestrictionGeneral(restrictionClient.getUcid(), restriction.getId(), "APPLIED");
    }

    @Test
    @DisplayName("Set restriction 'Deposits' API")
    @Feature("BMS-755 Add new restrictions")
    @AllureId("924")
    void setRestrictionApiDepositsTest() throws Exception {
        Restriction restriction = DEPOSITS;
        String applyReason = "reason" + getCurrentTimestampSeconds();
        String updatedBySystem = "system" + getCurrentTimestampSeconds();
        String updatedByUser = "user" + getCurrentTimestampSeconds();
        setRestrictionAPIGeneralResponse(
                restrictionClient.getUcid(), restriction.getCode(), applyReason, updatedBySystem, updatedByUser);
        AuHelper.checkRestrictionApplyAudit(restrictionClient.getUcid());
        RestrictionPage.checkKafkaRequestApplyUserId(restrictionClient.getUserId());
        RestrictionPage.checkUserHaveRestrictionGeneral(restrictionClient.getUcid(), restriction.getId(), "APPLIED");
    }

    @Test
    @DisplayName("Set restriction 'Withdrawals' API")
    @Feature("BMS-755 Add new restrictions")
    @AllureId("925")
    void setRestrictionApiWithdrawalsTest() throws Exception {
        Restriction restriction = Restriction.WITHDRAWALS;
        String applyReason = "reason" + getCurrentTimestampSeconds();
        String updatedBySystem = "system" + getCurrentTimestampSeconds();
        String updatedByUser = "user" + getCurrentTimestampSeconds();
        setRestrictionAPIGeneralResponse(
                restrictionClient.getUcid(), restriction.getCode(), applyReason, updatedBySystem, updatedByUser);
        AuHelper.checkRestrictionApplyAudit(restrictionClient.getUcid());
        RestrictionPage.checkKafkaRequestApplyUserId(restrictionClient.getUserId());
        RestrictionPage.checkUserHaveRestrictionGeneral(restrictionClient.getUcid(), restriction.getId(), "APPLIED");
    }

    @Test
    @DisplayName("Set restriction 'Login CRM' API")
    @Feature("BMS-755 Add new restrictions")
    @AllureId("925")
    void setRestrictionApiLoginCrmTest() throws Exception {
        Restriction restriction = Restriction.LOGIN_CRM;
        String applyReason = "reason" + getCurrentTimestampSeconds();
        String updatedBySystem = "system" + getCurrentTimestampSeconds();
        String updatedByUser = "user" + getCurrentTimestampSeconds();
        setRestrictionAPIGeneralResponse(
                restrictionClient.getUcid(), restriction.getCode(), applyReason, updatedBySystem, updatedByUser);
        AuHelper.checkRestrictionApplyAudit(restrictionClient.getUcid());
        RestrictionPage.checkKafkaRequestApplyUserId(restrictionClient.getUserId());
        RestrictionPage.checkUserHaveRestrictionGeneral(restrictionClient.getUcid(), restriction.getId(), "APPLIED");
    }

    @Test
    @DisplayName("Set restriction 'Close only mode' API")
    @Feature("BMS-755 Add new restrictions")
    @AllureId("926")
    void setRestrictionApiCloseOnlyModeTest() throws Exception {
        Restriction restriction = Restriction.CLOSE_ONLY_MODE;
        String applyReason = "reason" + getCurrentTimestampSeconds();
        String updatedBySystem = "system" + getCurrentTimestampSeconds();
        String updatedByUser = "user" + getCurrentTimestampSeconds();
        String restrictionIdRaw = setRestrictionAPITradeResponse(
                restrictionClient.getUcid(),
                restriction.getCode(),
                restrictionClient.getTradingAccount(),
                restrictionClient.getServerId(),
                applyReason,
                updatedBySystem,
                updatedByUser);
        int restrictionID = Integer.parseInt(restrictionIdRaw.split(":")[1].replace("}", ""));
        AuHelper.checkRestrictionApplyAudit(restrictionClient.getUcid());
        RestrictionPage.checkKafkaRequestApplyAccount(
                restrictionClient.getTradingAccount(),
                restrictionClient.getServerId(),
                525_600,
                restrictionID,
                applyReason,
                restriction.getCode());
        RestrictionPage.checkUserHaveRestrictionTrading(restrictionClient.getUcid(), restriction.getId());
    }

    @Test
    @DisplayName("Set restriction 'B-Book -> A-Book' API")
    @Feature("BMS-755 Add new restrictions")
    @AllureId("926")
    void setRestrictionApiBaBookTest() throws Exception {
        Restriction restriction = Restriction.B_BOOK_TO_A_BOOK;
        String applyReason = "reason" + getCurrentTimestampSeconds();
        String updatedBySystem = "system" + getCurrentTimestampSeconds();
        String updatedByUser = "user" + getCurrentTimestampSeconds();
        String restrictionIdRaw = setRestrictionAPITradeResponse(
                restrictionClient.getUcid(),
                restriction.getCode(),
                restrictionClient.getTradingAccount(),
                restrictionClient.getServerId(),
                applyReason,
                updatedBySystem,
                updatedByUser);
        int restrictionID = Integer.parseInt(restrictionIdRaw.split(":")[1].replace("}", ""));
        AuHelper.checkRestrictionApplyAudit(restrictionClient.getUcid());
        RestrictionPage.checkKafkaRequestApplyAccount(
                restrictionClient.getTradingAccount(),
                restrictionClient.getServerId(),
                525_600,
                restrictionID,
                applyReason,
                restriction.getCode());
        RestrictionPage.checkUserHaveRestrictionTrading(restrictionClient.getUcid(), restriction.getId());
    }

    @Test
    @DisplayName("Set restriction 'Manual Withdrawal Review' API")
    @Feature("BMS-755 Add new restrictions")
    @AllureId("927")
    void setRestrictionApiManualWithdrawalReviewTest() throws Exception {
        Restriction restriction = Restriction.MANUAL_WITHDRAWAL_REVIEW;
        String applyReason = "reason" + getCurrentTimestampSeconds();
        String updatedBySystem = "system" + getCurrentTimestampSeconds();
        String updatedByUser = "user" + getCurrentTimestampSeconds();
        setRestrictionAPIGeneralResponse(
                restrictionClient.getUcid(), restriction.getCode(), applyReason, updatedBySystem, updatedByUser);
        AuHelper.checkRestrictionApplyAudit(restrictionClient.getUcid());
        RestrictionPage.checkKafkaRequestApplyUserId(restrictionClient.getUserId());
        RestrictionPage.checkUserHaveRestrictionGeneral(restrictionClient.getUcid(), restriction.getId(), "APPLIED");
    }

    @Test
    @DisplayName("Set restriction 'Credit and Bonus' API")
    @Feature("BMS-755 Add new restrictions")
    @AllureId("928")
    void setRestrictionApiCreditAndBonusTest() throws Exception {
        Restriction restriction = Restriction.CREDIT_AND_BONUS;
        String applyReason = "reason" + getCurrentTimestampSeconds();
        String updatedBySystem = "system" + getCurrentTimestampSeconds();
        String updatedByUser = "user" + getCurrentTimestampSeconds();
        setRestrictionAPIGeneralResponse(
                restrictionClient.getUcid(), restriction.getCode(), applyReason, updatedBySystem, updatedByUser);
        AuHelper.checkRestrictionApplyAudit(restrictionClient.getUcid());
        RestrictionPage.checkKafkaRequestApplyUserId(restrictionClient.getUserId());
        RestrictionPage.checkUserHaveRestrictionGeneral(restrictionClient.getUcid(), restriction.getId(), "APPLIED");
    }

    @Test
    @DisplayName("Set restriction 'Read-only mode' API")
    @Feature("BMS-755 Add new restrictions")
    @AllureId("930")
    void setRestrictionApiReadOnlyModeTest() throws Exception {
        Restriction restriction = Restriction.READ_ONLY_MODE;
        String applyReason = "reason" + getCurrentTimestampSeconds();
        String updatedBySystem = "system" + getCurrentTimestampSeconds();
        String updatedByUser = "user" + getCurrentTimestampSeconds();
        String restrictionIdRaw = setRestrictionAPITradeResponse(
                restrictionClient.getUcid(),
                restriction.getCode(),
                restrictionClient.getTradingAccount(),
                restrictionClient.getServerId(),
                applyReason,
                updatedBySystem,
                updatedByUser);
        int restrictionID = Integer.parseInt(restrictionIdRaw.split(":")[1].replace("}", ""));
        AuHelper.checkRestrictionApplyAudit(restrictionClient.getUcid());
        RestrictionPage.checkKafkaRequestApplyAccount(
                restrictionClient.getTradingAccount(),
                restrictionClient.getServerId(),
                525_600,
                restrictionID,
                applyReason,
                restriction.getCode());
        RestrictionPage.checkUserHaveRestrictionTrading(restrictionClient.getUcid(), restriction.getId());
    }

    @Test
    @DisplayName("Set restriction 'Off quotes' API")
    @Feature("BMS-755 Add new restrictions")
    @AllureId("931")
    void setRestrictionApiOffQuotesTest() throws Exception {
        Restriction restriction = Restriction.OFF_QUOTES;
        String applyReason = "reason" + getCurrentTimestampSeconds();
        String updatedBySystem = "system" + getCurrentTimestampSeconds();
        String updatedByUser = "user" + getCurrentTimestampSeconds();
        String restrictionIdRaw = setRestrictionAPITradeResponse(
                restrictionClient.getUcid(),
                restriction.getCode(),
                restrictionClient.getTradingAccount(),
                restrictionClient.getServerId(),
                applyReason,
                updatedBySystem,
                updatedByUser);
        int restrictionID = Integer.parseInt(restrictionIdRaw.split(":")[1].replace("}", ""));
        AuHelper.checkRestrictionApplyAudit(restrictionClient.getUcid());
        RestrictionPage.checkKafkaRequestApplyAccount(
                restrictionClient.getTradingAccount(),
                restrictionClient.getServerId(),
                525_600,
                restrictionID,
                applyReason,
                restriction.getCode());
        RestrictionPage.checkUserHaveRestrictionTrading(restrictionClient.getUcid(), restriction.getId());
    }

    @Test
    @DisplayName("Set restriction 'Trading hours' API")
    @Feature("BMS-755 Add new restrictions")
    @AllureId("1148")
    void setRestrictionApiTradingHoursTest() throws Exception {
        Restriction restriction = Restriction.TRADING_HOURS;
        String applyReason = "reason" + getCurrentTimestampSeconds();
        String updatedBySystem = "system" + getCurrentTimestampSeconds();
        String updatedByUser = "user" + getCurrentTimestampSeconds();
        String restrictionIdRaw = setRestrictionAPITradeResponse(
                restrictionClient.getUcid(),
                restriction.getCode(),
                restrictionClient.getTradingAccount(),
                restrictionClient.getServerId(),
                applyReason,
                updatedBySystem,
                updatedByUser);
        int restrictionID = Integer.parseInt(restrictionIdRaw.split(":")[1].replace("}", ""));
        AuHelper.checkRestrictionApplyAudit(restrictionClient.getUcid());
        RestrictionPage.checkKafkaRequestApplyAccount(
                restrictionClient.getTradingAccount(),
                restrictionClient.getServerId(),
                525_600,
                restrictionID,
                applyReason,
                restriction.getCode());
        RestrictionPage.checkUserHaveRestrictionTrading(restrictionClient.getUcid(), restriction.getId());
    }

    @Test
    @DisplayName("Set restriction 'Login MT' API")
    @Feature("BMS-755 Add new restrictions")
    @AllureId("932")
    void setRestrictionApiLoginMTTest() throws Exception {
        Restriction restriction = Restriction.LOGIN_MT;
        String applyReason = "reason" + getCurrentTimestampSeconds();
        String updatedBySystem = "system" + getCurrentTimestampSeconds();
        String updatedByUser = "user" + getCurrentTimestampSeconds();
        String restrictionIdRaw = setRestrictionAPITradeResponse(
                restrictionClient.getUcid(),
                restriction.getCode(),
                restrictionClient.getTradingAccount(),
                restrictionClient.getServerId(),
                applyReason,
                updatedBySystem,
                updatedByUser);
        int restrictionID = Integer.parseInt(restrictionIdRaw.split(":")[1].replace("}", ""));
        AuHelper.checkRestrictionApplyAudit(restrictionClient.getUcid());
        RestrictionPage.checkKafkaRequestApplyAccount(
                restrictionClient.getTradingAccount(),
                restrictionClient.getServerId(),
                525_600,
                restrictionID,
                applyReason,
                restriction.getCode());
        RestrictionPage.checkUserHaveRestrictionTrading(restrictionClient.getUcid(), restriction.getId());
    }

    @Test
    @DisplayName("Set restriction 'Note for withdrawals' API")
    @Feature("BMS-755 Add new restrictions")
    @AllureId("933")
    void setRestrictionApiNoteForWithdrawalsTest() throws Exception {
        Restriction restriction = Restriction.NOTE_FOR_WITHDRAWALS;
        String applyReason = "reason" + getCurrentTimestampSeconds();
        String updatedBySystem = "system" + getCurrentTimestampSeconds();
        String updatedByUser = "user" + getCurrentTimestampSeconds();
        setRestrictionAPIGeneralResponse(
                restrictionClient.getUcid(), restriction.getCode(), applyReason, updatedBySystem, updatedByUser);
        AuHelper.checkRestrictionApplyAudit(restrictionClient.getUcid());
        RestrictionPage.checkKafkaRequestApplyUserId(restrictionClient.getUserId());
        RestrictionPage.checkUserHaveRestrictionGeneral(restrictionClient.getUcid(), restriction.getId(), "APPLIED");
    }

    @Test
    @DisplayName("Set restriction 'Group Change' API")
    @Feature("BMS-755 Add new restrictions")
    @AllureId("934")
    void setRestrictionApiGroupChangeTest() throws Exception {
        Restriction restriction = Restriction.GROUP_CHANGE;
        String applyReason = "reason" + getCurrentTimestampSeconds();
        String updatedBySystem = "system" + getCurrentTimestampSeconds();
        String updatedByUser = "user" + getCurrentTimestampSeconds();
        String restrictionIdRaw = setRestrictionAPITradeResponse(
                restrictionClient.getUcid(),
                restriction.getCode(),
                restrictionClient.getTradingAccount(),
                restrictionClient.getServerId(),
                applyReason,
                updatedBySystem,
                updatedByUser);
        int restrictionID = Integer.parseInt(restrictionIdRaw.split(":")[1].replace("}", ""));
        AuHelper.checkRestrictionApplyAudit(restrictionClient.getUcid());
        RestrictionPage.checkKafkaRequestApplyAccount(
                restrictionClient.getTradingAccount(),
                restrictionClient.getServerId(),
                525_600,
                restrictionID,
                applyReason,
                restriction.getCode());
        RestrictionPage.checkUserHaveRestrictionTrading(restrictionClient.getUcid(), restriction.getId());
    }

    @Test
    @DisplayName("Set restriction 'KYC' API")
    @Feature("BMS-755 Add new restrictions")
    @AllureId("935")
    void setRestrictionApiKYCTest() throws Exception {
        Restriction restriction = Restriction.KYC;
        String applyReason = "reason" + getCurrentTimestampSeconds();
        String updatedBySystem = "system" + getCurrentTimestampSeconds();
        String updatedByUser = "user" + getCurrentTimestampSeconds();
        setRestrictionAPIGeneralResponse(
                restrictionClient.getUcid(), restriction.getCode(), applyReason, updatedBySystem, updatedByUser);
        AuHelper.checkRestrictionApplyAudit(restrictionClient.getUcid());
        RestrictionPage.checkKafkaRequestApplyUserId(restrictionClient.getUserId());
        RestrictionPage.checkUserHaveRestrictionGeneral(restrictionClient.getUcid(), restriction.getId(), "APPLIED");
    }

    @Test
    @DisplayName("Set restriction 'Leverage' API")
    @Feature("BMS-755 Add new restrictions")
    @AllureId("936")
    void setRestrictionApiLeverageTest() throws Exception {
        Restriction restriction = Restriction.LEVERAGE;
        String applyReason = "reason" + getCurrentTimestampSeconds();
        String updatedBySystem = "system" + getCurrentTimestampSeconds();
        String updatedByUser = "user" + getCurrentTimestampSeconds();
        setRestrictionAPIGeneralResponse(
                restrictionClient.getUcid(), restriction.getCode(), applyReason, updatedBySystem, updatedByUser);
        AuHelper.checkRestrictionApplyAudit(restrictionClient.getUcid());
        RestrictionPage.checkKafkaRequestApplyUserId(restrictionClient.getUserId());
        RestrictionPage.checkUserHaveRestrictionGeneral(restrictionClient.getUcid(), restriction.getId(), "APPLIED");
    }

    @Test
    @DisplayName("Set restriction 'Warning Letter' API")
    @Feature("BMS-755 Add new restrictions")
    @AllureId("937")
    void setRestrictionApiWarningLetterTest() throws Exception {
        Restriction restriction = Restriction.WARNING_LETTER;
        String applyReason = "reason" + getCurrentTimestampSeconds();
        String updatedBySystem = "system" + getCurrentTimestampSeconds();
        String updatedByUser = "user" + getCurrentTimestampSeconds();
        setRestrictionAPIGeneralResponse(
                restrictionClient.getUcid(), restriction.getCode(), applyReason, updatedBySystem, updatedByUser);
        AuHelper.checkRestrictionApplyAudit(restrictionClient.getUcid());
        RestrictionPage.checkKafkaRequestApplyUserId(restrictionClient.getUserId());
        RestrictionPage.checkUserHaveRestrictionGeneral(restrictionClient.getUcid(), restriction.getId(), "APPLIED");
    }

    @Test
    @DisplayName("Set restriction 'Remove swap free option' API")
    @Feature("BMS-755 Add new restrictions")
    @AllureId("938")
    void setRestrictionApiRemoveSwapFreeOptionTest() throws Exception {
        Restriction restriction = Restriction.GROUP_CHANGE;
        String applyReason = "reason" + getCurrentTimestampSeconds();
        String updatedBySystem = "system" + getCurrentTimestampSeconds();
        String updatedByUser = "user" + getCurrentTimestampSeconds();
        String restrictionIdRaw = setRestrictionAPITradeResponse(
                restrictionClient.getUcid(),
                restriction.getCode(),
                restrictionClient.getTradingAccount(),
                restrictionClient.getServerId(),
                applyReason,
                updatedBySystem,
                updatedByUser);
        int restrictionID = Integer.parseInt(restrictionIdRaw.split(":")[1].replace("}", ""));
        AuHelper.checkRestrictionApplyAudit(restrictionClient.getUcid());
        RestrictionPage.checkKafkaRequestApplyAccount(
                restrictionClient.getTradingAccount(),
                restrictionClient.getServerId(),
                525_600,
                restrictionID,
                applyReason,
                restriction.getCode());
        RestrictionPage.checkUserHaveRestrictionTrading(restrictionClient.getUcid(), restriction.getId());
    }

    @Test
    @AllureId("1075")
    @DisplayName("Verify logic for internalReason field with check format")
    void internalReasonTest() throws IOException, InterruptedException {
        PostRestrictionRequestBody postRestriction = new PostRestrictionRequestBody(
                restrictionClient.getUcid(),
                "05",
                "GENERAL",
                null,
                null,
                null,
                new PostRestrictionRequestBody.UpdatedBy("autotest", "autotest"),
                new PostRestrictionRequestBody.AdditionalParam[] {
                    new PostRestrictionRequestBody.AdditionalParam("connectionScore", "string", "0.75"),
                    new PostRestrictionRequestBody.AdditionalParam(
                            "potentialFraudTypes", "array", new String[] {"HEDGING"}),
                    new PostRestrictionRequestBody.AdditionalParam(
                            "confirmedFraudTypes", "array", new String[] {"PRICING_ERROR"})
                });
        Response response = postRestriction(postRestriction);
        assertThat("Verify 200 response code", response.code(), is(200));
        assertThat(response.body(), notNullValue());
        KafkaHelper kafka = new KafkaHelper();
        List<String> consumedMessages = kafka.consumeMessages(
                KAFKA_TOPIC_CLIENT_RESTRICTIONS_APPLY,
                restrictionClient.getUserId().toString());
        boolean internalReasonFound = false;
        String expectedInternalReason =
                """
                Potential Fraud Type: Hedging (mirror trading) - Description: Client are engaging in Hedging (Mirror trading) fraud in order to abuse our deposit bonus scheme and gain guaranteed profit through their trades.
                Confirmed Fraud Type: Pricing errors - Description: Client is taking advantage of errors in our quotes/pricing in order to make guaranteed profits.
                Restriction: Login CRM - Description: Considering the severity of certain clients' actions, their accounts need to be blocked completely. This can be relevant for more serious cases of Market manipulation, thin liquidity scalping, gap trading and more.
                Connection Score is 0.75""";
        for (String message : consumedMessages) {
            ClientRestrictionApply kafkaMessage = objectMapper.readValue(message, ClientRestrictionApply.class);
            writeLog((kafkaMessage.restrictions[0].internalReason));
            if ((kafkaMessage.restrictions.length == 1)
                    && Objects.equals(kafkaMessage.restrictions[0].internalReason, expectedInternalReason)) {
                internalReasonFound = true;
            }
        }
        assertThat("Verify internalReason was found in one of the kafka messages ", internalReasonFound, is(true));
    }

    @Test
    @AllureId("1972")
    @DisplayName("Verify logic for internalReason field restriction AccountCreation, fraud Hedging potential")
    void internalReasonTest1() throws IOException, InterruptedException {
        String fraudCode = HEDGING.getCode();
        String restrictionCode = ACCOUNT_CREATION.getCode();
        PostRestrictionRequestBody postRestriction = new PostRestrictionRequestBody(
                restrictionClient.getUcid(),
                restrictionCode,
                "GENERAL",
                null,
                null,
                null,
                new PostRestrictionRequestBody.UpdatedBy("autotest", "autotest"),
                new PostRestrictionRequestBody.AdditionalParam[] {
                    new PostRestrictionRequestBody.AdditionalParam("connectionScore", "string", "0.75"),
                    new PostRestrictionRequestBody.AdditionalParam(
                            "potentialFraudTypes", "array", new String[] {fraudCode})
                });
        Response response = postRestriction(postRestriction);
        assertThat("Verify 200 response code", response.code(), is(200));
        assertThat(response.body(), notNullValue());
        KafkaHelper kafka = new KafkaHelper();
        List<String> consumedMessages = kafka.consumeMessages(
                KAFKA_TOPIC_CLIENT_RESTRICTIONS_APPLY,
                restrictionClient.getUserId().toString());
        for (String message : consumedMessages) {
            ClientRestrictionApply kafkaMessage = objectMapper.readValue(message, ClientRestrictionApply.class);
            writeLog((kafkaMessage.restrictions[0].internalReason));
            String actualInternalReason = kafkaMessage.restrictions[0].internalReason;
            assertTrue(actualInternalReason.contains(InternalReason.HEDGING.getText()));
            assertTrue(actualInternalReason.contains(InternalReason.ACCOUNT_CREATION.getText()));
        }
    }

    @Test
    @AllureId("1973")
    @DisplayName(
            "Verify logic for internalReason field restriction INTERNAL_TRANSFER, fraud LATENCY_ARBITRAGE confirmed, CPA_ABUSE potential")
    void internalReasonTest2() throws IOException, InterruptedException {
        String fraudCode = LATENCY_ARBITRAGE.getCode();
        String fraudCode2 = CPA_ABUSE.getCode();
        String restrictionCode = INTERNAL_TRANSFER.getCode();
        PostRestrictionRequestBody postRestriction = new PostRestrictionRequestBody(
                restrictionClient.getUcid(),
                restrictionCode,
                "GENERAL",
                null,
                null,
                null,
                new PostRestrictionRequestBody.UpdatedBy("autotest", "autotest"),
                new PostRestrictionRequestBody.AdditionalParam[] {
                    new PostRestrictionRequestBody.AdditionalParam("connectionScore", "string", "0.75"),
                    new PostRestrictionRequestBody.AdditionalParam(
                            "potentialFraudTypes", "array", new String[] {fraudCode2}),
                    new PostRestrictionRequestBody.AdditionalParam(
                            "confirmedFraudTypes", "array", new String[] {fraudCode})
                });
        Response response = postRestriction(postRestriction);
        assertThat("Verify 200 response code", response.code(), is(200));
        assertThat(response.body(), notNullValue());
        KafkaHelper kafka = new KafkaHelper();
        List<String> consumedMessages = kafka.consumeMessages(
                KAFKA_TOPIC_CLIENT_RESTRICTIONS_APPLY,
                restrictionClient.getUserId().toString());
        for (String message : consumedMessages) {
            ClientRestrictionApply kafkaMessage = objectMapper.readValue(message, ClientRestrictionApply.class);
            writeLog((kafkaMessage.restrictions[0].internalReason));
            String actualInternalReason = kafkaMessage.restrictions[0].internalReason;
            assertTrue(actualInternalReason.contains(InternalReason.LATENCY_ARBITRAGE.getText()));
            assertTrue(actualInternalReason.contains(InternalReason.CPA_ABUSE.getText()));
            assertTrue(actualInternalReason.contains(InternalReason.INTERNAL_TRANSFER.getText()));
        }
    }

    @Test
    @AllureId("1973")
    @DisplayName(
            "Verify logic for internalReason field restriction DEPOSITS, Confirmed fraud -BONUS_ABUSE,NBP_ABUSE , LOSS_VOUCHER_ABUSE GAP_TRADING potential")
    void internalReasonTest3() throws IOException, InterruptedException {
        String fraudCode = BONUS_ABUSE.getCode();
        String fraudCode2 = LOSS_VOUCHER_ABUSE.getCode();
        String fraudCode3 = NBP_ABUSE.getCode();
        String fraudCode4 = GAP_TRADING.getCode();
        String restrictionCode = DEPOSITS.getCode();
        PostRestrictionRequestBody postRestriction = new PostRestrictionRequestBody(
                restrictionClient.getUcid(),
                restrictionCode,
                "GENERAL",
                null,
                null,
                null,
                new PostRestrictionRequestBody.UpdatedBy("autotest", "autotest"),
                new PostRestrictionRequestBody.AdditionalParam[] {
                    new PostRestrictionRequestBody.AdditionalParam("connectionScore", "string", "0.75"),
                    new PostRestrictionRequestBody.AdditionalParam(
                            "potentialFraudTypes", "array", new String[] {fraudCode2, fraudCode4}),
                    new PostRestrictionRequestBody.AdditionalParam(
                            "confirmedFraudTypes", "array", new String[] {fraudCode, fraudCode3})
                });
        Response response = postRestriction(postRestriction);
        assertThat("Verify 200 response code", response.code(), is(200));
        assertThat(response.body(), notNullValue());
        KafkaHelper kafka = new KafkaHelper();
        List<String> consumedMessages = kafka.consumeMessages(
                KAFKA_TOPIC_CLIENT_RESTRICTIONS_APPLY,
                restrictionClient.getUserId().toString());
        for (String message : consumedMessages) {
            ClientRestrictionApply kafkaMessage = objectMapper.readValue(message, ClientRestrictionApply.class);
            writeLog((kafkaMessage.restrictions[0].internalReason));
            String actualInternalReason = kafkaMessage.restrictions[0].internalReason;
            assertTrue(actualInternalReason.contains(InternalReason.DEPOSITS.getText()));
            assertTrue(actualInternalReason.contains(InternalReason.BONUS_ABUSE.getText()));
            assertTrue(actualInternalReason.contains(InternalReason.LOSS_VOUCHER_ABUSE.getText()));
            assertTrue(actualInternalReason.contains(InternalReason.NBP_ABUSE.getText()));
            assertTrue(actualInternalReason.contains(InternalReason.GAP_TRADING.getText()));
        }
    }

    @Test
    @AllureId("1974")
    @DisplayName(
            "Verify logic for internalReason field restriction DEPOSITS, Confirmed fraud - for each frauds not exceptional, LOSS_VOUCHER_ABUSE GAP_TRADING potential")
    void internalReasonTest4() throws Exception {
        List<InternalReason> unexceptionalFraudReason = getNonExceptionalReasons("FRAUD");
        for (InternalReason i : unexceptionalFraudReason) {
            String fraudCode = FraudType.valueOfCode(i.getCode()).getCode();
            String fraudCode2 = LOSS_VOUCHER_ABUSE.getCode();
            String fraudCode3 = NBP_ABUSE.getCode();
            String restrictionCode = DEPOSITS.getCode();
            PostRestrictionRequestBody postRestriction = new PostRestrictionRequestBody(
                    restrictionClient.getUcid(),
                    restrictionCode,
                    "GENERAL",
                    null,
                    null,
                    null,
                    new PostRestrictionRequestBody.UpdatedBy("autotest", "autotest"),
                    new PostRestrictionRequestBody.AdditionalParam[] {
                        new PostRestrictionRequestBody.AdditionalParam("connectionScore", "string", "0.75"),
                        new PostRestrictionRequestBody.AdditionalParam(
                                "potentialFraudTypes", "array", new String[] {fraudCode2, fraudCode3}),
                        new PostRestrictionRequestBody.AdditionalParam(
                                "confirmedFraudTypes", "array", new String[] {fraudCode})
                    });
            Response response = postRestriction(postRestriction);
            assertThat("Verify 200 response code", response.code(), is(200));
            assertThat(response.body(), notNullValue());
            KafkaHelper kafka = new KafkaHelper();
            List<String> consumedMessages = kafka.consumeMessages(
                    KAFKA_TOPIC_CLIENT_RESTRICTIONS_APPLY,
                    restrictionClient.getUserId().toString());
            for (String message : consumedMessages) {
                ClientRestrictionApply kafkaMessage = objectMapper.readValue(message, ClientRestrictionApply.class);
                writeLog("internal reason in found message is: \n" + (kafkaMessage.restrictions[0].internalReason));
                writeLog("expected value is: \n" + (i.getText()));
                writeLog("searched code is: \n" + (i.getCode()));
                String actualInternalReason = kafkaMessage.restrictions[0].internalReason;
                assertTrue(actualInternalReason.contains(InternalReason.DEPOSITS.getText()));
                assertTrue(actualInternalReason.contains(i.getText()));
                assertTrue(actualInternalReason.contains(InternalReason.LOSS_VOUCHER_ABUSE.getText()));
                assertTrue(actualInternalReason.contains(InternalReason.NBP_ABUSE.getText()));
            }
            before();
        }
    }

    @Test
    @AllureId("1974")
    @DisplayName(
            "Verify logic for internalReason field restriction DEPOSITS, Potential fraud - for each frauds not exceptional, LOSS_VOUCHER_ABUSE GAP_TRADING confirmed")
    void internalReasonTest5() throws Exception {
        List<InternalReason> unexceptionalFraudReason = getNonExceptionalReasons("FRAUD");
        for (InternalReason i : unexceptionalFraudReason) {
            String fraudCode = FraudType.valueOfCode(i.getCode()).getCode();
            String fraudCode2 = LOSS_VOUCHER_ABUSE.getCode();
            String fraudCode3 = NBP_ABUSE.getCode();
            String restrictionCode = DEPOSITS.getCode();
            PostRestrictionRequestBody postRestriction = new PostRestrictionRequestBody(
                    restrictionClient.getUcid(),
                    restrictionCode,
                    "GENERAL",
                    null,
                    null,
                    null,
                    new PostRestrictionRequestBody.UpdatedBy("autotest", "autotest"),
                    new PostRestrictionRequestBody.AdditionalParam[] {
                        new PostRestrictionRequestBody.AdditionalParam("connectionScore", "string", "0.75"),
                        new PostRestrictionRequestBody.AdditionalParam(
                                "potentialFraudTypes", "array", new String[] {fraudCode}),
                        new PostRestrictionRequestBody.AdditionalParam(
                                "confirmedFraudTypes", "array", new String[] {fraudCode2, fraudCode3})
                    });
            Response response = postRestriction(postRestriction);
            assertThat("Verify 200 response code", response.code(), is(200));
            assertThat(response.body(), notNullValue());
            KafkaHelper kafka = new KafkaHelper();
            List<String> consumedMessages = kafka.consumeMessages(
                    KAFKA_TOPIC_CLIENT_RESTRICTIONS_APPLY,
                    restrictionClient.getUserId().toString());
            for (String message : consumedMessages) {
                ClientRestrictionApply kafkaMessage = objectMapper.readValue(message, ClientRestrictionApply.class);
                writeLog("internal reason in found message is: \n" + (kafkaMessage.restrictions[0].internalReason));
                writeLog("expected value is: \n" + (i.getText()));
                writeLog("searched code is: \n" + (i.getCode()));
                String actualInternalReason = kafkaMessage.restrictions[0].internalReason;
                assertTrue(actualInternalReason.contains(InternalReason.DEPOSITS.getText()));
                assertTrue(actualInternalReason.contains(i.getText()));
                assertTrue(actualInternalReason.contains(InternalReason.LOSS_VOUCHER_ABUSE.getText()));
                assertTrue(actualInternalReason.contains(InternalReason.NBP_ABUSE.getText()));
            }
            before();
        }
    }

    @Test
    @AllureId("1979")
    @DisplayName(
            "Verify logic for internalReason field restriction DEPOSITS, Potential fraud - for each frauds exceptional, LOSS_VOUCHER_ABUSE GAP_TRADING confirmed")
    void internalReasonTest6() throws Exception {
        List<InternalReason> exceptionalFraudReasonFiltered = List.of(EXCHANGER, UPGRADER, POTENTIAL_CHARGEBACK);
        for (InternalReason i : exceptionalFraudReasonFiltered) {
            String fraudCode = FraudType.valueOfCode(i.getCode()).getCode();
            String fraudCode2 = LOSS_VOUCHER_ABUSE.getCode();
            String fraudCode3 = NBP_ABUSE.getCode();
            String restrictionCode = DEPOSITS.getCode();
            PostRestrictionRequestBody postRestriction = new PostRestrictionRequestBody(
                    restrictionClient.getUcid(),
                    restrictionCode,
                    "GENERAL",
                    null,
                    null,
                    null,
                    new PostRestrictionRequestBody.UpdatedBy("autotest", "autotest"),
                    new PostRestrictionRequestBody.AdditionalParam[] {
                        new PostRestrictionRequestBody.AdditionalParam("connectionScore", "string", "0.75"),
                        new PostRestrictionRequestBody.AdditionalParam(
                                "potentialFraudTypes", "array", new String[] {fraudCode}),
                        new PostRestrictionRequestBody.AdditionalParam(
                                "confirmedFraudTypes", "array", new String[] {fraudCode2, fraudCode3})
                    });
            Response response = postRestriction(postRestriction);
            assertThat("Verify 200 response code", response.code(), is(200));
            assertThat(response.body(), notNullValue());
            KafkaHelper kafka = new KafkaHelper();
            List<String> consumedMessages = kafka.consumeMessages(
                    KAFKA_TOPIC_CLIENT_RESTRICTIONS_APPLY,
                    restrictionClient.getUserId().toString());
            for (String message : consumedMessages) {
                ClientRestrictionApply kafkaMessage = objectMapper.readValue(message, ClientRestrictionApply.class);
                writeLog("internal reason in found message is: \n" + (kafkaMessage.restrictions[0].internalReason));
                writeLog("expected value is: \n" + (i.getText()));
                writeLog("searched code is: \n" + (i.getCode()));
                String actualInternalReason = kafkaMessage.restrictions[0].internalReason;
                assertFalse(actualInternalReason.contains(InternalReason.DEPOSITS.getText()));
                assertTrue(actualInternalReason.contains(i.getText()));
                assertTrue(actualInternalReason.contains(InternalReason.LOSS_VOUCHER_ABUSE.getText()));
                assertTrue(actualInternalReason.contains(InternalReason.NBP_ABUSE.getText()));
            }
            before();
        }
    }

    @Test
    @AllureId("1980")
    @DisplayName(
            "Verify logic for internalReason field restriction DEPOSITS, Confirmed fraud - EXCHANGER UPGRADER, LOSS_VOUCHER_ABUSE GAP_TRADING potential")
    void internalReasonTest8() throws Exception {
        List<InternalReason> exceptionalFraudReasonFiltered = List.of(EXCHANGER, UPGRADER);
        for (InternalReason i : exceptionalFraudReasonFiltered) {
            String fraudCode = FraudType.valueOfCode(i.getCode()).getCode();
            String fraudCode2 = LOSS_VOUCHER_ABUSE.getCode();
            String fraudCode3 = NBP_ABUSE.getCode();
            String restrictionCode = DEPOSITS.getCode();
            PostRestrictionRequestBody postRestriction = new PostRestrictionRequestBody(
                    restrictionClient.getUcid(),
                    restrictionCode,
                    "GENERAL",
                    null,
                    null,
                    null,
                    new PostRestrictionRequestBody.UpdatedBy("autotest", "autotest"),
                    new PostRestrictionRequestBody.AdditionalParam[] {
                        new PostRestrictionRequestBody.AdditionalParam("connectionScore", "string", "0.75"),
                        new PostRestrictionRequestBody.AdditionalParam(
                                "confirmedFraudTypes", "array", new String[] {fraudCode}),
                        new PostRestrictionRequestBody.AdditionalParam(
                                "potentialFraudTypes", "array", new String[] {fraudCode2, fraudCode3})
                    });
            Response response = postRestriction(postRestriction);
            assertThat("Verify 200 response code", response.code(), is(200));
            assertThat(response.body(), notNullValue());
            KafkaHelper kafka = new KafkaHelper();
            List<String> consumedMessages = kafka.consumeMessages(
                    KAFKA_TOPIC_CLIENT_RESTRICTIONS_APPLY,
                    restrictionClient.getUserId().toString());
            for (String message : consumedMessages) {
                ClientRestrictionApply kafkaMessage = objectMapper.readValue(message, ClientRestrictionApply.class);
                writeLog("internal reason in found message is: \n" + (kafkaMessage.restrictions[0].internalReason));
                writeLog("expected value is: \n" + (i.getText()));
                writeLog("searched code is: \n" + (i.getCode()));
                String actualInternalReason = kafkaMessage.restrictions[0].internalReason;
                assertFalse(actualInternalReason.contains(InternalReason.DEPOSITS.getText()));
                assertTrue(actualInternalReason.contains(i.getText()));
                assertTrue(actualInternalReason.contains(InternalReason.LOSS_VOUCHER_ABUSE.getText()));
                assertTrue(actualInternalReason.contains(InternalReason.NBP_ABUSE.getText()));
            }
            before();
        }
    }

    @Test
    @AllureId("1981")
    @DisplayName(
            "Verify logic for internalReason field restriction DEPOSITS, Confirmed fraud Chargeback, LOSS_VOUCHER_ABUSE GAP_TRADING potential")
    void internalReasonTest7() throws Exception {
        InternalReason i = CONFIRMED_CHARGEBACK;
        String fraudCode = FraudType.valueOfCode(i.getCode()).getCode();
        String fraudCode2 = LOSS_VOUCHER_ABUSE.getCode();
        String fraudCode3 = NBP_ABUSE.getCode();
        String restrictionCode = DEPOSITS.getCode();
        PostRestrictionRequestBody postRestriction = new PostRestrictionRequestBody(
                restrictionClient.getUcid(),
                restrictionCode,
                "GENERAL",
                null,
                null,
                null,
                new PostRestrictionRequestBody.UpdatedBy("autotest", "autotest"),
                new PostRestrictionRequestBody.AdditionalParam[] {
                    new PostRestrictionRequestBody.AdditionalParam("connectionScore", "string", "0.75"),
                    new PostRestrictionRequestBody.AdditionalParam(
                            "confirmedFraudTypes", "array", new String[] {fraudCode}),
                    new PostRestrictionRequestBody.AdditionalParam(
                            "potentialFraudTypes", "array", new String[] {fraudCode2, fraudCode3})
                });
        Response response = postRestriction(postRestriction);
        assertThat("Verify 200 response code", response.code(), is(200));
        assertThat(response.body(), notNullValue());
        KafkaHelper kafka = new KafkaHelper();
        List<String> consumedMessages = kafka.consumeMessages(
                KAFKA_TOPIC_CLIENT_RESTRICTIONS_APPLY,
                restrictionClient.getUserId().toString());
        for (String message : consumedMessages) {
            ClientRestrictionApply kafkaMessage = objectMapper.readValue(message, ClientRestrictionApply.class);
            writeLog("internal reason in found message is: \n" + (kafkaMessage.restrictions[0].internalReason));
            writeLog("expected value is: \n" + (i.getText()));
            writeLog("searched code is: \n" + (i.getCode()));
            String actualInternalReason = kafkaMessage.restrictions[0].internalReason;
            assertTrue(actualInternalReason.contains(InternalReason.DEPOSITS.getText()));
            assertTrue(actualInternalReason.contains(i.getText()));
            assertTrue(actualInternalReason.contains(InternalReason.LOSS_VOUCHER_ABUSE.getText()));
            assertTrue(actualInternalReason.contains(InternalReason.NBP_ABUSE.getText()));
        }
    }

    @AllureId("1486")
    @DisplayName("restriction messages for byBit restrictions appears in Lark")
    @Test
    void byBitCancellationLarkTest() throws IOException, InterruptedException {
        String cancellationReason = "cancellationReason" + getCurrentTimestampSeconds();
        String applyReason = "applyReason" + getCurrentTimestampSeconds();
        String updatedBySystem = "system" + getCurrentTimestampSeconds();
        String updatedByUser = "user" + getCurrentTimestampSeconds();
        PostRestrictionByBitRequest bb = new PostRestrictionByBitRequest();
        bb.updatedBy = new PostRestrictionByBitRequest.UpdatedBy("API", "QA");
        bb.account = byBitClient.getTradingAccount().toString();
        bb.ucid = byBitClient.getUcid();
        bb.code = "22";
        bb.comment = applyReason;
        Response response0 = postRestrictionByBit(bb);
        assertEquals(200, response0.code());

        PostRestrictionByBitResponse applyResponse =
                objectMapper.readValue(response0.body().string(), PostRestrictionByBitResponse.class);

        Thread.sleep(1000);

        CancelRestrictionByBitRequest.UpdatedBy cancelBy =
                new CancelRestrictionByBitRequest.UpdatedBy(updatedByUser, updatedBySystem);
        CancelRestrictionByBitRequest cancelRequest =
                new CancelRestrictionByBitRequest(applyResponse.getId(), cancellationReason, cancelBy);
        Response responseCancel = cancelRestrictionByIdByBit(cancelRequest);
        assertEquals(200, responseCancel.code());

        Response tenant = getTenantToken("cli_a829a3882cb8902f", "x3Tu9aG8DBY8XOQXc0WZneu8lQdauXR2");
        String token = objectMapper
                .readValue(tenant.body().string(), TenantAccessTokenResponse.class)
                .getTenantAccessToken();
        Response messageHistory = getMessagesChatLast10Minutes(token, "oc_dfeae72f51c406fadbc16c3890678895");
        ChatHistoryResponse response =
                objectMapper.readValue(messageHistory.body().string(), ChatHistoryResponse.class);
        List<ChatHistoryResponse.LarkApiDataItem> items = response.getData().getItems();

        // check apply message
        List<ChatHistoryResponse.LarkApiDataItem> itemsFiltered = items.stream()
                .filter(i -> i.getBody().getContent().contains(applyReason))
                .toList();
        String clearedContent = itemsFiltered
                .getFirst()
                .getBody()
                .getContent()
                .replace("\\n", "")
                .replace("\\", "");
        ByBitRestrictionBotMessage message = objectMapper.readValue(clearedContent, ByBitRestrictionBotMessage.class);
        Allure.step("check that message  title is correct");
        assertEquals("Withdrawal ban was applied", message.getTitle());
        Allure.step("check that message contains accountId");
        assertEquals(
                ": " + byBitClient.getTradingAccount(),
                message.getElements().getFirst().get(1).getText());
        Allure.step("check that message contains userId");
        assertEquals(
                ": " + byBitClient.getUserId(),
                message.getElements().getFirst().get(3).getText());
        Allure.step("check that message contains cancellation reason");
        assertEquals(": " + applyReason, message.getElements().getFirst().get(5).getText());

        // check cancellation message
        List<ChatHistoryResponse.LarkApiDataItem> itemsFiltered2 = items.stream()
                .filter(i -> i.getBody().getContent().contains(cancellationReason))
                .toList();
        String clearedContent2 = itemsFiltered2
                .getFirst()
                .getBody()
                .getContent()
                .replace("\\n", "")
                .replace("\\", "");
        ByBitRestrictionBotMessage message2 = objectMapper.readValue(clearedContent2, ByBitRestrictionBotMessage.class);
        Allure.step("check that message  title is correct");
        assertEquals("Withdrawal restriction: Removal request", message2.getTitle());
        Allure.step("check that message contains accountId");
        assertEquals(
                ": " + byBitClient.getTradingAccount(),
                message2.getElements().getFirst().get(1).getText());
        Allure.step("check that message contains userId");
        assertEquals(
                ": " + byBitClient.getUserId(),
                message2.getElements().getFirst().get(3).getText());
        Allure.step("check that message contains cancellation reason");
        assertEquals(
                ": " + cancellationReason,
                message2.getElements().getFirst().get(5).getText());
    }

    @Test
    @AllureId("1987")
    @DisplayName("successIfRestrictionBybit")
    void successIfRestrictionBybit() throws Exception {

        PostRestrictionByBitRequest bb = new PostRestrictionByBitRequest();
        bb.updatedBy = new PostRestrictionByBitRequest.UpdatedBy("API", "QA");
        bb.account = byBitClient.getTradingAccount().toString();
        bb.ucid = byBitClient.getUcid();
        bb.code = "22";
        bb.comment = "API test";

        Allure.step("Send and check first request");
        Response response = postRestrictionByBit(bb);
        assertEquals(200, response.code());
    }

    @Test
    @DisplayName("Put worse trading restriction V3 test")
    @Feature("BMS-2921 Put worse trading restriction V3")
    @AllureId("1940")
    void putWorseTradingRestrictionV3() throws Exception {
        var timeout = 20_000;
        var rq1 = new NewTradingEnvRestrictionRequestBody(
                RestrictionType.TRADING_ENVIRONMENT,
                restrictionClient.getUcid(),
                "23",
                "Comment 1",
                CorrelationType.RULE_ENGINE,
                UUID.randomUUID().toString(),
                new UpdatedBy().system("Rule Engine"),
                Collections.emptyList(),
                new BigInteger(restrictionClient.getTradingAccount() + ""),
                restrictionClient.getServerId(),
                LOW,
                "Application reason");

        try (var response1 = RestrictionHelper.putRestrictionV3(rq1)) {
            Awaitility.await()
                    .pollDelay(Duration.ofMillis(timeout / 20))
                    .pollInterval(Duration.ofMillis(timeout / 20))
                    .atMost(Duration.ofMillis(timeout))
                    .until(
                            () -> RestrictionHelper.getClientRestrictionsV3(restrictionClient.getUcid()),
                            getResponse -> {
                                try (getResponse) {
                                    assertNotNull(getResponse);
                                    assertEquals(200, response1.code());
                                    assertNotNull(getResponse.body());
                                    var restrictionsJson = getResponse.body().string();
                                    var listReference = new TypeReference<List<ClientRestriction>>() {};
                                    var actual = objectMapper.readValue(restrictionsJson, listReference);
                                    return actual.stream()
                                            .filter(r -> r.getType() == RestrictionType.TRADING_ENVIRONMENT)
                                            .map(ClientTradingEnvironmentRestriction.class::cast)
                                            .anyMatch(r -> Objects.equals(r.getLevel(), "LOW")
                                                    && Objects.equals(r.getStatus(), RestrictionStatus.APPLIED));
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            });
        }
        RestrictionPage.checkKafkaRequestApplyTradingEnv(ApplyTradingEnvironmentRestrictionMessage.builder()
                .clientId(new BigInteger(restrictionClient.getUserId() + ""))
                .brand(restrictionClient.getBrand())
                .accountId(new BigInteger(restrictionClient.getTradingAccount() + ""))
                .serverId(restrictionClient.getServerId())
                .initialBanDurationInMinutes(525_600)
                .restriction(ApplyTradingEnvironmentRestrictionMessage.TradingEnvironmentRestriction.builder()
                        .restrictionCode("23")
                        .riskLevel("low")
                        .build())
                .build());
        var rq2 = new NewTradingEnvRestrictionRequestBody(
                RestrictionType.TRADING_ENVIRONMENT,
                restrictionClient.getUcid(),
                "23",
                "Comment 1",
                CorrelationType.RULE_ENGINE,
                UUID.randomUUID().toString(),
                new UpdatedBy().system("Rule Engine"),
                Collections.emptyList(),
                new BigInteger(restrictionClient.getTradingAccount() + ""),
                restrictionClient.getServerId(),
                LOW,
                "Application reason");
        try (var response2 = RestrictionHelper.putRestrictionV3(rq2)) {
            Awaitility.await()
                    .pollDelay(Duration.ofMillis(timeout / 20))
                    .pollInterval(Duration.ofMillis(timeout / 20))
                    .atMost(Duration.ofMillis(timeout))
                    .until(
                            () -> RestrictionHelper.getClientRestrictionsV3(restrictionClient.getUcid()),
                            getResponse -> {
                                try (getResponse) {
                                    assertNotNull(getResponse);
                                    assertEquals(200, response2.code());
                                    assertNotNull(getResponse.body());
                                    var restrictionsJson = getResponse.body().string();
                                    var listReference = new TypeReference<List<ClientRestriction>>() {};
                                    var actual = objectMapper.readValue(restrictionsJson, listReference);
                                    return actual.stream()
                                            .filter(r -> r.getType() == RestrictionType.TRADING_ENVIRONMENT)
                                            .map(ClientTradingEnvironmentRestriction.class::cast)
                                            .anyMatch(r -> Objects.equals(r.getLevel(), LOW.name())
                                                    && Objects.equals(r.getStatus(), RestrictionStatus.APPLIED));
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            });
        }
    }
}
