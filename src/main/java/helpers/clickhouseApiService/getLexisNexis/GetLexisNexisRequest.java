package helpers.clickhouseApiService.getLexisNexis;

import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.httpHelper.HttpHelper;
import okhttp3.OkHttpClient;
import okhttp3.Response;

import java.io.IOException;
import java.util.Map;

import static utils.ConfigFactory.*;

public class GetLexisNexisRequest {
    public static OkHttpClient httpClient = new OkHttpClient();
    public static ObjectMapper objectMapper = new ObjectMapper();

    public static Response getLexisNexis(Map<String, Object> paramsMap) throws IOException {
        return new HttpHelper().sendGetRequest(CLICKHOUSE_API_BASE_PATH + CLICKHOUSE_API_GET_LEXIS_NEXIS_PATH, null, paramsMap);
    }
}
