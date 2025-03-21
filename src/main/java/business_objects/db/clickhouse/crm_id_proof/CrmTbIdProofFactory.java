package business_objects.db.clickhouse.crm_id_proof;

import helpers.data.ClientHelper;
import net.datafaker.Faker;

import java.util.random.RandomGenerator;

import static utils.Utils.getCurrentTimestampDbFormat;
import static utils.Utils.getRandomUuidString;

public class CrmTbIdProofFactory {
    static Faker faker = new Faker();

    public static CrmTbIdProofObject generateIdProofObject(ClientHelper client) {
        CrmTbIdProofObject proof = new CrmTbIdProofObject();
        proof.setId(RandomGenerator.getDefault().nextInt(525_200));
        proof.setUcid(client.getUcid());
        proof.setUid(getRandomUuidString());
        proof.setUserId(client.getUserId());
        proof.setBrand(client.getBrand());
        proof.setRegulator(client.getRegulator());
        proof.setCreateTime("2024-11-21 11:17:50.030000000");
        proof.setCreateTimeUtc("2024-11-21 11:17:50.030000000");
        proof.setFirstName(faker.name().name());
        proof.setMiddleName(faker.starWars().wookieWords());
        proof.setLastName(faker.name().lastName());
        proof.setDateOfBirth("1961-02-01");
        proof.setStatusId(1);
        proof.setStatus("SUBMITTED");
        proof.setAuditor("Auditman Tesserovich");
        proof.setNotes("Test Note");
        proof.setReason("Test Reason");
        proof.setDocumentType("Document Test Type");
        proof.setDocumentNumber("Document Test number");
        proof.setNationalityId(1);
        proof.setNationality("Nationality");
        proof.setAuditId(21);
        proof.setAuditType("Audit Test Type");
        proof.setFileTypeId(12);
        proof.setFileType("Test File Type");
        proof.setLastUpdated(getCurrentTimestampDbFormat());


        return proof;
    }

}
