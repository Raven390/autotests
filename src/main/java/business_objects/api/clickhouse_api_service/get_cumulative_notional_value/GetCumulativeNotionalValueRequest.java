package business_objects.api.clickhouse_api_service.get_cumulative_notional_value;

import static utils.ConfigFactory.*;

import helpers.http_helper.HttpHelper;
import io.qameta.allure.Step;
import java.io.IOException;
import java.util.Map;
import okhttp3.Response;

public class GetCumulativeNotionalValueRequest {

    @Step("Get cumulative notional value")
    public static Response getCumulativeNotionalValue(Map<String, Object> queryParamsMap) throws IOException {
        return new HttpHelper()
                .sendGetRequest(
                        CLICKHOUSE_API_BASE_TEST + CLICKHOUSE_API_GET_CUMULATIVE_NOTIONAL_VALUE, null, queryParamsMap);
    }
}
