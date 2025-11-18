package business_objects.api.payment_gate.rule_executions;

import helpers.http_helper.HttpHelper;
import okhttp3.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static utils.ConfigFactory.*;

public class RuleExecutionsRequests {

    public static Response putRuleExecutionsRequest(PutRuleExecutionsBody putRuleExecutionsBody) throws IOException {
        Map<String, Object> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");
        return new HttpHelper().sendPutRequest(PAYMENT_GATE_SERVICE_BASE_PATH + PAYMENT_GATE_RULE_EXECUTIONS_PATH.replace("{paymentId}", putRuleExecutionsBody.getPaymentId().toString()), headers, null, putRuleExecutionsBody);
    }

    public static Response postRuleExecutionsRequest(PostRuleExecutionsBody postRuleExecutionsBody) throws IOException {
        Map<String, Object> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");
        return new HttpHelper().sendPostRequest(PAYMENT_GATE_SERVICE_BASE_PATH + PAYMENT_GATE_RULE_EXECUTIONS_PATH.replace("{paymentId}", postRuleExecutionsBody.getPaymentId().toString()), headers, null, postRuleExecutionsBody);
    }

    public static Response getRuleExecutionsRequest(String id, Map<String, Object> paramsMap) throws IOException {
        Map<String, Object> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");
        return new HttpHelper().sendGetRequest(PAYMENT_GATE_SERVICE_BASE_PATH + PAYMENT_GATE_RULE_EXECUTIONS_PATH.replace("{paymentId}", id), headers, paramsMap);
    }

    public static Response getRuleExecutionsByUcidRequest(String ucid, Map<String, Object> paramsMap)
            throws IOException {
        Map<String, Object> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");
        return new HttpHelper().sendGetRequest(PAYMENT_GATE_SERVICE_BASE_PATH + PAYMENT_GATE_RULE_EXECUTIONS_BY_UCID_PATH.replace("{ucid}", ucid), headers, paramsMap);
    }
}
