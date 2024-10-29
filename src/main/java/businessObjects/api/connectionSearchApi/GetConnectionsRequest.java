package businessObjects.api.connectionSearchApi;

import helpers.httpHelper.HttpHelper;
import okhttp3.Response;

import java.io.IOException;
import java.util.Map;

import static utils.ConfigFactory.CONNECTION_SEARCH_BASE_PATH;
import static utils.ConfigFactory.CONNECTION_SEARCH_GET_CONNECTIONS_BY_CLIENT;

public class GetConnectionsRequest {

    public static Response getConnectionsByClientId(Map<String, Object> paramsMap) throws IOException {
        return new HttpHelper().sendGetRequest(CONNECTION_SEARCH_BASE_PATH + CONNECTION_SEARCH_GET_CONNECTIONS_BY_CLIENT, null, paramsMap);
    }

}
