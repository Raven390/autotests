package helpers.database;

import static helpers.database.DbHelper.getObjectsFromDB;
import static helpers.database.DbName.POSTGRES;
import static utils.Constants.*;
import static utils.Utils.writeLog;

import business_objects.db.payment_gate.payment_decisions.PaymentDecisionsObject;
import business_objects.db.payment_gate.payment_decisions_sent.PaymentDecisionsSentObject;
import business_objects.db.payment_gate.payment_details.PaymentDetailsObject;
import business_objects.db.payment_gate.payment_events.PaymentEventsObject;
import business_objects.db.payment_gate.payment_rule_executions.PaymentRuleExecutionsObject;
import java.util.List;
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

    public static PaymentRuleExecutionsObject getPaymentRuleExecution(String paymentId, String ruleId)
            throws Exception {
        List<PaymentRuleExecutionsObject> objects = getObjectsFromDB(
                POSTGRES,
                PAYMENT_GATEWAY_PAYMENT_RULE_EXECUTIONS_TABLE,
                String.format("payment_id='%s'", paymentId) + String.format(" and rule_id ='%s'", ruleId),
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
                "ucid='%s'".replace("%s", ucid),
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
}
