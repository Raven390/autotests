package businessObjects.db.clickhouse.mtMt5DealsTable;

import io.qameta.allure.Step;

import static utils.Utils.getCurrentTimestampDbFormat;
import static utils.Utils.getRandomIntPositive;

public class Mt5DealsFactory {

    @Step("Generate mt5 deals")
    public static Mt5DealsObject generateTradeByAccountServerId(Integer account, Integer serverId) {
        return new Mt5DealsObject(1, "[]", "Comment",1.0,1.2,getRandomIntPositive(),
                                  1,1,2,1,1234L,"externalId",1.0,
                                  1,"", getCurrentTimestampDbFormat(), account,2.0,1.1,
                                  1.0,1, "1", 11111111L,111111L,2.2,
                                  3.3,1.0,1.0,1.0, 1.0,1.0,
                                  1.0,2.0,"",serverId,32, "MT5-IUK",
                                  "MT5_INF",1,"EURUSD",1.2,1.1,getCurrentTimestampDbFormat(),getCurrentTimestampDbFormat(),
                getRandomIntPositive().longValue(),0.0,500.0,50.0,25.0,1.0,1.0);
    }

    public static Mt5DealsObject generateTradeForGroupBy1(Integer account, Integer serverId) {
        Mt5DealsObject trade = generateTradeByAccountServerId(account, serverId);
        trade.entry = 2;
        trade.time = "2024-01-01 00:00:00";
        trade.profit = 1d;
        return trade;
    }

    public static Mt5DealsObject generateTradeForGroupBy2(Integer account, Integer serverId) {
        Mt5DealsObject trade = generateTradeByAccountServerId(account, serverId);
        trade.action = 2;
        trade.time = "2024-02-02 00:00:00";
        trade.profit = 2d;
        return trade;
    }

    public static Mt5DealsObject generateTradeForGroupBy3(Integer account, Integer serverId) {
        Mt5DealsObject trade = generateTradeByAccountServerId(account, serverId);
        trade.entry = 2;
        trade.time = "2024-01-01 00:00:00";
        trade.symbol = "GBPUSD";
        trade.profit = 3d;
        return trade;
    }

    public static Mt5DealsObject generateTradeForGroupBy4(Integer account, Integer serverId) {
        Mt5DealsObject trade = generateTradeByAccountServerId(account, serverId);
        trade.action = 2;
        trade.time = "2024-02-02 00:00:00";
        trade.symbol = "GBPUSD";
        trade.profit = 4d;
        return trade;
    }
}