package business_objects.api.payment_gate.payments_decisions;

import static utils.ConfigFactory.*;

import helpers.http_helper.HttpHelper;
import io.qameta.allure.Step;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import okhttp3.Response;

public class DecisionsRequests {

    @Step("Get decisions")
    public static Response getDecisions(String paymentId, Map<String, Object> paramsMap) throws IOException {
        return new HttpHelper()
                .sendGetRequest(
                        PAYMENT_GATE_SERVICE_BASE_PATH + PAYMENT_GATE_DECISIONS_PATH.replace("{paymentId}", paymentId),
                        null,
                        paramsMap);
    }

    @Step("Post decisions")
    public static Response postDecisions(String paymentId, List<PostDecisionsRequestBody> body) throws IOException {
        return new HttpHelper()
                .sendPostRequest(
                        PAYMENT_GATE_SERVICE_BASE_PATH + PAYMENT_GATE_DECISIONS_PATH.replace("{paymentId}", paymentId),
                        null,
                        null,
                        body);
    }

    @Step("put decisions")
    public static Response putDecisions(String paymentId, List<PutDecisionsRequestBody> body) throws IOException {
        return new HttpHelper()
                .sendPutRequest(
                        PAYMENT_GATE_SERVICE_BASE_PATH + PAYMENT_GATE_DECISIONS_PATH.replace("{paymentId}", paymentId),
                        null,
                        null,
                        body);
    }
}
