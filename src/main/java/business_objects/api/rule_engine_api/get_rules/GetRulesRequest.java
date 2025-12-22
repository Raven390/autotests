package business_objects.api.rule_engine_api.get_rules;

import static utils.ConfigFactory.RULE_ENGINE_PATH_TEST_ENV;
import static utils.ConfigFactory.RULE_ENGINE_RULES;

import helpers.http_helper.HttpHelper;
import io.qameta.allure.Step;
import java.io.IOException;
import okhttp3.Response;

public class GetRulesRequest {
    @Step("Get rules")
    public static Response getRules() throws IOException {
        return new HttpHelper().sendGetRequest(RULE_ENGINE_PATH_TEST_ENV + RULE_ENGINE_RULES, null, null);
    }
}
