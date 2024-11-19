package businessObjects.api.clickhouseApiService.getMirrorAccountsByTrades;

import helpers.httpHelper.HttpHelper;
import io.qameta.allure.Step;
import okhttp3.Response;

import java.io.IOException;
import java.util.Map;

import static utils.ConfigFactory.*;

public class GetMirrorAccountsByTradesRequest {

    @Step("Get mirror account by trades")
    public static Response getMirrorAccountsByTrades(Map<String, Object> paramsMap) throws IOException {
        return new HttpHelper().sendGetRequest(CLICKHOUSE_API_BASE_PATH + CLICKHOUSE_API_GET_MIRROR_ACCOUNTS_BY_TRADES_PATH, null, paramsMap);
    }

}
