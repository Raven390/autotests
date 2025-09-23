package helpers.database;

import business_objects.db.payment_gate.payment_decisions.PaymentDecisionsObject;
import business_objects.db.payment_gate.payment_details.PaymentDetailsObject;
import business_objects.db.payment_gate.payment_events.PaymentEventsObject;
import business_objects.db.payment_gate.payment_rule_executions.PaymentRuleExecutionsObject;

import java.util.List;
import java.util.UUID;

import static helpers.database.DbHelper.getObjectsFromDB;
import static utils.Constants.*;

public class PaymentGateHelper {

    public static PaymentRuleExecutionsObject getPaymentRuleExecutionById(Integer id) throws Exception {
        List<PaymentRuleExecutionsObject> objects = getObjectsFromDB(DbName.PAYMENT_GATE, PAYMENT_GATEWAY_PAYMENT_RULE_EXECUTIONS_TABLE, "id='%s'".replace("%s", id.toString()), PaymentRuleExecutionsObject.class);
        System.out.println(objects);
        if (objects.isEmpty()) {
            return null;
        } else {
            return objects.get(0);
        }
    }

    public static PaymentEventsObject getPaymentEventByUcid(String ucid) throws Exception {
        List<PaymentEventsObject> objects = getObjectsFromDB(DbName.PAYMENT_GATE, PAYMENT_GATEWAY_PAYMENT_EVENTS_TABLE, "ucid='%s'".replace("%s", ucid), PaymentEventsObject.class);
        System.out.println(objects);
        if (objects.isEmpty()) {
            return null;
        } else {
            return objects.get(0);
        }
    }

    public static PaymentEventsObject getFailedPaymentEventByCrmId(String crmId) throws Exception {
        List<PaymentEventsObject> objects = getObjectsFromDB(DbName.PAYMENT_GATE, PAYMENT_GATEWAY_PAYMENT_EVENTS_TABLE, "crm_id='%s' AND delivery_status='FAILED'".replace("%s", crmId), PaymentEventsObject.class);
        System.out.println(objects);
        if (objects.isEmpty()) {
            return null;
        } else {
            return objects.get(0);
        }
    }

    public static PaymentDetailsObject getPaymentDetailsByClientId(Integer clientId) throws Exception {
        List<PaymentDetailsObject> objects = getObjectsFromDB(DbName.PAYMENT_GATE, PAYMENT_GATEWAY_PAYMENT_DETAILS_TABLE, "client_id='%s'".replace("%s", clientId.toString()), PaymentDetailsObject.class);
        System.out.println(objects);
        if (objects.isEmpty()) {
            return null;
        } else {
            return objects.get(0);
        }
    }

    public static List<PaymentDecisionsObject> getPaymentDecisionsByPaymentId(UUID paymentId) throws Exception {
        List<PaymentDecisionsObject> objects = getObjectsFromDB(DbName.PAYMENT_GATE, PAYMENT_GATEWAY_PAYMENT_DECISIONS_TABLE, "payment_id='%s'".replace("%s", paymentId.toString()), PaymentDecisionsObject.class);
        System.out.println(objects);
        if (objects.isEmpty()) {
            return null;
        } else {
            return objects;
        }
    }
}
