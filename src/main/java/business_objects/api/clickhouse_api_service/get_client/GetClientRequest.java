package business_objects.api.clickhouse_api_service.get_client;

import static utils.ConfigFactory.*;

import helpers.http_helper.HttpHelper;
import io.qameta.allure.Step;
import java.io.IOException;
import okhttp3.Response;

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
