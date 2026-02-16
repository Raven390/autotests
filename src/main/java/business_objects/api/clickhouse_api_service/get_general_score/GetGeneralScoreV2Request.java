package business_objects.api.clickhouse_api_service.get_general_score;

import static utils.ConfigFactory.*;

import helpers.http_helper.HttpHelper;
import io.qameta.allure.Step;
import java.io.IOException;
import java.util.Map;
import okhttp3.Response;

public class GetGeneralScoreV2Request {

    @Step("Get general score V2")
    public static Response getGeneralScoreV2(Map<String, Object> paramsMap) throws IOException {
        return new HttpHelper()
                .sendGetRequest(CLICKHOUSE_API_BASE_TEST + CLICKHOUSE_API_GET_GENERAL_SCORE_V2, null, paramsMap);
    }
}
