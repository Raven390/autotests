package tests.click_house_api_service_tests;

import static business_objects.api.clickhouse_api_service.get_client.GetClientRequest.getClient;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateStaticUserByClient;
import static helpers.data.ClientFactory.getRandomVantageClient;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Utils.timestampFromIsoToDb;

import business_objects.api.clickhouse_api_service.ClickhouseApiErrorResponse;
import business_objects.api.clickhouse_api_service.get_client.GetClientResponse;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import helpers.data.ClientHelper;
import io.qameta.allure.*;
import java.io.IOException;
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
class GetClientTests extends TestBaseApi {

    @Test
    @DisplayName("Clickhouse Api. Get client success(200)")
    @AllureId("59")
    void getClientSuccessTest() throws IOException {
        // Create an instance of ClientHelper
        ClientHelper client = getRandomVantageClient();
        CrmTbUserObject userObject = generateStaticUserByClient(client);
        insertObjectToDb(CRM_USER_TABLE_NAME, userObject);
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
        assertThat("Check email", getClientResponse.email, is("VGlhbRQlxOaLfl/CgrjL1CfZEIYLXEQL"));
        assertThat("Check phoneNum", getClientResponse.phoneNum, is("cTsGbMYzhsD5SxSOhmgpmQ=="));
        assertThat("Check phoneCountryCode", getClientResponse.phoneCountryCode, is("357"));
        assertThat("Check twoFaUser", getClientResponse.twoFaUser, is("true"));
        assertThat("Check authentication", getClientResponse.authentication, is("2FA"));
        assertThat("Check websiteUserType", getClientResponse.websiteUserType, is("2"));
        assertThat("Check emailVerificationMark", getClientResponse.emailVerificationMark, is("1"));
        assertThat("Check phoneVerificationMark", getClientResponse.phoneVerificationMark, is("2"));
        assertThat("Check ibId", getClientResponse.ibId, is("1"));
        assertThat("Check cpaId", getClientResponse.cpaId, is("2"));
        assertThat("Check rafReferrerId", getClientResponse.rafReferrerId, is("3"));
        assertThat("Check kycStatus", getClientResponse.kycStatus, is("PARTIAL_KYC_ID_PASS"));
        assertThat("Check lastUpdated", timestampFromIsoToDb(getClientResponse.lastUpdated), is(userObject.lastUpdated));
        assertThat("Check poi completion time", timestampFromIsoToDb(getClientResponse.poiCompletionTime), is(userObject.poiCompleteTs));
    }

    @Test
    @DisplayName("Clickhouse Api. Get client not found (404)")
    @AllureId("61")
    void getClientNotFoundTest() throws IOException {
        Response response = getClient("AlphaTick-999");
        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 404", response.code(), is(404));
        assertThat("Assert that code is 404", mappedResponse.status, is(404));
        assertThat("Assert error text", mappedResponse.error, is("Client data is not found for the request with parameters: {clientId=AlphaTick-999}."));
    }

    @Test
    @DisplayName("Clickhouse Api. Get client bad request (400)")
    @AllureId("62")
    void getClientBadRequestTest() throws IOException {
        Response response = getClient(1);
        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);

        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert that code is 400", mappedResponse.status, is(400));
        assertThat("Assert error text", mappedResponse.error, containsString("Invalid clientId format"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get client internal error (500)")
    @AllureId("63")
    @Tag(TAG_MANUAL)
    void getClientServerErrorTest() {
        Allure.step("Shut down service");
        Allure.step("Send getClient request");
        Allure.step("Check that there are 500 error in response");
    }
}
