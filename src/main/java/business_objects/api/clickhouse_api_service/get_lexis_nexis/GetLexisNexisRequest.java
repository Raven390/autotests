package business_objects.api.clickhouse_api_service.get_lexis_nexis;

import static utils.ConfigFactory.*;

import helpers.http_helper.HttpHelper;
import io.qameta.allure.Step;
import java.io.IOException;
import java.util.Map;
import okhttp3.Response;

public class GetLexisNexisRequest {

    @Step("Get lexis nexis user data")
    public static Response getLexisNexis(Map<String, Object> paramsMap) throws IOException {
        return new HttpHelper()
                .sendGetRequest(CLICKHOUSE_API_BASE_TEST + CLICKHOUSE_API_GET_LEXIS_NEXIS, null, paramsMap);
    }
}
