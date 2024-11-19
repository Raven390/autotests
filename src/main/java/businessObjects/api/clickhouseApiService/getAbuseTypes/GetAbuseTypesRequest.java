package businessObjects.api.clickhouseApiService.getAbuseTypes;

import helpers.httpHelper.HttpHelper;
import io.qameta.allure.Step;
import okhttp3.Response;

import java.io.IOException;

import static utils.ConfigFactory.*;

public class GetAbuseTypesRequest {

    @Step("Post abuse types")
    public static Response GetAbuseTypes(Object requestBody) throws IOException {
        return new HttpHelper().sendPostRequest(CLICKHOUSE_API_BASE_PATH + CLICKHOUSE_API_GET_BONUSES, null, null,requestBody);
    }

}
