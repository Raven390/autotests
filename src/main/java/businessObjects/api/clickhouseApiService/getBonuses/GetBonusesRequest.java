package businessObjects.api.clickhouseApiService.getBonuses;

import helpers.httpHelper.HttpHelper;
import io.qameta.allure.Step;
import okhttp3.Response;

import java.io.IOException;
import java.util.Map;

import static utils.ConfigFactory.*;

public class GetBonusesRequest {

    @Step("Get client bonuses")
    public static Response getBonuses(Map<String, Object> paramsMap) throws IOException {
        return new HttpHelper().sendGetRequest(CLICKHOUSE_API_BASE_TEST + CLICKHOUSE_API_GET_BONUSES, null, paramsMap);
    }

}
