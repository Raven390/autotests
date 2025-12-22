package business_objects.db.clickhouse.crm_tb_address_proof;

import static utils.Utils.*;

import business_objects.db.clickhouse.crm_tb_kyc_files.CrmTbKycFilesObject;
import helpers.data.ClientHelper;
import net.datafaker.Faker;

public class CrmTbAddressProofFactory {

    private CrmTbAddressProofFactory() {}

    static Faker faker = new Faker();

    public static CrmTbAddressProofObject generateAddressProofObjectByClient(
            ClientHelper client, CrmTbKycFilesObject kycFile) {
        return CrmTbAddressProofObject.builder()
                .sourceIdSt(kycFile.getSourceIdSt())
                .brandUid(kycFile.getBrandUid())
                .brand(client.getBrand())
                .regulator(client.getRegulator())
                .userId(Long.valueOf(client.getUserId()))
                .ucid(client.getUcid())
                .id(kycFile.getProofId())
                .createTime(kycFile.getUploadTime())
                .createTimeUtc(kycFile.getUploadTimeUtc())
                .updateTime(kycFile.getUpdateTime())
                .updateTimeUtc(kycFile.getUpdateTimeUtc())
                .postcode(faker.address().zipCode())
                .countryId(1)
                .country("Australia")
                .state("Melbourne")
                .city("Melbourne")
                .address(faker.address().streetAddress())
                .statusId(1)
                .status("SUBMITTED")
                .auditor("Auditman Tesserovich")
                .notes("Test note")
                .pendingReason(1)
                .rejectReason(1)
                .reason("Test reason")
                .translateAddress(faker.address().streetAddress())
                .auditId(1)
                .auditType("Audit test type")
                .fileTypeId(kycFile.getFileTypeId())
                .fileType(kycFile.getFileType())
                .isDel(0)
                .lastUpdated(getCurrentTimestampDbFormat())
                .build();
    }
}
