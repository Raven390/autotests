package tests.production_tests;

import business_objects.api.clickhouse_api_service.get_client.GetClientResponse;
import business_objects.api.clickhouse_api_service.get_clients.GetClientsResponse;
import business_objects.api.clickhouse_api_service.get_deposits.GetDepositsResponse;
import business_objects.api.clickhouse_api_service.get_lexis_nexis.GetLexisNexisResponse;
import business_objects.api.clickhouse_api_service.get_swap_free_volumes.GetSwapFreeVolumesResponse;
import helpers.http_helper.HttpHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Story;
import okhttp3.Response;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.TestBaseApi;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.ConfigFactory.*;
import static utils.Constants.*;

@Tag(LAYER_API)
@Tag(SUITE_SMOKE_PROD)
@Story(STORY_PRODUCTION_TESTS)
class ClickHouseProductionSmokeTests extends TestBaseApi {

    private final String userId = "3384621";
    private final String brand = "Vantage";
    private final String ucid = brand.toLowerCase() + "-" + userId;
    private final Integer tradingAccount = 52_625_605;
    private final Integer serverId = 1;

    // TODO ADD REAL DATA
    @Test
    @DisplayName("Production smoke test. Get abuse types (200)")
    @AllureId("741")
    void testClickHouseApiProd1() throws IOException {
        Map<String, Object> queryParamsMap = new HashMap<>();
        queryParamsMap.put("clientIds", "vantage-3384621");
        Response response = new HttpHelper().sendGetRequest(CLICKHOUSE_API_BASE_PROD + CLICKHOUSE_API_GET_ABUSE_TYPES, null, queryParamsMap);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    // TODO ADD REAL DATA
    @Test
    @DisplayName("Production smoke test. Get balance orders (200)")
    @AllureId("799")
    void testClickHouseApiProd2() throws IOException {
        Map<String, Object> queryParamsMap = new HashMap<>();
        queryParamsMap.put("tradingAccount", "3384621");
        queryParamsMap.put("serverId", 1);
        Response response = new HttpHelper().sendGetRequest(CLICKHOUSE_API_BASE_PROD + CLICKHOUSE_API_GET_BALANCE_ORDERS, null, queryParamsMap);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    // TODO ADD REAL DATA
    @Test
    @DisplayName("Production smoke test. Get bonuses (200)")
    @AllureId("800")
    void testClickHouseApiProd3() throws IOException {
        Map<String, Object> queryParamsMap = new HashMap<>();
        queryParamsMap.put("clientIds", "vantage-123");
        Response response = new HttpHelper().sendGetRequest(CLICKHOUSE_API_BASE_PROD + CLICKHOUSE_API_GET_BONUSES, null, queryParamsMap);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Production smoke test. Get clients (200)")
    @AllureId("801")
    void testClickHouseApiProd4() throws IOException {
        Map<String, Object> queryParamsMap = new HashMap<>();
        queryParamsMap.put("userId", userId);
        queryParamsMap.put("brand", "Vantage");
        Response response = new HttpHelper().sendGetRequest(CLICKHOUSE_API_BASE_PROD + CLICKHOUSE_API_GET_CLIENTS, null, queryParamsMap);
        assertThat("Assert that code is 200", response.code(), is(200));
        GetClientsResponse mappedResponse = objectMapper.readValue(response.body().string(), GetClientsResponse.class);
        assertThat("Assert clientId not null", mappedResponse.clientId, is(notNullValue()));
    }

    @Test
    @DisplayName("Production smoke test. Get client (200)")
    @AllureId("802")
    void testClickHouseApiProd5() throws IOException {
        Map<String, Object> queryParamsMap = new HashMap<>();
        queryParamsMap.put("clientId", ucid);
        Response response = new HttpHelper().sendGetRequest(CLICKHOUSE_API_BASE_PROD + CLICKHOUSE_API_GET_CLIENT + ucid, null, null);

        assertThat("Assert that code is 200", response.code(), is(200));
        GetClientResponse mappedResponse = objectMapper.readValue(response.body().string(), GetClientResponse.class);
        assertThat("Assert clientId not null", mappedResponse.clientId, is(notNullValue()));
        assertThat("Assert userId not null", mappedResponse.userId, is(notNullValue()));
        assertThat("Assert brand not null", mappedResponse.brand, is(notNullValue()));
        assertThat("Assert regulator not null", mappedResponse.regulator, is(notNullValue()));
        assertThat("Assert registrationDate not null", mappedResponse.registrationDate, is(notNullValue()));
        assertThat("Assert registrationDate not null", mappedResponse.registrationDate, is(notNullValue()));
        assertThat("Assert firstName not null", mappedResponse.firstName, is(notNullValue()));
        assertThat("Assert lastName not null", mappedResponse.lastName, is(notNullValue()));
        assertThat("Assert gender not null", mappedResponse.gender, is(notNullValue()));
        assertThat("Assert birthday not null", mappedResponse.birthday, is(notNullValue()));
        assertThat("Assert country not null", mappedResponse.country, is(notNullValue()));
        assertThat("Assert countryCode not null", mappedResponse.countryCode, is(notNullValue()));
        assertThat("Assert isoCountryCode not null", mappedResponse.isoCountryCode, is(notNullValue()));
        assertThat("Assert language not null", mappedResponse.language, is(notNullValue()));
        assertThat("Assert nationality not null", mappedResponse.nationality, is(notNullValue()));
        assertThat("Assert email not null", mappedResponse.email, is(notNullValue()));
        assertThat("Assert phoneNum not null", mappedResponse.phoneNum, is(notNullValue()));
        assertThat("Assert phoneCountryCode not null", mappedResponse.phoneCountryCode, is(notNullValue()));
        assertThat("Assert twoFaUser not null", mappedResponse.twoFaUser, is(notNullValue()));
        assertThat("Assert authentication not null", mappedResponse.authentication, is(notNullValue()));
        assertThat("Assert websiteUserType not null", mappedResponse.websiteUserType, is(notNullValue()));
        assertThat("Assert emailVerificationMark is null", mappedResponse.emailVerificationMark, is("0"));
        assertThat("Assert phoneVerificationMark is null", mappedResponse.phoneVerificationMark, is("0"));
        assertThat("Assert ibId is null", mappedResponse.ibId, is("0"));
        assertThat("Assert cpaId is null", mappedResponse.cpaId, is(nullValue()));
        assertThat("Assert kycStatus not null", mappedResponse.kycStatus, is(notNullValue()));
        assertThat("Assert lastUpdated not null", mappedResponse.lastUpdated, is(notNullValue()));

    }

    // TODO ADD REAL DATA
    @Test
    @DisplayName("Production smoke test. Get client trading account (200)")
    @AllureId("803")
    void testClickHouseApiProd6() throws IOException {
        Map<String, Object> queryParamsMap = new HashMap<>();
        queryParamsMap.put("clientId", ucid);
        Response response = new HttpHelper().sendGetRequest(CLICKHOUSE_API_BASE_TEST + CLICKHOUSE_API_GET_CLIENT_TRADING_ACCOUNTS.replace("{clientId}", ucid), null, queryParamsMap);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    // TODO ADD REAL DATA
    @Test
    @DisplayName("Production smoke test. Get credit equity ratio (200)")
    @AllureId("804")
    void testClickHouseApiProd7() throws IOException {
        Map<String, Object> queryParamsMap = new HashMap<>();
        queryParamsMap.put("tradingAccount", tradingAccount);
        queryParamsMap.put("serverId", serverId);
        Response response = new HttpHelper().sendGetRequest(CLICKHOUSE_API_BASE_PROD + CLICKHOUSE_API_GET_CREDIT_EQUITY, null, queryParamsMap);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    // TODO ADD REAL DATA
    @Test
    @DisplayName("Production smoke test. Get credit risk free revenue ratio (200)")
    @AllureId("805")
    void testClickHouseApiProd8() throws IOException {
        Map<String, Object> queryParamsMap = new HashMap<>();
        queryParamsMap.put("tradingAccount", tradingAccount);
        queryParamsMap.put("serverId", serverId);
        Response response = new HttpHelper().sendGetRequest(CLICKHOUSE_API_BASE_PROD + CLICKHOUSE_API_GET_CREDIT_RISK_FREE_REVENUE_RATIO, null, queryParamsMap);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    // TODO ADD REAL DATA
    @Test
    @DisplayName("Production smoke test. Get credits (200)")
    @AllureId("806")
    void testClickHouseApiProd9() throws IOException {
        Map<String, Object> queryParamsMap = new HashMap<>();
        queryParamsMap.put("tradingAccount", tradingAccount);
        queryParamsMap.put("serverId", serverId);
        Response response = new HttpHelper().sendGetRequest(CLICKHOUSE_API_BASE_PROD + CLICKHOUSE_API_GET_CREDITS, null, queryParamsMap);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Production smoke test. Get deposits (200)")
    @AllureId("807")
    void testClickHouseApiProd10() throws IOException {
        Map<String, Object> queryParamsMap = new HashMap<>();
        queryParamsMap.put("clientId", ucid);
        Response response = new HttpHelper().sendGetRequest(CLICKHOUSE_API_BASE_PROD + CLICKHOUSE_API_GET_DEPOSITS, null, queryParamsMap);
        assertThat("Assert that code is 200", response.code(), is(200));
        GetDepositsResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetDepositsResponse[].class);
        assertThat("Assert transferId not null", mappedResponse[0].transferId, is(notNullValue()));
        assertThat("Assert actualAmount not null", mappedResponse[0].actualAmount, is(notNullValue()));
        assertThat("Assert actualAmountUsd not null", mappedResponse[0].actualAmountUsd, is(notNullValue()));
        assertThat("Assert createTime not null", mappedResponse[0].createTime, is(notNullValue()));
        assertThat("Assert paymentChannel not null", mappedResponse[0].paymentChannel, is(notNullValue()));
        assertThat("Assert clientId not null", mappedResponse[0].clientId, is(notNullValue()));
    }

    // TODO ADD REAL DATA
    @Test
    @DisplayName("Production smoke test. Get floating trades group by (200)")
    @AllureId("808")
    void testClickHouseApiProd11() throws IOException {
        Map<String, Object> queryParamsMap = new HashMap<>();
        queryParamsMap.put("tradingAccount", tradingAccount);
        queryParamsMap.put("serverId", serverId);
        Response response = new HttpHelper().sendGetRequest(CLICKHOUSE_API_BASE_PROD + CLICKHOUSE_API_GET_FLOATING_TRADES_GROUP_BY, null, queryParamsMap);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    // TODO ADD REAL DATA
    @Test
    @DisplayName("Production smoke test. Get lexis nexis data (200)")
    @AllureId("809")
    void testClickHouseApiProd12() throws IOException {
        Map<String, Object> queryParamsMap = new HashMap<>();
        queryParamsMap.put("clientId", ucid);
        Response response = new HttpHelper().sendGetRequest(CLICKHOUSE_API_BASE_PROD + CLICKHOUSE_API_GET_LEXIS_NEXIS_DATA, null, queryParamsMap);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Production smoke test. Get lexis nexis (200)")
    @AllureId("810")
    void testClickHouseApiProd13() throws IOException {
        Map<String, Object> queryParamsMap = new HashMap<>();
        queryParamsMap.put("clientId", ucid);
        queryParamsMap.put("eventType", "login");
        queryParamsMap.put("eventId", "0");
        Response response = new HttpHelper().sendGetRequest(CLICKHOUSE_API_BASE_PROD + CLICKHOUSE_API_GET_LEXIS_NEXIS, null, queryParamsMap);
        assertThat("Assert that code is 200", response.code(), is(200));
        GetLexisNexisResponse mappedResponse = objectMapper.readValue(response.body().string(), GetLexisNexisResponse.class);
        assertThat("Assert ucid not null", mappedResponse.ucid, is(ucid));
        assertThat("Assert uid not null", mappedResponse.uid, is(ucid));
        assertThat("Assert userId not null", mappedResponse.userId, is(notNullValue()));
        assertThat("Assert id not null", mappedResponse.id, is(notNullValue()));
        assertThat("Assert brand not null", mappedResponse.brand, is(notNullValue()));
        assertThat("Assert sessionId not null", mappedResponse.sessionId, is(notNullValue()));
        assertThat("Assert sessionId not null", mappedResponse.sessionId, is(notNullValue()));
        assertThat("Assert email not null", mappedResponse.email, is(notNullValue()));
        assertThat("Assert mobileCode not null", mappedResponse.mobileCode, is(notNullValue()));
        assertThat("Assert eventType not null", mappedResponse.eventType, is(notNullValue()));
        assertThat("Assert isFromApp not null", mappedResponse.isFromApp, is(notNullValue()));
        assertThat("Assert createTime not null", mappedResponse.createTime, is(notNullValue()));
        assertThat("Assert policyScore not null", mappedResponse.policyScore, is(notNullValue()));
        assertThat("Assert riskRating not null", mappedResponse.riskRating, is(notNullValue()));
        assertThat("Assert deviceId not null", mappedResponse.deviceId, is(notNullValue()));
        assertThat("Assert digitalId not null", mappedResponse.digitalId, is(notNullValue()));
        assertThat("Assert eventDatetime not null", mappedResponse.eventDatetime, is(notNullValue()));
        assertThat("Assert eventId not null", mappedResponse.eventId, is(notNullValue()));
        // EMPTY DATA because of not using vpn
//        assertThat("Assert proxyIp not null", mappedResponse.proxyIp, is(notNullValue()));
//        assertThat("Assert proxyIpActivities not null", mappedResponse.proxyIpActivities, is(notNullValue()));
//        assertThat("Assert proxyIpAttributes not null", mappedResponse.proxyIpAttributes, is(notNullValue()));
//        assertThat("Assert proxyIpConnectionType not null", mappedResponse.proxyIpConnectionType, is(notNullValue()));
//        assertThat("Assert proxyIpFirstSeen not null", mappedResponse.proxyIpFirstSeen, is(notNullValue()));
//        assertThat("Assert proxyIpGeo not null", mappedResponse.proxyIpGeo, is(notNullValue()));
//        assertThat("Assert proxyIpHome not null", mappedResponse.proxyIpHome, is(notNullValue()));
//        assertThat("Assert proxyIpIsp not null", mappedResponse.proxyIpIsp, is(notNullValue()));
//        assertThat("Assert proxyIpLatitude not null", mappedResponse.proxyIpLatitude, is(notNullValue()));
//        assertThat("Assert proxyIpLongitude not null", mappedResponse.proxyIpLongitude, is(notNullValue()));
//        assertThat("Assert proxyIpOrganization not null", mappedResponse.proxyIpOrganization, is(notNullValue()));
//        assertThat("Assert proxyIpOrganizationType not null", mappedResponse.proxyIpOrganizationType, is(notNullValue()));
//        assertThat("Assert proxyIpRegion not null", mappedResponse.proxyIpRegion, is(notNullValue()));
//        assertThat("Assert proxyIpResult not null", mappedResponse.proxyIpResult, is(notNullValue()));
//        assertThat("Assert proxyIpRoutingType not null", mappedResponse.proxyIpRoutingType, is(notNullValue()));
//        assertThat("Assert proxyIpScore not null", mappedResponse.proxyIpScore, is(notNullValue()));
//        assertThat("Assert proxyIpWorstScore not null", mappedResponse.proxyIpWorstScore, is(notNullValue()));
//        assertThat("Assert proxyIpv6 not null", mappedResponse.proxyIpv6, is(notNullValue()));
//        assertThat("Assert proxyName not null", mappedResponse.proxyName, is(notNullValue()));
//        assertThat("Assert proxyScore not null", mappedResponse.proxyScore, is(notNullValue()));
//        assertThat("Assert proxyType not null", mappedResponse.proxyType, is(notNullValue()));
        assertThat("Assert trueIp not null", mappedResponse.trueIp, is(notNullValue()));
        assertThat("Assert trueIpActivities not null", mappedResponse.trueIpActivities, is(notNullValue()));
        assertThat("Assert trueIpAttributes not null", mappedResponse.trueIpAttributes, is(notNullValue()));
        assertThat("Assert trueIpCity not null", mappedResponse.trueIpCity, is(notNullValue()));
        assertThat("Assert trueIpCountryConfidence not null", mappedResponse.trueIpCountryConfidence, is(notNullValue()));
        assertThat("Assert trueIpFirstSeen not null", mappedResponse.trueIpFirstSeen, is(notNullValue()));
        assertThat("Assert trueIpFirstSeen not null", mappedResponse.trueIpFirstSeen, is(notNullValue()));
        assertThat("Assert trueIpGeo not null", mappedResponse.trueIpGeo, is(notNullValue()));
        assertThat("Assert trueIpIsp not null", mappedResponse.trueIpIsp, is(notNullValue()));
        assertThat("Assert trueIpLastEvent not null", mappedResponse.trueIpLastEvent, is(notNullValue()));
        assertThat("Assert trueIpOrganization not null", mappedResponse.trueIpOrganization, is(notNullValue()));
        assertThat("Assert trueIpOrganizationType not null", mappedResponse.trueIpOrganizationType, is(notNullValue()));
        assertThat("Assert trueIpOrganizationType not null", mappedResponse.trueIpOrganizationType, is(notNullValue()));
        assertThat("Assert trueIpPostalCode not null", mappedResponse.trueIpPostalCode, is(notNullValue()));
        assertThat("Assert trueIpRegion not null", mappedResponse.trueIpRegion, is(notNullValue()));
        assertThat("Assert trueIpResult not null", mappedResponse.trueIpResult, is(notNullValue()));
        assertThat("Assert trueIpRoutingType not null", mappedResponse.trueIpRoutingType, is(notNullValue()));
        assertThat("Assert trueIpScore not null", mappedResponse.trueIpScore, is(notNullValue()));
        assertThat("Assert trueIpWorstScore not null", mappedResponse.trueIpWorstScore, is(notNullValue()));
        assertThat("Assert trueIpv6 not null", mappedResponse.trueIpv6, is(notNullValue()));
        assertThat("Assert vpnScore not null", mappedResponse.vpnScore, is(notNullValue()));

    }

    // TODO ADD REAL DATA
    @Test
    @DisplayName("Production smoke test. Get mirror accounts by trades (200)")
    @AllureId("811")
    void testClickHouseApiProd14() throws IOException {
        Map<String, Object> queryParamsMap = new HashMap<>();
        queryParamsMap.put("tradingAccount", tradingAccount);
        queryParamsMap.put("serverId", serverId);
        queryParamsMap.put("symbol", "EURUSD");
        Response response = new HttpHelper().sendGetRequest(CLICKHOUSE_API_BASE_PROD + CLICKHOUSE_API_GET_MIRROR_ACCOUNTS_BY_TRADES, null, queryParamsMap);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    // TODO ADD REAL DATA
    @Test
    @DisplayName("Production smoke test. Get swap free fees (200)")
    @AllureId("812")
    void testClickHouseApiProd15() throws IOException {
        Map<String, Object> queryParamsMap = new HashMap<>();
        queryParamsMap.put("tradingAccount", tradingAccount);
        queryParamsMap.put("serverId", serverId);
        Response response = new HttpHelper().sendGetRequest(CLICKHOUSE_API_BASE_PROD + CLICKHOUSE_API_GET_SWAP_FREE_FEES, null, queryParamsMap);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Disabled("Response body: {\"error\":\"Endpoint '/v1/swapFreeVolumes' is disabled by the administrator.\",\"status\":404}\n")
    @Test
    @DisplayName("Production smoke test. Get swap free volumes (200)")
    @AllureId("813")
    void testClickHouseApiProd16() throws IOException {
        Map<String, Object> queryParamsMap = new HashMap<>();
        queryParamsMap.put("tradingAccount", tradingAccount);
        queryParamsMap.put("serverId", serverId);
        Response response = new HttpHelper().sendGetRequest(CLICKHOUSE_API_BASE_PROD + CLICKHOUSE_API_GET_SWAP_FREE_VOLUMES, null, queryParamsMap);
        assertThat("Assert that code is 200", response.code(), is(200));
        GetSwapFreeVolumesResponse mappedResponse = objectMapper.readValue(response.body().string(), GetSwapFreeVolumesResponse.class);
        assertThat("Assert indicatorDate not null", mappedResponse.indicatorDate, is(notNullValue()));
        assertThat("Assert tradingIndicators not null", mappedResponse.tradingIndicators, is(notNullValue()));
    }

    // TODO ADD REAL DATA
    @Test
    @DisplayName("Production smoke test. Get trades group by (200)")
    @AllureId("814")
    void testClickHouseApiProd17() throws IOException {
        Map<String, Object> queryParamsMap = new HashMap<>();
        queryParamsMap.put("tradingAccount", tradingAccount);
        queryParamsMap.put("serverId", serverId);
        Response response = new HttpHelper().sendGetRequest(CLICKHOUSE_API_BASE_PROD + CLICKHOUSE_API_GET_TRADES_GROUP_BY, null, queryParamsMap);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    // TODO ADD REAL DATA
    @Test
    @DisplayName("Production smoke test. Get trades (200)")
    @AllureId("815")
    void testClickHouseApiProd18() throws IOException {
        Map<String, Object> queryParamsMap = new HashMap<>();
        queryParamsMap.put("tradingAccount", tradingAccount);
        queryParamsMap.put("serverId", serverId);
        Response response = new HttpHelper().sendGetRequest(CLICKHOUSE_API_BASE_PROD + CLICKHOUSE_API_GET_TRADES, null, queryParamsMap);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    // TODO ADD REAL DATA
    @Test
    @DisplayName("Production smoke test. Get unclosed trades (200)")
    @AllureId("816")
    void testClickHouseApiProd19() throws IOException {
        Map<String, Object> queryParamsMap = new HashMap<>();
        queryParamsMap.put("tradingAccount", tradingAccount);
        queryParamsMap.put("serverId", serverId);
        Response response = new HttpHelper().sendGetRequest(CLICKHOUSE_API_BASE_PROD + CLICKHOUSE_API_GET_UNCLOSED_TRADES, null, queryParamsMap);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    // TODO ADD REAL DATA
    @Test
    @DisplayName("Production smoke test. Get withdrawals (200)")
    @AllureId("817")
    void testClickHouseApiProd20() throws IOException {
        Map<String, Object> queryParamsMap = new HashMap<>();
        queryParamsMap.put("clientId", ucid);
        Response response = new HttpHelper().sendGetRequest(CLICKHOUSE_API_BASE_PROD + CLICKHOUSE_API_GET_WITHDRAWALS, null, queryParamsMap);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Production smoke test. Get lexisNexis v2 (200)")
    @AllureId("1011")
    void testClickHouseApiProd21() throws IOException {
        Map<String, Object> queryParamsMap = new HashMap<>();
        queryParamsMap.put("clientId", ucid);
        queryParamsMap.put("eventType", "login");
        Response response = new HttpHelper().sendGetRequest(CLICKHOUSE_API_BASE_PROD + CLICKHOUSE_API_GET_LEXIS_NEXIS_V2, null, queryParamsMap);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Production smoke test. Get fast trades (200)")
    @AllureId("1012")
    void testClickHouseApiProd22() throws IOException {
        Map<String, Object> queryParamsMap = new HashMap<>();
        queryParamsMap.put("clientId", ucid);
        queryParamsMap.put("tradingAccount", tradingAccount);
        queryParamsMap.put("serverId", serverId);
        queryParamsMap.put("tradeDurationSeconds", 1);
        Response response = new HttpHelper().sendGetRequest(CLICKHOUSE_API_BASE_PROD + CLICKHOUSE_API_GET_FAST_TRADES, null, queryParamsMap);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Production smoke test. Get trades by trade id (200)")
    @AllureId("1013")
    void testClickHouseApiProd23() throws IOException {
        Map<String, Object> queryParamsMap = new HashMap<>();
        queryParamsMap.put("clientId", "vt-733549");
        queryParamsMap.put("tradingAccount", 11_115_040);
        queryParamsMap.put("serverId", 57);
        Response response = new HttpHelper().sendGetRequest(CLICKHOUSE_API_BASE_PROD + CLICKHOUSE_API_GET_TRADES_BY_TRADE_ID.replace("{tradeId}", "2015940769"), null, queryParamsMap);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Production smoke test. Get total loyalties (200)")
    @AllureId("1014")
    void testClickHouseApiProd24() throws IOException {
        Map<String, Object> queryParamsMap = new HashMap<>();
        queryParamsMap.put("clientId", ucid);
        Response response = new HttpHelper().sendGetRequest(CLICKHOUSE_API_BASE_PROD + CLICKHOUSE_API_GET_TOTAL_LOYALTIES, null, queryParamsMap);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Production smoke test. Get mirror clients by trades (200)")
    @AllureId("1015")
    void testClickHouseApiProd25() throws IOException {
        Map<String, Object> queryParamsMap = new HashMap<>();
        queryParamsMap.put("clientId", ucid);
        Response response = new HttpHelper().sendGetRequest(CLICKHOUSE_API_BASE_PROD + CLICKHOUSE_API_GET_MIRROR_CLIENTS_BY_TRADES, null, queryParamsMap);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Production smoke test. Get Financial Calendar (200)")
    @AllureId("1016")
    void testClickHouseApiProd26() throws IOException {
        Response response = new HttpHelper().sendGetRequest(CLICKHOUSE_API_BASE_PROD + CLICKHOUSE_API_GET_FINANCIAL_CALENDAR, null, null);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Production smoke test. Get Dummy trade data (200)")
    @AllureId("1017")
    void testClickHouseApiProd27() throws IOException {
        Map<String, Object> queryParamsMap = new HashMap<>();
        queryParamsMap.put("clientId", ucid);
        queryParamsMap.put("tradingAccount", tradingAccount);
        queryParamsMap.put("serverId", serverId);
        Response response = new HttpHelper().sendGetRequest(CLICKHOUSE_API_BASE_PROD + CLICKHOUSE_API_GET_DUMMY_TRADE_DATA, null, queryParamsMap);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Production smoke test. Get account balance (200)")
    @AllureId("1018")
    void testClickHouseApiProd28() throws IOException {
        Map<String, Object> queryParamsMap = new HashMap<>();
        queryParamsMap.put("clientId", ucid);
        queryParamsMap.put("tradingAccount", tradingAccount);
        queryParamsMap.put("serverId", serverId);
        queryParamsMap.put("dateTo", "2030-01-01T00:00:00");
        Response response = new HttpHelper().sendGetRequest(CLICKHOUSE_API_BASE_PROD + CLICKHOUSE_API_GET_ACCOUNT_BALANCE, null, queryParamsMap);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Production smoke test. Get abnormal profit (200)")
    @AllureId("1019")
    void testClickHouseApiProd29() throws IOException {
        Map<String, Object> queryParamsMap = new HashMap<>();
        queryParamsMap.put("clientId", ucid);
        Response response = new HttpHelper().sendGetRequest(CLICKHOUSE_API_BASE_PROD + CLICKHOUSE_API_GET_ABNORMAL_PROFIT, null, queryParamsMap);
        assertThat("Assert that code is 200", response.code(), is(200));
    }
}
