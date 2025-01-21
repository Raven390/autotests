package businessObjects.api.clickhouseApiService.getLexisNexis;

import helpers.httpHelper.HttpHelper;
import io.qameta.allure.Step;
import okhttp3.Response;

import java.io.IOException;
import java.util.Map;

import static utils.ConfigFactory.*;

public class GetLexisNexisRequest {

    @Step("Get lexis nexis user data")
    public static Response getLexisNexis(Map<String, Object> paramsMap) throws IOException {
        return new HttpHelper().sendGetRequest(CLICKHOUSE_API_BASE_TEST + CLICKHOUSE_API_GET_LEXIS_NEXIS, null, paramsMap);
    }
}
