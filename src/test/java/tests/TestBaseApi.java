package tests;

import static helpers.database.DbHelper.startSshTunnel;
import static helpers.database.DbHelper.stopSshTunnel;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public class TestBaseApi {
    public static ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void setup() {
        startSshTunnel();
    }

    @AfterAll
    static void teardown() {
        stopSshTunnel();
    }
}
