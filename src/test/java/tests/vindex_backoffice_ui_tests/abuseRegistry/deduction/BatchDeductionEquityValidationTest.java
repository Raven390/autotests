package tests.vindex_backoffice_ui_tests.abuseRegistry.deduction;

import business_objects.db.abuse_registry_db.AbuserDeduction;
import business_objects.db.abuse_registry_db.AbuserHistory;
import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.mt_account.MtAccountObject;
import business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObject;
import helpers.data.ClientHelper;
import helpers.data.enums.deduction.*;
import helpers.database.DbName;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

import java.util.List;

import static business_objects.db.abuse_registry_db.AbuserDeductionFactory.generateAbuserDeductionByAccount;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateAdditionalCrmTbAccountDataForUi;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountDataForUi;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObjectFactory.generateMt4TradesCoercedAccountProfitComment;
import static helpers.api.AbuseRegistryHelper.addFraudForClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.Currency.EUR;
import static helpers.data.enums.FraudSubtype.INTERNAL;
import static helpers.data.enums.FraudType.CPA_ABUSE;
import static helpers.data.enums.FraudType.HEDGING;
import static helpers.data.enums.FraudTypeStatus.CONFIRMED;
import static helpers.database.ArHelper.deleteUserFromAbuseRegistry;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Utils.insertCrmAccountsToDb;


@Tag(TEAM_BACKOFFICE)
@Tag(LAYER_WEB)
@Tag(ABUSE_REGISTRY)
@Feature("BMS-2712 Change validation for insufficient balance in batch deduction to checking for equity > 0")
class BatchDeductionEquityValidationTest extends TestBaseWeb {

    private static final ClientHelper client = getRandomVantageClientAllFields();
    private static final CrmTbUserObject crmTbUser = generateUserByClient(client);
    private static final CrmTbAccountObject account = generateCrmTbAccountDataForUi(client);
    private static final CrmTbAccountObject account2 = generateAdditionalCrmTbAccountDataForUi(client);
    private static AbuserDeduction deduction;
    private static AbuserDeduction deduction2;


    @BeforeAll
    static void setup() throws Exception {
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        account.currency = EUR.getCode();
        account2.currency = EUR.getCode();
        MtAccountObject mtAccount = generateMtAccountByCrmTbAccount(account);
        MtAccountObject mtAccount2 = generateMtAccountByCrmTbAccount(account2);
        mtAccount.balance = 1001d;
        mtAccount2.balance = 1001d;
        mtAccount.equity = 0d;
        mtAccount2.equity = 1d;
        insertCrmAccountsToDb(account, account2);
        insertObjectsToDb(MT_ACCOUNT_TABLE_NAME, List.of(mtAccount, mtAccount2));
        Thread.sleep(2000);
        addFraudForClient(client, HEDGING, INTERNAL, CONFIRMED, null);
        addFraudForClient(client, CPA_ABUSE, null, CONFIRMED, null);
        List<AbuserHistory> abuserHistory = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_HISTORY_TABLE_NAME, String.format("ucid = '%s' and source = 'FRAUD_TYPE_STATUS'", client.getUcid()), AbuserHistory.class);
        deduction = generateAbuserDeductionByAccount(account, abuserHistory.getFirst().getId());
        deduction.setIllegalProfit(1000d);
        deduction.setIllegalProfitUsd(1100d);
        deduction.setSuggestedDeduction(1000d);
        deduction.setSuggestedDeductionUsd(1100d);
        deduction.setApprovedDeduction(1000d);
        deduction.setApprovedDeductionUsd(1100d);
        deduction.setBalanceAtResolution(1001d);
        deduction.setBalanceAtResolution(1101d);
        deduction.setStatusOpenPositions(DeductionStatusOpenPositions.NOT_HOLDING.getDisplayName());
        deduction.setStatusEmail(DeductionStatusEmail.SENT.getDisplayName());
        deduction.setStatusDeduction(DeductionStatusDeduction.TO_BE_DEDUCTED.getDisplayName());
        deduction.setStatusApproval(DeductionStatusApproval.AWAITING_APPROVAL.getDisplayName());
        deduction.setActualDeduction(null);
        deduction.setActualDeductionUsd(null);
        deduction.setDeductionDate(null);
        deduction.setCommentDeduction(null);
        deduction.setTypeAccount(DeductionTypeAccount.ILLEGAL_PROFIT.getDisplayName());
        deduction.setDeductionType(DeductionType.FULL_DEDUCTION.getDisplayName());
        deduction.setDeleted(false);
        deduction2 = generateAbuserDeductionByAccount(account2, abuserHistory.getLast().getId());
        deduction2.setIllegalProfit(1000d);
        deduction2.setIllegalProfitUsd(1100d);
        deduction2.setSuggestedDeduction(1000d);
        deduction2.setSuggestedDeductionUsd(1100d);
        deduction2.setApprovedDeduction(1000d);
        deduction2.setApprovedDeductionUsd(1100d);
        deduction2.setBalanceAtResolution(1001d);
        deduction2.setBalanceAtResolution(1101d);
        deduction2.setStatusOpenPositions(DeductionStatusOpenPositions.NOT_HOLDING.getDisplayName());
        deduction2.setStatusEmail(DeductionStatusEmail.SENT.getDisplayName());
        deduction2.setStatusDeduction(DeductionStatusDeduction.TO_BE_DEDUCTED.getDisplayName());
        deduction2.setStatusApproval(DeductionStatusApproval.AWAITING_APPROVAL.getDisplayName());
        deduction2.setActualDeduction(null);
        deduction2.setActualDeductionUsd(null);
        deduction2.setDeductionDate(null);
        deduction2.setCommentDeduction(null);
        deduction2.setTypeAccount(DeductionTypeAccount.ILLEGAL_PROFIT.getDisplayName());
        deduction2.setDeductionType(DeductionType.FULL_DEDUCTION.getDisplayName());
        deduction2.setDeleted(false);
        insertObjectsToDb(DbName.POSTGRES, AR_ABUSER_DEDUCTION_TABLE_NAME, List.of(deduction, deduction2));
        MtMt4TradesCoercedObject trade = generateMt4TradesCoercedAccountProfitComment(account, 1001d, "comment");
        MtMt4TradesCoercedObject trade2 = generateMt4TradesCoercedAccountProfitComment(account2, 1001d, "comment");
        insertObjectsToDb(MT4_TRADES_COERCED_TABLE_NAME, List.of(trade, trade2));
    }

    @AfterAll
    static void teardown() throws Exception {
        deleteEntryFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        deleteUserFromAbuseRegistry(client.getUcid());
    }

    @Test
    @AllureId("1938")
    @DisplayName("Verify equity validation in batch deduction")
    void equityValidationInBatchDeductionTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(client.getUcid());
        alertsPage.waitForPageToLoad();
        deductionPage.clickAbuseRegistryButton();
        deductionPage.clickBatchDeductionButton();
        deductionPage.fillBatchDeductionAccountsList(String.format("%s %s%n%s %s", account.serverName, account.account, account2.serverName, account2.account));
        deductionPage.clickBatchDeductionApproveAllButton();
        deductionPage.clickBatchDeductionConfirmApproveButton();
        assertThat("Verify validation for only 1 account was triggered", deductionPage.getBatchDeductionValidationItems(), hasSize(1));
        assertThat("Verify triggered validation text", deductionPage.getBatchDeductionValidationItems().getFirst(), is(String.format("%s %s%nNo equity%nEquity is 0 or negative. Deduction is not possible", account.serverName, account.account)));
    }
}