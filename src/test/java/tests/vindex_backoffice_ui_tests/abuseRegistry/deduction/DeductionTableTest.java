package tests.vindex_backoffice_ui_tests.abuseRegistry.deduction;

import static business_objects.db.abuse_registry_db.AbuserDeductionFactory.generateAbuserDeductionByAccount;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountDataForUi;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static helpers.api.AbuseRegistryHelper.addFraudForClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.Currency.EUR;
import static helpers.data.enums.Currency.USD;
import static helpers.data.enums.FraudSubtype.INTERNAL;
import static helpers.data.enums.FraudType.HEDGING;
import static helpers.data.enums.FraudTypeStatus.CONFIRMED;
import static helpers.data.enums.deduction.DeductionEmailUi.*;
import static helpers.data.enums.deduction.DeductionStatusUi.*;
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
import helpers.data.ClientHelper;
import helpers.database.DbName;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

@Tag(TEAM_BACKOFFICE)
@Tag(LAYER_WEB)
@Tag(ABUSE_REGISTRY)
class DeductionTableTest extends TestBaseWeb {

    private static final ClientHelper client = getRandomVantageClientAllFields();
    private static final CrmTbUserObject crmTbUser = generateUserByClient(client);
    private static final CrmTbAccountObject account = generateCrmTbAccountDataForUi(client);
    private static final DecimalFormat formatter = new DecimalFormat("#,###.##");
    private static AbuserDeduction deduction;

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
        insertObjectToDb(DbName.POSTGRES, AR_ABUSER_DEDUCTION_TABLE_NAME, deduction);
    }

    @AfterAll
    static void teardown() throws Exception {
        deleteObjectFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        deleteUserFromAbuseRegistry(client.getUcid());
    }

    @Test
    @Feature("BMS-1141 Deduction list")
    @AllureId("1366")
    @DisplayName("Verify Abuse registry deduction table")
    void deductionTableTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(client.getUcid());
        alertsPage.waitForPageToLoad();
        deductionPage.clickAbuseRegistryButton();
        assertThat(
                "Check table headers",
                deductionPage.getDeductionTableHeaders(),
                contains(
                        "CLIENT",
                        "ACCOUNT",
                        "BEHAVIOR",
                        "STATUS",
                        "EMAIL",
                        "ILLEGAL PROFIT",
                        "SUGGESTION",
                        "DEDUCTION",
                        "CREATED",
                        "NOTE"));
        writeLog(deductionPage.getDeductionTableDataByRows().getFirst());
        assertThat(
                "Check table data",
                deductionPage.getDeductionTableDataByRows().getFirst(),
                contains(
                        String.format("%s %s", crmTbUser.firstName, crmTbUser.lastName),
                        client.getUserId().toString(),
                        account.account.toString(),
                        account.serverName,
                        "",
                        String.format(
                                "%s (%s)", HEDGING.getName(), INTERNAL.getName().toLowerCase()),
                        "Deduction failed",
                        "Full deduction",
                        "Approved",
                        client.getBrand(),
                        String.format("%s %s", formatter.format(deduction.getIllegalProfit()), account.currency),
                        String.format("%s %s", formatter.format(deduction.getIllegalProfitUsd()), USD.getIsoCode()),
                        String.format("%s %s", formatter.format(deduction.getSuggestedDeduction()), account.currency),
                        String.format(
                                "%s %s", formatter.format(deduction.getSuggestedDeductionUsd()), USD.getIsoCode()),
                        String.format("%s %s", formatter.format(deduction.getActualDeduction()), account.currency),
                        String.format("%s %s", formatter.format(deduction.getActualDeductionUsd()), USD.getIsoCode()),
                        deduction
                                .getCreatedAt()
                                .toLocalDateTime()
                                .plusHours(2)
                                .toLocalDate()
                                .toString(),
                        deduction
                                .getCreatedAt()
                                .toLocalDateTime()
                                .plusHours(2)
                                .toLocalTime()
                                .format(DateTimeFormatter.ofPattern("HH:mm")),
                        deduction.getComment(),
                        deduction.getCommentDeduction()));
    }

    @Test
    @Feature("BMS-1667 Filter by status")
    @AllureId("1367")
    @DisplayName("Verify Abuse registry deduction table status filter options")
    void deductionTableStatusFilterOptionsTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(client.getUcid());
        alertsPage.waitForPageToLoad();
        deductionPage.clickAbuseRegistryButton();
        deductionPage.clickDeductionTabButton();
        deductionPage.clickStatusFilter();
        assertThat(
                "Check options in status filter",
                deductionPage.getFilterOptions(),
                contains(
                        NO_DEDUCTION.getDisplayName(),
                        HOLDING.getDisplayName(),
                        TO_BE_DEDUCTED.getDisplayName(),
                        DEDUCTED.getDisplayName(),
                        PROCESSING.getDisplayName(),
                        DEDUCTION_FAILED.getDisplayName()));
    }

    @Test
    @Feature("BMS-1667 Filter by status")
    @AllureId("1368")
    @DisplayName("Verify Abuse registry deduction table email filter options")
    void deductionTableEmailFilterOptionsTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(client.getUcid());
        alertsPage.waitForPageToLoad();
        deductionPage.clickAbuseRegistryButton();
        deductionPage.clickDeductionTabButton();
        deductionPage.clickEmailFilter();
        assertThat(
                "Check options in email filter",
                deductionPage.getFilterOptions(),
                contains(
                        NO_EMAIL_SENT.getDisplayName(),
                        AWAITING_APPROVAL.getDisplayName(),
                        REJECTED.getDisplayName(),
                        APPROVED.getDisplayName(),
                        EMAIL_SENT.getDisplayName()));
    }

    @Test
    @Feature("BMS-1667 Filter by status")
    @AllureId("1369")
    @DisplayName("Verify Abuse registry deduction table filtration by status")
    void deductionTableFilterByStatusTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(client.getUcid());
        alertsPage.waitForPageToLoad();
        deductionPage.clickAbuseRegistryButton();
        deductionPage.clickDeductionTabButton();
        deductionPage.clickStatusFilter();
        deductionPage.clickFilterOptionByText(DEDUCTION_FAILED.getDisplayName());
        deductionPage.waitForPageToLoad();
        assertThat(
                "Check all statuses in the table are as selected",
                deductionPage.getStatusValues(),
                everyItem(is(DEDUCTION_FAILED.getDisplayName())));
    }

    @Test
    @Feature("BMS-1667 Filter by status")
    @AllureId("1370")
    @DisplayName("Verify Abuse registry deduction table filtration by email")
    void deductionTableFilterByEmailTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(client.getUcid());
        alertsPage.waitForPageToLoad();
        deductionPage.clickAbuseRegistryButton();
        deductionPage.clickDeductionTabButton();
        deductionPage.clickEmailFilter();
        deductionPage.clickFilterOptionByText(APPROVED.getDisplayName());
        deductionPage.waitForPageToLoad();
        assertThat(
                "Check all email statuses in the table are as selected",
                deductionPage.getEmailValues(),
                everyItem(is(APPROVED.getDisplayName())));
    }

    @Test
    @Feature("BMS-1667 Filter by brand group")
    @AllureId("1460")
    @DisplayName("Verify Abuse registry deduction table filtration by brand group")
    void deductionTableFilterByBrandTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(client.getUcid());
        alertsPage.waitForPageToLoad();
        deductionPage.clickAbuseRegistryButton();
        deductionPage.clickDeductionTabButton();
        deductionPage.clickBrandsFilter();
        deductionPage.clickFilterOptionByText(client.getBrand());
        deductionPage.waitForPageToLoad();
        assertThat(
                "Check all brand groups in the table are as selected",
                deductionPage.getBrandValues(),
                everyItem(is(client.getBrand())));
    }

    @Test
    @Feature("BMS-1667 Filter by status")
    @AllureId("1371")
    @DisplayName("Verify Abuse registry deduction table filtration by status and email")
    void deductionTableFilterByStatusAndEmailTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(client.getUcid());
        alertsPage.waitForPageToLoad();
        deductionPage.clickAbuseRegistryButton();
        deductionPage.clickDeductionTabButton();
        deductionPage.clickStatusFilter();
        deductionPage.clickFilterOptionByText(DEDUCTION_FAILED.getDisplayName());
        deductionPage.clickEmailFilter();
        deductionPage.clickFilterOptionByText(APPROVED.getDisplayName());
        deductionPage.waitForPageToLoad();
        assertThat(
                "Check all statuses in the table are as selected",
                deductionPage.getStatusValues(),
                everyItem(is(DEDUCTION_FAILED.getDisplayName())));
        assertThat(
                "Check all email statuses in the table are as selected",
                deductionPage.getEmailValues(),
                everyItem(is(APPROVED.getDisplayName())));
    }

    @Test
    @Feature("BMS-3016 Filter Deduction table")
    @AllureId("2095")
    @DisplayName("Verify Abuse registry deduction table filtration by brand in filter drawer")
    void deductionTableFilterBrandTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        deductionPage.navigateDeduction();
        deductionPage.clickFilterButton();
        deductionPage.clickBrandCheckboxInFilterDrawer(client.getBrand());
        deductionPage.clickApplyFilterButton();
        deductionPage.waitForPageToLoad();
        assertThat(
                "Check all brand groups in the table are as selected",
                deductionPage.getBrandValues(),
                everyItem(is(client.getBrand())));
    }

    @Test
    @Feature("BMS-3016 Filter Deduction table")
    @AllureId("2096")
    @DisplayName("Verify Abuse registry deduction table filtration by email stage in filter drawer")
    void deductionTableFilterEmailStageTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        deductionPage.navigateDeduction();
        deductionPage.clickFilterButton();
        deductionPage.clickEmailStageFilterOption(APPROVED.name());
        deductionPage.clickApplyFilterButton();
        deductionPage.waitForPageToLoad();
        assertThat(
                "Check all email statuses in the table are as selected",
                deductionPage.getEmailValues(),
                everyItem(is(APPROVED.getDisplayName())));
    }

    @Test
    @Feature("BMS-3016 Filter Deduction table")
    @AllureId("2097")
    @DisplayName("Verify Abuse registry deduction table filtration by deduction status in filter drawer")
    void deductionTableFilterDeductionStatusTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        deductionPage.navigateDeduction();
        deductionPage.clickFilterButton();
        deductionPage.clickDeductionStatusFilterOption(DEDUCTION_FAILED.name());
        deductionPage.clickApplyFilterButton();
        deductionPage.waitForPageToLoad();
        assertThat(
                "Check all statuses in the table are as selected",
                deductionPage.getStatusValues(),
                everyItem(is(DEDUCTION_FAILED.getDisplayName())));
    }

    @Test
    @Feature("BMS-3016 Filter Deduction table")
    @AllureId("2098")
    @DisplayName("Verify Abuse registry deduction table filtration by creation date in filter drawer")
    void deductionTableFilterCreationDateTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        deductionPage.navigateDeduction();
        deductionPage.clickFilterButton();
        deductionPage.clickCreatedPresetFilterOption("Today");
        deductionPage.clickApplyFilterButton();
        deductionPage.waitForPageToLoad();
        assertThat(
                "Check all created dates in the table are as selected",
                deductionPage.getCreatedColumnValues(),
                everyItem(matchesPattern("^" + getCurrentDate() + "\\s\\d{2}:\\d{2}$")));
    }

    @Test
    @Feature("BMS-3016 Filter Deduction table")
    @AllureId("2099")
    @DisplayName("Verify Abuse registry deduction table filtration by fraud type in filter drawer")
    void deductionTableFilterFraudTypeTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        deductionPage.navigateDeduction();
        deductionPage.clickFilterButton();
        deductionPage.clickFraudTypeCheckboxInFilterDrawer(HEDGING.getCode());
        deductionPage.clickApplyFilterButton();
        deductionPage.waitForPageToLoad();
        assertThat(
                "Check all behavior values in the table are as selected",
                deductionPage.getBehaviorColumnValues(),
                everyItem(containsString(HEDGING.getName())));
    }

    @Test
    @Feature("BMS-3016 Filter Deduction table")
    @AllureId("2100")
    @DisplayName("Verify Abuse registry deduction table amount filters in filter drawer are visible")
    void deductionTableFilterAmountTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        deductionPage.navigateDeduction();
        deductionPage.clickFilterButton();
        assertThat(
                "Check illegal profit filter is visible",
                deductionPage.isIllegalProfitFilterVisibleInDrawer(),
                is(true));
        assertThat(
                "Check suggestion filter is visible",
                deductionPage.isSuggestedDeductionFilterVisibleInDrawer(),
                is(true));
        assertThat(
                "Check deducted amount filter is visible",
                deductionPage.isDeductedAmountFilterVisibleInDrawer(),
                is(true));
    }
}
