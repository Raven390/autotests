package tests.clickHouseApiServiceTests;

import static helpers.clickhouseApiService.getClient.GetClientRequest.getClient;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static utils.Constants.*;

import helpers.clickhouseApiService.getClient.GetClientResponse;
import helpers.clickhouseApiService.getClient.GetClientResponseError;
import io.qameta.allure.*;
import java.io.IOException;
import java.sql.SQLException;

import okhttp3.Response;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.TestBaseApi;

@Feature(FEATURE_CLICKHOUSE_API_SERVICE)
@Story(STORY_CLICKHOUSE_API_SERVICE_GET_CLIENT)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
public class GetClientTests extends TestBaseApi {

    @Test
    @DisplayName("Clickhouse Api. Get client success(200)")
    @AllureId("59")
    public void getClientSuccessTest() throws IOException, SQLException, ClassNotFoundException, InterruptedException {
        Response response = getClient("vt-1420224584");
        System.out.println(response);

        GetClientResponse client = objectMapper.readValue(response.body().string(), GetClientResponse.class);
        assertThat("Check response code", response.code(), is(200));
        assertThat("Check clientId", client.clientId, is("vt-1420224584"));
        assertThat("Check userId", client.userId, is("1420224584"));
        assertThat("Check brand", client.brand, is("VT"));
        assertThat("Check regulator", client.regulator, is("SVG"));
        assertThat("Check registrationDate", client.registrationDate, is("2024-04-09T00:00:00Z"));
        assertThat("Check firstName", client.firstName, is("Michael"));
        assertThat("Check lastName", client.lastName, is("Johnson"));
        assertThat("Check gender", client.gender, is("0"));
        assertThat("Check birthday", client.birthday, is("1975-05-17"));
        assertThat("Check country", client.country, is("Australia"));
        assertThat("Check countryCode", client.countryCode, is("123"));
        assertThat("Check isoCountryCode", client.isoCountryCode, is("AU"));
        assertThat("Check language", client.language, is("fr"));
        assertThat("Check nationality", client.nationality, is("GBR"));
        assertThat("Check email", client.email, is("DUrksdLPlqZB6byC9vfKk6qm9BpUmsOS"));
        assertThat("Check phoneNum", client.phoneNum, is("BjrbbdAHkwhBFLnPclfvbg=="));
        assertThat("Check phoneCountryCode", client.phoneCountryCode, is("543"));
        assertThat("Check twoFaUser", client.twoFaUser, is("false"));
        assertThat("Check authentication", client.authentication, is("Password"));
        assertThat("Check websiteUserType", client.websiteUserType, is("2"));
        assertThat("Check emailVerificationDate", client.emailVerificationDate, is("2024-04-18T13:44:21.819Z"));
        assertThat("Check phoneVerificationDate", client.phoneVerificationDate, is("2024-04-16T13:44:21.819Z"));
        assertThat("Check ibId", client.ibId, is("1484"));
        assertThat("Check cpaId", client.cpaId, is("8917"));
        assertThat("Check rafReferrerId", client.rafReferrerId, is("2969"));
        assertThat("Check phoneVerificationDate", client.kycStatus, is("PARTIAL_KYC_ID_PASS"));
        assertThat("Check phoneVerificationDate", client.lastUpdated, is("2024-10-28T08:59:40.553Z"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get client not found (404)")
    @AllureId("61")
    public void getClientNotFoundTest() throws IOException {
        Response response = getClient("AlphaTick-999");
        GetClientResponseError responseBody = objectMapper.readValue(response.body().string(), GetClientResponseError.class);
        assertThat("Assert that code is 404", response.code(), is(404));
        assertThat("Assert that code is 400", responseBody.status, is("404"));
        assertThat("Assert that code is 400", responseBody.error, is("Client not found."));
    }

    @Test
    @DisplayName("Clickhouse Api. Get client bad request (400)")
    @AllureId("62")
    public void getClientBadRequestTest() throws IOException {
        Response response = getClient(1);
        GetClientResponseError responseBody = objectMapper.readValue(response.body().string(), GetClientResponseError.class);

        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert that code is 400", responseBody.status, is("400"));
        assertThat("Assert that code is 400", responseBody.error, containsString("Invalid clientId format"));
    }

    @Disabled
    @Test
    @DisplayName("Clickhouse Api. Get client internal error (500)")
    @AllureId("63")
    public void getClientServerErrorTest() {
        Allure.step("Shut down service");
        Allure.step("Send getClient request");
        Allure.step("Check that there are 500 error in response");
    }
}
