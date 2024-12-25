package businessObjects.api.clickhouseApiService.getBalanceOrders;

import helpers.httpHelper.HttpHelper;
import io.qameta.allure.Step;
import okhttp3.Response;

import java.io.IOException;
import java.util.Map;

import static utils.ConfigFactory.*;

public class GetBalanceOrdersRequest {

    @Step("Get balance orders for user")
    public static Response getBalanceOrders(Map<String, Object> paramsMap) throws IOException {
        return new HttpHelper().sendGetRequest(CLICKHOUSE_API_BASE_PATH + CLICKHOUSE_API_GET_BALANCE_ORDERS, null, paramsMap);
    }
}
