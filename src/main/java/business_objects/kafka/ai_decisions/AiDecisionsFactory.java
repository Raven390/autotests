package business_objects.kafka.ai_decisions;

import static helpers.data.enums.FraudType.HEDGING;

import business_objects.kafka.alerts.*;
import helpers.data.ClientHelper;
import io.qameta.allure.Step;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public class AiDecisionsFactory {

    private AiDecisionsFactory() {}

    @Step("Generate ai decisions for client with alert id and classifications")
    public static AiDecisions generateAiDecisionsByClientAlertIdClassifications(
            ClientHelper client, UUID alertId, List<AiDecisions.Classification> classifications) {
        return AiDecisions.builder()
                .messageId(UUID.randomUUID())
                .timestamp(OffsetDateTime.now())
                .producedAtUtc(OffsetDateTime.now())
                .alertId(alertId)
                .alertDate(OffsetDateTime.now())
                .eventDate(OffsetDateTime.now())
                .alertType("WITHDRAW_ALERT")
                .ucid(client.getUcid())
                .tradingAccount(client.getTradingAccount().longValue())
                .serverId(client.getServerId())
                .rule("Withdrawal review")
                .fraudType(HEDGING.getCode())
                .classifications(classifications)
                .build();
    }
}
