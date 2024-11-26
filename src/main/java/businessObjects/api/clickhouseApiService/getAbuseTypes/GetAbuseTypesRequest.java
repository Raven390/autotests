package businessObjects.api.clickhouseApiService.getAbuseTypes;

import helpers.httpHelper.HttpHelper;
import io.qameta.allure.Step;
import okhttp3.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static utils.ConfigFactory.*;

public class GetAbuseTypesRequest {

    @Step("Post abuse types")
    public static Response getAbuseTypes(List<String> clientIds) throws IOException {
        Map<String, Object> queryParamsMap = new HashMap<>();
        queryParamsMap.put("clientIds", clientIds);
        return new HttpHelper().sendGetRequest(CLICKHOUSE_API_BASE_PATH + CLICKHOUSE_API_GET_ABUSE_TYPES, null, queryParamsMap);
    }

}
