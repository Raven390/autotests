package tests;

import api.payload.response.GetUser.GetUserResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static config.ConfigFactory.UserConfig.BASE_API_URL;
import static config.ConfigFactory.UserConfig.PATH_GET_USER;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ApiExampleOkHttp extends BaseTestOkHttp{

    @Test
    public void getUserTest() throws IOException {
        //Get response
        final OkHttpClient client = new OkHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();
        String path = BASE_API_URL + PATH_GET_USER;

        Request request = new Request.Builder()
                .url(path)
                .build();

        Response response = client.newCall(request).execute();

        //Parse body from response
        assert response.body() != null;
        GetUserResponse responseBody = objectMapper.readValue(response.body().string(), GetUserResponse.class);

        //Print some data
        System.out.println(responseBody.data.id);
        System.out.println(response.code());

        //Assertions
        assertThat(response.code(), is(200));
        assertThat(responseBody.data.id, is(2));
    }
}
