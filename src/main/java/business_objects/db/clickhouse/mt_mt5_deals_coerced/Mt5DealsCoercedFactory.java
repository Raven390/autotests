package business_objects.db.clickhouse.mt_mt5_deals_coerced;

import helpers.data.ClientHelper;
import helpers.data.enums.DateTimeFormat;
import io.qameta.allure.Step;

import static utils.Constants.*;
import static utils.Utils.*;

public class Mt5DealsCoercedFactory {

    @Step("Generate mt5 deals by client")
    public static Mt5DealsCoercedObject generateTradeByClient(ClientHelper client) {
        return new Mt5DealsCoercedObject(client.getBrand(), client.getRegulator(), client.getUserId(), client.getUcid(), client.getTradingAccount(), PLATFORM_MT_5, client.getServerId(), "MT5", "accountType", "accountGroup", "USD", getRandomLongPositive(), getRandomLongPositive(), 0, 0, 1, 1d, getCurrentTimestampMinusOffsetFormatted(
                DateTimeFormat.DATE_AND_TIME, 0, 0, 1, 0, 0), getCurrentTimestampDbFormat(), EURUSD, EURUSD, "EUR", "USD", 1d, 1d, 1d, 1d, 1d, 1d, 1d, 1d, 1d, 1d, 1d, 1d, 1d, 1L, getRandomLongPositive(), COMMENT_AUTOMATION_TESTS, 1d, 2d, 1d, 1d, 1d, 1d, 0, getCurrentTimestampDbFormat(), COMMENT_AUTOMATION_TESTS);
    }

    @Step("Generate mt5 deals by client")
    public static Mt5DealsCoercedObject generateTradeByClient(ClientHelper client, Integer action, Integer entry,
            Integer days, Long order) {
        return new Mt5DealsCoercedObject(client.getBrand(), client.getRegulator(), client.getUserId(), client.getUcid(), client.getTradingAccount(), PLATFORM_MT_5, client.getServerId(), "MT5", "accountType", "accountGroup", "USD", getRandomLongPositive(), order, action, entry, 1, 1d, getCurrentTimestampMinusOffsetFormatted(
                DateTimeFormat.DATE_AND_TIME, 0, 0, days, 0, 0), getCurrentTimestampDbFormat(), EURUSD, EURUSD, "EUR", "USD", 1d, 1d, 1d, 1d, 1d, 1d, 1d, 1d, 1d, 1d, 1d, 1d, 1d, 1L, getRandomLongPositive(), COMMENT_AUTOMATION_TESTS, 1d, 2d, 1d, 1d, 1d, 1d, 0, getCurrentTimestampDbFormat(), COMMENT_AUTOMATION_TESTS);
    }
}