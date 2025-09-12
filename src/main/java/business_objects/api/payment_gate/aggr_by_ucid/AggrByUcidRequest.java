package business_objects.api.payment_gate.aggr_by_ucid;

import helpers.http_helper.HttpHelper;
import io.qameta.allure.Step;
import okhttp3.Response;

import java.io.IOException;
import java.util.Map;

import static utils.ConfigFactory.*;

public class AggrByUcidRequest {
    @Step("Get aggr by ucid request")
    public static Response getAggrByUcidRequest(Map<String, Object> paramsMap) throws IOException {
        return new HttpHelper().sendGetRequest(PAYMENT_GATE_SERVICE_BASE_PATH + PAYMENT_GATE_AGGR_BY_UCID_PATH, null, paramsMap);
    }

    @Step("Get aggr by ucid with body (POST)")
    public static Response getAggrByUcidRequest(GetAggrByUcidRequestBody body, Map<String, Object> paramsMap)
            throws IOException {
        // Some servers accept POST for complex filters while keeping the same path.
        return new HttpHelper().sendPostRequest(PAYMENT_GATE_SERVICE_BASE_PATH + PAYMENT_GATE_AGGR_BY_UCID_PATH, null, paramsMap, body);
    }
}
