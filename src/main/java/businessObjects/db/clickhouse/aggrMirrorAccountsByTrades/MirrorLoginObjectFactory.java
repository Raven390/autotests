package businessObjects.db.clickhouse.aggrMirrorAccountsByTrades;

import helpers.data.ClientHelper;
import io.qameta.allure.Step;

import static utils.Utils.getCurrentTimestampDbFormat;


public class MirrorLoginObjectFactory {
    @Step("Generate mirror trades by account object")
    public static MirrorLoginObject generateMirrorTradesByAccount(ClientHelper client) {
        return new MirrorLoginObject("EURUSD", client.getTradingAccount().toString(), client.getServerId().toString(), 1.1, String.valueOf(1234), String.valueOf(2), 3.3, getCurrentTimestampDbFormat());
    }

    @Step("Generate mirror trades client from to")
    public static MirrorLoginObject generateMirrorTradesByClients(ClientHelper clientFrom, ClientHelper clientTo) {
        return new MirrorLoginObject("EURUSD", clientFrom.getTradingAccount().toString(), clientFrom.getServerId().toString(), 1.1, clientTo.getTradingAccount().toString(), clientTo.getServerId().toString(), 3.3, getCurrentTimestampDbFormat());
    }
}