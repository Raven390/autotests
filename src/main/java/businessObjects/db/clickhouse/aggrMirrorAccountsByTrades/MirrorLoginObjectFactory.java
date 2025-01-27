package businessObjects.db.clickhouse.aggrMirrorAccountsByTrades;

import helpers.data.ClientHelper;
import io.qameta.allure.Step;

import static utils.Utils.getCurrentTimestampDbFormat;


public class MirrorLoginObjectFactory {
    @Step("Generate mirror trades by account object")
    public static MirrorLoginObject generateMirrorTradesByAccount(ClientHelper client) {
        return new MirrorLoginObject("EURUSD", client.getTradingAccount().toString(), client.getServerId().toString(), 1.1, String.valueOf(1234), String.valueOf(2), 3.3, getCurrentTimestampDbFormat());
    }
}