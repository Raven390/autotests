package business_objects.api.payment_gate.payments;

import static utils.ConfigFactory.*;

import helpers.http_helper.HttpHelper;
import io.qameta.allure.Step;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PaymentsRequests {

    public static Response postPayments(PostPaymentsRequestBody postPaymentsRequestBody) throws IOException {
        // Send as JSON with proper headers
        Map<String, Object> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");
        return new HttpHelper()
                .sendPostRequest(
                        PAYMENT_GATE_SERVICE_BASE_PATH + PAYMENT_GATE_PAYMENTS_PATH,
                        headers,
                        null,
                        postPaymentsRequestBody);
    }

    public static Response postPayments(String postPaymentsRequestBody, Integer errorCode) throws IOException {
        // Send raw body as invalid JSON with appropriate headers to trigger server JSON parsing
        Map<String, Object> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");
        RequestBody body = RequestBody.create(postPaymentsRequestBody, MediaType.parse("application/json"));
        return new HttpHelper()
                .sendPostRequest(PAYMENT_GATE_SERVICE_BASE_PATH + PAYMENT_GATE_PAYMENTS_PATH, headers, null, body);
    }

    public static Response putPayments(PutPaymentsRequestBody putPaymentsRequestBody) throws IOException {
        // Send as JSON with proper headers
        Map<String, Object> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");
        return new HttpHelper()
                .sendPutRequest(
                        PAYMENT_GATE_SERVICE_BASE_PATH + PAYMENT_GATE_PAYMENTS_PATH,
                        headers,
                        null,
                        putPaymentsRequestBody);
    }

    public static Response putPayments(String putPaymentsRequestBody, Integer errorCode) throws IOException {
        // Send raw body as invalid JSON with appropriate headers to trigger server JSON parsing
        Map<String, Object> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");
        RequestBody body = RequestBody.create(putPaymentsRequestBody, MediaType.parse("application/json"));
        return new HttpHelper()
                .sendPutRequest(PAYMENT_GATE_SERVICE_BASE_PATH + PAYMENT_GATE_PAYMENTS_PATH, headers, null, body);
    }

    public static Response putPaymentsV2(PutPaymentsV2RequestBody putPaymentsRequestBody) throws IOException {
        // Send as JSON with proper headers
        Map<String, Object> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");
        return new HttpHelper()
                .sendPutRequest(
                        PAYMENT_GATE_SERVICE_BASE_PATH + PAYMENT_GATE_PAYMENTS_V2_PATH,
                        headers,
                        null,
                        putPaymentsRequestBody);
    }

    public static Response putPaymentsV2(String putPaymentsRequestBody, Integer errorCode) throws IOException {
        // Send raw body as invalid JSON with appropriate headers to trigger server JSON parsing
        Map<String, Object> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");
        RequestBody body = RequestBody.create(putPaymentsRequestBody, MediaType.parse("application/json"));
        return new HttpHelper()
                .sendPutRequest(PAYMENT_GATE_SERVICE_BASE_PATH + PAYMENT_GATE_PAYMENTS_V2_PATH, headers, null, body);
    }

    @Step("Get payments")
    public static Response getPayments(Map<String, Object> paramsMap) throws IOException {
        return new HttpHelper()
                .sendGetRequest(PAYMENT_GATE_SERVICE_BASE_PATH + PAYMENT_GATE_PAYMENTS_PATH, null, paramsMap);
    }
}
