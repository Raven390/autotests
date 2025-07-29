package tests.vindex_backoffice_ui_tests.investigationTool;

import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalObject;
import business_objects.db.clickhouse.mt_account.MtAccountObject;
import business_objects.db.clickhouse.mt_mt5_positions.MtMt5PositionsObject;
import business_objects.kafka.alerts.RuleAlert;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.data.ClientHelper;
import helpers.kafka.KafkaHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

import java.text.DecimalFormat;
import java.util.List;
import java.util.function.Function;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountDataForUi;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalObjectFactory.generateWithdrawalByClient;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static business_objects.db.clickhouse.mt_mt5_positions.MtMt5PositionsObjectFactory.generateMtMt5PositionsObject;
import static business_objects.kafka.alerts.RuleAlertFactory.generateRuleAlertByUcid;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.FraudType.MARKET_MANIPULATION;
import static helpers.data.enums.FraudTypeStatus.CONFIRMED;
import static helpers.data.enums.FraudTypeStatus.POTENTIAL;
import static helpers.database.BoHelper.closeAlert;
import static helpers.database.BoHelper.deleteUserAR;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Utils.getRandomIntPositive;

@Feature("BMS-1426 Resolution. Calculating illegal profit")
class ResolveDeductionsCalculationTest extends TestBaseWeb {

    private static final KafkaHelper kafka = new KafkaHelper();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final ClientHelper client = getRandomVantageClientAllFields();
    private static final CrmTbUserObject crmTbUser = generateUserByClient(client);
    private static final String CURRENCY_USD = "USD";
    private static final String CURRENCY_EUR = "EUR";
    private static final DecimalFormat formatter = new DecimalFormat("#,##0.##");
    private static final String SUGGESTED_DEDUCTION_PATTERN_ILLEGAL = "Account %sBalance %s USD ・ Illegal %s USD";
    private static final String SUGGESTED_DEDUCTION_PATTERN = SUGGESTED_DEDUCTION_PATTERN_ILLEGAL.split(" ・")[0];
    private static MtAccountObject mtAccount1;
    private static MtAccountObject mtAccount2;
    private static MtAccountObject mtAccount3;
    private static MtAccountObject mtAccount4;
    private static MtAccountObject mtAccount5;
    private static CrmTbWithdrawalObject withdrawal;

    @BeforeAll
    static void setup() throws JsonProcessingException {
        CrmTbAccountObject account1 = generateCrmTbAccountDataForUi(client);
        account1.currency = CURRENCY_USD;
        CrmTbAccountObject account2 = generateCrmTbAccountDataForUi(client);
        account2.account = getRandomIntPositive();
        account2.currency = CURRENCY_USD;
        CrmTbAccountObject account3 = generateCrmTbAccountDataForUi(client);
        account3.account = getRandomIntPositive();
        account3.currency = CURRENCY_EUR;
        CrmTbAccountObject account4 = generateCrmTbAccountDataForUi(client);
        account4.account = getRandomIntPositive();
        account4.currency = CURRENCY_EUR;
        CrmTbAccountObject account5 = generateCrmTbAccountDataForUi(client);
        account5.account = getRandomIntPositive();
        account5.currency = CURRENCY_USD;
        mtAccount1 = generateMtAccountByCrmTbAccount(account1);
        mtAccount1.balanceUsd = 500.12;
        mtAccount1.balance = 500.12;
        mtAccount2 = generateMtAccountByCrmTbAccount(account2);
        mtAccount2.balanceUsd = 1000.23;
        mtAccount2.balance = 1000.23;
        mtAccount3 = generateMtAccountByCrmTbAccount(account3);
        mtAccount3.balanceUsd = 2000.45;
        mtAccount3.balance = 1800.45;
        mtAccount4 = generateMtAccountByCrmTbAccount(account4);
        mtAccount4.balanceUsd = 3000.67;
        mtAccount4.balance = 2800.67;
        mtAccount5 = generateMtAccountByCrmTbAccount(account5);
        mtAccount5.balanceUsd = 1500.89;
        mtAccount5.balance = 1500.89;
        withdrawal = generateWithdrawalByClient(client);
        withdrawal.amountUsd = 10_000d;
        withdrawal.amount = 10_000d;
        withdrawal.reversedAmountUsd = 0d;
        withdrawal.reversedAmount = 0d;
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        insertObjectsToDb(CRM_TB_ACCOUNT_TABLE_NAME, List.of(account1, account2, account3, account4, account5));
        insertObjectsToDb(MT_ACCOUNT_TABLE_NAME, List.of(mtAccount1, mtAccount2, mtAccount3, mtAccount4, mtAccount5));
        insertObjectToDb(CRM_WITHDRAWAL_TABLE_NAME, withdrawal);
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
        deleteEntryFromDb(CRM_WITHDRAWAL_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        deleteEntryFromDb(MT5_POSITIONS_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        deleteUserAR(client.getUcid());
        closeAlert(crmTbUser.ucid);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1381")
    @DisplayName("Verify calculation of deductions in resolve")
    void deductionsCalculationTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        resolvePage.openResolveSuspicious();
        resolvePage.addFraud(MARKET_MANIPULATION, CONFIRMED);
        assertThat("Verify total suggested deduction amount", resolvePage.getSuggestedDeductionAmount(), is(String.format("%s USD", formatter.format(mtAccount1.balanceUsd + mtAccount3.balanceUsd + mtAccount4.balanceUsd + mtAccount5.balanceUsd))));
        assertThat("Verify total illegal profit amount", resolvePage.getIllegalProfitAmount(), is(String.format("%s USD illegal profit", formatter.format(mtAccount1.balanceUsd + withdrawal.amountUsd))));
        assertThat("Verify calculation of illegal profit and balance by accounts", resolvePage.getSuggestedDeductionItems(), contains(
                String.format(SUGGESTED_DEDUCTION_PATTERN_ILLEGAL, mtAccount1.account, formatter.format(mtAccount1.balanceUsd), formatter.format(mtAccount1.balanceUsd + withdrawal.amountUsd)), String.format(SUGGESTED_DEDUCTION_PATTERN, mtAccount5.account, formatter.format(mtAccount5.balanceUsd)), String.format(SUGGESTED_DEDUCTION_PATTERN, mtAccount4.account, formatter.format(mtAccount4.balanceUsd)), String.format(SUGGESTED_DEDUCTION_PATTERN, mtAccount3.account, formatter.format(mtAccount3.balanceUsd)), String.format(SUGGESTED_DEDUCTION_PATTERN, mtAccount2.account, formatter.format(mtAccount2.balanceUsd))
        ));
        Function<MtAccountObject, String> calculateDeduction = acc -> String.format("%s USD", formatter.format(acc.balanceUsd * -1));
        assertThat("Verify suggested deductions by accounts", resolvePage.getSuggestedDeductionValues(), contains(calculateDeduction.apply(mtAccount1), calculateDeduction.apply(mtAccount5), calculateDeduction.apply(mtAccount4), calculateDeduction.apply(mtAccount3), "Holding"));
    }

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
        assertThat("Verify account with alert has lightning icon", resolvePage.isAccountWithAlert(mtAccount1.account.toString()), is(true));
        resolvePage.clickAccountInDropdown(mtAccount1.account.toString());
        resolvePage.clickUseAsIllegalProfit();
        assertThat("Verify suggested deduction is 0 when no account is selected", resolvePage.getSuggestedDeductionAmount(), is("0 USD"));
        assertThat("Verify no illegal profit when no account is selected", resolvePage.getIllegalProfitAmount(), is("No illegal profit"));
    }

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
        assertThat("Verify no suggested deduction when potential fraud type", resolvePage.isSuggestedDeductionSectionVisible(), is(false));
    }
}
