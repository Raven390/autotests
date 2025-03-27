package tests.rule_engine_service_tests.api;

import business_objects.api.rule_engine_api.get_events.GetEventsResponse;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import okhttp3.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;

import static business_objects.api.rule_engine_api.get_events.GetEventsRequest.getEvents;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static tests.TestBaseApi.objectMapper;
import static utils.Constants.*;
import static utils.Constants.SUITE_RULE_ENGINE_API_TESTS;

@Feature(FEATURE_RULE_ENGINE_SERVICE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_API_TESTS)
class GetEventsTests {
    @Test
    @DisplayName("Rule engine api. Get events request success")
    @AllureId("1103")
    void getEventsTest1() throws IOException {

        GetEventsResponse withdrawal = new GetEventsResponse();
        withdrawal.setName("Withdrawal");
        withdrawal.setType("withdrawal");

        Response response = getEvents();

        assert response.body() != null;
        GetEventsResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetEventsResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Check the response body", Arrays.stream(mappedResponse).toList(), hasItem(withdrawal));
        assertThat(mappedResponse.length, greaterThan(1));
    }
}
