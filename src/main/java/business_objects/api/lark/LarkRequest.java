package business_objects.api.lark;

import static utils.ConfigFactory.*;

import business_objects.api.lark.TenantAccessToken.TenantAccessTokenRequest;
import helpers.http_helper.HttpHelper;
import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import okhttp3.Response;

public class LarkRequest {

    public static Response getTenantToken(String appId, String appSecret) throws IOException {
        return new HttpHelper()
                .sendPostRequest(
                        LARK_BASE_URL + LARK_GET_TENANT_TOKEN_PATH,
                        Map.of("Content-Type", "application/json; charset=utf-8"),
                        null,
                        new TenantAccessTokenRequest(appId, appSecret));
    }

    public static Response getMessagesChatLast10Minutes(String tenantToken, String containerId) throws IOException {
        return new HttpHelper()
                .sendGetRequest(
                        LARK_BASE_URL + LARK_GET_MESSAGE_HISTORY_PATH,
                        Map.of(
                                "Content-Type",
                                "application/json; charset=utf-8",
                                "Authorization",
                                String.format("Bearer %s", tenantToken)),
                        Map.of(
                                "container_id_type",
                                "chat",
                                "container_id",
                                containerId,
                                "start_time",
                                String.valueOf(Instant.now().minusSeconds(600).getEpochSecond()),
                                "end_time",
                                String.valueOf(Instant.now().plusSeconds(600).getEpochSecond())));
    }
}
