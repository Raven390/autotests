package business_objects.db.clickhouse.crm_tb_kyc_files;

import helpers.data.ClientHelper;

import static utils.Constants.FILE_KYC_NAME;
import static utils.Utils.*;

import java.math.BigInteger;

public class CrmTbKycFilesFactory {

    private CrmTbKycFilesFactory() {
    }

    public static CrmTbKycFilesObject generateKycFilesObjectByClient(ClientHelper client) {
        return CrmTbKycFilesObject.builder().sourceIdSt(1).brandUid(1).brand(client.getBrand()).regulator(client.getRegulator()).userId(Long.valueOf(client.getUserId())).ucid(client.getUcid()).id(BigInteger.valueOf(getRandomLongPositive())).proofId(BigInteger.valueOf(getRandomLongPositive())).docFileId(BigInteger.valueOf(getRandomLongPositive())).fileName(FILE_KYC_NAME).filePath(FILE_KYC_NAME).fileTypeId(12).fileType("ID_PROOF").uploadTime(getCurrentTimestampDbFormat()).uploadTimeUtc(getCurrentTimestampDbFormat()).updateTime(getCurrentTimestampDbFormat()).updateTimeUtc(getCurrentTimestampDbFormat()).isDel(0).lastUpdated(getCurrentTimestampDbFormat()).build();
    }
}
