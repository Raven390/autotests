package business_objects.api.mitigation_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.data.ClientHelper;
import helpers.http_helper.HttpHelper;
import okhttp3.Response;

import java.io.IOException;
import java.util.Map;

import static utils.ConfigFactory.*;

public class MitigationServiceRequest {
    public static final ObjectMapper objectMapper = new ObjectMapper();

    public static Response getRestrictionCatalog() throws IOException {
        return new HttpHelper().sendGetRequest(MITIGATION_SERVICE_BASE_PATH + MITIGATION_SERVICE_GET_RESTRICTION_CATALOG, null, null);
    }

    public static Response postRestriction(PostRestrictionRequestBody postRestrictionRequestBody) throws IOException {
        return new HttpHelper().sendPostRequest(MITIGATION_SERVICE_BASE_PATH + MITIGATION_SERVICE_RESTRICTIONS, null, null, postRestrictionRequestBody);
    }

    public static PostRestrictionResponse postRestriction(ClientHelper client, String type, String code)
            throws IOException {
        PostRestrictionRequestBody postRestrictionRequestBody = new PostRestrictionRequestBody(
                client.getUcid(), code, type, client.getTradingAccount(), client.getServerId(), "Integration test", new PostRestrictionRequestBody.UpdatedBy("API", "QA")
        );
        return objectMapper.readValue(postRestriction(postRestrictionRequestBody).body().string(), PostRestrictionResponse.class);
    }

    public static Response putRestriction(PostRestrictionRequestBody putRestrictionRequestBody) throws IOException {
        return new HttpHelper().sendPutRequest(MITIGATION_SERVICE_BASE_PATH + MITIGATION_SERVICE_RESTRICTIONS, null, null, putRestrictionRequestBody);
    }

    public static Response putRestrictionV3(NewTradingEnvRestrictionRequestBody putRestrictionRequestBody)
            throws IOException {
        return new HttpHelper().sendPutRequest(MITIGATION_SERVICE_BASE_PATH + MITIGATION_SERVICE_RESTRICTIONS_V3, null, null, putRestrictionRequestBody);
    }

    public static Response getRestrictionsByUcid(String ucid) throws IOException {
        return new HttpHelper().sendGetRequest(MITIGATION_SERVICE_BASE_PATH + MITIGATION_SERVICE_RESTRICTIONS, null, Map.of("ucid", ucid));
    }

    public static Response getRestrictionsByUcidV3(String ucid) throws IOException {
        return new HttpHelper().sendGetRequest(MITIGATION_SERVICE_BASE_PATH + MITIGATION_SERVICE_RESTRICTIONS_V3, null, Map.of("ucid", ucid));
    }

    public static Response cancelRestriction(Integer restrictionId)
            throws IOException {
        return new HttpHelper().sendPostRequest(MITIGATION_SERVICE_BASE_PATH + MITIGATION_SERVICE_CANCEL_RESTRICTION.replace("{restrictionId}", restrictionId.toString()), null, null, new CancelRestrictionRequestBody("Test", new CancelRestrictionRequestBody.UpdatedBy("API", "QA")));
    }

    public static Response enableCRMEmulator() throws IOException {
        return new HttpHelper().sendPostRequest(BASE_URL_CRM_EMULATOR + "/mitigation/enabled", null, null, "true");
    }

    public static Response disableCRMEmulator() throws IOException {
        return new HttpHelper().sendPostRequest(BASE_URL_CRM_EMULATOR + "/mitigation/enabled", null, null, "false");
    }

    public static Response getRestrictionCatalogInsight(String token) throws IOException {
        return new HttpHelper().sendGetRequest(MITIGATION_SERVICE_INSIGHT_BASE_PATH + MITIGATION_SERVICE_GET_RESTRICTION_CATALOG, Map.of("Authorization", String.format("Bearer %s", token)), null);
    }

    public static Response postRestrictionInsight(String token, PostRestrictionRequestBody postRestrictionRequestBody)
            throws IOException {
        return new HttpHelper().sendPostRequest(MITIGATION_SERVICE_INSIGHT_BASE_PATH + MITIGATION_SERVICE_RESTRICTIONS, Map.of("Authorization", String.format("Bearer %s", token)), null, postRestrictionRequestBody);
    }

    public static Response getRestrictionsByAccountServerIdInsight(String token, Integer account, Integer serverId)
            throws IOException {
        return new HttpHelper().sendGetRequest(MITIGATION_SERVICE_INSIGHT_BASE_PATH + MITIGATION_SERVICE_RESTRICTIONS, Map.of("Authorization", String.format("Bearer %s", token)), Map.of("accountId", account, "serverId", serverId));
    }

    public static Response cancelRestrictionInsight(String token, Integer id,
            CancelRestrictionRequestBody cancelRestrictionRequestBody)
            throws IOException {
        return new HttpHelper().sendPostRequest(String.format(MITIGATION_SERVICE_INSIGHT_BASE_PATH + MITIGATION_SERVICE_CANCEL_RESTRICTION, id), Map.of("Authorization", String.format("Bearer %s", token)), null, cancelRestrictionRequestBody);
    }

    public static Response postRestrictionByBit(PostRestrictionByBitRequest restrictionBody) throws IOException {
        return new HttpHelper().sendPostRequest(MITIGATION_SERVICE_BASE_PATH + MITIGATION_SERVICE_RESTRICTIONS_BYBIT, null, null, restrictionBody);
    }

    public static Response cancelRestrictionByIdByBit(CancelRestrictionByBitRequest cancelRestrictionRequestBody)
            throws IOException {
        return new HttpHelper().sendPostRequest(String.format(MITIGATION_SERVICE_BASE_PATH + MITIGATION_SERVICE_CANCEL_RESTRICTION_BYBIT), null, null, cancelRestrictionRequestBody);
    }
}
