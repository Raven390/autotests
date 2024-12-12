package businessObjects.db.clickhouse.mtBalanceOrdersTable;

import helpers.data.ClientHelper;
import io.qameta.allure.Step;
import utils.Utils;

public class MtBalanceOrdersObjectFactory {
    @Step("Generate mt balance orders for given account")
    public static MtBalanceOrdersObject generateBalanceOrders(ClientHelper client, Double profit, Double profitUsd,
            String date) {
        return new MtBalanceOrdersObject(Utils.getRandomUuidString(), 123, client.getServerId(), "Test", client.getUcid(), client.getBrand(), "vfsc", client.getUserId(), client.getTradingAccount(), date, profit, profitUsd, "EURUSD", 3, "Administration Fee Automation test");
    }
}
