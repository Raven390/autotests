package business_objects.api.clickhouse_api_service.get_clients;

import helpers.http_helper.HttpHelper;
import io.qameta.allure.Step;
import okhttp3.Response;

import java.io.IOException;
import java.util.Map;

import static utils.ConfigFactory.*;

public class GetClientsRequest {
    @Step("Get client trading account")
    public static Response getClientsIdByTradingAccountServerId(Map<String, Object> paramsMap) throws IOException {
        return new HttpHelper().sendGetRequest(CLICKHOUSE_API_BASE_TEST + CLICKHOUSE_API_GET_CLIENTS, null, paramsMap);
    }
}
