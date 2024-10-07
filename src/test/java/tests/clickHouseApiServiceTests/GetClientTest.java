package tests.clickHouseApiServiceTests;

import static helpers.clickhouseApiService.GetClientRequest.getClient;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static utils.Constants.*;

import helpers.clickhouseApiService.GetClientResponse;
import helpers.clickhouseApiService.GetClientResponseError;
import io.qameta.allure.*;
import java.io.IOException;
import okhttp3.Response;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.TestBaseApi;

public class GetClientTest extends TestBaseApi {

    @Test
    @DisplayName("Clickhouse Api. Get client success(200)")
    @Owner(OWNER_NIKOLAI_KORIAGIN)
    @Feature(FEATURE_CLICKHOUSE_API_SERVICE)
    @Tag(TEAM_CORE)
    @AllureId("59")
    public void getClientSuccessTest() throws IOException {
        Response response = getClient("AlphaTick-11-SVG");
        System.out.println(response);
        GetClientResponse client = objectMapper.readValue(response.body().string(), GetClientResponse.class);

        assertThat("Check response code", response.code(), is(200));
        assertThat("Check websiteUserType", client.websiteUserType, is("2"));
        assertThat("Check clientId", client.clientId, is("c9b1d679-cbc4-4c79-ade0-c30d93025eba"));
        assertThat("Check regulator", client.regulator, is("SVG"));
        assertThat("Check phoneNum", client.phoneNum, is("RElrvmE8Jkpejano3hhRzQ=="));
        assertThat("Check updateTime", client.updateTime, is("2024-08-09T07:24:12Z"));
        assertThat("Check lastName", client.lastName, is("client"));
        assertThat("Check twoFaUser", client.twoFaUser, is("false"));
        assertThat("Check firstName", client.firstName, is("chuck"));
        assertThat("Check email", client.email, is("UtT1Jtk6R3eE1q0i2DinVl/FbLaXR1jx"));
        assertThat("Check createTime", client.createTime, is("2023-01-17T08:30:43Z"));
        assertThat("Check countryCode", client.countryCode, is("3512"));
        assertThat("Check brand", client.brand, is("AlphaTick"));
        assertThat("Check birthday", client.birthday, is("2005-01-02"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get client not found(404)")
    @Owner(OWNER_NIKOLAI_KORIAGIN)
    @Feature(FEATURE_CLICKHOUSE_API_SERVICE)
    @Tag(TEAM_CORE)
    @AllureId("61")
    public void getClientNotFoundTest() throws IOException {
        Response response = getClient("AlphaTick-999-SVG");
        GetClientResponseError responseBody =
                objectMapper.readValue(response.body().string(), GetClientResponseError.class);
        assertThat("Assert that code is 404", response.code(), is(404));
        assertThat("Assert that code is 400", responseBody.status, is("404"));
        assertThat("Assert that code is 400", responseBody.error, is("Client not found."));
    }

    @Test
    @DisplayName("Clickhouse Api. Get client bad request(400)")
    @Owner(OWNER_NIKOLAI_KORIAGIN)
    @Feature(FEATURE_CLICKHOUSE_API_SERVICE)
    @Tag(TEAM_CORE)
    @AllureId("62")
    public void getClientBadRequestTest() throws IOException {
        Response response = getClient(1);
        GetClientResponseError responseBody =
                objectMapper.readValue(response.body().string(), GetClientResponseError.class);

        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert that code is 400", responseBody.status, is("400"));
        assertThat("Assert that code is 400", responseBody.error, is("Invalid clientId format"));
    }

    @Disabled
    @Test
    @DisplayName("Clickhouse Api. Get client internal error(500)")
    @Owner(OWNER_NIKOLAI_KORIAGIN)
    @Feature(FEATURE_CLICKHOUSE_API_SERVICE)
    @Tag(TEAM_CORE)
    @AllureId("63")
    public void getClientServerErrorTest() {
        Allure.step("Shut down service");
        Allure.step("Send getClient request");
        Allure.step("Check that there are 500 error in response");
    }
}
