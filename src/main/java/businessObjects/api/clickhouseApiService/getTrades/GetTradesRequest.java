package businessObjects.api.clickhouseApiService.getTrades;

import helpers.httpHelper.HttpHelper;
import io.qameta.allure.Step;
import okhttp3.Response;

import java.io.IOException;
import java.util.Map;

import static utils.ConfigFactory.*;

public class GetTradesRequest {

    @Step("Get client trades")
    public static Response getTrades(Map<String, Object> paramsMap) throws IOException {
        return new HttpHelper().sendGetRequest(CLICKHOUSE_API_BASE_TEST + CLICKHOUSE_API_GET_TRADES, null, paramsMap);
    }

}
