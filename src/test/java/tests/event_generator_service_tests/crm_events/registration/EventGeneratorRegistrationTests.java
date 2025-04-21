package tests.event_generator_service_tests.crm_events.registration;

import static business_objects.kafka.crm_db_events.registration.RegistrationDbEventFactory.generateRegistrationDbEvent;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.kafka.KafkaHelper;
import helpers.kafka.MatchResultWithMessage;
import business_objects.kafka.crm_db_events.registration.RegistrationDbEvent;
import business_objects.kafka.crm_events.RegistrationEvent;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Feature(FEATURE_EVENT_GENERATOR_SERVICE)
@Story(STORY_EVENT_GENERATOR_SERVICE_REGISTRATION)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_EVENT_GENERATOR_SERVICE)
@Disabled
@Tag(TAG_MANUAL)
class EventGeneratorRegistrationTests {

    KafkaHelper kafka = new KafkaHelper();
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Generate registration event with event generator service")
    @AllureId("64")
    void generateRegistrationEventTest() throws JsonProcessingException, InterruptedException {

        RegistrationDbEvent registrationDbEvent = generateRegistrationDbEvent();

        Allure.step("Write message to crm-db-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(registrationDbEvent), KAFKA_TOPIC_CRM_DB_EVENTS);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        String consumedMessage = kafka.consumeMessage(KAFKA_TOPIC_CRM_EVENTS, registrationDbEvent.data.userId.toString());
        RegistrationEvent retrievedRegistrationEvent = objectMapper.readValue(consumedMessage, RegistrationEvent.class);

        RegistrationEvent expectedRegistrationEvent = new RegistrationEvent(
                registrationDbEvent.data.createTime, registrationDbEvent.data.userId, registrationDbEvent.data.brand, registrationDbEvent.data.regulator, registrationDbEvent.data.mtAccount, "clientRegistration"
        );

        Allure.step("Verify that message was written correctly");
        assertThat("Check id", retrievedRegistrationEvent.id, notNullValue());
        assertThat("Check all fields except id", retrievedRegistrationEvent, equalTo(expectedRegistrationEvent));
    }

    @Test
    @DisplayName("Generate registration event two times for the same user")
    @AllureId("128")
    void verifyRegistrationEventIsFilteredOutTest() throws JsonProcessingException {

        RegistrationDbEvent registrationDbEvent1 = generateRegistrationDbEvent();
        RegistrationDbEvent registrationDbEvent2 = generateRegistrationDbEvent();
        registrationDbEvent2.data.userId = registrationDbEvent1.data.userId;

        Allure.step("Write messages to crm-db-events topic");
        kafka.produceMessages(KAFKA_MESSAGE_KEY, KAFKA_TOPIC_CRM_DB_EVENTS, objectMapper.writeValueAsString(registrationDbEvent1), objectMapper.writeValueAsString(registrationDbEvent2));

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_CRM_EVENTS, registrationDbEvent2.data.createTime);

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }
}
