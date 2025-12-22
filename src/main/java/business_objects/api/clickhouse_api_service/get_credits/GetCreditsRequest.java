package business_objects.api.clickhouse_api_service.get_credits;

import static utils.ConfigFactory.*;

import helpers.http_helper.HttpHelper;
import io.qameta.allure.Step;
import java.io.IOException;
import java.util.Map;
import okhttp3.Response;

public class GetCreditsRequest {
    @Step("Get client credits")
    public static Response getCredits(Map<String, Object> paramsMap) throws IOException {
        return new HttpHelper().sendGetRequest(CLICKHOUSE_API_BASE_TEST + CLICKHOUSE_API_GET_CREDITS, null, paramsMap);
    }
}
