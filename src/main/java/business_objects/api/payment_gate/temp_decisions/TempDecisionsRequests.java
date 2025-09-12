package business_objects.api.payment_gate.temp_decisions;


import helpers.http_helper.HttpHelper;
import okhttp3.Response;

import java.io.IOException;

import static utils.ConfigFactory.*;

public class TempDecisionsRequests {

    public static Response postTempDecisionsRequest(PostTempDecisionsRequestBody postTempDecisionsRequestBody)
            throws IOException {
        return new HttpHelper().sendPostRequest(PAYMENT_GATE_SERVICE_BASE_PATH + PAYMENT_GATE_TEMP_DECISIONS_PATH, null, null, postTempDecisionsRequestBody);
    }
}
