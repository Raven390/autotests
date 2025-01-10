package helpers.api;

import businessObjects.api.mitigationService.PostRestrictionRequestBody;
import io.qameta.allure.Allure;
import okhttp3.Response;

import static businessObjects.api.mitigationService.MitigationServiceRequest.postRestriction;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MitigationHelper {


    public static void setGeneralRestrictionApi(String ucid, String restrictionCode) throws Exception {

        PostRestrictionRequestBody postRestrictionRequestBody = new PostRestrictionRequestBody(
                ucid, restrictionCode, "GENERAL", null, null, "set test data", new PostRestrictionRequestBody.UpdatedBy("API", "QA")
        );

        Allure.step("Send and check first request");
        Response response = postRestriction(postRestrictionRequestBody);
        assertEquals(response.code(), 200);
    }
}
