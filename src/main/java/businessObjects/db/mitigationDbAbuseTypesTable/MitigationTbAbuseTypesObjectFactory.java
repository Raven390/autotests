package businessObjects.db.mitigationDbAbuseTypesTable;

import helpers.data.ClientHelper;
import io.qameta.allure.Step;

public class MitigationTbAbuseTypesObjectFactory {
    @Step("Generate user object by user id")
    public static MitigationTbAbuseTypesObject generateAbuseTypesClient(ClientHelper client) {
        return new MitigationTbAbuseTypesObject(1);
    }
}
