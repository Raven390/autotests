package tests.vindex_backoffice_ui_tests.abuseRegistry.deduction;

import business_objects.db.abuse_registry_db.AbuserDeduction;
import business_objects.db.abuse_registry_db.AbuserHistory;
import business_objects.db.clickhouse.account_ib_relation.AccountIbRelationObject;
import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalObject;
import business_objects.db.clickhouse.mt_account.MtAccountObject;
import business_objects.db.clickhouse.mt_mt5_positions.MtMt5PositionsObject;
import business_objects.db.clickhouse.s3___dim_client.S3DimClientObject;
import helpers.data.ClientHelper;
import helpers.database.DbName;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

import java.util.Currency;
import java.util.List;
import java.util.logging.Logger;

import static business_objects.db.abuse_registry_db.AbuserDeductionFactory.generateAbuserDeductionByAccount;
import static business_objects.db.clickhouse.account_ib_relation.AccountIbRelationFactory.generateAccountIbRelationObjectByClient;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateAdditionalCrmTbAccountData;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountDataForUi;
import static business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositObjectFactory.generateDepositByClient;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalObjectFactory.generateCrmTbWithdrawalObjectByClient;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static business_objects.db.clickhouse.mt_mt5_positions.MtMt5PositionsObjectFactory.generateMtMt5PositionsObject;
import static business_objects.db.clickhouse.s3___dim_client.S3DimClientFactory.generateS3DimClientObject;
import static helpers.api.AbuseRegistryHelper.addFraudForClient;
import static helpers.data.ClientFactory.*;
import static helpers.data.enums.FraudSubtype.*;
import static helpers.data.enums.FraudType.*;
import static helpers.data.enums.FraudTypeStatus.CONFIRMED;
import static helpers.data.enums.deduction.DeductionStatusApproval.APPROVED;
import static helpers.data.enums.deduction.DeductionStatusApproval.AWAITING_APPROVAL;
import static helpers.data.enums.deduction.DeductionStatusDeduction.TO_BE_DEDUCTED;
import static helpers.data.enums.deduction.DeductionStatusEmail.NOT_SENT;
import static helpers.database.ArHelper.deleteUserFromAbuseRegistry;
import static helpers.database.BoHelper.closeAlert;
import static helpers.database.CleanTableHelper.cleanCrmUserTableByClient;
import static helpers.database.DbHelper.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.Constants.*;
import static utils.Utils.*;

@Feature("BMS-1553 Manage single deduction")
public class ManageSingleDeductionTest extends TestBaseWeb {

    static CrmTbUserObject crmTbUser;
    static ClientHelper client;
    static CrmTbAccountObject account;
    static CrmTbAccountObject account2;
    static AccountIbRelationObject ibRelation;
    static S3DimClientObject s3Dim;
    static CrmTbDepositObject deposit;
    static CrmTbWithdrawalObject withdrawal;
    static MtMt5PositionsObject position1;
    static MtMt5PositionsObject position2;
    static MtAccountObject mtAccount;
    static MtAccountObject mtAccount2;
    static AbuserDeduction deduction;

    @AfterEach
    void teardown() throws Exception {
        deleteUserFromAbuseRegistry(client.getUcid());
        deleteEntryFromDb(CRM_TB_USER_EXTENDS_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        try {
            deleteEntryFromDb(CRM_TB_ACCOUNT_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        } catch (Exception e) {
            Logger.getLogger(ManageSingleDeductionTest.class.getName()).info("Account deletion failed");
        }
        deleteEntryFromDb(MT_ACCOUNT_TABLE_NAME, String.format("account = '%s'", account.account));
        deleteEntryFromDb(MT_ACCOUNT_TABLE_NAME, String.format("account = '%s'", account2.account));
        deleteEntryFromDb(ACCOUNT_IB_RELATION_TABLE_NAME, String.format("ucid = '%s'", ibRelation.getUcid()));
        deleteEntryFromDb(S3_DIM_CLIENT, String.format("ucid = '%s'", s3Dim.getUcid()));
        deleteEntryFromDb(CRM_DEPOSIT_TABLE_NAME, String.format("ucid = '%s'", deposit.getUcid()));
        deleteEntryFromDb(CLICKHOUSE_CRM_TB_WITHDRAWAL, String.format("ucid = '%s'", withdrawal.getUcid()));
        deleteEntryFromDb(MT5_POSITIONS_TABLE_NAME, String.format("ucid = '%s'", position1.getUcid()));
        deleteEntryFromDb(MT5_POSITIONS_TABLE_NAME, String.format("ucid = '%s'", position2.getUcid()));
        deleteEntryFromDb(DbName.POSTGRES, AR_ABUSER_DEDUCTION_TABLE_NAME, String.format("ucid = '%s'", deduction.getUcid()));
        closeAlert(crmTbUser.ucid);
        cleanCrmUserTableByClient(crmTbUser.ucid);
    }

    @Test
    @DisplayName("user can edit and save deduction")
    @AllureId("1530")
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
        account2 = generateAdditionalCrmTbAccountData(client);
        account.currency = Currency.getInstance("EUR").getCurrencyCode();
        account2.currency = Currency.getInstance("EUR").getCurrencyCode();
        mtAccount = generateMtAccountByCrmTbAccount(account);
        mtAccount2 = generateMtAccountByCrmTbAccount(account2);
        insertObjectsToDb(CRM_TB_ACCOUNT_TABLE_NAME, List.of(account, account2));
        insertObjectsToDb(MT_ACCOUNT_TABLE_NAME, List.of(mtAccount, mtAccount2));
        Thread.sleep(1000);

        //add fraud for the client
        addFraudForClient(client, GAP_TRADING, FIRST_TIME, CONFIRMED, List.of("EURUSD", "GBPUSD"));

        //generate deduction
        List<AbuserHistory> abuserHistory = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_HISTORY_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()), AbuserHistory.class);
        deduction = generateAbuserDeductionByAccount(account, abuserHistory.getLast().getId());
        deduction.setStatusDeduction(TO_BE_DEDUCTED.getDisplayName());
        deduction.setStatusApproval(AWAITING_APPROVAL.getDisplayName());
        deduction.setStatusEmail(NOT_SENT.getDisplayName());
        deduction.setIllegalProfitUsd(-12.00);//set gap_illegal_profit_total (illegal profit less than) gap_pnl_total
        deduction.setIllegalProfit(deduction.getIllegalProfitUsd());
        insertObjectToDb(DbName.POSTGRES, AR_ABUSER_DEDUCTION_TABLE_NAME, deduction);

        //set IB relation
        ibRelation = generateAccountIbRelationObjectByClient(client);
        insertObjectToDb(ACCOUNT_IB_RELATION_TABLE_NAME, ibRelation);

        //set Sale relation
        s3Dim = generateS3DimClientObject(client, ibRelation.getSalesId());
        insertObjectToDb(S3_DIM_CLIENT, s3Dim);

        //set deposit
        deposit = generateDepositByClient(client);
        insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, deposit);

        //set withdrawal
        withdrawal = generateCrmTbWithdrawalObjectByClient(client);
        insertObjectToDb(CLICKHOUSE_CRM_TB_WITHDRAWAL, withdrawal);

        //set MT5 positions gap_pnl_total = (sum(trading_pnl_usd) = sum(profit_usd + storage_usd)) and must be grater than gap_illegal_profit_total: sum of illegal_profit_usd
        position1 = generateMtMt5PositionsObject(client);//
        position1.setProfitUsd(1100.00);
        position1.setStorageUsd(1000.00);
        position1.setAccount(account.account);
        position1.setServerId(account.serverIdSt);
        position1.setServerName(account.serverName);
        position2 = generateMtMt5PositionsObject(client);
        position2.setAccount(account2.account);
        position2.setServerId(account2.serverIdSt);
        position2.setServerName(account2.serverName);
        position2.setProfitUsd(1200.00);
        position2.setStorageUsd(1000.00);
        insertObjectsToDb(MT5_POSITIONS_TABLE_NAME, List.of(position1, position1));
        page.waitForTimeout(1000);

        System.out.println("client ucid is " + client.getUcid());

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        deductionPage.navigateDeduction();
        deductionPage.hoverOverDeductionTableRow(client.getUserId());
        deductionPage.openEditDrawer();
        deductionPage.isSaveIsInactive();
        double newIllegalProfit = getRandomRoundedDouble(0.01, 999.99);
        double newIllegalProfitUSD = convertToUsd(newIllegalProfit, account.currency);
        double newSuggestedDeduction = getRandomRoundedDouble(0.01, 999.99);
        double newSuggestedDeductionUSD = convertToUsd(newSuggestedDeduction, account.currency);
        deductionPage.fillIllegalProfitInput(Double.toString(newIllegalProfit));
        deductionPage.fillSuggestedDeductionInput(Double.toString(newSuggestedDeduction));
        deductionPage.isSaveIsActive();
        deductionPage.saveDeduction();
        Allure.step("get clients deduction from DB");
        AbuserDeduction changedDeduction = (getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_DEDUCTION_TABLE_NAME, "ucid = '" + client.getUcid() + "'", AbuserDeduction.class)).getFirst();
        Allure.step("check that illegal profit was changed");
        assertEquals(roundDouble(changedDeduction.getIllegalProfit(), 2), roundDouble(newIllegalProfit, 2));
        assertEquals(roundDouble(changedDeduction.getIllegalProfitUsd(), 2), roundDouble(newIllegalProfitUSD, 2));
        Allure.step("check that suggested deduction was changed");
        assertEquals(roundDouble(changedDeduction.getSuggestedDeduction(), 2), roundDouble(newSuggestedDeduction, 2));
        assertEquals(roundDouble(changedDeduction.getSuggestedDeductionUsd(), 2), roundDouble(newSuggestedDeductionUSD, 2));
    }

    @Test
    @DisplayName("user can finalize deduction")
    @AllureId("1531")
    void userCanDeductDeduction() throws Exception {

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
        account2 = generateAdditionalCrmTbAccountData(client);
        account.currency = Currency.getInstance("EUR").getCurrencyCode();
        account2.currency = Currency.getInstance("EUR").getCurrencyCode();
        mtAccount = generateMtAccountByCrmTbAccount(account);
        mtAccount2 = generateMtAccountByCrmTbAccount(account2);
        insertObjectsToDb(CRM_TB_ACCOUNT_TABLE_NAME, List.of(account, account2));
        insertObjectsToDb(MT_ACCOUNT_TABLE_NAME, List.of(mtAccount, mtAccount2));
        Thread.sleep(1000);

        //add fraud for the client
        addFraudForClient(client, GAP_TRADING, FIRST_TIME, CONFIRMED, List.of("EURUSD", "GBPUSD"));

        //generate deduction
        List<AbuserHistory> abuserHistory = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_HISTORY_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()), AbuserHistory.class);
        deduction = generateAbuserDeductionByAccount(account, abuserHistory.getLast().getId());
        deduction.setStatusDeduction(TO_BE_DEDUCTED.getDisplayName());
        deduction.setStatusApproval(AWAITING_APPROVAL.getDisplayName());
        deduction.setStatusEmail(NOT_SENT.getDisplayName());
        deduction.setIllegalProfitUsd(-12.00);//set gap_illegal_profit_total (illegal profit less than) gap_pnl_total
        deduction.setIllegalProfit(deduction.getIllegalProfitUsd());
        insertObjectToDb(DbName.POSTGRES, AR_ABUSER_DEDUCTION_TABLE_NAME, deduction);

        //set IB relation
        ibRelation = generateAccountIbRelationObjectByClient(client);
        insertObjectToDb(ACCOUNT_IB_RELATION_TABLE_NAME, ibRelation);

        //set Sale relation
        s3Dim = generateS3DimClientObject(client, ibRelation.getSalesId());
        insertObjectToDb(S3_DIM_CLIENT, s3Dim);

        //set deposit
        deposit = generateDepositByClient(client);
        insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, deposit);

        //set withdrawal
        withdrawal = generateCrmTbWithdrawalObjectByClient(client);
        insertObjectToDb(CLICKHOUSE_CRM_TB_WITHDRAWAL, withdrawal);

        //set MT5 positions gap_pnl_total = (sum(trading_pnl_usd) = sum(profit_usd + storage_usd)) and must be grater than gap_illegal_profit_total: sum of illegal_profit_usd
        position1 = generateMtMt5PositionsObject(client);//
        position1.setProfitUsd(1100.00);
        position1.setStorageUsd(1000.00);
        position1.setAccount(account.account);
        position1.setServerId(account.serverIdSt);
        position1.setServerName(account.serverName);
        position2 = generateMtMt5PositionsObject(client);
        position2.setAccount(account2.account);
        position2.setServerId(account2.serverIdSt);
        position2.setServerName(account2.serverName);
        position2.setProfitUsd(1200.00);
        position2.setStorageUsd(1000.00);
        insertObjectsToDb(MT5_POSITIONS_TABLE_NAME, List.of(position1, position1));
        page.waitForTimeout(1000);

        System.out.println("client ucid is " + client.getUcid());

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        deductionPage.navigateDeduction();
        deductionPage.hoverOverDeductionTableRow(client.getUserId());
        deductionPage.openEditDrawer();
        deductionPage.isDeductIsInactive();
        double deductionValue = getRandomRoundedDouble(0.01, 999.99);
        double deductionValueUSD = convertToUsd(deductionValue, account.currency);
        deductionPage.fillDeductionInput(Double.toString(deductionValue));
        deductionPage.isDeductIsActive();
        deductionPage.finishDeduction();
        Allure.step("get clients deduction from DB");
        AbuserDeduction deductedDeduction = (getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_DEDUCTION_TABLE_NAME, "ucid = '" + client.getUcid() + "'", AbuserDeduction.class)).getFirst();
        Allure.step("check that Deduction field was changed");
        assertEquals(roundDouble(deductionValue, 2), roundDouble(deductedDeduction.getApprovedDeduction(), 2));
        assertEquals(roundDouble(deductionValueUSD, 2), roundDouble(deductedDeduction.getApprovedDeductionUsd(), 2));
        assertEquals(APPROVED.getDisplayName(), deductedDeduction.getStatusApproval());
    }
}