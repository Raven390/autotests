package business_objects.db.clickhouse.bo_alerts;

import static utils.Utils.getCurrentTimestampDbFormat;

import helpers.data.ClientHelper;
import utils.Utils;

public class BoAlertsFactory {

    public static BoAlertsObject generateAlert(ClientHelper client) {
        BoAlertsObject alert = new BoAlertsObject();
        alert.setAlertId(Utils.getRandomIntPositive());
        alert.setUcid(client.getUcid());
        alert.setBrand(client.getBrand());
        alert.setCreatedAt(getCurrentTimestampDbFormat());
        alert.setCreatedAtUtc(getCurrentTimestampDbFormat());
        alert.setResolvedAt(getCurrentTimestampDbFormat());
        alert.setResolvedAtUtc(getCurrentTimestampDbFormat());
        alert.setStatus("STATUS");
        alert.setRule("RULE");
        alert.setAlertResolution("RESOLUTION");
        alert.setLastUpdated(getCurrentTimestampDbFormat());

        return alert;
    }

    public static BoAlertsObject generateAlertCustomAttributes(ClientHelper client, String ruleAttributes) {
        BoAlertsObject alert = new BoAlertsObject();
        alert.setId(Utils.getRandomIntPositive());
        alert.setStatus("STATUS");
        alert.setRule("RULE");
        alert.setAlertResolution("RESOLUTION");
        alert.setLastUpdated(getCurrentTimestampDbFormat());
        alert.setRuleAttributes(ruleAttributes);

        return alert;
    }
}
