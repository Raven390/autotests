package tests.vindex_backoffice_ui_tests.investigationTool.trading;

import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.mt_account.MtAccountObject;
import business_objects.db.clickhouse.mt_mt4_trades.MtMt4TradesObject;
import business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedObject;
import business_objects.db.clickhouse.mt_mt5_positions.MtMt5PositionsObject;
import business_objects.kafka.alerts.RuleAlert;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.data.ClientHelper;
import helpers.kafka.KafkaHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

import java.sql.SQLException;
import java.util.List;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateAdditionalCrmTbAccountDataForUi;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountDataForUi;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static business_objects.db.clickhouse.mt_mt4_trades.MtMt4TradesObjectFactory.generateMt4TradesObject;
import static business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedFactory.generateTradeByClient;
import static business_objects.db.clickhouse.mt_mt5_positions.MtMt5PositionsObjectFactory.generateMtMt5PositionsObject;
import static business_objects.kafka.alerts.RuleAlertFactory.generateRuleAlertByUcid;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.BoHelper.closeAlert;
import static helpers.database.CleanTableHelper.cleanMt5CoercedTableByUcid;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static utils.Constants.*;
import static utils.Utils.getRandomRoundedDouble;

class TradingInfoAccountsPnlFullTest extends TestBaseWeb {

    private static final KafkaHelper kafka = new KafkaHelper();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final ClientHelper client = getRandomVantageClientAllFields();
    private static final CrmTbUserObject crmTbUser = generateUserByClient(client);
    private static final String CURRENCY_USD = "USD";
    private static CrmTbAccountObject account1;
    private static CrmTbAccountObject account2;
    private static MtAccountObject mtAccount1;
    private static MtAccountObject mtAccount2;
    private static Mt5DealsCoercedObject trade511;
    private static Mt5DealsCoercedObject trade512;
    private static Mt5DealsCoercedObject trade521;
    private static MtMt4TradesObject trade411;
    private static MtMt4TradesObject trade412;
    private static MtMt4TradesObject trade421;
    private static MtMt5PositionsObject positions11;
    private static MtMt5PositionsObject positions12;
    private static MtMt5PositionsObject positions21;
    private static double calculatedPnl;


    @BeforeAll
    static void setup() throws ReflectiveOperationException, SQLException, JsonProcessingException {
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        account1 = generateCrmTbAccountDataForUi(client);
        account1.currency = "EUR";
        insertObjectToDb(CRM_TB_ACCOUNT_TABLE_NAME, account1);
        insertObjectToDb(MT_ACCOUNT_TABLE_NAME, generateMtAccountByCrmTbAccount(account1));
        account2 = generateAdditionalCrmTbAccountDataForUi(client);
        account2.serverIdSt = 22;
        account2.accountStatus = "Inactive";
        trade511 = generateTradeByClient(client);
        trade511.setProfitUsd(getRandomRoundedDouble(-555_555, 555_555));
        trade511.setStorageUsd(getRandomRoundedDouble(-555_555, 555_555));
        trade511.setCommissionUsd(getRandomRoundedDouble(-555_555, 555_555));
        trade512 = generateTradeByClient(client);
        trade512.setProfitUsd(getRandomRoundedDouble(-555_555, 555_555));
        trade512.setStorageUsd(getRandomRoundedDouble(-555_555, 555_555));
        trade512.setCommissionUsd(getRandomRoundedDouble(-555_555, 555_555));
        trade521 = generateTradeByClient(client);
        trade521.setAccount(account2.account);
        trade521.setServerId(account2.serverIdSt);
        trade521.setProfitUsd(22.2);
        trade521.setStorageUsd(0d);
        trade521.setCommissionUsd(0.0);

        trade411 = generateMt4TradesObject(client);
        trade411.setProfitUsd(getRandomRoundedDouble(-555_555, 555_555));
        trade411.setStorageUsd(getRandomRoundedDouble(-555_555, 555_555));
        trade411.setCommissionUsd(getRandomRoundedDouble(-555_555, 555_555));
        trade412 = generateMt4TradesObject(client);
        trade412.setProfitUsd(getRandomRoundedDouble(-555_555, 555_555));
        trade412.setStorageUsd(getRandomRoundedDouble(-555_555, 555_555));
        trade412.setCommissionUsd(getRandomRoundedDouble(-555_555, 555_555));
        trade421 = generateMt4TradesObject(client);
        trade421.setProfitUsd(getRandomRoundedDouble(-555_555, 555_555));
        trade421.setStorageUsd(getRandomRoundedDouble(-555_555, 555_555));
        trade421.setCommissionUsd(getRandomRoundedDouble(-555_555, 555_555));
        trade421.setAccount(account2.account);

        positions11 = generateMtMt5PositionsObject(client);
        positions11.setProfitUsd(getRandomRoundedDouble(-555_555, 555_555));
        positions11.setStorageUsd(getRandomRoundedDouble(-555_555, 555_555));
        positions12 = generateMtMt5PositionsObject(client);
        positions12.setProfitUsd(getRandomRoundedDouble(-555_555, 555_555));
        positions12.setStorageUsd(getRandomRoundedDouble(-555_555, 555_555));
        positions21 = generateMtMt5PositionsObject(client);
        positions21.setProfitUsd(getRandomRoundedDouble(-555_555, 555_555));
        positions21.setStorageUsd(getRandomRoundedDouble(-555_555, 555_555));
        positions21.setAccount(account2.account);

        calculatedPnl = (trade511.getProfitUsd() + trade511.getStorageUsd() + trade511.getCommissionUsd()) + (trade512.getProfitUsd() + trade512.getStorageUsd() + trade512.getCommissionUsd()) + (trade411.getProfitUsd() + trade411.getStorageUsd() + trade411.getCommissionUsd()) + (trade412.getProfitUsd() + trade412.getStorageUsd() + trade412.getCommissionUsd()) + (positions11.getProfitUsd() + positions11.getStorageUsd()) + (positions12.getProfitUsd() + positions12.getStorageUsd());
        insertObjectToDb(CRM_TB_ACCOUNT_TABLE_NAME, account2);

        mtAccount1 = generateMtAccountByCrmTbAccount(account1);
        mtAccount2 = generateMtAccountByCrmTbAccount(account2);
        insertObjectsToDb(MT_ACCOUNT_TABLE_NAME, List.of(mtAccount1, mtAccount2));
        insertObjectToDb(MT_ACCOUNT_TABLE_NAME, generateMtAccountByCrmTbAccount(account2));
        insertObjectsToDb(MT5_DEALS_COERCED_TABLE_NAME, List.of(trade511, trade512, trade521));
        insertObjectsToDb(MT4_TRADES_TABLE_NAME, List.of(trade411, trade412, trade421));
        insertObjectsToDb(MT5_POSITIONS_TABLE_NAME, List.of(positions11, positions12, positions21));
        RuleAlert alert = generateRuleAlertByUcid(crmTbUser.ucid);
        kafka.produceMessage(alert.alertId, objectMapper.writeValueAsString(alert), KAFKA_TOPIC_ALERTS);
    }

    @AfterAll
    static void teardown() throws Exception {
        deleteEntryFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", crmTbUser.ucid));
        deleteEntryFromDb(MT4_TRADES_TABLE_NAME, String.format("ucid = '%s'", crmTbUser.ucid));
        deleteEntryFromDb(MT5_POSITIONS_TABLE_NAME, String.format("ucid = '%s'", crmTbUser.ucid));
        cleanMt5CoercedTableByUcid(MT5_DEALS_COERCED_TABLE_NAME, crmTbUser.ucid);
        closeAlert(crmTbUser.ucid);
    }

    @Test
    @AllureId("1646")
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @Feature("BMS-2302 Adjust data in investigation tool")
    @DisplayName("Verify PNL in trading accounts calculated right")
    void verifyAccountsCardViewPnlTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        tradingPage.openTradingTab();
        tradingPage.openAccountsTab();
        assertThat("Assert that account total pnl in card view is as expected", tradingPage.getAccountTradingPnl(account1.account), equalTo(String.format("%s %s", decimalFormat.format(calculatedPnl), CURRENCY_USD)));
    }

}
