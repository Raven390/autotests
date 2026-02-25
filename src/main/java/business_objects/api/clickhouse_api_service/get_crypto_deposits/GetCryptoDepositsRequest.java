package business_objects.api.clickhouse_api_service.get_crypto_deposits;

import static utils.ConfigFactory.*;

import helpers.http_helper.HttpHelper;
import io.qameta.allure.Step;
import java.io.IOException;
import java.util.Map;
import okhttp3.Response;

public class GetCryptoDepositsRequest {

    @Step("Get crypto deposits")
    public static Response getCryptoDeposits(Map<String, Object> paramsMap) throws IOException {
        return new HttpHelper()
                .sendGetRequest(CLICKHOUSE_API_BASE_TEST + CLICKHOUSE_API_GET_CRYPTO_DEPOSITS, null, paramsMap);
    }
}
