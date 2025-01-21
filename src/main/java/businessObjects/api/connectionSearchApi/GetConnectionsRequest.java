package businessObjects.api.connectionSearchApi;

import helpers.httpHelper.HttpHelper;
import io.qameta.allure.Step;
import okhttp3.Response;

import java.io.IOException;
import java.util.Map;

import static utils.ConfigFactory.*;

public class GetConnectionsRequest {

    @Step("Get user connections by ClientId")
    public static Response getConnectionsByClientId(Map<String, Object> paramsMap) throws IOException {
        return new HttpHelper().sendGetRequest(CONNECTION_SEARCH_BASE_PATH_TEST + CONNECTION_SEARCH_GET_CONNECTIONS_BY_CLIENT, null, paramsMap);
    }

    @Step("Get user connections by attributes")
    public static Response getConnectionsByAttributes(Map<String, Object> paramsMap) throws IOException {
        return new HttpHelper().sendGetRequest(CONNECTION_SEARCH_BASE_PATH_TEST + CONNECTION_SEARCH_GET_CONNECTIONS_BY_ATTRIBUTES, null, paramsMap);
    }
}
