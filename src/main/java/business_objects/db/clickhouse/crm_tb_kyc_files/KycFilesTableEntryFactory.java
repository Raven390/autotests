package business_objects.db.clickhouse.crm_tb_kyc_files;

import helpers.data.ClientHelper;

import static utils.Constants.FILE_KYC_NAME;
import static utils.Utils.*;

public class KycFilesTableEntryFactory {

    public static KycFilesTableEntry getKycFile(ClientHelper client) {
        return new KycFilesTableEntry(getRandomIntPositive(), getRandomUuidString(), 777, 473, client.getUserId(), client.getUcid(), client.getBrand(), client.getRegulator(), FILE_KYC_NAME, FILE_KYC_NAME, 12, "ID_PROOF", getCurrentTimestampDbFormat(), getCurrentTimestampDbFormat(), getCurrentTimestampDbFormat());
    }

    public static KycFilesTableEntry getStaticKycFile(ClientHelper client) {
        String updateTime = "2024-11-21 11:30:50.030000000";
        return new KycFilesTableEntry(getRandomIntPositive(), "f538e5db-5df8-43ae-863e-72c9f5ee5192", 777, 473, client.getUserId(), client.getUcid(), client.getBrand(), client.getRegulator(), FILE_KYC_NAME, FILE_KYC_NAME, 12, "ID_PROOF", updateTime, updateTime, updateTime);
    }
}
