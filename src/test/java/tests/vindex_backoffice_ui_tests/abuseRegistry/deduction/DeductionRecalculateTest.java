package tests.vindex_backoffice_ui_tests.abuseRegistry.deduction;

import business_objects.db.abuse_registry_db.AbuserDeduction;
import business_objects.db.abuse_registry_db.AbuserHistory;
import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositEntity;
import business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositEntityFactory;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalEntity;
import business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalEntityFactory;
import business_objects.db.clickhouse.mt_account.MtAccountObject;
import business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObject;
import business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObjectFactory;
import helpers.data.ClientHelper;
import helpers.database.DbName;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tests.TestBaseWeb;

import java.util.Currency;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static business_objects.db.abuse_registry_db.AbuserDeductionFactory.generateAbuserDeductionByAccount;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateAdditionalCrmTbAccountData;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountDataForUi;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static helpers.api.AbuseRegistryHelper.addFraudForClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.FraudSubtype.FIRST_TIME;
import static helpers.data.enums.FraudType.GAP_TRADING;
import static helpers.data.enums.FraudTypeStatus.CONFIRMED;
import static helpers.data.enums.deduction.DeductionStatusApproval.AWAITING_APPROVAL;
import static helpers.data.enums.deduction.DeductionStatusDeduction.TO_BE_DEDUCTED;
import static helpers.data.enums.deduction.DeductionStatusEmail.NOT_SENT;
import static helpers.database.ArHelper.deleteUserFromAbuseRegistry;
import static helpers.database.BoHelper.closeAlert;
import static helpers.database.CleanTableHelper.cleanCrmUserTableByClient;
import static helpers.database.DbHelper.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static utils.Constants.*;
import static utils.Utils.*;

@Feature("BMS-2440 Recalculate respective deduction records when 'Get file' button is pressed in Deduction tab")
public class DeductionRecalculateTest extends TestBaseWeb {

    static CrmTbUserObject crmTbUser;
    static ClientHelper client;
    static CrmTbAccountObject account;
    static CrmTbAccountObject account2;
    static CrmTbDepositEntity deposit;
    static CrmTbWithdrawalEntity withdrawal;
    static MtAccountObject mtAccount;
    static MtAccountObject mtAccount2;
    static MtMt4TradesCoercedObject mtMt4TradesCoercedObject;
    static MtMt4TradesCoercedObject mtMt4TradesCoercedObject2;
    static AbuserDeduction deduction;

    @AfterEach
    void teardown() throws Exception {
        deleteUserFromAbuseRegistry(client.getUcid());
        deleteEntryFromDb(CRM_TB_USER_EXTENDS_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        try {
            deleteEntryFromDb(CRM_TB_ACCOUNT_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        } catch (Exception e) {
            writeLog("Account deletion failed");
        }
        deleteEntryFromDb(MT_ACCOUNT_TABLE_NAME, String.format("account = '%s'", account.account));
        deleteEntryFromDb(MT_ACCOUNT_TABLE_NAME, String.format("account = '%s'", account2.account));
        deleteEntryFromDb(CRM_DEPOSIT_TABLE_NAME, String.format("ucid = '%s'", deposit.getUcid()));
        deleteEntryFromDb(CLICKHOUSE_CRM_TB_WITHDRAWAL, String.format("ucid = '%s'", withdrawal.getUcid()));
        deleteEntryFromDb(MT4_TRADES_COERCED_TABLE_NAME, String.format("ucid = '%s'", mtMt4TradesCoercedObject.ucid));
        deleteEntryFromDb(MT4_TRADES_COERCED_TABLE_NAME, String.format("ucid = '%s'", mtMt4TradesCoercedObject2.ucid));
        deleteEntryFromDb(DbName.POSTGRES, AR_ABUSER_DEDUCTION_TABLE_NAME, String.format("ucid = '%s'", deduction.getUcid()));
        closeAlert(crmTbUser.ucid);
        cleanCrmUserTableByClient(crmTbUser.ucid);
    }

    @Test
    @DisplayName("Get file deduction recalculation")
    @AllureId("1711")
    void userCanSaveDeduction() throws Exception {

        //Create a record about a client in crm_tb_user
        client = getRandomVantageClientAllFields();
        crmTbUser = generateUserByClient(client);
        client.setFirstName(faker.name().firstName());
        client.setLastName(faker.name().lastName());
        crmTbUser.firstName = client.getFirstName();
        crmTbUser.lastName = client.getLastName();
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);

        //Create a record about a client's account
        account = generateCrmTbAccountDataForUi(client);
        account.balance = 200_000.00;
        account.balanceUsd = 220_000.00;
        account2 = generateAdditionalCrmTbAccountData(client);
        account2.balance = 200_000.00;
        account2.balanceUsd = 220_000.00;
        account.currency = Currency.getInstance("EUR").getCurrencyCode();
        account2.currency = Currency.getInstance("EUR").getCurrencyCode();
        mtAccount = generateMtAccountByCrmTbAccount(account);
        mtAccount2 = generateMtAccountByCrmTbAccount(account2);
        mtMt4TradesCoercedObject = MtMt4TradesCoercedObjectFactory.generateMt4TradesCoercedRandomized(client);
        mtMt4TradesCoercedObject.ticketType = "Balance";
        mtMt4TradesCoercedObject.reasonName = "Client";
        mtMt4TradesCoercedObject.profit = 1200.50;
        mtMt4TradesCoercedObject.storage = 1400.50;
        mtMt4TradesCoercedObject.commission = 1600.00;
        mtMt4TradesCoercedObject.profitUsd = 1200.50;
        mtMt4TradesCoercedObject.storageUsd = 1400.50;
        mtMt4TradesCoercedObject.commissionUsd = 1600.00;
        mtMt4TradesCoercedObject2 = MtMt4TradesCoercedObjectFactory.generateMt4TradesCoercedRandomized(client);
        mtMt4TradesCoercedObject2.ticketType = "Balance";
        mtMt4TradesCoercedObject2.reasonName = "Client";
        mtMt4TradesCoercedObject2.profit = 2200.50;
        mtMt4TradesCoercedObject2.storage = 2400.50;
        mtMt4TradesCoercedObject2.commission = 2600.00;
        mtMt4TradesCoercedObject2.profitUsd = 2200.50;
        mtMt4TradesCoercedObject2.storageUsd = 2400.50;
        mtMt4TradesCoercedObject2.commissionUsd = 2600.00;
        mtMt4TradesCoercedObject2.account = Long.valueOf(client.getTradingAccount2());
        insertCrmAccountsToDb(account, account2);
        insertObjectsToDb(MT_ACCOUNT_TABLE_NAME, List.of(mtAccount, mtAccount2));
        insertObjectsToDb(MT4_TRADES_COERCED_TABLE_NAME, List.of(mtMt4TradesCoercedObject, mtMt4TradesCoercedObject2));
        Awaitility.await().pollDelay(1, TimeUnit.SECONDS).untilAsserted(() -> assertTrue(true));
        //add fraud for the client
        addFraudForClient(client, GAP_TRADING, FIRST_TIME, CONFIRMED, List.of("EURUSD", "GBPUSD"));

        //generate deduction
        List<AbuserHistory> abuserHistory = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_HISTORY_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()), AbuserHistory.class);
        deduction = generateAbuserDeductionByAccount(account, abuserHistory.getLast().getId());
        deduction.setStatusDeduction(TO_BE_DEDUCTED.getDisplayName());
        deduction.setStatusApproval(AWAITING_APPROVAL.getDisplayName());
        deduction.setStatusEmail(NOT_SENT.getDisplayName());
        deduction.setIllegalProfitUsd(-12.00);
        deduction.setIllegalProfit(deduction.getIllegalProfitUsd());
        insertObjectToDb(DbName.POSTGRES, AR_ABUSER_DEDUCTION_TABLE_NAME, deduction);
        var deduction = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_DEDUCTION_TABLE_NAME, "ucid='" + client.getUcid() + "'", AbuserDeduction.class).getFirst();

        //set deposit
        deposit = CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(client);
        insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, deposit);

        //set withdrawal
        withdrawal = CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient(client);
        insertObjectToDb(CLICKHOUSE_CRM_TB_WITHDRAWAL, withdrawal);

        page.waitForTimeout(1000);

        writeLog("client ucid is " + client.getUcid());

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        deductionPage.navigateDeduction();
        deductionPage.clickOnDeductionCheckBox(deduction.getId());
        page.waitForTimeout(500);
        assertTrue(deductionPage.getFileButtonIsVisible());
        deductionPage.clickOnGetFileButton();
        deductionPage.checkOneDeductionRecalculated();
        page.waitForTimeout(500);
        var deductionAfterRecalculation = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_DEDUCTION_TABLE_NAME, "id = " + deduction.getId() + " AND ucid='" + client.getUcid() + "'", AbuserDeduction.class).getFirst();
        assertEquals(mtMt4TradesCoercedObject.profit + mtMt4TradesCoercedObject.storage + mtMt4TradesCoercedObject.commission, deductionAfterRecalculation.getIllegalProfit());
        assertEquals(mtMt4TradesCoercedObject.profit + mtMt4TradesCoercedObject.storage + mtMt4TradesCoercedObject.commission, deductionAfterRecalculation.getSuggestedDeduction());
    }
}