package tests.vindex_backoffice_ui_tests.investigationTool;

import business_objects.db.clickhouse.account_ib_relation.AccountIbRelationObject;
import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_extends.CrmTbUserExtendsObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.mt_account.MtAccountObject;
import business_objects.db.clickhouse.s3_fact_cpa_commissions.S3FactCpaCommissionsObject;
import business_objects.db.clickhouse.s3_fact_ib_sales_commissions.S3FactIbSalesCommissionsObject;
import business_objects.db.clickhouse.s3_fact_login_metrics.S3FactLoginMetricsObject;
import business_objects.kafka.alerts.RuleAlert;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.data.ClientHelper;
import helpers.kafka.KafkaHelper;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static business_objects.db.clickhouse.account_ib_relation.AccountIbRelationFactory.generateAccountIbRelationObjectByClient;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountDataForUi;
import static business_objects.db.clickhouse.crm_tb_user_extends.CrmTbUserExtendsObjectFactory.generateCrmTbUserExtendsByClient;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static business_objects.db.clickhouse.s3_fact_cpa_commissions.S3FactCpaCommissionsFactory.generates3FactCpaCommissionsObject;
import static business_objects.db.clickhouse.s3_fact_ib_sales_commissions.S3FactIbSalesCommissionsFactory.generateS3FactIbSalesCommissionsClient;
import static business_objects.db.clickhouse.s3_fact_login_metrics.S3FactLoginMetricsFactory.generateS3FactLoginMetricsClient;
import static business_objects.kafka.alerts.RuleAlertFactory.generateRuleAlertByUcid;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.DateTimeFormat.DATE;
import static helpers.database.BoHelper.closeAlert;
import static helpers.database.CleanTableHelper.cleanCrmUserTableByClient;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Utils.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CpaOverviewChartTest extends TestBaseWeb {

    private static final KafkaHelper kafka = new KafkaHelper();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final ClientHelper client = getRandomVantageClientAllFields();
    private static final ClientHelper ibClient = getRandomVantageClientAllFields();
    private static final CrmTbUserObject crmTbUser = generateUserByClient(client);
    private static final CrmTbUserObject ibCrmTbUser = generateUserByClient(ibClient);
    private static final CrmTbUserExtendsObject crmTbUserExtends = generateCrmTbUserExtendsByClient(client);
    private static final CrmTbAccountObject account = generateCrmTbAccountDataForUi(client);
    private static final CrmTbAccountObject ibAccount = generateCrmTbAccountDataForUi(ibClient);
    private static final MtAccountObject mtAccount = generateMtAccountByCrmTbAccount(account);
    private static final MtAccountObject ibMtAccount = generateMtAccountByCrmTbAccount(ibAccount);
    private static AccountIbRelationObject relation;
    private static S3FactIbSalesCommissionsObject commission1;
    private static S3FactLoginMetricsObject factLoginMetrics1;
    private static S3FactCpaCommissionsObject cpaCommission1;
    private static S3FactLoginMetricsObject factLoginMetrics2;
    private static S3FactCpaCommissionsObject cpaCommission2;
    private static S3FactLoginMetricsObject factLoginMetrics3;
    private static S3FactCpaCommissionsObject cpaCommission3;
    private static S3FactLoginMetricsObject factLoginMetrics4;
    private static S3FactCpaCommissionsObject cpaCommission4;
    private static S3FactLoginMetricsObject factLoginMetrics5;
    private static S3FactCpaCommissionsObject cpaCommission5;
    private static S3FactLoginMetricsObject factLoginMetrics6;
    private static S3FactCpaCommissionsObject cpaCommission6;
    private static S3FactLoginMetricsObject factLoginMetrics7;
    private static S3FactCpaCommissionsObject cpaCommission7;
    private static List<S3FactCpaCommissionsObject> cpaCommissionsList = new ArrayList<>();
    private static List<S3FactLoginMetricsObject> loginMetricsList = new ArrayList<>();
    private static final DecimalFormat formatter = new DecimalFormat("#,###.#");
    private static final String MONTH_DAY_LABEL_PATTERN = "^(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec) \\d{2}$";
    private static final String MONTH_YEAR_LABEL_PATTERN = "^(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec) \\d{4}$";

    @BeforeAll
    public static void setup() throws ReflectiveOperationException, SQLException, JsonProcessingException {
        insertObjectsToDb(CRM_USER_TABLE_NAME, List.of(crmTbUser, ibCrmTbUser));
        insertCrmAccountsToDb(account, ibAccount);
        insertObjectToDb(CRM_TB_USER_EXTENDS_TABLE_NAME, crmTbUserExtends);
        insertObjectsToDb(MT_ACCOUNT_TABLE_NAME, List.of(mtAccount, ibMtAccount));
        // Relations
        relation = generateAccountIbRelationObjectByClient(client);
        relation.setDirectIbRebateAccount(ibAccount.account);
        relation.setRecordDeletedFlag("N");
        relation.setDirectIbLevel(3);
        relation.setDirectIb(ibClient.getUserId());
        insertObjectToDb(ACCOUNT_IB_RELATION_TABLE_NAME, relation);
        // Commissions & metrics
        commission1 = generateS3FactIbSalesCommissionsClient(client);
        commission1.setIbRebateAccount(ibAccount.account);
        commission1.setIbCommission(21_314.45);
        commission1.setDate(getCurrentDate());
        cpaCommission1 = generates3FactCpaCommissionsObject(client);
        cpaCommission1.setDate(getCurrentDate());
        cpaCommission1.setCommission(235.0);
        factLoginMetrics1 = generateS3FactLoginMetricsClient(client);
        factLoginMetrics1.setDate(getCurrentDate());
        factLoginMetrics1.setDailyNetClosedPnl(878.23);
        factLoginMetrics1.setDailyNetDeposit(142.342);
        cpaCommission2 = generates3FactCpaCommissionsObject(client);
        cpaCommission2.setDate(getCurrentTimestampMinusOffsetFormatted(DATE, 0, 0, 1, 0, 0));
        cpaCommission2.setCommission(854.37);
        factLoginMetrics2 = generateS3FactLoginMetricsClient(client);
        factLoginMetrics2.setDate(getCurrentTimestampMinusOffsetFormatted(DATE, 0, 0, 1, 0, 0));
        factLoginMetrics2.setDailyNetClosedPnl(-985.45);
        factLoginMetrics2.setDailyNetDeposit(253.1);
        cpaCommission3 = generates3FactCpaCommissionsObject(client);
        cpaCommission3.setDate(getCurrentTimestampMinusOffsetFormatted(DATE, 0, 0, 2, 0, 0));
        cpaCommission3.setCommission(352.08);
        factLoginMetrics3 = generateS3FactLoginMetricsClient(client);
        factLoginMetrics3.setDate(getCurrentTimestampMinusOffsetFormatted(DATE, 0, 0, 2, 0, 0));
        factLoginMetrics3.setDailyNetClosedPnl(-649.243);
        factLoginMetrics3.setDailyNetDeposit(495.53);
        cpaCommission4 = generates3FactCpaCommissionsObject(client);
        cpaCommission4.setDate(getCurrentTimestampMinusOffsetFormatted(DATE, 0, 0, 3, 0, 0));
        cpaCommission4.setCommission(98.68);
        factLoginMetrics4 = generateS3FactLoginMetricsClient(client);
        factLoginMetrics4.setDate(getCurrentTimestampMinusOffsetFormatted(DATE, 0, 0, 3, 0, 0));
        factLoginMetrics4.setDailyNetClosedPnl(754.68);
        factLoginMetrics4.setDailyNetDeposit(976.745);
        cpaCommission5 = generates3FactCpaCommissionsObject(client);
        cpaCommission5.setDate(getCurrentTimestampMinusOffsetFormatted(DATE, 0, 0, 4, 0, 0));
        cpaCommission5.setCommission(344.22);
        factLoginMetrics5 = generateS3FactLoginMetricsClient(client);
        factLoginMetrics5.setDate(getCurrentTimestampMinusOffsetFormatted(DATE, 0, 0, 4, 0, 0));
        factLoginMetrics5.setDailyNetClosedPnl(453.2);
        factLoginMetrics5.setDailyNetDeposit(35.45);
        insertObjectToDb(S3_FACT_IB_SALES_COMMISSIONS, commission1);
        insertObjectsToDb(S3_FACT_LOGIN_METRICS_TABLE_NAME, List.of(factLoginMetrics1, factLoginMetrics2, factLoginMetrics3, factLoginMetrics4, factLoginMetrics5));
        insertObjectsToDb(S3_FACT_CPA_COMMISSIONS, List.of(cpaCommission1, cpaCommission2, cpaCommission3, cpaCommission4, cpaCommission5));

        RuleAlert alert = generateRuleAlertByUcid(crmTbUser.ucid);
        kafka.produceMessage(alert.alertId, objectMapper.writeValueAsString(alert), KAFKA_TOPIC_ALERTS);
        formatter.setMinimumFractionDigits(0);
        formatter.setMaximumFractionDigits(2);
        formatter.setRoundingMode(RoundingMode.HALF_DOWN);
    }

    @Order(1)
    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1078")
    @DisplayName("Verify CPA overview Chart by days")
    public void verifyCpaOverviewChart1Test() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        generalTab.clickGeneralTabButton();
        generalTab.clickCpaOverviewButton();
        ibCpaOverviewPage.waitForPageToLoad();
        cpaCommissionsList.addAll(List.of(cpaCommission1, cpaCommission2, cpaCommission3, cpaCommission4, cpaCommission5));
        loginMetricsList.addAll(List.of(factLoginMetrics1, factLoginMetrics2, factLoginMetrics3, factLoginMetrics4, factLoginMetrics5));
        Double totalRebates = calculateTotalRebates(cpaCommissionsList);
        Double totalPnl = calculateTotalPnl(loginMetricsList);
        Double totalDeposit = calculateTotalDeposit(loginMetricsList);
        assertThat("Verify CPA overview summary clients performance items", ibCpaOverviewPage.getClientsPerformanceItems(), contains(String.format("%sCPA rebates", formatter.format(totalRebates)), String.format("%sNet PNL", formatter.format(totalPnl)), String.format("%sNet deposit", formatter.format(totalDeposit))));
        assertThat("Verify Y axis label", ibCpaOverviewPage.getChartYAxisLabel(), is("2.5K"));
        assertThat("Verify X axis labels", ibCpaOverviewPage.getChartXAxisLabels(), everyItem(matchesPattern(MONTH_DAY_LABEL_PATTERN)));
    }

    @Order(2)
    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1079")
    @DisplayName("Verify CPA overview Chart by days with empty data")
    public void verifyCpaOverviewChart2Test() {
        cpaCommission6 = generates3FactCpaCommissionsObject(client);
        cpaCommission6.setDate(getCurrentTimestampMinusOffsetFormatted(DATE, 0, 0, 119, 0, 0));
        cpaCommission6.setCommission(2444.87);
        factLoginMetrics6 = generateS3FactLoginMetricsClient(client);
        factLoginMetrics6.setDate(getCurrentTimestampMinusOffsetFormatted(DATE, 0, 0, 119, 0, 0));
        factLoginMetrics6.setDailyNetClosedPnl(-12.123);
        factLoginMetrics6.setDailyNetDeposit(4124.498);
        insertObjectToDb(S3_FACT_CPA_COMMISSIONS, cpaCommission6);
        insertObjectToDb(S3_FACT_LOGIN_METRICS_TABLE_NAME, factLoginMetrics6);

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        generalTab.clickGeneralTabButton();
        generalTab.clickCpaOverviewButton();
        ibCpaOverviewPage.waitForPageToLoad();
        cpaCommissionsList.add(cpaCommission6);
        loginMetricsList.add(factLoginMetrics6);
        Double totalRebates = calculateTotalRebates(cpaCommissionsList);
        Double totalPnl = calculateTotalPnl(loginMetricsList);
        Double totalDeposit = calculateTotalDeposit(loginMetricsList);
        assertThat("Verify CPA overview summary clients performance items", ibCpaOverviewPage.getClientsPerformanceItems(), contains(String.format("%sCPA rebates", formatter.format(totalRebates)), String.format("%sNet PNL", formatter.format(totalPnl)), String.format("%sNet deposit", formatter.format(totalDeposit))));
        assertThat("Verify Y axis label", ibCpaOverviewPage.getChartYAxisLabel(), is("7K"));
        assertThat("Verify X axis labels", ibCpaOverviewPage.getChartXAxisLabels(), everyItem(matchesPattern(MONTH_DAY_LABEL_PATTERN)));
    }

    @Order(3)
    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1080")
    @DisplayName("Verify CPA overview Chart by months")
    public void verifyCpaOverviewChart3Test() {
        cpaCommission7 = generates3FactCpaCommissionsObject(client);
        cpaCommission7.setDate(getCurrentTimestampMinusOffsetFormatted(DATE, 0, 0, 121, 0, 0));
        cpaCommission7.setCommission(5448.56);
        factLoginMetrics7 = generateS3FactLoginMetricsClient(client);
        factLoginMetrics7.setDate(getCurrentTimestampMinusOffsetFormatted(DATE, 0, 0, 121, 0, 0));
        factLoginMetrics7.setDailyNetClosedPnl(-9877.12);
        factLoginMetrics7.setDailyNetDeposit(32_456.654);
        insertObjectToDb(S3_FACT_CPA_COMMISSIONS, cpaCommission7);
        insertObjectToDb(S3_FACT_LOGIN_METRICS_TABLE_NAME, factLoginMetrics7);

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        generalTab.clickGeneralTabButton();
        generalTab.clickCpaOverviewButton();
        ibCpaOverviewPage.waitForPageToLoad();
        cpaCommissionsList.add(cpaCommission7);
        loginMetricsList.add(factLoginMetrics7);
        Double totalRebates = calculateTotalRebates(cpaCommissionsList);
        Double totalPnl = calculateTotalPnl(loginMetricsList);
        Double totalDeposit = calculateTotalDeposit(loginMetricsList);
        assertThat("Verify CPA overview summary clients performance items", ibCpaOverviewPage.getClientsPerformanceItems(), contains(String.format("%sCPA rebates", formatter.format(totalRebates)), String.format("%sNet PNL", formatter.format(totalPnl)), String.format("%sNet deposit", formatter.format(totalDeposit))));
        assertThat("Verify Y axis label", ibCpaOverviewPage.getChartYAxisLabel(), is("45K"));
        assertThat("Verify X axis labels", ibCpaOverviewPage.getChartXAxisLabels(), everyItem(matchesPattern(MONTH_YEAR_LABEL_PATTERN)));
    }

    private static Double calculateTotalRebates(List<S3FactCpaCommissionsObject> commissionsList) {
        return commissionsList.stream().map(S3FactCpaCommissionsObject::getCommission).map(value -> BigDecimal.valueOf(value).setScale(2, RoundingMode.DOWN).doubleValue()).mapToDouble(Double::doubleValue).sum();
    }

    private static Double calculateTotalPnl(List<S3FactLoginMetricsObject> loginMetricsList) {
        return loginMetricsList.stream().map(S3FactLoginMetricsObject::getDailyNetClosedPnl).map(value -> BigDecimal.valueOf(value).setScale(2, RoundingMode.DOWN).doubleValue()).mapToDouble(Double::doubleValue).sum();
    }

    private static Double calculateTotalDeposit(List<S3FactLoginMetricsObject> loginMetricsList) {
        return loginMetricsList.stream().map(S3FactLoginMetricsObject::getDailyNetDeposit).map(value -> BigDecimal.valueOf(value).setScale(2, RoundingMode.DOWN).doubleValue()).mapToDouble(Double::doubleValue).sum();
    }

    @AfterAll
    public static void teardown() throws Exception {
        cleanCrmUserTableByClient(crmTbUser.ucid, ibCrmTbUser.ucid);
        deleteEntryFromDb(CRM_TB_USER_EXTENDS_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        deleteEntryFromDb(ACCOUNT_IB_RELATION_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        deleteEntryFromDb(S3_FACT_IB_SALES_COMMISSIONS, String.format("ucid = '%s'", client.getUcid()));
        deleteEntryFromDb(S3_FACT_LOGIN_METRICS_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        deleteEntryFromDb(S3_FACT_CPA_COMMISSIONS, String.format("ucid = '%s'", client.getUcid()));
        closeAlert(crmTbUser.ucid);
    }
}
