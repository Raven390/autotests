package business_objects.db.clickhouse.ts_by_symbol_daily;

import helpers.data.ClientHelper;

import static utils.Utils.getCurrentDate;
import static utils.Utils.getCurrentTimestampDbFormat;

public class TsBySymbolDailyFactory {

    public static TsBySymbolDailyObject generateTsBySymbolDailyByClient(ClientHelper client) {
        return new TsBySymbolDailyObject(
                client.getUcid(), client.getTradingAccount().longValue(), getCurrentDate(), "USDEUR", 123.45, 99.87, 25L, 30L, 2L, 5.67, getCurrentTimestampDbFormat(), 103.65
        );
    }
}
