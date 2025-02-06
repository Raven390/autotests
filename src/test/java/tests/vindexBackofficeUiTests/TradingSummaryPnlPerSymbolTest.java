package tests.vindexBackofficeUiTests;

import businessObjects.db.clickhouse.crmTbAccount.CrmTbAccountObject;
import businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObject;
import businessObjects.db.clickhouse.mtMt4TradesCoerced.MtMt4TradesCoercedObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import helpers.data.ClientHelper;
import helpers.data.enums.Brand;
import helpers.data.enums.Regulator;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tests.TestBaseWeb;

import java.sql.SQLException;
import java.util.List;

import static businessObjects.db.clickhouse.crmTbAccount.CrmTbAccountObjectFactory.generateStaticCrmTbAccountActive;
import static businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObjectFactory.generateStaticUserByClient;
import static businessObjects.db.clickhouse.mtMt4TradesCoerced.MtMt4TradesCoercedObjectFactory.generateMt4TradesCoercedRandomized;
import static helpers.database.DbHelper.insertObjectToDb;
import static helpers.database.DbHelper.insertObjectsToDb;
import static utils.Constants.*;

public class TradingSummaryPnlPerSymbolTest extends TestBaseWeb {


    private static ClientHelper client = new ClientHelper(202_002, "e5880ca5-8578-4a1e-969d-7a64716ca40f", Brand.INFINOX, Regulator.FCA, 202_002_001, 42);
    private static CrmTbUserObject crmTbUser = generateStaticUserByClient(client);
    private static CrmTbAccountObject account1 = generateStaticCrmTbAccountActive(client);

    @BeforeAll
    public static void setup() throws ReflectiveOperationException, SQLException, JsonProcessingException {
        crmTbUser.firstName = "Pienell";
        crmTbUser.lastName = "Symboll";
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        insertObjectToDb(CRM_ACCOUNT_TABLE_NAME, account1);
    }

    @BeforeEach
    public void cleanup() throws SQLException {
        tradingPage.deleteClientDeals(client.getUcid());
    }


    @Test
    @AllureId("")
    @Feature("BMS-721 PNL by symbol")
    @DisplayName("")
    public void pnlByDurationAmountsTest() throws ReflectiveOperationException, SQLException {
        investigationPage.navigate();
        keycloackPage.loginAsDevUser();
        tradingPage.navigate(client.getUcid());

        MtMt4TradesCoercedObject trade0 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade1 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade2 = generateMt4TradesCoercedRandomized(client);
        MtMt4TradesCoercedObject trade3 = generateMt4TradesCoercedRandomized(client);

        trade0.symbol = "MUNY";
        trade1.symbol = "ZENY";
        trade2.symbol = "GIL";
        trade3.symbol = "USDDDDDD";

        insertObjectsToDb(MT4_TRADES_COERCED_TABLE_NAME, List.of(trade0, trade2, trade1, trade3));


    }
}
