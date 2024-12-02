package businessObjects.db.clickhouse.csTbConnectionTableV3;

import static utils.Utils.getCurrentTimestampDbFormat;

public class ConnectionTableEntryV3Factory {

    public static ConnectionTableEntryV3 getConnectionTableEntryByClientV3() {
        return new ConnectionTableEntryV3(
                "vantage-99996",
                "vantage-99999",
                "Same Person",
                1d,
                "{\"payout\": \"463344**** **5603\"}",
                getCurrentTimestampDbFormat()
                );
    }

    public static ConnectionTableEntryV3 getConnectionTableEntryByClientLvl2V3() {
        return new ConnectionTableEntryV3(
                "vantage-99999",
                "vantage-99991",
                "Same Person",
                0.5d,
                "{\"payout\": \"463344**** **5603\"}",
                getCurrentTimestampDbFormat()
        );
    }

    public static ConnectionTableEntryV3 getConnectionTableEntryByDocumentV3() {
        return new ConnectionTableEntryV3(
                "vantage-99991",
                "vantage-99999",
                "Same Person",
                1d,
                "{\"payout\": \"463344**** **5603\"}",
                getCurrentTimestampDbFormat()
        );
    }

    public static ConnectionTableEntryV3 getConnectionTableEntryByEmailV3() {
        return new ConnectionTableEntryV3(
                "vantage-99992",
                "vantage-99999",
                "Same Person",
                1d,
                "{\"payout\": \"463344**** **5603\"}",
                getCurrentTimestampDbFormat()
        );
    }

    public static ConnectionTableEntryV3 getConnectionTableEntryByIpV3() {
        return new ConnectionTableEntryV3(
                "vantage-99993",
                "vantage-99999",
                "Same Person",
                1d,
                "{\"payout\": \"463344**** **5603\"}",
                getCurrentTimestampDbFormat()
        );
    }

    public static ConnectionTableEntryV3 getConnectionTableEntryByPhoneV3() {
        return new ConnectionTableEntryV3(
                "vantage-99994",
                "vantage-99999",
                "Same Person",
                1d,
                "{\"payout\": \"463344**** **5603\"}",
                getCurrentTimestampDbFormat()
        );
    }

    public static ConnectionTableEntryV3 getConnectionTableEntryByPayoutV3() {
        return new ConnectionTableEntryV3(
                "vantage-99995",
                "vantage-99999",
                "Same Person",
                1d,
                "{\"payout\": \"463344**** **5603\"}",
                getCurrentTimestampDbFormat()
        );
    }

    public static ConnectionTableEntryV3 getConnectionTableEntryForDepthV3() {
        return new ConnectionTableEntryV3(
                "vantage-99999",
                "vantage-99997",
                "Same Person",
                1d,
                "{\"payout\": \"463344**** **5603\"}",
                getCurrentTimestampDbFormat()
        );
    }
}
