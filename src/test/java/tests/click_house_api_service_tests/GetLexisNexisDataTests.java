package tests.click_house_api_service_tests;

import business_objects.api.clickhouse_api_service.ClickhouseApiErrorResponse;
import business_objects.api.clickhouse_api_service.get_lexis_nexis_data.GetLexisNexisDataResponse;
import business_objects.db.clickhouse.ln_session_parsed.LnSessionParsedObject;
import helpers.data.ClientHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import tests.TestBaseApi;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static business_objects.api.clickhouse_api_service.get_lexis_nexis_data.GetLexisNexisDataRequest.getLexisNexisData;
import static business_objects.api.clickhouse_api_service.get_lexis_nexis_data.GetLexisNexisDataResponse.Items.getItem;
import static business_objects.db.clickhouse.ln_session_parsed.LnSessionParsedObjectFactory.generateLexisNexisDataByClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.CleanTableHelper.cleanLexisNexisTableByUcid;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

@Feature(FEATURE_CLICKHOUSE_API_SERVICE)
@Story(STORY_CLICKHOUSE_API_SERVICE_GET_LEXIS_NEXIS_DATA)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CLICKHOUSE_API_SERVICE)
class GetLexisNexisDataTests extends TestBaseApi {

    static ClientHelper client = getRandomVantageClientAllFields();
    static LnSessionParsedObject object1 = generateLexisNexisDataByClient(client);
    static LnSessionParsedObject object2 = generateLexisNexisDataByClient(client);

    static String ucid1 = object1.getUcid();
    static String ucid2 = object2.getUcid();

    @BeforeAll
    static void setupData() {
        insertObjectsToDb(LEXIS_NEXIS_TABLE_NAME, List.of(object1, object2));
    }

    @AfterAll
    static void teardownData() throws Exception {
        cleanLexisNexisTableByUcid(ucid1, ucid2);
    }

    @Test
    @DisplayName("Clickhouse Api. Get lexisNexisData success response by brand+userId (200)")
    @AllureId("676")
    void getLexisNexisDataTest1() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("brand", client.getBrand());
        queryParams.put("userId", client.getUserId());
        queryParams.put("columnNames", "id");
        Response response = getLexisNexisData(queryParams);
        String responseBody = response.body().string();

        GetLexisNexisDataResponse mappedResponse = objectMapper.readValue(responseBody, GetLexisNexisDataResponse.class);

        assertThat("Check response code", response.code(), is(200));
        assertThat("Check response id", mappedResponse.totalCount, is(2));
        assertThat("Check response item", mappedResponse.items.toString(), containsString(object1.getId().toString()));
        assertThat("Check response item", mappedResponse.items.toString(), containsString(object2.getId().toString()));
    }

    @Test
    @DisplayName("Clickhouse Api. Get lexisNexisData success response by clientId (200)")
    @AllureId("677")
    void getLexisNexisDataTest2() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", client.getUcid());
        queryParams.put("columnNames", "id");
        Response response = getLexisNexisData(queryParams);
        String responseBody = response.body().string();

        GetLexisNexisDataResponse mappedResponse = objectMapper.readValue(responseBody, GetLexisNexisDataResponse.class);
        System.out.println(object1.getId());
        System.out.println(mappedResponse.items);

        assertThat("Check response code", response.code(), is(200));
        assertThat("Check response id", mappedResponse.totalCount, is(2));
        assertThat("Check response item", mappedResponse.items.toString(), containsString(object1.getId().toString()));
        assertThat("Check response item", mappedResponse.items.toString(), containsString(object2.getId().toString()));
    }

    @Test
    @DisplayName("Clickhouse Api. Get lexisNexisData no such column error (400)")
    @AllureId("678")
    void getLexisNexisDataTest3() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("brand", client.getBrand());
        queryParams.put("userId", client.getUserId());
        queryParams.put("columnNames", "ucid123");
        Response response = getLexisNexisData(queryParams);
        String responseBody = response.body().string();

        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(responseBody, ClickhouseApiErrorResponse.class);

        assertThat("Check response code", response.code(), is(400));
        assertThat("Check response error", mappedResponse.error, is("No such column: ucid123"));
        assertThat("Check response status", mappedResponse.status, is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get lexisNexisData success response by invalid clientId format (400)")
    @AllureId("679")
    void getLexisNexisDataTest4() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", client.getUcid().replace("-", ""));
        queryParams.put("columnNames", "ucid");
        Response response = getLexisNexisData(queryParams);
        String responseBody = response.body().string();

        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(responseBody, ClickhouseApiErrorResponse.class);

        assertThat("Check response code", response.code(), is(400));
        assertThat("Check response error", mappedResponse.error, is("Invalid &quot;clientId&quot; property format. The property clientId must contain brand and userId divided by a dash e.g., vantage-2068746030"));
        assertThat("Check response status", mappedResponse.status, is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get lexisNexisData success response by invalid clientId format (400)")
    @AllureId("680")
    void getLexisNexisDataTest5() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("columnNames", "ucid");
        Response response = getLexisNexisData(queryParams);
        String responseBody = response.body().string();

        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(responseBody, ClickhouseApiErrorResponse.class);

        assertThat("Check response code", response.code(), is(400));
        assertThat("Check response error", mappedResponse.error, is("Either clientId or deviceId or userId and brand must be provided."));
        assertThat("Check response status", mappedResponse.status, is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get lexisNexisData success response by brand only (400)")
    @AllureId("681")
    void getLexisNexisDataTest6() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("columnNames", "ucid");
        queryParams.put("brand", client.getBrand());
        Response response = getLexisNexisData(queryParams);
        String responseBody = response.body().string();

        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(responseBody, ClickhouseApiErrorResponse.class);

        assertThat("Check response code", response.code(), is(400));
        assertThat("Check response error", mappedResponse.error, is("Either clientId or deviceId or userId and brand must be provided."));
        assertThat("Check response status", mappedResponse.status, is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get lexisNexisData success response by userId (400)")
    @AllureId("682")
    void getLexisNexisDataTest7() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("columnNames", "ucid");
        queryParams.put("userId", client.getUserId());
        Response response = getLexisNexisData(queryParams);
        String responseBody = response.body().string();

        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(responseBody, ClickhouseApiErrorResponse.class);

        assertThat("Check response code", response.code(), is(400));
        assertThat("Check response error", mappedResponse.error, is("Either clientId or deviceId or userId and brand must be provided."));
        assertThat("Check response status", mappedResponse.status, is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get lexisNexisData success response no params (400)")
    @AllureId("683")
    void getLexisNexisDataTest8() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        Response response = getLexisNexisData(queryParams);
        String responseBody = response.body().string();

        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(responseBody, ClickhouseApiErrorResponse.class);

        assertThat("Check response code", response.code(), is(400));
        assertThat("Check response error", mappedResponse.error, is("Either clientId or deviceId or userId and brand must be provided."));
        assertThat("Check response status", mappedResponse.status, is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get lexisNexisData error client not found")
    @AllureId("684")
    void getLexisNexisDataTest9() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("columnNames", "ucid");
        queryParams.put("clientId", client.getUcid() + "123");
        Response response = getLexisNexisData(queryParams);
        String responseBody = response.body().string();

        GetLexisNexisDataResponse mappedResponse = objectMapper.readValue(responseBody, GetLexisNexisDataResponse.class);

        assertThat("Check response code", response.code(), is(200));
        assertThat("Check response ucid", mappedResponse.totalCount, is(0));
        assertThat("Check response item", mappedResponse.items, empty());
    }

    @Test
    @DisplayName("Clickhouse Api. Get lexisNexisData success response by brand+userId (200)")
    @AllureId("685")
    void getLexisNexisDataTest10() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("brand", client.getBrand());
        queryParams.put("userId", client.getUserId());
        queryParams.put("columnNames", "id");
        Response response = getLexisNexisData(queryParams);
        String responseBody = response.body().string();

        GetLexisNexisDataResponse mappedResponse = objectMapper.readValue(responseBody, GetLexisNexisDataResponse.class);

        assertThat("Check response code", response.code(), is(200));
        assertThat("Check response id", mappedResponse.totalCount, is(2));
        assertThat("Check response item", mappedResponse.items.toString(), containsString(object1.getId().toString()));
        assertThat("Check response item", mappedResponse.items.toString(), containsString(object2.getId().toString()));
    }

    @Test
    @DisplayName("Clickhouse Api. Get lexisNexisData success response with multiple columns (200)")
    @AllureId("686")
    void getLexisNexisDataTest11() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("brand", client.getBrand());
        queryParams.put("userId", client.getUserId());
        queryParams.put("columnNames", List.of("id", "mobile_code", "eventId"));
        Response response = getLexisNexisData(queryParams);
        String responseBody = response.body().string();

        GetLexisNexisDataResponse mappedResponse = objectMapper.readValue(responseBody, GetLexisNexisDataResponse.class);

        assertThat("Check response code", response.code(), is(200));
        assertThat("Check response", mappedResponse.totalCount, is(2));
        assertThat("Check response item", mappedResponse.items, containsInAnyOrder(getItem(String.valueOf(
                object1.getId()), 60, 123), getItem(String.valueOf(object2.getId()), 60, 123)));
    }

    @Test
    @DisplayName("Clickhouse Api. Get lexisNexisData success response by deviceId (200)")
    @AllureId("874")
    void getLexisNexisDataTest12() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("deviceId", client.getDeviceId());
        queryParams.put("columnNames", "id");
        Response response = getLexisNexisData(queryParams);
        String responseBody = response.body().string();

        GetLexisNexisDataResponse mappedResponse = objectMapper.readValue(responseBody, GetLexisNexisDataResponse.class);

        assertThat("Check response code", response.code(), is(200));
        assertThat("Check response id", mappedResponse.totalCount, is(2));
        assertThat("Check response item", mappedResponse.items.toString(), containsString(object1.getId().toString()));
        assertThat("Check response item", mappedResponse.items.toString(), containsString(object2.getId().toString()));
    }
}
