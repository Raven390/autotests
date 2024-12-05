package businessObjects.db.clickhouse.csTbConnectionTableV2;

import static utils.Utils.getCurrentTimestampDbFormat;

public class ConnectionTableEntryV2Factory {

    @Deprecated
    public static ConnectionTableEntryV2 getConnectionTableEntryByClientV2() {
        return new ConnectionTableEntryV2(
                "vantage-99996", "vantage-99999", 1, """
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
                        }""", getCurrentTimestampDbFormat()
        );
    }

    @Deprecated
    public static ConnectionTableEntryV2 getConnectionTableEntryByDocumentV2() {
        return new ConnectionTableEntryV2(
                "vantage-99991", "vantage-99999", 1, """
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
                        }""", getCurrentTimestampDbFormat()
        );
    }

    @Deprecated
    public static ConnectionTableEntryV2 getConnectionTableEntryByEmailV2() {
        return new ConnectionTableEntryV2(
                "vantage-99992", "vantage-99999", 1, """
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
                        }""", getCurrentTimestampDbFormat()
        );
    }

    @Deprecated
    public static ConnectionTableEntryV2 getConnectionTableEntryByIpV2() {
        return new ConnectionTableEntryV2(
                "vantage-99993", "vantage-99999", 1, """
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
                        }""", getCurrentTimestampDbFormat()
        );
    }

    @Deprecated
    public static ConnectionTableEntryV2 getConnectionTableEntryByPhoneV2() {
        return new ConnectionTableEntryV2(
                "vantage-99994", "vantage-99999", 1, """
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
                        }""", getCurrentTimestampDbFormat()
        );
    }

    @Deprecated
    public static ConnectionTableEntryV2 getConnectionTableEntryByPayoutV2() {
        return new ConnectionTableEntryV2(
                "vantage-99995", "vantage-99999", 1, """
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
                        }""", getCurrentTimestampDbFormat()
        );
    }

    @Deprecated
    public static ConnectionTableEntryV2 getConnectionTableEntryForDepthV2() {
        return new ConnectionTableEntryV2(
                "vantage-99997", "vantage-99999", 2, """
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
                        }""", getCurrentTimestampDbFormat()
        );
    }
}
