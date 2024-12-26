package tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.kafka.KafkaHelper;
import org.junit.jupiter.api.extension.ExtendWith;
import utils.TestResultWatcher;


@ExtendWith(TestResultWatcher.class)
public class TestBaseRule {
    public static KafkaHelper kafka = new KafkaHelper();
    public static ObjectMapper objectMapper = new ObjectMapper();
}
