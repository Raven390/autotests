package tests.click_house_api_service_tests;

import static business_objects.api.clickhouse_api_service.get_general_score.GetGeneralScoreV2Request.getGeneralScoreV2;
import static business_objects.db.data_science.ucid_general_score_python_test.UcidGeneralScorePythonTestFactory.generateUcidGeneralScorePythonTestObject;
import static helpers.data.ClientFactory.getRandomVantageClient;
import static helpers.data.DataSetupHelper.setupData;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

import business_objects.api.clickhouse_api_service.ClickhouseApiErrorResponse;
import business_objects.api.clickhouse_api_service.get_general_score.GetGeneralScoreV2Response;
import com.fasterxml.jackson.core.type.TypeReference;
import helpers.data.DataHelper;
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
@Story(STORY_CLICKHOUSE_API_SERVICE_GET_ABUSE_TYPES)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CLICKHOUSE_API_SERVICE)
class GetGeneralScoreV2Tests extends TestBaseApi {

    @BeforeAll
    static void setup() throws IOException {}

    @AfterAll
    static void delete() throws Exception {}

    @Test
    @DisplayName("Clickhouse Api. Get general score success (200)")
    @AllureId("")
    void getAbuseTypesSingleClientTest1() throws IOException {
        DataHelper data = new DataHelper();
        data.createClient(getRandomVantageClient());
        data.setUcidGeneralScorePythonTest(generateUcidGeneralScorePythonTestObject(data, 0.99, 0.99, 0.99, 0.99));
        setupData(data);

        Map<String, Object> queryParamsMap = new HashMap<>();
        queryParamsMap.put("clientId", data.getClientHelper().getUcid());
        Response response = getGeneralScoreV2(queryParamsMap);
        GetGeneralScoreV2Response mappedResponse =
                objectMapper.readValue(response.body().string(), new TypeReference<>() {});
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response", mappedResponse.getUcidScore(), is(0.99));
        assertThat("Assert response", mappedResponse.getModelScore(), is(0.99));
        assertThat("Assert response", mappedResponse.getAvgPastUcidScore(), is(0.99));
        assertThat("Assert response", mappedResponse.getAvgFivePastUcidScore(), is(0.99));
    }

    @Test
    @DisplayName("Clickhouse Api. Get general score success user not exists (200)")
    @AllureId("")
    void getAbuseTypesSingleClientTest2() throws IOException {
        DataHelper data = new DataHelper();
        data.createClient(getRandomVantageClient());

        Map<String, Object> queryParamsMap = new HashMap<>();
        queryParamsMap.put("clientId", data.getClientHelper().getUcid());
        Response response = getGeneralScoreV2(queryParamsMap);
        GetGeneralScoreV2Response mappedResponse =
                objectMapper.readValue(response.body().string(), new TypeReference<>() {});
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response", mappedResponse.getUcidScore(), is(0.0));
        assertThat("Assert response", mappedResponse.getModelScore(), is(0.0));
        assertThat("Assert response", mappedResponse.getAvgPastUcidScore(), is(nullValue()));
        assertThat("Assert response", mappedResponse.getAvgFivePastUcidScore(), is(nullValue()));
    }

    @Test
    @DisplayName("Clickhouse Api. Get general score success all params(200)")
    @AllureId("")
    void getAbuseTypesSingleClientTest3() throws IOException {
        DataHelper data = new DataHelper();
        data.createClient(getRandomVantageClient());
        data.setUcidGeneralScorePythonTest(generateUcidGeneralScorePythonTestObject(data, 0.99, 0.99, 0.99, 0.99));
        setupData(data);

        Map<String, Object> queryParamsMap = new HashMap<>();
        queryParamsMap.put("clientId", data.getClientHelper().getUcid());
        queryParamsMap.put("dateTo", "2030-01-01T01:01:01Z");
        Response response = getGeneralScoreV2(queryParamsMap);
        GetGeneralScoreV2Response mappedResponse =
                objectMapper.readValue(response.body().string(), new TypeReference<>() {});
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response", mappedResponse.getUcidScore(), is(0.99));
        assertThat("Assert response", mappedResponse.getModelScore(), is(0.99));
        assertThat("Assert response", mappedResponse.getAvgPastUcidScore(), is(0.99));
        assertThat("Assert response", mappedResponse.getAvgFivePastUcidScore(), is(0.99));
    }

    @Test
    @DisplayName("Clickhouse Api. Get general score success filtered by date(200)")
    @AllureId("")
    void getAbuseTypesSingleClientTest4() throws IOException {
        DataHelper data = new DataHelper();
        data.createClient(getRandomVantageClient());
        data.setUcidGeneralScorePythonTest(generateUcidGeneralScorePythonTestObject(data, 0.99, 0.99, 0.99, 0.99));
        setupData(data);

        Map<String, Object> queryParamsMap = new HashMap<>();
        queryParamsMap.put("clientId", data.getClientHelper().getUcid());
        queryParamsMap.put("dateTo", "2020-01-01T01:01:01Z");
        Response response = getGeneralScoreV2(queryParamsMap);
        GetGeneralScoreV2Response mappedResponse =
                objectMapper.readValue(response.body().string(), new TypeReference<>() {});
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response", mappedResponse.getUcidScore(), is(0.0));
        assertThat("Assert response", mappedResponse.getModelScore(), is(0.0));
        assertThat("Assert response", mappedResponse.getAvgPastUcidScore(), is(nullValue()));
        assertThat("Assert response", mappedResponse.getAvgFivePastUcidScore(), is(nullValue()));
    }

    @Test
    @DisplayName("Clickhouse Api. Get general score error no ucid")
    @AllureId("")
    void getAbuseTypesSingleClientTest5() throws IOException {
        DataHelper data = new DataHelper();
        data.createClient(getRandomVantageClient());
        data.setUcidGeneralScorePythonTest(generateUcidGeneralScorePythonTestObject(data, 0.99, 0.99, 0.99, 0.99));
        setupData(data);

        Map<String, Object> queryParamsMap = new HashMap<>();
        Response response = getGeneralScoreV2(queryParamsMap);
        ClickhouseApiErrorResponse mappedResponse =
                objectMapper.readValue(response.body().string(), new TypeReference<>() {});
        assertThat("Assert that code is 200", response.code(), is(400));
        assertThat("Assert response", mappedResponse.getStatus(), is(400));
        assertThat(
                "Assert response",
                mappedResponse.getError(),
                is(
                        "Invalid \"clientId\" property format. The property clientId must contain brand and userId divided by a dash e.g., vantage-2068746030"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get general score wrong ucid")
    @AllureId("")
    void getAbuseTypesSingleClientTest6() throws IOException {
        DataHelper data = new DataHelper();
        data.createClient(getRandomVantageClient());
        data.setUcidGeneralScorePythonTest(generateUcidGeneralScorePythonTestObject(data, 0.99, 0.99, 0.99, 0.99));
        setupData(data);

        Map<String, Object> queryParamsMap = new HashMap<>();
        queryParamsMap.put("clientId", "123");
        Response response = getGeneralScoreV2(queryParamsMap);
        ClickhouseApiErrorResponse mappedResponse =
                objectMapper.readValue(response.body().string(), new TypeReference<>() {});
        assertThat("Assert that code is 200", response.code(), is(400));
        assertThat("Assert response", mappedResponse.getStatus(), is(400));
        assertThat(
                "Assert response",
                mappedResponse.getError(),
                is(
                        "Invalid clientId property format. The property clientId must contain brand and userId divided by a dash e.g., vantage-2068746030"));
    }
}
