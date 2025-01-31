package businessObjects.db.clickhouse.crmIdProof;

import helpers.data.ClientHelper;
import net.datafaker.Faker;

import java.util.random.RandomGenerator;

import static utils.Utils.getRandomUuidString;

public class CrmTbIdProofFactory {
    static Faker faker = new Faker();

    public static CrmTbIdProofObject generateIdProofObject(ClientHelper client) {
        return new CrmTbIdProofObject(RandomGenerator.getDefault().nextInt(525_200), getRandomUuidString(), client.getUcid(), client.getUserId(), client.getBrand(), client.getRegulator(), "2024-11-21 11:17:50.030000000", "2024-12-21 11:17:50.030000000", faker.name().name(), faker.starWars().wookieWords(), faker.name().lastName(), "1961-02-01", 1, "SUBMITTED", "Auditman Tesserovich", "Test Note", "Test Reason", "Document Test Type", "Document Test number", 1, "Test Nationality", 21, "Audit Test Type", 12, "Test File Type", "2024-11-21 11:17:50.030000000");
    }

}
