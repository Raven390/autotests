package helpers.api;

import static business_objects.api.mitigation_service.MitigationServiceRequest.cancelRestriction;
import static business_objects.api.mitigation_service.MitigationServiceRequest.postRestriction;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import business_objects.api.mitigation_service.DeleteTradingEnvRestrictionRequestBody;
import business_objects.api.mitigation_service.MitigationServiceRequest;
import business_objects.api.mitigation_service.NewTradingEnvRestrictionRequestBody;
import business_objects.api.mitigation_service.PostRestrictionRequestBody;
import helpers.data.ClientHelper;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import java.io.IOException;
import okhttp3.Response;

public class RestrictionHelper {
    @Step("Set restriction through API")
    public static void setRestrictionAPIGeneral(String ucid, String code) throws IOException {
        Allure.step("Set restriction through API GENERAL");
        PostRestrictionRequestBody postRestrictionRequestBody = new PostRestrictionRequestBody(
                ucid,
                code,
                "GENERAL",
                null,
                null,
                "Integration test",
                new PostRestrictionRequestBody.UpdatedBy("test", "automation"));
        Response response = postRestriction(postRestrictionRequestBody);
        assertEquals(200, response.code());
    }

    @Step("Set restriction through API")
    public static void setRestrictionAPIGeneral(String ucid, String code, String comment) throws IOException {
        Allure.step("Set restriction through API GENERAL");
        PostRestrictionRequestBody postRestrictionRequestBody = new PostRestrictionRequestBody(
                ucid,
                code,
                "GENERAL",
                null,
                null,
                comment,
                new PostRestrictionRequestBody.UpdatedBy("test", "automation"));
        Response response = postRestriction(postRestrictionRequestBody);
        assertEquals(200, response.code());
    }

    @Step
    public static void setRestrictionAPITrade(String ucid, int accId, int serverId, String code) throws IOException {
        Allure.step("Set restriction through API TRADE");
        PostRestrictionRequestBody postRestrictionRequestBody = new PostRestrictionRequestBody(
                ucid,
                code,
                "TRADING",
                accId,
                serverId,
                "Integration test",
                new PostRestrictionRequestBody.UpdatedBy("string", "string"));
        Response response = postRestriction(postRestrictionRequestBody);
        assertEquals(200, response.code());
    }

    @Deprecated
    public static void setRestrictionAPIGeneral(
            String ucid, String code, String applyReason, String updatedBySystem, String updatedByUser)
            throws IOException {
        Allure.step("Set restriction through API GENERAL");
        PostRestrictionRequestBody postRestrictionRequestBody = new PostRestrictionRequestBody(
                ucid,
                code,
                "GENERAL",
                null,
                null,
                applyReason,
                new PostRestrictionRequestBody.UpdatedBy(updatedBySystem, updatedByUser));
        Response response = postRestriction(postRestrictionRequestBody);
        assertNotNull(response);
        assertEquals(200, response.code());
    }

    @Deprecated
    public static String setRestrictionAPIGeneralResponse(
            String ucid, String code, String applyReason, String updatedBySystem, String updatedByUser)
            throws IOException {
        Allure.step("Set restriction through API GENERAL");
        PostRestrictionRequestBody postRestrictionRequestBody = new PostRestrictionRequestBody(
                ucid,
                code,
                "GENERAL",
                null,
                null,
                applyReason,
                new PostRestrictionRequestBody.UpdatedBy(updatedBySystem, updatedByUser));
        Response response = postRestriction(postRestrictionRequestBody);
        assertNotNull(response);
        assertEquals(response.code(), 200);
        assertThat(response.body(), is(notNullValue()));
        return response.body().string();
    }

    @Step
    public static String setRestrictionAPITradeResponse(
            String ucid,
            String code,
            int accId,
            int serverId,
            String applyReason,
            String updatedBySystem,
            String updatedByUser)
            throws IOException {
        Allure.step("Set restriction through API TRADE");
        PostRestrictionRequestBody postRestrictionRequestBody = new PostRestrictionRequestBody(
                ucid,
                code,
                "TRADING",
                accId,
                serverId,
                applyReason,
                new PostRestrictionRequestBody.UpdatedBy(updatedBySystem, updatedByUser));
        Response response = postRestriction(postRestrictionRequestBody);
        assertNotNull(response);
        assertEquals(200, response.code());
        assertThat(response.body(), is(notNullValue()));
        return response.body().string();
    }

    public static void addCancelledRestriction(ClientHelper client, String type, String code) throws IOException {
        // add bonus restriction
        Integer restrictionId = postRestriction(client, type, code).id;
        // cancel bonus restriction
        assertThat("Assert response code", cancelRestriction(restrictionId).code(), is(204));
    }

    @Step
    public static Response putRestrictionV3(NewTradingEnvRestrictionRequestBody putRestrictionRequestBody)
            throws IOException {
        Allure.step("Set trading env restriction through API");
        Response response = MitigationServiceRequest.putRestrictionV3(putRestrictionRequestBody);
        assertNotNull(response);
        assertEquals(200, response.code());
        return response;
    }

    @Step
    public static Response getClientRestrictionsV3(String ucid) throws IOException {
        Allure.step("Get client's restrictions through API");
        Response response = MitigationServiceRequest.getRestrictionsByUcidV3(ucid);
        assertNotNull(response);
        assertEquals(200, response.code());
        return response;
    }

    @Step("Set trading env restriction through API")
    public static Response postRestrictionV3(NewTradingEnvRestrictionRequestBody postRestrictionRequestBody)
            throws IOException {
        Response response = MitigationServiceRequest.postRestrictionV3(postRestrictionRequestBody);
        assertNotNull(response);
        assertEquals(200, response.code());
        return response;
    }

    @Step("Delete trading env restriction through API")
    public static Response deleteRestrictionV3(DeleteTradingEnvRestrictionRequestBody deleteRestrictionRequestBody)
            throws IOException {
        Response response = MitigationServiceRequest.deleteRestrictionV3(deleteRestrictionRequestBody);
        assertNotNull(response);
        assertEquals(200, response.code());
        return response;
    }
}
