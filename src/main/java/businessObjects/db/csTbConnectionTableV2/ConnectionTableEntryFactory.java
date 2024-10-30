package businessObjects.db.csTbConnectionTableV2;

import static utils.Utils.getCurrentTimestampDbFormat;

public class ConnectionTableEntryFactory {

    public static ConnectionTableEntry getConnectionTableEntryByClient() {
        return new ConnectionTableEntry(
                "vantage-99996",
                "vantage-99999",
                1,
                """
                    {
                        "connect_info":{
                            "user_1":{"user_id": "99996","brand": "vantage"},
                            "connection_1":{
                                "attr_info":{"payout": "463344**** **5603"},
                                "degree_connection": "Same Person",
                                "connection_score": 1
                            },
                            "user_2":{"user_id": "99999","brand": "vantage"}
                        }
                    }""",
                getCurrentTimestampDbFormat()
                );
    }

    public static ConnectionTableEntry getConnectionTableEntryByDocument() {
        return new ConnectionTableEntry(
                "vantage-99991",
                "vantage-99999",
                1,
                """
                    {
                        "connect_info":{
                            "user_1":{"user_id": "99991","brand": "vantage"},
                            "connection_1":{
                                "attr_info":{"payout": "463344**** **5603"},
                                "degree_connection": "Same Person",
                                "connection_score": 1
                            },
                            "user_2":{"user_id": "99999","brand": "vantage"}
                        }
                    }""",
                getCurrentTimestampDbFormat()
        );
    }

    public static ConnectionTableEntry getConnectionTableEntryByEmail() {
        return new ConnectionTableEntry(
                "vantage-99992",
                "vantage-99999",
                1,
                """
                    {
                        "connect_info":{
                            "user_1":{"user_id": "99992","brand": "vantage"},
                            "connection_1":{
                                "attr_info":{"payout": "463344**** **5603"},
                                "degree_connection": "Same Person",
                                "connection_score": 1
                            },
                            "user_2":{"user_id": "99999","brand": "vantage"}
                        }
                    }""",
                getCurrentTimestampDbFormat()
        );
    }

    public static ConnectionTableEntry getConnectionTableEntryByIp() {
        return new ConnectionTableEntry(
                "vantage-99993",
                "vantage-99999",
                1,
                """
                    {
                        "connect_info":{
                            "user_1":{"user_id": "99993","brand": "vantage"},
                            "connection_1":{
                                "attr_info":{"payout": "463344**** **5603"},
                                "degree_connection": "Same Person",
                                "connection_score": 1
                            },
                            "user_2":{"user_id": "99999","brand": "vantage"}
                        }
                    }""",
                getCurrentTimestampDbFormat()
        );
    }

    public static ConnectionTableEntry getConnectionTableEntryByPhone() {
        return new ConnectionTableEntry(
                "vantage-99994",
                "vantage-99999",
                1,
                """
                    {
                        "connect_info":{
                            "user_1":{"user_id": "99994","brand": "vantage"},
                            "connection_1":{
                                "attr_info":{"payout": "463344**** **5603"},
                                "degree_connection": "Same Person",
                                "connection_score": 1
                            },
                            "user_2":{"user_id": "99999","brand": "vantage"}
                        }
                    }""",
                getCurrentTimestampDbFormat()
        );
    }

    public static ConnectionTableEntry getConnectionTableEntryByPayout() {
        return new ConnectionTableEntry(
                "vantage-99995",
                "vantage-99999",
                1,
                """
                    {
                        "connect_info":{
                            "user_1":{"user_id": "99995","brand": "vantage"},
                            "connection_1":{
                                "attr_info":{"payout": "463344**** **5603"},
                                "degree_connection": "Same Person",
                                "connection_score": 1
                            },
                            "user_2":{"user_id": "99999","brand": "vantage"}
                        }
                    }""",
                getCurrentTimestampDbFormat()
        );
    }
}
