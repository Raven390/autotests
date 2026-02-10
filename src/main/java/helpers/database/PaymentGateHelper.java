package helpers.database;

import static business_objects.db.payment_gate.payment_events.PaymentEventsObjectFactory.generatePaymentEventsObject;
import static helpers.database.DbHelper.getObjectsFromDB;
import static helpers.database.DbName.POSTGRES;
import static utils.Constants.*;
import static utils.Utils.*;

import business_objects.db.payment_gate.payment_decisions.PaymentDecisionsObject;
import business_objects.db.payment_gate.payment_decisions.PaymentDecisionsObjectFactory;
import business_objects.db.payment_gate.payment_decisions_sent.PaymentDecisionsSentObject;
import business_objects.db.payment_gate.payment_details.PaymentDetailsObject;
import business_objects.db.payment_gate.payment_details.PaymentDetailsObjectFactory;
import business_objects.db.payment_gate.payment_events.PaymentEventsObject;
import business_objects.db.payment_gate.payment_rule_executions.PaymentRuleExecutionsObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.data.ClientHelper;
import helpers.data.PaymentGateData;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PaymentGateHelper {

    public static PaymentRuleExecutionsObject getPaymentRuleExecution(Integer id) throws Exception {
        List<PaymentRuleExecutionsObject> objects = getObjectsFromDB(
                POSTGRES,
                PAYMENT_GATEWAY_PAYMENT_RULE_EXECUTIONS_TABLE,
                "id='%s'".replace("%s", id.toString()),
                PaymentRuleExecutionsObject.class,
                60);
        writeLog(objects);
        if (objects.isEmpty()) {
            return null;
        } else {
            return objects.getFirst();
        }
    }

    public static PaymentRuleExecutionsObject getPaymentRuleExecution(String paymentId) throws Exception {
        List<PaymentRuleExecutionsObject> objects = getObjectsFromDB(
                POSTGRES,
                PAYMENT_GATEWAY_PAYMENT_RULE_EXECUTIONS_TABLE,
                "payment_id='%s'".replace("%s", paymentId),
                PaymentRuleExecutionsObject.class,
                60);
        writeLog(objects);
        if (objects.isEmpty()) {
            return null;
        } else {
            return objects.getFirst();
        }
    }

    @Deprecated
    public static PaymentRuleExecutionsObject getPaymentRuleExecution(String paymentId, String ruleId)
            throws Exception {
        List<PaymentRuleExecutionsObject> objects = getObjectsFromDB(
                POSTGRES,
                PAYMENT_GATEWAY_PAYMENT_RULE_EXECUTIONS_TABLE,
                String.format("payment_id='%s'", paymentId) + String.format(" and rule_id ='%s'", ruleId)
                        + "ORDER BY date_updated DESC",
                PaymentRuleExecutionsObject.class,
                60);
        writeLog(objects);
        if (objects.isEmpty()) {
            return null;
        } else {
            return objects.getFirst();
        }
    }

    public static PaymentRuleExecutionsObject getPaymentRuleExecution(String paymentId, int ruleId) throws Exception {
        List<PaymentRuleExecutionsObject> objects = getObjectsFromDB(
                POSTGRES,
                PAYMENT_GATEWAY_PAYMENT_RULE_EXECUTIONS_TABLE,
                String.format("payment_id='%s'", paymentId) + String.format(" and rule_id ='%s'", ruleId)
                        + "ORDER BY date_updated DESC",
                PaymentRuleExecutionsObject.class,
                60);
        writeLog(objects);
        if (objects.isEmpty()) {
            return null;
        } else {
            return objects.getFirst();
        }
    }

    public static PaymentEventsObject getPaymentEvent(String ucid) throws Exception {
        List<PaymentEventsObject> objects = getObjectsFromDB(
                POSTGRES,
                PAYMENT_GATEWAY_PAYMENT_EVENTS_TABLE,
                "ucid='%s'".replace("%s", ucid) + " ORDER BY date_updated DESC",
                PaymentEventsObject.class,
                60);
        writeLog(objects);
        if (objects.isEmpty()) {
            return null;
        } else {
            return objects.getFirst();
        }
    }

    public static PaymentEventsObject getPaymentEvent(UUID paymentId) throws Exception {
        List<PaymentEventsObject> objects = getObjectsFromDB(
                POSTGRES,
                PAYMENT_GATEWAY_PAYMENT_EVENTS_TABLE,
                "payment_id='%s'".replace("%s", paymentId.toString()),
                PaymentEventsObject.class,
                60);
        writeLog(objects);
        if (objects.isEmpty()) {
            return null;
        } else {
            return objects.getFirst();
        }
    }

    public static PaymentEventsObject getFailedPaymentEventByCrmId(String crmId) throws Exception {
        List<PaymentEventsObject> objects = getObjectsFromDB(
                POSTGRES,
                PAYMENT_GATEWAY_PAYMENT_EVENTS_TABLE,
                "crm_id='%s' AND delivery_status='FAILED'".replace("%s", crmId),
                PaymentEventsObject.class,
                60);
        writeLog(objects);
        if (objects.isEmpty()) {
            return null;
        } else {
            return objects.getFirst();
        }
    }

    public static PaymentDetailsObject getPaymentDetails(Integer clientId) throws Exception {
        List<PaymentDetailsObject> objects = getObjectsFromDB(
                POSTGRES,
                PAYMENT_GATEWAY_PAYMENT_DETAILS_TABLE,
                "client_id='%s'".replace("%s", clientId.toString()),
                PaymentDetailsObject.class,
                60);
        writeLog(objects);
        if (objects.isEmpty()) {
            return null;
        } else {
            return objects.getFirst();
        }
    }

    public static PaymentDetailsObject getPaymentDetails(UUID paymentId) throws Exception {
        List<PaymentDetailsObject> objects = getObjectsFromDB(
                POSTGRES,
                PAYMENT_GATEWAY_PAYMENT_DETAILS_TABLE,
                "payment_id='%s'".replace("%s", paymentId.toString()),
                PaymentDetailsObject.class,
                60);
        writeLog(objects);
        if (objects.isEmpty()) {
            return null;
        } else {
            return objects.getFirst();
        }
    }

    public static List<PaymentDecisionsObject> getPaymentDecisionsByPaymentId(UUID paymentId) throws Exception {
        List<PaymentDecisionsObject> objects = getObjectsFromDB(
                POSTGRES,
                PAYMENT_GATEWAY_PAYMENT_DECISIONS_TABLE,
                "payment_id='%s'".replace("%s", paymentId.toString()),
                PaymentDecisionsObject.class,
                60);
        writeLog(objects);
        if (objects.isEmpty()) {
            return null;
        } else {
            return objects;
        }
    }

    public static PaymentDecisionsSentObject getPaymentDecisionSent(UUID paymentId) throws Exception {
        List<PaymentDecisionsSentObject> objects = getObjectsFromDB(
                POSTGRES,
                PAYMENT_GATEWAY_PAYMENT_DECISIONS_SENT_TABLE,
                "payment_id='%s'".replace("%s", paymentId.toString()),
                PaymentDecisionsSentObject.class,
                60);
        writeLog(objects);
        if (objects.isEmpty()) {
            return null;
        } else {
            return objects.getFirst();
        }
    }

    public static PaymentGateData generatePaymentWithdrawalPaymentGateData(ClientHelper client)
            throws JsonProcessingException {
        PaymentGateData paymentGateData = new PaymentGateData();
        paymentGateData.setPaymentEvent(generatePaymentEventsObject(client));
        PaymentDetailsObject details =
                PaymentDetailsObjectFactory.generatePaymentDetailsObject(paymentGateData.getPaymentEvent(), client);
        details.setType("withdrawal");
        Map<String, Object> payload = Map.ofEntries(
                Map.entry("id", getRandomUuidString()),
                Map.entry("iban", ""),
                Map.entry("type", "withdrawal"),
                Map.entry("brand", client.getBrand()),
                Map.entry("bankName", "testBankName"),
                Map.entry("clientId", client.getUserId()),
                Map.entry("platform", "WEB"),
                Map.entry("checkName", "Little_Amount"),
                Map.entry("eventDate", "2025-09-18T06:15:50+03:00"),
                Map.entry("regulator", client.getRegulator()),
                Map.entry("mt4Account", client.getTradingAccount()),
                Map.entry("accountType", "MT4"),
                Map.entry("withdrawalId", getRandomIntPositive()),
                Map.entry("schemaVersion", "1.0"),
                Map.entry("merchantOrderId", "VU856068920250918061547"),
                Map.entry("paymentTypeName", "Bank Transfers"),
                Map.entry("withdrawalAmount", 100),
                Map.entry("paymentMethodCode", "Brazil Bank Transfer"),
                Map.entry("paymentChannelCode", "642"),
                Map.entry("paymentChannelName", "Brazil-CPS"),
                Map.entry("withdrawalCurrency", "EUR"),
                Map.entry("withdrawalAmountUSD", 105),
                Map.entry("bankAccountHolderName", "testBankAccountHolderName"),
                Map.entry("withdrawalApplicationTime", "2025-09-18T06:15:46.379Z"));
        ObjectMapper objectMapper = new ObjectMapper();
        String payloadString = objectMapper.writeValueAsString(payload);
        details.setPayload(payloadString);
        paymentGateData.setPaymentDetails(details);
        PaymentDecisionsObject decision =
                PaymentDecisionsObjectFactory.generatePaymentDecisionObject(paymentGateData.getPaymentEvent());
        decision.setDecisionType("payment");
        decision.setDecisionCode(0);
        paymentGateData.setPaymentDecisions(decision);
        return paymentGateData;
    }

    public static PaymentGateData generateTradingWithdrawalPaymentGateData(ClientHelper client)
            throws JsonProcessingException {
        PaymentGateData paymentGateData = generatePaymentWithdrawalPaymentGateData(client);
        paymentGateData.getPaymentDecisions().setDecisionType("risk");
        return paymentGateData;
    }
}
