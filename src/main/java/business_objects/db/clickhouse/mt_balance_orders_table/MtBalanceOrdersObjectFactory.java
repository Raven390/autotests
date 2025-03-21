package business_objects.db.clickhouse.mt_balance_orders_table;

import helpers.data.ClientHelper;
import io.qameta.allure.Step;

import static utils.Utils.getRandomIntPositive;

public class MtBalanceOrdersObjectFactory {
    @Step("Generate mt balance orders for given account")
    public static MtBalanceOrdersObject generateBalanceOrders(ClientHelper client, Double profit, Double profitUsd,
            String date) {
        return new MtBalanceOrdersObject(getRandomIntPositive(), client.getServerId(), "Test", client.getUcid(), client.getBrand(), "vfsc", client.getUserId(), client.getTradingAccount(), date, profit, profitUsd, "EURUSD", "Administration Fee Automation test");
    }
}
