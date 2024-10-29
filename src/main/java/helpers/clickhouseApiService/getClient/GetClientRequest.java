package helpers.clickhouseApiService.getClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static utils.ConfigFactory.*;

public class GetClientRequest {
    public static OkHttpClient httpClient = new OkHttpClient();
    public static ObjectMapper objectMapper = new ObjectMapper();

    public static Response getClient(String clientId) throws IOException {
        return httpClient.newCall(new Request.Builder().url(CLICKHOUSE_API_BASE_PATH + CLICKHOUSE_API_GET_CLIENT_PATH + clientId).build()).execute();
    }

    public static Response getClient(Integer clientId) throws IOException {
        return httpClient.newCall(new Request.Builder().url(CLICKHOUSE_API_BASE_PATH + CLICKHOUSE_API_GET_CLIENT_PATH + clientId).build()).execute();
    }
}
