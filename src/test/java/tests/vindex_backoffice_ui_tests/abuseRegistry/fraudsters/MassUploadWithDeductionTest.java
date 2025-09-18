package tests.vindex_backoffice_ui_tests.abuseRegistry.fraudsters;

import business_objects.db.abuse_registry_db.AbuserDeduction;
import business_objects.db.abuse_registry_db.AbuserHistory;
import business_objects.db.abuse_registry_db.PendingProcessing;
import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.mt_account.MtAccountObject;
import business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObject;
import business_objects.db.clickhouse.mt_mt5_positions.MtMt5PositionsObject;
import helpers.data.ClientHelper;
import helpers.data.enums.*;
import helpers.data.enums.deduction.DeductionType;
import helpers.database.DbName;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

import java.util.List;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountDataForUi;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObjectFactory.generateMt4TradesCoercedAccountProfitComment;
import static business_objects.db.clickhouse.mt_mt5_positions.MtMt5PositionsObjectFactory.generateMtMt5PositionsObject;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.Currency.USD;
import static org.hamcrest.Matchers.*;
import static helpers.data.enums.deduction.DeductionStatusApproval.*;
import static helpers.data.enums.deduction.DeductionStatusDeduction.NO_DEDUCTION;
import static helpers.data.enums.deduction.DeductionStatusDeduction.TO_BE_DEDUCTED;
import static helpers.data.enums.deduction.DeductionStatusEmail.NOT_SENT;
import static helpers.data.enums.deduction.DeductionStatusOpenPositions.*;
import static helpers.database.ArHelper.deleteUserFromAbuseRegistry;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static utils.Constants.*;
import static utils.Utils.getCurrentTimestampSeconds;
import static utils.Utils.getRandomIntPositive;

@Tag(TEAM_BACKOFFICE)
@Tag(LAYER_WEB)
@Tag(ABUSE_REGISTRY)
@Feature("BMS-2034 Generate deduction record")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MassUploadWithDeductionTest extends TestBaseWeb {

    private static final ClientHelper client2 = getRandomVantageClientAllFields();
    private static final ClientHelper client1 = getRandomVantageClientAllFields();
    private static final CrmTbUserObject crmTbUser = generateUserByClient(client1);
    private static final CrmTbAccountObject account1 = generateCrmTbAccountDataForUi(client1);
    private static final CrmTbAccountObject account2 = generateCrmTbAccountDataForUi(client2);
    private static MtAccountObject mtAccount1;
    private static MtMt4TradesCoercedObject trade1;
    private static MtMt4TradesCoercedObject tradeWithdrawal;

    @BeforeAll
    static void setup() {
        account1.currency = USD.getCode();

        mtAccount1 = generateMtAccountByCrmTbAccount(account1);


        String comment = "comment";
        trade1 = generateMt4TradesCoercedAccountProfitComment(account1, 500.12 + 10_000d, comment);
        tradeWithdrawal = generateMt4TradesCoercedAccountProfitComment(account1, -10_000d, "withdraw");
        account2.account = getRandomIntPositive();
        MtMt4TradesCoercedObject trade2 = generateMt4TradesCoercedAccountProfitComment(account2, 500.12 + 10_000d, comment);
        CrmTbUserObject crmClient2 = generateUserByClient(client2);
        CrmTbAccountObject account3 = generateCrmTbAccountDataForUi(client2);
        insertObjectsToDb(CRM_USER_TABLE_NAME, List.of(crmTbUser, crmClient2));
        insertObjectsToDb(CRM_TB_ACCOUNT_TABLE_NAME, List.of(account1, account2, account3));
        insertObjectsToDb(MT_ACCOUNT_TABLE_NAME, List.of(mtAccount1));
        insertObjectsToDb(MT4_TRADES_COERCED_TABLE_NAME, List.of(trade1, tradeWithdrawal, trade2));
        MtMt5PositionsObject position1 = generateMtMt5PositionsObject(client1);
        MtMt5PositionsObject position2 = generateMtMt5PositionsObject(client2);
        insertObjectsToDb(MT5_POSITIONS_TABLE_NAME, List.of(position1, position2));
    }

    @AfterAll
    static void teardown() throws Exception {
        deleteUserFromAbuseRegistry(client1.getUcid());
        deleteUserFromAbuseRegistry(client2.getUcid());
        deleteEntryFromDb(CLICKHOUSE_CRM_TB_WITHDRAWAL, String.format("ucid = '%s'", client1.getUcid()));
        deleteEntryFromDb(MT5_POSITIONS_TABLE_NAME, String.format("ucid = '%s'", client1.getUcid()));
    }

    @Test
    @AllureId("1555")
    @DisplayName("Batch upload PARTIAL_DEDUCTION test")
    void partialTest() throws Exception {

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        fraudstersPage.navigateAbuseRegistryFraudsters();
        fraudstersPage.openUploadDrawer();
        fraudstersPage.selectBrandToUpload(Brand.VANTAGE.getDisplayName());
        fraudstersPage.typeClientsID(client1.getUserId().toString(), client2.getUserId().toString());
        fraudstersPage.clickAddFraudButton();
        FraudType fraudTypeOld = FraudType.PRICING_ERROR;
        fraudstersPage.addSelectedFraudAdd(fraudTypeOld.getName(), "Confirmed");
        String commentary = "test" + getCurrentTimestampSeconds();
        fraudstersPage.fillCommentary(commentary);
        fraudstersPage.clickApplyUpload();
        fraudstersPage.verifySuccessMessageUpload();
        fraudstersPage.verifyWarningMessageUpload(2);

        page.waitForTimeout(1000);

        List<PendingProcessing> pendingProcessing = getObjectsFromDB(DbName.POSTGRES, AR_PENDING_PROCESSING_TABLE_NAME, String.format("ucid in ('%s')", client1.getUcid()), PendingProcessing.class);
        assertThat(pendingProcessing.size(), is(1));
        PendingProcessing pending = pendingProcessing.getFirst();
        assertThat(pending.getUcid(), is(client1.getUcid()));
        assertThat(pending.getFraudTypeCode(), is(fraudTypeOld.getCode()));
        assertThat(pendingProcessing.getFirst().getFraudSubtypeCode(), nullValue());

    }

    @Test
    @AllureId("1556")
    @DisplayName("Batch upload more than one acc client create pending_processing and 1 acc client create Holding deduction with FULL_DEDUCTION test")
    void moreThan1accAndFullDeductionTest() throws Exception {

        deleteUserFromAbuseRegistry(client1.getUcid());
        deleteUserFromAbuseRegistry(client2.getUcid());

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        fraudstersPage.navigateAbuseRegistryFraudsters();
        fraudstersPage.openUploadDrawer();
        fraudstersPage.selectBrandToUpload(Brand.VANTAGE.getDisplayName());
        fraudstersPage.typeClientsID(client1.getUserId().toString(), client2.getUserId().toString());
        fraudstersPage.clickAddFraudButton();
        FraudType fraudType = FraudType.LOOPHOLE_ABUSE;
        //Partial Deduction
        fraudstersPage.addSelectedFraudAdd(fraudType.getName(), "Confirmed");
        String commentary = "test" + getCurrentTimestampSeconds();
        fraudstersPage.fillCommentary(commentary);
        fraudstersPage.clickApplyUpload();
        fraudstersPage.verifySuccessMessageUpload(1);
        fraudstersPage.verifyWarningMessageUpload(1);
        page.waitForTimeout(1000);

        List<PendingProcessing> pendingProcessing = getObjectsFromDB(DbName.POSTGRES, AR_PENDING_PROCESSING_TABLE_NAME, String.format("ucid in ('%s')", client1.getUcid()), PendingProcessing.class);
        assertThat(pendingProcessing.size(), is(0));
        List<PendingProcessing> pendingProcessing2 = getObjectsFromDB(DbName.POSTGRES, AR_PENDING_PROCESSING_TABLE_NAME, String.format("ucid in ('%s')", client2.getUcid()), PendingProcessing.class);
        assertThat(pendingProcessing2.size(), is(1));
        PendingProcessing pending = pendingProcessing2.getFirst();
        assertThat(pending.getUcid(), is(client2.getUcid()));
        assertThat(pending.getFraudTypeCode(), is(fraudType.getCode()));
        assertThat(pendingProcessing2.getFirst().getFraudSubtypeCode(), nullValue());

        List<AbuserHistory> abuserHistory = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_HISTORY_TABLE_NAME, String.format("ucid = '%s'", client1.getUcid()), AbuserHistory.class);

        AbuserDeduction expected = new AbuserDeduction(
                client1.getUcid(), abuserHistory.getLast().getId(), account1.account.toString(), account1.serverIdSt, account1.serverName, "USD", "Vantage", HOLDING.getDisplayName(), NOT_SENT.getDisplayName(), TO_BE_DEDUCTED.getDisplayName(), AWAITING_APPROVAL.getDisplayName(), commentary, 10_500.12, 10_500.12, 0.0, 0.0, null, null, null, null, "backoffice-test backoffice-test", "Vindex BO", "ILLEGAL_PROFIT", null, null, 0.0, 0.0, client1.getUserId().toString(), 500.12, 500.12, false, DeductionType.FULL_DEDUCTION.getDisplayName()
        );

        List<AbuserDeduction> deductionList = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_DEDUCTION_TABLE_NAME, String.format("ucid = '%s'", client1.getUcid()), AbuserDeduction.class);
        assertThat(deductionList.size(), is(1));
        AbuserDeduction actualDeduction = deductionList.getFirst();
        assertThat(actualDeduction, is(expected));


        List<AbuserDeduction> deductionList2 = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_DEDUCTION_TABLE_NAME, String.format("ucid = '%s'", client2.getUcid()), AbuserDeduction.class);
        assertThat(deductionList2.size(), is(0));
    }


    @Test
    @AllureId("1557")
    @DisplayName("Batch upload more than one acc client create pending_processing and 1 acc client create deduction with FULL_DEDUCTION test")
    void moreThan1accAndFullDeductionNotHoldingTest() throws Exception {

        deleteUserFromAbuseRegistry(client1.getUcid());
        deleteUserFromAbuseRegistry(client2.getUcid());
        executeQueryToDb(DbName.CLICKHOUSE, String.format("UPDATE %s SET is_deleted = 1 WHERE account = %s", MT5_POSITIONS_TABLE_NAME, mtAccount1.account));


        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        fraudstersPage.navigateAbuseRegistryFraudsters();
        fraudstersPage.openUploadDrawer();
        fraudstersPage.selectBrandToUpload(Brand.VANTAGE.getDisplayName());
        fraudstersPage.typeClientsID(client1.getUserId().toString(), client2.getUserId().toString());
        fraudstersPage.clickAddFraudButton();
        FraudType fraudType = FraudType.LOOPHOLE_ABUSE;
        //Partial Deduction
        fraudstersPage.addSelectedFraudAdd(fraudType.getName(), "Confirmed");
        String commentary = "test" + getCurrentTimestampSeconds();
        fraudstersPage.fillCommentary(commentary);
        fraudstersPage.clickApplyUpload();
        fraudstersPage.verifySuccessMessageUpload(1);
        fraudstersPage.verifyWarningMessageUpload(1);
        page.waitForTimeout(1000);

        List<PendingProcessing> pendingProcessing = getObjectsFromDB(DbName.POSTGRES, AR_PENDING_PROCESSING_TABLE_NAME, String.format("ucid in ('%s')", client1.getUcid()), PendingProcessing.class);
        assertThat(pendingProcessing.size(), is(0));
        List<PendingProcessing> pendingProcessing2 = getObjectsFromDB(DbName.POSTGRES, AR_PENDING_PROCESSING_TABLE_NAME, String.format("ucid in ('%s')", client2.getUcid()), PendingProcessing.class);
        assertThat(pendingProcessing2.size(), is(1));
        PendingProcessing pending = pendingProcessing2.getFirst();
        assertThat(pending.getUcid(), is(client2.getUcid()));
        assertThat(pending.getFraudTypeCode(), is(fraudType.getCode()));
        assertThat(pendingProcessing2.getFirst().getFraudSubtypeCode(), nullValue());

        List<AbuserHistory> abuserHistory = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_HISTORY_TABLE_NAME, String.format("ucid = '%s'", client1.getUcid()), AbuserHistory.class);

        AbuserDeduction expected = new AbuserDeduction(
                client1.getUcid(), abuserHistory.getLast().getId(), account1.account.toString(), account1.serverIdSt, account1.serverName, "USD", "Vantage", NOT_HOLDING.getDisplayName(), NOT_SENT.getDisplayName(), TO_BE_DEDUCTED.getDisplayName(), AWAITING_APPROVAL.getDisplayName(), commentary, 10_500.12, 10_500.12, 500.12, 500.12, null, null, null, null, "backoffice-test backoffice-test", "Vindex BO", "ILLEGAL_PROFIT", null, null, 500.12, 500.12, client1.getUserId().toString(), 500.12, 500.12, false, DeductionType.FULL_DEDUCTION.getDisplayName()
        );

        List<AbuserDeduction> deductionList = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_DEDUCTION_TABLE_NAME, String.format("ucid = '%s'", client1.getUcid()), AbuserDeduction.class);
        assertThat(deductionList.size(), is(1));
        AbuserDeduction actualDeduction = deductionList.getFirst();
        assertThat(actualDeduction, is(expected));


        List<AbuserDeduction> deductionList2 = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_DEDUCTION_TABLE_NAME, String.format("ucid = '%s'", client2.getUcid()), AbuserDeduction.class);
        assertThat(deductionList2.size(), is(0));

        executeQueryToDb(DbName.CLICKHOUSE, String.format("UPDATE %s SET is_deleted = 0 WHERE account = %s", MT5_POSITIONS_TABLE_NAME, mtAccount1.account));
    }

    @Test
    @AllureId("1558")
    @DisplayName("Batch upload more than one acc client create pending_processing and 1 acc client create Holding deduction with NO_DEDUCTION test")
    void moreThan1accAndNoDeductionTest() throws Exception {

        deleteUserFromAbuseRegistry(client1.getUcid());
        deleteUserFromAbuseRegistry(client2.getUcid());

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        fraudstersPage.navigateAbuseRegistryFraudsters();
        fraudstersPage.openUploadDrawer();
        fraudstersPage.selectBrandToUpload(Brand.VANTAGE.getDisplayName());
        fraudstersPage.typeClientsID(client1.getUserId().toString(), client2.getUserId().toString());
        fraudstersPage.clickAddFraudButton();
        FraudType fraudType = FraudType.CHARGEBACK;
        //Partial Deduction
        fraudstersPage.addSelectedFraudAdd(fraudType.getName(), "Confirmed");
        String commentary = "test" + getCurrentTimestampSeconds();
        fraudstersPage.fillCommentary(commentary);
        fraudstersPage.clickApplyUpload();
        fraudstersPage.verifySuccessMessageUpload(1);
        fraudstersPage.verifyWarningMessageUpload(1);
        page.waitForTimeout(1000);

        List<PendingProcessing> pendingProcessing = getObjectsFromDB(DbName.POSTGRES, AR_PENDING_PROCESSING_TABLE_NAME, String.format("ucid in ('%s')", client1.getUcid()), PendingProcessing.class);
        assertThat(pendingProcessing.size(), is(0));
        List<PendingProcessing> pendingProcessing2 = getObjectsFromDB(DbName.POSTGRES, AR_PENDING_PROCESSING_TABLE_NAME, String.format("ucid in ('%s')", client2.getUcid()), PendingProcessing.class);
        assertThat(pendingProcessing2.size(), is(1));
        PendingProcessing pending = pendingProcessing2.getFirst();
        assertThat(pending.getUcid(), is(client2.getUcid()));
        assertThat(pending.getFraudTypeCode(), is(fraudType.getCode()));
        assertThat(pendingProcessing2.getFirst().getFraudSubtypeCode(), nullValue());

        List<AbuserHistory> abuserHistory = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_HISTORY_TABLE_NAME, String.format("ucid = '%s'", client1.getUcid()), AbuserHistory.class);

        AbuserDeduction expected = new AbuserDeduction(
                client1.getUcid(), abuserHistory.getLast().getId(), account1.account.toString(), account1.serverIdSt, account1.serverName, "USD", "Vantage", WAS_HOLDING.getDisplayName(), NOT_SENT.getDisplayName(), NO_DEDUCTION.getDisplayName(), NOT_REQUIRED.getDisplayName(), commentary, 0.0, 0.0, 0.0, 0.0, null, null, null, null, "backoffice-test backoffice-test", "Vindex BO", "ILLEGAL_PROFIT", null, null, 0.0, 0.0, client1.getUserId().toString(), 500.12, 500.12, false, DeductionType.NO_DEDUCTION.getDisplayName()
        );

        List<AbuserDeduction> deductionList = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_DEDUCTION_TABLE_NAME, String.format("ucid = '%s'", client1.getUcid()), AbuserDeduction.class);
        assertThat(deductionList.size(), is(1));
        AbuserDeduction actualDeduction = deductionList.getFirst();
        assertThat(actualDeduction, is(expected));


        List<AbuserDeduction> deductionList2 = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_DEDUCTION_TABLE_NAME, String.format("ucid = '%s'", client2.getUcid()), AbuserDeduction.class);
        assertThat(deductionList2.size(), is(0));
    }

    @Test
    @AllureId("1559")
    @DisplayName("Batch upload, then manageFraud check add deduction to fraud")
    void manageFraudCreateDeduction() throws Exception {

        deleteUserFromAbuseRegistry(client1.getUcid());
        deleteUserFromAbuseRegistry(client2.getUcid());

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        fraudstersPage.navigateAbuseRegistryFraudsters();
        fraudstersPage.openUploadDrawer();
        fraudstersPage.selectBrandToUpload(Brand.VANTAGE.getDisplayName());
        fraudstersPage.typeClientsID(client2.getUserId().toString());
        fraudstersPage.clickAddFraudButton();
        FraudType fraudType = FraudType.CHARGEBACK;
        //Partial Deduction
        fraudstersPage.addSelectedFraudAdd(fraudType.getName(), "Confirmed");
        String commentary = "test" + getCurrentTimestampSeconds();
        fraudstersPage.fillCommentary(commentary);
        fraudstersPage.clickApplyUpload();
        fraudstersPage.verifySuccessMessageUpload();
        fraudstersPage.verifyWarningMessageUpload(1);
        page.waitForTimeout(1000);

        List<PendingProcessing> pendingProcessing2 = getObjectsFromDB(DbName.POSTGRES, AR_PENDING_PROCESSING_TABLE_NAME, String.format("ucid in ('%s')", client2.getUcid()), PendingProcessing.class);
        assertThat(pendingProcessing2.size(), is(1));
        PendingProcessing pending = pendingProcessing2.getFirst();
        assertThat(pending.getUcid(), is(client2.getUcid()));
        assertThat(pending.getFraudTypeCode(), is(fraudType.getCode()));
        assertThat(pendingProcessing2.getFirst().getFraudSubtypeCode(), nullValue());

        //manageFraud

        investigationPage.navigateToClient(client2.getUcid());
        alertsPage.waitForPageToLoad();
        resolvePage.openReportFraudForm();

        resolvePage.previouslyReportedFraudAddDeduction();
        resolvePage.clickIllegalProfitAccountsDropdown();
        resolvePage.clickAccountInDropdown(account2.account.toString());
        resolvePage.clickUseAsIllegalProfit();
        resolvePage.fillCommentAndApply("test comment");

        resolvePage.openReportFraudForm();
        List<String> previouslyReportedFraudItems = resolvePage.getPreviouslyReportedFraudItems2();
        assertThat(previouslyReportedFraudItems.size(), is(1));

        List<AbuserDeduction> deductionList = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_DEDUCTION_TABLE_NAME, String.format("ucid = '%s'", client2.getUcid()), AbuserDeduction.class);
        assertThat(deductionList.size(), is(1));
        AbuserDeduction actual = deductionList.getFirst();
        assertThat(actual.getAbuserHistoryId(), is(pending.getAbuserHistoryId()));
    }

    @Test
    @AllureId("1560")
    @DisplayName("Batch upload no deduction created on potential")
    void potentialNoDeductionTest() throws Exception {

        deleteUserFromAbuseRegistry(client1.getUcid());
        deleteUserFromAbuseRegistry(client2.getUcid());

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        fraudstersPage.navigateAbuseRegistryFraudsters();
        fraudstersPage.openUploadDrawer();
        fraudstersPage.selectBrandToUpload(Brand.VANTAGE.getDisplayName());
        fraudstersPage.typeClientsID(client1.getUserId().toString(), client2.getUserId().toString());
        fraudstersPage.clickAddFraudButton();
        FraudType fraudType = FraudType.CHARGEBACK;
        //Partial Deduction
        fraudstersPage.addSelectedFraudAdd(fraudType.getName(), "Potential");
        String commentary = "test" + getCurrentTimestampSeconds();
        fraudstersPage.fillCommentary(commentary);
        fraudstersPage.clickApplyUpload();
        fraudstersPage.verifySuccessMessageUpload();
        page.waitForTimeout(1000);

        List<PendingProcessing> pendingProcessing = getObjectsFromDB(DbName.POSTGRES, AR_PENDING_PROCESSING_TABLE_NAME, String.format("ucid in ('%s')", client1.getUcid()), PendingProcessing.class);
        assertThat(pendingProcessing.size(), is(0));
        List<PendingProcessing> pendingProcessing2 = getObjectsFromDB(DbName.POSTGRES, AR_PENDING_PROCESSING_TABLE_NAME, String.format("ucid in ('%s')", client2.getUcid()), PendingProcessing.class);
        assertThat(pendingProcessing2.size(), is(0));

        List<AbuserDeduction> deductionList = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_DEDUCTION_TABLE_NAME, String.format("ucid = '%s'", client1.getUcid()), AbuserDeduction.class);
        assertThat(deductionList.size(), is(0));

        List<AbuserDeduction> deductionList2 = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_DEDUCTION_TABLE_NAME, String.format("ucid = '%s'", client2.getUcid()), AbuserDeduction.class);
        assertThat(deductionList2.size(), is(0));
    }

}
