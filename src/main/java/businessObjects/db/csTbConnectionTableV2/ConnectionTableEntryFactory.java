package businessObjects.db.csTbConnectionTableV2;

import static utils.Utils.getCurrentTimestampDbFormat;

public class ConnectionTableEntryFactory {

    public static ConnectionTableEntry getConnectionTableEntry() {
        return new ConnectionTableEntry(
                "vantage-autotest1",
                "vantage-autotest2",
                1,
                """
                    {
                        "connect_info":{
                            "user_1":{"user_id": "autotest1","brand": "vantage"},
                            "connection_1":{
                                "attr_info":{"payout": "463344**** **5603"},
                                "degree_connection": "Same Person",
                                "connection_score": 1
                            },
                            "user_2":{"user_id": "autotest2","brand": "vantage"}
                        }
                    }""",
                getCurrentTimestampDbFormat()
                );
    }
}
