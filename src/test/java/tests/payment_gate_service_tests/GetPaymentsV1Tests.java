package tests.payment_gate_service_tests;

import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import tests.TestBaseApi;


import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Constants.LAYER_API;
import static utils.Constants.SUITE_PAYMENT_GATE_TESTS;

@Feature(FEATURE_PAYMENT_GATE)
@Story(STORY_PAYMENT_GATE_GET_PAYMENTS)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_PAYMENT_GATE_TESTS)
class GetPaymentsV1Tests extends TestBaseApi {
}
