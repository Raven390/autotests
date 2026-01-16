package business_objects.api.clickhouse_api_service.get_free_margin;

import static utils.ConfigFactory.*;

import helpers.http_helper.HttpHelper;
import io.qameta.allure.Step;
import java.io.IOException;
import java.util.Map;
import okhttp3.Response;

public class GetFreeMarginRequest {

    private GetFreeMarginRequest() {}

    @Step("Get free margin by trading account")
    public static Response getFreeMargin(Map<String, Object> paramsMap) throws IOException {
        return new HttpHelper()
                .sendGetRequest(CLICKHOUSE_API_BASE_TEST + CLICKHOUSE_API_GET_FREE_MARGIN, null, paramsMap);
    }
}
