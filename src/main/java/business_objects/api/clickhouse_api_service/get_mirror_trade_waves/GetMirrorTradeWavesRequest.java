package business_objects.api.clickhouse_api_service.get_mirror_trade_waves;

import static utils.ConfigFactory.*;

import helpers.http_helper.HttpHelper;
import io.qameta.allure.Step;
import java.io.IOException;
import java.util.Map;
import okhttp3.Response;

public class GetMirrorTradeWavesRequest {

    @Step("Get mirror trade waves")
    public static Response getMirrorTradeWavesV2(Map<String, Object> paramsMap) throws IOException {
        return new HttpHelper()
                .sendGetRequest(CLICKHOUSE_API_BASE_TEST + CLICKHOUSE_API_GET_MIRROR_TRADE_WAVES_V2, null, paramsMap);
    }
}
