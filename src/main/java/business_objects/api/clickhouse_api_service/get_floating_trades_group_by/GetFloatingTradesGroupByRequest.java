package business_objects.api.clickhouse_api_service.get_floating_trades_group_by;

import helpers.http_helper.HttpHelper;
import io.qameta.allure.Step;
import okhttp3.Response;

import java.io.IOException;
import java.util.Map;

import static utils.ConfigFactory.*;

public class GetFloatingTradesGroupByRequest {

    @Step("Get floating trades group by")
    public static Response getFloatingTradesGroupBy(Map<String, Object> paramsMap) throws IOException {
        return new HttpHelper().sendGetRequest(CLICKHOUSE_API_BASE_TEST + CLICKHOUSE_API_GET_FLOATING_TRADES_GROUP_BY, null, paramsMap);
    }

}
