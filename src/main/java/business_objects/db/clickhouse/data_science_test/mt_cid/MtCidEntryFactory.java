package business_objects.db.clickhouse.data_science_test.mt_cid;

import helpers.data.ClientHelper;

public class MtCidEntryFactory {
    public static MtCidTableEntry mtCidTableEntryForConnectionSearch(ClientHelper client) {
        return new MtCidTableEntry(client.getUcid(), client.getMtCid());
    }

    public static MtCidTableEntry mtCidTableEntryForConnectionSearch(ClientHelper client, String mtCid) {
        return new MtCidTableEntry(client.getUcid(), mtCid);
    }
}
