package tests.vindex_backoffice_ui_tests.investigationTool;

import business_objects.db.abuse_registry_db.AbuserDeduction;
import business_objects.db.abuse_registry_db.AbuserFraudType;
import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_account_for_mt.crm_tb_account.CrmTbAccountForMtObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.mt_account.MtAccountObject;
import business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObject;
import business_objects.db.clickhouse.mt_mt5_positions.MtMt5PositionsObject;
import business_objects.db.mitigation_service_db.ClientGeneralRestriction;
import helpers.data.ClientHelper;
import helpers.data.enums.deduction.DeductionStatusUi;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.function.Function;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountDataForUi;
import static business_objects.db.clickhouse.crm_tb_account_for_mt.crm_tb_account.CrmTbAccountForMtObjectFactory.generateAccountForMtByAccount;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObjectFactory.generateMt4TradesCoercedAccountProfitComment;
import static business_objects.db.clickhouse.mt_mt5_positions.MtMt5PositionsObjectFactory.generateMtMt5PositionsObject;
import static business_objects.ui.user.UserFactory.autotestUserOne;
import static helpers.api.AbuseRegistryHelper.addFraudForClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.Currency.EUR;
import static helpers.data.enums.Currency.USD;
import static helpers.data.enums.FraudSource.INSIGHT;
import static helpers.data.enums.FraudSubtype.INTERNAL;
import static helpers.data.enums.FraudType.*;
import static helpers.data.enums.FraudTypeStatus.*;
import static helpers.data.enums.Restriction.*;
import static helpers.data.enums.deduction.DeductionStatusApproval.AWAITING_APPROVAL;
import static helpers.data.enums.deduction.DeductionStatusDeduction.TO_BE_DEDUCTED;
import static helpers.data.enums.deduction.DeductionStatusEmail.NOT_SENT;
import static helpers.data.enums.deduction.DeductionStatusOpenPositions.HOLDING;
import static helpers.data.enums.deduction.DeductionStatusOpenPositions.NOT_HOLDING;
import static helpers.data.enums.deduction.DeductionType.FULL_DEDUCTION;
import static helpers.data.enums.deduction.DeductionTypeAccount.ILLEGAL_PROFIT;
import static helpers.data.enums.deduction.DeductionTypeAccount.NO_ILLEGAL_PROFIT;
import static helpers.database.ArHelper.deleteUserFromAbuseRegistry;
import static helpers.database.DbHelper.*;
import static helpers.database.DbName.POSTGRES;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static page_objects.backoffice_pages.investigationTool.RestrictionPage.cleanUserRestriction;
import static utils.Constants.*;
import static utils.Utils.getRandomIntPositive;

@Feature("BMS-1821 Change report fraud drawer")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ManageFraudWithDeductionsTest extends TestBaseWeb {

    private static final ClientHelper client = getRandomVantageClientAllFields();
    private static final CrmTbUserObject crmTbUser = generateUserByClient(client);
    private static final DecimalFormat formatter = new DecimalFormat("#,##0.##");
    private static final String REGEX_PATTERN = "\\d{1,3}(,\\d{3})*(\\.\\d{1,2})? USD$";
    private static final String REGEX_PATTERN_DEDUCTION = String.format("^-%s", REGEX_PATTERN);
    private static final String SUGGESTED_DEDUCTION_PATTERN_ILLEGAL = "Account %sBalance %s USD ・ Illegal %s USD";
    private static final String SUGGESTED_DEDUCTION_PATTERN = "^Account %sBalance " + REGEX_PATTERN;
    private static final String SUGGESTED_DEDUCTION_PATTERN_USD = SUGGESTED_DEDUCTION_PATTERN_ILLEGAL.split(" ・")[0];
    private static final String COMMENT = "Test comment for deductions";
    private static MtAccountObject mtAccount1;
    private static MtAccountObject mtAccount2;
    private static MtAccountObject mtAccount3;
    private static MtAccountObject mtAccount4;
    private static MtAccountObject mtAccount5;
    private static CrmTbAccountForMtObject crmAccountMt1;
    private static CrmTbAccountForMtObject crmAccountMt2;
    private static CrmTbAccountForMtObject crmAccountMt3;
    private static CrmTbAccountForMtObject crmAccountMt4;
    private static CrmTbAccountForMtObject crmAccountMt5;
    private static MtMt4TradesCoercedObject trade1;
    private static MtMt4TradesCoercedObject trade2;
    private static MtMt4TradesCoercedObject trade3;
    private static MtMt4TradesCoercedObject trade4;
    private static MtMt4TradesCoercedObject trade5;
    private static MtMt4TradesCoercedObject tradeWithdrawal;

    @BeforeAll
    static void setup() throws IOException {
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
        crmAccountMt1 = generateAccountForMtByAccount(account1);
        crmAccountMt2 = generateAccountForMtByAccount(account2);
        crmAccountMt3 = generateAccountForMtByAccount(account3);
        crmAccountMt4 = generateAccountForMtByAccount(account4);
        crmAccountMt5 = generateAccountForMtByAccount(account5);


        String comment = "comment";
        trade1 = generateMt4TradesCoercedAccountProfitComment(account1, 500.12 + 10_000d, comment);
        trade2 = generateMt4TradesCoercedAccountProfitComment(account2, 1000.23, comment);
        trade3 = generateMt4TradesCoercedAccountProfitComment(account3, 1800.45, comment);
        trade4 = generateMt4TradesCoercedAccountProfitComment(account4, 2800.67, comment);
        trade5 = generateMt4TradesCoercedAccountProfitComment(account5, 1500.89, comment);
        tradeWithdrawal = generateMt4TradesCoercedAccountProfitComment(account1, -10_000d, "withdraw");

        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        insertObjectsToDb(CRM_TB_ACCOUNT_TABLE_NAME, List.of(account1, account2, account3, account4, account5));
        insertObjectsToDb(MT_ACCOUNT_TABLE_NAME, List.of(mtAccount1, mtAccount2, mtAccount3, mtAccount4, mtAccount5));
        insertObjectsToDb(CRM_TB_ACCOUNT_FOR_MT_TABLE_NAME, List.of(crmAccountMt1, crmAccountMt2, crmAccountMt3, crmAccountMt4, crmAccountMt5));
        insertObjectsToDb(MT4_TRADES_COERCED_TABLE_NAME, List.of(trade1, trade2, trade3, trade4, trade5, tradeWithdrawal));
        MtMt5PositionsObject position = generateMtMt5PositionsObject(client);
        position.setAccount(mtAccount2.account);
        position.setServerId(mtAccount2.sourceIdSt);
        insertObjectToDb(MT5_POSITIONS_TABLE_NAME, position);
        addFraudForClient(client, MARKET_MANIPULATION, POTENTIAL, List.of());
        addFraudForClient(client, HEDGING, INTERNAL, CONFIRMED, List.of("EURUSD", "GBPUSD"));
    }

    @AfterAll
    static void teardown() throws Exception {
        deleteEntryFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        deleteEntryFromDb(MT4_TRADES_COERCED_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        deleteEntryFromDb(MT5_POSITIONS_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        deleteUserFromAbuseRegistry(client.getUcid());
        cleanUserRestriction(client.getUcid());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1477")
    @DisplayName("Verify deductions when no illegal profit is selected in fraud management")
    void deductionsNoIllegalProfitTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        resolvePage.openReportFraudForm();
        resolvePage.addFraud(MARKET_MANIPULATION, CONFIRMED);
        assertThat("Verify suggested deduction is 0 when no account is selected", resolvePage.getSuggestedDeductionAmount(), is("0 USD"));
        assertThat("Verify no illegal profit when no account is selected", resolvePage.getIllegalProfitAmount(), is("Select fraud account"));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1478")
    @DisplayName("Verify suggested deductions are not shown if potential fraud is selected in fraud management")
    void deductionsNoSuggestedIfPotentialFraudTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        resolvePage.openReportFraudForm();
        resolvePage.addFraud(MARKET_MANIPULATION, POTENTIAL);
        assertThat("Verify no suggested deduction when potential fraud type", resolvePage.isSuggestedDeductionSectionVisible(), is(false));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1479")
    @DisplayName("Verify deductions when Holding is selected in fraud management")
    void deductionsHoldingTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        resolvePage.openReportFraudForm();
        resolvePage.addFraud(MARKET_MANIPULATION, CONFIRMED);
        resolvePage.clickIllegalProfitAccountsDropdown();
        resolvePage.clickAccountInDropdown(mtAccount2.account.toString());
        resolvePage.clickUseAsIllegalProfit();
        assertThat("Verify suggested deduction is 0 when no account is selected", resolvePage.getSuggestedDeductionAmount(), is("Holding"));
        assertThat("Verify calculation of illegal profit and balance by accounts", resolvePage.getSuggestedDeductionItems(), contains(String.format(SUGGESTED_DEDUCTION_PATTERN_ILLEGAL, mtAccount2.account, formatter.format(trade2.profit), formatter.format(trade2.profit))));
        assertThat("Verify suggested deductions by accounts", resolvePage.getSuggestedDeductionValues(), contains("Holding"));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1480")
    @DisplayName("Verify deductions when No deduction is selected in fraud management")
    void deductionsNoDeductionTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        resolvePage.openReportFraudForm();
        resolvePage.addFraud(MARKET_MANIPULATION, CONFIRMED);
        resolvePage.clickNoDeductionSwitch();
        assertThat("Verify total suggested deduction amount", resolvePage.getSuggestedDeductionAmount(), is("No deduction"));
        assertThat("Verify total illegal profit amount", resolvePage.getIllegalProfitAmount(), is("Select fraud account"));
        resolvePage.clickIllegalProfitAccountsDropdown();
        resolvePage.clickAccountInDropdown(mtAccount1.account.toString());
        resolvePage.clickUseAsIllegalProfit();
        assertThat("Verify calculation of illegal profit and balance by accounts", resolvePage.getSuggestedDeductionItems(), contains(String.format("Account %sBalance %s USD ・ Profit %s USD", mtAccount1.account, formatter.format(trade1.profit + tradeWithdrawal.profit), formatter.format((trade1.profit + tradeWithdrawal.profit) - tradeWithdrawal.profit))));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1481")
    @DisplayName("Verify calculation of deductions in fraud management")
    void deductionsCalculationTest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        resolvePage.openReportFraudForm();
        String potentialMarketManipulation = String.format("%s %s", POTENTIAL.getDisplayName(), MARKET_MANIPULATION.getName());
//        assertThat("Verify previously reported fraud", resolvePage.getPreviouslyReportedFraudItems2(), contains(List.of(String.format("%s (%s)", HEDGING.getName(), INTERNAL.getName().toLowerCase()), "EURUSD, GBPUSD"), List.of(potentialMarketManipulation, "")));
        resolvePage.deleteFraudByNameNoPopup(potentialMarketManipulation);
        resolvePage.addFraud(LATENCY_ARBITRAGE, CONFIRMED);
        resolvePage.selectFraudSource(INSIGHT.getDisplayName());
        resolvePage.clickIllegalProfitAccountsDropdown();
        resolvePage.clickAccountInDropdown(mtAccount1.account.toString());
        resolvePage.clickUseAsIllegalProfit();
        assertThat("Verify total suggested deduction amount", resolvePage.getSuggestedDeductionAmount(), matchesPattern(REGEX_PATTERN));
        assertThat("Verify total illegal profit amount", resolvePage.getIllegalProfitAmount(), is(String.format("%s USD illegal profit", formatter.format((trade1.profit + tradeWithdrawal.profit) - tradeWithdrawal.profit))));

        List<String> deductionItems = resolvePage.getSuggestedDeductionItems();
        assertThat("Verify amount of deductions", deductionItems.size(), is(5));
        assertThat("Verify 1st deduction illegal profit and balance", deductionItems.stream().filter(u -> u.contains(mtAccount1.account.toString())).toList().getFirst(), is(String.format(SUGGESTED_DEDUCTION_PATTERN_ILLEGAL, mtAccount1.account, formatter.format(trade1.profit + tradeWithdrawal.profit), formatter.format((trade1.profit + tradeWithdrawal.profit) - tradeWithdrawal.profit))));
        assertThat("Verify 2nd deduction illegal profit and balance", deductionItems.stream().filter(u -> u.contains(mtAccount5.account.toString())).toList().getFirst(), is(String.format(SUGGESTED_DEDUCTION_PATTERN_USD, mtAccount5.account, formatter.format(trade5.profit))));
        assertThat("Verify 3rd deduction illegal profit and balance", deductionItems.stream().filter(u -> u.contains(mtAccount4.account.toString())).toList().getFirst(), matchesPattern(String.format(SUGGESTED_DEDUCTION_PATTERN, mtAccount4.account)));
        assertThat("Verify 4th deduction illegal profit and balance", deductionItems.stream().filter(u -> u.contains(mtAccount3.account.toString())).toList().getFirst(), matchesPattern(String.format(SUGGESTED_DEDUCTION_PATTERN, mtAccount3.account)));
        assertThat("Verify 5th deduction illegal profit and balance", deductionItems.stream().filter(u -> u.contains(mtAccount2.account.toString())).toList().getFirst(), is(String.format(SUGGESTED_DEDUCTION_PATTERN_USD, mtAccount2.account, formatter.format(trade2.profit))));

        Function<Double, String> calculateDeduction = deduction -> String.format("%s USD", formatter.format(deduction * -1));
        List<String> suggestedDeductionValues = resolvePage.getSuggestedDeductionValues();
        assertThat("Verify amount of suggested deductions", suggestedDeductionValues.size(), is(5));
        assertThat("Verify 1st suggested deduction value", suggestedDeductionValues.getFirst(), is(calculateDeduction.apply(trade1.profit + tradeWithdrawal.profit)));
        assertThat("Verify 2nd suggested deduction value", suggestedDeductionValues.get(3), is(calculateDeduction.apply(trade5.profit)));
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
        AbuserDeduction deduction1 = new AbuserDeduction(client.getUcid(), null, mtAccount1.account.toString(), mtAccount1.sourceIdSt, mtAccount1.server, mtAccount1.currency, client.getBrand(), NOT_HOLDING.getDisplayName(), NOT_SENT.getDisplayName(), TO_BE_DEDUCTED.getDisplayName(), AWAITING_APPROVAL.getDisplayName(), COMMENT, (trade1.profit + tradeWithdrawal.profit) - tradeWithdrawal.profit, null, trade1.profit + tradeWithdrawal.profit, null, null, null, null, null, String.format("%s %s", autotestUserOne().getFirstName(), autotestUserOne().getLastName()), VINDEX_BO_SYSTEM, ILLEGAL_PROFIT.getDisplayName(), null, null, trade1.profit + tradeWithdrawal.profit, null, client.getUserId().toString(), trade1.profit + tradeWithdrawal.profit, null, false, FULL_DEDUCTION.getDisplayName());
        AbuserDeduction deduction2 = new AbuserDeduction(client.getUcid(), null, mtAccount5.account.toString(), mtAccount5.sourceIdSt, mtAccount5.server, mtAccount5.currency, client.getBrand(), NOT_HOLDING.getDisplayName(), NOT_SENT.getDisplayName(), TO_BE_DEDUCTED.getDisplayName(), AWAITING_APPROVAL.getDisplayName(), COMMENT, 0d, null, trade5.profit, null, null, null, null, null, String.format("%s %s", autotestUserOne().getFirstName(), autotestUserOne().getLastName()), VINDEX_BO_SYSTEM, NO_ILLEGAL_PROFIT.getDisplayName(), null, null, trade5.profit, null, client.getUserId().toString(), trade5.profit, null, false, FULL_DEDUCTION.getDisplayName());
        AbuserDeduction deduction3 = new AbuserDeduction(client.getUcid(), null, mtAccount4.account.toString(), mtAccount4.sourceIdSt, mtAccount4.server, mtAccount4.currency, client.getBrand(), NOT_HOLDING.getDisplayName(), NOT_SENT.getDisplayName(), TO_BE_DEDUCTED.getDisplayName(), AWAITING_APPROVAL.getDisplayName(), COMMENT, 0d, null, trade4.profit, null, null, null, null, null, String.format("%s %s", autotestUserOne().getFirstName(), autotestUserOne().getLastName()), VINDEX_BO_SYSTEM, NO_ILLEGAL_PROFIT.getDisplayName(), null, null, trade4.profit, null, client.getUserId().toString(), trade4.profit, null, false, FULL_DEDUCTION.getDisplayName());
        AbuserDeduction deduction4 = new AbuserDeduction(client.getUcid(), null, mtAccount3.account.toString(), mtAccount3.sourceIdSt, mtAccount3.server, mtAccount3.currency, client.getBrand(), NOT_HOLDING.getDisplayName(), NOT_SENT.getDisplayName(), TO_BE_DEDUCTED.getDisplayName(), AWAITING_APPROVAL.getDisplayName(), COMMENT, 0d, null, trade3.profit, null, null, null, null, null, String.format("%s %s", autotestUserOne().getFirstName(), autotestUserOne().getLastName()), VINDEX_BO_SYSTEM, NO_ILLEGAL_PROFIT.getDisplayName(), null, null, trade3.profit, null, client.getUserId().toString(), trade3.profit, null, false, FULL_DEDUCTION.getDisplayName());
        AbuserDeduction deduction5 = new AbuserDeduction(client.getUcid(), null, mtAccount2.account.toString(), mtAccount2.sourceIdSt, mtAccount2.server, mtAccount2.currency, client.getBrand(), HOLDING.getDisplayName(), NOT_SENT.getDisplayName(), TO_BE_DEDUCTED.getDisplayName(), AWAITING_APPROVAL.getDisplayName(), COMMENT, 0d, null, 0d, null, null, null, null, null, String.format("%s %s", autotestUserOne().getFirstName(), autotestUserOne().getLastName()), VINDEX_BO_SYSTEM, NO_ILLEGAL_PROFIT.getDisplayName(), null, null, 0d, null, client.getUserId().toString(), trade2.profit, null, false, FULL_DEDUCTION.getDisplayName());
        assertThat("Verify deductions in abuser_deduction table are as expected", deductionList, containsInAnyOrder(deduction1, deduction2, deduction3, deduction4, deduction5));

        List<ClientGeneralRestriction> restrictionList = getObjectsFromDB(POSTGRES, MITIGATION_CLIENT_GENERAL_RESTRICTION, String.format("ucid = '%s'", client.getUcid()), ClientGeneralRestriction.class);
        ClientGeneralRestriction restrictionCredit = new ClientGeneralRestriction();
        restrictionCredit.setUcid(client.getUcid());
        restrictionCredit.setRegulator(client.getRegulator());
        restrictionCredit.setRestrictionId(CREDIT_AND_BONUS.getIdLong());
        restrictionCredit.setComment(COMMENT);
        restrictionCredit.setStatus(APPLIED_STATUS);
        ClientGeneralRestriction restrictionAccountCreation = new ClientGeneralRestriction();
        restrictionAccountCreation.setUcid(client.getUcid());
        restrictionAccountCreation.setRegulator(client.getRegulator());
        restrictionAccountCreation.setRestrictionId(ACCOUNT_CREATION.getIdLong());
        restrictionAccountCreation.setComment(COMMENT);
        restrictionAccountCreation.setStatus(APPLIED_STATUS);
        ClientGeneralRestriction restrictionDeposits = new ClientGeneralRestriction();
        restrictionDeposits.setUcid(client.getUcid());
        restrictionDeposits.setRegulator(client.getRegulator());
        restrictionDeposits.setRestrictionId(DEPOSITS.getIdLong());
        restrictionDeposits.setComment(COMMENT);
        restrictionDeposits.setStatus(APPLIED_STATUS);
        ClientGeneralRestriction restrictionInternalTransfer = new ClientGeneralRestriction();
        restrictionInternalTransfer.setUcid(client.getUcid());
        restrictionInternalTransfer.setRegulator(client.getRegulator());
        restrictionInternalTransfer.setRestrictionId(INTERNAL_TRANSFER.getIdLong());
        restrictionInternalTransfer.setComment(COMMENT);
        restrictionInternalTransfer.setStatus(APPLIED_STATUS);
        ClientGeneralRestriction restrictionWithdrawal = new ClientGeneralRestriction();
        restrictionWithdrawal.setUcid(client.getUcid());
        restrictionWithdrawal.setRegulator(client.getRegulator());
        restrictionWithdrawal.setRestrictionId(WITHDRAWALS.getIdLong());
        restrictionWithdrawal.setComment(COMMENT);
        restrictionWithdrawal.setStatus(APPLIED_STATUS);
        assertThat("Verify restrictions in client_general_restriction table are as expected", restrictionList, containsInAnyOrder(restrictionCredit, restrictionAccountCreation, restrictionDeposits, restrictionInternalTransfer, restrictionWithdrawal));

        List<AbuserFraudType> abuserFraudTypeList = getObjectsFromDB(POSTGRES, AR_ABUSER_FRAUD_TYPE_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()), AbuserFraudType.class);
        AbuserFraudType fraudMarketManipulation = new AbuserFraudType();
        fraudMarketManipulation.setUcid(client.getUcid());
        fraudMarketManipulation.setFraudTypeCode(MARKET_MANIPULATION.getCode());
        fraudMarketManipulation.setStatus(CLEANED.getStatus());
        fraudMarketManipulation.setComment(COMMENT);
        fraudMarketManipulation.setModifiedByUser(String.format("%s %s", autotestUserOne().getFirstName(), autotestUserOne().getLastName()));
        fraudMarketManipulation.setModifiedBySystem(VINDEX_BO_SYSTEM);
        fraudMarketManipulation.setSymbols("[]");
        fraudMarketManipulation.setFraudSource(INSIGHT.getDisplayName());
        AbuserFraudType fraudHedging = new AbuserFraudType();
        fraudHedging.setUcid(client.getUcid());
        fraudHedging.setFraudTypeCode(HEDGING.getCode());
        fraudHedging.setStatus(CONFIRMED.getStatus());
        fraudHedging.setComment("Set by autotest");
        fraudHedging.setModifiedByUser("Auto Test");
        fraudHedging.setModifiedBySystem("BO");
        fraudHedging.setFraudSubtypeCode(INTERNAL.getCode());
        fraudHedging.setSymbols("[\"EURUSD\",\"GBPUSD\"]");
        AbuserFraudType fraudLatencyArbitrage = new AbuserFraudType();
        fraudLatencyArbitrage.setUcid(client.getUcid());
        fraudLatencyArbitrage.setFraudTypeCode(LATENCY_ARBITRAGE.getCode());
        fraudLatencyArbitrage.setStatus(CONFIRMED.getStatus());
        fraudLatencyArbitrage.setComment(COMMENT);
        fraudLatencyArbitrage.setModifiedByUser(String.format("%s %s", autotestUserOne().getFirstName(), autotestUserOne().getLastName()));
        fraudLatencyArbitrage.setModifiedBySystem(VINDEX_BO_SYSTEM);
        fraudLatencyArbitrage.setFraudSource(INSIGHT.getDisplayName());
        assertThat("Verify frauds in abuser_fraud_type table are as expected", abuserFraudTypeList, containsInAnyOrder(fraudMarketManipulation, fraudHedging, fraudLatencyArbitrage));
    }
}