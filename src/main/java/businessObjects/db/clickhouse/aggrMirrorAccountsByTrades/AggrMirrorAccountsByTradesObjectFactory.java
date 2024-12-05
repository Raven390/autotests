package businessObjects.db.clickhouse.aggrMirrorAccountsByTrades;

import helpers.data.ClientHelper;
import io.qameta.allure.Step;


public class AggrMirrorAccountsByTradesObjectFactory {
    @Step("Generate mirror trades by account object")
    public static AggrMirrorAccountsByTradesObject generateMirrorTradesByAccount(ClientHelper client) {
        return new AggrMirrorAccountsByTradesObject("EURUSD", client.getTradingAccount().toString(), client.getServerId().toString(), 1.1, String.valueOf(1234), String.valueOf(2), 3.3);
    }
}