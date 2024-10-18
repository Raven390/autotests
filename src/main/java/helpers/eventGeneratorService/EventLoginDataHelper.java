package helpers.eventGeneratorService;

import static utils.Utils.getRandomInt;

import helpers.kafka.crmDbEvents.eventGeneratorInbound.login.LoginDbEventData;
import helpers.kafka.crmDbEvents.eventGeneratorInbound.login.LoginDbEventMetadata;

public class EventLoginDataHelper {

    public static LoginDbEventData getLoginEventData() {
        LoginDbEventData eventData = new LoginDbEventData();
        // Prepare data object
        eventData.loginDatetime = "2024-10-02T10:34:00.058786Z";
        eventData.userId = getRandomInt();
        eventData.brand = "Vantage";
        eventData.ipAddress = "192.168.0.1";
        eventData.uaString = "123";
        eventData.cookie = "83a76a4c-6hce-4b9c-a39f-3s65d3c51775";
        return eventData;
    }

    public static LoginDbEventMetadata getLoginEventMetadata() {
        LoginDbEventMetadata eventMetadata = new LoginDbEventMetadata();
        // Prepare metadata object
        eventMetadata.timestamp = "2024-10-02T10:34:00.058786Z";
        eventMetadata.recordType = "data";
        eventMetadata.operation = "login";
        eventMetadata.partitionKeyType = "attribute-name";
        eventMetadata.schemaName = "dev_m_regulator_vfsc";
        eventMetadata.tableName = "tb_user_login_info";
        return eventMetadata;
    }
}
