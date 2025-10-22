package tests.vindex_backoffice_ui_tests.investigationTool.trading;

import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_account_for_mt.crm_tb_account.CrmTbAccountForMtObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.mt_account.MtAccountObject;
import business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObject;
import business_objects.db.clickhouse.mt_mt5_deals.Mt5DealsObject;
import business_objects.db.clickhouse.mt_mt5_positions.MtMt5PositionsObject;
import helpers.data.ClientHelper;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountDataForUi;
import static business_objects.db.clickhouse.crm_tb_account_for_mt.crm_tb_account.CrmTbAccountForMtObjectFactory.generateAccountForMtByAccount;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObjectFactory.generateMt4TradesCoercedAccountProfitComment;
import static business_objects.db.clickhouse.mt_mt5_deals.Mt5DealsFactory.generateMt5DealsObject;
import static business_objects.db.clickhouse.mt_mt5_positions.MtMt5PositionsObjectFactory.generateMtMt5PositionsObject;
import static helpers.api.AbuseRegistryHelper.addFraudForClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.Currency.EUR;
import static helpers.data.enums.Currency.USD;
import static helpers.data.enums.FraudSource.INSIGHT;
import static helpers.data.enums.FraudSubtype.INTERNAL;
import static helpers.data.enums.FraudType.*;
import static helpers.data.enums.FraudTypeStatus.*;
import static helpers.data.enums.Restriction.*;
import static helpers.database.ArHelper.deleteUserFromAbuseRegistry;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static page_objects.backoffice_pages.investigationTool.RestrictionPage.cleanUserRestriction;
import static utils.Constants.*;
import static utils.Utils.*;

class ManageFraudWithDeductionsFullCalculationTest extends TestBaseWeb {

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
    private static CrmTbAccountForMtObject crmAccountMt1;
    private static MtMt4TradesCoercedObject trade1;
    private static MtMt4TradesCoercedObject trade2;
    private static MtMt4TradesCoercedObject trade3;
    private static MtMt4TradesCoercedObject trade4;
    private static MtMt4TradesCoercedObject trade5;
    private static MtMt4TradesCoercedObject tradeWithdrawal;
    private static Mt5DealsObject deal1;
    private static Mt5DealsObject deal2;

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
        crmAccountMt1 = generateAccountForMtByAccount(account1);


        String comment = "comment";
        trade1 = generateMt4TradesCoercedAccountProfitComment(account1, getRandomRoundedDouble(-9999.99, 99_999.99), comment);
        trade2 = generateMt4TradesCoercedAccountProfitComment(account1, getRandomRoundedDouble(-9999.99, 99_999.99), comment);
        tradeWithdrawal = generateMt4TradesCoercedAccountProfitComment(account1, getRandomRoundedDouble(-9999.99, -1.00), "withdraw");

        deal1 = generateMt5DealsObject(client, 1, 0);
        deal1.setPositionId(trade1.positionId);
        deal2 = generateMt5DealsObject(client, 0, 0);
        deal2.setPositionId(trade2.positionId);

        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        insertObjectsToDb(CRM_TB_ACCOUNT_TABLE_NAME, List.of(account1));
        insertObjectsToDb(MT_ACCOUNT_TABLE_NAME, List.of(mtAccount1));
        insertObjectsToDb(CRM_TB_ACCOUNT_FOR_MT_TABLE_NAME, List.of(crmAccountMt1));
        insertObjectsToDb(MT4_TRADES_COERCED_TABLE_NAME, List.of(trade1, trade2, tradeWithdrawal));
        insertObjectsToDb(MT5_DEALS_TABLE_NAME, List.of(deal1, deal2));
        MtMt5PositionsObject position = generateMtMt5PositionsObject(client);
        position.setAccount(mtAccount1.account);
        position.setServerId(mtAccount1.sourceIdSt);
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
    @AllureId("")
    @DisplayName("Verify calculation of deductions in fraud management")
    void deductionsIllegalProfitCalculationTest() throws Exception {
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
        assertThat("Verify total illegal profit amount", resolvePage.getIllegalProfitAmount(), is(String.format("%s USD illegal profit", formatter.format((trade1.profit + tradeWithdrawal.profit) - tradeWithdrawal.profit))));

        List<String> deductionItems = resolvePage.getSuggestedDeductionItems();
        assertThat("Verify amount of deductions", deductionItems.size(), is(1));
        assertThat("Verify 1st deduction illegal profit and balance", deductionItems.stream().filter(u -> u.contains(mtAccount1.account.toString())).toList().getFirst(), is(String.format(SUGGESTED_DEDUCTION_PATTERN_ILLEGAL, mtAccount1.account, formatter.format(trade1.profit + tradeWithdrawal.profit), formatter.format((trade1.profit + tradeWithdrawal.profit) - tradeWithdrawal.profit))));

    }
}