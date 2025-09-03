package business_objects.api.clickhouse_api_service.get_client;

import java.io.IOException;

import helpers.http_helper.HttpHelper;
import io.qameta.allure.Step;
import okhttp3.Response;

import static utils.ConfigFactory.*;

public class GetClientRequest {

    @Step("Get client data by client id")
    public static Response getClient(String ucid) throws IOException {
        return new HttpHelper().sendGetRequest(CLICKHOUSE_API_BASE_TEST + CLICKHOUSE_API_GET_CLIENT + ucid, null, null);
    }

    @Step("Get client data by client id")
    public static Response getClient(Integer ucid) throws IOException {
        return new HttpHelper().sendGetRequest(CLICKHOUSE_API_BASE_TEST + CLICKHOUSE_API_GET_CLIENT + ucid, null, null);
    }
}
