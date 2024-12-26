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
                userFrom.getUcid(), userTo.getUcid(), "Same Network", 0.2d, "[{\"connectionAttributeName\": \"email\", \"connectionAttributeValue\": \"matisse@gmx.net\", \"sourceAttributeValue\": \"maatiuss@gmail.com\", \"relationType\": \"similar\"}]"
        );
    }

    public static ConnectionTableEntry getConnectionTableEntryForUi(ClientHelper userFrom, ClientHelper userTo) {
        return new ConnectionTableEntry(
                userFrom.getUcid(), userTo.getUcid(), "Same Person", 1d, "[{\"connectionAttributeName\": \"payout\", \"connectionAttributeValue\": \"535456**** **0344\", \"sourceAttributeValue\": \"535456**** **0344\", \"relationType\": \"exact\"}, {\"connectionAttributeName\": \"device\", \"connectionAttributeValue\": \"4a25971ab724427eb8fc24a257c5b2df\", \"sourceAttributeValue\": \"4a25971ab724427eb8fc24a257c5b2df\", \"relationType\": \"exact\"}, {\"connectionAttributeName\": \"ip\", \"connectionAttributeValue\": \"92.14.100.34\", \"sourceAttributeValue\": \"92.14.100.34\", \"relationType\": \"exact\"}, {\"connectionAttributeName\": \"digital\", \"connectionAttributeValue\": \"5784170d787442f8945926a2c9b24c40\", \"sourceAttributeValue\": \"5784170d787442f8945926a2c9b24c40\", \"relationType\": \"exact\"}, {\"connectionAttributeName\": \"email\", \"connectionAttributeValue\": \"matisse@gmx.net\", \"sourceAttributeValue\": \"maatiuss@gmail.com\", \"relationType\": \"similar\"}, {\"connectionAttributeName\": \"phone\", \"connectionAttributeValue\": \"F2jTWljlC4HSI0uMpPz5Yw==\", \"sourceAttributeValue\": \"F2jTWljlC4HSI0uMpPz5Yw==\", \"relationType\": \"exact\"}, {\"connectionAttributeName\": \"document\", \"connectionAttributeValue\": \"3110200460092\", \"sourceAttributeValue\": \"3110200460092\", \"relationType\": \"exact\"}, {\"connectionAttributeName\": \"name+dateofbirth\", \"connectionAttributeValue\": \"kanjana sirajindapirom 1978-02-17\", \"sourceAttributeValue\": \"kanjana sirajindapirom 1978-02-17\", \"relationType\": \"exact\"}, {\"connectionAttributeName\": \"session\", \"connectionAttributeValue\": \"63767f3e2a9340efafd32c354660b128\", \"sourceAttributeValue\": \"63767f3e2a9340efafd32c354660b128\", \"relationType\": \"exact\"}]"
        );
    }
}
