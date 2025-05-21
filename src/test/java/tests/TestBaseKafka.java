package tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.kafka.KafkaHelper;


public class TestBaseKafka {
    public static KafkaHelper kafka = new KafkaHelper();
    public static ObjectMapper objectMapper = new ObjectMapper();
}
