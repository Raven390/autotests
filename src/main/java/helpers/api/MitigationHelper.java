package helpers.api;

import business_objects.api.mitigation_service.PostRestrictionRequestBody;
import business_objects.db.mitigation_service_db.ClientsRestriction;
import io.qameta.allure.Allure;
import okhttp3.Response;

import java.util.List;

import static business_objects.api.mitigation_service.MitigationServiceRequest.postRestriction;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class MitigationHelper {


    public static void setGeneralRestrictionApi(String ucid, String restrictionCode) throws Exception {

        PostRestrictionRequestBody postRestrictionRequestBody = new PostRestrictionRequestBody(
                ucid, restrictionCode, "GENERAL", null, null, "set test data", new PostRestrictionRequestBody.UpdatedBy("API", "QA")
        );

        Allure.step("Send and check first request");
        Response response = postRestriction(postRestrictionRequestBody);
        assertEquals(response.code(), 200);
    }

    public static void checkAppliedRestriction(List<ClientsRestriction> clientsRestrictions, Integer restrictionCode) {
        for (ClientsRestriction clientsRestriction : clientsRestrictions) {
            if ("APPLIED".equals(clientsRestriction.status) && clientsRestriction.restrictionId == restrictionCode.longValue()) {
                return;
            }
            fail("expected restriction is not in applied restrictions");
        }
    }
}
