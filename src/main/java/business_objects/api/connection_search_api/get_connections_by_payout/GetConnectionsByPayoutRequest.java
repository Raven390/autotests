package business_objects.api.connection_search_api.get_connections_by_payout;

import static utils.ConfigFactory.*;

import helpers.http_helper.HttpHelper;
import io.qameta.allure.Step;
import java.io.IOException;
import java.util.Map;
import okhttp3.Response;

public class GetConnectionsByPayoutRequest {

    @Step("Get user connections by payout")
    public static Response getConnectionsByPayout(Map<String, Object> paramsMap) throws IOException {
        return new HttpHelper()
                .sendGetRequest(
                        CONNECTION_SEARCH_BASE_PATH_TEST + CONNECTION_SEARCH_GET_CONNECTIONS_BY_PAYOUT,
                        null,
                        paramsMap);
    }
}
