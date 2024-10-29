package tests.clickHouseApiServiceTests;

import helpers.clickhouseApiService.getLexisNexis.GetLexisNexisResponse;
import helpers.database.dbObjects.LnSessionParsedTable.LnSessionParsedObject;
import io.qameta.allure.*;
import okhttp3.Response;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.TestBaseApi;
import utils.Utils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static helpers.clickhouseApiService.getLexisNexis.GetLexisNexisRequest.getLexisNexis;
import static helpers.database.DbHelper.insertObjectToDb;
import static helpers.database.dbObjects.LnSessionParsedTable.LnSessionParsedObjectFactory.generateLexisNexisDataForUserId;
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
    static Integer eventId = 123;

    @Test
    @DisplayName("Clickhouse Api. Get lexisNexis success response(200)")
    @AllureId("141")
    public void getLexisNexisSuccessfulTest() throws IOException, ReflectiveOperationException, SQLException {

        Integer userId = Utils.getRandomIntPositive();
        String uid = Utils.getRandomUuidString();
        LnSessionParsedObject object = generateLexisNexisDataForUserId(uid,userId,eventId);
        insertObjectToDb("vindex_test.ln__session_parsed", object);

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("brand", brand);
        queryParams.put("userId", userId);
        queryParams.put("eventType", eventTypeRegistration);
        queryParams.put("eventId", eventId);
        Response response = getLexisNexis(queryParams);
        String responseBody = response.body().string();
        System.out.println(responseBody);
        GetLexisNexisResponse lexisNexisResponse = objectMapper.readValue(responseBody, GetLexisNexisResponse.class);

        assertThat("Check response code", response.code(), is(200));
        assertThat("Check response uid", lexisNexisResponse.uid, is(uid));
        assertThat("Check response id", lexisNexisResponse.id, is(123));
        assertThat("Check response brand", lexisNexisResponse.brand, is("vt"));
        assertThat("Check response sessionId", lexisNexisResponse.sessionId, is("sessionId"));
        assertThat("Check response userId", lexisNexisResponse.userId, is(userId));
        assertThat("Check response email", lexisNexisResponse.email, is("email@email.com"));
        assertThat("Check response mobileCode", lexisNexisResponse.mobileCode, is("60"));
        assertThat("Check response mobile", lexisNexisResponse.mobile, is("123456"));
        assertThat("Check response eventType", lexisNexisResponse.eventType, is("registration"));
        assertThat("Check response isFromApp", lexisNexisResponse.isFromApp, is(true));
        assertThat("Check response createTime", lexisNexisResponse.createTime, is("1970-01-01T00:00:00Z"));
        assertThat("Check response policyScore", lexisNexisResponse.policyScore, is(0));
        assertThat("Check response riskRating", lexisNexisResponse.riskRating, is("12"));
        assertThat("Check response deviceId", lexisNexisResponse.deviceId, is("device_id"));
        assertThat("Check response digitalId", lexisNexisResponse.digitalId, is("12345"));
        assertThat("Check response eventDateTime", lexisNexisResponse.eventDatetime, is("1971-01-01T00:00:00Z"));
        assertThat("Check response eventId", lexisNexisResponse.eventId, is(123));
        assertThat("Check response proxyIp", lexisNexisResponse.proxyIp, is("127.0.0.1"));
        assertThat("Check response proxyIpActivities", lexisNexisResponse.proxyIpActivities, is("proxyIpActivities"));
        assertThat("Check response proxyIpAttributes", Arrays.asList(lexisNexisResponse.proxyIpAttributes), is(Arrays.asList("String_1", "String_2")));
        assertThat("Check response proxyIpCity", lexisNexisResponse.proxyIpCity, is("proxyIpCity"));
        assertThat("Check response proxyIpConnectionType", lexisNexisResponse.proxyIpConnectionType, is("proxyIpConnection"));
        assertThat("Check response proxyIpFirstSeen", lexisNexisResponse.proxyIpFirstSeen, is("1972-01-01"));
        assertThat("Check response proxyIpGeo", lexisNexisResponse.proxyIpGeo, is("proxyIpGeo"));
        assertThat("Check response proxyIpHome", lexisNexisResponse.proxyIpHome, is("proxyIpHome"));
        assertThat("Check response proxyIpIsp", lexisNexisResponse.proxyIpIsp, is("proxyIpIsp"));
        assertThat("Check response proxyIpLatitude", lexisNexisResponse.proxyIpLatitude, is(30.300_00));
        assertThat("Check response proxyIpLongitude", lexisNexisResponse.proxyIpLongitude, is(40.400_00));
        assertThat("Check response proxyIpOrganization", lexisNexisResponse.proxyIpOrganization, is("proxyIpOrganization"));
        assertThat("Check response proxyIpOrganizationType", lexisNexisResponse.proxyIpOrganizationType, is("proxyIpOrganizationType"));
        assertThat("Check response proxyIpPostalCode", lexisNexisResponse.proxyIpPostalCode, is("proxyIpPostalCode"));
        assertThat("Check response proxyIpRegion", lexisNexisResponse.proxyIpRegion, is("proxyIpRegion"));
        assertThat("Check response proxyIpResult", lexisNexisResponse.proxyIpResult, is("proxyIpResult"));
        assertThat("Check response proxyIpRoutingType", lexisNexisResponse.proxyIpRoutingType, is("proxyIpRoutingType"));
        assertThat("Check response proxyIpScore", lexisNexisResponse.proxyIpScore, is(2));
        assertThat("Check response proxyIpWorstScore", lexisNexisResponse.proxyIpWorstScore, is(2));
        assertThat("Check response proxyIpV6", lexisNexisResponse.proxyIpv6, is("proxyIpV6"));
        assertThat("Check response proxyName", lexisNexisResponse.proxyName, is("proxyName"));
        assertThat("Check response proxyScore", lexisNexisResponse.proxyScore, is(3.00));
        assertThat("Check response proxyType", lexisNexisResponse.proxyType, is("proxyType"));
        assertThat("Check response trueIp", lexisNexisResponse.trueIp, is("192.168.0.1"));
        assertThat("Check response trueIpActivities", lexisNexisResponse.trueIpActivities, is("trueIpActivities"));
        assertThat("Check response trueIpAttributes", Arrays.asList(lexisNexisResponse.trueIpAttributes), is(Arrays.asList("String_1", "String_2")));
        assertThat("Check response proxyIpCity", lexisNexisResponse.proxyIpCity, is("proxyIpCity"));
        assertThat("Check response trueIpCity", lexisNexisResponse.trueIpCity, is("trueIpCity"));
        assertThat("Check response trueIpCountryConfidence", lexisNexisResponse.trueIpCountryConfidence, is(4));
        assertThat("Check response trueIpFirstSeen", lexisNexisResponse.trueIpFirstSeen, is("1973-01-01"));
        assertThat("Check response trueIpGeo", lexisNexisResponse.trueIpGeo, is("trueIpGeo"));
        assertThat("Check response trueIpIsp", lexisNexisResponse.trueIpIsp, is("trueIpIsp"));
        assertThat("Check response trueIpLastEvent", lexisNexisResponse.trueIpLastEvent, is("1973-01-01"));
        assertThat("Check response trueIpOrganization", lexisNexisResponse.trueIpOrganization, is("trueIpOrganization"));
        assertThat("Check response trueIpOrganizationType", lexisNexisResponse.trueIpOrganizationType, is("trueIpOrganizationType"));
        assertThat("Check response trueIpPostalCode", lexisNexisResponse.trueIpPostalCode, is("trueIpPostalCode"));
        assertThat("Check response trueIpRegion", lexisNexisResponse.trueIpRegion, is("trueIpRegion"));
        assertThat("Check response trueIpResult", lexisNexisResponse.trueIpResult, is("trueIpResult"));
        assertThat("Check response trueIpRoutingType", lexisNexisResponse.trueIpRoutingType, is("trueIpRoutingType"));
        assertThat("Check response trueIpScore", lexisNexisResponse.trueIpScore, is(100));
        assertThat("Check response trueIpWorstScore", lexisNexisResponse.trueIpWorstScore, is(1));
        assertThat("Check response trueIpv6", lexisNexisResponse.trueIpv6, is("1"));
        assertThat("Check response vpnScore", lexisNexisResponse.vpnScore, is(1));
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
        //GetLexisNexisResponse lexisNexisResponse = objectMapper.readValue(response.body().string(), GetLexisNexisResponse.class);

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
        //GetLexisNexisResponse lexisNexisResponse = objectMapper.readValue(response.body().string(), GetLexisNexisResponse.class);

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

        assertThat("Assert that code is 404", response.code(), is(404));
    }

    @Disabled
    @Test
    @DisplayName("Clickhouse Api. Get lexisNexis internal server error response(500)")
    @AllureId("144")
    public void getLexisNexisInternalErrorTest() {
        //Can't check it with automation tests
    }
}
