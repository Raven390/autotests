package tests.click_house_api_service_tests;

import static business_objects.api.clickhouse_api_service.get_chargeback_score.GetChargebackScoreRequest.getChargebackScore;
import static business_objects.db.clickhouse.account_ib_relation.AccountIbRelationFactory.generateAccountIbRelationObjectByClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.DbHelper.insertObjectToDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Constants.LAYER_API;
import static utils.Constants.SUITE_CLICKHOUSE_API_SERVICE;

import business_objects.api.clickhouse_api_service.get_chargeback_score.GetChargebackScoreResponse;
import business_objects.db.clickhouse.account_ib_relation.AccountIbRelationObject;
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
@Story(STORY_CLICKHOUSE_API_SERVICE_GET_CHARGEBACK_SCORE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CLICKHOUSE_API_SERVICE)
class GetChargebackScoreTests extends TestBaseApi {

    static AccountIbRelationObject ibRelation;
    static ClientHelper client;

    @BeforeAll
    static void setup() {
        client = getRandomVantageClientAllFields();
        ibRelation = generateAccountIbRelationObjectByClient(client);
        insertObjectToDb(ACCOUNT_IB_RELATION_TABLE_NAME, ibRelation);
    }

    @AfterAll
    static void teardown() throws Exception {}

    @Test
    @AllureId("2047")
    @DisplayName("Clickhouse Api. Get Chargeback Score by all params. Response code 200")
    void getChargebackScoreTest1() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", client.getUcid());
        queryParams.put("depositCardRatio", 0);
        queryParams.put("rebateRatio", 0);
        queryParams.put("rebateEffeciency", 0);
        queryParams.put("profitToDeposit", 0);

        Response response = getChargebackScore(queryParams);

        assertThat(response.body(), is(notNullValue()));
        GetChargebackScoreResponse mappedResponse =
                objectMapper.readValue(response.body().string(), GetChargebackScoreResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response contains expected ratio abuse", mappedResponse.getRatioAbuse(), is(notNullValue()));
    }
}
