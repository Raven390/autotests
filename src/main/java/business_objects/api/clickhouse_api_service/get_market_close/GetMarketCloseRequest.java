package business_objects.api.clickhouse_api_service.get_market_close;

import helpers.http_helper.HttpHelper;
import io.qameta.allure.Step;
import okhttp3.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static utils.ConfigFactory.*;

public class GetMarketCloseRequest {

    @Step("Get market close")
    public static Response getMarketClose(String symbol, String serverId, String date) throws IOException {
        Map<String, Object> queryParamsMap = new HashMap<>();
        queryParamsMap.put("symbol", symbol);
        queryParamsMap.put("serverId", serverId);
        queryParamsMap.put("date", date);
        return new HttpHelper().sendGetRequest(CLICKHOUSE_API_BASE_TEST + CLICKHOUSE_API_GET_MARKET_CLOSE, null, queryParamsMap);
    }

    @Step("Get market close")
    public static Response getMarketClose(Map<String, Object> queryParamsMap) throws IOException {
        return new HttpHelper().sendGetRequest(CLICKHOUSE_API_BASE_TEST + CLICKHOUSE_API_GET_MARKET_CLOSE, null, queryParamsMap);
    }

}
