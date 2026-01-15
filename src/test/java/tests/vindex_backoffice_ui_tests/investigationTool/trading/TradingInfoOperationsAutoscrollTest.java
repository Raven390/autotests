package tests.vindex_backoffice_ui_tests.investigationTool.trading;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateAdditionalCrmTbAccountDataForUi;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountDataForUi;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObjectFactory.generateMt4TradesCoerced;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.DateTimeFormat.DATE_AND_TIME;
import static helpers.database.BoHelper.closeAlert;
import static helpers.database.DbHelper.*;
import static utils.Constants.*;
import static utils.Utils.*;

import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.mt_mt4_trades_coerced.MtMt4TradesCoercedObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import helpers.data.ClientHelper;
import io.qameta.allure.AllureId;
import java.sql.SQLException;
import java.util.List;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

class TradingInfoOperationsAutoscrollTest extends TestBaseWeb {

    private static final ClientHelper client = getRandomVantageClientAllFields();
    private static final CrmTbUserObject crmTbUser = generateUserByClient(client);
    private static CrmTbAccountObject account1;
    private static CrmTbAccountObject account2;
    static String timeFirst = getCurrentTimestampDbFormat();
    static String timeLast = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 2, 2, 0);

    @BeforeAll
    static void setup() throws ReflectiveOperationException, SQLException, JsonProcessingException {
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        account1 = generateCrmTbAccountDataForUi(client);
        insertCrmAccountsToDb(account1);
        insertObjectToDb(MT_ACCOUNT_TABLE_NAME, generateMtAccountByCrmTbAccount(account1));
        account2 = generateAdditionalCrmTbAccountDataForUi(client);
        account2.platform = "MT5";
        insertCrmAccountsToDb(account2);
        insertObjectToDb(MT_ACCOUNT_TABLE_NAME, generateMtAccountByCrmTbAccount(account2));

        List<MtMt4TradesCoercedObject> deals = new java.util.ArrayList<>(List.of());
        for (int i = 0; i < 41; i++) {
            MtMt4TradesCoercedObject trade = generateMt4TradesCoerced(client);
            trade.setOpenTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 1, 1, 0));
            deals.add(trade);
        }

        deals.getFirst().setOpenTime(timeFirst);
        deals.getLast().setOpenTime(timeLast);
        insertObjectsToDb(MT4_TRADES_COERCED_TABLE_NAME, deals);
    }

    @Test
    @AllureId("1647")
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @DisplayName("Verify autoscroll works test")
    void verifyAutoscrollWorksTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        tradingPage.openTradingTab();
        tradingPage.openOperationsTab();
        tradingPage.waitForPageToLoad();
        tradingPage.checkDealPresentedByDate(timeLast);
        tradingPage.checkDealHiddenByDate(timeFirst);
        tradingPage.clickScrollOperationsListDownButton();
        tradingPage.checkDealPresentedByDate(timeFirst);
        tradingPage.checkDealHiddenByDate(timeLast);
        tradingPage.clickScrollOperationsListUpButton();
        tradingPage.checkDealPresentedByDate(timeLast);
        tradingPage.checkDealHiddenByDate(timeFirst);
    }

    @AfterAll
    static void teardown() throws SQLException {
        deleteObjectFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", crmTbUser.ucid));
        deleteObjectFromDb(
                MT4_TRADES_COERCED_TABLE_NAME,
                String.format("account = %s OR account = %s", account1.account, account2.account));
        closeAlert(crmTbUser.ucid);
    }
}
