package businessObjects.db.clickhouse.csTbIpTable;

import static utils.Utils.getCurrentTimestampDbFormat;

public class IpTableEntryFactory {

    public static IpTableEntry ipTableEntryForConnectionSearch() {
        return new IpTableEntry(
                "99993",
                99_993,
                "vantage",
                "111.111.111.111",
                getCurrentTimestampDbFormat()
        );
    }
}
