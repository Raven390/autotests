package business_objects.api.clickhouse_api_service.get_market_manipulator_flag;

import static utils.ConfigFactory.*;

import helpers.http_helper.HttpHelper;
import io.qameta.allure.Step;
import java.io.IOException;
import java.util.Map;
import okhttp3.Response;

public class GetMarketManipulatorFlagRequest {

    @Step("Get market manipulator flag")
    public static Response getMarketManipulatorFlag(Map<String, Object> paramsMap) throws IOException {
        return new HttpHelper()
                .sendGetRequest(CLICKHOUSE_API_BASE_TEST + CLICKHOUSE_API_GET_MARKET_MANIPULATOR_FLAG, null, paramsMap);
    }
}
