package tests.apiTests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static utils.Constants.*;

import io.qameta.allure.Owner;
import java.io.IOException;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.Test;
import tests.TestBaseApi;

public class TestCheckApi extends TestBaseApi {

    @Test
    @Owner(TAG_BUILD_CHECK)
    public void getUserTest() throws IOException {
        Response response = httpClient
                .newCall(new Request.Builder()
                        .url("https://reqres.in/api/users/2")
                        .build())
                .execute();
        assertThat("Check response code", response.code(), is(200));
    }
}
