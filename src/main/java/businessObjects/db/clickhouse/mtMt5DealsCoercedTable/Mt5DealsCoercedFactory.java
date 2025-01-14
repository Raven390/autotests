package businessObjects.db.clickhouse.mtMt5DealsCoercedTable;

import helpers.data.ClientHelper;
import io.qameta.allure.Step;

import java.time.LocalDateTime;

import static utils.Utils.*;

public class Mt5DealsCoercedFactory {

    @Step("Generate mt5 deals")
    public static Mt5DealsCoercedObject generateTradeByAccountServerId(Integer account, Integer serverId) {
        return new Mt5DealsCoercedObject("brand", "regulator", 1, "ucid", account, "MT5", serverId, "serverName", "accountType", "accountGroup", "USD", getRandomIntPositive(), getRandomLongPositive(), 1, 1, 1, 1d, LocalDateTime.now().minusDays(1).toString().replace("T", " "), getCurrentTimestampDbFormat(), "EURUSD", "EURUSD", "EUR", "USD", 1d, 1d, 1d, 1d, 1d, 1d, 1d, 1d, 1, 1d, 1d, 1, 1d, 1L, getRandomLongPositive(), "comment", 1d, 2d, 1d, 1d, 1d, 1d, 0, getCurrentTimestampDbFormat(), "Automation tests");
    }

    @Step("Generate mt5 deals by client")
    public static Mt5DealsCoercedObject generateTradeByClient(ClientHelper client) {
        return new Mt5DealsCoercedObject(client.getBrand(), client.getRegulator(), client.getUserId(), client.getUcid(), client.getTradingAccount(), "MT5", client.getServerId(), "MT5", "accountType", "accountGroup", "USD", getRandomIntPositive(), getRandomLongPositive(), 0, 0, 1, 1d, getCurrentTimestampDbFormatMinusDays(1), getCurrentTimestampDbFormat(), "EURUSD", "EURUSD", "EUR", "USD", 1d, 1d, 1d, 1d, 1d, 1d, 1d, 1d, 1, 1d, 1d, 1, 1d, 1L, getRandomLongPositive(), "comment", 1d, 2d, 1d, 1d, 1d, 1d, 0, getCurrentTimestampDbFormat(), "Automation tests");
    }

    @Step("Generate mt5 deals by client")
    public static Mt5DealsCoercedObject generateTradeByClient(ClientHelper client, Integer action, Integer entry,
            Integer days, Long order) {
        return new Mt5DealsCoercedObject(client.getBrand(), client.getRegulator(), client.getUserId(), client.getUcid(), client.getTradingAccount(), "MT5", client.getServerId(), "MT5", "accountType", "accountGroup", "USD", getRandomIntPositive(), order, action, entry, 1, 1d, getCurrentTimestampDbFormatMinusDays(days), getCurrentTimestampDbFormat(), "EURUSD", "EURUSD", "EUR", "USD", 1d, 1d, 1d, 1d, 1d, 1d, 1d, 1d, 1, 1d, 1d, 1, 1d, 1L, getRandomLongPositive(), "comment", 1d, 2d, 1d, 1d, 1d, 1d, 0, getCurrentTimestampDbFormat(), "Automation tests");
    }

    public static Mt5DealsCoercedObject generateTradeForGroupBy1(Integer account, Integer serverId) {
        Mt5DealsCoercedObject trade = generateTradeByAccountServerId(account, serverId);
        trade.entry = 2;
        trade.time = "2024-01-01 00:00:00";
        trade.profit = 1d;
        return trade;
    }

    public static Mt5DealsCoercedObject generateTradeForGroupBy2(Integer account, Integer serverId) {
        Mt5DealsCoercedObject trade = generateTradeByAccountServerId(account, serverId);
        trade.action = 2;
        trade.time = "2024-02-02 00:00:00";
        trade.profit = 2d;
        return trade;
    }

    public static Mt5DealsCoercedObject generateTradeForGroupBy3(Integer account, Integer serverId) {
        Mt5DealsCoercedObject trade = generateTradeByAccountServerId(account, serverId);
        trade.entry = 2;
        trade.time = "2024-01-01 00:00:00";
        trade.symbol = "GBPUSD";
        trade.profit = 3d;
        return trade;
    }

    public static Mt5DealsCoercedObject generateTradeForGroupBy4(Integer account, Integer serverId) {
        Mt5DealsCoercedObject trade = generateTradeByAccountServerId(account, serverId);
        trade.action = 2;
        trade.time = "2024-02-02 00:00:00";
        trade.symbol = "GBPUSD";
        trade.profit = 4d;
        return trade;
    }
}