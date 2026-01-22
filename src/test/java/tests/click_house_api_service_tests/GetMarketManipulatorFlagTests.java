package tests.click_house_api_service_tests;

import static business_objects.api.clickhouse_api_service.get_market_manipulator_flag.GetMarketManipulatorFlagRequest.getMarketManipulatorFlag;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static utils.Constants.*;

import business_objects.api.clickhouse_api_service.get_market_manipulator_flag.GetMarketManipulatorFlagResponse;
import helpers.data.ClientHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import tests.TestBaseApi;

@Feature(FEATURE_CLICKHOUSE_API_SERVICE)
@Story(STORY_CLICKHOUSE_API_SERVICE_GET_MARKET_MANIPULATOR_FLAG)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CLICKHOUSE_API_SERVICE)
class GetMarketManipulatorFlagTests extends TestBaseApi {

    static ClientHelper client;

    @BeforeAll
    static void setup() {
        client = getRandomVantageClientAllFields();
    }

    @AfterAll
    static void teardown() throws Exception {}

    @Test
    @AllureId("2047")
    @DisplayName("Clickhouse Api. Get market manipulator flag")
    void getChargebackScoreTest1() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", client.getUcid());
        queryParams.put("profitCutoffSum", 0); // Default value : 2000
        queryParams.put("toxicityThreshold", 0); // Default value : 0.3
        queryParams.put("realLeverageThreshold", 0); // Default value : 80
        queryParams.put("consecutiveFlagThreshold", 0); // Default value : 5
        queryParams.put("consecutiveSecondsThreshold", 0); // Default value : 3

        Response response = getMarketManipulatorFlag(queryParams);

        assertThat(response.body(), is(notNullValue()));
        GetMarketManipulatorFlagResponse mappedResponse =
                objectMapper.readValue(response.body().string(), GetMarketManipulatorFlagResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }
}
