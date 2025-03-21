package business_objects.api.clickhouse_api_service.get_client;

import java.io.IOException;

import io.qameta.allure.Step;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static utils.ConfigFactory.*;

public class GetClientRequest {
    private static final OkHttpClient httpClient = new OkHttpClient();

    @Step("Get client data by client id")
    public static Response getClient(String clientId) throws IOException {
        return httpClient.newCall(new Request.Builder().url(CLICKHOUSE_API_BASE_TEST + CLICKHOUSE_API_GET_CLIENT + clientId).build()).execute();
    }

    @Step("Get client data by client id in wrong format")
    public static Response getClient(Integer clientId) throws IOException {
        return httpClient.newCall(new Request.Builder().url(CLICKHOUSE_API_BASE_TEST + CLICKHOUSE_API_GET_CLIENT + clientId).build()).execute();
    }
}
