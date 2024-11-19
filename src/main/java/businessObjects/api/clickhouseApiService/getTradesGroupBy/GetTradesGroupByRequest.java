package businessObjects.api.clickhouseApiService.getTradesGroupBy;

import helpers.httpHelper.HttpHelper;
import io.qameta.allure.Step;
import okhttp3.Response;

import java.io.IOException;
import java.util.Map;

import static utils.ConfigFactory.*;

public class GetTradesGroupByRequest {

    @Step("Get client trades group by symbol")
    public static Response getTradesGroupBySymbol(Map<String, Object> paramsMap) throws IOException {
        return new HttpHelper().sendGetRequest(CLICKHOUSE_API_BASE_PATH + CLICKHOUSE_API_GET_TRADES_GROUP_BY, null, paramsMap);
    }
}
