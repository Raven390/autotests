package business_objects.api.clickhouse_api_service.get_mirror_trade_on_withdrawal;

import static utils.ConfigFactory.*;

import helpers.http_helper.HttpHelper;
import io.qameta.allure.Step;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import okhttp3.Response;

public class GetMirrorTradeOnWithdrawalRequest {

    @Step("Get mirror trade on withdrawal")
    public static Response getMirrorTradesOnWithdrawal(String ucid) throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", ucid);
        return new HttpHelper()
                .sendGetRequest(
                        CLICKHOUSE_API_BASE_TEST + CLICKHOUSE_API_GET_MIRROR_TRADE_ON_WITHDRAWAL, null, queryParams);
    }
}
