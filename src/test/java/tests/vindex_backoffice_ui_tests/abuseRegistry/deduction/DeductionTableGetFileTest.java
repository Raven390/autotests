package tests.vindex_backoffice_ui_tests.abuseRegistry.deduction;

import static business_objects.db.abuse_registry_db.AbuserDeductionFactory.generateAbuserDeductionByAccount;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountDataForUi;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static helpers.api.AbuseRegistryHelper.addFraudForClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.Currency.EUR;
import static helpers.data.enums.FraudSubtype.INTERNAL;
import static helpers.data.enums.FraudType.HEDGING;
import static helpers.data.enums.FraudTypeStatus.CONFIRMED;
import static helpers.data.enums.deduction.DeductionStatusEmail.NOT_SENT;
import static helpers.data.enums.deduction.DeductionStatusEmail.SENT;
import static helpers.data.enums.deduction.DeductionStatusOpenPositions.NOT_HOLDING;
import static helpers.database.ArHelper.deleteUserFromAbuseRegistry;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Utils.*;

import business_objects.db.abuse_registry_db.AbuserDeduction;
import business_objects.db.abuse_registry_db.AbuserHistory;
import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.mt_account.MtAccountObject;
import com.microsoft.playwright.Download;
import helpers.data.ClientHelper;
import helpers.data.enums.deduction.DeductionStatusApproval;
import helpers.data.enums.deduction.DeductionStatusDeduction;
import helpers.database.DbName;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

@Tag(TEAM_BACKOFFICE)
@Tag(LAYER_WEB)
@Tag(ABUSE_REGISTRY)
@Feature("BMS-2994 Change UI flow of 'Get file' and 'Move to next status' features in Deductions")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DeductionTableGetFileTest extends TestBaseWeb {

    private static final ClientHelper client = getRandomVantageClientAllFields();
    private static final CrmTbUserObject crmTbUser = generateUserByClient(client);
    private static final CrmTbAccountObject account = generateCrmTbAccountDataForUi(client);
    private static AbuserDeduction deduction;
    private static Path downloadsDir;

    @BeforeAll
    static void setup() throws Exception {
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        account.currency = EUR.getIsoCode();
        MtAccountObject mtAccount = generateMtAccountByCrmTbAccount(account);
        insertCrmAccountsToDb(account);
        insertObjectToDb(MT_ACCOUNT_TABLE_NAME, mtAccount);
        Thread.sleep(2000);
        addFraudForClient(client, HEDGING, INTERNAL, CONFIRMED, List.of("EURUSD", "GBPUSD"));
        List<AbuserHistory> abuserHistory = getObjectsFromDB(
                DbName.POSTGRES,
                AR_ABUSER_HISTORY_TABLE_NAME,
                String.format("ucid = '%s'", client.getUcid()),
                AbuserHistory.class);
        deduction = generateAbuserDeductionByAccount(
                account, abuserHistory.getLast().getId());
        deduction.setStatusOpenPositions(NOT_HOLDING.getDisplayName());
        deduction.setStatusEmail(NOT_SENT.getDisplayName());
        deduction.setStatusDeduction(DeductionStatusDeduction.TO_BE_DEDUCTED.getDisplayName());
        deduction.setStatusApproval(DeductionStatusApproval.AWAITING_APPROVAL.getDisplayName());
        insertObjectToDb(DbName.POSTGRES, AR_ABUSER_DEDUCTION_TABLE_NAME, deduction);
    }

    @AfterAll
    static void teardown() throws Exception {
        deleteObjectFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        deleteUserFromAbuseRegistry(client.getUcid());
    }

    @BeforeEach
    void createTempDir() throws IOException {
        downloadsDir = Files.createTempDirectory("pw-downloads-");
    }

    @AfterEach
    void cleanUpDownloads() throws IOException {
        if (downloadsDir != null && Files.exists(downloadsDir)) {
            Files.walk(downloadsDir).sorted(Comparator.reverseOrder()).forEach(path -> {
                try {
                    Files.delete(path);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to delete " + path, e);
                }
            });
        }
    }

    @Test
    @AllureId("2139")
    @DisplayName("Get deductions file without changing status")
    @Order(1)
    void deductionTableGetFileNoStatusChangeTest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        deductionPage.navigateDeduction();
        deductionPage.fillSearchAndClickSearchButton(account.account.toString());
        deductionPage.waitForPageToLoad();
        deductionPage.clickAllDeductionsCheckbox();
        deductionPage.clickOnGetFileButton();

        Download download = deductionPage.clickGetFilePopupNoButton();

        Path downloadPath = downloadsDir.resolve(download.suggestedFilename());
        download.saveAs(downloadPath);

        assertThat("Verify file is downloaded", Files.exists(downloadPath), is(true));

        List<AbuserDeduction> deductionList = getObjectsFromDB(
                DbName.POSTGRES,
                AR_ABUSER_DEDUCTION_TABLE_NAME,
                String.format("ucid = '%s'", client.getUcid()),
                AbuserDeduction.class);

        assertThat(
                "Verify that deduction status email didn't change after file download",
                deductionList.stream().map(AbuserDeduction::getStatusEmail).toList(),
                everyItem(is(NOT_SENT.getDisplayName())));
    }

    @Test
    @AllureId("2140")
    @DisplayName("Get deductions file with changing status")
    @Order(2)
    void deductionTableGetFileWithStatusChangeTest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        deductionPage.navigateDeduction();
        deductionPage.fillSearchAndClickSearchButton(account.account.toString());
        deductionPage.waitForPageToLoad();
        deductionPage.clickAllDeductionsCheckbox();
        deductionPage.clickOnGetFileButton();

        Download download = deductionPage.clickGetFilePopupYesButton();

        Path downloadPath = downloadsDir.resolve(download.suggestedFilename());
        download.saveAs(downloadPath);

        assertThat("Verify file is downloaded", Files.exists(downloadPath), is(true));

        List<AbuserDeduction> deductionList = getObjectsFromDB(
                DbName.POSTGRES,
                AR_ABUSER_DEDUCTION_TABLE_NAME,
                String.format("ucid = '%s'", client.getUcid()),
                AbuserDeduction.class);

        assertThat(
                "Verify that deduction status email changed after file download",
                deductionList.stream().map(AbuserDeduction::getStatusEmail).toList(),
                everyItem(is(SENT.getDisplayName())));
    }
}
