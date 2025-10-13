package tests.event_generator_service_tests.crm_events.login;

import static business_objects.kafka.crm_db_events.login.LoginDbEventFactory.generateLoginDbEvent;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import business_objects.kafka.crm_db_events.login.LoginDbEvent;
import business_objects.kafka.crm_events.EgLoginEvent;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.TestBaseKafka;

@Feature(FEATURE_EVENT_GENERATOR_SERVICE)
@Story(STORY_EVENT_GENERATOR_SERVICE_LOGIN)
@Tag(LAYER_API)
@Tag(TEAM_CORE)
@Tag(SUITE_EVENT_GENERATOR_SERVICE)
class EventGeneratorLoginTests extends TestBaseKafka {

    @Test
    @DisplayName("Generate login event with event generator service")
    @AllureId("65")
    void generateLoginEventTest() throws JsonProcessingException, InterruptedException {

        LoginDbEvent loginDbEvent = generateLoginDbEvent();

        Allure.step("Write message to crm-db-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(loginDbEvent), KAFKA_TOPIC_CRM_DB_EVENTS);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        String consumedMessage = kafka.consumeMessage(KAFKA_TOPIC_CRM_EVENTS, loginDbEvent.data.userId.toString());
        EgLoginEvent retrievedLoginEvent = objectMapper.readValue(consumedMessage, EgLoginEvent.class);

        EgLoginEvent expectedLoginEvent = new EgLoginEvent(
                loginDbEvent.data.loginDatetime, loginDbEvent.data.userId, loginDbEvent.data.brand, loginDbEvent.data.ipAddress, loginDbEvent.data.uaString, loginDbEvent.data.cookie, "websiteLogin", EG_LOGIN_EVENT
        );

        Allure.step("Verify that message was written correctly");
        assertThat("Check type", retrievedLoginEvent.type, is(EG_LOGIN_EVENT));
        assertThat("Check id", retrievedLoginEvent.id, notNullValue());
        assertThat("Check all fields except id", retrievedLoginEvent, equalTo(expectedLoginEvent));
    }
}
