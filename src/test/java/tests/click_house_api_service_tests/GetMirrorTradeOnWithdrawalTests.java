package tests.click_house_api_service_tests;

import static business_objects.api.clickhouse_api_service.get_mirror_trade_on_withdrawal.GetMirrorTradeOnWithdrawalRequest.getMirrorTradesOnWithdrawal;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.DataSetupHelper.setupData;
import static helpers.data.rules.MirrorTradeOnWithdrawalDataInserter.insertMirrorTradeOnWithdrawalData;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static utils.Constants.*;

import business_objects.api.clickhouse_api_service.ClickhouseApiErrorResponse;
import business_objects.api.clickhouse_api_service.get_mirror_trade_on_withdrawal.GetMirrorTradeOnWithdrawalResponse;
import helpers.data.ClientHelper;
import helpers.data.DataHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import java.io.IOException;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import tests.TestBaseApi;

@Feature(FEATURE_CLICKHOUSE_API_SERVICE)
@Story(STORY_CLICKHOUSE_API_SERVICE_GET_MIRROR_TRADE_ON_WITHDRAWAL)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CLICKHOUSE_API_SERVICE)
class GetMirrorTradeOnWithdrawalTests extends TestBaseApi {

    @BeforeAll
    static void setup() {}

    @AfterAll
    static void teardown() {}

    @Test
    @AllureId("1897")
    @DisplayName("Clickhouse Api. Get mirror trade on withdrawal(200)")
    void getMirrorTradeOnLastWithdrawalTest1() throws IOException {
        DataHelper data = new DataHelper();
        ClientHelper client = getRandomVantageClientAllFields();
        data.createClient(client);
        insertMirrorTradeOnWithdrawalData(data.clientHelper);
        setupData(data);

        Response response = getMirrorTradesOnWithdrawal(client.getUcid());
        assertThat(response.body(), is(notNullValue()));
        GetMirrorTradeOnWithdrawalResponse mappedResponse =
                objectMapper.readValue(response.body().string(), GetMirrorTradeOnWithdrawalResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.getSuspiciousFlag(), is(true));
    }

    @Test
    @AllureId("1896")
    @DisplayName("Clickhouse Api. Get mirror trade on last withdrawal. Empty response(200)")
    void getMirrorTradeOnLastWithdrawalTest2() throws IOException {
        DataHelper data = new DataHelper();
        ClientHelper client = getRandomVantageClientAllFields();
        data.createClient(client);

        Response response = getMirrorTradesOnWithdrawal(client.getUcid());
        assertThat(response.body(), is(notNullValue()));
        GetMirrorTradeOnWithdrawalResponse mappedResponse =
                objectMapper.readValue(response.body().string(), GetMirrorTradeOnWithdrawalResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.getSuspiciousFlag(), is(false));
    }

    @Test
    @AllureId("1895")
    @DisplayName("Clickhouse Api. Get mirror trade on last withdrawal(400)")
    void getMirrorTradeOnLastWithdrawalTest3() throws IOException {
        Response response = getMirrorTradesOnWithdrawal("ucid");
        assertThat(response.body(), is(notNullValue()));
        ClickhouseApiErrorResponse mappedResponse =
                objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 200", response.code(), is(400));
        assertThat(
                "Assert response error",
                mappedResponse.getError(),
                is(
                        "Invalid clientId property format. The property clientId must contain brand and userId divided by a dash e.g., vantage-2068746030"));
        assertThat("Assert response length", mappedResponse.getStatus(), is(400));
    }
}
