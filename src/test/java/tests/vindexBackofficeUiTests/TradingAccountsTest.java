package tests.vindexBackofficeUiTests;

import businessObjects.db.clickhouse.accountIbRelation.accountIbRelationObject;
import businessObjects.db.clickhouse.crmTbAccount.CrmTbAccountObject;
import businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObject;
import businessObjects.db.clickhouse.mtAccount.MtAccountObject;
import businessObjects.db.clickhouse.s3FactIbSalesCommissions.s3FactIbSalesCommissionsObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import helpers.data.ClientHelper;
import helpers.data.enums.Brand;
import helpers.data.enums.Regulator;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

import java.sql.SQLException;

import static businessObjects.db.clickhouse.accountIbRelation.AccountIbRelationFactory.generateAccountIbRelationObjectByClient;
import static businessObjects.db.clickhouse.crmTbAccount.CrmTbAccountObjectFactory.*;
import static businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObjectFactory.generateStaticUserByClient;
import static businessObjects.db.clickhouse.mtAccount.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static businessObjects.db.clickhouse.s3FactIbSalesCommissions.s3FactIbSalesCommissionsFactory.generateS3FactIbSalesCommissionsClient;
import static helpers.database.DbHelper.*;
import static utils.Constants.*;
import static utils.Utils.getRandomRoundedDouble;

public class TradingAccountsTest extends TestBaseWeb {

    private static final ClientHelper client = new ClientHelper(212_101, "d555fa11-3e45-44d3-8070-e28eaff997c7", Brand.INFINOX, Regulator.VFSC2, 212_101_001, 42);
    private static final CrmTbUserObject crmTbUser = generateStaticUserByClient(client);
    private static CrmTbAccountObject account1;
    private static MtAccountObject mtAccount1;
    private static accountIbRelationObject relation;
    private static s3FactIbSalesCommissionsObject commission;


    @BeforeAll
    public static void setup() throws ReflectiveOperationException, SQLException, JsonProcessingException,
            InterruptedException {
        deleteObjectFromDb(ACCOUNT_IB_RELATION_TABLE_NAME, "ucid ='" + client.getUcid() + "'");
        deleteObjectFromDb(S2_FACT_IB_SALES_COMMISSIONS, "ucid ='" + client.getUcid() + "'");
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        account1 = generateStaticCrmTbAccountActive(client);
        insertObjectToDb(CRM_ACCOUNT_TABLE_NAME, account1);
        mtAccount1 = generateMtAccountByCrmTbAccount(account1);
        insertObjectToDb(MT_ACCOUNT_TABLE_NAME, mtAccount1);
        relation = generateAccountIbRelationObjectByClient(client);
        relation.setDirectIbRebateAccount(202_007_001);
        insertObjectToDb(ACCOUNT_IB_RELATION_TABLE_NAME, relation);
        commission = generateS3FactIbSalesCommissionsClient(client);
        commission.setIbRebateAccount(relation.getDirectIbRebateAccount());
        commission.setSalesCommission(getRandomRoundedDouble(0.00, 5_000_000.00));
        commission.setIbCommission(getRandomRoundedDouble(0.00, 5_000_000.00));
        insertObjectToDb(S2_FACT_IB_SALES_COMMISSIONS, commission);

    }

    @Test
    @AllureId("1020")
    @Feature("BMS-744 Display IB info in account card and table")
    @DisplayName("Trading/Account. User can see IB account in clients account info")
    public void ibInfoDisplayedOnCardTest() {
        Allure.step("create test data for IB relation and commissions with one account for the test account");
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(client.getUcid());
        alertsPage.waitForPageToLoad();
        tradingPage.openTradingTab();
        tradingPage.openAccountsTab();
        tradingPage.checkIbAccountValueCard(client.getTradingAccount(), relation.getDirectIbRebateAccount());
        tradingPage.checkIbRebatesValueCard(client.getTradingAccount(), (commission.getSalesCommission() + commission.getIbCommission()));
        tradingPage.clickTableViewButton();
        tradingPage.checkIbAccountValueTable(client.getTradingAccount(), relation.getDirectIbRebateAccount());
        tradingPage.checkIbRebatesValueTable(client.getTradingAccount(), (commission.getSalesCommission() + commission.getIbCommission()));
    }

    @Test
    @AllureId("1021")
    @Feature("BMS-744 Display IB info in account card and table")
    @DisplayName("Trading/Account. User can see IB account in clients account info more than one account")
    public void ibInfoDisplayedOnCardMultipleTest() {
        Allure.step("create test data for IB relation and commissions with more than one account for the test account");
        accountIbRelationObject relation2 = generateAccountIbRelationObjectByClient(client);
        relation2.setDirectIbRebateAccount(202_007_002);
        insertObjectToDb(ACCOUNT_IB_RELATION_TABLE_NAME, relation2);
        s3FactIbSalesCommissionsObject commission2 = generateS3FactIbSalesCommissionsClient(client);
        commission2.setIbRebateAccount(relation2.getDirectIbRebateAccount());
        commission2.setSalesCommission(getRandomRoundedDouble(0.00, 5_000_000.00));
        commission2.setIbCommission(getRandomRoundedDouble(0.00, 5_000_000.00));
        insertObjectToDb(S2_FACT_IB_SALES_COMMISSIONS, commission2);
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(client.getUcid());
        alertsPage.waitForPageToLoad();
        tradingPage.openTradingTab();
        tradingPage.openAccountsTab();
        tradingPage.checkIbAccountValueCard(client.getTradingAccount(), relation.getDirectIbRebateAccount(), 2);
        tradingPage.checkIbAccountValueCard(client.getTradingAccount(), relation2.getDirectIbRebateAccount(), 1);
        tradingPage.checkIbRebatesValueCard(client.getTradingAccount(), (commission.getSalesCommission() + commission.getIbCommission()), 2);
        tradingPage.checkIbRebatesValueCard(client.getTradingAccount(), (commission2.getSalesCommission() + commission2.getIbCommission()), 1);
        tradingPage.clickTableViewButton();
        tradingPage.checkIbAccountValueTable(client.getTradingAccount(), relation.getDirectIbRebateAccount(), 2);
        tradingPage.checkIbRebatesValueTable(client.getTradingAccount(), (commission.getSalesCommission() + commission.getIbCommission()), 2);
        tradingPage.checkIbAccountValueTable(client.getTradingAccount(), relation2.getDirectIbRebateAccount(), 1);
        tradingPage.checkIbRebatesValueTable(client.getTradingAccount(), (commission2.getSalesCommission() + commission2.getIbCommission()), 1);
    }
}
