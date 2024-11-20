package tests.clickHouseApiServiceTests;

import businessObjects.api.clickhouseApiService.getAbuseTypes.GetAbuseTypesResponse;
import businessObjects.api.clickhouseApiService.getAbuseTypes.GetAbuseTypesResponseError;
import helpers.data.ClientHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Muted;
import io.qameta.allure.Story;
import okhttp3.Response;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.TestBaseApi;

import java.io.IOException;
import java.sql.SQLException;

import static businessObjects.api.clickhouseApiService.getAbuseTypes.GetAbuseTypesRequest.GetAbuseTypes;
import static businessObjects.db.clickhouse.mitigationDbAbuseTypesTable.MitigationTbAbuseTypesObjectFactory.generateAbuseTypesClient;
import static helpers.data.ClientFactory.getRandomClient;
import static helpers.database.DbHelper.insertObjectToDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static utils.Constants.*;

@Feature(FEATURE_CLICKHOUSE_API_SERVICE)
@Story(STORY_CLICKHOUSE_API_SERVICE_POST_ABUSE_TYPES)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CLICKHOUSE_API_SERVICE)
@Disabled
@Muted
public class GetAbuseTypeTests extends TestBaseApi {

    @Test
    @DisplayName("Clickhouse Api. Post abuse types request (200)")
    @AllureId("219")
    public void postAbuseTypesTest1() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(MITIGATION_ABUSE_TYPES_TABLE_NAME, generateAbuseTypesClient(client));
        Object body = "{'clientIds': ['vantage-12345', 'vantage-67890']}";

        //Send request
        Response response = GetAbuseTypes(body);

        assert response.body() != null;
        GetAbuseTypesResponse
                mappedResponse = objectMapper.readValue(response.body().string(), GetAbuseTypesResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert listSize",mappedResponse.list.size(),is(2));
        assertThat("Assert ucid",mappedResponse.list.getFirst().clientId,is("vantage-12345"));
        assertThat("Assert fraudTypes",mappedResponse.list.getFirst().fraudType,is("[\"fraudulent_login\", \"credit_card_fraud\"]"));
    }

    @Test
    @DisplayName("Clickhouse Api. Post abuse types request without required params (400)")
    @AllureId("219")
    public void postAbuseTypesTest2() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        // Todo prepare test data
        Object body = "{'clientIds': []}";

        //Send request
        Response response = GetAbuseTypes(body);

        assert response.body() != null;
        GetAbuseTypesResponseError
                mappedResponse = objectMapper.readValue(response.body().string(), GetAbuseTypesResponseError.class);
        assertThat("Assert that code is 400", response.code(), is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Post abuse types request. User not found (404)")
    @AllureId("219")
    public void postAbuseTypesTest3() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        // Todo prepare test data
        Object body = "{'clientIds': ['vantage-1']}";

        //Send request
        Response response = GetAbuseTypes(body);

        assert response.body() != null;
        GetAbuseTypesResponseError
                mappedResponse = objectMapper.readValue(response.body().string(), GetAbuseTypesResponseError.class);
        assertThat("Assert that code is 404", response.code(), is(404));
    }

}
