package tests.click_house_api_service_tests;

import static business_objects.api.clickhouse_api_service.get_cumulative_notional_value.GetCumulativeNotionalValueRequest.getCumulativeNotionalValue;
import static business_objects.db.clickhouse.mt___mt5_deals_coerced_dd.Mt5DealsCoercedDdFactoryV2.generateMt5DealsCoercedDDObject;
import static business_objects.db.clickhouse.mt___mt5_deals_coerced_dd.Mt5DealsCoercedDdFactoryV2.generateMt5DealsCoercedDdObject;
import static helpers.data.ClientFactory.getRandomVantageClient;
import static helpers.data.DataSetupHelper.setupData;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static utils.Constants.*;

import business_objects.api.clickhouse_api_service.ClickhouseApiErrorResponse;
import business_objects.api.clickhouse_api_service.get_cumulative_notional_value.GetCumulativeNotionalValueResponse;
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
@Story(STORY_CLICKHOUSE_API_SERVICE_GET_CUMULATIVE_NOTIONAL_VALUE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CLICKHOUSE_API_SERVICE)
class GetCumulativeNotionalValueTests extends TestBaseApi {

    @BeforeAll
    static void setup() throws IOException {}

    @AfterAll
    static void delete() throws Exception {}

    @Test
    @AllureId("2111")
    @DisplayName("Clickhouse Api. Get cumulative notional value success (200)")
    void getCumulativeNotionalValueTest1() throws IOException {
        DataHelper data = new DataHelper();
        data.createClient(getRandomVantageClient());
        data.setMt5DealsCoercedDdObjectsV2(List.of(generateMt5DealsCoercedDDObject(data.clientHelper)));
        data.getMt5DealsCoercedDdObjectsV2().getFirst().setVolumeLots(2d);
        data.getMt5DealsCoercedDdObjectsV2().getFirst().setContractSize(2L);
        data.getMt5DealsCoercedDdObjectsV2().getFirst().setPrice(2d);
        setupData(data);

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", data.clientHelper.getTradingAccount());
        queryParams.put("serverId", data.clientHelper.getServerId());

        Response response = getCumulativeNotionalValue(queryParams);

        assertThat(response.body(), is(notNullValue()));
        List<GetCumulativeNotionalValueResponse> mappedResponse =
                objectMapper.readValue(response.body().string(), new TypeReference<>() {});
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response", mappedResponse.getFirst().getCumulativeNotionalValue(), is(8d));
    }

    @Test
    @AllureId("2112")
    @DisplayName("Clickhouse Api. Get cumulative notional value success >1 deal(200)")
    void getCumulativeNotionalValueTest2() throws IOException {
        DataHelper data = new DataHelper();
        data.createClient(getRandomVantageClient());
        data.setMt5DealsCoercedDdObjectsV2(generateMt5DealsCoercedDdObject(data.clientHelper, 2));
        data.getMt5DealsCoercedDdObjectsV2().getFirst().setVolumeLots(2d);
        data.getMt5DealsCoercedDdObjectsV2().getFirst().setContractSize(2L);
        data.getMt5DealsCoercedDdObjectsV2().getFirst().setPrice(2d);

        data.getMt5DealsCoercedDdObjectsV2().getLast().setVolumeLots(2d);
        data.getMt5DealsCoercedDdObjectsV2().getLast().setContractSize(2L);
        data.getMt5DealsCoercedDdObjectsV2().getLast().setPrice(2d);
        setupData(data);

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", data.clientHelper.getTradingAccount());
        queryParams.put("serverId", data.clientHelper.getServerId());
        queryParams.put("dateTo", "2030-12-31T00:00:00");

        Response response = getCumulativeNotionalValue(queryParams);

        assertThat(response.body(), is(notNullValue()));
        List<GetCumulativeNotionalValueResponse> mappedResponse =
                objectMapper.readValue(response.body().string(), new TypeReference<>() {});
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response", mappedResponse.getFirst().getCumulativeNotionalValue(), is(16d));
    }

    @Test
    @AllureId("2113")
    @DisplayName("Clickhouse Api. Get cumulative notional value success, empty, filter by data (200)")
    void getCumulativeNotionalValueTest3() throws IOException {
        DataHelper data = new DataHelper();
        data.createClient(getRandomVantageClient());
        data.setMt5DealsCoercedDdObjectsV2(generateMt5DealsCoercedDdObject(data.clientHelper, 2));
        data.getMt5DealsCoercedDdObjectsV2().getFirst().setVolumeLots(2d);
        data.getMt5DealsCoercedDdObjectsV2().getFirst().setContractSize(2L);
        data.getMt5DealsCoercedDdObjectsV2().getFirst().setPrice(2d);
        setupData(data);

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", data.clientHelper.getTradingAccount());
        queryParams.put("serverId", data.clientHelper.getServerId());
        queryParams.put("dateTo", "2020-12-31T00:00:00");

        Response response = getCumulativeNotionalValue(queryParams);

        assertThat(response.body(), is(notNullValue()));
        List<GetCumulativeNotionalValueResponse> mappedResponse =
                objectMapper.readValue(response.body().string(), new TypeReference<>() {});
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response", mappedResponse.size(), is(1));
        assertThat("Assert response", mappedResponse.getFirst().getCumulativeNotionalValue(), is(0d));
    }

    @Test
    @AllureId("2114")
    @DisplayName("Clickhouse Api. Get cumulative notional value no account (400)")
    void getCumulativeNotionalValueTest4() throws IOException {
        DataHelper data = new DataHelper();
        data.createClient(getRandomVantageClient());
        data.setMt5DealsCoercedDdObjectsV2(generateMt5DealsCoercedDdObject(data.clientHelper, 2));
        data.getMt5DealsCoercedDdObjectsV2().getFirst().setVolumeLots(2d);
        data.getMt5DealsCoercedDdObjectsV2().getFirst().setContractSize(2L);
        data.getMt5DealsCoercedDdObjectsV2().getFirst().setPrice(2d);
        setupData(data);

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("serverId", data.clientHelper.getServerId());
        queryParams.put("dateTo", "2020-12-31T00:00:00");

        Response response = getCumulativeNotionalValue(queryParams);

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
    @AllureId("2115")
    @DisplayName("Clickhouse Api. Get cumulative notional value no server (400)")
    void getCumulativeNotionalValueTest5() throws IOException {
        DataHelper data = new DataHelper();
        data.createClient(getRandomVantageClient());
        data.setMt5DealsCoercedDdObjectsV2(generateMt5DealsCoercedDdObject(data.clientHelper, 2));
        data.getMt5DealsCoercedDdObjectsV2().getFirst().setVolumeLots(2d);
        data.getMt5DealsCoercedDdObjectsV2().getFirst().setContractSize(2L);
        data.getMt5DealsCoercedDdObjectsV2().getFirst().setPrice(2d);
        setupData(data);

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", data.clientHelper.getTradingAccount());
        queryParams.put("dateTo", "2020-12-31T00:00:00");

        Response response = getCumulativeNotionalValue(queryParams);

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
