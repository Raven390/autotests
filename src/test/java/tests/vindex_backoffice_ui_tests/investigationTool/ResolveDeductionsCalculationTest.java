package tests.vindex_backoffice_ui_tests.investigationTool;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountDataForUi;
import static business_objects.db.clickhouse.crm_tb_account_for_mt.crm_tb_account.CrmTbAccountForMtObjectFactory.generateAccountForMtByAccount;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObjectFactory.generateMt4TradesCoercedAccountProfitComment;
import static business_objects.db.clickhouse.mt_mt5_positions.MtMt5PositionsObjectFactory.generateMtMt5PositionsObject;
import static business_objects.kafka.alerts.RuleAlertFactory.generateRuleAlertByUcid;
import static business_objects.ui.user.UserFactory.autotestUserOne;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.Currency.EUR;
import static helpers.data.enums.Currency.USD;
import static helpers.data.enums.FraudType.MARKET_MANIPULATION;
import static helpers.data.enums.FraudTypeStatus.CONFIRMED;
import static helpers.data.enums.FraudTypeStatus.POTENTIAL;
import static helpers.data.enums.deduction.DeductionStatusApproval.AWAITING_APPROVAL;
import static helpers.data.enums.deduction.DeductionStatusApproval.NOT_REQUIRED;
import static helpers.data.enums.deduction.DeductionStatusDeduction.NO_DEDUCTION;
import static helpers.data.enums.deduction.DeductionStatusDeduction.TO_BE_DEDUCTED;
import static helpers.data.enums.deduction.DeductionStatusEmail.NOT_SENT;
import static helpers.data.enums.deduction.DeductionStatusOpenPositions.HOLDING;
import static helpers.data.enums.deduction.DeductionStatusOpenPositions.NOT_HOLDING;
import static helpers.data.enums.deduction.DeductionType.FULL_DEDUCTION;
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

import business_objects.db.abuse_registry_db.AbuserDeduction;
import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_account_for_mt.crm_tb_account.CrmTbAccountForMtObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.mt_account.MtAccountObject;
import business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObject;
import business_objects.db.clickhouse.mt_mt5_positions.MtMt5PositionsObject;
import business_objects.kafka.alerts.RuleAlert;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.data.ClientHelper;
import helpers.data.enums.deduction.DeductionStatusUi;
import helpers.kafka.KafkaHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import java.text.DecimalFormat;
import java.util.List;
import java.util.function.Function;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

@Feature("BMS-1426 Resolution. Calculating illegal profit")
@Feature("BMS-1812 Resolution. Complete investigation with deduction")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ResolveDeductionsCalculationTest extends TestBaseWeb {

    private static final KafkaHelper kafka = new KafkaHelper();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final ClientHelper client = getRandomVantageClientAllFields();
    private static final CrmTbUserObject crmTbUser = generateUserByClient(client);
    private static final DecimalFormat formatter = new DecimalFormat("#,##0.##");
    private static final String REGEX_PATTERN = "\\d{1,3}(,\\d{3})*(\\.\\d{1,2})? USD$";
    private static final String REGEX_PATTERN_DEDUCTION = String.format("^-%s", REGEX_PATTERN);
    private static final String SUGGESTED_DEDUCTION_PATTERN_ILLEGAL = "Account %sBalance %s USD ・ Illegal %s USD";
    private static final String SUGGESTED_DEDUCTION_PATTERN = "^Account %sBalance " + REGEX_PATTERN;
    private static final String SUGGESTED_DEDUCTION_PATTERN_USD =
            SUGGESTED_DEDUCTION_PATTERN_ILLEGAL.split(" ・")[0];
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
    private static MtMt4TradesCoercedObject tradeWithdrawal;

    @BeforeAll
    static void setup() throws JsonProcessingException {
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

        CrmTbAccountForMtObject crmTbAccountMt1 = generateAccountForMtByAccount(account1);
        CrmTbAccountForMtObject crmTbAccountMt2 = generateAccountForMtByAccount(account2);
        CrmTbAccountForMtObject crmTbAccountMt3 = generateAccountForMtByAccount(account3);
        CrmTbAccountForMtObject crmTbAccountMt4 = generateAccountForMtByAccount(account4);
        CrmTbAccountForMtObject crmTbAccountMt5 = generateAccountForMtByAccount(account5);

        String comment = "comment";
        trade1 = generateMt4TradesCoercedAccountProfitComment(account1, 500.12 + 10_000d, comment);
        trade2 = generateMt4TradesCoercedAccountProfitComment(account2, 1000.23, comment);
        trade3 = generateMt4TradesCoercedAccountProfitComment(account3, 1800.45, comment);
        trade4 = generateMt4TradesCoercedAccountProfitComment(account4, 2800.67, comment);
        trade5 = generateMt4TradesCoercedAccountProfitComment(account5, 1500.89, comment);
        tradeWithdrawal = generateMt4TradesCoercedAccountProfitComment(account1, -10_000d, "withdraw");

        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        insertObjectsToDb(
                CRM_TB_ACCOUNT_FOR_MT_TABLE_NAME,
                List.of(crmTbAccountMt1, crmTbAccountMt2, crmTbAccountMt3, crmTbAccountMt4, crmTbAccountMt5));
        insertObjectsToDb(MT_ACCOUNT_TABLE_NAME, List.of(mtAccount1, mtAccount2, mtAccount3, mtAccount4, mtAccount5));
        insertObjectsToDb(
                MT4_TRADES_COERCED_TABLE_NAME, List.of(trade1, trade2, trade3, trade4, trade5, tradeWithdrawal));
        MtMt5PositionsObject position = generateMtMt5PositionsObject(client);
        position.setAccount(mtAccount2.account);
        position.setServerId(mtAccount2.sourceIdSt);
        insertObjectToDb(MT5_POSITIONS_TABLE_NAME, position);
        RuleAlert alert = generateRuleAlertByUcid(client.getUcid());
        alert.rule.attributes.account = mtAccount1.account.toString();
        kafka.produceMessage(alert.alertId, objectMapper.writeValueAsString(alert), KAFKA_TOPIC_ALERTS);
    }

    @AfterAll
    static void teardown() throws Exception {
        deleteEntryFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        deleteEntryFromDb(MT4_TRADES_COERCED_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        deleteEntryFromDb(MT5_POSITIONS_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        deleteUserFromAbuseRegistry(client.getUcid());
        closeAlert(crmTbUser.ucid);
    }

    @Order(1)
    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1382")
    @DisplayName("Verify deductions when no illegal profit is selected")
    void deductionsNoIllegalProfitTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        resolvePage.openResolveSuspicious();
        resolvePage.addFraud(MARKET_MANIPULATION, CONFIRMED);
        resolvePage.clickIllegalProfitAccountsDropdown();
        assertThat(
                "Verify account with alert has lightning icon",
                resolvePage.isAccountWithAlert(mtAccount1.account.toString()),
                is(true));
        resolvePage.clickAccountInDropdown(mtAccount1.account.toString());
        resolvePage.clickUseAsIllegalProfit();
        assertThat(
                "Verify suggested deduction is 0 when no account is selected",
                resolvePage.getSuggestedDeductionAmount(),
                is("0 USD"));
        assertThat(
                "Verify no illegal profit when no account is selected",
                resolvePage.getIllegalProfitAmount(),
                is("Select fraud account"));
    }

    @Order(2)
    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1383")
    @DisplayName("Verify suggested deductions are not shown if potential fraud is selected")
    void deductionsNoSuggestedIfPotentialFraudTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        resolvePage.openResolveSuspicious();
        resolvePage.addFraud(MARKET_MANIPULATION, POTENTIAL);
        assertThat(
                "Verify no suggested deduction when potential fraud type",
                resolvePage.isSuggestedDeductionSectionVisible(),
                is(false));
    }

    @Order(3)
    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1384")
    @DisplayName("Verify deductions when Holding is selected")
    void deductionsHoldingTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        resolvePage.openResolveSuspicious();
        resolvePage.addFraud(MARKET_MANIPULATION, CONFIRMED);
        resolvePage.clickIllegalProfitAccountsDropdown();
        resolvePage.clickAccountInDropdown(mtAccount1.account.toString());
        resolvePage.clickAccountInDropdown(mtAccount2.account.toString());
        resolvePage.clickUseAsIllegalProfit();
        assertThat(
                "Verify suggested deduction is 0 when no account is selected",
                resolvePage.getSuggestedDeductionAmount(),
                is("Holding"));
        assertThat(
                "Verify calculation of illegal profit and balance by accounts",
                resolvePage.getSuggestedDeductionItems(),
                contains(String.format(
                        SUGGESTED_DEDUCTION_PATTERN_ILLEGAL,
                        mtAccount2.account,
                        formatter.format(trade2.getProfit()),
                        formatter.format(trade2.getProfit()))));
        assertThat(
                "Verify suggested deductions by accounts",
                resolvePage.getSuggestedDeductionValues(),
                contains("Holding"));
    }

    @Order(4)
    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1381")
    @DisplayName("Verify calculation of deductions in resolve")
    void deductionsCalculationTest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        resolvePage.openResolveSuspicious();
        resolvePage.addFraud(MARKET_MANIPULATION, CONFIRMED);
        assertThat(
                "Verify total suggested deduction amount",
                resolvePage.getSuggestedDeductionAmount(),
                matchesPattern(REGEX_PATTERN));
        assertThat(
                "Verify total illegal profit amount",
                resolvePage.getIllegalProfitAmount(),
                is(String.format(
                        "%s USD illegal profit",
                        formatter.format(
                                (trade1.getProfit() + tradeWithdrawal.getProfit()) - tradeWithdrawal.getProfit()))));

        List<String> deductionItems = resolvePage.getSuggestedDeductionItems();
        assertThat("Verify amount of deductions", deductionItems.size(), is(5));
        assertThat(
                "Verify 1st deduction illegal profit and balance",
                deductionItems.getFirst(),
                is(String.format(
                        SUGGESTED_DEDUCTION_PATTERN_ILLEGAL,
                        mtAccount1.account,
                        formatter.format(trade1.getProfit() + tradeWithdrawal.getProfit()),
                        formatter.format(
                                (trade1.getProfit() + tradeWithdrawal.getProfit()) - tradeWithdrawal.getProfit()))));
        assertThat(
                "Verify 2nd deduction illegal profit and balance",
                deductionItems.get(1),
                is(String.format(
                        SUGGESTED_DEDUCTION_PATTERN_USD, mtAccount5.account, formatter.format(trade5.getProfit()))));
        assertThat(
                "Verify 3rd deduction illegal profit and balance",
                deductionItems.get(2),
                matchesPattern(String.format(SUGGESTED_DEDUCTION_PATTERN, mtAccount4.account)));
        assertThat(
                "Verify 4th deduction illegal profit and balance",
                deductionItems.get(3),
                matchesPattern(String.format(SUGGESTED_DEDUCTION_PATTERN, mtAccount3.account)));
        assertThat(
                "Verify 5th deduction illegal profit and balance",
                deductionItems.getLast(),
                is(String.format(
                        SUGGESTED_DEDUCTION_PATTERN_USD, mtAccount2.account, formatter.format(trade2.getProfit()))));

        Function<Double, String> calculateDeduction =
                deduction -> String.format("%s USD", formatter.format(deduction * -1));
        List<String> suggestedDeductionValues = resolvePage.getSuggestedDeductionValues();
        assertThat("Verify amount of suggested deductions", suggestedDeductionValues.size(), is(5));
        assertThat(
                "Verify 1st suggested deduction value",
                suggestedDeductionValues.getFirst(),
                is(calculateDeduction.apply(trade1.getProfit() + tradeWithdrawal.getProfit())));
        assertThat(
                "Verify 2nd suggested deduction value",
                suggestedDeductionValues.get(1),
                is(calculateDeduction.apply(trade5.getProfit())));
        assertThat(
                "Verify 3rd suggested deduction value",
                suggestedDeductionValues.get(2),
                matchesPattern(REGEX_PATTERN_DEDUCTION));
        assertThat(
                "Verify 4th suggested deduction value",
                suggestedDeductionValues.get(3),
                matchesPattern(REGEX_PATTERN_DEDUCTION));
        assertThat(
                "Verify 5th suggested deduction value",
                suggestedDeductionValues.getLast(),
                is(DeductionStatusUi.HOLDING.getDisplayName()));

        resolvePage.resolveNoActions(COMMENT);
        List<AbuserDeduction> deductionList = getObjectsFromDB(
                POSTGRES,
                AR_ABUSER_DEDUCTION_TABLE_NAME,
                String.format("ucid = '%s'", client.getUcid()),
                AbuserDeduction.class);
        for (AbuserDeduction deduction : deductionList) {
            assertThat(
                    "Verify created deduction has abuser_history_id not null",
                    deduction.getAbuserHistoryId(),
                    notNullValue());
            assertThat(
                    "Verify created deduction has illegal_profit not null",
                    deduction.getIllegalProfitUsd(),
                    notNullValue());
            assertThat(
                    "Verify created deduction has suggested_deduction not null",
                    deduction.getSuggestedDeductionUsd(),
                    notNullValue());
            assertThat("Verify created deduction has created_at not null", deduction.getCreatedAt(), notNullValue());
            assertThat("Verify created deduction has updated_at not null", deduction.getUpdatedAt(), notNullValue());
        }
        AbuserDeduction deduction1 = new AbuserDeduction(
                client.getUcid(),
                null,
                mtAccount1.account.toString(),
                mtAccount1.sourceIdSt,
                mtAccount1.server,
                mtAccount1.currency,
                client.getBrand(),
                NOT_HOLDING.getDisplayName(),
                NOT_SENT.getDisplayName(),
                TO_BE_DEDUCTED.getDisplayName(),
                AWAITING_APPROVAL.getDisplayName(),
                COMMENT,
                (trade1.getProfit() + tradeWithdrawal.getProfit()) - tradeWithdrawal.getProfit(),
                null,
                trade1.getProfit() + tradeWithdrawal.getProfit(),
                null,
                null,
                null,
                null,
                null,
                String.format(
                        "%s %s",
                        autotestUserOne().getFirstName(), autotestUserOne().getLastName()),
                VINDEX_BO_SYSTEM,
                ILLEGAL_PROFIT.getDisplayName(),
                null,
                null,
                trade1.getProfit() + tradeWithdrawal.getProfit(),
                null,
                client.getUserId().toString(),
                trade1.getProfit() + tradeWithdrawal.getProfit(),
                null,
                false,
                FULL_DEDUCTION.getDisplayName());
        AbuserDeduction deduction2 = new AbuserDeduction(
                client.getUcid(),
                null,
                mtAccount5.account.toString(),
                mtAccount5.sourceIdSt,
                mtAccount5.server,
                mtAccount5.currency,
                client.getBrand(),
                NOT_HOLDING.getDisplayName(),
                NOT_SENT.getDisplayName(),
                TO_BE_DEDUCTED.getDisplayName(),
                AWAITING_APPROVAL.getDisplayName(),
                COMMENT,
                0d,
                null,
                trade5.getProfit(),
                null,
                null,
                null,
                null,
                null,
                String.format(
                        "%s %s",
                        autotestUserOne().getFirstName(), autotestUserOne().getLastName()),
                VINDEX_BO_SYSTEM,
                NO_ILLEGAL_PROFIT.getDisplayName(),
                null,
                null,
                trade5.getProfit(),
                null,
                client.getUserId().toString(),
                trade5.getProfit(),
                null,
                false,
                FULL_DEDUCTION.getDisplayName());
        AbuserDeduction deduction3 = new AbuserDeduction(
                client.getUcid(),
                null,
                mtAccount4.account.toString(),
                mtAccount4.sourceIdSt,
                mtAccount4.server,
                mtAccount4.currency,
                client.getBrand(),
                NOT_HOLDING.getDisplayName(),
                NOT_SENT.getDisplayName(),
                TO_BE_DEDUCTED.getDisplayName(),
                AWAITING_APPROVAL.getDisplayName(),
                COMMENT,
                0d,
                null,
                trade4.getProfit(),
                null,
                null,
                null,
                null,
                null,
                String.format(
                        "%s %s",
                        autotestUserOne().getFirstName(), autotestUserOne().getLastName()),
                VINDEX_BO_SYSTEM,
                NO_ILLEGAL_PROFIT.getDisplayName(),
                null,
                null,
                trade4.getProfit(),
                null,
                client.getUserId().toString(),
                trade4.getProfit(),
                null,
                false,
                FULL_DEDUCTION.getDisplayName());
        AbuserDeduction deduction4 = new AbuserDeduction(
                client.getUcid(),
                null,
                mtAccount3.account.toString(),
                mtAccount3.sourceIdSt,
                mtAccount3.server,
                mtAccount3.currency,
                client.getBrand(),
                NOT_HOLDING.getDisplayName(),
                NOT_SENT.getDisplayName(),
                TO_BE_DEDUCTED.getDisplayName(),
                AWAITING_APPROVAL.getDisplayName(),
                COMMENT,
                0d,
                null,
                trade3.getProfit(),
                null,
                null,
                null,
                null,
                null,
                String.format(
                        "%s %s",
                        autotestUserOne().getFirstName(), autotestUserOne().getLastName()),
                VINDEX_BO_SYSTEM,
                NO_ILLEGAL_PROFIT.getDisplayName(),
                null,
                null,
                trade3.getProfit(),
                null,
                client.getUserId().toString(),
                trade3.getProfit(),
                null,
                false,
                FULL_DEDUCTION.getDisplayName());
        AbuserDeduction deduction5 = new AbuserDeduction(
                client.getUcid(),
                null,
                mtAccount2.account.toString(),
                mtAccount2.sourceIdSt,
                mtAccount2.server,
                mtAccount2.currency,
                client.getBrand(),
                HOLDING.getDisplayName(),
                NOT_SENT.getDisplayName(),
                TO_BE_DEDUCTED.getDisplayName(),
                AWAITING_APPROVAL.getDisplayName(),
                COMMENT,
                0d,
                null,
                0d,
                null,
                null,
                null,
                null,
                null,
                String.format(
                        "%s %s",
                        autotestUserOne().getFirstName(), autotestUserOne().getLastName()),
                VINDEX_BO_SYSTEM,
                NO_ILLEGAL_PROFIT.getDisplayName(),
                null,
                null,
                0d,
                null,
                client.getUserId().toString(),
                trade2.getProfit(),
                null,
                false,
                FULL_DEDUCTION.getDisplayName());
        assertThat(
                "Verify deductions in abuser_deduction table are as expected",
                deductionList,
                containsInAnyOrder(deduction1, deduction2, deduction3, deduction4, deduction5));
    }

    @Order(5)
    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1386")
    @DisplayName("Verify deductions when No deduction is selected")
    void deductionsNoDeductionTest() throws Exception {
        deleteUserFromAbuseRegistry(client.getUcid());
        RuleAlert alert = generateRuleAlertByUcid(client.getUcid());
        alert.rule.attributes.account = mtAccount1.account.toString();
        kafka.produceMessage(alert.alertId, objectMapper.writeValueAsString(alert), KAFKA_TOPIC_ALERTS);
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        resolvePage.openResolveSuspicious();
        resolvePage.addFraud(MARKET_MANIPULATION, CONFIRMED);
        resolvePage.clickNoDeductionSwitch();
        assertThat(
                "Verify total suggested deduction amount",
                resolvePage.getSuggestedDeductionAmount(),
                is("No deduction"));
        assertThat("Verify total illegal profit amount", resolvePage.getIllegalProfitAmount(), is("1 fraud account"));
        assertThat(
                "Verify calculation of illegal profit and balance by accounts",
                resolvePage.getSuggestedDeductionItems(),
                contains(String.format(
                        "Account %sBalance %s USD ・ Profit %s USD",
                        mtAccount1.account,
                        formatter.format(trade1.getProfit() + tradeWithdrawal.getProfit()),
                        formatter.format(
                                (trade1.getProfit() + tradeWithdrawal.getProfit()) - tradeWithdrawal.getProfit()))));
        resolvePage.resolveNoActions(COMMENT);
        AbuserDeduction deduction = getObjectsFromDB(
                        POSTGRES,
                        AR_ABUSER_DEDUCTION_TABLE_NAME,
                        String.format("ucid = '%s'", client.getUcid()),
                        AbuserDeduction.class)
                .getFirst();
        assertThat(
                "Verify created deduction has abuser_history_id not null",
                deduction.getAbuserHistoryId(),
                notNullValue());
        assertThat(
                "Verify created deduction has illegal_profit not null", deduction.getIllegalProfit(), notNullValue());
        assertThat(
                "Verify created deduction has suggested_deduction not null",
                deduction.getSuggestedDeduction(),
                notNullValue());
        assertThat("Verify created deduction has created_at not null", deduction.getCreatedAt(), notNullValue());
        assertThat("Verify created deduction has updated_at not null", deduction.getUpdatedAt(), notNullValue());
        AbuserDeduction expectedDeduction = new AbuserDeduction(
                client.getUcid(),
                null,
                mtAccount1.account.toString(),
                mtAccount1.sourceIdSt,
                mtAccount1.server,
                mtAccount1.currency,
                client.getBrand(),
                NOT_HOLDING.getDisplayName(),
                NOT_SENT.getDisplayName(),
                NO_DEDUCTION.getDisplayName(),
                NOT_REQUIRED.getDisplayName(),
                COMMENT,
                0d,
                null,
                0d,
                null,
                null,
                null,
                null,
                null,
                String.format(
                        "%s %s",
                        autotestUserOne().getFirstName(), autotestUserOne().getLastName()),
                VINDEX_BO_SYSTEM,
                ILLEGAL_PROFIT.getDisplayName(),
                null,
                null,
                0d,
                null,
                client.getUserId().toString(),
                trade1.getProfit() + tradeWithdrawal.getProfit(),
                null,
                false,
                NO_DEDUCTION.getDisplayName());
        assertThat("Verify deductions in abuser_deduction table are as expected", deduction, is(expectedDeduction));
    }
}
