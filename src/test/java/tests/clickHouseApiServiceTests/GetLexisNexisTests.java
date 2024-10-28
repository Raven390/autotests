package tests.clickHouseApiServiceTests;

import helpers.clickhouseApiService.getLexisNexis.GetLexisNexisResponse;
import io.qameta.allure.*;
import okhttp3.Response;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.TestBaseApi;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static helpers.clickhouseApiService.getLexisNexis.GetLexisNexisRequest.getLexisNexis;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static utils.Constants.*;

@Feature(FEATURE_CLICKHOUSE_API_SERVICE)
@Story(STORY_CLICKHOUSE_API_SERVICE_GET_LEXIS_NEXIS)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
public class GetLexisNexisTests extends TestBaseApi {

    static String brand = "vt";
    static String userId = "6666";
    static String eventTypeRegistration = "registration";
    static String eventTypeLogin = "login";
    static Integer eventId = 555;

    @Test
    @DisplayName("Clickhouse Api. Get lexisNexis success response(200)")
    @AllureId("141")
    public void getLexisNexisSuccessfulTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("brand", brand);
        queryParams.put("userId", userId);
        queryParams.put("eventType", eventTypeRegistration);
        queryParams.put("eventId", eventId);
        Response response = getLexisNexis(queryParams);
        System.out.println(response);
        GetLexisNexisResponse lexisNexisResponse = objectMapper.readValue(response.body().string(), GetLexisNexisResponse.class);

        System.out.println(response.body());

        assertThat("Check response code", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get lexisNexis 400 error without brand parameter")
    @AllureId("151")
    public void getLexisNexisNoBrandTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("userId", userId);
        queryParams.put("eventType", eventTypeRegistration);
        queryParams.put("eventId", eventId);
        Response response = getLexisNexis(queryParams);
        System.out.println(response);
        //GetLexisNexisResponse lexisNexisResponse = objectMapper.readValue(response.body().string(), GetLexisNexisResponse.class);

        System.out.println(response.body());

        assertThat("Check response code", response.code(), is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get lexisNexis 400 error without userId parameter")
    @AllureId("152")
    public void getLexisNexisUserIdTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("brand", brand);
        queryParams.put("eventType", eventTypeRegistration);
        queryParams.put("eventId", eventId);
        Response response = getLexisNexis(queryParams);
        System.out.println(response);
        //GetLexisNexisResponse lexisNexisResponse = objectMapper.readValue(response.body().string(), GetLexisNexisResponse.class);

        System.out.println(response.body());

        assertThat("Check response code", response.code(), is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get lexisNexis 400 error without eventType and eventId parameters")
    @AllureId("154")
    public void getLexisNexisWithoutEventTypeIdTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("userId", userId);
        queryParams.put("brand", brand);
        Response response = getLexisNexis(queryParams);
        System.out.println(response);

        assertThat("Check response code", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get lexisNexis 400 error without eventId and eventType=login")
    @AllureId("153")
    public void getLexisNexisWithoutEventIdTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("brand", brand);
        queryParams.put("userId", userId);
        queryParams.put("eventType", eventTypeLogin);
        Response response = getLexisNexis(queryParams);
        System.out.println(response);

        assertThat("Check response code", response.code(), is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get lexisNexis 200 when eventType registration and eventId=null")
    @AllureId("153")
    public void getLexisNexisWithoutEventIdTest1() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("brand", brand);
        queryParams.put("userId", userId);
        queryParams.put("eventType", eventTypeRegistration);
        Response response = getLexisNexis(queryParams);
        System.out.println(response);

        assertThat("Check response code", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get lexisNexis client not found response(404)")
    @AllureId("142")
    public void getLexisNexisClientNotFoundTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("brand", 1);
        queryParams.put("userId", 1);
        queryParams.put("eventType", eventTypeRegistration);
        Response response = getLexisNexis(queryParams);
        System.out.println(response);

        assertThat("Assert that code is 404", response.code(), is(404));
    }

    @Disabled
    @Test
    @DisplayName("Clickhouse Api. Get lexisNexis internal server error response(500)")
    @AllureId("144")
    public void getLexisNexisInternalErrorTest() throws IOException {
        //Can't check it with automation tests
    }
}
