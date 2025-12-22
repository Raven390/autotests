package business_objects.api.connection_search_api.get_abuse_types_v2;

import static utils.ConfigFactory.*;

import helpers.http_helper.HttpHelper;
import io.qameta.allure.Step;
import java.io.IOException;
import java.util.Map;
import okhttp3.Response;

public class GetAbuseTypesRequestV2 {

    @Step("Get abuse types by client id V2")
    public static Response getAbuseTypesByClientIdV2(Map<String, Object> paramsMap) throws IOException {
        return new HttpHelper()
                .sendGetRequest(
                        CONNECTION_SEARCH_BASE_PATH_TEST + CONNECTION_SEARCH_GET_ABUSE_TYPES_BY_CLIENT_V2,
                        null,
                        paramsMap);
    }
}
