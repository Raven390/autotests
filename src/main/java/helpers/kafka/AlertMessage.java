package helpers.kafka;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

public class AlertMessage extends JSONObject {

    JSONObject ruleDataJSON = new JSONObject();

    JSONObject triggerDataJSON = new JSONObject();

    JSONObject ruleJSON = new JSONObject();

    JSONObject triggerJSON = new JSONObject();

    List<JSONObject> rulesList = new ArrayList<>();

    JSONObject alertJSON = new JSONObject();

    public AlertMessage() {
        JSONObject alertMessage = this.alertJSON;
        JSONObject ruleData = this.ruleDataJSON;
        JSONObject rule = this.ruleJSON;
        List<JSONObject> rulesList = this.rulesList;
        JSONObject trigger = this.triggerJSON;
        JSONObject triggerData = this.triggerDataJSON;

        triggerData.put("field1", "value1").put("field2", "value2").put("field3", "value3");

        trigger.put("type", "Registration")
                .put("timestamp", "2024-08-22T14:30:00Z")
                .put("account_id", "003093")
                .put("trigger_data", triggerData);

        ruleData.put("field1", "value1").put("field2", "value2").put("field3", "value3");

        rule.put("rule_id", "R001")
                .put("rule_ver", "01")
                .put("rule_name", "Check for High Value Transactions")
                .put("rule_data", ruleData);

        rulesList.add(rule);

        alertMessage
                .put("alert_id", "3f6b0b35-d5c5-4d78-9a55-07ec4b6b9f89")
                .put("timestamp", "2024-08-22T13:45:56Z")
                .put("un_cl_id", "efs3b0b35-d5c5-4d78-9a55-07ec4b6dw32d")
                .put("rules", rulesList)
                .put("trigger", trigger)
                .put("amount_cur", 55_555_000.00)
                .put("currency", "USD");
    }
    // TODO need to make more getters and setters for objects

    public JSONObject getAlertMessage() {
        return this.alertJSON;
    }
}
