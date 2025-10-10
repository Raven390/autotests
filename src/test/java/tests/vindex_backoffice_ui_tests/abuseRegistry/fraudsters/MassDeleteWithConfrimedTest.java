package tests.vindex_backoffice_ui_tests.abuseRegistry.fraudsters;

import business_objects.db.abuse_registry_db.Abuser;
import business_objects.db.abuse_registry_db.AbuserDeduction;
import business_objects.db.abuse_registry_db.AbuserFraudType;
import business_objects.db.abuse_registry_db.PendingProcessing;
import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.mt_account.MtAccountObject;
import business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObject;
import business_objects.db.clickhouse.mt_mt5_positions.MtMt5PositionsObject;
import helpers.data.ClientHelper;
import helpers.data.enums.*;
import helpers.database.DbName;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

import java.util.List;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountDataForUi;
import static business_objects.db.clickhouse.crm_tb_account_for_mt.crm_tb_account.CrmTbAccountForMtObjectFactory.generateAccountForMtByAccount;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObjectFactory.generateMt4TradesCoercedAccountProfitComment;
import static business_objects.db.clickhouse.mt_mt5_positions.MtMt5PositionsObjectFactory.generateMtMt5PositionsObject;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.Currency.USD;
import static helpers.database.ArHelper.deleteUserFromAbuseRegistry;
import static helpers.database.ArHelper.waitForClientToChangeStatus;
import static helpers.database.DbHelper.*;
import static helpers.database.DbHelper.deleteEntryFromDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static utils.Constants.*;
import static utils.Utils.getCurrentTimestampSeconds;
import static utils.Utils.getRandomIntPositive;

@Tag(TEAM_BACKOFFICE)
@Tag(LAYER_WEB)
@Tag(ABUSE_REGISTRY)
@Feature("BMS-2293 Delete confirmed fraud in bulk operations")
class MassDeleteWithConfrimedTest extends TestBaseWeb {

    private static final ClientHelper client2 = getRandomVantageClientAllFields();
    private static final ClientHelper client1 = getRandomVantageClientAllFields();
    private static final CrmTbUserObject crmTbUser = generateUserByClient(client1);
    private static final CrmTbAccountObject account1 = generateCrmTbAccountDataForUi(client1);
    private static final CrmTbAccountObject account2 = generateCrmTbAccountDataForUi(client2);
    private static MtAccountObject mtAccount1;
    private static MtMt4TradesCoercedObject trade1;

    @BeforeAll
    static void setup() {
        account1.currency = USD.getCode();

        mtAccount1 = generateMtAccountByCrmTbAccount(account1);


        String comment = "comment";
        trade1 = generateMt4TradesCoercedAccountProfitComment(account1, 500.12 + 10_000d, comment);
        account2.account = getRandomIntPositive();
        CrmTbUserObject crmClient2 = generateUserByClient(client2);
        CrmTbAccountObject account3 = generateCrmTbAccountDataForUi(client2);
        insertObjectsToDb(CRM_USER_TABLE_NAME, List.of(crmTbUser, crmClient2));
        insertObjectsToDb(CRM_TB_ACCOUNT_TABLE_NAME, List.of(account1, account2, account3));
        insertObjectsToDb(CRM_TB_ACCOUNT_FOR_MT_TABLE_NAME, List.of(generateAccountForMtByAccount(account1), generateAccountForMtByAccount(account2), generateAccountForMtByAccount(account3)));
        insertObjectsToDb(MT_ACCOUNT_TABLE_NAME, List.of(mtAccount1));
        insertObjectsToDb(MT4_TRADES_COERCED_TABLE_NAME, List.of(trade1));
        MtMt5PositionsObject position1 = generateMtMt5PositionsObject(client1);
        MtMt5PositionsObject position2 = generateMtMt5PositionsObject(client2);
        insertObjectsToDb(MT5_POSITIONS_TABLE_NAME, List.of(position1, position2));
    }

    @AfterAll
    static void teardown() throws Exception {
        deleteUserFromAbuseRegistry(client1.getUcid());
        deleteUserFromAbuseRegistry(client2.getUcid());
        deleteEntryFromDb(MT5_POSITIONS_TABLE_NAME, String.format("ucid = '%s'", client1.getUcid()));
    }

    @AfterEach
    void deleteFromAR() throws Exception {
        deleteUserFromAbuseRegistry(client1.getUcid());
        deleteUserFromAbuseRegistry(client2.getUcid());
    }

    @Test
    @AllureId("1638")
    @DisplayName("Bulk delete confirmed frauds for pending processing client with deduction")
    void abuseRegistryMassDeleteWithConfirmedFullFlowTest() throws Exception {
        //mass upload set confirmed
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsDutyOpsUser();
        fraudstersPage.navigateAbuseRegistryFraudsters();
        fraudstersPage.openUploadDrawer();
        fraudstersPage.selectClientIdsAndBrandToUpload(Brand.VANTAGE.getDisplayName());
        fraudstersPage.typeClientsID(client1.getUserId().toString(), client2.getUserId().toString());
        fraudstersPage.clickAddFraudButton();
        FraudType fraudTypeOld = FraudType.LOOPHOLE_ABUSE;
        fraudstersPage.addSelectedFraudAdd(fraudTypeOld.getName(), "Confirmed");
        String commentary = String.format("test%s", getCurrentTimestampSeconds());
        fraudstersPage.fillCommentary(commentary);
        fraudstersPage.clickApplyUpload();
        fraudstersPage.verifySuccessMessageUpload(1);
        fraudstersPage.verifyWarningMessageUpload(1);

        //delete
        fraudstersPage.openRemoveDrawer();
        fraudstersPage.selectBrandToUpload(Brand.VANTAGE.getDisplayName());
        fraudstersPage.typeClientsID(client1.getUserId().toString(), client2.getUserId().toString());
        FraudType fraudType = FraudType.LOOPHOLE_ABUSE;
        fraudstersPage.addFraudForDeleteWithStatus(fraudType, FraudTypeStatus.CONFIRMED);
        fraudstersPage.fillCommentary(commentary);
        fraudstersPage.clickDeleteUpload();
        fraudstersPage.verifySuccessMessageDelete();

        waitForClientToChangeStatus(client1.getUcid(), FraudTypeStatus.CLEANED);

        List<PendingProcessing> pendingProcessing = getObjectsFromDB(DbName.POSTGRES, AR_PENDING_PROCESSING_TABLE_NAME, String.format("ucid in ('%s','%s')", client2.getUcid(), client2.getUcid()), PendingProcessing.class);
        assertThat("Verify there is no pending_processing", pendingProcessing.size(), is(0));

        List<AbuserDeduction> deduction = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_DEDUCTION_TABLE_NAME, String.format("ucid in ('%s')", client1.getUcid()), AbuserDeduction.class);
        assertThat("Verify deduction is deleted", deduction.getFirst().getDeleted(), is(true));

        List<AbuserFraudType> frauds1 = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_FRAUD_TYPE_TABLE_NAME, String.format("ucid='%s'", client1.getUcid()), AbuserFraudType.class);
        assertThat("Verify fraud is cleaned", frauds1.getFirst().getStatus(), is(FraudTypeStatus.CLEANED.getStatus()));

        List<AbuserFraudType> frauds2 = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_FRAUD_TYPE_TABLE_NAME, String.format("ucid='%s'", client2.getUcid()), AbuserFraudType.class);
        assertThat("Verify fraud is cleaned", frauds2.getFirst().getStatus(), is(FraudTypeStatus.CLEANED.getStatus()));

        Abuser abuser1 = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_TABLE_NAME, String.format("ucid='%s'", client1.getUcid()), Abuser.class).getFirst();
        assertThat("Verify abuser is cleaned", abuser1.getStatus(), is(FraudTypeStatus.CLEANED.getStatus()));

        Abuser abuser2 = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_TABLE_NAME, String.format("ucid='%s'", client2.getUcid()), Abuser.class).getFirst();
        assertThat("Verify abuser is cleaned", abuser2.getStatus(), is(FraudTypeStatus.CLEANED.getStatus()));

    }

    @Test
    @AllureId("1639")
    @DisplayName("Bulk delete potential fraud type")
    void abuseRegistryMassDeleteWithConfirmedPotentialTest() throws Exception {
        //mass upload set potential
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsDutyOpsUser();
        fraudstersPage.navigateAbuseRegistryFraudsters();
        fraudstersPage.openUploadDrawer();
        fraudstersPage.selectClientIdsAndBrandToUpload(Brand.VANTAGE.getDisplayName());
        fraudstersPage.typeClientsID(client1.getUserId().toString(), client2.getUserId().toString());
        fraudstersPage.clickAddFraudButton();
        FraudType fraudTypeOld = FraudType.LOOPHOLE_ABUSE;
        fraudstersPage.addSelectedFraudAdd(fraudTypeOld.getName(), FraudTypeStatus.POTENTIAL.getDisplayName());
        String commentary = String.format("test%s", getCurrentTimestampSeconds());
        fraudstersPage.fillCommentary(commentary);
        fraudstersPage.clickApplyUpload();
        fraudstersPage.verifySuccessMessageUpload();

        //delete
        fraudstersPage.openRemoveDrawer();
        fraudstersPage.selectBrandToUpload(Brand.VANTAGE.getDisplayName());
        fraudstersPage.typeClientsID(client1.getUserId().toString(), client2.getUserId().toString());
        FraudType fraudType = FraudType.LOOPHOLE_ABUSE;
        fraudstersPage.addFraudForDeleteWithStatus(fraudType, FraudTypeStatus.POTENTIAL);
        fraudstersPage.fillCommentary(commentary);
        fraudstersPage.clickDeleteUpload();
        fraudstersPage.verifySuccessMessageDelete();

        waitForClientToChangeStatus(client1.getUcid(), FraudTypeStatus.CLEANED);

        List<AbuserFraudType> frauds1 = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_FRAUD_TYPE_TABLE_NAME, String.format("ucid='%s'", client1.getUcid()), AbuserFraudType.class);
        assertThat("Verify fraud is cleaned", frauds1.getFirst().getStatus(), is(FraudTypeStatus.CLEANED.getStatus()));

        List<AbuserFraudType> frauds2 = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_FRAUD_TYPE_TABLE_NAME, String.format("ucid='%s'", client2.getUcid()), AbuserFraudType.class);
        assertThat("Verify fraud is cleaned", frauds2.getFirst().getStatus(), is(FraudTypeStatus.CLEANED.getStatus()));

        Abuser abuser1 = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_TABLE_NAME, String.format("ucid='%s'", client1.getUcid()), Abuser.class).getFirst();
        assertThat("Verify abuser is cleaned", abuser1.getStatus(), is(FraudTypeStatus.CLEANED.getStatus()));

        Abuser abuser2 = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_TABLE_NAME, String.format("ucid='%s'", client2.getUcid()), Abuser.class).getFirst();
        assertThat("Verify abuser is cleaned", abuser2.getStatus(), is(FraudTypeStatus.CLEANED.getStatus()));

    }


    @Test
    @AllureId("1640")
    @DisplayName("Bulk delete set potential after confirmed is deleted test")
    void abuseRegistryMassDeleteWithConfirmedAfterSetPotentialTest() throws Exception {
        //mass upload
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsDutyOpsUser();
        fraudstersPage.navigateAbuseRegistryFraudsters();
        //set potential
        fraudstersPage.openUploadDrawer();
        fraudstersPage.selectClientIdsAndBrandToUpload(Brand.VANTAGE.getDisplayName());
        fraudstersPage.typeClientsID(client1.getUserId().toString(), client2.getUserId().toString());
        fraudstersPage.clickAddFraudButton();
        FraudType fraudTypeOld = FraudType.LOOPHOLE_ABUSE;
        fraudstersPage.addSelectedFraudAdd(fraudTypeOld.getName(), FraudTypeStatus.POTENTIAL.getDisplayName());
        String commentary = String.format("test%s", getCurrentTimestampSeconds());
        fraudstersPage.fillCommentary(commentary);
        fraudstersPage.clickApplyUpload();
        fraudstersPage.verifySuccessMessageUpload();
        //set confirmed
        fraudstersPage.openUploadDrawer();
        fraudstersPage.selectClientIdsAndBrandToUpload(Brand.VANTAGE.getDisplayName());
        fraudstersPage.typeClientsID(client1.getUserId().toString(), client2.getUserId().toString());
        fraudstersPage.clickAddFraudButton();
        fraudstersPage.addSelectedFraudAdd(fraudTypeOld.getName(), FraudTypeStatus.CONFIRMED.getDisplayName());
        fraudstersPage.fillCommentary(commentary);
        fraudstersPage.clickApplyUpload();
        fraudstersPage.verifySuccessMessageUpload(1);
        //delete
        fraudstersPage.openRemoveDrawer();
        fraudstersPage.selectBrandToUpload(Brand.VANTAGE.getDisplayName());
        fraudstersPage.typeClientsID(client1.getUserId().toString(), client2.getUserId().toString());
        FraudType fraudType = FraudType.LOOPHOLE_ABUSE;
        fraudstersPage.addFraudForDeleteWithStatus(fraudType, FraudTypeStatus.CONFIRMED);
        fraudstersPage.fillCommentary(commentary);
        fraudstersPage.clickDeleteUpload();
        fraudstersPage.verifySuccessMessageDelete();

        waitForClientToChangeStatus(client1.getUcid(), FraudTypeStatus.POTENTIAL);

        List<PendingProcessing> pendingProcessing = getObjectsFromDB(DbName.POSTGRES, AR_PENDING_PROCESSING_TABLE_NAME, String.format("ucid in ('%s','%s')", client2.getUcid(), client2.getUcid()), PendingProcessing.class);
        assertThat("Verify there is no pending_processing", pendingProcessing.size(), is(0));

        List<AbuserDeduction> deduction = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_DEDUCTION_TABLE_NAME, String.format("ucid in ('%s')", client1.getUcid()), AbuserDeduction.class);
        assertThat("Verify deduction is deleted", deduction.getFirst().getDeleted(), is(true));

        List<AbuserFraudType> frauds1 = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_FRAUD_TYPE_TABLE_NAME, String.format("ucid='%s'", client1.getUcid()), AbuserFraudType.class);
        assertThat("Verify fraud is potential", frauds1.getFirst().getStatus(), is(FraudTypeStatus.POTENTIAL.getStatus()));

        List<AbuserFraudType> frauds2 = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_FRAUD_TYPE_TABLE_NAME, String.format("ucid='%s'", client2.getUcid()), AbuserFraudType.class);
        assertThat("Verify fraud is potential", frauds2.getFirst().getStatus(), is(FraudTypeStatus.POTENTIAL.getStatus()));

        Abuser abuser1 = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_TABLE_NAME, String.format("ucid='%s'", client1.getUcid()), Abuser.class).getFirst();
        assertThat("Verify abuser is potential", abuser1.getStatus(), is(FraudTypeStatus.POTENTIAL.getStatus()));

        Abuser abuser2 = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_TABLE_NAME, String.format("ucid='%s'", client2.getUcid()), Abuser.class).getFirst();
        assertThat("Verify abuser is potential", abuser2.getStatus(), is(FraudTypeStatus.POTENTIAL.getStatus()));

    }

    @Test
    @AllureId("1641")
    @DisplayName("Bulk delete confirmed with multiple deductions test")
    void abuseRegistryMassDeleteWithConfirmedDeleteMultipleDeductionsTest() throws Exception {
        //mass upload
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsDutyOpsUser();
        fraudstersPage.navigateAbuseRegistryFraudsters();
        FraudType fraudTypeOld = FraudType.LOOPHOLE_ABUSE;
        String commentary = String.format("test%s", getCurrentTimestampSeconds());
        //set 2 confirmed
        fraudstersPage.openUploadDrawer();
        fraudstersPage.selectClientIdsAndBrandToUpload(Brand.VANTAGE.getDisplayName());
        fraudstersPage.typeClientsID(client1.getUserId().toString());
        fraudstersPage.clickAddFraudButton();
        fraudstersPage.addSelectedFraudAdd(fraudTypeOld.getName(), FraudTypeStatus.CONFIRMED.getDisplayName());
        fraudstersPage.fillCommentary(commentary);
        fraudstersPage.clickApplyUpload();
        fraudstersPage.verifySuccessMessageUpload(1);
        waitForClientToChangeStatus(client1.getUcid(), FraudTypeStatus.CONFIRMED);
        fraudstersPage.openUploadDrawer();
        fraudstersPage.selectClientIdsAndBrandToUpload(Brand.VANTAGE.getDisplayName());
        fraudstersPage.typeClientsID(client1.getUserId().toString());
        fraudstersPage.clickAddFraudButton();
        fraudstersPage.addSelectedFraudAdd(fraudTypeOld.getName(), FraudTypeStatus.CONFIRMED.getDisplayName());
        fraudstersPage.fillCommentary(commentary);
        fraudstersPage.clickApplyUpload();
        fraudstersPage.verifySuccessMessageUpload(1);
        //delete
        fraudstersPage.openRemoveDrawer();
        fraudstersPage.selectBrandToUpload(Brand.VANTAGE.getDisplayName());
        fraudstersPage.typeClientsID(client1.getUserId().toString());
        FraudType fraudType = FraudType.LOOPHOLE_ABUSE;
        fraudstersPage.addFraudForDeleteWithStatus(fraudType, FraudTypeStatus.CONFIRMED);
        fraudstersPage.fillCommentary(commentary);
        fraudstersPage.clickDeleteUpload();
        fraudstersPage.verifySuccessMessageDelete();

        waitForClientToChangeStatus(client1.getUcid(), FraudTypeStatus.CLEANED);

        List<AbuserDeduction> deduction = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_DEDUCTION_TABLE_NAME, String.format("ucid in ('%s')", client1.getUcid()), AbuserDeduction.class);
        assertThat("Verify deductions all is deleted", deduction.stream().allMatch(AbuserDeduction::getDeleted), is(true));

        List<AbuserFraudType> frauds1 = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_FRAUD_TYPE_TABLE_NAME, String.format("ucid='%s'", client1.getUcid()), AbuserFraudType.class);
        assertThat("Verify fraud is cleaned", frauds1.getFirst().getStatus(), is(FraudTypeStatus.CLEANED.getStatus()));

        Abuser abuser1 = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_TABLE_NAME, String.format("ucid='%s'", client1.getUcid()), Abuser.class).getFirst();
        assertThat("Verify abuser is cleaned", abuser1.getStatus(), is(FraudTypeStatus.CLEANED.getStatus()));

    }

    @Test
    @AllureId("1642")
    @DisplayName("Bulk delete validations NOT_FOUND SKIPPED_DEDUCTION_PROCESSED")
    void abuseRegistryMassDeleteWithConfirmedValidation1Test() {
        //mass upload
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsDutyOpsUser();
        fraudstersPage.navigateAbuseRegistryFraudsters();
        String commentary = String.format("test%s", getCurrentTimestampSeconds());
        //upload 1 for deduction
        FraudType fraudType = FraudType.LOOPHOLE_ABUSE;
        fraudstersPage.openUploadDrawer();
        fraudstersPage.selectClientIdsAndBrandToUpload(Brand.VANTAGE.getDisplayName());
        fraudstersPage.typeClientsID(client1.getUserId().toString());
        fraudstersPage.clickAddFraudButton();
        fraudstersPage.addSelectedFraudAdd(fraudType.getName(), FraudTypeStatus.CONFIRMED.getDisplayName());
        fraudstersPage.fillCommentary(commentary);
        fraudstersPage.clickApplyUpload();
        fraudstersPage.verifySuccessMessageUpload(1);
        //change status for deduction
        executeQueryToDb(DbName.POSTGRES, String.format("UPDATE %s SET status_deduction = 'DEDUCTED' WHERE ucid = '%s'", AR_ABUSER_DEDUCTION_TABLE_NAME, client1.getUcid()));


        //delete
        fraudstersPage.openRemoveDrawer();
        fraudstersPage.selectBrandToUpload(Brand.VANTAGE.getDisplayName());
        fraudstersPage.typeClientsID(client1.getUserId().toString(), client2.getUserId().toString());
        fraudstersPage.addFraudForDeleteWithStatus(fraudType, FraudTypeStatus.CONFIRMED);
        fraudstersPage.fillCommentary(commentary);
        fraudstersPage.clickDeleteUpload();
        List<String> validationList = fraudstersPage.getValidationList();
        assertThat("Verify validation list is not empty", validationList.size(), is(2));
        assertThat("Verify contains validation text", validationList.stream().allMatch(s -> s.contains(String.format("%sNo such fraud record for this client", client2.getUserId())) || s.contains(String.format("%sDeduction already processed", client1.getUserId()))), is(true));
    }


    @Test
    @AllureId("1643")
    @DisplayName("Bulk delete validation ALREADY_CLEANED")
    void abuseRegistryMassDeleteWithConfirmedValidation2Test() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsDutyOpsUser();
        fraudstersPage.navigateAbuseRegistryFraudsters();
        fraudstersPage.openUploadDrawer();
        fraudstersPage.selectClientIdsAndBrandToUpload(Brand.VANTAGE.getDisplayName());
        fraudstersPage.typeClientsID(client1.getUserId().toString(), client2.getUserId().toString());
        fraudstersPage.clickAddFraudButton();
        FraudType fraudTypeOld = FraudType.LOOPHOLE_ABUSE;
        fraudstersPage.addSelectedFraudAdd(fraudTypeOld.getName(), FraudTypeStatus.POTENTIAL.getDisplayName());
        String commentary = String.format("test%s", getCurrentTimestampSeconds());
        fraudstersPage.fillCommentary(commentary);
        fraudstersPage.clickApplyUpload();
        fraudstersPage.verifySuccessMessageUpload();

        //delete
        fraudstersPage.openRemoveDrawer();
        fraudstersPage.selectBrandToUpload(Brand.VANTAGE.getDisplayName());
        fraudstersPage.typeClientsID(client1.getUserId().toString(), client2.getUserId().toString());
        FraudType fraudType = FraudType.LOOPHOLE_ABUSE;
        fraudstersPage.addFraudForDeleteWithStatus(fraudType, FraudTypeStatus.POTENTIAL);
        fraudstersPage.fillCommentary(commentary);
        fraudstersPage.clickDeleteUpload();
        fraudstersPage.verifySuccessMessageDelete();
        waitForClientToChangeStatus(client1.getUcid(), FraudTypeStatus.CLEANED);
        //delete 2nd time
        fraudstersPage.openRemoveDrawer();
        fraudstersPage.selectBrandToUpload(Brand.VANTAGE.getDisplayName());
        fraudstersPage.typeClientsID(client1.getUserId().toString(), client2.getUserId().toString());
        fraudstersPage.addFraudForDeleteWithStatus(fraudType, FraudTypeStatus.POTENTIAL);
        fraudstersPage.fillCommentary(commentary);
        fraudstersPage.clickDeleteUpload();

        List<String> validationList = fraudstersPage.getValidationList();
        assertThat("Verify validation list is not empty", validationList.size(), is(2));
        assertThat("Verify contains validation text", validationList.stream().allMatch(s -> s.contains("Fraud already removed earlier")), is(true));


    }

    @Test
    @AllureId("1644")
    @DisplayName("Bulk delete no ra_senior role")
    void abuseRegistryMassDeleteWithConfirmedNoRoleTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsSeniorOpsUser();
        fraudstersPage.navigateAbuseRegistryFraudsters();
        //delete
        fraudstersPage.openRemoveDrawer();
        fraudstersPage.selectBrandToUpload(Brand.VANTAGE.getDisplayName());
        fraudstersPage.typeClientsID(client1.getUserId().toString());
        FraudType fraudType = FraudType.LOOPHOLE_ABUSE;
        assertThat("Verify status is not visible", fraudstersPage.addFraudForDeleteWithStatus(fraudType, FraudTypeStatus.CONFIRMED), is(false));


    }


}
