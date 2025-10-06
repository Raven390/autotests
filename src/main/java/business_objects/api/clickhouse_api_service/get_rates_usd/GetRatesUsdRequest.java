package business_objects.api.clickhouse_api_service.get_rates_usd;

import helpers.http_helper.HttpHelper;
import io.qameta.allure.Step;
import okhttp3.Response;

import java.io.IOException;
import java.util.Map;

import static utils.ConfigFactory.*;

public class GetRatesUsdRequest {

    @Step("Get rate usd")
    public static Response getRateUsd(Map<String, Object> paramsMap) throws IOException {
        return new HttpHelper().sendGetRequest(CLICKHOUSE_API_BASE_TEST + CLICKHOUSE_API_GET_RATE_USD, null, paramsMap);
    }

}
