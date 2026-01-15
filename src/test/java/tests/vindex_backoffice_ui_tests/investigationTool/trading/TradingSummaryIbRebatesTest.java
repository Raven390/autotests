package tests.vindex_backoffice_ui_tests.investigationTool.trading;

import static business_objects.db.clickhouse.account_ib_relation.AccountIbRelationFactory.generateAccountIbRelationObjectByClient;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.*;
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

import business_objects.db.clickhouse.account_ib_relation.AccountIbRelationObject;
import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.mt_account.MtAccountObject;
import business_objects.db.clickhouse.s3_fact_ib_sales_commissions.S3FactIbSalesCommissionsObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import helpers.data.ClientHelper;
import io.qameta.allure.AllureId;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TradingSummaryIbRebatesTest extends TestBaseWeb {

    private static final ClientHelper client = getRandomVantageClientAllFields();
    private static final CrmTbUserObject crmTbUser = generateUserByClient(client);
    private static final CrmTbAccountObject account = generateCrmTbAccountDataForUi(client);
    private static final CrmTbAccountObject account2 = generateAdditionalCrmTbAccountDataForUi(client);
    private static final MtAccountObject mtAccount = generateMtAccountByCrmTbAccount(account);
    private static final MtAccountObject mtAccount2 = generateMtAccountByCrmTbAccount(account2);
    private static final Integer rebateAccount1 = getRandomIntPositive();
    private static final Integer rebateAccount2 = getRandomIntPositive();
    private static final Integer rebateAccount3 = getRandomIntPositive();
    private static AccountIbRelationObject relation1;
    private static AccountIbRelationObject relation2;
    private static AccountIbRelationObject relation3;
    DecimalFormat formatter = new DecimalFormat("#,##0.00");

    @BeforeAll
    public static void setup() throws ReflectiveOperationException, SQLException, JsonProcessingException {
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        relation1 = generateAccountIbRelationObjectByClient(client);
        relation1.setDirectIbRebateAccount(rebateAccount1);
        relation2 = generateAccountIbRelationObjectByClient(client);
        relation2.setDirectIbRebateAccount(rebateAccount2);
        relation3 = generateAccountIbRelationObjectByClient(client);
        relation3.setAccount(Long.valueOf(account2.account));
        relation3.setServerId(account2.serverIdSt);
        relation3.setDirectIbRebateAccount(rebateAccount3);
    }

    @AfterEach
    public void teardownEach() {
        deleteObjectFromDb(S3_FACT_IB_SALES_COMMISSIONS, String.format("ucid = '%s'", client.getUcid()));
    }

    @Order(1)
    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1029")
    @DisplayName("Verify IB rebates widget no rebates, no accounts in Trading - Summary")
    public void verifyTradingSummaryIbRebates1Test() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        tradingPage.openTradingTab();
        tradingPage.openSummaryTab();
        assertThat("Verify title", tradingPage.getIbRebatesWidgetTitle(), is("IB rebates USD"));
        assertThat("Verify text", tradingPage.getIbRebatesWidgetText(), is("Not involved in IB program"));
    }

    @Order(2)
    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1030")
    @DisplayName("Verify IB rebates widget no rebates, 1 account in Trading - Summary")
    public void verifyTradingSummaryIbRebates2Test() {
        insertCrmAccountsToDb(account);
        insertObjectToDb(MT_ACCOUNT_TABLE_NAME, mtAccount);
        insertObjectToDb(ACCOUNT_IB_RELATION_TABLE_NAME, relation1);
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        tradingPage.openTradingTab();
        tradingPage.openSummaryTab();
        assertThat("Verify title", tradingPage.getIbRebatesWidgetTitle(), is("IB rebates USD"));
        assertThat("Verify value", tradingPage.getIbRebatesWidgetValue(), is("0"));
        assertThat("Verify info", tradingPage.getIbRebatesWidgetInfo(), is(String.format("on %s rebate account", "1")));
    }

    @Order(3)
    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1031")
    @DisplayName("Verify IB rebates widget rebates in Trading - Summary")
    public void verifyTradingSummaryIbRebates3Test() {
        insertCrmAccountsToDb(account2);
        insertObjectToDb(MT_ACCOUNT_TABLE_NAME, mtAccount2);
        insertObjectToDb(ACCOUNT_IB_RELATION_TABLE_NAME, relation2);
        insertObjectToDb(ACCOUNT_IB_RELATION_TABLE_NAME, relation3);
        S3FactIbSalesCommissionsObject commission1 = generateS3FactIbSalesCommissionsClient(client);
        commission1.setIbRebateAccount(rebateAccount1);
        commission1.setSalesCommission(12.34);
        commission1.setIbCommission(56.789);
        S3FactIbSalesCommissionsObject commission2 = generateS3FactIbSalesCommissionsClient(client);
        commission2.setIbRebateAccount(rebateAccount1);
        commission2.setSalesCommission(54.4);
        commission2.setIbCommission(32.2);
        commission2.setDate(getCurrentTimestampMinusOffsetFormatted(DATE, 0, 0, 1, 0, 0));
        S3FactIbSalesCommissionsObject commission3 = generateS3FactIbSalesCommissionsClient(client);
        commission3.setIbRebateAccount(rebateAccount2);
        commission3.setSalesCommission(723.12);
        commission3.setIbCommission(89.1);
        S3FactIbSalesCommissionsObject commission4 = generateS3FactIbSalesCommissionsClient(client);
        commission4.setIbRebateAccount(rebateAccount2);
        commission4.setSalesCommission(768.31);
        commission4.setIbCommission(87.1);
        commission4.setDate(getCurrentTimestampMinusOffsetFormatted(DATE, 0, 0, 2, 0, 0));
        S3FactIbSalesCommissionsObject commission5 = generateS3FactIbSalesCommissionsClient(client);
        commission5.setAccount(account2.account);
        commission5.setServerId(account2.serverIdSt);
        commission5.setIbRebateAccount(rebateAccount3);
        commission5.setSalesCommission(723.12);
        commission5.setIbCommission(89.1);
        S3FactIbSalesCommissionsObject commission6 = generateS3FactIbSalesCommissionsClient(client);
        commission6.setAccount(account2.account);
        commission6.setServerId(account2.serverIdSt);
        commission6.setIbRebateAccount(rebateAccount3);
        commission6.setSalesCommission(768.31);
        commission6.setIbCommission(87.1);
        commission6.setDate(getCurrentTimestampMinusOffsetFormatted(DATE, 0, 0, 3, 0, 0));
        List<S3FactIbSalesCommissionsObject> commissionsList =
                List.of(commission1, commission2, commission3, commission4, commission5, commission6);
        insertObjectsToDb(S3_FACT_IB_SALES_COMMISSIONS, commissionsList);
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        tradingPage.openTradingTab();
        tradingPage.openSummaryTab();
        assertThat("Verify title", tradingPage.getIbRebatesWidgetTitle(), is("IB rebates USD"));
        String value = formatter.format(commissionsList.stream()
                .mapToDouble(obj -> obj.getSalesCommission() + obj.getIbCommission())
                .sum());
        assertThat("Verify value", tradingPage.getIbRebatesWidgetValue(), is(value));
        assertThat(
                "Verify info", tradingPage.getIbRebatesWidgetInfo(), is(String.format("on %s rebate accounts", "3")));
    }

    @AfterAll
    public static void teardown() throws Exception {
        cleanCrmUserTableByClient(crmTbUser.ucid);
        deleteObjectFromDb(ACCOUNT_IB_RELATION_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        deleteObjectFromDb(S3_FACT_IB_SALES_COMMISSIONS, String.format("ucid = '%s'", client.getUcid()));
    }
}
