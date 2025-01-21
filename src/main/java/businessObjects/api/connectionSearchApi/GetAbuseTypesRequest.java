package businessObjects.api.connectionSearchApi;

import helpers.httpHelper.HttpHelper;
import io.qameta.allure.Step;
import okhttp3.Response;

import java.io.IOException;
import java.util.Map;

import static utils.ConfigFactory.*;

public class GetAbuseTypesRequest {

    @Step("Get abuse types by ClientId")
    public static Response getAbuseTypesByClientId(Map<String, Object> paramsMap) throws IOException {
        return new HttpHelper().sendGetRequest(CONNECTION_SEARCH_BASE_PATH_TEST + CONNECTION_SEARCH_GET_ABUSE_TYPES_BY_CLIENT, null, paramsMap);
    }

    @Step("Get abuse types by attributes")
    public static Response getAbuseTypesByAttributes(Map<String, Object> paramsMap) throws IOException {
        return new HttpHelper().sendGetRequest(CONNECTION_SEARCH_BASE_PATH_TEST + CONNECTION_SEARCH_GET_ABUSE_TYPES_BY_ATTRIBUTES, null, paramsMap);
    }

}
