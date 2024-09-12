package apiTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.BeforeAll;

public class TestBaseApi {
    public static OkHttpClient httpClient = new OkHttpClient();
    public static ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    public static void setUp() {
        // setup
    }
}
