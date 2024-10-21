package helpers.kafka;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

public class AlertMessage extends JSONObject {

    JSONObject ruleDataJSON = new JSONObject();

    JSONObject triggerDataJSON = new JSONObject();

    JSONObject ruleJSON = new JSONObject();

    JSONObject triggerJSON = new JSONObject();

    JSONObject alertJSON = new JSONObject();

    @Deprecated
    List<JSONObject> rulesList = new ArrayList<>();

    public AlertMessage() {
        JSONObject alertMessage = this.alertJSON;
        JSONObject ruleData = this.ruleDataJSON;
        JSONObject rule = this.ruleJSON;
        List<JSONObject> rulesList = this.rulesList;
        JSONObject trigger = this.triggerJSON;
        JSONObject triggerData = this.triggerDataJSON;

        triggerData.put("field1", "value1").put("field2", "value2").put("field3", "value3");

        trigger.put("type", "Registration").put("timestamp", "2024-08-22T14:30:00Z").put("account_id", "003093").put(
                "trigger_data", triggerData);

        ruleData.put("field1", "value1").put("field2", "value2").put("field3", "value3");

        rule.put("rule_id", "R001").put("rule_ver", "01").put("rule_name", "Check for High Value Transactions").put(
                "rule_data", ruleData);

        alertMessage.put("alert_id", "3f6b0b35-d5c5-4d78-9a55-07ec4b6b9f89").put("timestamp", "2024-08-22T13:45:56Z").put("un_cl_id", "efs3b0b35-d5c5-4d78-9a55-07ec4b6dw32d").put("rules", rule).put("trigger", trigger).put("amount_cur", 55_555_000.00).put("currency", "USD");
    }

    // Getters

    public JSONObject getAlertMessage() {
        return this.alertJSON;
    }

    public JSONObject getRuleDataJSON() {
        return this.ruleDataJSON;
    }

    public JSONObject getTriggerDataJSON() {
        return this.triggerDataJSON;
    }

    public JSONObject getRuleJSON() {
        return this.ruleJSON;
    }

    public JSONObject getTriggerJSON() {
        return this.triggerJSON;
    }

    public JSONObject getAlertJSON() {
        return this.alertJSON;
    }

    // Setters

    public void setRuleDataJSON(String key, String value) {
        this.ruleDataJSON.put(key, value);
    }

    public void setTriggerDataJSON(String key, String value) {
        this.triggerDataJSON.put(key, value);
    }

    public void setRuleJSON(String key, String value) {
        this.ruleJSON.put(key, value);
    }

    public void setTriggerJSON(String key, String value) {
        this.triggerJSON.put(key, value);
    }

    public void setTriggerJSON(String key, JSONObject value) {
        this.triggerJSON.put(key, value);
    }

    public void setAlertJSON(String key, String value) {
        this.alertJSON.put(key, value);
    }

    public void setAlertJSON(String key, JSONObject value) {
        this.alertJSON.put(key, value);
    }

    @Deprecated
    public void setRulesList(int index, JSONObject value) {
        this.rulesList.add(index, value);
    }

    @Deprecated
    public void setRulesList(JSONObject value) {
        this.rulesList.add(value);
    }

    // Cleaners

    public void clearRuleDataJSON() {
        this.ruleDataJSON.clear();
    }

    public void clearTriggerDataJSON() {
        this.triggerDataJSON.clear();
    }

    public void clearRuleJSON() {
        this.ruleJSON.clear();
    }

    public void clearTriggerJSON() {
        this.triggerJSON.clear();
    }

    public void clearAlertJSON() {
        this.alertJSON.clear();
    }

    @Deprecated
    public void clearRulesList() {
        this.rulesList.clear();
    }
}
