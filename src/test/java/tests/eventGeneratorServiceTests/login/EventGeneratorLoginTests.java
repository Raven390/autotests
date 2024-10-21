package tests.eventGeneratorServiceTests.login;

import static helpers.eventGeneratorService.EventLoginDataHelper.getLoginEventData;
import static helpers.eventGeneratorService.EventLoginDataHelper.getLoginEventMetadata;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static utils.Constants.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.kafka.KafkaHelper;
import helpers.kafka.crmDbEvents.eventGeneratorInbound.login.LoginDbEvent;
import helpers.kafka.crmDbEvents.eventGeneratorInbound.login.LoginDbEventData;
import helpers.kafka.crmDbEvents.eventGeneratorInbound.login.LoginDbEventMetadata;
import helpers.kafka.crmEvents.eventGeneratorOutboundEvents.LoginEvent;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class EventGeneratorLoginTests {
    KafkaHelper kafka = new KafkaHelper();
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Generate login event with event generator service")
    @Feature(FEATURE_EVENT_GENERATOR_SERVICE)
    @Story(FEATURE_EVENT_GENERATOR_SERVICE_LOGIN)
    @Owner(OWNER_NIKOLAI_KORIAGIN)
    @Tag(TEAM_CORE)
    @Tag(LAYER_API)
    @AllureId("65")
    public void generateLoginEventTest() throws JsonProcessingException, InterruptedException {

        Allure.step("Test data preparation");
        LoginDbEventData data = getLoginEventData();
        LoginDbEventMetadata metadata = getLoginEventMetadata();

        Allure.step("Write message to crm-db-events topic");
        LoginDbEvent crmDbEvent = LoginDbEvent.getLoginDbEvent(data, metadata);
        kafka.produceMessage("13", objectMapper.writeValueAsString(crmDbEvent), KAFKA_TOPIC_CRM_DB_EVENTS);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        String consumedMessage = kafka.consumeMessages(KAFKA_TOPIC_CRM_EVENTS, String.valueOf(data.userId));
        LoginEvent loginCrmEvent = objectMapper.readValue(consumedMessage, LoginEvent.class);
        Allure.step("Verify that message was written correctly");
        assertThat("Check UUID", loginCrmEvent.uuid, notNullValue());
        assertThat("Check loginTime", loginCrmEvent.login_time, equalTo(data.loginDatetime));
        assertThat("Check user_id", loginCrmEvent.user_id, equalTo(data.userId));
        assertThat("Check brand", loginCrmEvent.brand, equalTo(data.brand));
        assertThat("Check ip_address", loginCrmEvent.ip_address, equalTo(data.ipAddress));
        assertThat("Check cid", loginCrmEvent.cid, equalTo(data.uaString));
        assertThat("Check cookie", loginCrmEvent.cookie, equalTo(data.cookie));
        assertThat("Check type", loginCrmEvent.type, equalTo("websiteLogin"));
    }
}
