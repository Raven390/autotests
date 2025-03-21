package business_objects.api.clickhouse_api_service.get_credit_risk_free_revenue_ratio;

import helpers.http_helper.HttpHelper;
import io.qameta.allure.Step;
import okhttp3.Response;

import java.io.IOException;
import java.util.Map;

import static utils.ConfigFactory.*;

public class GetCreditRiskFreeRevenueRatioRequest {

    @Step("Get client withdrawals")
    public static Response getCreditRiskFreeRevenueRatio(Map<String, Object> paramsMap) throws IOException {
        return new HttpHelper().sendGetRequest(CLICKHOUSE_API_BASE_TEST + CLICKHOUSE_API_GET_CREDIT_RISK_FREE_REVENUE_RATIO, null, paramsMap);
    }
}
