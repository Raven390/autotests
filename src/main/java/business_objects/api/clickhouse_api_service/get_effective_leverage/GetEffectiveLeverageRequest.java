package business_objects.api.clickhouse_api_service.get_effective_leverage;

import static utils.ConfigFactory.*;

import helpers.http_helper.HttpHelper;
import io.qameta.allure.Step;
import java.io.IOException;
import java.util.Map;
import okhttp3.Response;

public class GetEffectiveLeverageRequest {

    @Step("Get effective leverage")
    public static Response getEffectiveLeverage(Map<String, Object> queryParamsMap) throws IOException {
        return new HttpHelper()
                .sendGetRequest(CLICKHOUSE_API_BASE_TEST + CLICKHOUSE_API_GET_EFFECTIVE_LEVERAGE, null, queryParamsMap);
    }
}
