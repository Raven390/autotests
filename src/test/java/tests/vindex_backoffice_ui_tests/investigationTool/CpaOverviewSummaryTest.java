package tests.vindex_backoffice_ui_tests.investigationTool;

import business_objects.db.clickhouse.account_ib_relation.AccountIbRelationObject;
import business_objects.db.clickhouse.client_fraud_types.ClientFraudTypes;
import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_extends.CrmTbUserExtendsObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.mtAccount.MtAccountObject;
import business_objects.db.clickhouse.s3_fact_cpa_commissions.S3FactCpaCommissionsObject;
import business_objects.db.clickhouse.s3_fact_ib_sales_commissions.S3FactIbSalesCommissionsObject;
import business_objects.db.clickhouse.s3_fact_login_metrics.S3FactLoginMetricsObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import helpers.data.ClientHelper;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

import java.math.RoundingMode;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;

import static business_objects.db.clickhouse.account_ib_relation.AccountIbRelationFactory.generateAccountIbRelationObjectByClient;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountDataForUi;
import static business_objects.db.clickhouse.crm_tb_user_extends.CrmTbUserExtendsObjectFactory.generateCrmTbUserExtendsByClient;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.mtAccount.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static business_objects.db.clickhouse.s3_fact_cpa_commissions.S3FactCpaCommissionsFactory.generates3FactCpaCommissionsObject;
import static business_objects.db.clickhouse.s3_fact_ib_sales_commissions.S3FactIbSalesCommissionsFactory.generateS3FactIbSalesCommissionsClient;
import static business_objects.db.clickhouse.s3_fact_login_metrics.S3FactLoginMetricsFactory.generateS3FactLoginMetricsClient;
import static helpers.data.ClientFactory.getRandomUltimaMarketsClientAllFields;
import static helpers.database.CleanTableHelper.cleanCrmUserTableByClient;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static utils.Constants.*;
import static utils.Utils.getCurrentDate;
import static utils.Utils.getCurrentTimestampDbFormat;

public class CpaOverviewSummaryTest extends TestBaseWeb {

    private static final ClientHelper client = getRandomUltimaMarketsClientAllFields();
    private static final ClientHelper ibClient = getRandomUltimaMarketsClientAllFields();
    private static final CrmTbUserObject crmTbUser = generateUserByClient(client);
    private static final CrmTbUserObject ibCrmTbUser = generateUserByClient(ibClient);
    private static final CrmTbUserExtendsObject crmTbUserExtends = generateCrmTbUserExtendsByClient(client);
    private static final CrmTbAccountObject account = generateCrmTbAccountDataForUi(client);
    private static final CrmTbAccountObject ibAccount = generateCrmTbAccountDataForUi(ibClient);
    private static final MtAccountObject mtAccount = generateMtAccountByCrmTbAccount(account);
    private static final MtAccountObject ibMtAccount = generateMtAccountByCrmTbAccount(ibAccount);
    private static AccountIbRelationObject relation;
    private static S3FactIbSalesCommissionsObject commission;
    private static S3FactLoginMetricsObject factLoginMetrics;
    private static S3FactCpaCommissionsObject cpaCommission;
    private static final DecimalFormat formatter = new DecimalFormat("#,###.#");

    @BeforeAll
    public static void setup() throws ReflectiveOperationException, SQLException, JsonProcessingException {
        insertObjectsToDb(CRM_USER_TABLE_NAME, List.of(crmTbUser, ibCrmTbUser));
        insertObjectsToDb(CRM_ACCOUNT_TABLE_NAME, List.of(account, ibAccount));
        insertObjectToDb(CRM_TB_USER_EXTENDS_TABLE_NAME, crmTbUserExtends);
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
        insertObjectToDb(S3_FACT_IB_SALES_COMMISSIONS, commission);
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
        insertObjectToDb(S3_FACT_LOGIN_METRICS_TABLE_NAME, factLoginMetrics);
        // Frauds
        ClientFraudTypes fraud = new ClientFraudTypes(client.getUcid(), "HEDGING", "VINDEX", 0, getCurrentTimestampDbFormat());
        insertObjectToDb(CLIENT_FRAUD_TYPES_TABLE_NAME, fraud);
        // CPA commissions
        cpaCommission = generates3FactCpaCommissionsObject(client);
        cpaCommission.setDate(getCurrentDate());
        cpaCommission.setCommission(23_525.23);
        insertObjectToDb(S3_FACT_CPA_COMMISSIONS, cpaCommission);

        formatter.setMinimumFractionDigits(0);
        formatter.setMaximumFractionDigits(2);
        formatter.setRoundingMode(RoundingMode.DOWN);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1077")
    @DisplayName("Verify CPA overview Summary")
    public void verifyCpaOverviewSummaryTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        generalTab.clickGeneralTabButton();
        generalTab.clickCpaOverviewButton();
        ibCpaOverviewPage.waitForPageToLoad();
        assertThat("Verify CPA overview summary title", ibCpaOverviewPage.getOverviewTitle(), is("CPA overview"));
        assertThat("Verify CPA overview summary subheader", ibCpaOverviewPage.getOverviewSubheaderText(), is(String.format("CPA %s, %s", client.getCpaId(), ibAccount.brand)));
        assertThat("Verify CPA overview summary under this ib title", ibCpaOverviewPage.getUnderThisTitle(), is("Under this CPA"));
        assertThat("Verify CPA overview summary under this ib items", ibCpaOverviewPage.getUnderThisItems(), contains(String.format("%sclient", 1), String.format("%sfraudster", 1)));
        assertThat("Verify CPA overview summary clients performance title", ibCpaOverviewPage.getClientsPerformanceTitle(), is("Clients performance USD"));
        assertThat("Verify CPA overview summary clients performance items", ibCpaOverviewPage.getClientsPerformanceItems(), contains(String.format("%sCPA rebates", formatter.format(cpaCommission.getCommission())), String.format("%sNet PNL", formatter.format(factLoginMetrics.getDailyNetClosedPnl())), String.format("%sNet deposit", formatter.format(factLoginMetrics.getDailyNetDeposit()))));
        assertThat("Verify CPA overview summary clients totals title", ibCpaOverviewPage.getClientsTotalsTitle(), is("Clients totals USD"));
        assertThat("Verify CPA overview summary clients totals items", ibCpaOverviewPage.getClientsTotalsItems(), contains(String.format("%sVolume", formatter.format((factLoginMetrics.getDailyTradingVolIn() + factLoginMetrics.getDailyTradingVolOut()) / 1_000_000)), String.format("%sProfit", formatter.format(factLoginMetrics.getDailyGrossClientPnl())), String.format("%sEquity", formatter.format(factLoginMetrics.getEquity())), String.format("%sDeposit", formatter.format(factLoginMetrics.getDailyDeposit())), String.format("%sWithdrawal", formatter.format(factLoginMetrics.getDailyWithdraw())), String.format("%sIB rebates", formatter.format(commission.getIbCommission()))));
    }


    @AfterAll
    public static void teardown() throws Exception {
        cleanCrmUserTableByClient(crmTbUser.ucid, ibCrmTbUser.ucid);
        deleteEntryFromDb(CRM_TB_USER_EXTENDS_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        deleteEntryFromDb(ACCOUNT_IB_RELATION_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        deleteEntryFromDb(S3_FACT_IB_SALES_COMMISSIONS, String.format("ucid = '%s'", client.getUcid()));
        deleteEntryFromDb(S3_FACT_LOGIN_METRICS_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        deleteEntryFromDb(CLIENT_FRAUD_TYPES_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        deleteEntryFromDb(S3_FACT_CPA_COMMISSIONS, String.format("ucid = '%s'", client.getUcid()));
    }
}
