package tests.mirror_trading_score_service_tests;

import business_objects.db.data_science.bybit_feature_store.feature_store_service.BybitFeatureStore;
import business_objects.db.data_science.feature_store_service.FeatureStoreService;
import business_objects.db.data_science.ucid_mirror_score.UcidMirrorScore;
import helpers.data.ClientHelper;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import tests.TestBaseApi;

import java.util.List;

import static business_objects.api.mirror_trading_score_service.MirrorTradingScoreRequest.getMirrorTradingScore;
import static business_objects.db.data_science.bybit_feature_store.feature_store_service.BybitFeatureStoreFactory.*;
import static business_objects.db.data_science.feature_store_service.FeatureStoreServiceFactory.*;
import static helpers.data.ClientFactory.getRandomBybitClient;
import static helpers.data.ClientFactory.getRandomVantageClient;
import static helpers.database.DbHelper.*;
import static helpers.database.MirrorScoreHelper.cleanUserMirrorScoreDataDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static utils.Constants.*;

@Feature(FEATURE_MIRROR_TRADING_SCORE_API_SERVICE)
@Story(STORY_GET_MIRROR_TRADING_SCORE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_MIRROR_TRADING_SCORE_API_TESTS)
class GetMirrorTradingScoreTests extends TestBaseApi {

    static ClientHelper client;
    static ClientHelper client2;

    @BeforeAll
    static void setupData() {
        client = getRandomVantageClient();
        client2 = getRandomBybitClient();
    }

    @AfterAll
    static void deleteData() throws Exception {
        cleanUserMirrorScoreDataDb(client.getUcid());
        deleteEntryFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
    }

    @Test
    @DisplayName("Get mirror score data API")
    @AllureId("1149")
    void getMirrorScoreTest() throws Exception {

        Allure.step("setup DB data");
        cleanUserMirrorScoreDataDb(client.getUcid());
        FeatureStoreService source4 = getFeatureStoreServiceDepositObject(client);

        insertObjectsToDb(DATA_SCIENCE_FEATURE_STORE_SERVICE_TABLE_NAME, List.of(source4));

        // Add wait for service to process the data
        Thread.sleep(10_000);

        Allure.step("send API request for mirror score data");
        Response response = getMirrorTradingScore(client);
        assertThat(response.code(), is(200));
        Allure.step("Validate Data in response");
        UcidMirrorScore mappedResponse = objectMapper.readValue(response.body().string(), UcidMirrorScore.class);
        assertThat("Assert that modelScore is match expected", mappedResponse.getModelScore(), is(0.889_995_75));
        assertThat("Assert that ucidScore is match expected", mappedResponse.getUcidScore(), is(1.0));
    }

    @Test
    @AllureId("1363")
    @DisplayName("Get mirror score data for bybit user API")
    void getMirrorScoreTest2() throws Exception {

        Allure.step("setup DB data");
        cleanUserMirrorScoreDataDb(client2.getUcid());

        // Create BybitFeatureStore objects using the factory methods
        BybitFeatureStore source3 = getBybitFeatureStoreWithdrawalObject(client2);
        insertObjectsToDb(DATA_SCIENCE_BYBIT_FEATURE_STORE_TABLE_NAME, List.of(source3));

        // Add wait for service to process the data
        Thread.sleep(10_000);

        Allure.step("send API request for mirror score data");
        Response response = getMirrorTradingScore(client2);
        assertThat(response.code(), is(200));
        Allure.step("Validate Data in response");
        UcidMirrorScore mappedResponse = objectMapper.readValue(response.body().string(), UcidMirrorScore.class);
        assertThat("Assert that modelScore is match expected", mappedResponse.getModelScore(), is(0.885_080_2));
        assertThat("Assert that ucidScore is match expected", mappedResponse.getUcidScore(), is(1.0));
    }

}
