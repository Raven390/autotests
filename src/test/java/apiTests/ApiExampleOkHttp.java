package apiTests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static utils.ConfigFactory.BASE_API_URL;
import static utils.ConfigFactory.PATH_GET_USER;
import static utils.Constants.*;

import helpers.rest.models.GetUserResponse;
import io.qameta.allure.AllureId;
import io.qameta.allure.Owner;
import java.io.IOException;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class ApiExampleOkHttp extends BaseTestOkHttp {

    @Disabled
    @Test
    @AllureId("38")
    @Owner(OWNER_NIKOLAI_KORIAGIN)
    @Tag(TEAM_CORE)
    @Tag(STATUS_AUTOMATED)
    @Tag(LAYER_API)
    @DisplayName("DEMO TEST")
    public void getUserTest() throws IOException {
        Request request =
                new Request.Builder().url(BASE_API_URL + PATH_GET_USER).build();

        Response response = httpClient.newCall(request).execute();

        // Parse body from response
        assert response.body() != null;
        GetUserResponse responseBody = objectMapper.readValue(response.body().string(), GetUserResponse.class);

        // Print some data
        System.out.println(responseBody.data.id);
        System.out.println(response.code());

        // Hamcrest assertions
        assertThat("Check response code", response.code(), is(200));
        assertThat("Check user id", responseBody.data.id, is(2));
    }
}
