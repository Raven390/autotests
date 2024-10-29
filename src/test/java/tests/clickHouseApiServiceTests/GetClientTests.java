package tests.clickHouseApiServiceTests;

import static businessObjects.api.clickhouseApiService.getClient.GetClientRequest.getClient;
import static helpers.database.DbHelper.insertObjectToDb;
import static businessObjects.db.crmTbUserTable.CrmTbUserObjectFactory.generateUserByUserId;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static utils.Constants.*;

import businessObjects.api.clickhouseApiService.getClient.GetClientResponse;
import businessObjects.api.clickhouseApiService.getClient.GetClientResponseError;
import businessObjects.db.crmTbUserTable.CrmTbUserObject;
import io.qameta.allure.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Response;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.TestBaseApi;
import utils.Utils;

@Feature(FEATURE_CLICKHOUSE_API_SERVICE)
@Story(STORY_CLICKHOUSE_API_SERVICE_GET_CLIENT)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
public class GetClientTests extends TestBaseApi {

    @Test
    @DisplayName("Clickhouse Api. Get client success(200)")
    @AllureId("59")
    public void getClientSuccessTest() throws IOException, ReflectiveOperationException, SQLException {
        Integer userId = Utils.getRandomIntPositive();
        String brand = "vantage";
        String ucid = brand+"-"+userId;
        CrmTbUserObject object = generateUserByUserId(userId);
        insertObjectToDb("vindex_test.crm__tb_user", object);

        // Execute request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("userId", ucid);
        Response response = getClient(ucid);

        // Assert response
        assert response.body() != null;
        GetClientResponse client = objectMapper.readValue(response.body().string(), GetClientResponse.class);
        assertThat("Check response code", response.code(), is(200));
        assertThat("Check clientId", client.clientId, is(ucid));
        assertThat("Check userId", client.userId, is(userId.toString()));
        assertThat("Check brand", client.brand, is("Vantage"));
        assertThat("Check regulator", client.regulator, is("VSFC"));
        assertThat("Check registrationDate", client.registrationDate, is("2024-10-23T14:56:59Z"));
        assertThat("Check firstName", client.firstName, is("Test"));
        assertThat("Check lastName", client.lastName, is("User"));
        assertThat("Check gender", client.gender, is("1"));
        assertThat("Check birthday", client.birthday, is("1961-02-01"));
        assertThat("Check country", client.country, is("Cyprus"));
        assertThat("Check countryCode", client.countryCode, is("CY"));
        assertThat("Check isoCountryCode", client.isoCountryCode, is("CY"));
        assertThat("Check language", client.language, is("en"));
        assertThat("Check nationality", client.nationality, is("RUS"));
        assertThat("Check email", client.email, is("DUrksdLPlqZB6byC9vfKk6qm9BpUmsOS"));
        assertThat("Check phoneNum", client.phoneNum, is("BjrbbdAHkwhBFLnPclfvbg=="));
        assertThat("Check phoneCountryCode", client.phoneCountryCode, is("357"));
        assertThat("Check twoFaUser", client.twoFaUser, is("true"));
        assertThat("Check authentication", client.authentication, is("2FA"));
        assertThat("Check websiteUserType", client.websiteUserType, is("2"));
        assertThat("Check emailVerificationDate", client.emailVerificationDate, is("2024-10-23T15:14:17.232Z"));
        assertThat("Check phoneVerificationDate", client.phoneVerificationDate, is("2024-10-23T15:14:10.722Z"));
        assertThat("Check ibId", client.ibId, is("1"));
        assertThat("Check cpaId", client.cpaId, is("2"));
        assertThat("Check rafReferrerId", client.rafReferrerId, is("3"));
        assertThat("Check phoneVerificationDate", client.kycStatus, is("PARTIAL_KYC_ID_PASS"));
        assertThat("Check phoneVerificationDate", client.lastUpdated, is("2024-10-29T09:55:01.3Z"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get client not found (404)")
    @AllureId("61")
    public void getClientNotFoundTest() throws IOException {
        Response response = getClient("AlphaTick-999");
        assert response.body() != null;
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
        assert response.body() != null;
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
