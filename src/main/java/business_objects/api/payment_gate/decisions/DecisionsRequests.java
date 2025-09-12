package business_objects.api.payment_gate.decisions;

import helpers.http_helper.HttpHelper;
import io.qameta.allure.Step;
import okhttp3.Response;

import java.io.IOException;
import java.util.Map;

import static utils.ConfigFactory.*;

public class DecisionsRequests {

    @Step("Get decisions")
    public static Response getDecisions(Map<String, Object> paramsMap) throws IOException {
        return new HttpHelper().sendGetRequest(PAYMENT_GATE_SERVICE_BASE_PATH + PAYMENT_GATE_DECISIONS_PATH, null, paramsMap);
    }

    @Step("Post decisions")
    public static Response postDecisions(Map<String, Object> paramsMap, PostDecisionsRequestBody body)
            throws IOException {
        return new HttpHelper().sendPostRequest(PAYMENT_GATE_SERVICE_BASE_PATH + PAYMENT_GATE_DECISIONS_PATH, null, null, body);
    }

    @Step("put decisions")
    public static Response putDecisions(Map<String, Object> paramsMap, PutDecisionsRequestBody body)
            throws IOException {
        return new HttpHelper().sendPutRequest(PAYMENT_GATE_SERVICE_BASE_PATH + PAYMENT_GATE_DECISIONS_PATH, null, null, body);
    }

}
