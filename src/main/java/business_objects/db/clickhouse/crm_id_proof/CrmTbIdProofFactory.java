package business_objects.db.clickhouse.crm_id_proof;

import business_objects.db.clickhouse.crm_tb_kyc_files.CrmTbKycFilesObject;
import helpers.data.ClientHelper;
import net.datafaker.Faker;

public class CrmTbIdProofFactory {
    static Faker faker = new Faker();

    public static CrmTbIdProofObject generateIdProofObjectByClient(ClientHelper client, CrmTbKycFilesObject kycFile) {
        return CrmTbIdProofObject.builder()
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
                .firstName(client.getFirstName())
                .middleName(faker.starWars().wookieWords())
                .lastName(client.getLastName())
                .dateOfBirth(client.getDateOfBirth())
                .statusId(1)
                .status("SUBMITTED")
                .auditor("Auditman Tesserovich")
                .notes("Test note")
                .pendingReason(1)
                .rejectReason(1)
                .reason("Test reason")
                .documentType("Passport")
                .documentNumber("Document Test number")
                .nationalityId(1)
                .nationality("AUSTRALIAN")
                .auditId(1)
                .auditType("GENERAL")
                .fileTypeId(kycFile.getFileTypeId())
                .fileType(kycFile.getFileType())
                .isDel(0)
                .lastUpdated(kycFile.getLastUpdated())
                .build();
    }
}
