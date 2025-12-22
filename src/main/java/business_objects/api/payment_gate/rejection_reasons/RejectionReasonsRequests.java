package business_objects.api.payment_gate.rejection_reasons;

import static utils.ConfigFactory.*;

import helpers.http_helper.HttpHelper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import okhttp3.Response;

public class RejectionReasonsRequests {

    public static Response getRejectionReasons(String code, String name) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("code", code);
        paramsMap.put("name", name);
        // Send as JSON with proper headers
        return new HttpHelper()
                .sendGetRequest(PAYMENT_GATE_SERVICE_BASE_PATH + PAYMENT_GATE_REJECTION_REASONS_PATH, null, paramsMap);
    }

    public static Response getRejectionReasons() throws IOException {
        // Send as JSON with proper headers
        return new HttpHelper()
                .sendGetRequest(PAYMENT_GATE_SERVICE_BASE_PATH + PAYMENT_GATE_REJECTION_REASONS_PATH, null, null);
    }
}
