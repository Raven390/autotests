package businessObjects.api.clickhouseApiService.getUnclosedTrades;

import helpers.httpHelper.HttpHelper;
import io.qameta.allure.Step;
import okhttp3.Response;

import java.io.IOException;
import java.util.Map;

import static utils.ConfigFactory.*;

public class GetUnclosedTradesRequest {

    @Step("Get clients unclosed trades")
    public static Response getUnclosedTrades(Map<String, Object> paramsMap) throws IOException {
        return new HttpHelper().sendGetRequest(CLICKHOUSE_API_BASE_TEST + CLICKHOUSE_API_GET_UNCLOSED_TRADES, null, paramsMap);
    }

}
