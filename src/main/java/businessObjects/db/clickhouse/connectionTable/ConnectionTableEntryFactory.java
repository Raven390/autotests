package businessObjects.db.clickhouse.connectionTable;

import helpers.data.ClientHelper;

public class ConnectionTableEntryFactory {

    public static ConnectionTableEntry getConnectionTableEntry(ClientHelper userFrom, ClientHelper userTo) {
        return new ConnectionTableEntry(
                userFrom.getUcid(), userTo.getUcid(), "Same Person", 1d, "[{\"connectionAttributeName\": \"payout\", \"connectionAttributeValue\": \"535456**** **0344\", \"sourceAttributeValue\": \"535456**** **0344\", \"relationType\": \"exact\"}]"
        );
    }

    public static ConnectionTableEntry getConnectionTableEntryLvl2(ClientHelper userFrom, ClientHelper userTo) {
        return new ConnectionTableEntry(
                userFrom.getUcid(), userTo.getUcid(), "Same Person", 0.5d, "[{\"connectionAttributeName\": \"payout\", \"connectionAttributeValue\": \"535456**** **0344\", \"sourceAttributeValue\": \"535456**** **0344\", \"relationType\": \"exact\"}]"
        );
    }

    public static ConnectionTableEntry getConnectionTableEntryForFiltration(ClientHelper userFrom,
            ClientHelper userTo) {
        return new ConnectionTableEntry(
                userFrom.getUcid(), userTo.getUcid(), "Same Network", 0.2d, "[{\"connectionAttributeName\": \"email\", \"connectionAttributeValue\": \"wcJSyCAcOAT2WPuuoN+t6Z/WaBiHSPPa\", \"sourceAttributeValue\": \"wcJSyCAcOAT2WPuuoN+t6Z/WaBiHSPPa\", \"relationType\": \"exact\"}]"
        );
    }
}
