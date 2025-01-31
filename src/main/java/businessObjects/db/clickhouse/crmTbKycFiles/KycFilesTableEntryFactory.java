package businessObjects.db.clickhouse.crmTbKycFiles;

import helpers.data.ClientHelper;

import static utils.Utils.*;

public class KycFilesTableEntryFactory {

    public static KycFilesTableEntry getKycFile(ClientHelper client) {
        return new KycFilesTableEntry(getRandomIntPositive(), getRandomUuidString(), 777, 473, client.getUserId(), client.getUcid(), client.getBrand(), client.getRegulator(), "/POA.jpg", "/POA.jpg", 12, "ID_PROOF", getCurrentTimestampDbFormat(), getCurrentTimestampDbFormat(), getCurrentTimestampDbFormat());
    }

    public static KycFilesTableEntry getStaticKycFile(ClientHelper client) {
        return new KycFilesTableEntry(getRandomIntPositive(), "f538e5db-5df8-43ae-863e-72c9f5ee5192", 777, 473, client.getUserId(), client.getUcid(), client.getBrand(), client.getRegulator(), "/POA.jpg", "/POA.jpg", 12, "ID_PROOF", "2024-11-21 11:30:50.030000000", "2024-11-21 11:30:50.030000000", "2024-11-21 11:30:50.030000000");
    }
}
