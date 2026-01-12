package business_objects.db.clickhouse.mt___symbol_session;

import helpers.data.ClientHelper;
import java.time.DayOfWeek;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class MtSymbolSessionObjectFactory {

    private MtSymbolSessionObjectFactory() {}

    public static MtSymbolSessionObject generateMtSymbolSessionObjectByClient(
            ClientHelper client, String symbol, String trade) {

        return new MtSymbolSessionObject(
                symbol,
                client.getServerId(),
                dayIndex(OffsetDateTime.now(ZoneOffset.UTC).getDayOfWeek()),
                trade);
    }

    private static int dayIndex(DayOfWeek dow) {
        return dow.getValue() % 7; // Sun=7 -> Sun=0
    }
}
