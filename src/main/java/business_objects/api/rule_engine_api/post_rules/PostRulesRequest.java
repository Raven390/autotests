package business_objects.api.rule_engine_api.post_rules;

import helpers.http_helper.HttpHelper;
import io.qameta.allure.Step;
import okhttp3.Response;

import java.io.IOException;
import java.util.Map;

import static utils.ConfigFactory.*;


public class PostRulesRequest {

    @Step("Post rules")
    public static Response postRules(RuleObject rule)
            throws IOException {
        // Create request body
        RuleObject requestBody = new RuleObject(rule.getId(), rule.getEventType(), rule.getValue());
        // Send POST request
        return new HttpHelper().sendPostRequest(RULE_ENGINE_PATH_TEST_ENV + RULE_ENGINE_RULES, Map.of("Content-Type", "application/json"), null, requestBody);
    }
}

