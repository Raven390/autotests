package helpers.api;

import business_objects.api.mitigation_service.PostRestrictionRequestBody;
import helpers.data.ClientHelper;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import okhttp3.Response;

import java.io.IOException;

import static business_objects.api.mitigation_service.MitigationServiceRequest.cancelRestriction;
import static business_objects.api.mitigation_service.MitigationServiceRequest.postRestriction;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RestrictionHelper {
    @Step("Set restriction through API")
    public static void setRestrictionAPIGeneral(String ucid, String code) throws IOException {
        Allure.step("Set restriction through API GENERAL");
        PostRestrictionRequestBody postRestrictionRequestBody = new PostRestrictionRequestBody(
                ucid, code, "GENERAL", null, null, "Integration test", new PostRestrictionRequestBody.UpdatedBy("test", "automation")
        );
        Response response = postRestriction(postRestrictionRequestBody);
        assertEquals(200, response.code());
    }

    @Step("Set restriction through API")
    public static void setRestrictionAPIGeneral(String ucid, String code, String comment) throws IOException {
        Allure.step("Set restriction through API GENERAL");
        PostRestrictionRequestBody postRestrictionRequestBody = new PostRestrictionRequestBody(
                ucid, code, "GENERAL", null, null, comment, new PostRestrictionRequestBody.UpdatedBy("test", "automation")
        );
        Response response = postRestriction(postRestrictionRequestBody);
        assertEquals(200, response.code());
    }

    @Step
    public static void setRestrictionAPITrade(String ucid, int accId, int serverId, String code) throws IOException {
        Allure.step("Set restriction through API TRADE");
        PostRestrictionRequestBody postRestrictionRequestBody = new PostRestrictionRequestBody(
                ucid, code, "TRADING", accId, serverId, "Integration test", new PostRestrictionRequestBody.UpdatedBy("string", "string")
        );
        Response response = postRestriction(postRestrictionRequestBody);
        assertEquals(200, response.code());
    }

    @Deprecated
    public static void setRestrictionAPIGeneral(String ucid, String code, String applyReason, String updatedBySystem,
            String updatedByUser) throws IOException {
        Allure.step("Set restriction through API GENERAL");
        PostRestrictionRequestBody postRestrictionRequestBody = new PostRestrictionRequestBody(
                ucid, code, "GENERAL", null, null, applyReason, new PostRestrictionRequestBody.UpdatedBy(updatedBySystem, updatedByUser)
        );
        Response response = postRestriction(postRestrictionRequestBody);
        assertNotNull(response);
        assertEquals(200, response.code());
    }

    @Deprecated
    public static String setRestrictionAPIGeneralResponse(String ucid, String code, String applyReason,
            String updatedBySystem,
            String updatedByUser) throws IOException {
        Allure.step("Set restriction through API GENERAL");
        PostRestrictionRequestBody postRestrictionRequestBody = new PostRestrictionRequestBody(
                ucid, code, "GENERAL", null, null, applyReason, new PostRestrictionRequestBody.UpdatedBy(updatedBySystem, updatedByUser)
        );
        Response response = postRestriction(postRestrictionRequestBody);
        assertNotNull(response);
        assertEquals(response.code(), 200);
        assertThat(response.body(), is(notNullValue()));
        return response.body().string();
    }

    @Step
    public static String setRestrictionAPITradeResponse(String ucid, String code, int accId, int serverId,
            String applyReason,
            String updatedBySystem, String updatedByUser) throws IOException {
        Allure.step("Set restriction through API TRADE");
        PostRestrictionRequestBody postRestrictionRequestBody = new PostRestrictionRequestBody(
                ucid, code, "TRADING", accId, serverId, applyReason, new PostRestrictionRequestBody.UpdatedBy(updatedBySystem, updatedByUser)
        );
        Response response = postRestriction(postRestrictionRequestBody);
        assertNotNull(response);
        assertEquals(200, response.code());
        assertThat(response.body(), is(notNullValue()));
        return response.body().string();
    }

    public static void addCancelledRestriction(ClientHelper client, String type, String code) throws IOException {
        //add bonus restriction
        Integer restrictionId = postRestriction(client, type, code).id;
        //cancel bonus restriction
        assertThat("Assert response code", cancelRestriction(restrictionId).code(), is(204));
    }
}
