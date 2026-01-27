package tests.click_house_api_service_tests;

import static business_objects.api.clickhouse_api_service.get_mirror_trade_waves.GetMirrorTradeWavesRequest.getMirrorTradeWavesV2;
import static helpers.data.ClientFactory.getRandomVantageClient;
import static helpers.data.DataSetupHelper.setupData;
import static helpers.data.rules.WaveFlagInserterV2.insertMirrorWaveV2Data;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static utils.Constants.*;

import business_objects.api.clickhouse_api_service.get_mirror_trade_waves.GetMirrorTradeWavesResponse;
import helpers.data.ClientHelper;
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
@Story(STORY_CLICKHOUSE_API_SERVICE_GET_MIRROR_TRADE_WAVES_V2)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CLICKHOUSE_API_SERVICE)
class GetMirrorTradeWavesV2Tests extends TestBaseApi {

    @Test
    @AllureId("2092")
    @DisplayName("Clickhouse Api. Get mirror trade waves V2 (200). True")
    void getMirrorTradeWavesTest1() throws IOException, InterruptedException {
        DataHelper data = new DataHelper();
        ClientHelper client1 = getRandomVantageClient();
        data.createClient(client1);
        insertMirrorWaveV2Data(data.clientHelper);
        setupData(data);

        // Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("ucid", client1.getUcid());
        queryParams.put("maxLeverageWave", 60);
        queryParams.put("equityDeltaPct", 130);
        queryParams.put("newProfitDepRatio", 30);
        queryParams.put("tvWavePercent", 50);
        queryParams.put("exposureWavePercent", 95);
        queryParams.put("maxBonusLvlWave", 30);
        queryParams.put("waveLength", 2);
        queryParams.put("profitWaveSoFar", 100);
        Response response = getMirrorTradeWavesV2(queryParams);

        assertThat(response.body(), is(notNullValue()));
        GetMirrorTradeWavesResponse mappedResponse =
                objectMapper.readValue(response.body().string(), GetMirrorTradeWavesResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert suspicious flag = true", mappedResponse.getSuspiciousFlag(), is(true));
    }

    @Test
    @AllureId("2091")
    @DisplayName("Clickhouse Api. Get mirror trade waves V2 (200). False")
    void getMirrorTradeWavesTest2() throws IOException {
        DataHelper data = new DataHelper();
        ClientHelper client2 = getRandomVantageClient();
        data.createClient(client2);
        setupData(data);

        // Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("ucid", client2.getUcid());
        queryParams.put("maxLeverageWave", 60);
        queryParams.put("equityDeltaPct", 130);
        queryParams.put("newProfitDepRatio", 30);
        queryParams.put("tvWavePercent", 50);
        queryParams.put("exposureWavePercent", 95);
        queryParams.put("maxBonusLvlWave", 30);
        queryParams.put("waveLength", 2);
        queryParams.put("profitWaveSoFar", 100);
        Response response = getMirrorTradeWavesV2(queryParams);

        assertThat(response.body(), is(notNullValue()));
        GetMirrorTradeWavesResponse mappedResponse =
                objectMapper.readValue(response.body().string(), GetMirrorTradeWavesResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert suspicious flag = true", mappedResponse.getSuspiciousFlag(), is(false));
    }
}
