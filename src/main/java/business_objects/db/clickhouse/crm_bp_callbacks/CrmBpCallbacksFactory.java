package business_objects.db.clickhouse.crm_bp_callbacks;

import helpers.data.ClientHelper;

import java.time.OffsetDateTime;

import static utils.Utils.getRandomCardMaskedNumber;
import static utils.Utils.getRandomIntPositive;

public class CrmBpCallbacksFactory {

    public static CrmBpCallbacksObject generateCrmBpCallbacksObject(ClientHelper client) {
        return CrmBpCallbacksObject.builder().userId(client.getUserId().longValue()).ucid(client.getUcid()).brand(client.getBrand()).regulator(client.getRegulator()).businessOrderId(getRandomIntPositive().toString()).orderNumber(getRandomIntPositive().toString()).paymentProfileKey(getRandomCardMaskedNumber()).status("approved").isDeclined(null).isFraudDeclined(null).type("callback_deposit").createTime(OffsetDateTime.now()).kafkaTimestamp(OffsetDateTime.now()).lastUpdated(OffsetDateTime.now()).chInsertTs(OffsetDateTime.now()).build();
    }
}
