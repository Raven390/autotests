package tests.click_house_api_service_tests;

import static business_objects.api.clickhouse_api_service.get_client.GetClientRequest.getClient;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
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
    @DisplayName("Clickhouse Api. Get client by ucid success(200)")
    @AllureId("59")
    void getClientSuccessTest() throws IOException, InterruptedException {
        // Create an instance of ClientHelper
        ClientHelper client = getRandomVantageClient();
        client.setCountry("Cyprus");
        client.setCountryCode("CY");
        client.setIbId(3);
        client.setCpaId(4);
        client.setReferrerId(5);
        CrmTbUserObject userObject = generateUserByClient(client);
        insertObjectToDb(CRM_USER_TABLE_NAME, userObject);
        // Execute request
        Response response = getClient(client.getUcid());
        Thread.sleep(2000);

        // Assert response
        assertThat(response.body(), is(notNullValue()));
        GetClientResponse getClientResponse = objectMapper.readValue(response.body().string(), GetClientResponse.class);
        assertThat("Check response code", 200, is(response.code()));
        assertThat("Check clientId", client.getUcid(), is(getClientResponse.getClientId()));
        assertThat("Check userId", client.getUserId().toString(), is(getClientResponse.getUserId()));
        assertThat("Check brand", "Vantage", is(getClientResponse.getBrand()));
        assertThat("Check regulator", "VFSC", is(getClientResponse.getRegulator()));
        assertThat("Check registrationDate", "2025-01-30T14:56:59Z", is(getClientResponse.getRegistrationDate()));
        assertThat("Check firstName", "Test", is(getClientResponse.getFirstName()));
        assertThat("Check lastName", "User", is(getClientResponse.getLastName()));
        assertThat("Check gender", "1", is(getClientResponse.getGender()));
        assertThat("Check birthday", "1961-02-01", is(getClientResponse.getBirthday()));
        assertThat("Check country", "Cyprus", is(getClientResponse.getCountry()));
        assertThat("Check countryCode", "CY", is(getClientResponse.getCountryCode()));
        assertThat("Check isoCountryCode", "CY", is(getClientResponse.getIsoCountryCode()));
        assertThat("Check language", "en", is(getClientResponse.getLanguage()));
        assertThat("Check nationality", "RUS", is(getClientResponse.getNationality()));
        assertThat("Check email", is(notNullValue()));
        assertThat("Check email", is(instanceOf(String.class)));
        assertThat("Check phoneNum", is(notNullValue()));
        assertThat("Check phoneNum", is(instanceOf(String.class)));
        assertThat("Check phoneCountryCode", "357", is(getClientResponse.getPhoneCountryCode()));
        assertThat("Check twoFaUser", "true", is(getClientResponse.getTwoFaUser()));
        assertThat("Check authentication", "2FA", is(getClientResponse.getAuthentication()));
        assertThat("Check websiteUserType", "2", is(getClientResponse.getWebsiteUserType()));
        assertThat("Check emailVerificationMark", "1", is(getClientResponse.getEmailVerificationMark()));
        assertThat("Check phoneVerificationMark", "2", is(getClientResponse.getPhoneVerificationMark()));
        assertThat("Check ibId", "3", is(getClientResponse.getIbId()));
        assertThat("Check cpaId", "4", is(getClientResponse.getCpaId()));
        assertThat("Check rafReferrerId", "5", is(getClientResponse.getRafReferrerId()));
        assertThat("Check kycStatus", "PARTIAL_KYC_ID_PASS", is(getClientResponse.getKycStatus()));
        assertThat("Check lastUpdated", userObject.lastUpdated, is(timestampFromIsoToDb(getClientResponse.getLastUpdated())));
        assertThat("Check poi completion time", userObject.poiCompleteTs, is(timestampFromIsoToDb(getClientResponse.getPoiCompletionTime())));
    }

    @Test
    @DisplayName("Clickhouse Api. Get client not found (404)")
    @AllureId("61")
    void getClientNotFoundTest() throws IOException {
        Response response = getClient("AlphaTick-999");
        assertThat(response.body(), is(notNullValue()));
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 404", response.code(), is(404));
        assertThat("Assert that code is 404", mappedResponse.getStatus(), is(404));
        assertThat("Assert error text", mappedResponse.getError(), is("Client data is not found for the request with parameters: {clientId=AlphaTick-999}."));
    }

    @Test
    @DisplayName("Clickhouse Api. Get client bad request (400)")
    @AllureId("62")
    void getClientBadRequestTest() throws IOException {
        Response response = getClient(1);
        assertThat(response.body(), is(notNullValue()));
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);

        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert that code is 400", mappedResponse.getStatus(), is(400));
        assertThat("Assert error text", mappedResponse.getError(), containsString("Invalid clientId format"));
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
