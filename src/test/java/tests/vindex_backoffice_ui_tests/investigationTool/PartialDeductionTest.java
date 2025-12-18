package tests.vindex_backoffice_ui_tests.investigationTool;

import business_objects.db.abuse_registry_db.AbuserDeduction;
import business_objects.db.backoffice_db.IllegalTrades;
import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.mt_account.MtAccountObject;
import business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObject;
import business_objects.db.clickhouse.mt_mt5_positions.MtMt5PositionsObject;
import business_objects.kafka.alerts.RuleAlert;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.data.ClientHelper;
import helpers.data.enums.deduction.DeductionStatusUi;
import helpers.kafka.KafkaHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

import java.text.DecimalFormat;
import java.util.List;
import java.util.function.Function;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountDataForUi;
import static business_objects.db.clickhouse.crm_tb_account_for_mt.crm_tb_account.CrmTbAccountForMtObjectFactory.generateAccountForMtByAccount;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObjectFactory.generateMt4TradesCoercedAccountProfitComment;
import static business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObjectFactory.generateMt4TradesCoercedAccountProfitCommentBuy;
import static business_objects.db.clickhouse.mt_mt5_positions.MtMt5PositionsObjectFactory.generateMtMt5PositionsObject;
import static business_objects.kafka.alerts.RuleAlertFactory.generateRuleAlertByUcid;
import static business_objects.ui.user.UserFactory.autotestUserOne;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.Currency.EUR;
import static helpers.data.enums.Currency.USD;
import static helpers.data.enums.FraudType.*;
import static helpers.data.enums.FraudTypeStatus.*;
import static helpers.data.enums.deduction.DeductionStatusApproval.AWAITING_APPROVAL;
import static helpers.data.enums.deduction.DeductionStatusDeduction.TO_BE_DEDUCTED;
import static helpers.data.enums.deduction.DeductionStatusEmail.NOT_SENT;
import static helpers.data.enums.deduction.DeductionStatusOpenPositions.HOLDING;
import static helpers.data.enums.deduction.DeductionStatusOpenPositions.NOT_HOLDING;
import static helpers.data.enums.deduction.DeductionType.PARTIAL_DEDUCTION;
import static helpers.data.enums.deduction.DeductionTypeAccount.ILLEGAL_PROFIT;
import static helpers.data.enums.deduction.DeductionTypeAccount.NO_ILLEGAL_PROFIT;
import static helpers.database.ArHelper.deleteUserFromAbuseRegistry;
import static helpers.database.BoHelper.closeAlert;
import static helpers.database.DbHelper.*;
import static helpers.database.DbName.POSTGRES;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Utils.getRandomIntPositive;

@Feature("BMS-1758 Calculating illegal profit (partial)")
class PartialDeductionTest extends TestBaseWeb {

    private static final KafkaHelper kafka = new KafkaHelper();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final ClientHelper client = getRandomVantageClientAllFields();
    private static final CrmTbUserObject crmTbUser = generateUserByClient(client);
    private static final DecimalFormat formatter = new DecimalFormat("#,##0.##");
    private static final String REGEX_PATTERN = "\\d{1,3}(,\\d{3})*(\\.\\d{1,2})? USD$";
    private static final String REGEX_PATTERN_DEDUCTION = String.format("^-%s", REGEX_PATTERN);
    private static final String SUGGESTED_DEDUCTION_PATTERN_ILLEGAL_PARTIAL = "Account %sBalance %s USD ・ Illegal %s USD from %s trade";
    private static final String SUGGESTED_DEDUCTION_PATTERN = "^Account %sBalance " + REGEX_PATTERN;
    private static final String SUGGESTED_DEDUCTION_PATTERN_USD = SUGGESTED_DEDUCTION_PATTERN_ILLEGAL_PARTIAL.split(" ・")[0];
    private static final String COMMENT = "Test comment for deductions";
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
    static void setup() {
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
        insertObjectsToDb(CRM_TB_ACCOUNT_FOR_MT_TABLE_NAME, List.of(generateAccountForMtByAccount(account1), generateAccountForMtByAccount(account2), generateAccountForMtByAccount(account3), generateAccountForMtByAccount(account4), generateAccountForMtByAccount(account5)));
        insertObjectsToDb(MT_ACCOUNT_TABLE_NAME, List.of(mtAccount1, mtAccount2, mtAccount3, mtAccount4, mtAccount5));
        insertObjectsToDb(MT4_TRADES_COERCED_TABLE_NAME, List.of(trade1, trade2, tradeWithdrawal, trade3, trade4, trade5, trade6, trade7, trade8, trade9, trade10));
        MtMt5PositionsObject position = generateMtMt5PositionsObject(client);
        position.setAccount(mtAccount2.account);
        position.setServerId(mtAccount2.sourceIdSt);
        insertObjectToDb(MT5_POSITIONS_TABLE_NAME, position);
    }

    @AfterEach
    void teardownEach() throws Exception {
        deleteUserFromAbuseRegistry(client.getUcid());
        closeAlert(client.getUcid());
    }

    @AfterAll
    static void teardown() throws Exception {
        deleteEntryFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        deleteEntryFromDb(MT4_TRADES_COERCED_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        deleteEntryFromDb(MT5_POSITIONS_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        deleteUserFromAbuseRegistry(client.getUcid());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1505")
    @DisplayName("Verify end to end partial deduction in report fraud")
    void partialDeductionTest1() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigateOperations(client.getUcid());
        tradingPage.clickIllegalProfitButton();
        tradingPage.selectIllegalTradeByTicket(trade1.getTicket());
        assertThat("Verify illegal profit amount in UI", tradingPage.getIllegalProfitAmount(), is(String.format("%s %s", formatter.format(trade1.getProfit()), mtAccount1.currency)));
        assertThat("Verify quantity of accounts with illegal trades", tradingPage.getIllegalProfitAccountsQuantity(), is("profit on 1 account"));
        assertThat("Verify quantity of illegal trades", tradingPage.getSelectedIllegalTradesCounter(), is("1 selected"));
        tradingPage.clickSaveAsIllegalProfit();
        resolvePage.openReportFraudForm();
        resolvePage.addFraud(LATENCY_ARBITRAGE, CONFIRMED);
        assertThat("Verify total suggested deduction amount", resolvePage.getSuggestedDeductionAmount(), matchesPattern(REGEX_PATTERN));
        assertThat("Verify total illegal profit amount", resolvePage.getIllegalProfitPartialAmount(), is(String.format("%s USD illegal profit", formatter.format(trade1.getProfit()))));

        List<String> deductionItems = resolvePage.getSuggestedDeductionItems();
        assertThat("Verify amount of deductions", deductionItems.size(), is(5));
        assertThat("Verify 1st deduction illegal profit and balance", deductionItems.getFirst(), is(String.format(SUGGESTED_DEDUCTION_PATTERN_ILLEGAL_PARTIAL, mtAccount1.account, formatter.format(trade1.getProfit() + trade2.getProfit() + tradeWithdrawal.getProfit()), formatter.format(trade1.getProfit()), 1)));
        assertThat("Verify 2nd deduction illegal profit and balance", deductionItems.get(1), is(String.format(SUGGESTED_DEDUCTION_PATTERN_USD, mtAccount5.account, formatter.format(trade9.getProfit() + trade10.getProfit()))));
        assertThat("Verify 3rd deduction illegal profit and balance", deductionItems.get(2), matchesPattern(String.format(SUGGESTED_DEDUCTION_PATTERN, mtAccount4.account)));
        assertThat("Verify 4th deduction illegal profit and balance", deductionItems.get(3), matchesPattern(String.format(SUGGESTED_DEDUCTION_PATTERN, mtAccount3.account)));
        assertThat("Verify 5th deduction illegal profit and balance", deductionItems.getLast(), is(String.format(SUGGESTED_DEDUCTION_PATTERN_USD, mtAccount2.account, formatter.format(trade3.getProfit() + trade4.getProfit()))));

        Function<Double, String> calculateDeduction = deduction -> String.format("%s USD", formatter.format(deduction * -1));
        List<String> suggestedDeductionValues = resolvePage.getSuggestedDeductionValues();
        assertThat("Verify amount of suggested deductions", suggestedDeductionValues.size(), is(5));
        assertThat("Verify 1st suggested deduction value", suggestedDeductionValues.getFirst(), is(calculateDeduction.apply(trade1.getProfit() + trade2.getProfit() + tradeWithdrawal.getProfit())));
        assertThat("Verify 2nd suggested deduction value", suggestedDeductionValues.get(1), is(calculateDeduction.apply(trade9.getProfit() + trade10.getProfit())));
        assertThat("Verify 3rd suggested deduction value", suggestedDeductionValues.get(2), matchesPattern(REGEX_PATTERN_DEDUCTION));
        assertThat("Verify 4th suggested deduction value", suggestedDeductionValues.get(3), matchesPattern(REGEX_PATTERN_DEDUCTION));
        assertThat("Verify 5th suggested deduction value", suggestedDeductionValues.getLast(), is(DeductionStatusUi.HOLDING.getDisplayName()));

        resolvePage.applyFraudManagement(COMMENT);
        List<AbuserDeduction> deductionList = getObjectsFromDB(POSTGRES, AR_ABUSER_DEDUCTION_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()), AbuserDeduction.class);
        for (AbuserDeduction deduction : deductionList) {
            assertThat("Verify created deduction has abuser_history_id not null", deduction.getAbuserHistoryId(), notNullValue());
            assertThat("Verify created deduction has illegal_profit not null", deduction.getIllegalProfitUsd(), notNullValue());
            assertThat("Verify created deduction has suggested_deduction not null", deduction.getSuggestedDeductionUsd(), notNullValue());
            assertThat("Verify created deduction has created_at not null", deduction.getCreatedAt(), notNullValue());
            assertThat("Verify created deduction has updated_at not null", deduction.getUpdatedAt(), notNullValue());
        }
        AbuserDeduction deduction1 = new AbuserDeduction(client.getUcid(), null, mtAccount1.account.toString(), mtAccount1.sourceIdSt, mtAccount1.server, mtAccount1.currency, client.getBrand(), NOT_HOLDING.getDisplayName(), NOT_SENT.getDisplayName(), TO_BE_DEDUCTED.getDisplayName(), AWAITING_APPROVAL.getDisplayName(), COMMENT, trade1.getProfit(), null, trade1.getProfit() + trade2.getProfit() + tradeWithdrawal.getProfit(), null, null, null, null, null, String.format("%s %s", autotestUserOne().getFirstName(), autotestUserOne().getLastName()), VINDEX_BO_SYSTEM, ILLEGAL_PROFIT.getDisplayName(), null, null, trade1.getProfit() + trade2.getProfit() + tradeWithdrawal.getProfit(), null, client.getUserId().toString(), trade1.getProfit() + trade2.getProfit() + tradeWithdrawal.getProfit(), null, false, PARTIAL_DEDUCTION.getDisplayName());
        AbuserDeduction deduction2 = new AbuserDeduction(client.getUcid(), null, mtAccount5.account.toString(), mtAccount5.sourceIdSt, mtAccount5.server, mtAccount5.currency, client.getBrand(), NOT_HOLDING.getDisplayName(), NOT_SENT.getDisplayName(), TO_BE_DEDUCTED.getDisplayName(), AWAITING_APPROVAL.getDisplayName(), COMMENT, 0d, null, trade9.getProfit() + trade10.getProfit(), null, null, null, null, null, String.format("%s %s", autotestUserOne().getFirstName(), autotestUserOne().getLastName()), VINDEX_BO_SYSTEM, NO_ILLEGAL_PROFIT.getDisplayName(), null, null, trade9.getProfit() + trade10.getProfit(), null, client.getUserId().toString(), trade9.getProfit() + trade10.getProfit(), null, false, PARTIAL_DEDUCTION.getDisplayName());
        AbuserDeduction deduction3 = new AbuserDeduction(client.getUcid(), null, mtAccount4.account.toString(), mtAccount4.sourceIdSt, mtAccount4.server, mtAccount4.currency, client.getBrand(), NOT_HOLDING.getDisplayName(), NOT_SENT.getDisplayName(), TO_BE_DEDUCTED.getDisplayName(), AWAITING_APPROVAL.getDisplayName(), COMMENT, 0d, null, trade7.getProfit() + trade8.getProfit(), null, null, null, null, null, String.format("%s %s", autotestUserOne().getFirstName(), autotestUserOne().getLastName()), VINDEX_BO_SYSTEM, NO_ILLEGAL_PROFIT.getDisplayName(), null, null, trade7.getProfit() + trade8.getProfit(), null, client.getUserId().toString(), trade7.getProfit() + trade8.getProfit(), null, false, PARTIAL_DEDUCTION.getDisplayName());
        AbuserDeduction deduction4 = new AbuserDeduction(client.getUcid(), null, mtAccount3.account.toString(), mtAccount3.sourceIdSt, mtAccount3.server, mtAccount3.currency, client.getBrand(), NOT_HOLDING.getDisplayName(), NOT_SENT.getDisplayName(), TO_BE_DEDUCTED.getDisplayName(), AWAITING_APPROVAL.getDisplayName(), COMMENT, 0d, null, trade5.getProfit() + trade6.getProfit(), null, null, null, null, null, String.format("%s %s", autotestUserOne().getFirstName(), autotestUserOne().getLastName()), VINDEX_BO_SYSTEM, NO_ILLEGAL_PROFIT.getDisplayName(), null, null, trade5.getProfit() + trade6.getProfit(), null, client.getUserId().toString(), trade5.getProfit() + trade6.getProfit(), null, false, PARTIAL_DEDUCTION.getDisplayName());
        AbuserDeduction deduction5 = new AbuserDeduction(client.getUcid(), null, mtAccount2.account.toString(), mtAccount2.sourceIdSt, mtAccount2.server, mtAccount2.currency, client.getBrand(), HOLDING.getDisplayName(), NOT_SENT.getDisplayName(), TO_BE_DEDUCTED.getDisplayName(), AWAITING_APPROVAL.getDisplayName(), COMMENT, 0d, null, 0d, null, null, null, null, null, String.format("%s %s", autotestUserOne().getFirstName(), autotestUserOne().getLastName()), VINDEX_BO_SYSTEM, NO_ILLEGAL_PROFIT.getDisplayName(), null, null, 0d, null, client.getUserId().toString(), trade3.getProfit() + trade4.getProfit(), null, false, PARTIAL_DEDUCTION.getDisplayName());
        assertThat("Verify deductions in abuser_deduction table are as expected", deductionList, containsInAnyOrder(deduction1, deduction2, deduction3, deduction4, deduction5));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1506")
    @DisplayName("Verify end to end partial deduction in resolve")
    void partialDeductionTest2() throws Exception {
        RuleAlert alert = generateRuleAlertByUcid(client.getUcid());
        alert.rule.attributes.account = mtAccount1.account.toString();
        kafka.produceMessage(alert.alertId, objectMapper.writeValueAsString(alert), KAFKA_TOPIC_ALERTS);
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigateOperations(client.getUcid());
        tradingPage.clickIllegalProfitButton();
        tradingPage.selectIllegalTradeByTicket(trade1.getTicket());
        assertThat("Verify illegal profit amount in UI", tradingPage.getIllegalProfitAmount(), is(String.format("%s %s", formatter.format(trade1.getProfit()), mtAccount1.currency)));
        assertThat("Verify quantity of accounts with illegal trades", tradingPage.getIllegalProfitAccountsQuantity(), is("profit on 1 account"));
        assertThat("Verify quantity of illegal trades", tradingPage.getSelectedIllegalTradesCounter(), is("1 selected"));
        tradingPage.clickSaveAsIllegalProfit();
        resolvePage.openResolveSuspicious();
        resolvePage.addFraud(LATENCY_ARBITRAGE, CONFIRMED);
        assertThat("Verify total suggested deduction amount", resolvePage.getSuggestedDeductionAmount(), matchesPattern(REGEX_PATTERN));
        assertThat("Verify total illegal profit amount", resolvePage.getIllegalProfitPartialAmount(), is(String.format("%s USD illegal profit", formatter.format(trade1.getProfit()))));

        List<String> deductionItems = resolvePage.getSuggestedDeductionItems();
        assertThat("Verify amount of deductions", deductionItems.size(), is(5));
        assertThat("Verify 1st deduction illegal profit and balance", deductionItems.getFirst(), is(String.format(SUGGESTED_DEDUCTION_PATTERN_ILLEGAL_PARTIAL, mtAccount1.account, formatter.format(trade1.getProfit() + trade2.getProfit() + tradeWithdrawal.getProfit()), formatter.format(trade1.getProfit()), 1)));
        assertThat("Verify 2nd deduction illegal profit and balance", deductionItems.get(1), is(String.format(SUGGESTED_DEDUCTION_PATTERN_USD, mtAccount5.account, formatter.format(trade9.getProfit() + trade10.getProfit()))));
        assertThat("Verify 3rd deduction illegal profit and balance", deductionItems.get(2), matchesPattern(String.format(SUGGESTED_DEDUCTION_PATTERN, mtAccount4.account)));
        assertThat("Verify 4th deduction illegal profit and balance", deductionItems.get(3), matchesPattern(String.format(SUGGESTED_DEDUCTION_PATTERN, mtAccount3.account)));
        assertThat("Verify 5th deduction illegal profit and balance", deductionItems.getLast(), is(String.format(SUGGESTED_DEDUCTION_PATTERN_USD, mtAccount2.account, formatter.format(trade3.getProfit() + trade4.getProfit()))));

        Function<Double, String> calculateDeduction = deduction -> String.format("%s USD", formatter.format(deduction * -1));
        List<String> suggestedDeductionValues = resolvePage.getSuggestedDeductionValues();
        assertThat("Verify amount of suggested deductions", suggestedDeductionValues.size(), is(5));
        assertThat("Verify 1st suggested deduction value", suggestedDeductionValues.getFirst(), is(calculateDeduction.apply(trade1.getProfit() + trade2.getProfit() + tradeWithdrawal.getProfit())));
        assertThat("Verify 2nd suggested deduction value", suggestedDeductionValues.get(1), is(calculateDeduction.apply(trade9.getProfit() + trade10.getProfit())));
        assertThat("Verify 3rd suggested deduction value", suggestedDeductionValues.get(2), matchesPattern(REGEX_PATTERN_DEDUCTION));
        assertThat("Verify 4th suggested deduction value", suggestedDeductionValues.get(3), matchesPattern(REGEX_PATTERN_DEDUCTION));
        assertThat("Verify 5th suggested deduction value", suggestedDeductionValues.getLast(), is(DeductionStatusUi.HOLDING.getDisplayName()));

        resolvePage.resolveNoActions(COMMENT);
        List<AbuserDeduction> deductionList = getObjectsFromDB(POSTGRES, AR_ABUSER_DEDUCTION_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()), AbuserDeduction.class);
        for (AbuserDeduction deduction : deductionList) {
            assertThat("Verify created deduction has abuser_history_id not null", deduction.getAbuserHistoryId(), notNullValue());
            assertThat("Verify created deduction has illegal_profit not null", deduction.getIllegalProfitUsd(), notNullValue());
            assertThat("Verify created deduction has suggested_deduction not null", deduction.getSuggestedDeductionUsd(), notNullValue());
            assertThat("Verify created deduction has created_at not null", deduction.getCreatedAt(), notNullValue());
            assertThat("Verify created deduction has updated_at not null", deduction.getUpdatedAt(), notNullValue());
        }
        AbuserDeduction deduction1 = new AbuserDeduction(client.getUcid(), null, mtAccount1.account.toString(), mtAccount1.sourceIdSt, mtAccount1.server, mtAccount1.currency, client.getBrand(), NOT_HOLDING.getDisplayName(), NOT_SENT.getDisplayName(), TO_BE_DEDUCTED.getDisplayName(), AWAITING_APPROVAL.getDisplayName(), COMMENT, trade1.getProfit(), null, trade1.getProfit() + trade2.getProfit() + tradeWithdrawal.getProfit(), null, null, null, null, null, String.format("%s %s", autotestUserOne().getFirstName(), autotestUserOne().getLastName()), VINDEX_BO_SYSTEM, ILLEGAL_PROFIT.getDisplayName(), null, null, trade1.getProfit() + trade2.getProfit() + tradeWithdrawal.getProfit(), null, client.getUserId().toString(), trade1.getProfit() + trade2.getProfit() + tradeWithdrawal.getProfit(), null, false, PARTIAL_DEDUCTION.getDisplayName());
        AbuserDeduction deduction2 = new AbuserDeduction(client.getUcid(), null, mtAccount5.account.toString(), mtAccount5.sourceIdSt, mtAccount5.server, mtAccount5.currency, client.getBrand(), NOT_HOLDING.getDisplayName(), NOT_SENT.getDisplayName(), TO_BE_DEDUCTED.getDisplayName(), AWAITING_APPROVAL.getDisplayName(), COMMENT, 0d, null, trade9.getProfit() + trade10.getProfit(), null, null, null, null, null, String.format("%s %s", autotestUserOne().getFirstName(), autotestUserOne().getLastName()), VINDEX_BO_SYSTEM, NO_ILLEGAL_PROFIT.getDisplayName(), null, null, trade9.getProfit() + trade10.getProfit(), null, client.getUserId().toString(), trade9.getProfit() + trade10.getProfit(), null, false, PARTIAL_DEDUCTION.getDisplayName());
        AbuserDeduction deduction3 = new AbuserDeduction(client.getUcid(), null, mtAccount4.account.toString(), mtAccount4.sourceIdSt, mtAccount4.server, mtAccount4.currency, client.getBrand(), NOT_HOLDING.getDisplayName(), NOT_SENT.getDisplayName(), TO_BE_DEDUCTED.getDisplayName(), AWAITING_APPROVAL.getDisplayName(), COMMENT, 0d, null, trade7.getProfit() + trade8.getProfit(), null, null, null, null, null, String.format("%s %s", autotestUserOne().getFirstName(), autotestUserOne().getLastName()), VINDEX_BO_SYSTEM, NO_ILLEGAL_PROFIT.getDisplayName(), null, null, trade7.getProfit() + trade8.getProfit(), null, client.getUserId().toString(), trade7.getProfit() + trade8.getProfit(), null, false, PARTIAL_DEDUCTION.getDisplayName());
        AbuserDeduction deduction4 = new AbuserDeduction(client.getUcid(), null, mtAccount3.account.toString(), mtAccount3.sourceIdSt, mtAccount3.server, mtAccount3.currency, client.getBrand(), NOT_HOLDING.getDisplayName(), NOT_SENT.getDisplayName(), TO_BE_DEDUCTED.getDisplayName(), AWAITING_APPROVAL.getDisplayName(), COMMENT, 0d, null, trade5.getProfit() + trade6.getProfit(), null, null, null, null, null, String.format("%s %s", autotestUserOne().getFirstName(), autotestUserOne().getLastName()), VINDEX_BO_SYSTEM, NO_ILLEGAL_PROFIT.getDisplayName(), null, null, trade5.getProfit() + trade6.getProfit(), null, client.getUserId().toString(), trade5.getProfit() + trade6.getProfit(), null, false, PARTIAL_DEDUCTION.getDisplayName());
        AbuserDeduction deduction5 = new AbuserDeduction(client.getUcid(), null, mtAccount2.account.toString(), mtAccount2.sourceIdSt, mtAccount2.server, mtAccount2.currency, client.getBrand(), HOLDING.getDisplayName(), NOT_SENT.getDisplayName(), TO_BE_DEDUCTED.getDisplayName(), AWAITING_APPROVAL.getDisplayName(), COMMENT, 0d, null, 0d, null, null, null, null, null, String.format("%s %s", autotestUserOne().getFirstName(), autotestUserOne().getLastName()), VINDEX_BO_SYSTEM, NO_ILLEGAL_PROFIT.getDisplayName(), null, null, 0d, null, client.getUserId().toString(), trade3.getProfit() + trade4.getProfit(), null, false, PARTIAL_DEDUCTION.getDisplayName());
        assertThat("Verify deductions in abuser_deduction table are as expected", deductionList, containsInAnyOrder(deduction1, deduction2, deduction3, deduction4, deduction5));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1507")
    @DisplayName("Verify trades are saved correctly to the illegal_trades table")
    void partialDeductionTest3() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigateOperations(client.getUcid());
        tradingPage.clickIllegalProfitButton();
        tradingPage.selectIllegalTradeByTicket(trade1.getTicket());
        tradingPage.selectIllegalTradeByTicket(trade2.getTicket());
        tradingPage.selectIllegalTradeByTicket(trade3.getTicket());
        assertThat("Verify illegal profit amount in UI", tradingPage.getIllegalProfitAmount(), is(String.format("%s %s", formatter.format(trade1.getProfit() + trade2.getProfit() + trade3.getProfit()), mtAccount1.currency)));
        assertThat("Verify quantity of accounts with illegal trades", tradingPage.getIllegalProfitAccountsQuantity(), is("profit on 2 accounts"));
        assertThat("Verify quantity of illegal trades", tradingPage.getSelectedIllegalTradesCounter(), is("3 selected"));
        tradingPage.clickSaveAsIllegalProfit();
        List<IllegalTrades> illegalTrades = getObjectsFromDB(POSTGRES, BO_ILLEGAL_TRADES_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()), IllegalTrades.class);
        IllegalTrades illegalTrade1 = new IllegalTrades(mtAccount1.account.toString(), mtAccount1.sourceIdSt, trade1.getProfit() + trade2.getProfit(), trade1.getProfit() + trade2.getProfit(), String.format("[%s, %s]", trade1.getTicket(), trade2.getTicket()), String.format("[%s]", trade1.getSymbol()), null, client.getUcid());
        IllegalTrades illegalTrade1Alternate = new IllegalTrades(mtAccount1.account.toString(), mtAccount1.sourceIdSt, trade1.getProfit() + trade2.getProfit(), trade1.getProfit() + trade2.getProfit(), String.format("[%s, %s]", trade2.getTicket(), trade1.getTicket()), String.format("[%s]", trade1.getSymbol()), null, client.getUcid());
        IllegalTrades illegalTrade2 = new IllegalTrades(mtAccount2.account.toString(), mtAccount2.sourceIdSt, trade3.getProfit(), trade3.getProfit(), String.format("[%s]", trade3.getTicket()), String.format("[%s]", trade3.getSymbol()), null, client.getUcid());
        assertThat("Verify data in illegal_trades table", illegalTrades, anyOf(containsInAnyOrder(illegalTrade1, illegalTrade2), containsInAnyOrder(illegalTrade1Alternate, illegalTrade2)));
        tradingPage.selectIllegalTradeByTicket(trade5.getTicket());
        tradingPage.clickSaveAsIllegalProfit();
        illegalTrades = getObjectsFromDB(POSTGRES, BO_ILLEGAL_TRADES_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()), IllegalTrades.class);
        IllegalTrades illegalTrade3 = new IllegalTrades(mtAccount3.account.toString(), mtAccount3.sourceIdSt, trade5.getProfit(), null, String.format("[%s]", trade5.getTicket()), String.format("[%s]", trade5.getSymbol()), null, client.getUcid());
        assertThat("Verify data in illegal_trades table", illegalTrades, contains(illegalTrade3));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1508")
    @DisplayName("Verify profit is deducted from the account it's earned on first")
    void partialDeductionTest4() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        tradingPage.navigateOperations(client.getUcid());
        tradingPage.clickIllegalProfitButton();
        tradingPage.selectIllegalTradeByTicket(trade2.getTicket());
        tradingPage.selectIllegalTradeByTicket(trade9.getTicket());
        tradingPage.clickSaveAsIllegalProfit();
        resolvePage.openReportFraudForm();
        resolvePage.addFraud(LATENCY_ARBITRAGE, CONFIRMED);
        assertThat("Verify total suggested deduction amount", resolvePage.getSuggestedDeductionAmount(), is(String.format("%s USD", formatter.format(trade2.getProfit() + trade9.getProfit()))));
        assertThat("Verify total illegal profit amount", resolvePage.getIllegalProfitPartialAmount(), is(String.format("%s USD illegal profit", formatter.format(trade2.getProfit() + trade9.getProfit()))));

        List<String> deductionItems = resolvePage.getSuggestedDeductionItems();
        assertThat("Verify amount of deductions", deductionItems.size(), is(2));
        assertThat("Verify 2nd deduction illegal profit and balance", deductionItems.getFirst(), is(String.format(SUGGESTED_DEDUCTION_PATTERN_ILLEGAL_PARTIAL, mtAccount5.account, formatter.format(trade9.getProfit() + trade10.getProfit()), formatter.format(trade9.getProfit()), 1)));
        assertThat("Verify 1st deduction illegal profit and balance", deductionItems.get(1), is(String.format(SUGGESTED_DEDUCTION_PATTERN_ILLEGAL_PARTIAL, mtAccount1.account, formatter.format(trade1.getProfit() + trade2.getProfit() + tradeWithdrawal.getProfit()), formatter.format(trade2.getProfit()), 1)));

        Function<Double, String> calculateDeduction = deduction -> String.format("%s USD", formatter.format(deduction * -1));
        List<String> suggestedDeductionValues = resolvePage.getSuggestedDeductionValues();
        assertThat("Verify amount of suggested deductions", suggestedDeductionValues.size(), is(2));
        assertThat("Verify 2nd suggested deduction value", suggestedDeductionValues.getFirst(), is(calculateDeduction.apply(trade9.getProfit())));
        assertThat("Verify 1st suggested deduction value", suggestedDeductionValues.get(1), is(calculateDeduction.apply(trade2.getProfit())));

    }

}
