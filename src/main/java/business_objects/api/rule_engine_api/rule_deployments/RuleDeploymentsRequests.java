package business_objects.api.rule_engine_api.rule_deployments;

import helpers.http_helper.HttpHelper;
import io.qameta.allure.Step;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static utils.ConfigFactory.*;

public class RuleDeploymentsRequests {
    @Step("Get rule deployments")
    public static Response getRuleDeploymentsByProcessId(String processId) throws IOException {
        Map<String, Object> queryParamsMap = new HashMap<>();
        queryParamsMap.put("processId", processId);
        return new HttpHelper().sendGetRequest(RULE_ENGINE_PATH_TEST_ENV + RULE_ENGINE_RULE_DEPLOYMENTS, null, queryParamsMap);
    }

    @Step("Get rule deployments without request parameters")
    public static Response getRuleDeploymentsNoParams() throws IOException {
        return new HttpHelper().sendGetRequest(RULE_ENGINE_PATH_TEST_ENV + RULE_ENGINE_RULE_DEPLOYMENTS, null, null);
    }

    @Step("Post rule deployment")
    public static Response postRuleDeployment(RequestBody body, String authorName, String ruleName) throws IOException {

        Map<String, Object> queryHeadersMap = new HashMap<>();
        queryHeadersMap.put("Accept", "application/json");
        queryHeadersMap.put("Content-Type", "text/xml");

        Map<String, Object> queryParamsMap = new HashMap<>();
        queryParamsMap.put("authorName", authorName);
        queryParamsMap.put("ruleName", ruleName);


        return new HttpHelper().sendPostRequest(RULE_ENGINE_PATH_TEST_ENV + RULE_ENGINE_RULE_DEPLOYMENTS, queryHeadersMap, queryParamsMap, body);
    }
}
