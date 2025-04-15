package tests.vindex_backoffice_ui_tests;

import business_objects.db.clickhouse.account_ib_relation.AccountIbRelationObject;
import business_objects.db.clickhouse.account_ib_relation_snapshot.AccountIbRelationSnapshotObject;
import business_objects.db.clickhouse.client_fraud_types.ClientFraudTypes;
import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.mtAccount.MtAccountObject;
import business_objects.db.clickhouse.s3_fact_ib_sales_commissions.S3FactIbSalesCommissionsObject;
import business_objects.db.clickhouse.s3_fact_login_metrics.S3FactLoginMetricsObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import helpers.data.ClientHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

import java.math.RoundingMode;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Stream;

import static business_objects.db.clickhouse.account_ib_relation.AccountIbRelationFactory.generateAccountIbRelationObjectByClient;
import static business_objects.db.clickhouse.account_ib_relation_snapshot.AccountIbRelationSnapshotFactory.generateAccountIbRelationSnapshotObjectByClient;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountDataForUi;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.mtAccount.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static business_objects.db.clickhouse.s3_fact_ib_sales_commissions.S3FactIbSalesCommissionsFactory.generateS3FactIbSalesCommissionsClient;
import static business_objects.db.clickhouse.s3_fact_login_metrics.S3FactLoginMetricsFactory.generateS3FactLoginMetricsClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.CleanTableHelper.cleanCrmUserTableByClient;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Utils.*;

public class IbOverviewSummaryTest extends TestBaseWeb {

    private static final ClientHelper client = getRandomVantageClientAllFields();
    private static final ClientHelper ibClient = getRandomVantageClientAllFields();
    private static final ClientHelper ibClient1 = getRandomVantageClientAllFields();
    private static final ClientHelper ibClient2 = getRandomVantageClientAllFields();
    private static final ClientHelper ibClient3 = getRandomVantageClientAllFields();
    private static final CrmTbUserObject crmTbUser = generateUserByClient(client);
    private static final CrmTbUserObject ibCrmTbUser = generateUserByClient(ibClient);
    private static final CrmTbAccountObject account = generateCrmTbAccountDataForUi(client);
    private static final CrmTbAccountObject ibAccount = generateCrmTbAccountDataForUi(ibClient);
    private static final MtAccountObject mtAccount = generateMtAccountByCrmTbAccount(account);
    private static final MtAccountObject ibMtAccount = generateMtAccountByCrmTbAccount(ibAccount);
    private static AccountIbRelationObject relation;
    private static AccountIbRelationSnapshotObject relationSnapshot1;
    private static AccountIbRelationSnapshotObject relationSnapshot2;
    private static AccountIbRelationSnapshotObject relationSnapshot3;
    private static AccountIbRelationSnapshotObject relationSnapshot4;
    private static S3FactIbSalesCommissionsObject commission;
    private static S3FactIbSalesCommissionsObject commission3;
    private static S3FactLoginMetricsObject factLoginMetrics;
    private static S3FactLoginMetricsObject factLoginMetrics3;
    private static final DecimalFormat formatter = new DecimalFormat("#,###.#");
    private static final DecimalFormat formatterTable = new DecimalFormat("#,###");

    @BeforeAll
    public static void setup() throws ReflectiveOperationException, SQLException, JsonProcessingException {
        insertObjectsToDb(CRM_USER_TABLE_NAME, List.of(crmTbUser, ibCrmTbUser));
        insertObjectsToDb(CRM_ACCOUNT_TABLE_NAME, List.of(account, ibAccount));
        insertObjectsToDb(MT_ACCOUNT_TABLE_NAME, List.of(mtAccount, ibMtAccount));
        // Relations
        relation = generateAccountIbRelationObjectByClient(client);
        relation.setDirectIbRebateAccount(ibAccount.account);
        relation.setRecordDeletedFlag("N");
        relation.setDirectIbLevel(3);
        relation.setDirectIb(ibClient.getUserId());
        insertObjectToDb(ACCOUNT_IB_RELATION_TABLE_NAME, relation);
        // Commissions
        commission = generateS3FactIbSalesCommissionsClient(client);
        commission.setIbRebateAccount(ibAccount.account);
        commission.setIbCommission(561.78);
        commission.setDate(getCurrentDate());
        commission3 = generateS3FactIbSalesCommissionsClient(ibClient3);
        commission3.setIbRebateAccount(ibClient3.getTradingAccount());
        commission3.setIbCommission(6464.33);
        commission3.setDate(getCurrentDate());
        insertObjectsToDb(S3_FACT_IB_SALES_COMMISSIONS, List.of(commission, commission3));
        // Metrics
        factLoginMetrics = generateS3FactLoginMetricsClient(client);
        factLoginMetrics.setDate(getCurrentDate());
        factLoginMetrics.setDailyNetClosedPnl(235_125.1);
        factLoginMetrics.setDailyNetDeposit(4344.98);
        factLoginMetrics.setDailyTradingVolIn(473_277_453.4);
        factLoginMetrics.setDailyTradingVolOut(92_538_898.01);
        factLoginMetrics.setDailyGrossClientPnl(-3525.7);
        factLoginMetrics.setEquity(698_467.0);
        factLoginMetrics.setDailyDeposit(432.1);
        factLoginMetrics.setDailyWithdraw(5646.999);
        factLoginMetrics3 = generateS3FactLoginMetricsClient(ibClient3);
        factLoginMetrics3.setDate(getCurrentDate());
        factLoginMetrics3.setDailyNetDeposit(3434.86);
        factLoginMetrics3.setDailyGrossClientPnl(10_553.87);
        insertObjectsToDb(S3_FACT_LOGIN_METRICS_TABLE_NAME, List.of(factLoginMetrics, factLoginMetrics3));
        // Frauds
        ClientFraudTypes fraud = new ClientFraudTypes(client.getUcid(), "HEDGING", "VINDEX", 0, getCurrentTimestampDbFormat());
        insertObjectToDb(CLIENT_FRAUD_TYPES_TABLE_NAME, fraud);
        // Relation snapshots
        relationSnapshot1 = generateAccountIbRelationSnapshotObjectByClient(client);
        relationSnapshot1.setDirectIb(ibClient.getUserId());
        relationSnapshot1.setDirectIbRebateAccount(ibAccount.account);
        relationSnapshot1.setDirectIbLevel(1);
        relationSnapshot2 = generateAccountIbRelationSnapshotObjectByClient(ibClient);
        relationSnapshot2.setDirectIb(ibClient1.getUserId());
        relationSnapshot2.setDirectIbRebateAccount(ibClient1.getTradingAccount());
        relationSnapshot2.setDirectIbLevel(1);
        relationSnapshot3 = generateAccountIbRelationSnapshotObjectByClient(ibClient2);
        relationSnapshot3.setDirectIb(client.getUserId());
        relationSnapshot3.setDirectIbRebateAccount(account.account);
        relationSnapshot3.setDirectIbLevel(2);
        relationSnapshot3.setIsRebateAccount(0);
        relationSnapshot4 = generateAccountIbRelationSnapshotObjectByClient(ibClient3);
        relationSnapshot4.setDirectIb(client.getUserId());
        relationSnapshot4.setDirectIbRebateAccount(account.account);
        relationSnapshot4.setDirectIbLevel(2);
        relationSnapshot4.setIsRebateAccount(0);
        insertObjectsToDb(ACCOUNT_IB_RELATION_SNAPSHOT_TABLE_NAME, List.of(relationSnapshot1, relationSnapshot2, relationSnapshot3, relationSnapshot4));

        formatter.setMinimumFractionDigits(0);
        formatter.setMaximumFractionDigits(2);
        formatter.setRoundingMode(RoundingMode.DOWN);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1034")
    @DisplayName("Verify IB overview Summary")
    public void verifyIbOverviewSummaryTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        generalTab.clickGeneralTabButton();
        generalTab.clickIbOverviewButton();
        ibCpaOverviewPage.waitForPageToLoad();
        assertThat("Verify IB overview summary title", ibCpaOverviewPage.getOverviewTitle(), is("IB overview"));
        assertThat("Verify IB overview summary subheader", ibCpaOverviewPage.getOverviewSubheaderText(), is(String.format("IB %s, %s, %s level", ibAccount.account, ibAccount.brand, relation.getDirectIbLevel())));
        assertThat("Verify IB overview summary under this ib title", ibCpaOverviewPage.getUnderThisTitle(), is("Under this IB"));
        assertThat("Verify IB overview summary under this ib items", ibCpaOverviewPage.getUnderThisItems(), contains(String.format("%sclient", 1), String.format("%sfraudster", 1), String.format("%slower-level IB", 2)));
        assertThat("Verify IB overview summary clients performance title", ibCpaOverviewPage.getClientsPerformanceTitle(), is("Clients performance USD"));
        assertThat("Verify IB overview summary clients performance items", ibCpaOverviewPage.getClientsPerformanceItems(), contains(String.format("%sIB rebates", formatter.format(commission.getIbCommission())), String.format("%sNet PNL", formatter.format(factLoginMetrics.getDailyNetClosedPnl())), String.format("%sNet deposit", formatter.format(factLoginMetrics.getDailyNetDeposit()))));
        assertThat("Verify IB overview summary clients totals title", ibCpaOverviewPage.getClientsTotalsTitle(), is("Clients totals USD"));
        assertThat("Verify IB overview summary clients totals items", ibCpaOverviewPage.getClientsTotalsItems(), contains(String.format("%sVolume", formatter.format((factLoginMetrics.getDailyTradingVolIn() + factLoginMetrics.getDailyTradingVolOut()) / 1_000_000)), String.format("%sProfit", formatter.format(factLoginMetrics.getDailyGrossClientPnl())), String.format("%sEquity", formatter.format(factLoginMetrics.getEquity())), String.format("%sDeposit", formatter.format(factLoginMetrics.getDailyDeposit())), String.format("%sWithdrawal", formatter.format(factLoginMetrics.getDailyWithdraw()))));
    }

    @Feature("BMS-830 Lower-level IB")
    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1129")
    @DisplayName("Verify IB overview Lower-level IB")
    public void verifyIbOverviewLowerLevelIbTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        generalTab.clickGeneralTabButton();
        generalTab.clickIbOverviewButton();
        ibCpaOverviewPage.waitForPageToLoad();
        ibCpaOverviewPage.clickLowerLevelIbTab();
        ibCpaOverviewPage.waitForPageToLoad();
        assertThat("Verify table headers", ibCpaOverviewPage.getLowerLevelIbTableHeaders(), containsInAnyOrder("LVL", "IB", "REBATES", "CLIENTS", "NET PNL", "NET DEPOSIT"));
        List<String> row1Data = List.of("1", ibAccount.account.toString(), formatterTable.format(commission.getIbCommission()), "1", formatterTable.format(factLoginMetrics.getDailyGrossClientPnl() + commission.getIbCommission()), formatterTable.format(factLoginMetrics.getDailyNetDeposit()));
        List<String> row2Data = List.of("2", account.account.toString(), formatterTable.format(commission3.getIbCommission()), "2", formatterTable.format(factLoginMetrics3.getDailyGrossClientPnl() + commission3.getIbCommission()), formatterTable.format(factLoginMetrics3.getDailyNetDeposit()));
        assertThat("Verify table 1st row", ibCpaOverviewPage.getLowerLevelIbAllRowsData(), containsInAnyOrder(Stream.of(row1Data, row2Data).flatMap(List::stream).toList().toArray()));
    }

    @AfterAll
    public static void teardown() throws Exception {
        cleanCrmUserTableByClient(crmTbUser.ucid, ibCrmTbUser.ucid);
        deleteEntryFromDb(ACCOUNT_IB_RELATION_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        deleteEntryFromDb(S3_FACT_IB_SALES_COMMISSIONS, String.format("ucid = '%s'", client.getUcid()));
        deleteEntryFromDb(S3_FACT_LOGIN_METRICS_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        deleteEntryFromDb(CLIENT_FRAUD_TYPES_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        deleteEntryFromDb(ACCOUNT_IB_RELATION_SNAPSHOT_TABLE_NAME, String.format("ucid IN ('%s', '%s', '%s', '%s')", relationSnapshot1.getUcid(), relationSnapshot2.getUcid(), relationSnapshot3.getUcid(), relationSnapshot4.getUcid()));
    }
}
