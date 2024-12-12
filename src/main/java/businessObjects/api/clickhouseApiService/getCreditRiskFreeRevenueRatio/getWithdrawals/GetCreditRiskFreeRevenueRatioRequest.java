package businessObjects.api.clickhouseApiService.getCreditRiskFreeRevenueRatio.getWithdrawals;

import helpers.httpHelper.HttpHelper;
import io.qameta.allure.Step;
import okhttp3.Response;

import java.io.IOException;
import java.util.Map;

import static utils.ConfigFactory.*;

public class GetCreditRiskFreeRevenueRatioRequest {

    @Step("Get client withdrawals")
    public static Response getCreditRiskFreeRevenueRatio(Map<String, Object> paramsMap) throws IOException {
        return new HttpHelper().sendGetRequest(CLICKHOUSE_API_BASE_PATH + CLICKHOUSE_API_GET_CREDIT_RISK_FREE_REVENUE_RATIO_PATH, paramsMap, null);
    }
}
