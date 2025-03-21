package business_objects.api.clickhouse_api_service.get_deposits;

import helpers.http_helper.HttpHelper;
import io.qameta.allure.Step;
import okhttp3.Response;

import java.io.IOException;
import java.util.Map;

import static utils.ConfigFactory.*;

public class GetDepositsRequest {

    @Step("Get client deposits")
    public static Response getDeposits(Map<String, Object> paramsMap) throws IOException {
        return new HttpHelper().sendGetRequest(CLICKHOUSE_API_BASE_TEST + CLICKHOUSE_API_GET_DEPOSITS, null, paramsMap);
    }

}
