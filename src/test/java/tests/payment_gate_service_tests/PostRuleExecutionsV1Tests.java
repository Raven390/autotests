package tests.payment_gate_service_tests;

import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.Tag;
import tests.TestBaseApi;

import static utils.Constants.*;
import static utils.Constants.LAYER_API;
import static utils.Constants.SUITE_PAYMENT_GATE_TESTS;

@Feature(FEATURE_PAYMENT_GATE)
@Story(STORY_PAYMENT_GATE_POST_RULE_EXECUTIONS)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_PAYMENT_GATE_TESTS)
public class PostRuleExecutionsV1Tests extends TestBaseApi {
}
