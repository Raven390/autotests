package tests.utilities_api_tests;

import static business_objects.api.utilities_api.decrypt.GetDecryptRequest.getDecryptRequest;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.nullValue;
import static utils.Constants.*;
import static utils.Constants.LAYER_API;

import business_objects.api.clickhouse_api_service.ClickhouseApiErrorResponse;
import business_objects.api.utilities_api.decrypt.GetDecryptResponse;
import helpers.data.ClientHelper;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import tests.TestBaseApi;

@Feature(FEATURE_UTILITIES_API_SERVICE)
@Story(STORY_UTILITIES_API_GET_DECRYPT)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_UTILITIES_API_SERVICE)
class GetDecryptTests extends TestBaseApi {

    private static ClientHelper client1 = getRandomVantageClientAllFields();
    private String emailEncrypted = "paRP/scRJ89KbGWkZOrJVF/FbLaXR1jx";
    private String phoneEncrypted = "OlIoGyRiWyMgmlKyQZkW6w==";

    private String emailDecrypted = "test14@example.com";
    private String phoneDecrypted = "+1810347493";

    @BeforeAll
    static void setup() {}

    @AfterAll
    static void teardown() throws Exception {}

    @Test
    @DisplayName("Clickhouse Api. Get email/phone decrypt success (200)")
    void getDecryptTest1() throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("brand", client1.getBrand()); // Required
        params.put("email", emailEncrypted); // Required
        params.put("phoneNum", phoneEncrypted); // Required
        Response response = getDecryptRequest(params);

        assertThat(response.body(), is(notNullValue()));
        GetDecryptResponse mappedResponse =
                objectMapper.readValue(response.body().string(), GetDecryptResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert email", mappedResponse.getEmailDecrypt(), is(emailDecrypted));
        assertThat("Assert phone", mappedResponse.getPhoneNumDecrypt(), is(phoneDecrypted));
    }

    @Test
    @DisplayName("Clickhouse Api. Get email decrypt success (200)")
    void getDecryptTest2() throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("brand", client1.getBrand()); // Required
        params.put("email", emailEncrypted); // Required
        Response response = getDecryptRequest(params);

        assertThat(response.body(), is(notNullValue()));
        GetDecryptResponse mappedResponse =
                objectMapper.readValue(response.body().string(), GetDecryptResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert email", mappedResponse.getEmailDecrypt(), is(emailDecrypted));
        assertThat("Assert phone", mappedResponse.getPhoneNumDecrypt(), is(nullValue()));
    }

    @Test
    @DisplayName("Clickhouse Api. Get phone decrypt success (200)")
    void getDecryptTest3() throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("brand", client1.getBrand()); // Required
        params.put("phoneNum", phoneEncrypted); // Required
        Response response = getDecryptRequest(params);

        assertThat(response.body(), is(notNullValue()));
        GetDecryptResponse mappedResponse =
                objectMapper.readValue(response.body().string(), GetDecryptResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert email", mappedResponse.getEmailDecrypt(), is(nullValue()));
        assertThat("Assert phone", mappedResponse.getPhoneNumDecrypt(), is(phoneDecrypted));
    }

    @Test
    @DisplayName("Clickhouse Api. Get no email/phone decrypt success (200)")
    void getDecryptTest4() throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("brand", client1.getBrand()); // Required
        Response response = getDecryptRequest(params);

        assertThat(response.body(), is(notNullValue()));
        GetDecryptResponse mappedResponse =
                objectMapper.readValue(response.body().string(), GetDecryptResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert email", mappedResponse.getEmailDecrypt(), is(nullValue()));
        assertThat("Assert phone", mappedResponse.getPhoneNumDecrypt(), is(nullValue()));
    }

    @Test
    @DisplayName("Clickhouse Api. Get decrypt fail no brand passed (400)")
    void getDecryptTest5() throws IOException {
        Map<String, Object> params = new HashMap<>();
        Response response = getDecryptRequest(params);

        assertThat(response.body(), is(notNullValue()));
        ClickhouseApiErrorResponse mappedResponse =
                objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat(
                "Assert email",
                mappedResponse.getError(),
                is("Required request parameter 'brand' for method parameter type String is not present"));
        assertThat("Assert phone", mappedResponse.getStatus(), is(400));
    }
}
