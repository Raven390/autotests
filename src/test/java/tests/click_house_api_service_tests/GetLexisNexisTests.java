package tests.click_house_api_service_tests;

import static business_objects.api.clickhouse_api_service.get_lexis_nexis.GetLexisNexisRequest.getLexisNexis;
import static business_objects.db.clickhouse.ln_session_parsed.LnSessionParsedObjectFactory.generateLexisNexisDataByClient;
import static helpers.data.ClientFactory.getRandomVantageClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.DbHelper.insertObjectsToDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Utils.writeLog;

import business_objects.api.clickhouse_api_service.get_lexis_nexis.GetLexisNexisResponse;
import business_objects.db.clickhouse.ln_session_parsed.LnSessionParsedObject;
import helpers.data.ClientHelper;
import io.qameta.allure.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import tests.TestBaseApi;

@Feature(FEATURE_CLICKHOUSE_API_SERVICE)
@Story(STORY_CLICKHOUSE_API_SERVICE_GET_LEXIS_NEXIS)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CLICKHOUSE_API_SERVICE)
class GetLexisNexisTests extends TestBaseApi {

    private static String eventTypeRegistration = "registration";
    private static ClientHelper client = getRandomVantageClientAllFields();
    private static ClientHelper client2 = getRandomVantageClient();
    private static ClientHelper client3 = getRandomVantageClient();
    private static ClientHelper client4 = getRandomVantageClient();
    private static LnSessionParsedObject event = generateLexisNexisDataByClient(client);
    private static LnSessionParsedObject event2 = generateLexisNexisDataByClient(client2);
    private static LnSessionParsedObject event3 = generateLexisNexisDataByClient(client3);
    private static LnSessionParsedObject event4 = generateLexisNexisDataByClient(client4);

    @BeforeAll
    static void setup() {
        insertObjectsToDb(LEXIS_NEXIS_TABLE_NAME, List.of(event, event2, event3, event4));
    }

    @Test
    @DisplayName("Clickhouse Api. Get lexisNexis success response(200)")
    @AllureId("141")
    void getLexisNexisTest1() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", client.getUcid());
        queryParams.put("eventType", eventTypeRegistration);
        queryParams.put("eventId", event.getEventId());
        Response response = getLexisNexis(queryParams);
        String responseBody = response.body().string();
        GetLexisNexisResponse lexisNexisResponse = objectMapper.readValue(responseBody, GetLexisNexisResponse.class);

        assertThat("Check response code", response.code(), is(200));
        assertThat("Check response id", lexisNexisResponse.id, is(event.getId()));
        assertThat("Check response brand", lexisNexisResponse.brand, is(client.getBrand()));
        assertThat("Check response sessionId", lexisNexisResponse.sessionId, is(event.getSessionId()));
        assertThat("Check response userId", lexisNexisResponse.userId, is(client.getUserId()));
        assertThat("Check response email", lexisNexisResponse.email, is(event.getEmail()));
        assertThat("Check response mobileCode", lexisNexisResponse.mobileCode, is("60"));
        assertThat("Check response mobile", lexisNexisResponse.mobile, is("123456"));
        assertThat("Check response eventType", lexisNexisResponse.eventType, is("registration"));
        assertThat("Check response isFromApp", lexisNexisResponse.isFromApp, is(true));
        assertThat("Check response createTime", lexisNexisResponse.createTime, is("1970-01-01T00:00:00Z"));
        assertThat("Check response policyScore", lexisNexisResponse.policyScore, is(0));
        assertThat("Check response riskRating", lexisNexisResponse.riskRating, is("12"));
        assertThat("Check response deviceId", lexisNexisResponse.deviceId, is(client.getDeviceId()));
        assertThat("Check response digitalId", lexisNexisResponse.digitalId, is("12345"));
        assertThat("Check response eventDateTime", lexisNexisResponse.eventDatetime, is("1971-01-01T00:00:00Z"));
        assertThat("Check response eventId", lexisNexisResponse.eventId, is(123));
        assertThat("Check response proxyIp", lexisNexisResponse.proxyIp, is("127.0.0.1"));
        assertThat("Check response proxyIpCity", lexisNexisResponse.proxyIpCity, is("proxyIpCity"));
        assertThat(
                "Check response proxyIpConnectionType",
                lexisNexisResponse.proxyIpConnectionType,
                is("proxyIpConnection"));
        assertThat("Check response proxyIpFirstSeen", lexisNexisResponse.proxyIpFirstSeen, is("1972-01-01"));
        assertThat("Check response proxyIpGeo", lexisNexisResponse.proxyIpGeo, is("proxyIpGeo"));
        assertThat("Check response proxyIpHome", lexisNexisResponse.proxyIpHome, is("proxyIpHome"));
        assertThat("Check response proxyIpIsp", lexisNexisResponse.proxyIpIsp, is("proxyIpIsp"));
        assertThat("Check response proxyIpLatitude", lexisNexisResponse.proxyIpLatitude, is(30.300_00));
        assertThat("Check response proxyIpLongitude", lexisNexisResponse.proxyIpLongitude, is(40.400_00));
        assertThat(
                "Check response proxyIpOrganization",
                lexisNexisResponse.proxyIpOrganization,
                is("proxyIpOrganization"));
        assertThat(
                "Check response proxyIpOrganizationType",
                lexisNexisResponse.proxyIpOrganizationType,
                is("proxyIpOrganizationType"));
        assertThat("Check response proxyIpPostalCode", lexisNexisResponse.proxyIpPostalCode, is("proxyIpPostalCode"));
        assertThat("Check response proxyIpRegion", lexisNexisResponse.proxyIpRegion, is("proxyIpRegion"));
        assertThat("Check response proxyIpResult", lexisNexisResponse.proxyIpResult, is("proxyIpResult"));
        assertThat(
                "Check response proxyIpRoutingType", lexisNexisResponse.proxyIpRoutingType, is("proxyIpRoutingType"));
        assertThat("Check response proxyIpScore", lexisNexisResponse.proxyIpScore, is(2));
        assertThat("Check response proxyIpWorstScore", lexisNexisResponse.proxyIpWorstScore, is(2));
        assertThat("Check response proxyIpV6", lexisNexisResponse.proxyIpv6, is("proxyIpV6"));
        assertThat("Check response proxyName", lexisNexisResponse.proxyName, is("proxyName"));
        assertThat("Check response proxyScore", lexisNexisResponse.proxyScore, is(3.00));
        assertThat("Check response proxyType", lexisNexisResponse.proxyType, is("proxyType"));
        assertThat("Check response trueIp", lexisNexisResponse.trueIp, is("192.168.0.1"));
        assertThat("Check response trueIpActivities", lexisNexisResponse.trueIpActivities, is("trueIpActivities"));
        assertThat("Check response proxyIpCity", lexisNexisResponse.proxyIpCity, is("proxyIpCity"));
        assertThat("Check response trueIpCity", lexisNexisResponse.trueIpCity, is("trueIpCity"));
        assertThat("Check response trueIpCountryConfidence", lexisNexisResponse.trueIpCountryConfidence, is(4));
        assertThat("Check response trueIpFirstSeen", lexisNexisResponse.trueIpFirstSeen, is("1973-01-01"));
        assertThat("Check response trueIpGeo", lexisNexisResponse.trueIpGeo, is("trueIpGeo"));
        assertThat("Check response trueIpIsp", lexisNexisResponse.trueIpIsp, is("trueIpIsp"));
        assertThat("Check response trueIpLastEvent", lexisNexisResponse.trueIpLastEvent, is("1973-01-01"));
        assertThat(
                "Check response trueIpOrganization", lexisNexisResponse.trueIpOrganization, is("trueIpOrganization"));
        assertThat(
                "Check response trueIpOrganizationType",
                lexisNexisResponse.trueIpOrganizationType,
                is("trueIpOrganizationType"));
        assertThat("Check response trueIpPostalCode", lexisNexisResponse.trueIpPostalCode, is("trueIpPostalCode"));
        assertThat("Check response trueIpRegion", lexisNexisResponse.trueIpRegion, is("trueIpRegion"));
        assertThat("Check response trueIpResult", lexisNexisResponse.trueIpResult, is("trueIpResult"));
        assertThat("Check response trueIpRoutingType", lexisNexisResponse.trueIpRoutingType, is("trueIpRoutingType"));
        assertThat("Check response trueIpScore", lexisNexisResponse.trueIpScore, is(100));
        assertThat("Check response trueIpWorstScore", lexisNexisResponse.trueIpWorstScore, is(1));
        assertThat("Check response trueIpv6", lexisNexisResponse.trueIpv6, is("1"));
        assertThat("Check response vpnScore", lexisNexisResponse.vpnScore, is(1));
        assertThat("Check response browserStringHash", lexisNexisResponse.browserStringHash, is("string_hash"));
        assertThat("Check response browserLanguage", lexisNexisResponse.browserLanguage, is("EN"));
        assertThat("Check response inputIpGeo", lexisNexisResponse.inputIpGeo, is("input_ip_geo"));
        assertThat("Check response fuzzyDeviceId", lexisNexisResponse.fuzzyDeviceId, is("fuzzy_device_id"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get lexisNexis 400 error without brand parameter")
    @AllureId("151")
    void getLexisNexisTest2() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("userId", client.getUserId());
        queryParams.put("eventType", eventTypeRegistration);
        queryParams.put("eventId", event.getEventId());
        Response response = getLexisNexis(queryParams);

        assertThat("Check response code", response.code(), is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get lexisNexis 400 error without userId parameter")
    @AllureId("152")
    void getLexisNexisTest3() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("brand", client.getBrand());
        queryParams.put("eventType", eventTypeRegistration);
        queryParams.put("eventId", event.getEventId());
        Response response = getLexisNexis(queryParams);

        assertThat("Check response code", response.code(), is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get lexisNexis 400 error without eventType and eventId parameters")
    @AllureId("154")
    void getLexisNexisTest4() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("userId", client.getUserId());
        queryParams.put("brand", client.getBrand());
        Response response = getLexisNexis(queryParams);

        assertThat("Check response code", response.code(), is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get lexisNexis 200 when eventType registration and eventId=null")
    @AllureId("153")
    void getLexisNexisTest6() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("brand", client.getBrand());
        queryParams.put("userId", client.getUserId());
        queryParams.put("eventType", eventTypeRegistration);
        Response response = getLexisNexis(queryParams);

        assertThat("Check response code", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get lexisNexis client not found response (200)")
    @AllureId("142")
    void getLexisNexisTest7() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("brand", 1);
        queryParams.put("userId", 1);
        queryParams.put("eventType", eventTypeRegistration);
        Response response = getLexisNexis(queryParams);
        assertThat(response.body(), is(notNullValue()));
        String responseBody = response.body().string();

        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert that body is empty", responseBody, is("{}"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get lexisNexis internal server error response(500)")
    @AllureId("144")
    @Tag(TAG_MANUAL)
    void getLexisNexisTest8() {
        // Can't check it with automation tests
    }

    @Test
    @DisplayName("Clickhouse Api. Get lexisNexis event if eventId filled search is made for exact id")
    @AllureId("283")
    void getLexisNexisTest9() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", client2.getUcid());
        queryParams.put("eventType", eventTypeRegistration);
        queryParams.put("eventId", event2.getEventId());
        Response response = getLexisNexis(queryParams);
        String responseBody = response.body().string();
        GetLexisNexisResponse lexisNexisResponse = objectMapper.readValue(responseBody, GetLexisNexisResponse.class);

        assertThat("Check response code", response.code(), is(200));
        assertThat("Check response code", lexisNexisResponse.eventId, is(event2.getEventId()));
    }

    @Test
    @DisplayName("Clickhouse Api. Get lexisNexis event If not filled - last event of selected type")
    @AllureId("284")
    void getLexisNexisTest10() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", client3.getUcid());
        queryParams.put("eventType", eventTypeRegistration);
        Response response = getLexisNexis(queryParams);
        String responseBody = response.body().string();
        GetLexisNexisResponse lexisNexisResponse = objectMapper.readValue(responseBody, GetLexisNexisResponse.class);
        writeLog(lexisNexisResponse.eventId);
        writeLog(event3.getEventId());
        writeLog(event3.getId());
        assertThat("Check response code", response.code(), is(200));
        assertThat("Check response code", lexisNexisResponse.eventId, is(event3.getEventId()));
    }

    @Test
    @DisplayName("Clickhouse Api. Get lexisNexis event If event_dateTime= null - sort by createTime")
    @AllureId("285")
    void getLexisNexisTest11() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("userId", client4.getUserId());
        queryParams.put("brand", client4.getBrand());
        queryParams.put("eventType", eventTypeRegistration);
        Response response = getLexisNexis(queryParams);
        String responseBody = response.body().string();
        GetLexisNexisResponse lexisNexisResponse = objectMapper.readValue(responseBody, GetLexisNexisResponse.class);

        assertThat("Check response code", response.code(), is(200));
        assertThat("Check response code", lexisNexisResponse.uid, is(client4.getUcid()));
        assertThat("Check response code", lexisNexisResponse.eventId, is(event4.getEventId()));
    }
}
