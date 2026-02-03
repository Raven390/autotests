package tests.click_house_api_service_tests;

import static business_objects.api.clickhouse_api_service.get_effective_leverage.GetEffectiveLeverageRequest.getEffectiveLeverage;
import static business_objects.db.clickhouse.mt___mt5_deals_coerced_dd.Mt5DealsCoercedDdFactoryV2.generateMt5DealsCoercedDDObject;
import static business_objects.db.clickhouse.mt___mt5_deals_coerced_dd.Mt5DealsCoercedDdFactoryV2.generateMt5DealsCoercedDdObject;
import static helpers.data.ClientFactory.getRandomVantageClient;
import static helpers.data.DataSetupHelper.setupData;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

import business_objects.api.clickhouse_api_service.ClickhouseApiErrorResponse;
import business_objects.api.clickhouse_api_service.get_effective_leverage.GetEffectiveLeverageResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import helpers.data.DataHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import tests.TestBaseApi;

@Feature(FEATURE_CLICKHOUSE_API_SERVICE)
@Story(STORY_CLICKHOUSE_API_SERVICE_GET_EFFECTIVE_LEVERAGE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CLICKHOUSE_API_SERVICE)
class GetEffectiveLeverageTests extends TestBaseApi {

    @BeforeAll
    static void setup() throws IOException {}

    @AfterAll
    static void delete() throws Exception {}

    @Test
    @AllureId("2108")
    @DisplayName("Clickhouse Api. Get effective leverage success for single deal (200)")
    void getEffectiveLeverageTest1() throws IOException {
        DataHelper data = new DataHelper();
        data.createClient(getRandomVantageClient());
        data.setMt5DealsCoercedDdObjectsV2(List.of(generateMt5DealsCoercedDDObject(data.clientHelper)));
        data.getMt5DealsCoercedDdObjectsV2().getFirst().setVolumeLots(10d);
        data.getMt5DealsCoercedDdObjectsV2().getFirst().setContractSize(10L);
        data.getMt5DealsCoercedDdObjectsV2().getFirst().setPrice(10d);
        data.getMt5DealsCoercedDdObjectsV2().getFirst().setEquity(10d);
        setupData(data);

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", data.clientHelper.getTradingAccount());
        queryParams.put("serverId", data.clientHelper.getServerId());
        queryParams.put(
                "dealId", data.getMt5DealsCoercedDdObjectsV2().getFirst().getDeal());

        Response response = getEffectiveLeverage(queryParams);

        assertThat(response.body(), is(notNullValue()));
        List<GetEffectiveLeverageResponse> mappedResponse =
                objectMapper.readValue(response.body().string(), new TypeReference<>() {});
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat(
                "Assert response",
                mappedResponse.getFirst().getDealId(),
                is(data.getMt5DealsCoercedDdObjectsV2().getFirst().getDeal().toString()));
        assertThat("Assert response", mappedResponse.getFirst().getEffectiveLeverage(), is(100.0));
    }

    @Test
    @AllureId("2109")
    @DisplayName("Clickhouse Api. Get effective leverage success for 2 deals (200)")
    void getEffectiveLeverageTest2() throws IOException {
        DataHelper data = new DataHelper();
        data.createClient(getRandomVantageClient());
        data.setMt5DealsCoercedDdObjectsV2(generateMt5DealsCoercedDdObject(data.clientHelper, 2));
        data.getMt5DealsCoercedDdObjectsV2().getFirst().setVolumeLots(10d);
        data.getMt5DealsCoercedDdObjectsV2().getFirst().setContractSize(10L);
        data.getMt5DealsCoercedDdObjectsV2().getFirst().setPrice(10d);
        data.getMt5DealsCoercedDdObjectsV2().getFirst().setEquity(10d);

        data.getMt5DealsCoercedDdObjectsV2().getLast().setVolumeLots(10d);
        data.getMt5DealsCoercedDdObjectsV2().getLast().setContractSize(10L);
        data.getMt5DealsCoercedDdObjectsV2().getLast().setPrice(10d);
        data.getMt5DealsCoercedDdObjectsV2().getLast().setEquity(10d);

        setupData(data);

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", data.clientHelper.getTradingAccount());
        queryParams.put("serverId", data.clientHelper.getServerId());

        Response response = getEffectiveLeverage(queryParams);

        assertThat(response.body(), is(notNullValue()));
        List<GetEffectiveLeverageResponse> mappedResponse =
                objectMapper.readValue(response.body().string(), new TypeReference<>() {});
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response", mappedResponse.size(), is(2));
    }

    @Test
    @AllureId("2110")
    @DisplayName("Clickhouse Api. Get effective leverage success for all deals (200)")
    void getEffectiveLeverageTest3() throws IOException {
        DataHelper data = new DataHelper();
        data.createClient(getRandomVantageClient());
        setupData(data);
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", data.clientHelper.getTradingAccount());
        queryParams.put("serverId", data.clientHelper.getServerId());

        Response response = getEffectiveLeverage(queryParams);

        assertThat(response.body(), is(notNullValue()));
        List<GetEffectiveLeverageResponse> mappedResponse =
                objectMapper.readValue(response.body().string(), new TypeReference<>() {});
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response", mappedResponse.size(), is(0));
    }

    @Test
    @DisplayName("Clickhouse Api. Get effective leverage no account (400)")
    @AllureId("")
    void getEffectiveLeverageTest4() throws IOException {
        DataHelper data = new DataHelper();
        data.createClient(getRandomVantageClient());
        setupData(data);

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("serverId", data.clientHelper.getServerId());

        Response response = getEffectiveLeverage(queryParams);

        assertThat(response.body(), is(notNullValue()));
        ClickhouseApiErrorResponse mappedResponse =
                objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 200", response.code(), is(400));
        assertThat(
                "Assert response",
                mappedResponse.getError(),
                is("Required request parameter 'tradingAccount' for method parameter type String is not present"));
        assertThat("Assert response", mappedResponse.getStatus(), is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get effective leverage no server (400)")
    @AllureId("")
    void getEffectiveLeverageTest5() throws IOException {
        DataHelper data = new DataHelper();
        data.createClient(getRandomVantageClient());
        setupData(data);

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", data.clientHelper.getTradingAccount());

        Response response = getEffectiveLeverage(queryParams);

        assertThat(response.body(), is(notNullValue()));
        ClickhouseApiErrorResponse mappedResponse =
                objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 200", response.code(), is(400));
        assertThat(
                "Assert response",
                mappedResponse.getError(),
                is("Required request parameter 'serverId' for method parameter type String is not present"));
        assertThat("Assert response", mappedResponse.getStatus(), is(400));
    }
}
