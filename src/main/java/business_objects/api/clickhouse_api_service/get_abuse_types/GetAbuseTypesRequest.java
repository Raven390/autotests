package business_objects.api.clickhouse_api_service.get_abuse_types;

import static utils.ConfigFactory.*;

import helpers.http_helper.HttpHelper;
import io.qameta.allure.Step;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import okhttp3.Response;

public class GetAbuseTypesRequest {

    @Step("Get abuse types")
    public static Response getAbuseTypes(List<String> clientIds) throws IOException {
        Map<String, Object> queryParamsMap = new HashMap<>();
        queryParamsMap.put("clientIds", clientIds);
        return new HttpHelper()
                .sendGetRequest(CLICKHOUSE_API_BASE_TEST + CLICKHOUSE_API_GET_ABUSE_TYPES, null, queryParamsMap);
    }
}
