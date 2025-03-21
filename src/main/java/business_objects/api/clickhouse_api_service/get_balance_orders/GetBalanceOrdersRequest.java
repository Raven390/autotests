package business_objects.api.clickhouse_api_service.get_balance_orders;

import helpers.http_helper.HttpHelper;
import io.qameta.allure.Step;
import okhttp3.Response;

import java.io.IOException;
import java.util.Map;

import static utils.ConfigFactory.*;

public class GetBalanceOrdersRequest {

    @Step("Get balance orders for user")
    public static Response getBalanceOrders(Map<String, Object> paramsMap) throws IOException {
        return new HttpHelper().sendGetRequest(CLICKHOUSE_API_BASE_TEST + CLICKHOUSE_API_GET_BALANCE_ORDERS, null, paramsMap);
    }
}
