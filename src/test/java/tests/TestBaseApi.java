package tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import static helpers.database.DbHelper.startSshTunnel;
import static helpers.database.DbHelper.stopSshTunnel;

public class TestBaseApi {
    public static ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    public static void setup() {
        startSshTunnel();
    }

    @AfterAll
    public static void teardown() {
        stopSshTunnel();
    }
}
