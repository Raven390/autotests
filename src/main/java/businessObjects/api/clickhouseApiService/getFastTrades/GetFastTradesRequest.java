package businessObjects.api.clickhouseApiService.getFastTrades;

import helpers.httpHelper.HttpHelper;
import io.qameta.allure.Step;
import okhttp3.Response;

import java.io.IOException;
import java.util.Map;

import static utils.ConfigFactory.*;

public class GetFastTradesRequest {

    @Step("Get client fast trades")
    public static Response getFastTrades(Map<String, Object> paramsMap) throws IOException {
        return new HttpHelper().sendGetRequest(CLICKHOUSE_API_BASE_TEST + CLICKHOUSE_API_GET_FAST_TRADES, null, paramsMap);
    }

}
