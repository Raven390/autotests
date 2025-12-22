package business_objects.api.clickhouse_api_service.get_trade_by_id;

import static utils.ConfigFactory.*;

import helpers.http_helper.HttpHelper;
import io.qameta.allure.Step;
import java.io.IOException;
import java.util.Map;
import okhttp3.Response;

public class GetTradeRequest {

    @Step("Get trade data by Iв")
    public static Response getTrade(Map<String, Object> paramsMap) throws IOException {
        return new HttpHelper()
                .sendGetRequest(
                        CLICKHOUSE_API_BASE_TEST
                                + CLICKHOUSE_API_GET_TRADES_BY_TRADE_ID.replace(
                                        "{tradeId}", String.valueOf(paramsMap.get("tradeId"))),
                        null,
                        paramsMap);
    }
}
