package business_objects.api.clickhouse_api_service.get_swap_free_fees;

import static utils.ConfigFactory.*;

import helpers.http_helper.HttpHelper;
import io.qameta.allure.Step;
import java.io.IOException;
import java.util.Map;
import okhttp3.Response;

public class GetSwapFreeFeesRequest {

    @Step("Get swap free fees")
    public static Response getSwapFreeFees(Map<String, Object> paramsMap) throws IOException {
        return new HttpHelper()
                .sendGetRequest(CLICKHOUSE_API_BASE_TEST + CLICKHOUSE_API_GET_SWAP_FREE_FEES, null, paramsMap);
    }
}
