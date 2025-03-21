package business_objects.api.clickhouse_api_service.get_lexis_nexis_data;

import helpers.http_helper.HttpHelper;
import io.qameta.allure.Step;
import okhttp3.Response;

import java.io.IOException;
import java.util.Map;

import static utils.ConfigFactory.*;

public class GetLexisNexisDataRequest {

    @Step("Get lexis nexis user custom data")
    public static Response getLexisNexisData(Map<String, Object> paramsMap) throws IOException {
        return new HttpHelper().sendGetRequest(CLICKHOUSE_API_BASE_TEST + CLICKHOUSE_API_GET_LEXIS_NEXIS_DATA, null, paramsMap);
    }
}
