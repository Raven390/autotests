package businessObjects.db.clickhouse.crmTbKycFiles;

import helpers.data.ClientHelper;

import static utils.Utils.*;

public class KycFilesTableEntryFactory {

    public static KycFilesTableEntry getKycFile(ClientHelper client) {
        return new KycFilesTableEntry(getRandomIntPositive(), getRandomUuidString(), 777, 473, client.getUserId(), client.getUcid(), client.getBrand(), client.getRegulator(), "/POA.jpg", "/POA.jpg", 12, "ID_PROOF", getCurrentTimestampDbFormat(), getCurrentTimestampDbFormat(), getCurrentTimestampDbFormat());
    }
}
