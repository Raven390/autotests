package tests.clickHouseApiServiceTests;

import static businessObjects.api.clickhouseApiService.getClient.GetClientRequest.getClient;
import static businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObjectFactory.generateUserByClient;
import static helpers.data.ClientFactory.getRandomVantageClient;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

import businessObjects.api.clickhouseApiService.ClickhouseApiErrorResponse;
import businessObjects.api.clickhouseApiService.getClient.GetClientResponse;
import helpers.data.ClientHelper;
import io.qameta.allure.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.TestBaseApi;

@Feature(FEATURE_CLICKHOUSE_API_SERVICE)
@Story(STORY_CLICKHOUSE_API_SERVICE_GET_CLIENT)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CLICKHOUSE_API_SERVICE)
public class GetClientTests extends TestBaseApi {

    @Test
    @DisplayName("Clickhouse Api. Get client success(200)")
    @AllureId("59")
    public void getClientSuccessTest() throws IOException, ReflectiveOperationException, SQLException {
        // Create an instance of ClientHelper
        ClientHelper client = getRandomVantageClient();
        insertObjectToDb(CRM_USER_TABLE_NAME, generateUserByClient(client));
        // Execute request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("userId", client.getUcid());
        Response response = getClient(client.getUcid());

        // Assert response
        assert response.body() != null;
        GetClientResponse getClientResponse = objectMapper.readValue(response.body().string(), GetClientResponse.class);
        assertThat("Check response code", response.code(), is(200));
        assertThat("Check clientId", getClientResponse.clientId, is(client.getUcid()));
        assertThat("Check userId", getClientResponse.userId, is(client.getUserId().toString()));
        assertThat("Check brand", getClientResponse.brand, is("Vantage"));
        assertThat("Check regulator", getClientResponse.regulator, is("VFSC"));
        assertThat("Check registrationDate", getClientResponse.registrationDate, is("2024-10-23T14:56:59Z"));
        assertThat("Check firstName", getClientResponse.firstName, is("Test"));
        assertThat("Check lastName", getClientResponse.lastName, is("User"));
        assertThat("Check gender", getClientResponse.gender, is("1"));
        assertThat("Check birthday", getClientResponse.birthday, is("1961-02-01"));
        assertThat("Check country", getClientResponse.country, is("Cyprus"));
        assertThat("Check countryCode", getClientResponse.countryCode, is("CY"));
        assertThat("Check isoCountryCode", getClientResponse.isoCountryCode, is("CY"));
        assertThat("Check language", getClientResponse.language, is("en"));
        assertThat("Check nationality", getClientResponse.nationality, is("RUS"));
        assertThat("Check email", getClientResponse.email, is("DUrksdLPlqZB6byC9vfKk6qm9BpUmsOS"));
        assertThat("Check phoneNum", getClientResponse.phoneNum, is("BjrbbdAHkwhBFLnPclfvbg=="));
        assertThat("Check phoneCountryCode", getClientResponse.phoneCountryCode, is("357"));
        assertThat("Check twoFaUser", getClientResponse.twoFaUser, is("true"));
        assertThat("Check authentication", getClientResponse.authentication, is("2FA"));
        assertThat("Check websiteUserType", getClientResponse.websiteUserType, is("2"));
        assertThat("Check emailVerificationDate", getClientResponse.emailVerificationMark, is("1"));
        assertThat("Check phoneVerificationDate", getClientResponse.phoneVerificationMark, is("2"));
        assertThat("Check ibId", getClientResponse.ibId, is("1"));
        assertThat("Check cpaId", getClientResponse.cpaId, is("2"));
        assertThat("Check rafReferrerId", getClientResponse.rafReferrerId, is("3"));
        assertThat("Check phoneVerificationDate", getClientResponse.kycStatus, is("PARTIAL_KYC_ID_PASS"));
        assertThat("Check phoneVerificationDate", getClientResponse.lastUpdated, is("2024-10-29T09:55:01.3Z"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get client not found (404)")
    @AllureId("61")
    public void getClientNotFoundTest() throws IOException {
        Response response = getClient("AlphaTick-999");
        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 404", response.code(), is(404));
        assertThat("Assert that code is 400", mappedResponse.status, is(404));
        assertThat("Assert that code is 400", mappedResponse.error, is("Client not found."));
    }

    @Test
    @DisplayName("Clickhouse Api. Get client bad request (400)")
    @AllureId("62")
    public void getClientBadRequestTest() throws IOException {
        Response response = getClient(1);
        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);

        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert that code is 400", mappedResponse.status, is(400));
        assertThat("Assert that code is 400", mappedResponse.error, containsString("Invalid clientId format"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get client internal error (500)")
    @AllureId("63")
    @Tag(TAG_MANUAL)
    public void getClientServerErrorTest() {
        Allure.step("Shut down service");
        Allure.step("Send getClient request");
        Allure.step("Check that there are 500 error in response");
    }
}
