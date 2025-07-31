package business_objects.db.clickhouse.bo_alerts;

import helpers.data.ClientHelper;
import utils.Utils;

import static utils.Utils.getCurrentTimestampDbFormat;

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

}
