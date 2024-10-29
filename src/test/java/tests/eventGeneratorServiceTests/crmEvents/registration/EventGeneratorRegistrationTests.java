package tests.eventGeneratorServiceTests.crmEvents.registration;

import static businessObjects.kafka.crmDbEvents.registration.RegistrationDbEventFactory.generateRegistrationDbEvent;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.kafka.KafkaHelper;
import helpers.kafka.MatchResultWithMessage;
import businessObjects.kafka.crmDbEvents.registration.RegistrationDbEvent;
import businessObjects.kafka.crmEvents.RegistrationEvent;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Feature(FEATURE_EVENT_GENERATOR_SERVICE)
@Story(STORY_EVENT_GENERATOR_SERVICE_REGISTRATION)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
public class EventGeneratorRegistrationTests {

    @Test
    @DisplayName("Generate registration event with event generator service")
    @AllureId("64")
    public void generateRegistrationEventTest() throws JsonProcessingException, InterruptedException {
        KafkaHelper kafka = new KafkaHelper();
        ObjectMapper objectMapper = new ObjectMapper();

        RegistrationDbEvent registrationDbEvent = generateRegistrationDbEvent();

        Allure.step("Write message to crm-db-events topic");
        kafka.produceMessage("13", objectMapper.writeValueAsString(registrationDbEvent), KAFKA_TOPIC_CRM_DB_EVENTS);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        String consumedMessage = kafka.consumeMessage(KAFKA_TOPIC_CRM_EVENTS, registrationDbEvent.data.userId.toString());
        RegistrationEvent retrievedRegistrationEvent = objectMapper.readValue(consumedMessage, RegistrationEvent.class);

        RegistrationEvent expectedRegistrationEvent = new RegistrationEvent(
                registrationDbEvent.data.createTime,
                registrationDbEvent.data.userId,
                registrationDbEvent.data.brand,
                registrationDbEvent.data.regulator,
                registrationDbEvent.data.mtAccount,
                "clientRegistration"
        );

        Allure.step("Verify that message was written correctly");
        assertThat("Check id", retrievedRegistrationEvent.id, notNullValue());
        assertThat("Check all fields except id", retrievedRegistrationEvent, equalTo(expectedRegistrationEvent));
    }

    @Test
    @DisplayName("Generate registration event two times for the same user")
    @AllureId("128")
    public void verifyRegistrationEventIsFilteredOutTest() throws JsonProcessingException, InterruptedException {
        KafkaHelper kafka = new KafkaHelper();
        ObjectMapper objectMapper = new ObjectMapper();

        RegistrationDbEvent registrationDbEvent1 = generateRegistrationDbEvent();
        RegistrationDbEvent registrationDbEvent2 = generateRegistrationDbEvent();
        registrationDbEvent2.data.userId = registrationDbEvent1.data.userId;

        Allure.step("Write messages to crm-db-events topic");
        kafka.produceMessages("13", KAFKA_TOPIC_CRM_DB_EVENTS, objectMapper.writeValueAsString(registrationDbEvent1), objectMapper.writeValueAsString(registrationDbEvent2));

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_CRM_EVENTS, registrationDbEvent2.data.createTime);

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }
}
