package uiTests.backoffice;

import static utils.Constants.*;

import helpers.kafka.BackOfficeKafkaMessageProducerHelperAlertEvent;
import io.qameta.allure.AllureId;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import uiTests.TestBaseE2E;

public class KafkaPrototype extends TestBaseE2E {

    @Test
    @Tag("kafka")
    @DisplayName("kafka")
    @Owner(OWNER_DMITRI_KALACHEV)
    @Tag(TEAM_BACKOFFICE)
    @Tag(STATUS_AUTOMATED)
    @Tag(FEATURE_EXAMPLE)
    @Tag(LAYER_WEB)
    @AllureId("")
    void request() {
        BackOfficeKafkaMessageProducerHelperAlertEvent alertEvent =
                new BackOfficeKafkaMessageProducerHelperAlertEvent();
        alertEvent.produceMessage("13", "rrr", "alert");
    }
}
