package helpers.database;

import static helpers.database.DbHelper.insertObjectsToDb; // Укажите ваши реальные методы
import static helpers.database.DbName.POSTGRES;

import business_objects.db.backoffice_db.ai_results.AiAlertResult;
import business_objects.db.backoffice_db.ai_results.AiAlertResultClassification;
import business_objects.kafka.alerts.RuleAlert;
import helpers.data.ClientHelper;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class AiDecisionHelper {

    public static void createAiDecision(ClientHelper client, RuleAlert alert, String category, boolean trust)
            throws Exception {
        Timestamp now = Timestamp.from(Instant.now());

        AiAlertResult result = new AiAlertResult();
        result.setMessageId(UUID.randomUUID());
        result.setMessageTimestamp(now);
        result.setProducedAt(now);
        result.setAlertId(UUID.fromString(alert.alertId));
        result.setAlertAiType("TRADING"); // Или другой нужный тип
        result.setUcid(client.getUcid());
        result.setAccount(String.valueOf(client.getTradingAccount()));
        result.setServerId("1");
        result.setRuleName(alert.rule.name);
        result.setFraudType(alert.rule.fraudType);
        result.setCreatedAt(now);
        result.setTrust(trust);
        result.setSendAuditStatus("SENT");

        insertObjectsToDb(POSTGRES, "bo.ai_alert_result", List.of(result));

        Long resultId = DbHelper.getObjectsFromDB(
                        POSTGRES, "bo.ai_alert_result", "alert_id = '" + alert.alertId + "'", AiAlertResult.class)
                .getFirst()
                .getId();

        AiAlertResultClassification classification = new AiAlertResultClassification();
        classification.setAiAlertResultId(resultId);
        classification.setCategory(category);
        classification.setConfidenceScore(95);
        classification.setCreatedAt(now);

        insertObjectsToDb(POSTGRES, "bo.ai_alert_result_classification", List.of(classification));
    }

    public static void ageAlertsToBypassCoolingPeriod(List<String> alertIds) throws Exception {
        if (alertIds == null || alertIds.isEmpty()) {
            return;
        }

        String inClause = String.join("','", alertIds);
        String query = String.format(
                "UPDATE bo.alert SET received_at = NOW() - INTERVAL '4 minutes' WHERE uuid IN ('%s')", inClause);

        DbHelper.executeQueryToDb(POSTGRES, query);
    }
}
