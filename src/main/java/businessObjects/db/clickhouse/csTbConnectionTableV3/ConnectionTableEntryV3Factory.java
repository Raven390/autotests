package businessObjects.db.clickhouse.csTbConnectionTableV3;

import static utils.Utils.getCurrentTimestampDbFormat;

public class ConnectionTableEntryV3Factory {

    public static ConnectionTableEntryV3 getConnectionTableEntryByClientV3() {
        return new ConnectionTableEntryV3(
                "vantage-99996",
                "vantage-99999",
                "Same Person",
                1d,
                "{\"payoutId\": \"463344**** **5603\"}",
                getCurrentTimestampDbFormat()
                );
    }

    public static ConnectionTableEntryV3 getConnectionTableEntryByClientLvl2V3() {
        return new ConnectionTableEntryV3(
                "vantage-99999",
                "vantage-99991",
                "Same Person",
                0.5d,
                "{\"payoutId\": \"463344**** **5603\"}",
                getCurrentTimestampDbFormat()
        );
    }

    public static ConnectionTableEntryV3 getConnectionTableEntryByDocumentV3() {
        return new ConnectionTableEntryV3(
                "vantage-99991",
                "vantage-99999",
                "Same Person",
                1d,
                "{\"payoutId\": \"463344**** **5603\"}",
                getCurrentTimestampDbFormat()
        );
    }

    public static ConnectionTableEntryV3 getConnectionTableEntryByEmailV3() {
        return new ConnectionTableEntryV3(
                "vantage-99992",
                "vantage-99999",
                "Same Person",
                1d,
                "{\"payoutId\": \"463344**** **5603\"}",
                getCurrentTimestampDbFormat()
        );
    }

    public static ConnectionTableEntryV3 getConnectionTableEntryByIpV3() {
        return new ConnectionTableEntryV3(
                "vantage-99993",
                "vantage-99999",
                "Same Person",
                1d,
                "{\"payoutId\": \"463344**** **5603\"}",
                getCurrentTimestampDbFormat()
        );
    }

    public static ConnectionTableEntryV3 getConnectionTableEntryByPhoneV3() {
        return new ConnectionTableEntryV3(
                "vantage-99994",
                "vantage-99999",
                "Same Person",
                1d,
                "{\"payoutId\": \"463344**** **5603\"}",
                getCurrentTimestampDbFormat()
        );
    }

    public static ConnectionTableEntryV3 getConnectionTableEntryByPayoutV3() {
        return new ConnectionTableEntryV3(
                "vantage-99995",
                "vantage-99999",
                "Same Person",
                1d,
                "{\"payoutId\": \"463344**** **5603\"}",
                getCurrentTimestampDbFormat()
        );
    }

    public static ConnectionTableEntryV3 getConnectionTableEntryForDepthV3() {
        return new ConnectionTableEntryV3(
                "vantage-99999",
                "vantage-99997",
                "Same Person",
                1d,
                "{\"payoutId\": \"463344**** **5603\"}",
                getCurrentTimestampDbFormat()
        );
    }

    public static ConnectionTableEntryV3 getConnectionTableEntryForFiltration1V3() {
        return new ConnectionTableEntryV3(
                "vantage-100001",
                "vantage-100002",
                "Same Person",
                1d,
                "{\"payoutId\": \"463344**** **5603\"}",
                getCurrentTimestampDbFormat()
        );
    }

    public static ConnectionTableEntryV3 getConnectionTableEntryForFiltration2V3() {
        return new ConnectionTableEntryV3(
                "vantage-100002",
                "vantage-100003",
                "Same Network",
                0.2d,
                "{\"emailAddress\": \"testfiltration@qatest.com\"}",
                getCurrentTimestampDbFormat()
        );
    }

    public static ConnectionTableEntryV3 getConnectionTableByAttributesEntryForFiltration1V3() {
        return new ConnectionTableEntryV3(
                "vantage-100004",
                "vantage-100005",
                "Same Person",
                1d,
                "{\"payoutId\": \"463344**** **5603\"}",
                getCurrentTimestampDbFormat()
        );
    }

    public static ConnectionTableEntryV3 getConnectionTableByAttributesEntryForFiltration2V3() {
        return new ConnectionTableEntryV3(
                "vantage-100005",
                "vantage-100006",
                "Same Network",
                0.2d,
                "{\"emailAddress\": \"testfiltration@qatest.com\"}",
                getCurrentTimestampDbFormat()
        );
    }
}
