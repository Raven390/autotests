package tests.event_generator_service_tests.crm_events.registration;

import static business_objects.kafka.crm_db_events.registration.RegistrationDbEventFactory.generateRegistrationDbEvent;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import helpers.kafka.MatchResultWithMessage;
import business_objects.kafka.crm_db_events.registration.RegistrationDbEvent;
import business_objects.kafka.crm_events.EgRegistrationEvent;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.TestBaseKafka;

@Feature(FEATURE_EVENT_GENERATOR_SERVICE)
@Story(STORY_EVENT_GENERATOR_SERVICE_REGISTRATION)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_EVENT_GENERATOR_SERVICE)
class EventGeneratorRegistrationTests extends TestBaseKafka {

    @Test
    @DisplayName("Generate registration event with event generator service")
    @AllureId("64")
    void generateRegistrationEventTest() throws JsonProcessingException, InterruptedException {

        RegistrationDbEvent registrationDbEvent = generateRegistrationDbEvent();

        Allure.step("Write message to crm-db-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(registrationDbEvent), KAFKA_TOPIC_CRM_DB_EVENTS);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        String consumedMessage = kafka.consumeMessage(KAFKA_TOPIC_CRM_EVENTS, registrationDbEvent.data.userId.toString());
        EgRegistrationEvent retrievedRegistrationEvent = objectMapper.readValue(consumedMessage, EgRegistrationEvent.class);

        EgRegistrationEvent expectedRegistrationEvent = new EgRegistrationEvent(
                registrationDbEvent.data.createTime, registrationDbEvent.data.userId, registrationDbEvent.data.brand, registrationDbEvent.data.regulator, registrationDbEvent.data.mtAccount, registrationDbEvent.data.createTime, "egRegistration", registrationDbEvent.data.createTime
        );

        Allure.step("Verify that message was written correctly");
        assertThat("Check id", retrievedRegistrationEvent.id, notNullValue());
        assertThat("Check createTime", retrievedRegistrationEvent.createTime, equalTo(expectedRegistrationEvent.createTime));
        assertThat("Check eventDate", retrievedRegistrationEvent.eventDate, equalTo(expectedRegistrationEvent.eventDate));
        assertThat("Check regulator", retrievedRegistrationEvent.regulator, equalTo(expectedRegistrationEvent.regulator));
        assertThat("Check metaTraderAccount", retrievedRegistrationEvent.metaTraderAccount, equalTo(expectedRegistrationEvent.metaTraderAccount));
        assertThat("Check initialEventTime", retrievedRegistrationEvent.initialEventTime, equalTo(expectedRegistrationEvent.initialEventTime));
        assertThat("Check clientId", retrievedRegistrationEvent.clientId, equalTo(expectedRegistrationEvent.clientId));
        assertThat("Check brand", retrievedRegistrationEvent.brand, equalTo(expectedRegistrationEvent.brand));
        assertThat("Check type", retrievedRegistrationEvent.type, equalTo(expectedRegistrationEvent.type));
    }

    @Test
    @DisplayName("Generate registration event two times for the same user")
    @AllureId("128")
    void verifyRegistrationEventIsFilteredOutTest() throws JsonProcessingException {

        RegistrationDbEvent registrationDbEvent1 = generateRegistrationDbEvent();
        RegistrationDbEvent registrationDbEvent2 = generateRegistrationDbEvent();
        registrationDbEvent2.data.userId = registrationDbEvent1.data.userId;
        registrationDbEvent2.metadata.timestamp = "2025-04-28T12:05:56.900Z";

        Allure.step("Write messages to crm-db-events topic");
        kafka.produceMessages(KAFKA_MESSAGE_KEY, KAFKA_TOPIC_CRM_DB_EVENTS, objectMapper.writeValueAsString(registrationDbEvent1));
        kafka.produceMessages(KAFKA_MESSAGE_KEY, KAFKA_TOPIC_CRM_DB_EVENTS, objectMapper.writeValueAsString(registrationDbEvent2));
        System.out.println(registrationDbEvent2.metadata.timestamp);
        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(KAFKA_TOPIC_CRM_EVENTS, registrationDbEvent2.metadata.timestamp);

        Allure.step("Verify that no matched results were found");
        assertThat("Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }
}
