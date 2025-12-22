package tests.vindex_backoffice_ui_tests.investigationTool;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateAdditionalCrmTbAccountDataForUi;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountDataForUi;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static business_objects.db.clickhouse.s3_fact_ib_sales_commissions.S3FactIbSalesCommissionsFactory.generateS3FactIbSalesCommissionsClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.DateTimeFormat.DATE;
import static helpers.database.CleanTableHelper.cleanCrmUserTableByClient;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static utils.Constants.*;
import static utils.Utils.*;

import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.mt_account.MtAccountObject;
import business_objects.db.clickhouse.s3_fact_ib_sales_commissions.S3FactIbSalesCommissionsObject;
import helpers.data.ClientHelper;
import helpers.data.enums.DateTimeFormat;
import io.qameta.allure.AllureId;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PaymentsSummaryRebatesReceivedTest extends TestBaseWeb {

    private static final ClientHelper client = getRandomVantageClientAllFields();
    private static final CrmTbUserObject crmTbUser = generateUserByClient(client);
    private static final CrmTbAccountObject account = generateCrmTbAccountDataForUi(client);
    private static final CrmTbAccountObject account2 = generateAdditionalCrmTbAccountDataForUi(client);
    private static final CrmTbAccountObject account3 = generateAdditionalCrmTbAccountDataForUi(client);
    private static final MtAccountObject mtAccount = generateMtAccountByCrmTbAccount(account);
    private static final MtAccountObject mtAccount2 = generateMtAccountByCrmTbAccount(account2);
    private static final MtAccountObject mtAccount3 = generateMtAccountByCrmTbAccount(account2);
    private static S3FactIbSalesCommissionsObject ibCommission1;
    private static S3FactIbSalesCommissionsObject ibCommission2;
    private static S3FactIbSalesCommissionsObject ibCommission3;
    DecimalFormat formatter = new DecimalFormat("#,##0.00");

    @BeforeAll
    static void setup() {
        crmTbUser.registrationDate = getCurrentTimestampMinusOffsetFormatted(DateTimeFormat.DATE, 1, 0, 0, 0, 0);
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        account3.account = getRandomIntPositive();
        mtAccount3.account = account3.account;
        Arrays.asList(account, account2).forEach(a -> a.accountGroup = "rebate");
        Arrays.asList(mtAccount, mtAccount2).forEach(a -> a.accountGroup = "rebate");
        ibCommission1 = generateS3FactIbSalesCommissionsClient(getRandomVantageClientAllFields());
        ibCommission1.setIbRebateAccount(account.account);
        ibCommission2 = generateS3FactIbSalesCommissionsClient(getRandomVantageClientAllFields());
        ibCommission2.setIbRebateAccount(account2.account);
        ibCommission3 = generateS3FactIbSalesCommissionsClient(getRandomVantageClientAllFields());
        ibCommission3.setIbRebateAccount(account2.account);
    }

    @AfterEach
    public void teardownEach() {
        deleteEntryFromDb(
                S3_FACT_IB_SALES_COMMISSIONS,
                String.format("ib_rebate_account IN (%s, %s)", account.account, account2.account));
    }

    @Order(1)
    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1132")
    @DisplayName("Verify Rebates received widget no rebates, no accounts in Payments - Summary")
    public void verifyPaymentsSummaryRebatesReceived1Test() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(client.getUcid());
        alertsPage.waitForPageToLoad();
        paymentsPage.clickPaymentsTabButton();
        assertThat("Verify title", paymentsPage.getRebatesReceivedWidgetTitle(), is("Rebates receivedUSD"));
        assertThat("Verify value", paymentsPage.getRebatesReceivedWidgetValue(), is("Not involved in IB program"));
    }

    @Order(2)
    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1133")
    @DisplayName("Verify Rebates received widget no rebates, 1 account in Payments - Summary")
    public void verifyPaymentsSummaryRebatesReceived2Test() {
        insertCrmAccountsToDb(account);
        insertObjectToDb(MT_ACCOUNT_TABLE_NAME, mtAccount);
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        paymentsPage.clickPaymentsTabButton();
        assertThat("Verify title", paymentsPage.getRebatesReceivedWidgetTitle(), is("Rebates receivedUSD"));
        assertThat("Verify value", paymentsPage.getRebatesReceivedWidgetValue(), is("0"));
        assertThat(
                "Verify counter",
                paymentsPage.getRebatesReceivedWidgetCounter(),
                is(String.format("on %s rebate account", "1")));
    }

    @Order(3)
    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1134")
    @DisplayName("Verify Rebates received widget rebates in Payments - Summary")
    public void verifyPaymentsSummaryRebatesReceived3Test() {
        insertObjectsToDb(CRM_TB_ACCOUNT_TABLE_NAME, List.of(account2, account3));
        insertObjectsToDb(MT_ACCOUNT_TABLE_NAME, List.of(mtAccount2, mtAccount3));
        insertObjectsToDb(S3_FACT_IB_SALES_COMMISSIONS, List.of(ibCommission1, ibCommission2, ibCommission3));
        S3FactIbSalesCommissionsObject commission1 =
                generateS3FactIbSalesCommissionsClient(getRandomVantageClientAllFields());
        commission1.setIbRebateAccount(account.account);
        commission1.setSalesCommission(4214.12);
        commission1.setIbCommission(74.54);
        S3FactIbSalesCommissionsObject commission2 =
                generateS3FactIbSalesCommissionsClient(getRandomVantageClientAllFields());
        commission2.setIbRebateAccount(account2.account);
        commission2.setSalesCommission(8787.43);
        commission2.setIbCommission(9458.44);
        commission2.setDate(getCurrentTimestampMinusOffsetFormatted(DATE, 0, 0, 1, 0, 0));
        S3FactIbSalesCommissionsObject commission3 =
                generateS3FactIbSalesCommissionsClient(getRandomVantageClientAllFields());
        commission3.setIbRebateAccount(account2.account);
        commission3.setSalesCommission(12_342.24);
        commission3.setIbCommission(0.56);
        List<S3FactIbSalesCommissionsObject> commissionsList = List.of(commission1, commission2, commission3);
        insertObjectsToDb(S3_FACT_IB_SALES_COMMISSIONS, commissionsList);
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(client.getUcid());
        alertsPage.waitForPageToLoad();
        paymentsPage.clickPaymentsTabButton();
        assertThat("Verify title", paymentsPage.getRebatesReceivedWidgetTitle(), is("Rebates receivedUSD"));
        String value = formatter.format(commissionsList.stream()
                .mapToDouble(obj -> obj.getSalesCommission() + obj.getIbCommission())
                .sum());
        assertThat("Verify value", paymentsPage.getRebatesReceivedWidgetValue(), is(value));
        assertThat(
                "Verify counter",
                paymentsPage.getRebatesReceivedWidgetCounter(),
                is(String.format("on %s rebate accounts", "2")));
    }

    @AfterAll
    static void teardown() throws Exception {
        cleanCrmUserTableByClient(client.getUcid());
        deleteEntryFromDb(
                S3_FACT_IB_SALES_COMMISSIONS,
                String.format("ib_rebate_account IN (%s, %s)", account.account, account2.account));
    }
}
