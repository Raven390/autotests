package business_objects.api.clickhouse_api_service.get_unclosed_trades_by_symbol;

import helpers.http_helper.HttpHelper;
import io.qameta.allure.Step;
import okhttp3.Response;

import java.io.IOException;
import java.util.Map;

import static utils.ConfigFactory.*;

public class GetUnclosedTradesBySymbolRequest {

    @Step("Get clients unclosed trades")
    public static Response getUnclosedTradesBySymbol(Map<String, Object> paramsMap) throws IOException {
        return new HttpHelper().sendGetRequest(CLICKHOUSE_API_BASE_TEST + CLICKHOUSE_API_GET_UNCLOSED_TRADES_BY_SYMBOL, null, paramsMap);
    }

}
