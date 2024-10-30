package businessObjects.api.clickhouseApiService.getClients;

import helpers.httpHelper.HttpHelper;
import okhttp3.Response;

import java.io.IOException;
import java.util.Map;

import static utils.ConfigFactory.*;

public class GetClientsRequest {

    public static Response getClientsIdByTradingAccountServerId(Map<String, Object> paramsMap) throws IOException {
        return new HttpHelper().sendGetRequest(CLICKHOUSE_API_BASE_PATH + CLICKHOUSE_API_GET_CLIENTS_PATH, null, paramsMap);
    }
}
