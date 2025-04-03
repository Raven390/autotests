package tests.vindex_backoffice_ui_tests.trading;

import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.mtAccount.MtAccountObject;
import business_objects.db.clickhouse.mt_mt4_trades.MtMt4TradesObject;
import business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedObject;
import business_objects.db.clickhouse.mt_mt5_positions.MtMt5PositionsObject;
import helpers.data.ClientHelper;
import helpers.data.enums.Brand;
import helpers.data.enums.Regulator;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import page_objects.backoffice_pages.TradingPage;
import tests.TestBaseWeb;

import java.util.List;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateAdditionalStaticCrmTbAccountActive;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateStaticCrmTbAccountActive;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateStaticUserByClient;
import static business_objects.db.clickhouse.mtAccount.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static business_objects.db.clickhouse.mt_mt4_trades.MtMt4TradesObjectFactory.generateMt4TradesObject;
import static business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedFactory.generateMt5DealsCoercedObject;
import static business_objects.db.clickhouse.mt_mt5_positions.MtMt5PositionsObjectFactory.generateMtMt5PositionsObject;
import static helpers.database.DbHelper.insertObjectToDb;
import static helpers.database.DbHelper.insertObjectsToDb;
import static utils.Constants.*;

class TradingOpenPositionsTest extends TestBaseWeb {

    static ClientHelper client = new ClientHelper(242_401, "063cde3b-ea9d-48b5-8e2c-99f3d5f67999", Brand.VANTAGE, Regulator.VFSC2, 242_401_001, 242_401_002, 42);
    private static final CrmTbUserObject crmTbUser = generateStaticUserByClient(client);
    private static CrmTbAccountObject account1;
    private static CrmTbAccountObject account2;
    private static MtAccountObject mtAccount1;
    private static MtAccountObject mtAccount2;

    @BeforeAll
    static void setup() throws Exception {
        crmTbUser.firstName = "Operationist";
        crmTbUser.lastName = "Opener";
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        account1 = generateStaticCrmTbAccountActive(client);
        account1.platform = "MT4";
        account2 = generateAdditionalStaticCrmTbAccountActive(client);
        account2.platform = "MT5";
        insertObjectsToDb(CRM_ACCOUNT_TABLE_NAME, List.of(account1, account2));
        mtAccount1 = generateMtAccountByCrmTbAccount(account1);
        mtAccount2 = generateMtAccountByCrmTbAccount(account2);
        insertObjectsToDb(MT_ACCOUNT_TABLE_NAME, List.of(mtAccount1, mtAccount2));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("")
    @Feature("BMS-1050 Open positions tab in Trading tab")
    @DisplayName("1112")
    void OpenTradesShowDataFromDb() throws Exception {

        openPositions.openPositionsClean(client);
        MtMt4TradesObject mt4trade = generateMt4TradesObject(client);
        mt4trade.setCloseTime("1970-01-01 00:00:00");
        mt4trade.setCmd(0);
        mt4trade.setAccount(client.getTradingAccount());
        mt4trade.setServerId(client.getServerId());
        MtMt5PositionsObject position = generateMtMt5PositionsObject(client);
        position.setAccount(client.getTradingAccount2());
        Mt5DealsCoercedObject deal = generateMt5DealsCoercedObject(position);
        position.setAccount(client.getTradingAccount2());
        insertObjectToDb(MT4_TRADES_TABLE_NAME, mt4trade);
        insertObjectToDb(MT5_POSITIONS_TABLE_NAME, position);
        insertObjectToDb(MT5_DEALS_COERCED_TABLE_NAME, deal);

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        openPositions.navigateOpenPositions(client.getUcid());
        openPositions.checkAccountCellValue(client.getTradingAccount(), mt4trade.getPlatform());
        openPositions.checkAccountCellValue(client.getTradingAccount2(), deal.getPlatform());
        openPositions.checkTypeCellValue(client.getTradingAccount2(), deal.getSymbol(), TradingPage.translateActionMT5(deal.getAction()));
        openPositions.checkTypeCellValue(client.getTradingAccount(), mt4trade.getSymbol(), TradingPage.translateActionMT4(mt4trade.getCmd()));
        openPositions.checkVolumeCellValue(client.getTradingAccount2(), position.getVolumeLots(), position.getNotionalValueUsd());
        openPositions.checkVolumeCellValue(client.getTradingAccount(), mt4trade.getVolumeLots(), mt4trade.getOpenNotionalValueUsd());
        openPositions.checkOpenCellValue(client.getTradingAccount2(), position.getPriceOpen(), position.getTimeCreate());
        openPositions.checkOpenCellValue(client.getTradingAccount(), mt4trade.getOpenPrice(), mt4trade.getOpenTime());
        openPositions.checkTpSlCellValue(client.getTradingAccount2(), position.getTp(), position.getSl());
        openPositions.checkTpSlCellValue(client.getTradingAccount(), mt4trade.getTp(), mt4trade.getSl());
        openPositions.checkFloatingPnlCellValue(client.getTradingAccount2(), position.getProfitUsd());
        openPositions.checkFloatingPnlCellValue(client.getTradingAccount(), mt4trade.getProfitUsd());
        openPositions.checkSwapCellValue(client.getTradingAccount2(), position.getStorageUsd());
        openPositions.checkSwapCellValue(client.getTradingAccount(), mt4trade.getStorageUsd());
        openPositions.checkCommissionCellValue(client.getTradingAccount2(), deal.getCommissionUsd());
        openPositions.checkCommissionCellValue(client.getTradingAccount(), mt4trade.getCommissionUsd());
        openPositions.checkMethodCellValue(client.getTradingAccount2(), TradingPage.translateReasonMT5(position.getReason()));
        openPositions.checkMethodCellValue(client.getTradingAccount(), TradingPage.translateReasonMT4(mt4trade.getReason().intValue()));
        openPositions.checkCommentCellValue(client.getTradingAccount2(), position.getComment());
        openPositions.checkCommentCellValue(client.getTradingAccount(), mt4trade.getComment());

    }


    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1111")
    @Feature("BMS-1050 Open positions tab in Trading tab")
    @DisplayName("Test that open positions subtab renders all basic elements")
    void rendersAllBasicElementsTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        openPositions.navigateOpenPositions(client.getUcid());
        openPositions.openPositionsRenders();
    }


}
