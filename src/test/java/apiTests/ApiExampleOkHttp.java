package apiTests;

import api.payload.response.GetUser.GetUserResponse;
import org.junit.jupiter.api.Test;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;
import static config.ConfigFactory.UserConfig.BASE_API_URL;
import static config.ConfigFactory.UserConfig.PATH_GET_USER;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ApiExampleOkHttp extends BaseTestOkHttp{

    @Test
    public void getUserTest() throws IOException {
        Request request = new Request.Builder()
                .url(BASE_API_URL + PATH_GET_USER)
                .build();

        Response response = httpClient.newCall(request).execute();

        //Parse body from response
        assert response.body() != null;
        GetUserResponse responseBody = objectMapper.readValue(response.body().string(), GetUserResponse.class);

        //Print some data
        System.out.println(responseBody.data.id);
        System.out.println(response.code());

        //Hamcrest assertions
        assertThat("Check response code",response.code(), is(200));
        assertThat("Check user id",responseBody.data.id, is(2));
    }
}
