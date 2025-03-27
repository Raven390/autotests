package business_objects.api.rule_engine_api.delete_rules;

import helpers.http_helper.HttpHelper;
import io.qameta.allure.Step;
import okhttp3.Response;

import java.io.IOException;

import static utils.ConfigFactory.*;

public class DeleteRulesRequest {
    @Step("Delete rules")
    public static Response deleteRules(String ruleId) throws IOException {
        return new HttpHelper().sendDeleteRequest(RULE_ENGINE_PATH_TEST_ENV + RULE_ENGINE_RULES + "/" + ruleId, null, null, null);
    }
}
