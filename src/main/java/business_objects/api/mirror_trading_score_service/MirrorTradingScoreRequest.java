package business_objects.api.mirror_trading_score_service;

import static utils.ConfigFactory.*;

import helpers.data.ClientHelper;
import helpers.http_helper.HttpHelper;
import io.qameta.allure.Step;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import okhttp3.Response;

public class MirrorTradingScoreRequest {

    @Step("Get Mirror trading score by ucid")
    public static Response getMirrorTradingScore(ClientHelper client) throws IOException, InterruptedException {
        Response response;
        int count = 0;
        Map<String, Object> queryParamsMap = new HashMap<>();
        queryParamsMap.put("clientId", client.getUcid());
        do {
            response = new HttpHelper()
                    .sendGetRequest(
                            CLICKHOUSE_API_BASE_TEST + MIRROR_TRADING_SCORE_SERVICE_GET_SCORE, null, queryParamsMap);
            count++;
            Thread.sleep(300);
        } while (response.code() != 200 && count < 50);
        return response;
    }
}
