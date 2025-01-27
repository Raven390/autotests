package tests.clickHouseApiServiceTests;

import businessObjects.api.clickhouseApiService.getLexisNexis.GetLexisNexisResponse;
import businessObjects.db.clickhouse.lnSessionParsed.LnSessionParsedObject;
import helpers.data.ClientHelper;
import io.qameta.allure.*;
import okhttp3.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.TestBaseApi;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static businessObjects.api.clickhouseApiService.getLexisNexis.GetLexisNexisRequest.getLexisNexis;
import static businessObjects.db.clickhouse.lnSessionParsed.LnSessionParsedObjectFactory.generateLexisNexisDataByClient;
import static helpers.data.ClientFactory.getRandomVantageClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.DbHelper.insertObjectToDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

@Feature(FEATURE_CLICKHOUSE_API_SERVICE)
@Story(STORY_CLICKHOUSE_API_SERVICE_GET_LEXIS_NEXIS)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CLICKHOUSE_API_SERVICE)
public class GetLexisNexisTests extends TestBaseApi {

    static String eventTypeRegistration = "registration";
    static String eventTypeLogin = "login";
    static ClientHelper client = getRandomVantageClientAllFields();
    static ClientHelper client2 = getRandomVantageClient();
    static ClientHelper client3 = getRandomVantageClient();
    static ClientHelper client4 = getRandomVantageClient();
    static LnSessionParsedObject event = generateLexisNexisDataByClient(client);
    static LnSessionParsedObject event2 = generateLexisNexisDataByClient(client2);
    static LnSessionParsedObject event3 = generateLexisNexisDataByClient(client3);
    static LnSessionParsedObject event4 = generateLexisNexisDataByClient(client4);

    @BeforeAll
    public static void setupData() throws ReflectiveOperationException, SQLException {
        insertObjectToDb(LEXIS_NEXIS_TABLE_NAME, event);
        insertObjectToDb(LEXIS_NEXIS_TABLE_NAME, event2);
        insertObjectToDb(LEXIS_NEXIS_TABLE_NAME, event3);
        insertObjectToDb(LEXIS_NEXIS_TABLE_NAME, event4);
    }

    @Test
    @DisplayName("Clickhouse Api. Get lexisNexis success response(200)")
    @AllureId("141")
    public void getLexisNexisTest1() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", client.getUcid());
        queryParams.put("eventType", eventTypeRegistration);
        queryParams.put("eventId", event.eventId);
        Response response = getLexisNexis(queryParams);
        String responseBody = response.body().string();
        GetLexisNexisResponse[] lexisNexisResponse = objectMapper.readValue(responseBody, GetLexisNexisResponse[].class);

        assertThat("Check response code", response.code(), is(200));
        assertThat("Check response id", lexisNexisResponse[0].id, is(event.id));
        assertThat("Check response brand", lexisNexisResponse[0].brand, is(client.getBrand()));
        assertThat("Check response sessionId", lexisNexisResponse[0].sessionId, is(event.sessionId));
        assertThat("Check response userId", lexisNexisResponse[0].userId, is(client.getUserId()));
        assertThat("Check response email", lexisNexisResponse[0].email, is(event.email));
        assertThat("Check response mobileCode", lexisNexisResponse[0].mobileCode, is("60"));
        assertThat("Check response mobile", lexisNexisResponse[0].mobile, is("123456"));
        assertThat("Check response eventType", lexisNexisResponse[0].eventType, is("registration"));
        assertThat("Check response isFromApp", lexisNexisResponse[0].isFromApp, is(true));
        assertThat("Check response createTime", lexisNexisResponse[0].createTime, is("1970-01-01T00:00:00Z"));
        assertThat("Check response policyScore", lexisNexisResponse[0].policyScore, is(0));
        assertThat("Check response riskRating", lexisNexisResponse[0].riskRating, is("12"));
        assertThat("Check response deviceId", lexisNexisResponse[0].deviceId, is(client.getDeviceId()));
        assertThat("Check response digitalId", lexisNexisResponse[0].digitalId, is("12345"));
        assertThat("Check response eventDateTime", lexisNexisResponse[0].eventDatetime, is("1971-01-01T00:00:00Z"));
        assertThat("Check response eventId", lexisNexisResponse[0].eventId, is(123));
        assertThat("Check response proxyIp", lexisNexisResponse[0].proxyIp, is("127.0.0.1"));
        //assertThat("Check response proxyIpActivities", Arrays.asList(lexisNexisResponse[0].proxyIpActivities), is(List.of("proxyIpActivities")));
        //assertThat("Check response proxyIpAttributes", Arrays.asList(lexisNexisResponse[0].proxyIpAttributes), is(Arrays.asList("String_1", "String_2")));
        assertThat("Check response proxyIpCity", lexisNexisResponse[0].proxyIpCity, is("proxyIpCity"));
        assertThat("Check response proxyIpConnectionType", lexisNexisResponse[0].proxyIpConnectionType, is("proxyIpConnection"));
        assertThat("Check response proxyIpFirstSeen", lexisNexisResponse[0].proxyIpFirstSeen, is("1972-01-01"));
        assertThat("Check response proxyIpGeo", lexisNexisResponse[0].proxyIpGeo, is("proxyIpGeo"));
        assertThat("Check response proxyIpHome", lexisNexisResponse[0].proxyIpHome, is("proxyIpHome"));
        assertThat("Check response proxyIpIsp", lexisNexisResponse[0].proxyIpIsp, is("proxyIpIsp"));
        assertThat("Check response proxyIpLatitude", lexisNexisResponse[0].proxyIpLatitude, is(30.300_00));
        assertThat("Check response proxyIpLongitude", lexisNexisResponse[0].proxyIpLongitude, is(40.400_00));
        assertThat("Check response proxyIpOrganization", lexisNexisResponse[0].proxyIpOrganization, is("proxyIpOrganization"));
        assertThat("Check response proxyIpOrganizationType", lexisNexisResponse[0].proxyIpOrganizationType, is("proxyIpOrganizationType"));
        assertThat("Check response proxyIpPostalCode", lexisNexisResponse[0].proxyIpPostalCode, is("proxyIpPostalCode"));
        assertThat("Check response proxyIpRegion", lexisNexisResponse[0].proxyIpRegion, is("proxyIpRegion"));
        assertThat("Check response proxyIpResult", lexisNexisResponse[0].proxyIpResult, is("proxyIpResult"));
        assertThat("Check response proxyIpRoutingType", lexisNexisResponse[0].proxyIpRoutingType, is("proxyIpRoutingType"));
        assertThat("Check response proxyIpScore", lexisNexisResponse[0].proxyIpScore, is(2));
        assertThat("Check response proxyIpWorstScore", lexisNexisResponse[0].proxyIpWorstScore, is(2));
        assertThat("Check response proxyIpV6", lexisNexisResponse[0].proxyIpv6, is("proxyIpV6"));
        assertThat("Check response proxyName", lexisNexisResponse[0].proxyName, is("proxyName"));
        assertThat("Check response proxyScore", lexisNexisResponse[0].proxyScore, is(3.00));
        assertThat("Check response proxyType", lexisNexisResponse[0].proxyType, is("proxyType"));
        assertThat("Check response trueIp", lexisNexisResponse[0].trueIp, is("192.168.0.1"));
        assertThat("Check response trueIpActivities", lexisNexisResponse[0].trueIpActivities, is("trueIpActivities"));
        //assertThat("Check response trueIpAttributes", Arrays.asList(lexisNexisResponse[0].trueIpAttributes), is(Arrays.asList("String_1", "String_2")));
        assertThat("Check response proxyIpCity", lexisNexisResponse[0].proxyIpCity, is("proxyIpCity"));
        assertThat("Check response trueIpCity", lexisNexisResponse[0].trueIpCity, is("trueIpCity"));
        assertThat("Check response trueIpCountryConfidence", lexisNexisResponse[0].trueIpCountryConfidence, is(4));
        assertThat("Check response trueIpFirstSeen", lexisNexisResponse[0].trueIpFirstSeen, is("1973-01-01"));
        assertThat("Check response trueIpGeo", lexisNexisResponse[0].trueIpGeo, is("trueIpGeo"));
        assertThat("Check response trueIpIsp", lexisNexisResponse[0].trueIpIsp, is("trueIpIsp"));
        assertThat("Check response trueIpLastEvent", lexisNexisResponse[0].trueIpLastEvent, is("1973-01-01"));
        assertThat("Check response trueIpOrganization", lexisNexisResponse[0].trueIpOrganization, is("trueIpOrganization"));
        assertThat("Check response trueIpOrganizationType", lexisNexisResponse[0].trueIpOrganizationType, is("trueIpOrganizationType"));
        assertThat("Check response trueIpPostalCode", lexisNexisResponse[0].trueIpPostalCode, is("trueIpPostalCode"));
        assertThat("Check response trueIpRegion", lexisNexisResponse[0].trueIpRegion, is("trueIpRegion"));
        assertThat("Check response trueIpResult", lexisNexisResponse[0].trueIpResult, is("trueIpResult"));
        assertThat("Check response trueIpRoutingType", lexisNexisResponse[0].trueIpRoutingType, is("trueIpRoutingType"));
        assertThat("Check response trueIpScore", lexisNexisResponse[0].trueIpScore, is(100));
        assertThat("Check response trueIpWorstScore", lexisNexisResponse[0].trueIpWorstScore, is(1));
        assertThat("Check response trueIpv6", lexisNexisResponse[0].trueIpv6, is("1"));
        assertThat("Check response vpnScore", lexisNexisResponse[0].vpnScore, is(1));
    }

    @Test
    @DisplayName("Clickhouse Api. Get lexisNexis 400 error without brand parameter")
    @AllureId("151")
    public void getLexisNexisTest2() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("userId", client.getUserId());
        queryParams.put("eventType", eventTypeRegistration);
        queryParams.put("eventId", event.eventId);
        Response response = getLexisNexis(queryParams);

        assertThat("Check response code", response.code(), is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get lexisNexis 400 error without userId parameter")
    @AllureId("152")
    public void getLexisNexisTest3() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("brand", client.getBrand());
        queryParams.put("eventType", eventTypeRegistration);
        queryParams.put("eventId", event.eventId);
        Response response = getLexisNexis(queryParams);

        assertThat("Check response code", response.code(), is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get lexisNexis 400 error without eventType and eventId parameters")
    @AllureId("154")
    public void getLexisNexisTest4() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("userId", client.getUserId());
        queryParams.put("brand", client.getBrand());
        Response response = getLexisNexis(queryParams);

        assertThat("Check response code", response.code(), is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get lexisNexis 200 when eventType registration and eventId=null")
    @AllureId("153")
    public void getLexisNexisTest6() throws IOException {
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
    public void getLexisNexisTest7() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("brand", 1);
        queryParams.put("userId", 1);
        queryParams.put("eventType", eventTypeRegistration);
        Response response = getLexisNexis(queryParams);
        assert response.body() != null;
        String responseBody = response.body().string();

        GetLexisNexisResponse[] mappedResponse = objectMapper.readValue(responseBody, GetLexisNexisResponse[].class);

        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert that body is empty", mappedResponse.length, is(0));
    }

    @Test
    @DisplayName("Clickhouse Api. Get lexisNexis internal server error response(500)")
    @AllureId("144")
    @Tag(TAG_MANUAL)
    public void getLexisNexisTest8() {
        //Can't check it with automation tests
    }

    @Test
    @DisplayName("Clickhouse Api. Get lexisNexis event if eventId filled search is made for exact id")
    @AllureId("283")
    public void getLexisNexisTest9() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", client2.getUcid());
        queryParams.put("eventType", eventTypeRegistration);
        queryParams.put("eventId", event2.eventId);
        Response response = getLexisNexis(queryParams);
        String responseBody = response.body().string();
        GetLexisNexisResponse[] lexisNexisResponse = objectMapper.readValue(responseBody, GetLexisNexisResponse[].class);

        assertThat("Check response code", response.code(), is(200));
        assertThat("Check response code", lexisNexisResponse[0].eventId, is(event2.eventId));
    }

    @Test
    @DisplayName("Clickhouse Api. Get lexisNexis event If not filled - last event of selected type")
    @AllureId("284")
    public void getLexisNexisTest10() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", client3.getUcid());
        queryParams.put("eventType", eventTypeRegistration);
        Response response = getLexisNexis(queryParams);
        String responseBody = response.body().string();
        GetLexisNexisResponse[] lexisNexisResponse = objectMapper.readValue(responseBody, GetLexisNexisResponse[].class);
        System.out.println(lexisNexisResponse[0].eventId);
        System.out.println(event3.eventId);
        System.out.println(event3.id);
        assertThat("Check response code", response.code(), is(200));
        assertThat("Check response code", lexisNexisResponse[0].eventId, is(event3.eventId));


    }

    @Test
    @DisplayName("Clickhouse Api. Get lexisNexis event If event_dateTime= null - sort by createTime")
    @AllureId("285")
    public void getLexisNexisTest11() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("userId", client4.getUserId());
        queryParams.put("brand", client4.getBrand());
        queryParams.put("eventType", eventTypeRegistration);
        Response response = getLexisNexis(queryParams);
        String responseBody = response.body().string();
        GetLexisNexisResponse[] lexisNexisResponse = objectMapper.readValue(responseBody, GetLexisNexisResponse[].class);

        assertThat("Check response code", response.code(), is(200));
        assertThat("Check response code", lexisNexisResponse[0].uid, is(client4.getUcid()));
        assertThat("Check response code", lexisNexisResponse[0].eventId, is(event4.eventId));
    }

}
