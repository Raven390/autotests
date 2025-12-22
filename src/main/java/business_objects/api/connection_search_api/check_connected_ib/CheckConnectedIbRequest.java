package business_objects.api.connection_search_api.check_connected_ib;

import static utils.ConfigFactory.*;

import helpers.http_helper.HttpHelper;
import io.qameta.allure.Step;
import java.io.IOException;
import java.util.Map;
import okhttp3.Response;

public class CheckConnectedIbRequest {

    @Step("Get user connections by ClientId")
    public static Response getCheckConnectedIb(Map<String, Object> paramsMap) throws IOException {
        return new HttpHelper()
                .sendGetRequest(
                        CONNECTION_SEARCH_BASE_PATH_TEST + CONNECTION_SEARCH_GET_CHECK_CONNECTED_IB, null, paramsMap);
    }
}
