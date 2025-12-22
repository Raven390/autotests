package business_objects.api.utilities_api.decrypt;

import static utils.ConfigFactory.*;

import helpers.http_helper.HttpHelper;
import io.qameta.allure.Step;
import java.io.IOException;
import java.util.Map;
import okhttp3.Response;

public class GetDecryptRequest {
    @Step("Get decrypt user data")
    public static Response getDecryptRequest(Map<String, Object> paramsMap) throws IOException {
        return new HttpHelper()
                .sendGetRequest(UTILITIES_API_SERVICE_TEST_BASE_PATH + UTILITIES_API_GET_DECRYPT, null, paramsMap);
    }
}
