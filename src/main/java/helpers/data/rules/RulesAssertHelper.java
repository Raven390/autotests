package helpers.data.rules;

import businessObjects.db.mitigationServiceDb.ClientsRestriction;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.data.ClientHelper;
import helpers.database.DbName;
import helpers.kafka.KafkaHelper;
import io.qameta.allure.Step;

import java.util.List;

import static helpers.database.DbHelper.getObjectsFromDB;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static utils.Constants.KAFKA_TOPIC_ALERTS;
import static utils.Constants.MITIGATION_CLIENTS_RESTRICTION;

public class RulesAssertHelper {

    public static KafkaHelper kafka = new KafkaHelper();
    public static ObjectMapper objectMapper = new ObjectMapper();

    @Step("Assert that there are no alerts by client")
    public static void asserAlertsAmountByClient(ClientHelper client, int alertsNumber) throws InterruptedException {

        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, client.getUcid());
        assertThat("Verify that there is only 1 alert", consumedMessages.size(), equalTo(alertsNumber));
    }

    @Step("Assert that there are no restrictions by client")
    public static void assertRestrictionsAmountByClient(ClientHelper client, int restrictionsAmount) throws Exception {
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, String.format("ucid = '%s'", client.getUcid()), ClientsRestriction.class);
        assertThat(String.format("Check that there are no restrictions for ucid %s", client.getUcid()), clientsRestrictions, empty());
    }
}
