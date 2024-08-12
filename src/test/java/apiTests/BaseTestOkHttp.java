package apiTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.BeforeAll;

public class BaseTestOkHttp {

    final OkHttpClient httpClient = new OkHttpClient();
    final ObjectMapper objectMapper = new ObjectMapper();


    @BeforeAll
    public static void setUp() {
        //setup
    }
}