package tests.mirror_trading_score_service_tests;

import business_objects.db.data_science.mirror_data_with_stat.MirrorDataWithStat;
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
import static business_objects.db.data_science.mirror_data_with_stat.MirrorDataWithStatFactory.getMirrorDataWithStatObject;
import static business_objects.db.data_science.mirror_data_with_stat.MirrorDataWithStatFactory.getMirrorDataWithStatObject1;
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
public class GetMirrorTradingScoreTests extends TestBaseApi {

    static ClientHelper client;


    @BeforeAll
    static void setupData() {
        client = getRandomVantageClient();
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
        MirrorDataWithStat source = getMirrorDataWithStatObject(client);
        MirrorDataWithStat source1 = getMirrorDataWithStatObject1(client);

        insertObjectsToDb(DATA_SCIENCE_MIRROR_DATA_WITH_STAT_TABLE_NAME, List.of(source, source1));


        Allure.step("send API request for mirror score data");
        Response response = getMirrorTradingScore(client);
        assertThat(response.code(), is(200));

        Allure.step("Validate Data in response");
        UcidMirrorScore mappedResponse = objectMapper.readValue(response.body().string(), UcidMirrorScore.class);
        assertThat("Assert that ModelScore is match expected", mappedResponse.getUcidScore(), is(0.5));
        assertThat("Assert that ModelScore is match expected", mappedResponse.getModelScore(), is(0.998_024));
    }

}
