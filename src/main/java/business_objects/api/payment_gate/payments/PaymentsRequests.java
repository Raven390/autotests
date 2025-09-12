package business_objects.api.payment_gate.payments;

import helpers.http_helper.HttpHelper;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static utils.ConfigFactory.*;

public class PaymentsRequests {

    public static Response postPaymentsRequest(PostPaymentsRequestBody postPaymentsRequestBody) throws IOException {
        // Send as JSON with proper headers
        Map<String, Object> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");
        return new HttpHelper().sendPostRequest(PAYMENT_GATE_SERVICE_BASE_PATH + PAYMENT_GATE_PAYMENTS_PATH, headers, null, postPaymentsRequestBody);
    }

    public static Response postPaymentsRequest(String postPaymentsRequestBody, Integer errorCode) throws IOException {
        // Send raw body as invalid JSON with appropriate headers to trigger server JSON parsing
        Map<String, Object> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");
        RequestBody body = RequestBody.create(postPaymentsRequestBody, MediaType.parse("application/json"));
        return new HttpHelper().sendPostRequest(PAYMENT_GATE_SERVICE_BASE_PATH + PAYMENT_GATE_PAYMENTS_PATH, headers, null, body);
    }
}