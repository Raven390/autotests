package businessObjects.api.clickhouseApiService.getCreditEquityRatio;

import helpers.httpHelper.HttpHelper;
import io.qameta.allure.Step;
import okhttp3.Response;

import java.io.IOException;
import java.util.Map;

import static utils.ConfigFactory.*;

public class GetCreditEquityRequest {

    @Step("Get credit equity by trading account")
    public static Response getCreditEquity(Map<String, Object> paramsMap) throws IOException {
        return new HttpHelper().sendGetRequest(CLICKHOUSE_API_BASE_PATH + CLICKHOUSE_API_GET_CREDIT_EQUITY_PATH, null, paramsMap);
    }

}
