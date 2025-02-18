package tests.clickHouseApiServiceTests;

import businessObjects.api.clickhouseApiService.ClickhouseApiErrorResponse;
import businessObjects.api.clickhouseApiService.getAbuseTypes.GetAbuseTypesResponse;
import businessObjects.db.clickhouse.boClientFraudTypes.BoClientFraudTypesObject;
import helpers.data.ClientHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import tests.TestBaseApi;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static businessObjects.api.clickhouseApiService.getAbuseTypes.GetAbuseTypesRequest.getAbuseTypes;
import static helpers.data.ClientFactory.getRandomVantageClient;
import static helpers.database.CleanTableHelper.cleanFraudTypeTableByClient;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

@Feature(FEATURE_CLICKHOUSE_API_SERVICE)
@Story(STORY_CLICKHOUSE_API_SERVICE_POST_ABUSE_TYPES)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CLICKHOUSE_API_SERVICE)
public class GetAbuseTypesTests extends TestBaseApi {

    private static BoClientFraudTypesObject fraud1;
    private static BoClientFraudTypesObject fraud2;
    private static BoClientFraudTypesObject fraud3;

    @BeforeAll
    public static void setupAbuseTypes() throws ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomVantageClient();
        fraud1 = new BoClientFraudTypesObject(client.getUcid(), 1, "HEDGING");
        fraud2 = new BoClientFraudTypesObject(client.getUcid(), 2, "CPA");
        fraud3 = new BoClientFraudTypesObject(getRandomVantageClient().getUcid(), 3, "LOSS_VOUCHER_ABUSE");
        insertObjectsToDb(BO_CLIENT_FRAUD_TYPES_TABLE_NAME, List.of(fraud1, fraud2, fraud3));
    }

    @AfterAll
    public static void teardownAbuseTypes() throws Exception {
        cleanFraudTypeTableByClient(fraud1.ucid, fraud2.ucid, fraud3.ucid);
    }

    @Test
    @DisplayName("Clickhouse Api. Get abuse types single client success (200)")
    @AllureId("429")
    public void getAbuseTypesSingleClientTest() throws IOException {

        Response response = getAbuseTypes(List.of(fraud1.ucid));

        assert response.body() != null;
        GetAbuseTypesResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetAbuseTypesResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert array size", mappedResponse.length, is(1));
        assertThat("Assert clientId", mappedResponse[0].clientId, is(fraud1.ucid));
        assertThat("Assert fraudType length", mappedResponse[0].fraudType.length, is(2));
        assertThat("Assert fraudType", mappedResponse[0].fraudType, hasItemInArray(fraud1.fraudTypeCode));
        assertThat("Assert fraudType", mappedResponse[0].fraudType, hasItemInArray(fraud2.fraudTypeCode));
    }

    @Test
    @DisplayName("Clickhouse Api. Get abuse types multiple clients success (200)")
    @AllureId("430")
    public void getAbuseTypesMultipleClientsTest() throws IOException {

        GetAbuseTypesResponse abuseTypesResponse1 = new GetAbuseTypesResponse(fraud1.ucid, new String[]{fraud1.fraudTypeCode, fraud2.fraudTypeCode});
        GetAbuseTypesResponse abuseTypesResponse2 = new GetAbuseTypesResponse(fraud3.ucid, new String[]{fraud3.fraudTypeCode});

        Response response = getAbuseTypes(List.of(fraud1.ucid, fraud3.ucid));

        assert response.body() != null;
        GetAbuseTypesResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetAbuseTypesResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert array size", mappedResponse.length, is(2));
        assertThat("Assert client 1 abuse types", mappedResponse, hasItemInArray(abuseTypesResponse1));
        assertThat("Assert client 2 abuse types", mappedResponse, hasItemInArray(abuseTypesResponse2));
    }

    @Test
    @DisplayName("Clickhouse Api. Get abuse types incorrect clientIds (400)")
    @AllureId("431")
    public void getAbuseTypesIncorrectClientIdsTest() throws IOException {

        Response response = getAbuseTypes(List.of("test"));

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert error", mappedResponse.error, is("Invalid \"clientId\" property format. The property clientId must contain brand and userId divided by a dash e.g., vantage-2068746030"));
        assertThat("Assert status", mappedResponse.status, is(400));
    }
}
