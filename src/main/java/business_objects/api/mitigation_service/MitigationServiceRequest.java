package business_objects.api.mitigation_service;

import helpers.http_helper.HttpHelper;
import okhttp3.Response;

import java.io.IOException;
import java.util.Map;

import static utils.ConfigFactory.*;

public class MitigationServiceRequest {

    public static Response getRestrictionCatalog() throws IOException {
        return new HttpHelper().sendGetRequest(MITIGATION_SERVICE_BASE_PATH + MITIGATION_SERVICE_GET_RESTRICTION_CATALOG, null, null);
    }

    public static Response postRestriction(PostRestrictionRequestBody postRestrictionRequestBody) throws IOException {
        return new HttpHelper().sendPostRequest(MITIGATION_SERVICE_BASE_PATH + MITIGATION_SERVICE_RESTRICTIONS, null, null, postRestrictionRequestBody);
    }

    public static Response putRestriction(PostRestrictionRequestBody putRestrictionRequestBody) throws IOException {
        return new HttpHelper().sendPutRequest(MITIGATION_SERVICE_BASE_PATH + MITIGATION_SERVICE_RESTRICTIONS, null, null, putRestrictionRequestBody);
    }

    public static Response getRestrictionsByUcid(String ucid) throws IOException {
        return new HttpHelper().sendGetRequest(MITIGATION_SERVICE_BASE_PATH + MITIGATION_SERVICE_RESTRICTIONS, null, Map.of("ucid", ucid));
    }

    public static Response cancelRestrictionById(Integer id, CancelRestrictionRequestBody cancelRestrictionRequestBody)
            throws IOException {
        return new HttpHelper().sendPostRequest(String.format(MITIGATION_SERVICE_BASE_PATH + MITIGATION_SERVICE_CANCEL_RESTRICTION, id), null, null, cancelRestrictionRequestBody);
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
}
