package businessObjects.db.clickhouse.mtBalanceOrdersTable;

import helpers.data.ClientHelper;
import io.qameta.allure.Step;

public class MtBalanceOrdersObjectFactory {
    @Step("Generate mt balance orders for given account")
    public static MtBalanceOrdersObject generateBalanceOrders(ClientHelper client, Double profit, Double profitUsd,
            String date) {
        return new MtBalanceOrdersObject(123, client.getServerId(), "Test", client.getUcid(), client.getBrand(), "vfsc", client.getUserId(), client.getTradingAccount(), date, profit, profitUsd, "EURUSD", "Administration Fee Automation test");
    }
}
